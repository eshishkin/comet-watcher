package org.eshishkin.edu.cometwatcher.service;

import io.quarkus.mailer.MailTemplate;
import io.quarkus.qute.api.ResourcePath;
import io.quarkus.scheduler.Scheduled;
import java.time.Instant;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.model.Subscription;

@Slf4j
@ApplicationScoped
public class ScheduledNotifier {

    @Inject
    SubscriberService subscriberService;

    @Inject
    CometService cometService;

    @ResourcePath("comets/comet-report")
    MailTemplate sender;

    @ConfigProperty(name = "application.mail.templates.comet-report.subject")
    String subject;

    @Scheduled(cron = "{application.schedulers.comet-notifier}")
    public void send() {
        List<Comet> comets = cometService.getComets();

        subscriberService.getSubscriptions()
                .stream()
                .map(Subscription::getEmail)
                .forEach(email -> send(email, comets));
    }

    private void send(String recipient, List<Comet> comets) {
        sender
                .to(recipient)
                .subject(subject)
                .data("comets", comets)
                .data("generated_at", Instant.now())
                .send()
                .toCompletableFuture()
                .handle((r, ex) -> {
                    if (ex != null) {
                        log.error("Unable to send email due to exception", ex);
                    } else {
                        log.info("Report has been successfully sent to {}", recipient);
                    }
                    return null;
                })
                .join();
    }
}
