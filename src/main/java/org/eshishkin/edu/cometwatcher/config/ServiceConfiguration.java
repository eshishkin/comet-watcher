package org.eshishkin.edu.cometwatcher.config;

import java.time.Clock;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class ServiceConfiguration {

    @Produces
    @ApplicationScoped
    public Clock clock() {
        return Clock.systemUTC();
    }
}
