package org.eshishkin.edu.cometwatcher.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Comet {
    private String name;
    private float brightness;
    private LocalDate observed;
    private Float altitude;
    private Long azimuth;
    private String direction;
    private String constellation;
}
