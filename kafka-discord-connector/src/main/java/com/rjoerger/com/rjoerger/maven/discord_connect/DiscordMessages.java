/**
 * @author Rich Joerger
 */

package com.rjoerger.com.rjoerger.maven.discord_connect;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * A very simple wrapper class which will read the messages from a Discord server and then enter them into a local
 * shared buffer. This very simple, it does not go back in time and read messages it may have missed nor does it persist
 * messages to anything other than RAM (via an instance of the MessageBuffer class).
 */
public class DiscordMessages implements Runnable {
	// Logger to log stuff.
	private static final Logger log = LoggerFactory.getLogger(DiscordMessages.class);

	// The DiscordClient client which is used to connect to the Discord Server
	private DiscordClient client;

	// This "Mono" is the active connection to the Discord server. The lambda within it handles message reading.
	private Mono<Void> login;

	// The shared buffer between the DiscordSrouceTask object and the DiscordMessages object. A synchornized object.
	private MessageBuffer messages;

	/**
	 * Constructor which takes a message buffer object (shared with the DiscordSourceTask) and the token used to connect
	 * to a given server.
	 * @param buffer - The Buffer object
	 * @param token - The token which we use to authroize a connection to a particular Discord server
	 */
	public DiscordMessages(MessageBuffer buffer, String token) {
		this.messages = buffer;
		this.client = DiscordClient.create(token);
	}

	/**
	 * Creates a DiscordClient to read all messages from the Discord server the bot is associated with. Adds them to the
	 * shared buffer class.
	 */
	public void run(){
		log.info("Starting the thread:\t" + Thread.currentThread().getId());
		this.login = client.withGateway((GatewayDiscordClient gateway) ->
				gateway.on(MessageCreateEvent.class, event -> {
					Message message = event.getMessage();

					if(!message.getContent().isEmpty()) {
						messages.addMessage(message);
						log.debug(message.getContent());
						log.debug("Buffer size is:\t" + Integer.toString(messages.getBufferSize()));
					}
					return Mono.empty();
				}));
		log.info("Blocking...");
		login.block();
	}

	/**
	 * Handles shutting down as needed. At this point, does nothing other than print.
	 */
	public void shutdown(){

	}
}
