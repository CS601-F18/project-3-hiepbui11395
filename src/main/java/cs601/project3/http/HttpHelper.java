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
		String fileRequested = null;
		try(BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
				PrintWriter out = new PrintWriter(this.socket.getOutputStream());
				BufferedOutputStream bos = new BufferedOutputStream(this.socket.getOutputStream())

				) {
			int contentLength = 0;
			//Get the first line of request from client
			if(!in.ready()) {
				return;
			}
			String firstLine = in.readLine();
			System.out.println(firstLine);
			String input;
			while((input = in.readLine())!=null && !input.trim().isEmpty()) {
				if (input.startsWith("Content-Length:")) {
					String cl = input.substring("Content-Length:".length()).trim();
					contentLength = Integer.parseInt(cl);
				}
				System.out.println(input);
			}
			//Parse request with string tokenizer
			StringTokenizer parse = new StringTokenizer(firstLine);
			//Get HTTP method from client
			String method = parse.nextToken().toUpperCase();
			//Get file requested
			fileRequested = parse.nextToken().toLowerCase();

			//Only support GET and POST
			if(method.equals("GET")) {
				this.getMethod(fileRequested, out, bos);
			} else if(method.equals("POST")) {
				char[] buf = new char[contentLength];
				in.read(buf);
				String params = new String(buf, 0, contentLength);
				this.postMethod(fileRequested, params, out, bos);
			} else {
				this.notFoundMethod(method, out, bos);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void notFoundMethod(String method, PrintWriter out, BufferedOutputStream bos) {
		if(HttpServer.verbose) {
			System.out.println("501 Not Implemented :" + method);
		}

		//Return not supported to the client
		File file = new File(HttpServer.WEB_ROOT, HttpServer.METHOD_NOT_SUPPORTED);
		int fileLength = (int) file.length();
		String contentMimeType = "text/html";
		//Read content to return to client
		byte[] fileData = null;
		try {
			fileData = FileHandler.readFileData(file, fileLength);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//Send header with data to client
		out.println("HTTP/1.1 501 Not Implemented");
		out.println("Server: Java HTTP Server 1.0");
		out.println("Date: " + new Date());
		out.println("Content-type: " + contentMimeType);
		out.println("Content-length: " + fileLength);
		out.println();
		out.flush();
		//data
		try {
			bos.write(fileData, 0, fileLength);
			bos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getMethod(String fileRequested, PrintWriter out, BufferedOutputStream bos) throws IOException {

		//GET method
		if(fileRequested.equals("/")) {
			fileRequested += HttpServer.DEFAULT_FILE;
			File file = new File(HttpServer.WEB_ROOT, fileRequested);
			int fileLength = (int) file.length();
			String content = getContentType(fileRequested);
			byte[] fileData = FileHandler.readFileData(file, fileLength);
			// send HTTP Headers
			out.println("HTTP/1.1 200 OK");
			out.println("Server: Java HTTP Server from SSaurel : 1.0");
			out.println("Date: " + new Date());
			out.println("Content-type: " + content);
			out.println(); // blank line between headers and content, very important !
			out.flush(); // flush character output stream buffer

			bos.write(fileData, 0, fileLength);
			bos.flush();
		} else {
			if(HttpServer.getMapping.containsKey(fileRequested)) {
				Handler handler = HttpServer.getMapping.get(fileRequested);
				handler.handle(fileRequested, out, bos);
			}
		}
	}

	private void postMethod(String fileRequested, String params, PrintWriter out, BufferedOutputStream bos) throws UnknownHostException {
		if(HttpServer.getMapping.containsKey(fileRequested)) {
			Handler handler = HttpServer.postMapping.get(fileRequested);
			handler.handle(params, out, bos);
		}
	}

	private String getContentType(String fileRequested) {
		if(fileRequested.endsWith(".htm") || fileRequested.endsWith(".html")) {
			return "text/html";
		} else {
			return "image/png";
		}
	}


}
