# BeamBridge

BeamBridge is a Java program that provides a bridge between a Mixer chat room and an IRC channel. It was written by Jacob Kauffmann at Nerd on the Street, who could not find a pre-existing solution for bridging chat from Mixer to other platforms.

## Configuration

Some pieces are still hard-coded into the build for now (such as the channel join message for IRC), but the majority of configuration has been moved to an easy-to-use configuration file. You can find an example file with all possible settings in this repository, called "BeamBridge-properties.config". The configuration file is heavily commented with explanations of each option.

## Usage

Once you have filled out the configuration file, simply run the JAR file (after building BeamBridge, if necessary) with the following syntax:

'java -jar ./BeamBridge-Build007-public.jar ./BeamBridge-properties.config'

As seen above, you just need to run the JAR file in Java, and pass in the config file's location as the first and only argument.

## Browing the code

This Git repo is an Eclipse Java project (you can clone the repo and open the root folder in Eclipse as a project.) The actual Java source files are located at /src/main/java/com/nots/beambridge/beambridge/*.java. The Maven configuration file (pom.xml) will automatically include dependencies, including PircBotX (licensed under GPLv3) and the Mixer API (licensed under MIT/Expat.)

## Prebuilt releases

While the source code for this program is mirrored on both GitLab and GitHub, releases are only published on GitHub (because GitHub's release system is easier to use and provides free hosting for the built files.) You can browse and download releases here: [https://github.com/jacobgkau/BeamBridge/releases](https://github.com/jacobgkau/BeamBridge/releases)

In the event that we have issues with GitHub's release system, I may begin offering builds hosted on NOTS infrastructure as well. Contact me at [jacob@nerdonthestreet.com](mailto:jacob@nerdonthestreet.com) if you see a need for this.

## Supporting development

If you find this bot useful, you can support development by joining the Nerd Club at [https://nerdclub.nots.co](https://nerdclub.nots.co).

## License

BeamBridge is licensed under the terms of the GNU GPLv3. The full text of this license is included in the LICENSE file.

BeamBridge depends on [PircBotX](https://github.com/pircbotx/pircbotx), which is also licensed under the GPLv3.

BeamBridge also depends on the [Mixer Java API](https://github.com/mixer/beam-client-java), which is licensed under the MIT License.