package cs601.project3.handlerImpl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cs601.project3.handler.Handler;
import cs601.project3.http.HttpConstant;
import cs601.project3.http.HttpConstantHeader;
import cs601.project3.http.HttpRequest;
import cs601.project3.http.HttpResponse;
import cs601.project3.utils.ConfigurationManager;
import cs601.project3.utils.HttpUtils;

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

	private void doGet(HttpRequest request, HttpResponse response) {
		StringBuilder body = new StringBuilder("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset=\"UTF-8\">\n" + 
				"<title>Chat Application</title>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"<h1>Chat Application</h1>\n");
		if(response.getMessage()!=null && !response.getMessage().isEmpty()) {
			body.append("<h5>" + response.getMessage() + "</h5>");
		}
		body.append("<form method=\"POST\">\n" + 
				"Keyword:<input required type=\"text\" name=\"" + keyName + "\">\n" + 
				"<button type=\"submit\">Submit</button>\n" + 
				"</form>\n" + 
				"</body>\n" + 
				"</html>");
		
		//Send header to client
		response.getPw().write(HttpConstantHeader.OK_V0);
		response.getPw().write(HttpConstant.CONNECTIONCLOSE);
		response.getPw().write("Content-Length: " + body.length() + "\n");
		response.getPw().write(System.lineSeparator());

		//Send body to client
		response.getPw().write(body.toString());
		response.getPw().close();
	}

	private void doPost(HttpRequest request, HttpResponse response) {
		//Get request body
		String value = "";
		Map<String, String> parameters = HttpUtils.parseQuery(request.getBody());
		if(parameters.containsKey(keyName) && parameters.get(keyName)!=null) {
			value = parameters.get(keyName).toString();
		}
		//create URL object
		int responseCode = this.sendRequestToSlack(this.apiUrl, this.token, this.channel, value);
		if(responseCode == 200) {
			response.setMessage(this.postSuccess);
		} else {
			response.setMessage(this.postFail);
		}
		response.setStatusCode(responseCode);
		this.doGet(request, response);
		
		logger.info("\n --- Server --- : Finished\n");
	}


	private int sendRequestToSlack(String apiUrl, String token, String channel, String text) {
		URL url;
		int responseCode = 0;
		try {
			url = new URL(apiUrl);
			//create secure connection 
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			String urlParameters = "token=" + token
					+ "&channel=" + channel
					+ "&text=" + text;

			// Send post request
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			responseCode = connection.getResponseCode();
			logger.info("\nSending 'POST' request to URL : " + url);
			logger.info("Post parameters : " + urlParameters);
			logger.info("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			String inputLine;

			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseCode;
	}
}
