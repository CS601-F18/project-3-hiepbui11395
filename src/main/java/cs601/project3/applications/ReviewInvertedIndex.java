package cs601.project3.applications;

import cs601.project3.invertedIndex.InvertedIndex;

public class ReviewInvertedIndex {
	
	private static InvertedIndex instance;
	
	private ReviewInvertedIndex() {}
	
	public static synchronized InvertedIndex getInstance(){
        if(instance == null){
            instance = new InvertedIndex();
        }
        return instance;
    }
	
}
