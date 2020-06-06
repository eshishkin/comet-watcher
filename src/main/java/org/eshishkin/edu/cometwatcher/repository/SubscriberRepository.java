package org.eshishkin.edu.cometwatcher.repository;

import java.util.List;
import java.util.Optional;

import org.eshishkin.edu.cometwatcher.model.Subscription;

public interface SubscriberRepository {

    List<Subscription> findAll();

    Optional<Subscription> get(String id);

    void create(Subscription subscription);

    void update(Subscription subscription);

    void delete(String id);
}
