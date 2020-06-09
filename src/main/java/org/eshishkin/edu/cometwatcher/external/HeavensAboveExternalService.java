package org.eshishkin.edu.cometwatcher.external;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

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
}
