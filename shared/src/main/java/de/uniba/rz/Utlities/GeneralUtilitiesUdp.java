package de.uniba.rz.Utlities;

import java.net.DatagramPacket;
import java.util.*;

import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketException;
import de.uniba.rz.entities.Type;
import de.uniba.rz.entities.Status;

public class GeneralUtilitiesUdp {

	public static int CLIENT_PORT_NO = 7845;

	public static enum Operations {
		NEW_TICKET, UPDATE_TICKET, GET_ALL_TICKET, GET_TICKET_BY_ID, REJECT_TICKET, ACCEPT_TICKET, CLOSE_TICKET
	}

	public static String udpMsgDelimiter = "\\$";
	public static String operationDelimiter = "|";
	public static String interTicketSeparator = "\\&";
	public static String intraTicketSeparator = "\\#";

	public static String assignOpcode(Operations argOp) {
		String opcode = "0";
		switch (argOp) {
		case NEW_TICKET:
			opcode = "1";
			break;
		case UPDATE_TICKET:
			opcode = "2";
			break;
		case GET_ALL_TICKET:
			opcode = "3";
			break;
		case GET_TICKET_BY_ID:
			opcode = "4";
			break;
		case REJECT_TICKET:
			opcode = "5";
			break;
		case ACCEPT_TICKET:
			opcode = "6";
			break;
		case CLOSE_TICKET:
			opcode = "7";
		default:

		}
		return opcode;
	}

	public static Operations assignEnum(String str) {

		switch (str) {
		case "1":
			return Operations.NEW_TICKET;
		case "2":
			return Operations.UPDATE_TICKET;
		case "3":
			return Operations.GET_ALL_TICKET;
		case "4":
			return Operations.GET_TICKET_BY_ID;
		case "5":
			return Operations.REJECT_TICKET;
		case "6":
			return Operations.ACCEPT_TICKET;
		case "7":
			return Operations.CLOSE_TICKET;

		}
		return null;
	}

	public static String createUdpMsg(String opcode, int id) {
		String newTicketMsg = opcode + operationDelimiter + Integer.toString(id);
		return newTicketMsg;
	}

	public static String createUdpMsg(String opcode, String dummyid, String reporter, String topic, String description,
			Type type, Priority priority) {
		String newTicketMsg = opcode + operationDelimiter + dummyid + intraTicketSeparator + reporter + intraTicketSeparator
				+ topic + intraTicketSeparator + description + intraTicketSeparator + type.name() + intraTicketSeparator
				+ priority.name() + intraTicketSeparator + Status.NEW.name();
		return newTicketMsg;
	}

	public static String createUdpMsg(String opcode) {
		String newTicketMsg = opcode + operationDelimiter;
		return newTicketMsg;
	}

	

	/**
	 * This method assigns String to Tickets
	 * 
	 * @param argReceivedTicket - String - the message from packet
	 * @return - Ticket object
	 */
	public static Ticket stringToTicketConversion(String argReceivedTicket) {

		Ticket newTicket = new Ticket();
		String[] splitedTicketVal = argReceivedTicket.split("\\"+intraTicketSeparator);
		newTicket.setId(Integer.parseInt(splitedTicketVal[0]));
		newTicket.setReporter(splitedTicketVal[1]);
		newTicket.setTopic(splitedTicketVal[2]);
		newTicket.setDescription(splitedTicketVal[3]);
		newTicket.setType(Type.valueOf(splitedTicketVal[4]));
		newTicket.setPriority(Priority.valueOf(splitedTicketVal[5].trim()));
		newTicket.setStatus(Status.valueOf(splitedTicketVal[6].trim()));
		return newTicket;
	}

	public static String listOfTicketsToString(List<Ticket> argTcikets) {
		StringBuilder listOfTickets = new StringBuilder();
		ListIterator<Ticket> iter = argTcikets.listIterator();
		while (iter.hasNext()) {
			Ticket ticket = iter.next();
			listOfTickets.append(ticket.toStringUdpTicket());
			if(iter.hasNext()) {
				listOfTickets.append(interTicketSeparator);
			}
		}
		String ticketString = listOfTickets.toString();
		System.out.println(ticketString);
		return ticketString;
		
	}

	/**
	 * This method converts the string from Server to list of tickets
	 * @param argmsg
	 * @return
	 */
	public static List<Ticket> stringToListOfTickets(String argmsg) {
		List<Ticket> tickets = new ArrayList<>();
		if(argmsg != null && argmsg.length() > 0 && !argmsg.equals("\n") && !argmsg.trim().isEmpty()) {
			String[] ticketList = (argmsg.split("\\" + interTicketSeparator));
			for (String ticket : ticketList) {
				tickets.add(stringToTicketConversion(ticket));
			}
		}
		return tickets;
	}
	
	public static Ticket getTicketByID(List<Ticket> tickets, int id) {
		for(Ticket ticket:tickets) {
			if (ticket.getId() == id) {
				return ticket;
			}
		}
		return null;
	}
	
	public static Ticket getTicketByID(HashMap<Integer, Ticket> amqptickets, int id) throws TicketException {
		if (!amqptickets.containsKey(id)) {
			throw new TicketException("Ticket ID is unknown");
		}
		return amqptickets.get(id);
	}

}
