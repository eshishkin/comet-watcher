package org.eshishkin.edu.cometwatcher.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailMessage;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ApplicationScoped
public class MailService {

    @ConfigProperty(name = "application.mailer.from")
    String from;

    @Inject
    MailClient client;

    public void send(String recipient, String subject, String html) {
        MailMessage msg = new MailMessage();
        msg.setFrom(from);
        msg.setTo(recipient);
        msg.setHtml(html);
        msg.setSubject(subject);

        client.sendMail(msg, event -> {
            if (event.failed()) {
                log.error("Unable to send email to {} because of an error", recipient, event.cause());
            }

            if (event.succeeded()) {
                log.debug("Email has been successfully sent to {}", recipient);
            }
        });

    }
}
