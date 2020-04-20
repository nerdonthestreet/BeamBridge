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
	static int mixerChannelUserId;
	static String mixerOauthToken;
	static String mixerClientId;
	static String ircBotName;
	static String ircServer;
	static String ircChannel;
	static String mixerUserJoinMessage;
	static String ircUserJoinMessage;
	static String mixerGreetingVisibility;
	static boolean mixerSendGreeting;
	static boolean ircSendGreeting;
	static boolean mixerGreetingCcHost;
	static List<String> mixerBannedWords;
	
	public static void loadConfig(String configFile) throws IOException {
		
		// Load in our config file as a Java properties file.
		Properties prop = new Properties();
		InputStream propFile = new FileInputStream(configFile);
		prop.load(propFile);
		
		// Set our attributes (global variables).
		ConfigLoader.mixerChannelIdJoin = Integer.parseInt(prop.getProperty("mixerChannelIdJoin")); // Convert to int.
		ConfigLoader.mixerChannelUserId = Integer.parseInt(prop.getProperty("mixerChannelUserId")); // Convert to int.
		ConfigLoader.mixerOauthToken = prop.getProperty("mixerOauthToken");
		ConfigLoader.mixerClientId = prop.getProperty("mixerClientId");
		ConfigLoader.ircBotName = prop.getProperty("ircBotName");
		ConfigLoader.ircServer = prop.getProperty("ircServer");
		ConfigLoader.ircChannel = prop.getProperty("ircChannel");
		ConfigLoader.mixerUserJoinMessage = prop.getProperty("mixerUserJoinMessage");
		ConfigLoader.ircUserJoinMessage = prop.getProperty("ircUserJoinMessage");
		ConfigLoader.mixerGreetingVisibility = prop.getProperty("mixerGreetingVisibility");
		ConfigLoader.mixerSendGreeting = Boolean.parseBoolean(prop.getProperty("mixerSendGreeting")); // Convert to bool.
		ConfigLoader.ircSendGreeting = Boolean.parseBoolean(prop.getProperty("ircSendGreeting")); // Convert to bool.
		ConfigLoader.mixerGreetingCcHost = Boolean.parseBoolean(prop.getProperty("mixerGreetingCcHost")); // Convert to bool.
		
		// The list of banned words needs to be read in as a string, then converted to a list.
		String mixerBannedWordsString = prop.getProperty("mixerBannedWords");
		if (mixerBannedWordsString.toCharArray().length != 0) { // Only populate the list if there's at least one banned word configured.
			ConfigLoader.mixerBannedWords = Arrays.asList(mixerBannedWordsString.split("\\s*,\\s*"));
		}
		
	}
}
