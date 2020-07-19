package de.uniba.rz.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class UdpSender {

	private int port;
	private String hostname;

	/**
	 * This constructor creates server address
	 * @param hostname - String - hostname
	 * @param port - int - port number in which the message needs to be sent
	 */
	public UdpSender(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	/**
	 * This method sends the udp packet to the server
	 * @param msg - String -- The message that needs to be sent
	 */
	public void sendMessage(String msg) {
		DatagramPacket packet = null;
		System.out.println("\t [Sender]: Trying to send message '"+msg+"'");
		try (DatagramSocket sock = new DatagramSocket(null)) {
			
			//Create receiver address
			SocketAddress serverAddress = new InetSocketAddress(hostname, port);
			
			// create packet
			byte[] messageData = msg.getBytes();
			packet = new DatagramPacket(messageData, messageData.length, serverAddress);
			
			System.out.println(packet.getData());
			
			// send packet
			sock.send(packet);
		} catch (IOException e) {
			// dummy exception handling
			e.printStackTrace();
		}

	}

}
