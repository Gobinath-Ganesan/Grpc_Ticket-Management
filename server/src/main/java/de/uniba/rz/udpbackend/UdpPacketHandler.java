package de.uniba.rz.udpbackend;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;

import de.uniba.rz.Utlities.GeneralUtilitiesUdp;
import de.uniba.rz.Utlities.GeneralUtilitiesUdp.Operations;
import de.uniba.rz.backend.TicketStore;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;

public class UdpPacketHandler extends Thread {
	
	DatagramPacket datagramPacket = null;
	String receivedMsg , opcode;
	private TicketStore ticketStore;
	
	public UdpPacketHandler(DatagramPacket argdatagrampack, TicketStore argTicketSt){
		this.datagramPacket = argdatagrampack;
		this.ticketStore = argTicketSt;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Inside run method");
		parsePacket();
		
	}
	
	/**
	 * This method splits the packet to tickets
	 */
	public void parsePacket() {
		
		System.out.println("Inside parsePacketd");
		//Convert received packet to String
		receivedMsg = packetToStringConversion(datagramPacket);
		
		//Split opcode and message
		String[] ticketInfo = receivedMsg.split("\\|");
		
		//Call the operations
		performOperations(ticketInfo);
		
	}
	
	/**
	 * This method extract the String message from packets
	 */
	public String packetToStringConversion(DatagramPacket argPacket) {
		System.out.println("Inside packetToStringConversion");
		String receivedMsg;
		// receivedMsg = new String(argPacket.getData(), 0, argPacket.getLength());
		byte[] tmpVal = argPacket.getData();
		receivedMsg = new String(tmpVal);
		System.out.println(receivedMsg);
		return receivedMsg;
	}
	
	/**
	 * This method performs the required backend operations
	 * @param argParsedMsg
	 */
	public void performOperations(String[] argParsedMsg)
	{
		System.out.println("performOperations");
		
		String ticketContent;
		Ticket newTicket;
		String receiverAddr = datagramPacket.getAddress().toString().substring(1);
		UdpSender udpSender = new UdpSender(receiverAddr, GeneralUtilitiesUdp.CLIENT_PORT_NO);
		Operations ops = GeneralUtilitiesUdp.assignEnum(argParsedMsg[0]);
		//ops.trim();
		
		//Assign operation from opcode
		switch(ops)
		{
			case NEW_TICKET:
				
				System.out.println("Inside new ticket");
				
				ticketContent = argParsedMsg[1];
				
				//Get the ticket from String
				newTicket = GeneralUtilitiesUdp.stringToTicketConversion(ticketContent);
				
				//Store new ticket
				newTicket = ticketStore.storeNewTicket(newTicket.getReporter(), newTicket.getTopic(), newTicket.getDescription(), newTicket.getType(), newTicket.getPriority());
				
				//Send newly created ticket
				//udpSender = new UdpSender(receiverAddr, GeneralUtilitiesUdp.CLIENT_PORT_NO);
				
				//Convert the Ticket to String
				String msg = newTicket.toStringUdpTicket();
						
				//Send the created Ticket back
				udpSender.sendMessage(msg);
				
				break;
				
			case UPDATE_TICKET:
				
				break;
				
			case GET_ALL_TICKET:
				
				System.out.println("Inside send all tickets");
				
				//Get All available Tickets and convert it to String
				String ticketlist = GeneralUtilitiesUdp.listOfTicketsToString(ticketStore.getAllTickets());
				
				//Send newly created ticket
				//udpSender = new UdpSender(receiverAddr, GeneralUtilitiesUdp.CLIENT_PORT_NO);
				
				System.out.println(ticketlist);
				
				//Send the message
				udpSender.sendMessage(ticketlist);
				
				break;
				
			case GET_TICKET_BY_ID:
				System.out.println("Inside send tickets by id");
				int id = Integer.parseInt(argParsedMsg[1].trim());
				System.out.println("Ticket id "+ id + ".");
				Ticket t = GeneralUtilitiesUdp.getTicketByID(ticketStore.getAllTickets(), id);
				
				//Send the message
				udpSender.sendMessage(t.toStringUdpTicket());
				break;
				
			case REJECT_TICKET:
				System.out.println("Inside reject tickets by id");
				int idToReject = Integer.parseInt(argParsedMsg[1].trim());
				System.out.println("Ticket id "+ idToReject + ".");
				Ticket ticketToReject = GeneralUtilitiesUdp.getTicketByID(ticketStore.getAllTickets(), idToReject);
				ticketToReject.setStatus(Status.REJECTED);
				break;
				
			case ACCEPT_TICKET:
				System.out.println("Inside accept tickets by id");
				int idToAccept = Integer.parseInt(argParsedMsg[1].trim());
				System.out.println("Ticket id "+ idToAccept + ".");
				Ticket ticketToAccept = GeneralUtilitiesUdp.getTicketByID(ticketStore.getAllTickets(), idToAccept);
				ticketToAccept.setStatus(Status.ACCEPTED);
				break;
				
			case CLOSE_TICKET:
				System.out.println("Inside close tickets by id");
				int idToClose = Integer.parseInt(argParsedMsg[1].trim());
				System.out.println("Ticket id "+ idToClose + ".");
				Ticket ticketToClose = GeneralUtilitiesUdp.getTicketByID(ticketStore.getAllTickets(), idToClose);
				ticketToClose.setStatus(Status.CLOSED);
				
				break;
				
			
		default:
			
			System.out.println("In default case");
			break;
				
		}
	}
	
	
	
	

}
