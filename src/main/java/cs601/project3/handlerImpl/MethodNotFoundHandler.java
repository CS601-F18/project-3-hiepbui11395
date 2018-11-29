package cs601.project3.handlerImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cs601.project3.handler.Handler;
import cs601.project3.http.HttpConstant;
import cs601.project3.http.HttpConstantHeader;
import cs601.project3.http.HttpRequest;
import cs601.project3.http.HttpResponse;

/**
 * A handler to return 405 Method Not Found page
 * @author hiepbui
 *
 */
public class MethodNotFoundHandler implements Handler {
	Logger logger = LogManager.getLogger();
	private static MethodNotFoundHandler methodNotFound;

	private MethodNotFoundHandler(){}

	public static synchronized MethodNotFoundHandler getInstance(){
		if(methodNotFound == null){
			methodNotFound = new MethodNotFoundHandler();
		}
		return methodNotFound;
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response) {
		//Send header to client
		logger.info(HttpConstantHeader.METHODNOTALLOWED_V0);
		response.getPw().write(HttpConstantHeader.METHODNOTALLOWED_V0);
		response.getPw().write(HttpConstant.CONNECTIONCLOSE);
		response.getPw().write(System.lineSeparator());
		//Send body to client
		response.getPw().write("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset=\"UTF-8\"/>\n" + 
				"<title>Method not allowed</title>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"<h1>Method not allowed</h1>\n" +
				"</body>\n" + 
				"</html>");
		response.getPw().close();
	}
}
