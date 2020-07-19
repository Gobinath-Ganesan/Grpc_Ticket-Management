package de.uniba.rz.amqpbackend;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import de.uniba.rz.backend.RemoteAccess;
import de.uniba.rz.backend.TicketStore;


public class AMQPRemoteAccess implements RemoteAccess {
	String QUEUE_NAME;
	String hostname, routingkey, apiType;
	ConnectionFactory connFactory = new ConnectionFactory();
	
	
	public AMQPRemoteAccess(String hostname, String routingkey, String apiType) {
		this.hostname = hostname;
		this.routingkey = routingkey;
		this.apiType = apiType;
		this.QUEUE_NAME = routingkey;

	}

	@Override
	public void run() {

	}
	
	@Override
	 public void prepareStartup(TicketStore ticketStore) {
        System.out.println(" [*] Inside Message queue prepare startup");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        AMQPMsgHandler handöeMsg = new AMQPMsgHandler(ticketStore);
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.queuePurge(QUEUE_NAME);
            Object monitor = new Object();
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            @SuppressWarnings("unchecked")
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                Map<String, String> receiveData = null;
                System.out.println(" [x] Received message'");
                byte[] tmpBuffer = new byte[0];
                try {
                	receiveData = (Map<String, String>) getDataFromByteArray(delivery.getBody());
                    tmpBuffer = handöeMsg.performOperations(receiveData);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("Sending ticket to "+ delivery.getProperties().getReplyTo());
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, tmpBuffer);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    tmpBuffer = new byte[0];
                    // RabbitMq consumer worker thread notifies the RPC server owner thread
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, (consumerTag -> { }));
            // Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private Object getDataFromByteArray(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inByte = new ByteArrayInputStream(data);
        ObjectInputStream inStream = new ObjectInputStream(inByte);
        return inStream.readObject();

    }

	@Override
	public void shutdown() {

	}

}
