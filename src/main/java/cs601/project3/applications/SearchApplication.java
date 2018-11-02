package cs601.project3.applications;

import java.io.IOException;

import cs601.project3.handlerImpl.FindHandler;
import cs601.project3.handlerImpl.ReviewSearchHandler;
import cs601.project3.http.HttpServer;
import cs601.project3.invertedIndex.InvertedIndex;
import cs601.project3.invertedIndex.ProductList;
import cs601.project3.invertedIndex.Utils;
import cs601.project3.utils.ConfigurationManager;

public class SearchApplication {

	public static InvertedIndex reviewIndex = ReviewInvertedIndex.getInstance();
	public static InvertedIndex qaIndex = QaInvertedIndex.getInstance();
	public static ProductList products = new ProductList();
	
	public static void main(String[] args) {

		int port = Integer.parseInt(
				ConfigurationManager.getXmlConfiguration("SearchApplication", "Port"));
		
		try {
			Utils.addToIndex(
					ConfigurationManager.getXmlConfiguration("SearchApplication", "ReviewFile"), 
					SearchApplication.products, 
					SearchApplication.reviewIndex, 
					Utils.TYPE.REVIEW);
			Utils.addToIndex(
					ConfigurationManager.getXmlConfiguration("SearchApplication", "QaFile"), 
					SearchApplication.products, 
					SearchApplication.qaIndex, 
					Utils.TYPE.QA);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HttpServer server = new HttpServer(port);
		server.addMapping("/reviewsearch", new ReviewSearchHandler());
		server.addMapping("/find", new FindHandler());
		server.startup();
	}
}
