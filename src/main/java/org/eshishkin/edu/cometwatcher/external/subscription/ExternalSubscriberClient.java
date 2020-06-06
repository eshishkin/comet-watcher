package org.eshishkin.edu.cometwatcher.external.subscription;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eshishkin.edu.cometwatcher.external.subscription.model.SearchRequest;
import org.eshishkin.edu.cometwatcher.external.subscription.model.SubscriberSearchResponse;
import org.eshishkin.edu.cometwatcher.external.subscription.model.SubscriptionRecord;

@Path("/subscribers")
public interface ExternalSubscriberClient {

    @POST
    @Path("/_find")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    SubscriberSearchResponse search(SearchRequest request);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    SubscriptionRecord get(@PathParam("id") String id);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void create(SubscriptionRecord subscription);

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void update(@PathParam("id") String id, @QueryParam("rev") String revision, SubscriptionRecord subscription);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") String id, @QueryParam("rev") String revision);
}
