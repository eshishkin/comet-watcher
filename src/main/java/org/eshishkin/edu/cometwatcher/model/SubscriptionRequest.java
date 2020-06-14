package org.eshishkin.edu.cometwatcher.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SubscriptionRequest {

    @Email
    @NotEmpty
    private String email;

    @NotNull
    private String observerLatitude;

    @NotNull
    private String observerLongitude;

    @Max(90)
    @Min(-90)
    private int observerAltitude;

    @NotNull
    private ScheduleInterval interval;

    private Float desiredStarMagnitude;
}
