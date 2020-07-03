package org.eshishkin.edu.cometwatcher.web;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.eshishkin.edu.cometwatcher.exception.InvalidDataException;
import org.eshishkin.edu.cometwatcher.model.Roles;
import org.eshishkin.edu.cometwatcher.service.ScheduledNotifier;

import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
@Path("/maintenance")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaintenanceController {

    private final ScheduledNotifier notifier;

    @POST
    @Path("/run")
    @RolesAllowed(Roles.ADMIN)
    public Response run() {
        notifier.send();
        return Response.ok().build();
    }

    @POST
    @Path("/send")
    @RolesAllowed(Roles.ADMIN)
    public Response send(@QueryParam("email") String email) {
        if (StringUtils.isBlank(email)) {
            throw new InvalidDataException("Empty email");
        }

        notifier.send(email);
        return Response.ok().build();
    }
}
