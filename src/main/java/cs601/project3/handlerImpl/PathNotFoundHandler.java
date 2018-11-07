package cs601.project3.handlerImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cs601.project3.handler.Handler;
import cs601.project3.http.HttpConstant;
import cs601.project3.http.HttpConstantHeader;
import cs601.project3.http.HttpRequest;
import cs601.project3.http.HttpResponse;

public class PathNotFoundHandler implements Handler {
	Logger logger = LogManager.getLogger();
	private static PathNotFoundHandler pathNotFound;

	public static synchronized PathNotFoundHandler getInstance(){
		if(pathNotFound == null){
			pathNotFound = new PathNotFoundHandler();
		}
		return pathNotFound;
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response) {
		//Send header to client
		logger.info(HttpConstantHeader.NOTFOUND_V0);
		response.getPw().write(HttpConstantHeader.NOTFOUND_V0);
		response.getPw().write(HttpConstant.CONNECTIONCLOSE);
		response.getPw().write(System.lineSeparator());

		//Send body to client
		response.getPw().write("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset=\"UTF-8\"/>\n" + 
				"<title>Url not found</title>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"<h1>URL not found</h1>\n" +
				"</body>\n" + 
				"</html>");
		response.getPw().close();

	}
}
