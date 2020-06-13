package org.eshishkin.edu.cometwatcher.service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;

import org.eshishkin.edu.cometwatcher.model.Subscription;
import org.eshishkin.edu.cometwatcher.model.SubscriptionRequest;
import org.eshishkin.edu.cometwatcher.repository.SubscriberRepository;

import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

    public List<Subscription> getSubscriptions() {
        return subscriberRepository.findAll();
    }

    public Optional<Subscription> findByEmail(String email) {
        return subscriberRepository.get(email);
    }

    public void subscribe(SubscriptionRequest request) {
        subscriberRepository.create(toSubscription(request));
    }

    public void updateLastTryDate(String email) {
        subscriberRepository.get(email).ifPresent(subscription -> {
            subscription.setLastSentOn(Instant.now());
            subscriberRepository.update(subscription);
        });
    }

    public void unsubscribe(String email) {
        subscriberRepository.delete(email);
    }

    private Subscription toSubscription(SubscriptionRequest request) {
        Subscription subscription = new Subscription();
        subscription.setEmail(request.getEmail());
        subscription.setObserverAltitude(request.getObserverAltitude());
        subscription.setObserverLatitude(request.getObserverLatitude());
        subscription.setObserverLongitude(request.getObserverLongitude());
        subscription.setObserverTimeZone(ZoneId.of("UTC"));
        subscription.setInterval(request.getInterval());
        subscription.setLastSentOn(Instant.now());
        return subscription;
    }
}
