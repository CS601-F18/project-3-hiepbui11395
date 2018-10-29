package cs601.project3.handlerImpl;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;

import cs601.project3.handler.Handler;
import cs601.project3.http.HttpRequest;

public class MethodNotFoundHandler implements Handler {
	private String header = "HTTP/1.0 405 Method Not Allowed\n" + "\r\n";
	private static MethodNotFoundHandler methodNotFound;

	private MethodNotFoundHandler(){}

	public static synchronized MethodNotFoundHandler getInstance(){
		if(methodNotFound == null){
			methodNotFound = new MethodNotFoundHandler();
		}
		return methodNotFound;
	}

	@Override
	public void handle(HttpRequest request, PrintWriter pw, BufferedOutputStream bos) {
		//Send header to client
		pw.write(this.header);
		//Send body to client
		pw.write("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset=\"UTF-8\">\n" + 
				"<title>Method not allowed</title>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"<h1>Method not allowed</h1>\n" +
				"</body>\n" + 
				"</html>");
		pw.close();
	}
}
