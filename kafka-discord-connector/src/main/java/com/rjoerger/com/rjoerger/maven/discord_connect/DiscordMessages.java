package com.rjoerger.com.rjoerger.maven.discord_connect;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class DiscordMessages {
	// Will have to make this a Kafka connect property
	
	private static final Logger log = LoggerFactory.getLogger(DiscordMessages.class);
	private DiscordClient client;
	
	public Mono<Void> login;
	
	private MessageBuffer messages;
	
	public DiscordMessages(MessageBuffer buffer, String token) {
		this.messages = buffer;
		this.client = DiscordClient.create(token);


		this.login = client.withGateway((GatewayDiscordClient gateway) -> 
		gateway.on(MessageCreateEvent.class, event -> { 
			Message message = event.getMessage();
			
			if(!message.getContent().isEmpty()) {
				messages.addMessage(message);
				log.info(message.getContent());
				log.info("Buffer size is:\t" + Integer.toString(messages.getBufferSize()));
			}
			return Mono.empty();
		}));
		log.info("Blocking....");
		login.block();
	}
}
