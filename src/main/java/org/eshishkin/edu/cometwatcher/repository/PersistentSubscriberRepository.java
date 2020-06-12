package org.eshishkin.edu.cometwatcher.repository;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import org.bson.codecs.pojo.annotations.BsonId;
import org.eshishkin.edu.cometwatcher.model.ScheduleInterval;
import org.eshishkin.edu.cometwatcher.model.Subscription;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

import io.quarkus.arc.profile.UnlessBuildProfile;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
@AllArgsConstructor
@UnlessBuildProfile("local")
public class PersistentSubscriberRepository implements SubscriberRepository {

    private static final String SUBSCRIBER_COLLECTION = "subscriber";
    private static final String ID = "_id";
    private static final String DATABASE = "comet_watcher_db";

    private final MongoClient client;

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
        getCollection().insertOne(toExternalModel(subscription));
    }

    @Override
    public void update(Subscription subscription) {
        getCollection().findOneAndReplace(eq(ID, subscription.getEmail()), toExternalModel(subscription));
    }

    @Override
    public void delete(String id) {
        getCollection().deleteOne(eq(ID, id));
    }

    private MongoCollection<SubscriptionRecord> getCollection() {
        return client.getDatabase(DATABASE).getCollection(SUBSCRIBER_COLLECTION, SubscriptionRecord.class);
    }

    private Subscription toBusinessModel(SubscriptionRecord external) {
        Subscription subscription = new Subscription();
        subscription.setEmail(external.getEmail());
        subscription.setInterval(external.getInterval());
        subscription.setObserverAltitude(external.getObserverAltitude());
        subscription.setObserverLatitude(external.getObserverLatitude());
        subscription.setObserverLongitude(external.getObserverLongitude());
        subscription.setObserverTimeZone(ZoneId.of(external.getObserverTimeZone()));
        subscription.setLastSentOn(external.getLastSentOn());
        return subscription;
    }

    private SubscriptionRecord toExternalModel(Subscription subscription) {
        SubscriptionRecord external = new SubscriptionRecord();
        external.setEmail(subscription.getEmail());
        external.setInterval(subscription.getInterval());
        external.setObserverAltitude(subscription.getObserverAltitude());
        external.setObserverLatitude(subscription.getObserverLatitude());
        external.setObserverLongitude(subscription.getObserverLongitude());
        external.setObserverTimeZone(subscription.getObserverTimeZone().getId());
        external.setLastSentOn(subscription.getLastSentOn());
        return external;
    }

    @Data
    public static class SubscriptionRecord {

        @BsonId
        private String email;
        private String observerLatitude;
        private String observerLongitude;
        private int observerAltitude;
        private String observerTimeZone;
        private ScheduleInterval interval;
        private Instant lastSentOn;
    }
}
