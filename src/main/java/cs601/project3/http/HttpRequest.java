package cs601.project3.http;

import java.util.HashMap;

public class HttpRequest {
	private String method;
	private String path;
	private String protocol;
	private String body;
	private String header;
	private HashMap<String, String> headers = new HashMap<String, String>();
	
	public HttpRequest() {
		super();
	}
	
	public HttpRequest(String method, String path, String protocol) {
		super();
		this.method = method;
		this.path = path;
		this.protocol = protocol;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public HashMap<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public void addHeader(String header) {
		String[] keyValue = header.split(":", 2);
		if(keyValue.length == 2) {
			headers.put(keyValue[0].trim(), keyValue[1].trim());
		}
	}
}
