package org.eshishkin.edu.cometwatcher.repository;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;

import io.quarkus.arc.profile.UnlessBuildProfile;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eshishkin.edu.cometwatcher.model.Subscription;

@ApplicationScoped
@UnlessBuildProfile("local")
public class ExternalSubscriberRepository implements SubscriberRepository {

    @ConfigProperty(name = "{application.datasource.subscribers.url}")
    String url;

    @ConfigProperty(name = "{application.datasource.subscribers.user}")
    String username;

    @ConfigProperty(name = "{application.datasource.subscribers.password}")
    String password;

    @Override
    public List<Subscription> findAll() {
        return new ArrayList<>();
    }

    @Override
    public Subscription get(String email) {
        return new Subscription();
    }

    @Override
    public void create(Subscription subscription) {
    }

    @Override
    public void delete(String email) {

    }
}
