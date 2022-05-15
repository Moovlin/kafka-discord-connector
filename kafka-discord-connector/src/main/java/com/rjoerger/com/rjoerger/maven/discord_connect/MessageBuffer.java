/**
 * @author Richard Joerger
 */

package com.rjoerger.com.rjoerger.maven.discord_connect;

import java.util.AbstractCollection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import discord4j.core.object.entity.Message;

import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * A shared buffering object. Very simple wrapper for a ConcurrentLinkedQueue
 */
public class MessageBuffer {
	// The collection which stores the messages. At this point it's strings but could become more complex objects with
	// some reworking
	private AbstractCollection<String> messageList;

	/**
	 * The constructor. Creates a ConcurrentLinkedQueue of type String.
	 */
	public MessageBuffer () {
		messageList = new ConcurrentLinkedQueue<String>();
	}

	/**
	 * Adds a message to the buffer.
	 * @param message - Takes a Discord4j.core.object.entity.Message
	 */
	public void addMessage(discord4j.core.object.entity.Message message) {
		messageList.add(message.getContent());
	}

	/**
	 * Returns all messages which currently are in the buffer. This is a synchronized method since we want to gurantee
	 * thread safety.
	 * @return - A List of Strings that contain the content of the message
	 */
	public synchronized List<String> getAllMessages(){
		ArrayList<String> retList = new ArrayList<String>();
		Iterator itr = messageList.iterator();
		while(itr.hasNext()){
			retList.add((String)itr.next());
			itr.remove();
		}
		return retList;
	}

	/**
	 * Returns if the collection is empty. Synchronized for thread safety.
	 * @return - if the collection has no items in it.
	 */
	public synchronized boolean isEmpty() {
		return messageList.isEmpty();
	}

	/**
	 * Returns the size of the buffer. Synchronized for thread safety.
	 * @return - The number of elements in the list.
	 */
	public synchronized int getBufferSize(){ return messageList.size(); }


}
