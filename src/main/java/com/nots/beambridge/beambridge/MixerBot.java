package com.nots.beambridge.beambridge;

import java.util.concurrent.ExecutionException;

import org.pircbotx.hooks.ListenerAdapter;
import com.mixer.api.MixerAPI;
import com.mixer.api.resource.MixerUser;
import com.mixer.api.resource.chat.MixerChat;
import com.mixer.api.resource.chat.events.IncomingMessageEvent;
import com.mixer.api.resource.chat.events.UserJoinEvent;
import com.mixer.api.resource.chat.methods.AuthenticateMessage;
import com.mixer.api.resource.chat.methods.ChatSendMethod;
import com.mixer.api.resource.chat.methods.WhisperMethod;
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
        
        // This call will get the User object for the host of the channel we're joining.
		MixerUser channelHost = mixer.use(UsersService.class).findOne(ConfigLoader.mixerChannelUserId).get();
		System.out.println("The host of this channel is " + channelHost.username + ".");
        
        // This code will run when the Mixer bot connects.
        if (chatConnectable.connect()) {
            chatConnectable.send(AuthenticateMessage.from(user.channel, user, chat.authkey), new ReplyHandler<AuthenticationReply>() {
                public void onSuccess(AuthenticationReply reply) {
                	// Send Mixer message stating we're connected.
                    chatConnectable.send(ChatSendMethod.of("BeamBridge is connected."));
                    mixerBotGlobal = chatConnectable;
                }
                public void onFailure(Throwable var1) {
                    var1.printStackTrace();
                }
            });
        }
        
        // Only listen for user joins & send greeting if the config option is enabled.
        if (ConfigLoader.mixerSendGreeting == true) {
        	
        	// This code will run when a user joins the Mixer chat & the above config option is "true".
        	System.out.println("Mixer greeting is enabled.");
        	
        	// If greeting visibility is "public", send greeting to the whole chat room.
        	if (ConfigLoader.mixerGreetingVisibility.equals("public")) {
        		System.out.println("Mixer greeting is set to public.");
        		
        		// Event will get called whenever a user joins (and configs are set to true/public).
        		chatConnectable.on(UserJoinEvent.class, event -> {
        			System.out.println("User " + event.data.username + " joined, sending public Mixer greeting...");
        			
        			// Send greeting to the whole chat.
        			chatConnectable.send(ChatSendMethod.of(
        					String.format(ConfigLoader.mixerUserJoinMessage, // Pull in the configured message.
        					event.data.username))); // Substitute the user's name for %s.
        		});
        	}
        	
    		// If greeting visibility is "private", send greeting only to the joining user & the channel host.
        	if (ConfigLoader.mixerGreetingVisibility.contentEquals("private")) {
        		
        		System.out.println("Mixer greeting is set to private.");
        		      		
        		// Event will get called whenever a user joins (and configs are set to true/private).
        		chatConnectable.on(UserJoinEvent.class, event -> {
        			System.out.println("User " + event.data.username + " joined, sending private Mixer greeting...");
        			
        			// Manually replace %s with the user's username.
            		String replacedMixerUserJoinMessage = ConfigLoader.mixerUserJoinMessage.replace("%s", event.data.username);
            		
        			// Send greeting to the user who joined.
        			MixerUser joiningUser = null;
					try {
						// Look up the joining user in Mixer's database.
						joiningUser = mixer.use(UsersService.class).findOne(Integer.parseInt(event.data.id)).get();
					} catch (NumberFormatException | InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// Build & send the whisper.
        			chatConnectable.send(WhisperMethod.builder().to(joiningUser).send(replacedMixerUserJoinMessage).build());
        			System.out.println("Sent whisper to user.");
        			
        			// If configured, also send the greeting to the channel host.
        			// CURRENTLY BROKEN, releasing build for other functionality but will continue working on this.
        			if (ConfigLoader.mixerGreetingCcHost == true) {
        				// Build & send the whisper.
        				chatConnectable.send(WhisperMethod.builder().to(channelHost).send(replacedMixerUserJoinMessage).build());
        				System.out.println("Sent whisper to host.");
        			}
        		});
        	}
        }
        
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
