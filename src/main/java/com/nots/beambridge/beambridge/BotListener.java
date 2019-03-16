package com.nots.beambridge.beambridge;

import java.io.IOException;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ChannelInfoEvent;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.output.OutputRaw;
import org.pircbotx.PircBotX;
import org.pircbotx.UserHostmask;

import com.mixer.api.resource.chat.methods.AuthenticateMessage;
import com.mixer.api.resource.chat.methods.ChatSendMethod;
import com.mixer.api.resource.chat.replies.AuthenticationReply;
import com.mixer.api.resource.chat.replies.ReplyHandler;

public class BotListener extends ListenerAdapter {

	// We are declaring another PircBotX object,
	// which will end up being a copy of the first one (ircBot) after initialization.
	public static PircBotX ircBot2;
	
	// This code will run when a message is received in IRC.
    public void onMessage(MessageEvent event) throws Exception {
        String message = event.getMessage(); // Get the contents of the message.
        System.out.println("IRC message received: " + message); // Print the message to the console.
        
        // This should only need to happen once, when the IRC channel sends its join message.
        // We will use this event to pull out parameters we can use to send future messages.
        // NOTE: if we have issues with ircBot2 breaking after long periods of time,
        // take this line back out of an "if" statement and let it get set on every message.
        /* if (ircBot2 == null) {
        	ircBot2 = event.getBot(); // Set our second PircBotX object to be a copy of the first one.
        } */
        
        // 
        RelayMessage.myTags = event.getTags();
        
        // Forward the message to Mixer.
        RelayMessage.sendMessageFromIrcToMixer(event.getMessage(), event.getUser().getNick());
    }
    
    // This code will run when a user joins IRC.
    public void onJoin(JoinEvent event) throws Exception {
    	// Send the user a welcome message, set in our config. Replace %u with the user's nick.
    	String LocalJoinMessage = ConfigLoader.ircUserJoinMessage.replace("%u", event.getUser().getNick().toString());
    	String joinedChannel = event.getChannel().getName().toString();
    	event.getBot().send().message(joinedChannel, LocalJoinMessage);
    	
    	// Use the user join event to set/update some properties that our PircBotX object will use to send messages.
    	RelayMessage.myChannel = event.getChannel();
    	RelayMessage.myUser = event.getUser();
    	RelayMessage.myUserHostmask = event.getUserHostmask();
        ircBot2 = event.getBot();
    	
    }

}