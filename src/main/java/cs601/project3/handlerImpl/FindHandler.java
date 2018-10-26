package cs601.project3.handlerImpl;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cs601.project1.Product;
import cs601.project3.SearchApplication;
import cs601.project3.handler.Handler;
import cs601.project3.http.HttpMethods;
import cs601.project3.http.HttpRequest;
import cs601.project3.http.HttpServer;

public class FindHandler implements Handler {
	private String keyName = "asin";
	private String header = "HTTP/1.0 200 OK\n" + "\r\n";

	@Override
	public void handle(HttpRequest request, PrintWriter pw, BufferedOutputStream bos) {
		switch(request.getMethod()) {
		case HttpMethods.GET:
			this.doGet(pw);
			break;

		case HttpMethods.POST:
			this.doPost(request.getBody(), pw);
			break;
		default:
			//TODO: implement method not found/ not available
			break;
		}
	}

	private void doGet(PrintWriter pw) {
		//Send header to client
		pw.write(this.header);
		
		//Send body to client
		pw.write("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset=\"UTF-8\">\n" + 
				"<title>Review Search</title>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"<h1>Review search</h1>\n" + 
				"<form method=\"POST\">\n" + 
				"Keyword:<input type=\"text\" name=\"" + keyName + "\">\n" + 
				"<button type=\"submit\">Submit</button>\n" + 
				"</form>\n" + 
				"</body>\n" + 
				"</html>");
		pw.close();
	}

	private void doPost(String requestBody, PrintWriter pw) {
		
		//Create response body
		String value = "";
		Map<String, Object> parameters = new HashMap<String, Object>();
		try {
			HttpServer.parseQuery(requestBody, parameters);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(parameters.containsKey(keyName) && parameters.get(keyName)!=null) {
			value = parameters.get(keyName).toString().toLowerCase();
		}
		System.out.println("\n --- Server --- : Search review:\n");
		ArrayList<Product> products = cs601.project1.Utils.findApi(value,
				SearchApplication.products);
		
		StringBuilder body = new StringBuilder("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset=\"UTF-8\">\n" + 
				"<title>Products</title>\n" + 
				"</head>\n" + 
				"<body>\n" +
				"<h1>Result for: " + value + "</h1>");
		products.forEach(product -> body.append("<h5>" + product.toString() + "\n</h5>"));
		body.append("</body></html>");

		//Send header to client
		pw.write(this.header);

		//Send body to client
		pw.write(body.toString());
		pw.close();
		System.out.println("\n --- Server --- : Finished\n");
	}

}
