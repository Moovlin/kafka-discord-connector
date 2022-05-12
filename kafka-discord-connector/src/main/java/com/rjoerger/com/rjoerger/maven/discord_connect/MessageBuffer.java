package com.rjoerger.com.rjoerger.maven.discord_connect;

import java.util.AbstractCollection;
import java.util.List;

import discord4j.core.object.entity.Message;

import java.util.concurrent.ConcurrentLinkedQueue;


public class MessageBuffer {
	private AbstractCollection<String> messageList;
	
	public MessageBuffer () {
		messageList = new ConcurrentLinkedQueue<String>();
	}
	
	public void addMessage(Message message) {
		messageList.add(message.getContent());
	}
	
	public void getAllMessages(List<String> messageArr) {
		messageList.removeAll(messageArr);
	}
	
	public boolean isEmpty() {
		return messageList.isEmpty();
	}

	public int getBufferSize(){ return messageList.size(); }
}
