package de.uniba.rz.jaxrsbackend.get;

import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketDTO;
import de.uniba.rz.jaxrsbackend.stubs.TicketService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Path("Ticket")
public class GetAllTickets {
    private static final Logger logger = Logger.getLogger("Get ticket by id");

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTickets(@Context UriInfo uriinfo){
        logger.info("Inside get all Tickets");
        List<TicketDTO> allTicketsDTO=new ArrayList<>();
        List<Ticket> allTickets = TicketService.getAllTickets();
        logger.info("Total Tickets "+allTickets.size());
        for (Ticket ticket:allTickets) {
            allTicketsDTO.add(new TicketDTO(ticket));
        }
        GenericEntity<List<TicketDTO>> list = new GenericEntity<List<TicketDTO>>(allTicketsDTO){};
        logger.info("Total Tickets in dto "+allTicketsDTO.size());
        return Response.ok()
                .entity(list)
                .build();
    }
}
