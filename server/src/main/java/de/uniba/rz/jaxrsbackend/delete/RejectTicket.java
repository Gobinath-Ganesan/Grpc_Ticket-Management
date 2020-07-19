package de.uniba.rz.jaxrsbackend.delete;

import de.uniba.rz.jaxrsbackend.stubs.TicketService;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("Ticket")
public class RejectTicket {

    @DELETE
    @Path("/{ticket-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTicket(@PathParam("ticket-id") int id){

        boolean isDeleted = TicketService.deleteTicket(id);
        if(isDeleted)
            return Response.status(Response.Status.NO_CONTENT).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }
}
