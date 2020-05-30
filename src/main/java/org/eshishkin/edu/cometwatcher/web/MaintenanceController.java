package org.eshishkin.edu.cometwatcher.web;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.eshishkin.edu.cometwatcher.ScheduledNotifier;

@ApplicationScoped
@AllArgsConstructor
@Path("/maintenance")
@Produces(MediaType.APPLICATION_JSON)
public class MaintenanceController {

    private final ScheduledNotifier notifier;

    @POST
    @Path("/notify")
    public Response send() {
        notifier.send();
        return Response.ok().build();
    }
}
