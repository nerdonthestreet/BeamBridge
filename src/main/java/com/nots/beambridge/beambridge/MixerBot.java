package com.nots.beambridge.beambridge;

import java.util.concurrent.ExecutionException;

import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.output.OutputRaw;

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

public class MixerBot extends ListenerAdapter {
	
	public static MixerChatConnectable mixerBotGlobal;
    
	public static void mixerBot() throws InterruptedException, ExecutionException {
		
		// These Mixer API keys will be used to authenticate the Mixer account the bot will run as.
		MixerAPI mixer = new MixerAPI(ConfigLoader.mixerOauthToken, ConfigLoader.mixerClientId);
		
		// These calls will determine the channel that our Mixer account joins. 
        MixerUser user = mixer.use(UsersService.class).getCurrent().get();
        user.channel.id = ConfigLoader.mixerChannelIdJoin;
        MixerChat chat = mixer.use(ChatService.class).findOne(user.channel.id).get();
        MixerChatConnectable chatConnectable = chat.connectable(mixer);
        
        // This code will run when the Mixer bot connects.
        if (chatConnectable.connect()) {
            chatConnectable.send(AuthenticateMessage.from(user.channel, user, chat.authkey), new ReplyHandler<AuthenticationReply>() {
                public void onSuccess(AuthenticationReply reply) {
                    chatConnectable.send(ChatSendMethod.of("BeamBridge is connected."));
                    mixerBotGlobal = chatConnectable;
                }
                public void onFailure(Throwable var1) {
                    var1.printStackTrace();
                }
            });
        }
        
        // This code will run when a user joins the Mixer chat.
        chatConnectable.on(UserJoinEvent.class, event -> {
            chatConnectable.send(ChatSendMethod.of(
                    String.format(ConfigLoader.mixerUserJoinMessage, // Pull in the configured message.
                    event.data.username))); // Substitute the user's name for %s.
        });
        
        // This code will run when a message is received on Mixer.
        chatConnectable.on(IncomingMessageEvent.class, event -> {
        	
        	// The below section is unneeded, but if we wanted to add Mixer commands, this is how we'd do it.
            /* if (event.data.message.message.get(0).text.startsWith("!ping")) {
                chatConnectable.send(ChatSendMethod.of(String.format("@%s PONG!",event.data.userName)));
            } */
        	
        	// Assign the Mixer data to variables.
            String messageFromMixer = event.data.message.message.get(0).text;
            String sendingMixerUser = event.data.userName;
        	System.out.println("Mixer message received: " + messageFromMixer); // Print message from Mixer to console.
            
        	// Only forward the message if it does not start with an opening bracket (this avoids echo/loops).
        	//  We also make sure the message is not our own greeting message to new chatters-- PircBotX does this
        	//  automatically in IRC, so we do it on Mixer for consistency.
            if ((messageFromMixer.charAt(0) != '[') && (messageFromMixer.startsWith("Welcome to the stream,") == false)) {
              try {
            	  // Forward the message to IRC.
				  RelayMessage.sendMessageFromMixerToIrc(messageFromMixer, sendingMixerUser);
			  } catch (Exception e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  }
            }
        });
	}
}