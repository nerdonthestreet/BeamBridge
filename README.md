# BeamBridge

BeamBridge is a Java program that provides a bridge between a Mixer chat room and an IRC channel. It was written by Jacob Kauffmann at Nerd on the Street, who could not find a pre-existing solution for bridging chat from Mixer to other platforms.

## Configuration

Some pieces are still hard-coded into the build for now (such as the channel join message for IRC), but the majority of configuration has been moved to an easy-to-use configuration file. You can find an example file with all possible settings in this repository, called "BeamBridge-properties.config". The configuration file is heavily commented with explanations of each option.

## Usage

Once you have filled out the configuration file, simply run the Jar file (after building BeamBridge, if necessary) with the following syntax:

'java -jar ./BeamBridge-Build007-public.jar ./BeamBridge-properties.config'

As seen above, you just need to run the Jar file in Java, and pass in the config file's location as the first and only argument.

## Browing the code

This Git repo is an Eclipse Java project (you can clone the repo and open the root folder in Eclipse as a project.) The actual Java source files are located at /src/main/java/com/nots/beambridge/beambridge/*.java. The Maven configuration file (pom.xml) will automatically include dependencies, including PircBotX (licensed under GPLv3) and the Mixer API (licensed under MIT/Expat.)