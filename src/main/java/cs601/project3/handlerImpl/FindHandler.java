package cs601.project3.handlerImpl;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;

import cs601.project3.handler.Handler;
import cs601.project3.http.HttpServer;

public class FindHandler implements Handler {

	@Override
	public void handle(String target, PrintWriter out, BufferedOutputStream bos) {
		RenderHandler render = new RenderHandler();
		render.handle(target, out, bos);
	}

}
