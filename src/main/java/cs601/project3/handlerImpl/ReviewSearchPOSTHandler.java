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
import cs601.project3.http.HttpServer;

public class ReviewSearchPOSTHandler implements Handler {

	@Override
	public void handle(String target, PrintWriter out, BufferedOutputStream bos) {
		String value = "";
		Map<String, Object> parameters = new HashMap<String, Object>();
		try {
			HttpServer.parseQuery(target, parameters);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(parameters.containsKey("query") && parameters.get("query")!=null) {
			value = parameters.get("query").toString();
		}
		System.out.println("\n --- Server --- : Search review:\n");
		ArrayList<Product> products = cs601.project1.Utils.searchByWordApi(value,
				SearchApplication.index, 
				SearchApplication.products);

		//Send body
		StringBuilder body = new StringBuilder( "<html> " + 
				"<head><title>Review Search</title></head>" + 
				"<body>");
		products.forEach(product -> body.append("<h5>" + product.toString() + "\n</h5>"));
		body.append("</body></html>");

		//Send header with data to client
		out.write("HTTP/1.1 200 OK\n" + "\r\n");
		
		//Body
		out.write(body.toString());
		out.flush();
		System.out.println("\n --- Server --- : Finished\n");
	}


}
