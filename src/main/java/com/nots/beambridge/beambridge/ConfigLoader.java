package com.nots.beambridge.beambridge;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigLoader {
	
	// Initialize attributes (global variables) we will use to store config values.
	static int mixerChannelIdJoin;
	static String mixerOauthToken;
	static String mixerClientId;
	static String ircBotName;
	static String ircServer;
	static String ircChannel;
	static String mixerUserJoinMessage;
	static String ircUserJoinMessage;
	static boolean mixerSendGreeting;
	static boolean ircSendGreeting;
	static List<String> mixerBannedWords;
	
	public static void loadConfig(String configFile) throws IOException {
		
		// Load in our config file as a Java properties file.
		Properties prop = new Properties();
		InputStream propFile = new FileInputStream(configFile);
		prop.load(propFile);
		
		// Set our attributes (global variables).
		ConfigLoader.mixerChannelIdJoin = Integer.parseInt(prop.getProperty("mixerChannelIdJoin")); // Convert to int.
		ConfigLoader.mixerOauthToken = prop.getProperty("mixerOauthToken");
		ConfigLoader.mixerClientId = prop.getProperty("mixerClientId");
		ConfigLoader.ircBotName = prop.getProperty("ircBotName");
		ConfigLoader.ircServer = prop.getProperty("ircServer");
		ConfigLoader.ircChannel = prop.getProperty("ircChannel");
		ConfigLoader.mixerUserJoinMessage = prop.getProperty("mixerUserJoinMessage");
		ConfigLoader.ircUserJoinMessage = prop.getProperty("ircUserJoinMessage");
		ConfigLoader.mixerSendGreeting = Boolean.parseBoolean(prop.getProperty("mixerSendGreeting")); // Convert to bool.
		ConfigLoader.ircSendGreeting = Boolean.parseBoolean(prop.getProperty("ircSendGreeting")); // Convert to bool.
		
		// The list of banned words needs to be read in as a string, then converted to a list.
		String mixerBannedWordsString = prop.getProperty("mixerBannedWords");
		ConfigLoader.mixerBannedWords = Arrays.asList(mixerBannedWordsString.split("\\s*,\\s*"));
		
	}
}
