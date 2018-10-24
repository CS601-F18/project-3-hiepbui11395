package cs601.project3.handlerImpl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import cs601.project1.Utils;
import cs601.project3.handler.Handler;
import cs601.project3.http.HttpServer;
import cs601.project3.utils.FileHandler;

public class ReviewSearchHandler implements Handler {

	@Override
	public void handle(String target, PrintWriter out, BufferedOutputStream bos) {
		RenderHandler render = new RenderHandler();
		render.handle(target, out, bos);
	}

}
