package org.eshishkin.edu.cometwatcher.service;

import org.eshishkin.edu.cometwatcher.model.Subscription;
import org.eshishkin.edu.cometwatcher.model.SubscriptionRequest;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class SubscriberService {

    private final Map<String, Subscription> subscriptions = new ConcurrentHashMap<>();

    public List<Subscription> getSubscriptions() {
        return new ArrayList<>(subscriptions.values());
    }

    public void subscribe(SubscriptionRequest request) {
        subscriptions.put(request.getEmail(), toSubscription(request));
    }

    public void updateLastTryDate(String email) {
        Subscription subscription = subscriptions.get(email);
        if (subscription != null) {
            subscription.setLastSentOn(Instant.now());
        }
    }

    public void unsubscribe(String email) {
        subscriptions.remove(email);
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
