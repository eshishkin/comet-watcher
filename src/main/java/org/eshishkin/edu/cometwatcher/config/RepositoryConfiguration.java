package org.eshishkin.edu.cometwatcher.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eshishkin.edu.cometwatcher.external.JsoupClient;
import org.eshishkin.edu.cometwatcher.external.JsoupClientImpl;
import org.eshishkin.edu.cometwatcher.repository.CometExternalRepository;
import org.eshishkin.edu.cometwatcher.repository.HeavensAboveCometRepository;

@Dependent
public class RepositoryConfiguration {

    @ConfigProperty(name = "application.external.url.heavens-above")
    String heavensAboveBaseUrl;

    @Produces
    @ApplicationScoped
    public CometExternalRepository cometExternalRepository(JsoupClient jsoupClient) {
        return new HeavensAboveCometRepository(heavensAboveBaseUrl, jsoupClient);
    }

    @Produces
    @ApplicationScoped
    public JsoupClient jsoupClient() {
        return new JsoupClientImpl();
    }
}
