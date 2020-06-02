package org.eshishkin.edu.cometwatcher.repository;

import io.quarkus.arc.profile.IfBuildProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.ApplicationScoped;
import org.eshishkin.edu.cometwatcher.model.Subscription;

@ApplicationScoped
@IfBuildProfile("local")
public class InMemorySubscriberRepository implements SubscriberRepository {

    private final Map<String, Subscription> subscriptions = new ConcurrentHashMap<>();

    @Override
    public List<Subscription> findAll() {
        return new ArrayList<>(subscriptions.values());
    }

    @Override
    public Subscription get(String email) {
        return subscriptions.get(email);
    }

    @Override
    public void create(Subscription subscription) {
        subscriptions.put(subscription.getEmail(), subscription);
    }

    @Override
    public void delete(String email) {
        subscriptions.remove(email);
    }
}
