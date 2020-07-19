package de.uniba.rz.udpbackend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import de.uniba.rz.backend.TicketStore;

public class UdpServer extends Thread {
	
	private boolean active = true;
	private int port;
	private TicketStore ticketstore;
	
	public UdpServer(int port, TicketStore ticketStore) {
		this.port = port;
		this.ticketstore = ticketStore;
	}

	@Override
	public void run() {
		
		System.out.println("\t [SERVER]: Start listening on port " + port + " for messages.");
		
		active = true;
		// try-with-resources, create unbound serverSocket
		try (DatagramSocket serverSocket = new DatagramSocket(null)) {
			// create socket address
			SocketAddress address = new InetSocketAddress(port);

			// and bind the socket to this address
			serverSocket.bind(address);

			// set timeout time to 5000ms
			serverSocket.setSoTimeout(5000);

			while (active) {
				// prepare packet to receive data
				byte[] data = new byte[256];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				try {
					// wait for packet
					serverSocket.receive(packet);
					System.out.println("\t [SERVER]: Received Packet. " + "Creating new Thread to handle it.");
					packet.getData();
					// handle packet externally in another thread
					new UdpPacketHandler(packet,ticketstore).start();
				} catch (SocketTimeoutException e) {
					// swallow timeout
				}
			}
		} catch (SocketException e) {
			// dummy exception handling - do NOT do this in your Assignment!
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			// dummy exception handling - do NOT do this in your Assignment!
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("\t [SERVER]: Stopped.");

	}
	
	/**
	 * This function stops the server.
	 */
	public void stopServer() {
		active = false;
		System.out.println("\t [SERVER]: Stopping to listen for messages.");
	}

}
