package de.uniba.rz.jaxrsbackend.stubs;

import de.uniba.rz.Utlities.GeneralUtilitiesUdp;
import de.uniba.rz.backend.TicketStore;
import de.uniba.rz.backend.UnknownTicketException;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketService {

        private static TicketStore ticketStore;

        public TicketService(TicketStore argTicketStore){
            this.ticketStore = argTicketStore;
        }

        public static  Ticket createNewTicket(Ticket argNewTicket){
            Ticket newTicket = ticketStore.storeNewTicket(argNewTicket.getReporter(),argNewTicket.getTopic(),argNewTicket.getDescription(),argNewTicket.getType(),argNewTicket.getPriority());
            return  newTicket;
        }

        public static List<Ticket> getAllTickets(){
            List<Ticket> allTickets = new ArrayList<>();
            allTickets = ticketStore.getAllTickets();
            return allTickets;
        }

        public static Ticket getTicketById(int id){
            Ticket ticket = null;
            ticket = GeneralUtilitiesUdp.getTicketByID(ticketStore.getAllTickets(), id);
            return  ticket;
        }

        public static Ticket updateTicket(int id, Status argStatus) throws UnknownTicketException {
            Ticket ticketToUpdate = null;
            ticketToUpdate = GeneralUtilitiesUdp.getTicketByID(ticketStore.getAllTickets(), id);
            ticketToUpdate.setStatus(argStatus);
            return ticketToUpdate;
        }

        public static boolean deleteTicket(int id){
            Ticket ticketToDelete = null;
            ticketToDelete = GeneralUtilitiesUdp.getTicketByID(ticketStore.getAllTickets(), id);
            ;
            if(ticketStore.getAllTickets().remove(ticketToDelete))
                return true;
            else
                return false;
        }

        public static List<Ticket> searchTicket(String argSearch){
            List<Ticket> allTickets = ticketStore.getAllTickets();

            //allTickets.stream().anyMatch(Ticket -> t..equals(argSearch));

            //myList = allTickets.stream().map(Ticket :: getDescription).collect(Collectors.toList());

            Optional<Ticket> any = allTickets.stream().filter(p -> p.getReporter().equals(argSearch) || p.getTopic().equals(argSearch) || p.getDescription().equals(argSearch)).findAny();

            System.out.println(any.get());

            return allTickets;

        }
}
