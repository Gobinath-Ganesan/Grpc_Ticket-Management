package de.uniba.rz.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import de.uniba.rz.Utlities.GeneralUtilitiesUdp;

public class UdpReceiver {
	public UdpReceiver() {
	}

	public String getMessageFromServer() {
		System.out.println("\t [SERVER]: Start listening at Client Side  port " + GeneralUtilitiesUdp.CLIENT_PORT_NO
				+ " for messages.");
		String receivedInfo = "";

		// try-with-resources, create unbound serverSocket
		try (DatagramSocket serverSocket = new DatagramSocket(null)) {
			// create socket address
			SocketAddress address = new InetSocketAddress(GeneralUtilitiesUdp.CLIENT_PORT_NO);

			// and bind the socket to this address
			serverSocket.bind(address);

			// set timeout time to 5000ms
			serverSocket.setSoTimeout(5000);

			// prepare packet to receive data
			byte[] data = new byte[256];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				// wait for packet
				serverSocket.receive(packet);
				System.out.println("\t [SERVER]: Received Packet from Server");

				// Get Date from packet
				byte[] receivedData = packet.getData();

				// Assign to STring
				receivedInfo = new String(receivedData);

			} catch (SocketTimeoutException e) {
				// swallow timeout

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
		return receivedInfo;

	}

}
