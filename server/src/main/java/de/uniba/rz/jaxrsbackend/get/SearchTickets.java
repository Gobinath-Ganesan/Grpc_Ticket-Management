package de.uniba.rz.jaxrsbackend.get;

import de.uniba.rz.entities.Ticket;
import de.uniba.rz.jaxrsbackend.stubs.TicketService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Path("Ticket/Search")
public class SearchTickets {

    private static final Logger logger = Logger.getLogger("Get ticket by id");

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchTicket(){
        logger.info("Inside serach backend");
        List<Ticket> searchTickets = new ArrayList<>();
        searchTickets = TicketService.searchTicket("Gopi");

        return Response.ok().build();
    }
}
