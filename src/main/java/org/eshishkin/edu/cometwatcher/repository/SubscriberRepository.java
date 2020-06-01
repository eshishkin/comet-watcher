package org.eshishkin.edu.cometwatcher.repository;

import java.util.List;
import org.eshishkin.edu.cometwatcher.model.Subscription;

public interface SubscriberRepository {

    List<Subscription> findAll();

    Subscription get(String email);

    void create(Subscription subscription);

    void delete(String email);
}
