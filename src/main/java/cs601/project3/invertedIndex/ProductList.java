package cs601.project3.invertedIndex;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contain a list of product that already convert into inverted index
 * @author hiepbui
 *
 */
public class ProductList {
	private static ProductList instance;

	private static HashMap<String, ArrayList<Product>> productsByAsin;
	private static HashMap<String, Product> productsByLine;
	
	private ProductList() {}
	
	public static synchronized ProductList getInstance(){
        if(instance == null){
            instance = new ProductList();
            productsByAsin = new HashMap<String, ArrayList<Product>>();
    		productsByLine = new HashMap<String, Product>();
        }
        return instance;
    }
	
	public void addProductToDictionary(Product product, String asin, String locationCode) {
		if(productsByAsin.containsKey(asin)) {
			productsByAsin.get(asin).add(product);
		} else {
			ArrayList<Product> list = new ArrayList<Product>();
			list.add(product);
			productsByAsin.put(asin, list);
		}
		productsByLine.put(locationCode, product);
	}
	
	/**
	 * Input a string(asin) and find out the product Review/Qa have the same asin
	 *
	 * @param  asin  a key to find product Review/Qa
	 * @return ArrayList<Product> a list of product with Review/Qa to print out
	 */
	public ArrayList<Product> getProductByAsin(String asin){
		ArrayList<Product> results = new ArrayList<Product>();
		if(productsByAsin.containsKey(asin)) {
			results = productsByAsin.get(asin);
		}
		return results;
	}
	
	/**
	 * Input a string and find out the Review/Qa that contain that string
	 *
	 * @param  word  a key to find Review/Qa
	 * @return ArrayList<Product> a list of product with Review/Qa
	 */
	public ArrayList<Product> getLineByWordAndSortByFreq(String word, InvertedIndex indexes){
		ArrayList<Product> result = new ArrayList<Product>();
		if(indexes.getIndexes().containsKey(word)) {
			LocationList locationList = indexes.getIndexes().get(word);
			for(Location location : locationList.getListLocation()){
				if(productsByLine.containsKey(location.getLocationCode())) {
					result.add(productsByLine.get(location.getLocationCode()));
				}
			}
		}
		return result;
	}
}
