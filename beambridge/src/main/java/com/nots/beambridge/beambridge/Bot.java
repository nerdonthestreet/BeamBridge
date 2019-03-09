package com.nots.beambridge.beambridge;

import com.mixer.api.MixerAPI;
import com.mixer.api.resource.MixerUser;
import com.mixer.api.resource.chat.MixerChat;
import com.mixer.api.resource.chat.events.IncomingMessageEvent;
import com.mixer.api.resource.chat.events.UserJoinEvent;
import com.mixer.api.resource.chat.methods.AuthenticateMessage;
import com.mixer.api.resource.chat.methods.ChatSendMethod;
import com.mixer.api.resource.chat.replies.AuthenticationReply;
import com.mixer.api.resource.chat.replies.ReplyHandler;
import com.mixer.api.resource.chat.ws.MixerChatConnectable;
import com.mixer.api.services.impl.ChatService;
import com.mixer.api.services.impl.UsersService;

import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.managers.ListenerManager;
import org.pircbotx.hooks.managers.ThreadedListenerManager;
import org.pircbotx.hooks.types.GenericEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.output.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.nots.beambridge.beambridge.MixerListener;

public class Bot extends ListenerAdapter {
	
	// Declare our PircBotX object (type PircBotX from the PircBotX library).	
	public static PircBotX ircBot;
	
	// Main/startup thread.
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
    	
    	ConfigLoader.loadConfig(args[0]);
    	
    	// Add both our IRC bot and our Mixer bot to the listener so they can communicate through events.
        ListenerManager listenerManager = new ThreadedListenerManager();
        listenerManager.addListener(new BotListener());
        listenerManager.addListener(new MixerListener());
        
        // Start the Mixer bot.
        MixerListener.mixerBot();
       
        // Set up the IRC bot.
        final Configuration configuration = new Configuration.Builder()
                .setName(ConfigLoader.ircBotName) // Sets the nick of our bot using the config value we loaded in.
                .addServer(ConfigLoader.ircServer) // Sets the IRC server our bot will connect to from the config.
                .addAutoJoinChannel(ConfigLoader.ircChannel) // Sets the IRC channel to join from the config.
                .setListenerManager(listenerManager)
                .buildConfiguration();
        try {
        	// Apply the above configuration to our IRC bot.
            final PircBotX ircBot = new PircBotX(configuration);
            // Start the IRC bot.
            ircBot.startBot();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
    }
    
}