package org.eshishkin.edu.cometwatcher.service;

import java.time.Instant;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.model.Subscription;
import org.eshishkin.edu.cometwatcher.utils.EncryptionUtils;

@ApplicationScoped
public class CometReportRenderer {

    @Inject
    EncryptionUtils encryptionUtils;

    @Inject
    TemplateService templateService;

    @ConfigProperty(name = "application.mail.templates.comet-report.subject")
    String subject;

    @ConfigProperty(name = "application.url")
    String applicationUrl;

    public String getSubject() {
        return subject;
    }

    public String render(Subscription subscriber, List<Comet> comets) {
        return templateService
                .getTemplate("comets/comet-report", subscriber.getLanguage())
                .data("comets", comets)
                .data("generated_at", Instant.now())
                .data("cancel_subscription_link", renderCancellationLink(subscriber.getEmail()))
                .render();
    }

    private String renderCancellationLink(String email) {
        return applicationUrl + "/subscribers/token/" + encryptionUtils.encryptAndEncode(email);
    }
}
