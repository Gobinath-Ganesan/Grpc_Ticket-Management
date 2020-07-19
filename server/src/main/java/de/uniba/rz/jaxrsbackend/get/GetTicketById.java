package de.uniba.rz.jaxrsbackend.get;

import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketDTO;
import de.uniba.rz.jaxrsbackend.stubs.TicketService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

@Path("Ticket")
public class GetTicketById {
    private static final Logger logger = Logger.getLogger("Get ticket by id");
    @GET
    @Path("/{ticket-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicketById(@Context UriInfo uriinfo, @PathParam("ticket-id") int id){
        logger.info("Inside get Ticket by id");

        Ticket ticket = TicketService.getTicketById(id);
        logger.info("Returning the ticket reported by "+ticket.getReporter());

        return Response.ok()
                .entity(new TicketDTO(ticket))
                .build();
    }

}
