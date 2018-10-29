package cs601.project3.handlerImpl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import cs601.project3.handler.Handler;
import cs601.project3.http.HttpMethods;
import cs601.project3.http.HttpRequest;
import cs601.project3.http.HttpServer;

public class ChatHandler implements Handler {

	private String keyName = "message";
	private String header = "HTTP/1.0 200 OK\n" + "\r\n";
	private String postSuccess = "Message had been post to slack!";
	private String postFail = "Error occur, try again!";
	private String apiUrl = "https://slack.com/api/chat.postMessage";
	private String token = "xoxb-378520430422-466222641045-prs118PZ7KdIheVzFvf77m2w";
	private String channel = "hiepbui";

	@Override
	public void handle(HttpRequest request, PrintWriter pw, BufferedOutputStream bos) {
		switch(request.getMethod()) {
		case HttpMethods.GET:
			this.doGet(request, pw);
			break;

		case HttpMethods.POST:
			this.doPost(request, pw);
			break;
		default:
			//TODO: implement method not found/ not available
			break;
		}
	}

	private void doGet(HttpRequest request, PrintWriter pw) {
		//Send header to client
		pw.write(this.header);

		//Send body to client
		pw.write("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset=\"UTF-8\">\n" + 
				"<title>Chat Application</title>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"<h1>Chat Application</h1>\n");
		if(request.getBody()!=null && !request.getBody().isEmpty()) {
			pw.write("<h5>" + request.getBody() + "</h5>");
		}
		pw.write("<form method=\"POST\">\n" + 
				"Keyword:<input type=\"text\" name=\"" + keyName + "\">\n" + 
				"<button type=\"submit\">Submit</button>\n" + 
				"</form>\n" + 
				"</body>\n" + 
				"</html>");
		pw.close();
	}

	private void doPost(HttpRequest request, PrintWriter pw) {
		//Get request body
		String value = "";
		Map<String, Object> parameters = new HashMap<String, Object>();
		try {
			HttpServer.parseQuery(request.getBody(), parameters);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(parameters.containsKey(keyName) && parameters.get(keyName)!=null) {
			value = parameters.get(keyName).toString().toLowerCase();
		}
		//create URL object
		int responseCode = this.sendRequestToSlack(this.apiUrl, this.token, this.channel, value);
		if(responseCode == 200) {
			request.setBody(this.postSuccess);
		} else {
			request.setBody(this.postFail);
		}
		this.doGet(request, pw);
		
		System.out.println("\n --- Server --- : Finished\n");
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
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);

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
