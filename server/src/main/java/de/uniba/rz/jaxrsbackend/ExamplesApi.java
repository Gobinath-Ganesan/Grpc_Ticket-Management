package de.uniba.rz.jaxrsbackend;



import de.uniba.rz.jaxrsbackend.delete.RejectTicket;
import de.uniba.rz.jaxrsbackend.get.GetAllTickets;
import de.uniba.rz.jaxrsbackend.get.GetTicketById;
import de.uniba.rz.jaxrsbackend.get.SearchTickets;
import de.uniba.rz.jaxrsbackend.post.CreateNewTicket;
import de.uniba.rz.jaxrsbackend.put.UpdateTicket;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
@Consumes(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ExamplesApi extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(RejectTicket.class);
        resources.add(GetAllTickets.class);
        resources.add(GetTicketById.class);
        resources.add(UpdateTicket.class);
        resources.add(CreateNewTicket.class);
        resources.add(SearchTickets.class);
        return resources;
    }
}
