package cs601.project3.invertedIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gson.JsonParseException;

/**
 * The base class of inverted Index
 * @author hiepbui
 *
 */
public class InvertedIndex {
	private HashMap<String, LocationList> indexes;

	public InvertedIndex(HashMap<String, LocationList> indexes) {
		super();
		this.indexes = indexes;
	}

	public InvertedIndex() {
		this.indexes = new HashMap<String, LocationList>();
	}

	public HashMap<String, LocationList> getIndexes() {
		return indexes;
	}

	public void setIndexes(HashMap<String, LocationList> index) {
		this.indexes = index;
	}

	/**
	 * Get a textReviewText or Question/Answer split it to words and put in to the InvertedIndex
	 *
	 * @param  text  a textReview or Question/Answer
	 * @param  locationCode work as an Id of each query
	 * @throws JsonParseException
	 */
	public synchronized void addWordToIndex(String text, String locationCode) {
		String[] words = text.split("\\W+");
		HashMap<String, Integer> countWords = new HashMap<String, Integer>();
		for(String word : words) {
			word = word.replaceAll("[^A-Za-z0-9]", "").toLowerCase().trim();
			if(!word.equals("")) {
				if(!countWords.containsKey(word)) {
					countWords.put(word, 1);
				} else {
					countWords.put(word, countWords.get(word)+1);
				}
			}
		}
		for(Entry<String, Integer> word : countWords.entrySet()) {
			if(indexes.containsKey(word.getKey())) {
				LocationList locationList = indexes.get(word.getKey());
				locationList.addToList(new Location(locationCode, word.getValue()));
			} else {
				LocationList locationList = new LocationList(new ArrayList<Location>());
				locationList.addToList(new Location(locationCode, word.getValue()));
				indexes.put(word.getKey(), locationList);
			}
		}
	}
}
