package org.eshishkin.edu.cometwatcher.service;

import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.vault.VaultKVSecretEngine;

@ApplicationScoped
public class VaultRepository {

    @Inject
    VaultKVSecretEngine secretEngine;

    @ConfigProperty(name = "application.vault.user-storage-path")
    String userStoragePath;

    @ConfigProperty(name = "application.vault.config-path")
    String configStoragePath;

    public Map<String, String> readUserData(String secret) {
        return readSecret(userStoragePath, secret);
    }

    public Optional<String> readSingleConfigValue(String key) {
        Map<String, String> values = secretEngine.readSecret(configStoragePath);
        return Optional.ofNullable(values.get(key));
    }

    public Map<String, String> readSecret(String path, String secret) {
        return secretEngine.readSecret(path + "/" + secret);
    }
}
