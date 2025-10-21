package controller.websocket;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.annotations.Channel;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import model.Alert;
import org.jboss.resteasy.reactive.RestStreamElementType;

@Path("/alerts")
public class AlertWebSocket {

    @Inject
    @Channel("alerts-out")
    Multi<Alert> alerts;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Multi<Alert> streamAlerts() {
        return alerts.filter(alert -> alert != null);
    }
}
