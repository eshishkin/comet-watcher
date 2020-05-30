package org.eshishkin.edu.cometwatcher.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Template;
import io.quarkus.qute.api.ResourcePath;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.model.Subscription;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;

@Slf4j
@ApplicationScoped
public class ScheduledNotifier {

    @Inject
    SubscriberService subscriberService;

    @Inject
    CometService cometService;

    @Inject
    Mailer sender;

    @ResourcePath("comets/comet-report")
    Template template;

    @ConfigProperty(name = "application.mail.templates.comet-report.subject")
    String subject;

    @Scheduled(cron = "{application.schedulers.comet-notifier}")
    public void send() {
        List<Subscription> subscriptions = subscriberService.getSubscriptions();

        if (subscriptions.isEmpty()) {
            log.info("There is no active subscription");
            return;
        }

        List<Comet> comets = cometService.getComets();

        String payload = render(comets);

        subscriptions.stream().map(Subscription::getEmail).forEach(email -> {
            sender.send(Mail.withHtml(email, subject, payload));
        });
    }

    private String render(List<Comet> comets) {
        return template
                .data("comets", comets)
                .data("generated_at", Instant.now())
                .render();
    }
}
