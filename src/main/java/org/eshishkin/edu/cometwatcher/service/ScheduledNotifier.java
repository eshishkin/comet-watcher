package org.eshishkin.edu.cometwatcher.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.model.CometStub;
import org.eshishkin.edu.cometwatcher.model.GeoRequest;
import org.eshishkin.edu.cometwatcher.model.ScheduleInterval;
import org.eshishkin.edu.cometwatcher.model.Subscription;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.toList;

@Slf4j
@ApplicationScoped
public class ScheduledNotifier {

    @Inject
    SubscriberService subscriberService;

    @Inject
    CometService cometService;

    @Inject
    CometReportRenderer renderer;

    @Inject
    Mailer sender;

    public void send(String email) {
        subscriberService.findByEmail(email).ifPresent(
            s -> send(renderer.render(s, evaluateComets(s)), s)
        );
    }

    @Scheduled(cron = "{application.schedulers.comet-notifier}")
    public void send() {
        Instant now = Instant.now();

        List<Subscription> subscriptions = subscriberService
                .getSubscriptions()
                .stream()
                .filter(s -> now.isAfter(calculateNextTryDate(s)))
                .collect(toList());

        if (subscriptions.isEmpty()) {
            log.info("There is no active subscription");
            return;
        }

        subscriptions.forEach(s -> send(renderer.render(s, evaluateComets(s)), s));
    }

    private void send(String payload, Subscription subscription) {
        send(payload, Collections.singletonList(subscription));
    }

    private void send(String payload, List<Subscription> subscriptions) {
        subscriptions.stream().map(Subscription::getEmail).forEach(email -> {
            sender.send(Mail.withHtml(email, renderer.getSubject(), payload));
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

    private List<Comet> evaluateComets(Subscription s) {
        GeoRequest observer = GeoRequest.of(
                s.getObserverLatitude(), s.getObserverLongitude(), s.getObserverAltitude(), s.getObserverTimeZone()
        );

        return cometService.getComets(observer)
                .stream()
                .filter(comet ->
                    s.getDesiredStarMagnitude() == null || comet.getBrightness() <= s.getDesiredStarMagnitude()
                )
                .map(CometStub::getId)
                .map(id -> cometService.getComet(id, observer))
                .collect(toList());

    }
}
