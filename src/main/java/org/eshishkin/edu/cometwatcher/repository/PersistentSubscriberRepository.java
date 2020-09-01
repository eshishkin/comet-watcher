package org.eshishkin.edu.cometwatcher.repository;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.codecs.pojo.annotations.BsonId;
import org.eshishkin.edu.cometwatcher.exception.ResourceAlreadyExistsException;
import org.eshishkin.edu.cometwatcher.exception.ResourceNotFoundException;
import org.eshishkin.edu.cometwatcher.model.Language;
import org.eshishkin.edu.cometwatcher.model.ScheduleInterval;
import org.eshishkin.edu.cometwatcher.model.Subscription;
import org.eshishkin.edu.cometwatcher.service.VaultRepository;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

import io.quarkus.arc.profile.UnlessBuildProfile;
import lombok.Data;

import static com.mongodb.ErrorCategory.DUPLICATE_KEY;
import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
@UnlessBuildProfile("local")
public class PersistentSubscriberRepository implements SubscriberRepository {

    private static final String SUBSCRIBER_COLLECTION = "subscriber";
    private static final String ID = "_id";

    private String database;

    @Inject
    MongoClient client;

    @Inject
    VaultRepository vault;

    @PostConstruct
    void init() {
        database = vault.readSingleConfigValue("datasource.database").orElseThrow(
                () -> new IllegalStateException("Database name is not found in Vault")
        );
    }

    @Override
    public List<Subscription> findAll() {
        return getCollection()
                .find()
                .map(this::toBusinessModel)
                .into(new ArrayList<>());
    }


    @Override
    public Optional<Subscription> get(String id) {
        Subscription doc = getCollection()
                .find(eq(ID, id))
                .map(this::toBusinessModel)
                .first();

        return Optional.ofNullable(doc);
    }

    @Override
    public void create(Subscription subscription) {
        try {
            getCollection().insertOne(toExternalModel(subscription));
        } catch (MongoWriteException ex) {
            if (ex.getError().getCategory() != DUPLICATE_KEY) {
                throw ex;
            }

            throw new ResourceAlreadyExistsException("Subscription already exists: " + subscription.getEmail());
        }

    }

    @Override
    public void update(Subscription subscription) {
        getCollection().findOneAndReplace(eq(ID, subscription.getEmail()), toExternalModel(subscription));
    }

    @Override
    public void delete(String id) {
        SubscriptionRecord deleted = getCollection().findOneAndDelete(eq(ID, id));
        if (deleted == null) {
            throw new ResourceNotFoundException("Subscription not found: " + id);
        }
    }

    private MongoCollection<SubscriptionRecord> getCollection() {
        return client.getDatabase(database).getCollection(SUBSCRIBER_COLLECTION, SubscriptionRecord.class);
    }

    private Subscription toBusinessModel(SubscriptionRecord external) {
        Subscription subscription = new Subscription();
        subscription.setEmail(external.getEmail());
        subscription.setDesiredStarMagnitude(external.getDesiredStarMagnitude());
        subscription.setInterval(external.getInterval());
        subscription.setObserverAltitude(external.getObserverAltitude());
        subscription.setObserverLatitude(external.getObserverLatitude());
        subscription.setObserverLongitude(external.getObserverLongitude());
        subscription.setObserverTimeZone(ZoneId.of(external.getObserverTimeZone()));
        subscription.setLastSentOn(external.getLastSentOn());
        subscription.setLanguage(external.getLanguage());
        return subscription;
    }

    private SubscriptionRecord toExternalModel(Subscription subscription) {
        SubscriptionRecord external = new SubscriptionRecord();
        external.setEmail(subscription.getEmail());
        external.setDesiredStarMagnitude(subscription.getDesiredStarMagnitude());
        external.setInterval(subscription.getInterval());
        external.setObserverAltitude(subscription.getObserverAltitude());
        external.setObserverLatitude(subscription.getObserverLatitude());
        external.setObserverLongitude(subscription.getObserverLongitude());
        external.setObserverTimeZone(subscription.getObserverTimeZone().getId());
        external.setLastSentOn(subscription.getLastSentOn());
        external.setLanguage(subscription.getLanguage());
        return external;
    }

    @Data
    public static class SubscriptionRecord {

        @BsonId
        private String email;
        private Float desiredStarMagnitude;
        private String observerLatitude;
        private String observerLongitude;
        private int observerAltitude;
        private String observerTimeZone;
        private ScheduleInterval interval;
        private Instant lastSentOn;
        private Language language;
    }
}
