package org.eshishkin.edu.cometwatcher.model;

import java.time.Instant;
import java.time.ZoneId;

import lombok.Data;

@Data
public class Subscription {
    private String email;
    private String observerLatitude;
    private String observerLongitude;
    private int observerAltitude;
    private ZoneId observerTimeZone;
    private ScheduleInterval interval;
    private Instant lastSentOn;
}
