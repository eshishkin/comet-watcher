package org.eshishkin.edu.cometwatcher.web;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eshishkin.edu.cometwatcher.model.Comet;
import org.eshishkin.edu.cometwatcher.service.CometService;

import lombok.AllArgsConstructor;

@Path("/comet")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class CometController {

    private final CometService cometService;

    @GET
    @Path("/all")
    public List<Comet> get() {
        return cometService.getComets();
    }
}
