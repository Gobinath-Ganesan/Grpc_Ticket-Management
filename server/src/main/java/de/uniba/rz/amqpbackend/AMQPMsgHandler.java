package de.uniba.rz.amqpbackend;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import de.uniba.rz.Utlities.GeneralUtilitiesUdp;
import de.uniba.rz.Utlities.GeneralUtilitiesUdp.Operations;
import de.uniba.rz.backend.TicketStore;
import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.Type;

public class AMQPMsgHandler {

	TicketStore ticketstore;

	public AMQPMsgHandler(TicketStore argTicketStore) {
		this.ticketstore = argTicketStore;
	}

	public byte[] performOperations(Map<String, String> argReceivedData) throws IOException {
		byte[] returnVal = null;
		System.out.println(" [x] Operation performing " + argReceivedData.get("operation"));
		Operations ops = GeneralUtilitiesUdp.assignEnum(argReceivedData.get("operation"));
		switch (ops) {
		case NEW_TICKET:
			System.out.println("Creating a new ticket");
			Ticket newTicket = ticketstore.storeNewTicket(argReceivedData.get("reporter"), argReceivedData.get("topic"),
					argReceivedData.get("description"), Type.valueOf(argReceivedData.get("type")),
					Priority.valueOf(argReceivedData.get("priority")));
			returnVal = ticketObjectToSend(newTicket);
			break;
		case GET_ALL_TICKET:
			List<Ticket> tickets = ticketstore.getAllTickets();
			returnVal = convertTickToByte(tickets);
			break;
		case GET_TICKET_BY_ID:
			System.out.println("Inside send tickets by id");
			int id = Integer.parseInt(argReceivedData.get("id").trim());
			System.out.println("Ticket id "+ id + ".");
			Ticket ticketToReturn = GeneralUtilitiesUdp.getTicketByID(ticketstore.getAllTickets(), id);
			returnVal = ticketObjectToSend(ticketToReturn);
			break;
		case ACCEPT_TICKET:
			System.out.println("Inside accept ticket by id");
			int idToAccept = Integer.parseInt(argReceivedData.get("id").trim());
			System.out.println("Ticket id " + idToAccept + ".");
			Ticket ticketToAccept = GeneralUtilitiesUdp.getTicketByID(ticketstore.getAllTickets(), idToAccept);
			ticketToAccept.setStatus(Status.ACCEPTED);
			returnVal = ticketObjectToSend(ticketToAccept);
			break;
		case REJECT_TICKET:
			System.out.println("Inside reject ticket by id");
			int idToReject = Integer.parseInt(argReceivedData.get("id").trim());
			System.out.println("Ticket id " + idToReject + ".");
			Ticket ticketToReject = GeneralUtilitiesUdp.getTicketByID(ticketstore.getAllTickets(), idToReject);
			ticketToReject.setStatus(Status.REJECTED);
			returnVal = ticketObjectToSend(ticketToReject);
			break;
		case CLOSE_TICKET:
			System.out.println("Inside close ticket by id");
			int idToClose = Integer.parseInt(argReceivedData.get("id").trim());
			System.out.println("Ticket id " + idToClose + ".");
			Ticket ticketToClose = GeneralUtilitiesUdp.getTicketByID(ticketstore.getAllTickets(), idToClose);
			ticketToClose.setStatus(Status.CLOSED);
			returnVal = ticketObjectToSend(ticketToClose);
			break;
		default:
			System.out.println("No valid operation");
		}
		return returnVal;
	}

	private byte[] ticketObjectToSend(Ticket argticket) throws IOException {
		List<Ticket> ticketList = new ArrayList<>();
		if (argticket != null) {
			ticketList.add(argticket);
		}
		return convertTickToByte(ticketList);
	}

	private byte[] convertTickToByte(List<Ticket> tickets) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ObjectOutputStream outObjStream = new ObjectOutputStream(outStream);
		outObjStream.writeObject(tickets);
		return outStream.toByteArray();

	}

}
