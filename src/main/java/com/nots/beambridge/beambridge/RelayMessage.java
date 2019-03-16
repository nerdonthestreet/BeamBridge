package com.nots.beambridge.beambridge;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UserHostmask;
import org.pircbotx.Utils;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.*;
import org.pircbotx.Utils.*;
import org.pircbotx.UserHostmask;

import com.google.common.collect.ImmutableMap;
import com.mixer.api.resource.chat.methods.ChatSendMethod;

public class RelayMessage {
	public static String MessageFromMixerGlobal;
	public static String SendingMixerUserGlobal;
	public static Channel myChannel;
	public static User myUser;
	public static UserHostmask myUserHostmask;
	public static ImmutableMap<String, String> myTags;
	public static String MessageFromIrcGlobal;
	public static String IrcSenderGlobal;
	
	public static void sendMessageFromMixerToIrc(String RawMixerMessage, String SendingMixerUser) throws Exception {
		System.out.println("Sending from Mixer to IRC...");
		RelayMessage.MessageFromMixerGlobal = RawMixerMessage;
		RelayMessage.SendingMixerUserGlobal = SendingMixerUser;
		String message = "[" + SendingMixerUser + "]" + " " + RawMixerMessage;		
		
		IrcBot.ircBot2.send().message("#mikecast", message);
		

	}
	
	public static void sendMessageFromIrcToMixer(String RawIrcMessage, String SendingIrcUser) throws Exception {
		
		// Filter out any banned words first. 
		for (String bannedMixerWord : ConfigLoader.mixerBannedWords) {
			RawIrcMessage = RawIrcMessage.replaceAll(bannedMixerWord, "<filtered>");
		}
		
		// Forward the filtered message to Mixer.
		System.out.println("Sending message from IRC to Mixer...");
		RelayMessage.MessageFromIrcGlobal = RawIrcMessage;
		RelayMessage.IrcSenderGlobal = SendingIrcUser;
		
		String message = "[" + SendingIrcUser + "]" + " " + RawIrcMessage;
		
		MixerBot.mixerBotGlobal.send(ChatSendMethod.of(message));
	}
}
