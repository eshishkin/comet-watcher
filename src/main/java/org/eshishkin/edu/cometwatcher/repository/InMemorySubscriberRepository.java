package org.eshishkin.edu.cometwatcher.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;

import org.eshishkin.edu.cometwatcher.model.Subscription;

import io.quarkus.arc.profile.IfBuildProfile;

@ApplicationScoped
@IfBuildProfile("local")
public class InMemorySubscriberRepository implements SubscriberRepository {

    private final Map<String, Subscription> subscriptions = new ConcurrentHashMap<>();

    @Override
    public List<Subscription> findAll() {
        return new ArrayList<>(subscriptions.values());
    }

    @Override
    public Optional<Subscription> get(String id) {
        return Optional.ofNullable(subscriptions.get(id));
    }

    @Override
    public void create(Subscription subscription) {
        subscriptions.put(subscription.getEmail(), subscription);
    }

    @Override
    public void update(Subscription subscription) {
        subscriptions.put(subscription.getEmail(), subscription);
    }

    @Override
    public void delete(String id) {
        subscriptions.remove(id);
    }
}
