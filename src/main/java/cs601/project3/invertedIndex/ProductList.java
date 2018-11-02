package cs601.project3.invertedIndex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

public class ProductList {

	private HashMap<String, ArrayList<Product>> productsByAsin;
	private HashMap<String, Product> productsByLine;
	public ProductList(HashMap<String, ArrayList<Product>> productsByAsin, HashMap<String, Product> productsByLine) {
		super();
		this.productsByAsin = productsByAsin;
		this.productsByLine = productsByLine;
	}
	public ProductList() {
		this.productsByAsin = new HashMap<String, ArrayList<Product>>();
		this.productsByLine = new HashMap<String, Product>();
	}
	public HashMap<String, ArrayList<Product>> getProductsByAsin() {
		return productsByAsin;
	}
	public void setProductsByAsin(HashMap<String, ArrayList<Product>> productsByAsin) {
		this.productsByAsin = productsByAsin;
	}
	public HashMap<String, Product> getProductsByLine() {
		return productsByLine;
	}
	public void setProductsByLine(HashMap<String, Product> productsByLine) {
		this.productsByLine = productsByLine;
	}
	
	public void addProductToDictionary(Product product, String asin, String locationCode) {
		if(this.getProductsByAsin().containsKey(asin)) {
			this.getProductsByAsin().get(asin).add(product);
		} else {
			ArrayList<Product> list = new ArrayList<Product>();
			list.add(product);
			this.getProductsByAsin().put(asin, list);
		}
		this.getProductsByLine().put(locationCode, product);
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
				if(this.getProductsByLine().containsKey(location.getLocationCode())) {
					result.add(this.getProductsByLine().get(location.getLocationCode()));
				}
			}
		}
		return result;
	}
	
	/**
	 * Input a string and find out the Review/Qa that contain that string as a partial word
	 *
	 * @param  partialWord  a key to find Review/Qa
	 * @return ArrayList<Product> a list of product with Review/Qa
	 */
	public ArrayList<Product> getLineByPartialWordAndSortByFreq(String partialWord, InvertedIndex indexes){
		ArrayList<Product> result = new ArrayList<Product>();
		LocationList locationList = new LocationList(new ArrayList<Location>());
		for(Entry<String, LocationList> index : indexes.getIndexes().entrySet()) {
			if(index.getKey().contains(partialWord)) {
				locationList.addListLocation(index.getValue());
			}
		}
		//ArrayList<Location> locations = listLocation.sortByCount();
		for(Location location : locationList.getListLocation()) {
			if(this.getProductsByLine().containsKey(location.getLocationCode())) {
				result.add(this.getProductsByLine().get(location.getLocationCode()));
			}
		}
		result.sort(new Comparator<Product>() {

			@Override
			public int compare(Product p1, Product p2) {
				return p1.getLocationCode().compareTo(p2.getLocationCode());
			}
		});
		return result;
	}
}
