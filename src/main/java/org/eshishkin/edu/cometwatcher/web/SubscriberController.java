package org.eshishkin.edu.cometwatcher.web;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eshishkin.edu.cometwatcher.model.Subscription;
import org.eshishkin.edu.cometwatcher.model.SubscriptionRequest;
import org.eshishkin.edu.cometwatcher.service.SubscriberService;

import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
@Path("/subscribers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SubscriberController {

    private final SubscriberService subscriberService;

    @GET
    public Response subscribers() {
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{email}")
    public Subscription get(@PathParam("email") String email) {
        return subscriberService.findByEmail(email).orElse(null);
    }


    @POST
    public Response subscribe(@Valid SubscriptionRequest request) {
        subscriberService.subscribe(request);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{email}")
    public Response unsubscribe(@PathParam("email") String email) {
        subscriberService.unsubscribe(email);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Deprecated
    @Path("/token/{token}")
    public Response unsubscribeViaToken(@PathParam("token") String token) {
        subscriberService.unsubscribeViaToken(token);
        return Response.status(Response.Status.OK).build();
    }
}
