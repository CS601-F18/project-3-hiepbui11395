package cs601.project3.handlerImpl;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;

import cs601.project3.handler.Handler;
import cs601.project3.http.HttpRequest;

public class PathNotFoundHandler implements Handler {
	private String header = "HTTP/1.0 404 NOT FOUND\n" + "\r\n";
	private static PathNotFoundHandler pathNotFound;

	public static synchronized PathNotFoundHandler getInstance(){
		if(pathNotFound == null){
			pathNotFound = new PathNotFoundHandler();
		}
		return pathNotFound;
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
				"<title>Url not found</title>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"<h1>URL not found</h1>\n" +
				"</body>\n" + 
				"</html>");
		pw.close();

	}
}
