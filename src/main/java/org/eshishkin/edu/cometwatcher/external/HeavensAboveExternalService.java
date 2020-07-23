package org.eshishkin.edu.cometwatcher.external;


import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import lombok.Builder;
import lombok.Getter;

public interface HeavensAboveExternalService {

    @GET
    @Path("/Comets.aspx")
    @Produces("application/html")
    String getComets(@QueryParam("lat") String latitude, @QueryParam("lng") String longitude,
                     @QueryParam("alt") int altitude, @QueryParam("tz") String timezone
    );

    @GET
    @Path("/comet.aspx")
    @Produces("text/html")
    String getComet(@QueryParam("cid") String name,
                    @QueryParam("lat") String latitude, @QueryParam("lng") String longitude,
                    @QueryParam("alt") int altitude, @QueryParam("tz") String timezone
    );

    @POST
    @Path("/comet.aspx")
    @Produces("text/html")
    @Consumes("application/x-www-form-urlencoded")
    String getComet(@BeanParam CometRequest request);

    @Getter
    @Builder
    class CometRequest {
        @QueryParam("cid") private String name;
        @QueryParam("lat") private String latitude;
        @QueryParam("lng") private String longitude;
        @QueryParam("alt") private int altitude;

        @FormParam("ctl00$cph1$TimeSelectionControl1$comboYear") private int year;
        @FormParam("ctl00$cph1$TimeSelectionControl1$comboMonth") private int month;
        @FormParam("ctl00$cph1$TimeSelectionControl1$comboDay") private int day;
        @FormParam("ctl00$cph1$TimeSelectionControl1$txtTime") private String time;

        @FormParam("__VIEWSTATE") private String aspViewState;
    }
}
