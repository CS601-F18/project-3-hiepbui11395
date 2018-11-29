package cs601.project3.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cs601.project3.handler.Handler;

//Each client connection will work with one different httpServer
public class HttpServer {
	static HashMap<String, Handler> mapping;

	ExecutorService helpers;

	private volatile boolean running = true;

	//Client connection via socket class
	private int port;

	public HttpServer(int  port) {
		mapping = new HashMap<String, Handler>();
		helpers = Executors.newFixedThreadPool(10);
		this.port = port;
	}

	/**
	 * Run a HttpServer and wait for a request
	 */
	public void startup() {
		Logger logger = LogManager.getLogger();
		try(ServerSocket serverSocket = new ServerSocket(this.port)){
			serverSocket.setSoTimeout(1000);
			logger.info("Server started.\nListening for connections on port : " + this.port + " ...\n");
			System.out.println("Server started.\nListening for connections on port : " + this.port + " ...\n");
			while(running) {
				try {
					//listen until user request
					HttpHelper helper = new HttpHelper(serverSocket.accept());
					logger.info("\n\nConnection opened. (" + new Date() + ")");
					//Create thread to manage client connection
					helpers.execute(helper);
				}catch(SocketTimeoutException ste) {
					if(running) {
						continue;
					} else {
						System.out.println("Server closed!");
					}
				}
			}
			helpers.shutdown();
			try {
				helpers.awaitTermination(5, TimeUnit.MINUTES);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Toggle a flag to shut down http server
	 */
	public void shutdown() {
		running = false;
	}

	/**
	 * Add a specific path to specific handler
	 * @param mapUrl
	 * @param handler
	 */
	public void addMapping(String mapUrl, Handler handler) {
		HttpServer.mapping.put(mapUrl, handler);
	}
}
