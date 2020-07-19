package de.uniba.rz.entities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import de.uniba.rz.Utlities.GeneralUtilitiesUdp;

/**
 * used Ticket Representation.
 * 
 */
public class Ticket implements Serializable {

	private static final long serialVersionUID = -6979364632920616224L;

	private int id;
	private String reporter;
	private String topic;
	private String description;
	private Type type;
	private Priority priority;
	private Status status;

	public Ticket() {}
	
	public Ticket(int id, String reporter, String topic, String description, Type type, Priority priority) {
		super();
		this.id = id;
		this.reporter = reporter;
		this.topic = topic;
		this.description = description;
		this.type = type;
		this.priority = priority;
		this.setStatus(Status.NEW);
	}

	public Ticket(int id, String reporter, String topic, String description, Type type, Priority priority,
			Status status) {
		super();
		this.id = id;
		this.reporter = reporter;
		this.topic = topic;
		this.description = description;
		this.type = type;
		this.priority = priority;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public String getReporter() {
		return reporter;
	}

	public Status getStatus() {
		return status;
	}

	public String getTopic() {
		return topic;
	}

	public Type getType() {
		return type;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

	public String toStringUdpTicket() {
		String seperator = GeneralUtilitiesUdp.intraTicketSeparator;
		/*StringBuilder ticketStringBuilder = new StringBuilder();
		ticketStringBuilder.append(id);
		ticketStringBuilder.append(seperator);
		ticketStringBuilder.append(topic);
		ticketStringBuilder.append(seperator);
		ticketStringBuilder.append(reporter);
		ticketStringBuilder.append(seperator);
		ticketStringBuilder.append(status);
		ticketStringBuilder.append(seperator);
		ticketStringBuilder.append(type);
		ticketStringBuilder.append(seperator);
		ticketStringBuilder.append(priority);
		ticketStringBuilder.append(seperator);
		ticketStringBuilder.append(description.trim());*/
		return id + seperator + reporter + seperator + topic + seperator + description + seperator + type.name() + seperator
				+ priority.name() + seperator + status.name();
	}

	@Override
	public String toString() {
		return "Ticket #" + id + ": " + topic + " (reported by: " + reporter + ")\n" + "Status: " + status + "\t Type:"
				+ type + "\t Priority: " + priority + "\n" + "Description:\n" + description;
	}

	@Override
	public Object clone() {
		return new Ticket(this.id, this.reporter, this.topic, this.description, this.type, this.priority, this.status);
	}
	
	public byte[] getBytes() {
        byte[]bytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
           // oos.flush();
            //oos.reset();
            //bytes = baos.toByteArray();
            //oos.close();
            //baos.close();
        } catch(IOException e){
            //bytes = new byte[] {};
            //Logger.getLogger("bsdlog").log(Level.ALL, "unable to write to output stream" + e);
        }
        return baos.toByteArray();
    }

}
