package com.rjoerger.com.rjoerger.maven.discord_connect;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

public class DiscordSourceConnector extends SourceConnector {

	public static final String TOPIC_CONFIG = "TOPIC";
	public static final String TOKEN_CONFIG = "TOKEN";
	public static final String TASK_BATCH_SIZE_CONFIG = "batch.size";
	
	public static final int DEFAULT_TASK_BATCH_SIZE = 2000;
	
	private static final ConfigDef CONFIG_DEF = new ConfigDef()
			.define(TOPIC_CONFIG, Type.LIST, Importance.HIGH, "Topic to publish messages to")
			.define(TOKEN_CONFIG, Type.STRING, Importance.HIGH, "Token to access Discord")
			.define(TASK_BATCH_SIZE_CONFIG, Type.INT, DEFAULT_TASK_BATCH_SIZE, Importance.LOW, "Max number to read");
	
	private String token;
	//private MessageBuffer buffer;
	private String topic;
	private int batchSize;
	
	@Override
	public String version() {
		return "0.0.1";
	}

	@Override
	public void start(Map<String, String> props) {
		AbstractConfig parsedConfig = new AbstractConfig(CONFIG_DEF, props);
		token = parsedConfig.getString(TOKEN_CONFIG);
		System.out.println(token);
		List<String> topicList = parsedConfig.getList(TOPIC_CONFIG);
		if (topicList.size() != 1){
			throw new ConfigException("'topic' requires only one be defined");
		}
		topic = topicList.get(0);
		batchSize = parsedConfig.getInt(TASK_BATCH_SIZE_CONFIG);
		//buffer = new MessageBuffer();
	}

	@Override
	public Class<? extends Task> taskClass() {
		// TODO Auto-generated method stub
		return DiscordSourceTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(int maxTasks) {
		// TODO Auto-generated method stub
		ArrayList<Map<String, String>> configs = new ArrayList<Map<String, String>>();
		Map<String, String> config = new HashMap<>();
		if (token != null) {
			config.put(TOKEN_CONFIG, token);
		}
		config.put(TOPIC_CONFIG, topic);
		config.put(TASK_BATCH_SIZE_CONFIG, String.valueOf(batchSize));
		configs.add(config);
		return configs;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		// NA, no monitoring for now
	}

	@Override
	public ConfigDef config() {
		// TODO Auto-generated method stub
		return CONFIG_DEF;
	}

}
