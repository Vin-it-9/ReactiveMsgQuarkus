package controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Range;
import service.RangeService;

@Path("/ranges")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RangeResource {

    @Inject
    RangeService rangeManager;

    @POST
    @Path("/{userId}")
    public Response setRange(@PathParam("userId") String userId, Range config) {
        config.setUserId(userId);
        rangeManager.setRange(userId, config);
        return Response.ok(config).build();
    }

    @GET
    @Path("/{userId}")
    public Response getRange(@PathParam("userId") String userId) {
        Range config = rangeManager.getRange(userId);
        if (config == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(config).build();
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteRange(@PathParam("userId") String userId) {
        rangeManager.removeRange(userId);
        return Response.noContent().build();
    }

    @GET
    public Response getAllRanges() {
        return Response.ok(rangeManager.getAllRanges()).build();
    }
}
