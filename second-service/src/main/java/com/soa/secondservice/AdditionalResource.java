package com.soa.secondservice;

import com.soa.ejb.model.domain.StudyGroup;
import com.soa.ejb.service.RemoteAdditionalService;
import com.soa.secondservice.jndi.JNDIConfig;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Path("")
public class AdditionalResource {
    private final RemoteAdditionalService additionalService = JNDIConfig.additionalService();

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }

    @POST
    @Path("/count-transferred")
    @Produces("text/plain")
    public Response countTransferred() {
        try {
            Long sum = additionalService.countTransferred();
            return Response.ok()
                    .entity(sum)
                    .build();
        } catch (NoSuchElementException e) {
            return Response.status(500).build();
        }
    }

    @POST
    @Path("/{from}/move/{to}")
    @Produces("application/json")
    public Response moveStudents(@PathParam("from") long from, @PathParam("to") long to) {
        try {
            List<StudyGroup> groups = additionalService.moveStudents(from, to);
            return Response.ok().entity(groups).build();
        } catch (RuntimeException e) {
            return Response.status(500).build();
        }
    }
}
