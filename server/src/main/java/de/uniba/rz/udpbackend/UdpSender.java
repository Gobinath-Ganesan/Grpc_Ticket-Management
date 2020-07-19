package de.uniba.rz.udpbackend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class UdpSender {
	
	private SocketAddress senderAddress;
	

	/**
	 * This constructor creates server address
	 * @param hostname - String - hostname
	 * @param port - int - port number in which the message needs to be sent
	 */
	public UdpSender(String hostname, int port) {
		this.senderAddress = new InetSocketAddress(hostname, port);
	}

	/**
	 * This method sends the udp packet to the server
	 * @param msg - String -- The message that needs to be sent
	 */
	public void sendMessage(String msg) {
		DatagramPacket packet = null;
		try (DatagramSocket sock = new DatagramSocket(null)) {

			
			// create packet
			byte[] messageData = msg.getBytes();
			packet = new DatagramPacket(messageData, messageData.length, senderAddress);

			// send packet
			sock.send(packet);
		} catch (IOException e) {
			// dummy exception handling
			e.printStackTrace();
		}

	}

}
