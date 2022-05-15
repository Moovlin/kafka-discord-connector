/**
 * @author Rich Joerger#
 */

package com.rjoerger.com.rjoerger.maven.discord_connect;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Kafka Connector task which bundles and then sends the messages into Kafka.
 */
public class DiscordSourceTask extends SourceTask{

	// The logger to be used.
	private static final Logger log = LoggerFactory.getLogger(DiscordSourceTask.class);

	// The token we are using to connect.
	private String token;

	// The shared message buffer.
	private MessageBuffer buffer;

	// The topic which we are writing messages to
	private String topic;

	// The message generations.
	private DiscordMessages messageGen;

	/**
	 * Outputs the version.
	 * @return - The version
	 */
	@Override
	public String version() {
		// TODO Auto-generated method stub
		return "0.1.9";
	}

	/**
	 * Sets up the DiscordMessages object, MessageBuffer, & then launches the DiscordMessages Thread based on the
	 * properties included
	 * @param props - The properties for the thread.
	 */
	@Override
	public void start(Map<String, String> props) {
		// TODO Auto-generated method stub
		log.info("Gathering Properties and creating objects");
		token = props.get(DiscordSourceConnector.TOKEN_CONFIG);
		topic = props.get(DiscordSourceConnector.TOPIC_CONFIG);
		buffer = new MessageBuffer();
		messageGen = new DiscordMessages(buffer, token);
		log.info("Starting the thread:\t" + Thread.currentThread().getId());
		Thread messageThread = new Thread(messageGen);
		messageThread.start();
		log.info("Created the message buffer and token");
	}

	/**
	 * The poll loop. Infinate loops waiting for messages in the buffer, bundles, and then sends them to Kafka.
	 * @return - A list of records.
	 * @throws InterruptedException - The thread has been interrupted
	 */
	@Override
	public List<SourceRecord> poll() throws InterruptedException {
		ArrayList<SourceRecord> records = new ArrayList<SourceRecord>();
		log.debug("In the poll. Message Buffer Size:\t" + buffer.getBufferSize());
		if (!buffer.isEmpty()) {
			List<String> buffMessage = new ArrayList<String>();
			buffMessage = buffer.getAllMessages();

			for (String msg : buffMessage) {
				SourceRecord rec = new SourceRecord(Collections.singletonMap("Discord", msg),
						Collections.singletonMap("Message", msg),
						topic,
						null,
						msg);
				log.info("Message from Discord:\t" + msg);
				records.add(rec);
			}

			log.info("Message Buff size:\t" + buffer.getBufferSize());
		} else {
			log.debug("No messages, sleeping.");
			Thread.sleep(1000);

		}
		if (!records.isEmpty()) {
			return records;
		} else {
			return null;
		}
	}

	/**
	 * Nothing to really stop at thi point. Empty thread.
	 */
	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

		
}
