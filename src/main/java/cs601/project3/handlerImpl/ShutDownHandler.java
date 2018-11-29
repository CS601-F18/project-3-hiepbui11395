package cs601.project3.handlerImpl;

import cs601.project3.handler.Handler;
import cs601.project3.http.HttpConstant;
import cs601.project3.http.HttpConstantHeader;
import cs601.project3.http.HttpRequest;
import cs601.project3.http.HttpResponse;
import cs601.project3.http.HttpServer;

/**
 * Shut down the web application
 * @author hiepbui
 *
 */
public class ShutDownHandler implements Handler {
	private HttpServer server;

	public ShutDownHandler(HttpServer server){
		this.server = server;
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response) {
		//Send header to client
		response.getPw().write(HttpConstantHeader.OK_V0);
		response.getPw().write(HttpConstant.CONNECTIONCLOSE);
		response.getPw().write(System.lineSeparator());
		//Send body to client
		response.getPw().write("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset=\"UTF-8\">\n" + 
				"<title>Shutdown</title>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"<h1>Shutdown server</h1>\n" +
				"</body>\n" + 
				"</html>");
		response.getPw().close();
		server.shutdown();
	}
}
