package com.rjoerger.com.rjoerger.maven.discord_connect;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

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
		return "0.0.1";
	}

	@Override
	public void start(Map<String, String> props) {
		// TODO Auto-generated method stub
		token = props.get(DiscordSourceConnector.TOKEN_CONFIG);
		topic = props.get(DiscordSourceConnector.TOPIC_CONFIG);
		buffer = new MessageBuffer();
		messageGen = new DiscordMessages(buffer, token);
	}

	@Override
	public List<SourceRecord> poll() throws InterruptedException {
		ArrayList<SourceRecord> records = new ArrayList<SourceRecord>();
		if(!buffer.isEmpty()) {
			List<String> buffMessage = new ArrayList<String>();
			buffer.getAllMessages(buffMessage);
			for(String msg : buffMessage) {
				SourceRecord rec = new SourceRecord(Collections.singletonMap("Discord", msg), 
						Collections.singletonMap("Message", msg),
						topic,
						null, 
						msg);
				log.info("Message from Discord:\t" + msg);
				records.add(rec);
			}
		} else {
			Thread.sleep(1);
		}
		if (!records.isEmpty()) {
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
