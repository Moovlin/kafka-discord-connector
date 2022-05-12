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

public class DiscordSourceTask extends SourceTask{

	
	private static final Logger log = LoggerFactory.getLogger(DiscordSourceTask.class);
	private String token;
	private MessageBuffer buffer;
	private String topic;
	private DiscordMessages messageGen;
		
	
	@Override
	public String version() {
		// TODO Auto-generated method stub
		return "0.1.5";
	}

	@Override
	public void start(Map<String, String> props) {
		// TODO Auto-generated method stub
		log.info("Gathering Properties and creating objects");
		token = props.get(DiscordSourceConnector.TOKEN_CONFIG);
		topic = props.get(DiscordSourceConnector.TOPIC_CONFIG);
		//buffer = new MessageBuffer();
		//messageGen = new DiscordMessages(buffer, token);
		log.info("Created the message buffer and token");
	}

	@Override
	public List<SourceRecord> poll() throws InterruptedException {
		ArrayList<SourceRecord> records = new ArrayList<SourceRecord>();
		/*
		while (true) {
			log.info("In the loop...");
			if (!buffer.isEmpty()) {
				List<String> buffMessage = new ArrayList<String>();
				buffer.getAllMessages(buffMessage);
				for (String msg : buffMessage) {
					SourceRecord rec = new SourceRecord(Collections.singletonMap("Discord", msg),
							Collections.singletonMap("Message", msg),
							topic,
							null,
							msg);
					log.info("Message from Discord:\t" + msg);
					records.add(rec);
				}
			} else {
				log.info("No messages, sleeping.");
				Thread.sleep(1000);

			}
			if (!records.isEmpty()) {
				return records;
			} else {
				return null;
			}
		}
		 */
		DiscordClient client = DiscordClient.create(this.token);
		Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) ->
				gateway.on(MessageCreateEvent.class, event -> {
					Message message = event.getMessage();
					if(!message.getContent().isEmpty()) {
						String msg = message.getContent();
						SourceRecord rec = new SourceRecord(Collections.singletonMap("Discord", msg),
								Collections.singletonMap("Message", msg),
								topic,
								null,
								msg);
						log.info("Message from Discord:\t" + msg);
						records.add(rec);
					}
					return Mono.empty();
				}));
		log.info("Blocking....");
		login.block();
		log.info("Unblocked...?");
		if (!records.isEmpty()) {
			log.info("Returning msg array");
			return records;
		} else {
			return null;
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

		
}
