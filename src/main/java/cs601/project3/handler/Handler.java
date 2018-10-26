package cs601.project3.handler;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;

import cs601.project3.http.HttpRequest;

public interface Handler {
	
	public void handle(HttpRequest request, PrintWriter pw, BufferedOutputStream bos);
}
