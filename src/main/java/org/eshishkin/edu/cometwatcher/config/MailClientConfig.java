package org.eshishkin.edu.cometwatcher.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eshishkin.edu.cometwatcher.service.VaultRepository;

import io.vertx.core.Vertx;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;

@Dependent
public class MailClientConfig {

    @ConfigProperty(name = "application.mailer.host")
    String host;

    @ConfigProperty(name = "application.mailer.port")
    String port;

    @Inject
    VaultRepository vault;

    @Produces
    @ApplicationScoped
    public MailClient mailer(Vertx vertx) {
        MailConfig cfg = new MailConfig();

        cfg.setHostname(host);
        cfg.setPort(Integer.parseInt(port));

        vault.readSingleConfigValue("smtp.user").ifPresent(cfg::setUsername);
        vault.readSingleConfigValue("smtp.password").ifPresent(cfg::setUsername);

        return MailClient.createShared(vertx, cfg);
    }
}
