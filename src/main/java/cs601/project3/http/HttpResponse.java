package cs601.project3.http;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;

public class HttpResponse {
	private int statusCode;
	
	private String message;
	
	private PrintWriter pw;
	
	private BufferedOutputStream bos;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public PrintWriter getPw() {
		return pw;
	}

	public void setPw(PrintWriter pw) {
		this.pw = pw;
	}

	public BufferedOutputStream getBos() {
		return bos;
	}

	public void setBos(BufferedOutputStream bos) {
		this.bos = bos;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
