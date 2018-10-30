package cs601.project3.handler;

import cs601.project3.http.HttpRequest;
import cs601.project3.http.HttpResponse;

public interface Handler {
	
	public void handle(HttpRequest request, HttpResponse response);
}
