package org.eshishkin.edu.cometwatcher;

import java.util.Collections;
import java.util.Map;

import static java.lang.String.format;

import org.testcontainers.vault.VaultContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VaultTestResource implements QuarkusTestResourceLifecycleManager {
    private static final int VAULT_PORT = 8200;
    private static final String VAULT_TEST_TOKEN = "vault-test-token";
    private static final String VAULT_IMAGE = "vault:1.4.2";

    private static final String VAULT_SECRET_PATH = "secret/applications/comet-watcher/config";

    private static final String ENV_VAULT_URL = "env.vault.url";
    private static final String ENV_VAULT_TOKEN = "env.vault.token";


    private VaultContainer<?> vault;

    @Override
    public Map<String, String> start() {
        vault = run();

        String url = format("http://%s:%s", vault.getHost(), vault.getMappedPort(VAULT_PORT));

        log.info("Vault has been started at {}", url);

        System.setProperty(ENV_VAULT_URL, url);
        System.setProperty(ENV_VAULT_TOKEN, VAULT_TEST_TOKEN);

        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        log.info("Stopping Vault");

        System.clearProperty(ENV_VAULT_URL);
        System.clearProperty(ENV_VAULT_TOKEN);

        vault.stop();
        log.info("Vault is successfully stopped");
    }


    private VaultContainer<?> run() {
        VaultContainer<?> container;

        String mongoUrl = format(
                "datasource.mongo_url=mongodb://test_user:test_password@localhost:%s/test_db",
                System.getProperty(MongoTestResource.ENV_MONGO_PORT)
        );

        String[] otherSecrets = {
                "datasource.database=test_db",
                "smtp.user=user",
                "smtp.password=password",
                "application.encryption_key=0123456789abcdef"
        };

        try(VaultContainer<?> ctr = new VaultContainer<>(VAULT_IMAGE)) {
            container = ctr
                    .withExposedPorts(VAULT_PORT)
                    .withVaultToken(VAULT_TEST_TOKEN)
                    .withSecretInVault(VAULT_SECRET_PATH, mongoUrl, otherSecrets);

        }

        container.start();

        return container;
    }


    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
