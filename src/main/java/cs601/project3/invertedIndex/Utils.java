package cs601.project3.invertedIndex;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class Utils {
	private static final Gson gson = new Gson();
	public static enum TYPE {REVIEW, QA};
	
	
	/**
	 * @param url file name to read from
	 * @param invertedIndex the invertedIndex to keep information
	 * @param type type of product's doc(Review/QA)
	 * @throws IOException
	 */
	public static void addToIndex(String url, ProductList products, InvertedIndex invertedIndex, TYPE type) throws IOException {
		String line = "";
		int count =0;
		Path path = Paths.get(url);
		try (
				BufferedReader br = Files.newBufferedReader(path, Charset.forName("ISO-8859-1"));
				){
			while((line = br.readLine()) != null) {
				count++;
				try {
					Product product;
					String text;
					if(type.equals(TYPE.REVIEW)) {
						product = gson.fromJson(line, Review.class);
						text = ((Review)product).getReviewText();
					} else {
						product = gson.fromJson(line, Qa.class);
						text = String.format("%s:%s", ((Qa)product).getQuestion(), ((Qa)product).getAnswer());
					}
					//locationCode includes fileName and lineNumber
					product.locationCode = String.format("%s - %d", url, count);
					product.setAsin(product.getAsin().toLowerCase());
					invertedIndex.addWordToIndex(text, product.getLocationCode());
					products.addProductToDictionary(product, product.getAsin(), product.getLocationCode());
				}
				catch(JsonParseException jspe) {
					//jspe.printStackTrace();
					continue;
				}
			}
			for(Entry<String, LocationList> index : invertedIndex.getIndexes().entrySet()) {
				index.getValue().sortByCount();
			}
		} catch (IOException ioe) {
			throw ioe;
		};
	}

	public static ArrayList<Product> findApi(String asin, ProductList  products) {
		ArrayList<Product> result = products.getProductByAsin(asin);
		return result;
	}

	/**
	 * execute the command search by word
	 * 
	 * @param term the key to find 
	 * @param Index an InvertedIndex of Review/Qa file
	 */
	public static ArrayList<Product> searchByWordApi(String term, InvertedIndex index, ProductList products) {
		ArrayList<Product> result = products.getLineByWordAndSortByFreq(term, index);
		return result;
	}

}
