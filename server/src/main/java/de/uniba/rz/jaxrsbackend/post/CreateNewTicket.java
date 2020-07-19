package de.uniba.rz.jaxrsbackend.post;

import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketDTO;
import de.uniba.rz.jaxrsbackend.stubs.TicketService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.logging.Logger;

@Path("Ticket")
public class CreateNewTicket {
    private static final Logger logger = Logger.getLogger("Inside create new Ticket");
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTicket(TicketDTO ticketDTO, @Context UriInfo uriinfo){

        logger.info("Inside create new Ticket");

        Ticket ticketWithID = TicketService.createNewTicket(ticketDTO.convertTicketDTOToTicket());

        //GenericEntity<List<Ticket>> list = new GenericEntity<List<Ticket>>(bots.addBottle(bot)) {};

        if (ticketWithID == null) {
            throw new WebApplicationException("Invalid request body" , 400);
        }

        String newId = String.valueOf(ticketWithID.getId());
        URI uri = uriinfo.getAbsolutePathBuilder().path(newId).build();
        return Response.created(uri)
                .entity(new TicketDTO(ticketWithID))
                .build();

    }
}
