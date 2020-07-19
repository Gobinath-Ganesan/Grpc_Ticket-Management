package de.uniba.rz.amqp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import de.uniba.rz.app.TicketManagementBackend;
import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketException;
import de.uniba.rz.entities.Type;

public class AMQPTicketManagementBackend implements TicketManagementBackend {

	private SenderQueue senderQ;

	public AMQPTicketManagementBackend(String hostname, String routingkey) throws IOException, TimeoutException {
		this.senderQ = new SenderQueue(hostname, routingkey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Ticket createNewTicket(String reporter, String topic, String description, Type type, Priority priority)
			throws TicketException {
		List<Ticket> newTicket = null;
		try {
			System.out.println("Creating and sending new ticket to the server");
			byte bufferData[] = new byte[0];
			bufferData = createNewTicketObject(reporter, topic, description, type, priority);
			Object result = this.senderQ.call(bufferData);
			newTicket = (List<Ticket>) result;
			System.out.println("New ticket id is " + newTicket.get(0).getId());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return (Ticket) newTicket.get(0).clone();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ticket> getAllTickets() throws TicketException {
		List<Ticket> allTicketsList = new ArrayList<>();

		byte bufferData[] = new byte[0];
		try {
			bufferData = allTicketsObject();
			Object result = this.senderQ.call(bufferData);
			allTicketsList = (List<Ticket>) result;
			System.out.println("new ticket list ");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return allTicketsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Ticket getTicketById(int id) throws TicketException {
		List<Ticket> ticket = null;
		try {
			byte bufferData[] = ticketByIdObject(id);
			Object result = null;
			result = this.senderQ.call(bufferData);
			ticket = (List<Ticket>) result;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return (Ticket) ticket.get(0).clone();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Ticket acceptTicket(int id) throws TicketException {
		List<Ticket> ticket = null;
		try {
			byte bufferData[] = acceptTicketObject(id);
			Object result = null;
			result = this.senderQ.call(bufferData);
			ticket = (List<Ticket>) result;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return (Ticket) ticket.get(0).clone();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Ticket rejectTicket(int id) throws TicketException {
		List<Ticket> ticket = null;
		try {
			byte bufferData[] = rejectTicketObject(id);
			Object result = null;
			result = this.senderQ.call(bufferData);
			ticket = (List<Ticket>) result;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return (Ticket) ticket.get(0).clone();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Ticket closeTicket(int id) throws TicketException {
		List<Ticket> ticket = null;
		try {
			byte bufferData[] = closeTicketObject(id);
			Object result = null;
			result = this.senderQ.call(bufferData);
			ticket = (List<Ticket>) result;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return (Ticket) ticket.get(0).clone();
	}

	@Override
	public void triggerShutdown() {
		try {
			this.senderQ.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private byte[] createNewTicketObject(String reporter, String topic, String description, Type type,
			Priority priority) throws IOException {
		Map<String, String> ticketInfo = new HashMap<String, String>();
		ticketInfo.put("reporter", reporter);
		ticketInfo.put("topic", topic);
		ticketInfo.put("description", description);
		ticketInfo.put("type", type.toString());
		ticketInfo.put("priority", priority.toString());
		ticketInfo.put("operation", "1");
		return createBuffer(ticketInfo);
	}

	private byte[] allTicketsObject() throws IOException {
		Map<String, String> ticketInfo = new HashMap<String, String>();
		ticketInfo.put("operation", "3");
		return createBuffer(ticketInfo);
	}

	private byte[] ticketByIdObject(int id) throws IOException {
		Map<String, String> ticketInfo = new HashMap<String, String>();
		ticketInfo.put("operation", "4");
		ticketInfo.put("id", String.valueOf(id));
		return createBuffer(ticketInfo);
	}

	private byte[] acceptTicketObject(int id) throws IOException {
		Map<String, String> ticketInfo = new HashMap<String, String>();
		ticketInfo.put("operation", "6");
		ticketInfo.put("id", String.valueOf(id));
		return createBuffer(ticketInfo);
	}

	private byte[] rejectTicketObject(int id) throws IOException {
		Map<String, String> ticketInfo = new HashMap<String, String>();
		ticketInfo.put("operation", "5");
		ticketInfo.put("id", String.valueOf(id));
		return createBuffer(ticketInfo);
	}

	private byte[] closeTicketObject(int id) throws IOException {
		Map<String, String> ticketInfo = new HashMap<String, String>();
		ticketInfo.put("operation", "7");
		ticketInfo.put("id", String.valueOf(id));
		return createBuffer(ticketInfo);
	}

	private byte[] createBuffer(Map<String, String> tickets) throws IOException {
		ByteArrayOutputStream outByte = new ByteArrayOutputStream();
		ObjectOutputStream oouObj = new ObjectOutputStream(outByte);
		oouObj.writeObject(tickets);
		return outByte.toByteArray();
	}
}
