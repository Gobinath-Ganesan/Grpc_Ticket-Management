package de.uniba.rz.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Enumeration to describe the Priority of a {@link Ticket} or
 * {@link TransferTicket}.
 * 
 * Possible Values:
 * <ul>
 * <li>{@code CRITICAL}</li>
 * <li>{@code MAJOR}</li>
 * <li>{@code MINOR}</li>
 * </ul>
 * 
 */
public enum Priority implements Serializable {
	CRITICAL, MAJOR, MINOR;

}
