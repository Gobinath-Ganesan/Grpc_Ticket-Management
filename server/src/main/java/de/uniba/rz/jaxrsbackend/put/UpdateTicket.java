package de.uniba.rz.jaxrsbackend.put;

import de.uniba.rz.backend.UnknownTicketException;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketDTO;
import de.uniba.rz.jaxrsbackend.stubs.TicketService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("Ticket")
public class UpdateTicket {
    private static final Logger logger = Logger.getLogger("Inside update Ticket");

    @PUT
    @Path("/{ticket-id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTicket(@PathParam("ticket-id") int id, TicketDTO updateTicket) throws UnknownTicketException {

        logger.info("Inside update Ticket");

        Ticket updatedTicket = TicketService.updateTicket(id,(updateTicket.convertTicketDTOToTicket().getStatus()));

        if (updatedTicket == null) {
            throw new WebApplicationException("No ticket found with id: " + id, 404);
        }

        if (updateTicket == null) {
            throw new WebApplicationException("Invalid request body", 400);
        }
        boolean isTicketUpdated = updatedTicket.getStatus() == updateTicket.getStatus()? true : false;
        if(isTicketUpdated)
            return Response.status(Response.Status.NO_CONTENT).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();

    }

}
