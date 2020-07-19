package de.uniba.rz.udpbackend;

import de.uniba.rz.backend.RemoteAccess;
import de.uniba.rz.backend.TicketStore;

public class UdpRemoteAccess implements RemoteAccess {

	private int port;
	
	UdpServer udpServer;

	public UdpRemoteAccess(String port) {
		this.port = Integer.parseInt(port);
	}

	@Override
	public void run() {
		udpServer.start();
	}

	@Override
	public void prepareStartup(TicketStore ticketStore) {
		udpServer = new UdpServer(port,ticketStore);
	}

	@Override
	public void shutdown() {
		udpServer.stopServer();
	}

}
