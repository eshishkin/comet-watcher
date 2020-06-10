package org.eshishkin.edu.cometwatcher.model;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

@Data
public class Comet {

    private String name;

    private float brightness;

    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDate observed;

    private Float altitude;

    private Long azimuth;

    private String direction;

    private String constellation;

    private String distanceFromSun;

    private String distanceFromEarth;

    private String perihelion;

    private String aphelion;

    private String period;

    private String eccentricity;

    private String rigthAccession;

    private String declination;

}
