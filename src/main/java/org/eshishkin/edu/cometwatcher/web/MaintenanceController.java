package org.eshishkin.edu.cometwatcher.web;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.eshishkin.edu.cometwatcher.exception.InvalidDataException;
import org.eshishkin.edu.cometwatcher.model.SubscriptionRequest;
import org.eshishkin.edu.cometwatcher.service.ScheduledNotifier;
import org.eshishkin.edu.cometwatcher.service.SubscriberService;

import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
@Path("/maintenance")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaintenanceController {

    private final ScheduledNotifier notifier;
    private final SubscriberService subscriberService;

    @POST
    @Path("/run")
    public Response run() {
        notifier.send();
        return Response.ok().build();
    }

    @POST
    @Path("/send")
    public Response send(@QueryParam("email") String email) {
        if (StringUtils.isBlank(email)) {
            throw new InvalidDataException("Empty email");
        }

        notifier.send(email);
        return Response.ok().build();
    }

    @GET
    @Path("/subscribers")
    public Response subscribers() {
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/subscribers")
    public Response subscribe(@Valid SubscriptionRequest request) {
        subscriberService.subscribe(request);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/subscribers")
    public Response subscribe(@QueryParam("email") String email) {
        if (StringUtils.isBlank(email)) {
            throw new InvalidDataException("Email is not specified");
        }

        subscriberService.unsubscribe(email);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
