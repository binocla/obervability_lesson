package com.example.tracing;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Path("trace")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MainResource {
    private static final Logger LOG = Logger.getLogger(MainResource.class);

    @GET
    @Blocking
    public String trace(@QueryParam("query") String query) {
        LOG.info("Upcoming request with " + query);

        return "Test trace: " + query;
    }


    @POST
    @Path("/add")
    public Uni<Response> create(Item i) {
        if (i == null || i.id == null) {
            throw new WebApplicationException("Id was invalid", 422);
        }
        return Panache.withTransaction(i::persist)
                .replaceWith(Response.ok(i).status(Response.Status.CREATED)::build);
    }

    @GET
    @Path("/generate")
    public Uni<Response> add() {
        Item i = new Item(UUID.randomUUID().toString());
        return Panache.withTransaction(i::persist)
                .replaceWith(Response.ok(i).status(Response.Status.CREATED)::build);
    }

    @GET
    @Path("/list")
    @Counted(name = "performedGets",
            description = "How many times this endpoint was invoked")
    @Timed(name = "invokeTimer",
            description = "Measurement of performance",
            unit = MetricUnits.MILLISECONDS)

    public Uni<List<Item>> get() {
        LOG.info("Reactive listAll");
        LOG.info("Largest number which was generated: " + generator());
        return Item.listAll();
    }

    @Gauge(name = "largestNumber",
            unit = MetricUnits.NONE,
            description = "The largest number which was randomly generated")
    Integer generator() {
        return new Random().nextInt();
    }
}
