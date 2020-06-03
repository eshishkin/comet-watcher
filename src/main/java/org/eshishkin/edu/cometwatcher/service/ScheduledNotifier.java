package org.eshishkin.edu.cometwatcher.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Template;
import io.quarkus.qute.api.ResourcePath;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.model.GeoRequest;
import org.eshishkin.edu.cometwatcher.model.ScheduleInterval;
import org.eshishkin.edu.cometwatcher.model.Subscription;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

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
        Instant now = Instant.now();

        Map<GeoRequest, List<Subscription>> subscriptions = subscriberService
                .getSubscriptions()
                .stream()
                .filter(s -> now.isAfter(calculateNextTryDate(s)))
                .collect(groupingBy(
                    s -> GeoRequest.of(s.getObserverLatitude(), s.getObserverLongitude(), s.getObserverAltitude())
                ));

        if (subscriptions.isEmpty()) {
            log.info("There is no active subscription");
            return;
        }

        subscriptions.entrySet().stream().forEach(e -> {
            GeoRequest geo = e.getKey();
            List<Subscription> subscribers = e.getValue();

            send(render(cometService.getComets(geo)), subscribers);
        });
    }

    private void send(String payload, List<Subscription> subscriptions) {
        subscriptions.stream().map(Subscription::getEmail).forEach(email -> {
            sender.send(Mail.withHtml(email, subject, payload));
            subscriberService.updateLastTryDate(email);
        });
    }

    private Instant calculateNextTryDate(Subscription s) {
        Instant lastTry = s.getLastSentOn();
        ScheduleInterval interval = s.getInterval();

        Instant next;
        switch (interval) {
            case DAILY:
                next = lastTry.plus(1, ChronoUnit.DAYS);
                break;
            case WEEKLY:
                next = lastTry.plus(1, ChronoUnit.WEEKS);
                break;
            case BIWEEKLY:
                next = lastTry.plus(2, ChronoUnit.WEEKS);
                break;
            case MONTHLY:
                next = lastTry.plus(1, ChronoUnit.MONTHS);
                break;
            default:
                throw new IllegalArgumentException("Unsupported interval type: " + interval);
        }

        return next;
    }

    private String render(List<Comet> comets) {
        return template
                .data("comets", comets)
                .data("generated_at", Instant.now())
                .render();
    }
}
