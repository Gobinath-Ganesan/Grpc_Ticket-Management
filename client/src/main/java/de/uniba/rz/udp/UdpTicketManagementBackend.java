package de.uniba.rz.udp;

import java.util.HashMap;
import java.util.List;
import de.uniba.rz.Utlities.GeneralUtilitiesUdp;
import de.uniba.rz.Utlities.GeneralUtilitiesUdp.Operations;
import de.uniba.rz.app.TicketManagementBackend;
import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketException;
import de.uniba.rz.entities.Type;

public class UdpTicketManagementBackend implements TicketManagementBackend {

	private String opcode;
	UdpSender udpsend;
	HashMap<Integer, Ticket> udpTicketStore = new HashMap<>();

	public UdpTicketManagementBackend(String hostname, int port) {
		super();
		udpsend = new UdpSender(hostname, port);
	}

	@Override
	public void triggerShutdown() {

	}

	@Override
	public Ticket createNewTicket(String reporter, String topic, String description, Type type, Priority priority)
			throws TicketException {
		// Send the ticket without id
		String newTicketMsg = null;
		String dummyId = "0", receivedMsg = "";
		Ticket newTicket = null;
		opcode = GeneralUtilitiesUdp.assignOpcode(Operations.NEW_TICKET);
		newTicketMsg = GeneralUtilitiesUdp.createUdpMsg(opcode, dummyId, reporter, topic, description, type, priority);

		udpsend.sendMessage(newTicketMsg);

		// Receive the newly created ticket
		UdpReceiver udpreceiver = new UdpReceiver();

		// Get the received message
		receivedMsg = udpreceiver.getMessageFromServer();

		// Convert String to Ticket newTicket =
		newTicket = GeneralUtilitiesUdp.stringToTicketConversion(receivedMsg);

		udpTicketStore.put(newTicket.getId(), newTicket);

		return (Ticket) newTicket.clone();

	}

	@Override
	public List<Ticket> getAllTickets() throws TicketException {
		// TODO Auto-generated method stub
		String allTicketsToRetrive;
		List<Ticket> receivedTicket;

		opcode = GeneralUtilitiesUdp.assignOpcode(Operations.GET_ALL_TICKET);

		udpsend.sendMessage(GeneralUtilitiesUdp.createUdpMsg(opcode));

		// Default receive
		UdpReceiver udpreceiver = new UdpReceiver();
		allTicketsToRetrive = udpreceiver.getMessageFromServer();

		// Parse string to ticket
		receivedTicket = GeneralUtilitiesUdp.stringToListOfTickets(allTicketsToRetrive);

		for (Ticket ticket : receivedTicket)
			udpTicketStore.put(ticket.getId(), ticket);

		return receivedTicket;
	}

	@Override
	public Ticket getTicketById(int id) throws TicketException {
		Ticket ticket = null;
		String ticketToRetrive = null;
		opcode = GeneralUtilitiesUdp.assignOpcode(Operations.GET_TICKET_BY_ID);

		ticketToRetrive = GeneralUtilitiesUdp.createUdpMsg(opcode, id);
		udpsend.sendMessage(ticketToRetrive);

		// Get the received message
		UdpReceiver udpreceiver = new UdpReceiver();
		String receivedMsg = udpreceiver.getMessageFromServer();

		// Convert String to Ticket newTicket =
		ticket = GeneralUtilitiesUdp.stringToTicketConversion(receivedMsg);
		return ticket;
	}

	@Override
	public Ticket acceptTicket(int id) throws TicketException {
		Ticket ticketToAccept = null;
		String msgToSend = null;
		if (udpTicketStore.containsKey(id)) {
			ticketToAccept = udpTicketStore.get(id);
		}
		opcode = GeneralUtilitiesUdp.assignOpcode(Operations.ACCEPT_TICKET);
		msgToSend = GeneralUtilitiesUdp.createUdpMsg(opcode, id);
		udpsend.sendMessage(msgToSend);

		return (Ticket) ticketToAccept.clone();
	}

	@Override
	public Ticket rejectTicket(int id) throws TicketException {
		Ticket newTicket = null;
		String ticketToReject = null;
		opcode = GeneralUtilitiesUdp.assignOpcode(Operations.REJECT_TICKET);
		ticketToReject = GeneralUtilitiesUdp.createUdpMsg(opcode, id);
		udpsend.sendMessage(ticketToReject);
		return newTicket;
	}

	@Override
	public Ticket closeTicket(int id) throws TicketException {
		Ticket newTicket = null;
		String ticketToClose = null;
		opcode = GeneralUtilitiesUdp.assignOpcode(Operations.CLOSE_TICKET);
		ticketToClose = GeneralUtilitiesUdp.createUdpMsg(opcode, id);
		udpsend.sendMessage(ticketToClose);
		return newTicket;
	}

}
