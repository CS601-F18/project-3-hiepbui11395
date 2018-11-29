package cs601.project3.applications;

import cs601.project3.handlerImpl.ChatHandler;
import cs601.project3.http.HttpServer;
import cs601.project3.utils.ConfigurationManager;

/**
 * 
 * @author hiepbui
 * Main class for ChatApplication
 * Add Chat Handler
 *
 */
public class ChatApplication {
	public static void main(String[] args) {
		int port = Integer.parseInt(
				ConfigurationManager.getXmlConfiguration("ChatApplication", "Port"));
		HttpServer server = new HttpServer(port);
		server.addMapping("/slackbot", new ChatHandler());
		server.startup();
	}
}
