package org.eshishkin.edu.cometwatcher.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "application.feature")
public interface FeatureConfig {

    @ConfigProperty(name = "use-midnight-for-comets")
    boolean isUseMidnightTime();
}
