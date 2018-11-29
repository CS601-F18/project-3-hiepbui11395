package cs601.project3.handlerImpl;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

import cs601.project3.handler.Handler;
import cs601.project3.http.HttpConstant;
import cs601.project3.http.HttpConstantHeader;
import cs601.project3.http.HttpRequest;
import cs601.project3.http.HttpResponse;
import cs601.project3.utils.ConfigurationManager;
import cs601.project3.utils.HttpUtils;

/**
 * 
 * @author hiepbui
 * Chat Handler class, handle GET and POSTmethod
 *
 */
public class ChatHandler implements Handler {
	Logger logger = LogManager.getLogger();

	private String keyName = "message";
	private String postSuccess = "Message had been post to slack!";
	private String postFail = "Error occur, try again!";
	private String apiUrl = ConfigurationManager.getXmlConfiguration("ChatApplication", "Api");
	private String token = ConfigurationManager.getXmlConfiguration("ChatApplication", "Token");
	private String channel = ConfigurationManager.getXmlConfiguration("ChatApplication", "Channel");

	@Override
	public void handle(HttpRequest request, HttpResponse response) {
		switch(request.getMethod()) {
		case HttpConstant.GET:
			this.doGet(request, response);
			break;

		case HttpConstant.POST:
			this.doPost(request, response);
			break;
		default:
			MethodNotFoundHandler methodNotFound = MethodNotFoundHandler.getInstance();
			methodNotFound.handle(request, response);
			break;
		}
	}
	
	/**
	 * Handle a GET method and return a html page to send message
	 * @param request
	 * @param response
	 */
	private void doGet(HttpRequest request, HttpResponse response) {
		StringBuilder body = new StringBuilder("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset=\"UTF-8\"/>\n" + 
				"<title>Chat Application</title>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"<h1>Chat Application</h1>\n");
		if(response.getMessage()!=null && !response.getMessage().isEmpty()) {
			body.append("<h5>" + response.getMessage() + "</h5>\n");
		}
		body.append("<form method=\"POST\">\n" + 
				"Keyword:<input required='required' type=\"text\" name=\"" + keyName + "\"/>\n" + 
				"<button type=\"submit\">Submit</button>\n" + 
				"</form>\n" + 
				"</body>\n" + 
				"</html>");
		
		//Send header to client
		logger.info(HttpConstantHeader.OK_V0);
		response.getPw().write(HttpConstantHeader.OK_V0);
		response.getPw().write(HttpConstant.CONNECTIONCLOSE);
		response.getPw().write("Content-Length: " + body.length() + "\n");
		response.getPw().write(System.lineSeparator());

		//Send body to client
		response.getPw().write(body.toString());
		response.getPw().close();
	}

	/**
	 * Handle a POSTmethod and send request api to Slack api
	 * @param request
	 * @param response
	 */
	private void doPost(HttpRequest request, HttpResponse response) {
		//Get request body
		String value = "";
		Map<String, String> parameters = HttpUtils.parseQuery(request.getBody());
		if(parameters.containsKey(keyName) && parameters.get(keyName)!=null) {
			value = parameters.get(keyName).toString();
		} else {
			response.setMessage("Invalid parameters! Try again!\n");
			this.doGet(request, response);
			return;
		}
		//create URL object
		JsonObject json = HttpUtils.callApi(this.apiUrl, this.token, this.channel, value);
		if(json.get("ok").getAsBoolean()) {
			response.setMessage(this.postSuccess);
		} else {
			response.setMessage(this.postFail);
		}
		this.doGet(request, response);
		
		logger.info("\n --- Server --- : Finished\n");
	}


	
}
