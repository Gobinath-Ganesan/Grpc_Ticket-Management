package de.uniba.rz.jaxrsbackend;

import de.uniba.rz.backend.RemoteAccess;
import de.uniba.rz.backend.TicketStore;
import de.uniba.rz.jaxrsbackend.stubs.TicketService;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Properties;

public class RestRemoteAccess implements RemoteAccess {

    private static Properties properties = Configuration.loadProperties();
    @Override
    public void prepareStartup(TicketStore ticketStore) {
        TicketService ticketService = new TicketService(ticketStore);
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void run() {
        String serverUri = properties.getProperty("serverUri");
        //String serverUri = "http://localhost:9999/";

        URI baseUri = UriBuilder.fromUri(serverUri).build();
        ResourceConfig config = ResourceConfig.forApplicationClass(ExamplesApi.class);
        JdkHttpServerFactory.createHttpServer(baseUri, config);
        System.out.println("Server ready to serve your JAX-RS requests...");
    }
}
