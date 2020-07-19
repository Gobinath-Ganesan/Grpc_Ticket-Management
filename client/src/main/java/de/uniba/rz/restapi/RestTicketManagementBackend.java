package de.uniba.rz.restapi;

import de.uniba.rz.Utlities.GeneralUtilitiesUdp;
import de.uniba.rz.app.TicketManagementBackend;
import de.uniba.rz.entities.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestTicketManagementBackend implements TicketManagementBackend {

    Client client = ClientBuilder.newClient();
    HashMap<Integer, Ticket> jaxrsTicketStore = new HashMap<>();
    @Override
    public Ticket createNewTicket(String reporter, String topic, String description, Type type, Priority priority) throws TicketException {

        System.out.println("Inside create new Ticket");
        Ticket newTicket = new Ticket(0,reporter,topic,description,type,priority);
        Entity<TicketDTO> newTicketToPost = Entity.json(new TicketDTO(newTicket));
        Ticket responseTicket = null;

        Response response = client.target("http://localhost:9999")
                .path("/Ticket")
                .request(MediaType.APPLICATION_JSON)
                .post(newTicketToPost);

        if (response.getStatus() == 201){
            System.out.println("Got the response "+response.getStatus());
            responseTicket = response.readEntity(TicketDTO.class).convertTicketDTOToTicket();
        }
        System.out.println("Returning the ticket "+ responseTicket.getId());
        jaxrsTicketStore.put(responseTicket.getId(), responseTicket);
        return responseTicket;
    }

    @Override
    public List<Ticket> getAllTickets() throws TicketException {
        List<Ticket> allTicketsList = new ArrayList<>();
        Response response = client.target("http://localhost:9999")
                .path("/Ticket")
                .request()
                .get();
        List<TicketDTO> ticketDTOList = null;

        if (response.getStatus() == 200){
            ticketDTOList = response.readEntity(new GenericType<List<TicketDTO>>() {});
        }
        for (TicketDTO ticket:ticketDTOList
             ) {
            allTicketsList.add(ticket.convertTicketDTOToTicket());
        }

        for (Ticket ticket : allTicketsList)
            jaxrsTicketStore.put(ticket.getId(), ticket);

        return allTicketsList;
    }

    @Override
    public Ticket getTicketById(int id) throws TicketException {
        Ticket responseTicket = null;
        Response response = client.target("http://localhost:9999")
                .path("/Ticket/").path(String.valueOf(id))
                .request()
                .get();
        if (response.getStatus() == 200){
            responseTicket = response.readEntity(TicketDTO.class).convertTicketDTOToTicket();
        }
        return responseTicket;
    }

    @Override
    public Ticket acceptTicket(int id) throws TicketException {
        Ticket acceptTicket = null;
        if (jaxrsTicketStore.containsKey(id)) {
            acceptTicket = jaxrsTicketStore.get(id);
        }

        acceptTicket.setStatus(Status.ACCEPTED);
        Entity<TicketDTO> ticketToUpdate = Entity.json(new TicketDTO(acceptTicket));
        Response response = client.target("http://localhost:9999")
                .path("/Ticket/").path(String.valueOf(id))
                .request(MediaType.APPLICATION_JSON)
                .put(ticketToUpdate);

        if (response.getStatus() == 204){
            System.out.println("Got the response "+response.getStatus());
            //responseTicket = response.readEntity(TicketDTO.class).convertTicketDTOToTicket();
        }
        System.out.println("Got the response "+acceptTicket.getStatus());
        return acceptTicket;
    }

    @Override
    public Ticket rejectTicket(int id) throws TicketException {
        Ticket rejectTicket = null;
        if (jaxrsTicketStore.containsKey(id)) {
            rejectTicket = jaxrsTicketStore.get(id);
        }

        rejectTicket.setStatus(Status.REJECTED);
        Entity<TicketDTO> ticketToReject = Entity.json(new TicketDTO(rejectTicket));
        Response response = client.target("http://localhost:9999")
                .path("/Ticket/").path(String.valueOf(id))
                .request(MediaType.APPLICATION_JSON)
                .put(ticketToReject);

        if (response.getStatus() == 204){
            System.out.println("Got the response "+response.getStatus());
            ;
            //responseTicket = response.readEntity(TicketDTO.class).convertTicketDTOToTicket();
        }
        return rejectTicket;
    }

    @Override
    public Ticket closeTicket(int id) throws TicketException {
        Ticket closeTicket = null;
        if (jaxrsTicketStore.containsKey(id)) {
            closeTicket = jaxrsTicketStore.get(id);
        }

        closeTicket.setStatus(Status.CLOSED);
        Entity<TicketDTO> ticketToClose = Entity.json(new TicketDTO(closeTicket));
        Response response = client.target("http://localhost:9999")
                .path("/Ticket/").path(String.valueOf(id))
                .request(MediaType.APPLICATION_JSON)
                .put(ticketToClose);

        if (response.getStatus() == 204){
            System.out.println("Got the response "+response.getStatus());
            closeTicket.setStatus(Status.CLOSED);
            //responseTicket = response.readEntity(TicketDTO.class).convertTicketDTOToTicket();
        }
        return closeTicket;
    }

    @Override
    public void triggerShutdown() {
        client.close();
    }
}
