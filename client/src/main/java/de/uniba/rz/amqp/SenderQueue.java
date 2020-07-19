package de.uniba.rz.amqp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderQueue {

	private Connection connection;
	private Channel channel;
	private String requestQueueName;

	public SenderQueue(String hostname, String routingkey) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(hostname);
		connection = factory.newConnection();
		channel = connection.createChannel();
		requestQueueName = routingkey;
	}

	public Object call(byte[] message) throws IOException, InterruptedException {
		final String corrId = UUID.randomUUID().toString();
		String replyQueueName = channel.queueDeclare().getQueue();
		AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName)
				.build();
		System.out.println("Publishing the ticket to the buffer ");
		channel.basicPublish("", requestQueueName, props, message);
		final BlockingQueue<Object> response = new ArrayBlockingQueue<>(1);

		String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
			if (delivery.getProperties().getCorrelationId().equals(corrId)) {
				try {
					System.out.println("Response received in the blocking array");
					response.offer(getDataFromByteArray(delivery.getBody()));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}, consumerTag -> {
		});

		Object result = response.take();
		System.out.println("Response is " + result);
		channel.basicCancel(ctag);
		return result;
	}

	public void close() throws Exception {
		connection.close();
	}

	private Object getDataFromByteArray(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return is.readObject();
	}
}
