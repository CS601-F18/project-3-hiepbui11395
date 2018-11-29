package cs601.project3.handler;

import cs601.project3.http.HttpRequest;
import cs601.project3.http.HttpResponse;

/**
 * 
 * @author hiepbui
 * The handler interface which will be implement by sub handler for specific purpose
 * 
 */
public interface Handler {
	
	public void handle(HttpRequest request, HttpResponse response);
}
