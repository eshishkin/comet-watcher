package org.eshishkin.edu.cometwatcher.external.subscription.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.ZoneId;
import lombok.Data;
import org.eshishkin.edu.cometwatcher.model.ScheduleInterval;

@Data
public class SubscriptionRecord {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("_rev")
    private String rev;
    private String email;
    private String observerLatitude;
    private String observerLongitude;
    private int observerAltitude;
    private ZoneId observerTimeZone;
    private ScheduleInterval interval;
    private Instant lastSentOn;


}