package org.eshishkin.edu.cometwatcher.model;

import java.time.ZoneId;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeoRequest {

    private String latitude;
    private String longitude;
    private int altitude;
    private ZoneId zone;

    public static GeoRequest asNullIsland() {
        return of("0", "0", 0, ZoneId.of("UTC"));
    }

    public static GeoRequest of(String latitude, String longitude, int altitude, ZoneId zone) {
        return new GeoRequest(latitude, longitude, altitude, zone);
    }
}
