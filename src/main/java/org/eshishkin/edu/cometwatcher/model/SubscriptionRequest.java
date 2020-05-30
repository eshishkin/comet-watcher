package org.eshishkin.edu.cometwatcher.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SubscriptionRequest {

    @Email
    @NotEmpty
    private String email;

    @NotNull
    private String observerLatitude;

    @NotNull
    private String observerLongitude;

    @NotNull
    private String observerAltitude;

    @NotNull
    private ScheduleInterval interval;

}
