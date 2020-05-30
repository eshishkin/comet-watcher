package org.eshishkin.edu.cometwatcher.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.ApplicationScoped;
import org.eshishkin.edu.cometwatcher.model.Subscription;

@ApplicationScoped
public class SubscriberService {

    private final Map<String, Subscription> subscriptions = new ConcurrentHashMap<>();

    public List<Subscription> getSubscriptions() {
        return new ArrayList<>(subscriptions.values());
    }

    public void subscribe(Subscription request) {
        subscriptions.put(request.getEmail(), request);
    }

    public void unsubscribe(String email) {
        subscriptions.remove(email);
    }
}
