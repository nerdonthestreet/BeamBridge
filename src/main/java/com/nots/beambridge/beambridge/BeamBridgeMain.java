package com.nots.beambridge.beambridge;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.managers.ListenerManager;
import org.pircbotx.hooks.managers.ThreadedListenerManager;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.nots.beambridge.beambridge.MixerBot;

public class BeamBridgeMain extends ListenerAdapter {
	
	// Declare our PircBotX object (type PircBotX from the PircBotX library).	
	public static PircBotX ircBot;
	
	// Main/startup thread.
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
    	
    	ConfigLoader.loadConfig(args[0]);
    	
    	// Add both our IRC bot and our Mixer bot to the listener so they can communicate through events.
        ListenerManager listenerManager = new ThreadedListenerManager();
        listenerManager.addListener(new IrcBot());
        listenerManager.addListener(new MixerBot());
        
        // Start the Mixer bot.
        MixerBot.mixerBot();
       
        // Set up the IRC bot.
        final Configuration configuration = new Configuration.Builder()
                .setName(ConfigLoader.ircBotName) // Sets the nick of our bot using the config value we loaded in.
                .addServer(ConfigLoader.ircServer) // Sets the IRC server our bot will connect to from the config.
                .addAutoJoinChannel(ConfigLoader.ircChannel) // Sets the IRC channel to join from the config.
                .setListenerManager(listenerManager)
                .buildConfiguration();
        try {
        	// Apply the above configuration to our IRC bot.
            @SuppressWarnings("resource")
			final PircBotX ircBot = new PircBotX(configuration);
            // Start the IRC bot.
            ircBot.startBot();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
    }
    
}