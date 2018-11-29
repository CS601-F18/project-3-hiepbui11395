package cs601.project3.http;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cs601.project3.handlerImpl.MethodNotFoundHandler;
import cs601.project3.handlerImpl.PathNotFoundHandler;
import cs601.project3.utils.HttpUtils;

/**
 * A helper class to support the HttpServer handle a request
 * @author hiepbui
 *
 */
public class HttpHelper implements Runnable {
	Logger logger = LogManager.getLogger();
	private Socket socket;

	public HttpHelper(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Run a http helper after receive a request and parse the request
	 */
	@Override
	public void run() {
		try(
				InputStream in = this.socket.getInputStream();
				PrintWriter pw = new PrintWriter(this.socket.getOutputStream());
				BufferedOutputStream bos = new BufferedOutputStream(this.socket.getOutputStream())
				) {

			HttpRequest request = new HttpRequest();
			boolean validRequest = HttpUtils.handleRequestHeader(in, request);
			if(!validRequest) {
				logger.info(HttpConstantHeader.BADREQUEST_V0);
				pw.write(HttpConstantHeader.BADREQUEST_V0);
				pw.write(System.lineSeparator());
				this.socket.close();
				return;
			} else {

				HttpResponse response = new HttpResponse();
				response.setPw(pw);
				response.setBos(bos);
				//Only support GET and POST
				if(!request.getMethod().equals("GET") && !request.getMethod().equals("POST")) {
					MethodNotFoundHandler methodNotFound = MethodNotFoundHandler.getInstance();
					methodNotFound.handle(request, response);
				}
				else if(!HttpServer.mapping.containsKey(request.getPath())) {
					PathNotFoundHandler pathNotFound = PathNotFoundHandler.getInstance();
					pathNotFound.handle(request, response);
				} else {
					//Handle request body if handle POST
					if(request.getMethod().equals(HttpConstant.POST)) {
						HttpUtils.handleRequestBody(in, request);
					}
					HttpServer.mapping.get(request.getPath()).handle(request, response);
				}

			}
			this.socket.close();
			logger.info("-----------------End Http Helper------------------\n\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
