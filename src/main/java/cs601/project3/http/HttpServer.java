package cs601.project3.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs601.project3.handler.Handler;

//Each client connection will work with one different httpServer
public class HttpServer {
	public static final File WEB_ROOT = new File("./src/main/resources/templates");
	public static final String DEFAULT_FILE = "index.html";
	public static final String FILE_NOT_FOUND = "404.html";
	public static final String METHOD_NOT_SUPPORTED = "not_supported.html";
	static HashMap<String, Handler> getMapping;
	static HashMap<String, Handler> postMapping;

	//port to listen connection

	static final boolean verbose = true;

	//Client connection via socket class
	private int port;

	public HttpServer(int  port) {
		getMapping = new HashMap<String, Handler>();
		postMapping = new HashMap<String, Handler>();
		this.port = port;
	}

	public void startup() {
		try(ServerSocket serverSocket = new ServerSocket(this.port)){
			System.out.println("Server started.\nListening for connections on port : " + this.port + " ...\n");
			//listen until user request
			while(true) {
				HttpHelper helper = new HttpHelper(serverSocket.accept());
				if(verbose) {
					System.out.println("\n\nConnection opened. (" + new Date() + ")");
				}
				
				//Create thread to manage client connection
				Thread thread = new Thread(helper);
				thread.start();
			}
		}catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void addMapping(String mapUrl, Handler handler) {
		if(handler.getClass().getName().contains("POST")) {
			HttpServer.postMapping.put(mapUrl, handler);
		} else {
			HttpServer.getMapping.put(mapUrl, handler);
		}
	}

	public static void parseQuery(String query, Map<String, 
			Object> parameters) throws UnsupportedEncodingException {

		if (query != null) {
			String pairs[] = query.split("[&]");
			for (String pair : pairs) {
				String param[] = pair.split("[=]");
				String key = null;
				String value = null;
				if (param.length > 0) {
					key = URLDecoder.decode(param[0], 
							System.getProperty("file.encoding"));
				}

				if (param.length > 1) {
					value = URLDecoder.decode(param[1], 
							System.getProperty("file.encoding"));
				}

				if (parameters.containsKey(key)) {
					Object obj = parameters.get(key);
					if (obj instanceof List<?>) {
						List<String> values = (List<String>) obj;
						values.add(value);

					} else if (obj instanceof String) {
						List<String> values = new ArrayList<String>();
						values.add((String) obj);
						values.add(value);
						parameters.put(key, values);
					}
				} else {
					parameters.put(key, value);
				}
			}
		}
	}
}
