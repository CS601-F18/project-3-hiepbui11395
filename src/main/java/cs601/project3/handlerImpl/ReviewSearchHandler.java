package cs601.project3.handlerImpl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cs601.project1.Product;
import cs601.project3.applications.SearchApplication;
import cs601.project3.handler.Handler;
import cs601.project3.http.HttpConstantHeader;
import cs601.project3.http.HttpMethods;
import cs601.project3.http.HttpRequest;
import cs601.project3.http.HttpResponse;
import cs601.project3.utils.HttpUtils;

public class ReviewSearchHandler implements Handler {
	private String keyName = "query";
	@Override
	public void handle(HttpRequest request, HttpResponse response) {
		switch(request.getMethod()) {
		case HttpMethods.GET:
			this.doGet(request, response);
			break;

		case HttpMethods.POST:
			this.doPost(request, response);
			break;
		default:
			MethodNotFoundHandler methodNotFound = MethodNotFoundHandler.getInstance();
			methodNotFound.handle(request, response);
			break;
		}
	}

	private void doGet(HttpRequest request, HttpResponse response) {
		//Send header to client
		response.getPw().write(HttpConstantHeader.OK_V0);
		response.getPw().write(System.lineSeparator());
		
		//Send body to client
		response.getPw().write("<!DOCTYPE html>\n" + 
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
		response.getPw().close();
	}

	private void doPost(HttpRequest request, HttpResponse response) {
		
		//Get request body
		String value = "";
		Map<String, Object> parameters = new HashMap<String, Object>();
		try {
			HttpUtils.parseQuery(request.getBody(), parameters);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(parameters.containsKey(keyName) && parameters.get(keyName)!=null) {
			value = parameters.get(keyName).toString();
		}
		System.out.println("\n --- Server --- : Search review:\n");
		ArrayList<Product> products = cs601.project1.Utils.searchByWordApi(value,
				SearchApplication.index, 
				SearchApplication.products);
		
		StringBuilder body = new StringBuilder("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset=\"UTF-8\">\n" + 
				"<title>Review Search Result</title>\n" + 
				"</head>\n" + 
				"<body>\n" +
				"<h1>Result for: " + value + "</h1>");
		products.forEach(product -> body.append("<h5>" + product.toString() + "\n</h5>"));
		body.append("</body></html>");

		//Send header to client
		response.getPw().write(HttpConstantHeader.OK_V0);
		response.getPw().write(System.lineSeparator());

		//Send body to client
		response.getPw().write(body.toString());
		response.getPw().close();
		System.out.println("\n --- Server --- : Finished\n");
	}

}
