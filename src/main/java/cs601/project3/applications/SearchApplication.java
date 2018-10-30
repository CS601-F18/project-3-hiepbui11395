package cs601.project3.applications;

import java.io.IOException;

import cs601.project1.InvertedIndex;
import cs601.project1.ProductList;
import cs601.project3.handlerImpl.FindHandler;
import cs601.project3.handlerImpl.ReviewSearchHandler;
import cs601.project3.http.HttpServer;
import cs601.project3.utils.ConfigurationManager;

public class SearchApplication {

	public static InvertedIndex index = new InvertedIndex();
	public static ProductList products = new ProductList();
	
	public static void main(String[] args) {

		int port = Integer.parseInt(
				ConfigurationManager.getXmlConfiguration("SearchApplication", "Port"));
		
		try {
			cs601.project1.Utils.addToIndex(
					ConfigurationManager.getXmlConfiguration("SearchApplication", "File"), 
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
