package cs601.project3.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cs601.project3.http.HttpRequest;

public class HttpUtils {
	public static boolean handleRequestHeader(InputStream in, HttpRequest request) {
		String line = HttpUtils.oneLine(in);

		Logger logger = LogManager.getLogger();
		logger.info("Request: " + line);

		if(line==null) {
			return false;
		}

		StringTokenizer parse = new StringTokenizer(line);
		if(parse.countTokens()!=3) {
			return false;
		}
		request.setMethod(parse.nextToken());
		String path = parse.nextToken();
		if(path.contains("?")) {
			String[] splitPath = path.split("\\?", 2);
			request.setPath(splitPath[0]);
			request.setHeaderQuery(splitPath[1]);
		} else {
			request.setPath(path);
		}
		request.setProtocol(parse.nextToken());

		while((line = HttpUtils.oneLine(in))!=null && !line.trim().isEmpty()) {
			logger.info("\t"+line);
			if(!request.addHeader(line)) {
				logger.info("\tFalse:"+line);
				return false;
			}
		}
		logger.info(System.lineSeparator());
		return true;
	}

	public static void handleRequestBody(InputStream in, HttpRequest request) {
		int length = Integer.parseInt(request.getHeaders().get("content-length"));
		byte[] bytes = new byte[length];
		int read;
		try {
			read = in.read(bytes);
			while(read < length) {
				read += in.read(bytes, read, (bytes.length-read));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String body = new String(bytes);
		request.setBody(body);
	}

	/**
	 * Read a line of bytes until \n character.
	 * @param instream
	 * @return
	 * @throws IOException
	 */
	public static String oneLine(InputStream in) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte b;
		try {
			b = (byte) in.read();
			while(b != '\n' && b!=-1) {
				bout.write(b);
				b = (byte) in.read();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(bout.toByteArray());
	}

	public static Map<String, String> parseQuery(String query){
		Map<String, String> parameters = new HashMap<String, String>();
		if (query != null) {
			String pairs[] = query.split("[&]");
			for (String pair : pairs) {
				String param[] = pair.split("=");
				String key = null;
				String value = null;
				if (param.length > 0) {
					key = param[0];
				}
				if (param.length > 1) {
					value = param[1];
				}
				parameters.put(key, value);
			}
		}
		return parameters;
	}

	public static String httpFetcher(String host, int PORT, String method, String path, String body) {
		StringBuffer buf = new StringBuffer();

		try (
				Socket sock = new Socket(host, PORT); //create a connection to the web server
				OutputStream out = sock.getOutputStream(); //get the output stream from socket
				InputStream instream = sock.getInputStream(); //get the input stream from socket
				//wrap the input stream to make it easier to read from
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream))
				) { 
			String request = null;
			//send request
			request = request(method, host, path, body);

			//			if(method.equals("POST")) {
			//				request = postRequest(host, path, body);
			//			}
			out.write(request.getBytes());
			out.flush();

			//receive response
			//note: a better approach would be to first read headers, determine content length
			//then read the remaining bytes as a byte stream
			String line = reader.readLine();
			while(line != null) {				
				buf.append(line + "\n"); //append the newline stripped by readline
				line = reader.readLine();
			}
			sock.close();
		} catch (IOException e) {
			System.out.println("HTTPFetcher::download " + e.getMessage());
		}
		return buf.toString();
	}

	private static String request(String method, String host, String path, String body) {
		String request = method + " " + path + " HTTP/1.1" + "\n" //GET request
				+ "Host: " + host + "\n" //Host header required for HTTP/1.1
				+ "Connection: close\n" //make sure the server closes the connection after we fetch one page
				+ (body!=null? "Content-Length: " + body.length() + "\n" : "")
				+ "\r\n"
				+ (body!=null?body:"");
		return request;
	}
}
