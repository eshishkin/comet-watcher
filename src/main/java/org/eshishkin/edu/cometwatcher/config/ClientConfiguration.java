package org.eshishkin.edu.cometwatcher.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eshishkin.edu.cometwatcher.external.HeavensAboveExternalService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Dependent
public class ClientConfiguration {

    @ConfigProperty(name = "application.external.url.heavens-above")
    String heavensAboveBaseUrl;

    @Produces
    @ApplicationScoped
    public HeavensAboveExternalService heavensAboveExternalService() throws MalformedURLException {
        return getDefaultBuilder()
                .baseUrl(new URL(heavensAboveBaseUrl))
                .build(HeavensAboveExternalService.class);
    }

    private RestClientBuilder getDefaultBuilder() {
        return RestClientBuilder.newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.MINUTES);
    }
}
