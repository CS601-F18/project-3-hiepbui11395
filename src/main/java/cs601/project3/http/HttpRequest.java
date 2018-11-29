package cs601.project3.http;

import java.util.HashMap;

/**
 * Contain the method, path, protocol, header, body of each request
 * @author hiepbui
 *
 */
public class HttpRequest {
	private String method;
	private String path;
	private String protocol;
	private String headerQuery;
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

	public boolean addHeader(String header) {
		String[] keyValue = header.split(":", 2);
		if(keyValue.length == 2) {
			if(HttpConstant.HEADERKEY.contains(keyValue[0].trim().toLowerCase())) {
				headers.put(keyValue[0].trim().toLowerCase(), keyValue[1].trim());
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	public String getHeaderQuery() {
		return headerQuery;
	}

	public void setHeaderQuery(String headerQuery) {
		this.headerQuery = headerQuery;
	}

}
