package org.eshishkin.edu.cometwatcher;

import java.util.Collections;
import java.util.Map;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MongoTestResource implements QuarkusTestResourceLifecycleManager {
    private static final int MONGO_PORT = 27017;
    private static final String ADMIN = "admin";

    public static final String ENV_MONGO_PORT = "env.mongo.port";

    private GenericContainer<?> mongo;

    @Override
    public Map<String, String> start() {
        mongo = run();

        log.info("MongoDB has been started at {}:{}", mongo.getHost(), mongo.getMappedPort(MONGO_PORT));

        System.setProperty(ENV_MONGO_PORT, String.valueOf(mongo.getMappedPort(MONGO_PORT)));

        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        log.info("Stopping MongoDB");

        System.clearProperty(ENV_MONGO_PORT);

        mongo.stop();
        log.info("MongoDB is successfully stopped");
    }


    private GenericContainer<?> run() {
        GenericContainer<?> container;
        try(GenericContainer<?> ctr = new GenericContainer<>("mongo:4.2.7")) {
            container = ctr
                    .withEnv("MONGO_INITDB_ROOT_USERNAME", ADMIN)
                    .withEnv("MONGO_INITDB_ROOT_PASSWORD", ADMIN)
                    .withClasspathResourceMapping(
                            "org/eshishkin/edu/cometwatcher/mongo_startup.js",
                            "/docker-entrypoint-initdb.d/mongo_startup.js",
                            BindMode.READ_ONLY
                    )
                    .withExposedPorts(MONGO_PORT);
        }

        container.start();

        return container;
    }
}
