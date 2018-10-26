package cs601.project3.http;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.StringTokenizer;

import cs601.project3.handler.Handler;
import cs601.project3.utils.FileHandler;

public class HttpHelper implements Runnable {

	private Socket socket;

	public HttpHelper(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try(BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
				PrintWriter pw = new PrintWriter(this.socket.getOutputStream());
				BufferedOutputStream bos = new BufferedOutputStream(this.socket.getOutputStream())
				) {

			//Get request
			if(!br.ready()) {
				return;
			}
			HttpRequest request = new HttpRequest();
			this.handleRequestHeader(br, request);

			//Only support GET and POST
			if(!HttpServer.mapping.containsKey(request.getPath())) {
				//TODO: path not found
			} else {
				if(!request.getMethod().equals(HttpMethods.POST) 
						&& !request.getMethod().equals(HttpMethods.GET)) {

				}
				//Handle request body if handle POST
				if(request.getMethod().equals(HttpMethods.POST)) {
					this.handleRequestBody(br, request);
				} else {
					//TODO: method ... not support
				}
				HttpServer.mapping.get(request.getPath()).handle(request, pw, bos);
			}
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private void handleRequestHeader(BufferedReader br, HttpRequest request) throws IOException {
		String firstLine = br.readLine();
		StringTokenizer parse = new StringTokenizer(firstLine);
		request.setMethod(parse.nextToken());
		request.setPath(parse.nextToken());
		request.setProtocol(parse.nextToken());

		System.out.println(firstLine);
		String input;
		while((input = br.readLine())!=null && !input.trim().isEmpty()) {
			request.addHeader(input);
		}
	}

	private void handleRequestBody(BufferedReader in, HttpRequest request) throws IOException {
		int length = Integer.parseInt(request.getHeaders().get("Content-Length"));
		char[] buf = new char[length];
		in.read(buf);
		String body = new String(buf, 0, length);
		request.setBody(body);
	}
}
