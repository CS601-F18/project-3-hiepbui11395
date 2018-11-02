package cs601.project3.applications;

import cs601.project3.invertedIndex.InvertedIndex;

public class QaInvertedIndex {
//	private static QaInvertedIndex instance;
	
	private static InvertedIndex instance;
	
	private QaInvertedIndex() {}
	
	public static synchronized InvertedIndex getInstance(){
        if(instance == null){
            instance = new InvertedIndex();
        }
        return instance;
    }
	
}
