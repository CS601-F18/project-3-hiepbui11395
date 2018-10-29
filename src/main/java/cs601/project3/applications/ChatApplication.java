package cs601.project3.applications;

import cs601.project3.handlerImpl.ChatHandler;
import cs601.project3.handlerImpl.FindHandler;
import cs601.project3.handlerImpl.ReviewSearchHandler;
import cs601.project3.http.HttpServer;

public class ChatApplication {
	public static void main(String[] args) {
		int port = 9000;
		HttpServer server = new HttpServer(port);
		server.addMapping("/slackbot", new ChatHandler());
		server.startup();
	}
}
