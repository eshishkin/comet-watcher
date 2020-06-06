package org.eshishkin.edu.cometwatcher.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eshishkin.edu.cometwatcher.external.subscription.ExternalSubscriberClient;
import org.eshishkin.edu.cometwatcher.external.subscription.model.SearchRequest;
import org.eshishkin.edu.cometwatcher.external.subscription.model.SubscriberSelector;
import org.eshishkin.edu.cometwatcher.external.subscription.model.SubscriptionRecord;
import org.eshishkin.edu.cometwatcher.model.Subscription;

import io.quarkus.arc.profile.UnlessBuildProfile;
import lombok.AllArgsConstructor;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@ApplicationScoped
@AllArgsConstructor
@UnlessBuildProfile("local")
public class PersistentSubscriberRepository implements SubscriberRepository {

    private final ExternalSubscriberClient client;

    @Override
    public List<Subscription> findAll() {
        return client.search(SearchRequest.of(new SubscriberSelector()))
                .getDocs()
                .stream()
                .map(this::toBusinessModel)
                .collect(Collectors.toList());
    }


    @Override
    public Optional<Subscription> get(String id) {
        return getById(id).map(this::toBusinessModel);
    }

    @Override
    public void create(Subscription subscription) {
        client.create(toExternalModel(subscription));
    }

    @Override
    public void update(Subscription subscription) {
        getById(subscription.getEmail())
                .ifPresent(s -> client.update(s.getId(), s.getRev(), toExternalModel(subscription)));
    }

    @Override
    public void delete(String id) {
        getById(id).ifPresent(s -> client.delete(s.getId(), s.getRev()));
    }

    private Optional<SubscriptionRecord> getById(String id) {
        try {
            return Optional.of(client.get(id));
        } catch (WebApplicationException ex) {
            try (Response response = ex.getResponse()) {
                if (response.getStatus() == NOT_FOUND.getStatusCode()) {
                    return Optional.empty();
                } else {
                    throw ex;
                }
            }
        }
    }

    private Subscription toBusinessModel(SubscriptionRecord external) {
        Subscription subscription = new Subscription();
        subscription.setEmail(external.getEmail());
        subscription.setInterval(external.getInterval());
        subscription.setObserverAltitude(external.getObserverAltitude());
        subscription.setObserverLatitude(external.getObserverLatitude());
        subscription.setObserverLongitude(external.getObserverLongitude());
        subscription.setObserverTimeZone(external.getObserverTimeZone());
        subscription.setLastSentOn(external.getLastSentOn());
        return subscription;
    }

    private SubscriptionRecord toExternalModel(Subscription subscription) {
        SubscriptionRecord external = new SubscriptionRecord();
        external.setId(subscription.getEmail());
        external.setEmail(subscription.getEmail());
        external.setInterval(subscription.getInterval());
        external.setObserverAltitude(subscription.getObserverAltitude());
        external.setObserverLatitude(subscription.getObserverLatitude());
        external.setObserverLongitude(subscription.getObserverLongitude());
        external.setObserverTimeZone(subscription.getObserverTimeZone());
        external.setLastSentOn(subscription.getLastSentOn());
        return external;
    }
}
