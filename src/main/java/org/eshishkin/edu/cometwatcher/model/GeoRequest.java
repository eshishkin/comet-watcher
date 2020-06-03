package org.eshishkin.edu.cometwatcher.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeoRequest {

    // todo make it big decimal and non nullable
    private String latitude;
    private String longitude;
    private int altitude;

    public static GeoRequest asNullIsland() {
        return of("0", "0", 0);
    }

    public static GeoRequest of(String latitude, String longitude, int altitude) {
        return new GeoRequest(latitude, longitude, altitude);
    }
}
