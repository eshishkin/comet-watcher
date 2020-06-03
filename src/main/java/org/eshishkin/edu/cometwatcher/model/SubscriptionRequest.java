package org.eshishkin.edu.cometwatcher.model;

import lombok.Data;

import javax.validation.constraints.*;

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

}
