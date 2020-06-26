package org.eshishkin.edu.cometwatcher.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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

    private String perihelionDistance;

    private String perihelionDate;

    private String aphelion;

    private String period;

    private String eccentricity;

    private String rightAccession;

    private String declination;

    private List<ImageLink> links;

    @Getter @Setter
    public static class ImageLink implements Serializable {
        private String description;
        private String link;

        public static ImageLink of(String description, String link) {
            ImageLink resource = new ImageLink();
            resource.setDescription(description);
            resource.setLink(link);
            return resource;
        }
    }
}
