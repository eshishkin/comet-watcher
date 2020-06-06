package org.eshishkin.edu.cometwatcher.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.ws.rs.client.ClientRequestFilter;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eshishkin.edu.cometwatcher.external.HeavensAboveExternalService;
import org.eshishkin.edu.cometwatcher.external.subscription.ExternalSubscriberClient;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

@Dependent
public class ClientConfiguration {

    private static final int DEFAULT_CONNECTION_TIMEOUT = 30;
    private static final int DEFAULT_READ_TIMEOUT = 2;

    @Produces
    @ApplicationScoped
    public HeavensAboveExternalService heavensAboveExternalService(
            @ConfigProperty(name = "application.external.heavens-above.url") String url) {

        return getDefaultBuilder()
                .baseUrl(toURL(url))
                .build(HeavensAboveExternalService.class);
    }

    @Produces
    @ApplicationScoped
    public ExternalSubscriberClient externalSubscriberService(
            @ConfigProperty(name = "application.external.subscribers.url") String url,
            @ConfigProperty(name = "application.external.subscribers.user") String user,
            @ConfigProperty(name = "application.external.subscribers.password") String password) {

        return getDefaultBuilder()
                .baseUrl(toURL(url))
                .register((ClientRequestFilter) context -> context.getHeaders().add(
                        "Authorization",
                        "Basic " + Base64.getEncoder().encodeToString(format("%s:%s", user, password).getBytes(UTF_8))
                ))
                .build(ExternalSubscriberClient.class);
    }

    private URL toURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Unable to create URL", ex);
        }
    }

    private RestClientBuilder getDefaultBuilder() {
        return RestClientBuilder.newBuilder()
                .connectTimeout(DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MINUTES);
    }
}
