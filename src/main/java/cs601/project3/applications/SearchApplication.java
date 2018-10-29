package cs601.project3.applications;

import java.io.IOException;

import cs601.project1.InvertedIndex;
import cs601.project1.ProductList;
import cs601.project3.handlerImpl.FindHandler;
import cs601.project3.handlerImpl.ReviewSearchHandler;
import cs601.project3.http.HttpServer;

public class SearchApplication {

	public static InvertedIndex index = new InvertedIndex();
	public static ProductList products = new ProductList();
	
	public static void main(String[] args) {
		int port = 8080;
		
		try {
			cs601.project1.Utils.addToIndex("reviews_Cell_Phones_and_Accessories_5.json", 
					SearchApplication.products, 
					SearchApplication.index, 
					cs601.project1.Utils.TYPE.REVIEW);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		HttpServer server = new HttpServer(port);
		server.addMapping("/reviewsearch", new ReviewSearchHandler());
		server.addMapping("/find", new FindHandler());
		server.startup();
	}
}
