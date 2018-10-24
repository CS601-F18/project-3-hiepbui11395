package cs601.project3.handler;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;

import cs601.project3.http.HttpServer;

public interface Handler {
	public void handle(String target, PrintWriter out, BufferedOutputStream bos);
}
