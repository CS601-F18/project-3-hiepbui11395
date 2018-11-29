package cs601.project3.invertedIndex;

/**
 * Review class contains review's information
 * @author hiepbui
 *
 */
public class Review extends Product{
	private String reviewText;
	private String reviewerID;
	private double overall;

	public Review(String location, String asin, String reviewText, String reviewerID, double overall) {
		super(location, asin);
		this.reviewText = reviewText;
		this.reviewerID = reviewerID;
		this.overall = overall;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}
	
	public String getReviewerID() {
		return reviewerID;
	}

	public void setReviewerID(String reviewerID) {
		this.reviewerID = reviewerID;
	}

	public double getOverall() {
		return overall;
	}

	public void setOverall(double overall) {
		this.overall = overall;
	}

	public String toString() {
		return String.format(" - ASIN: %s\n - ReviewerID: %s\n - Score: %f\n - Review Text: %s\n\n", this.getAsin(), this.reviewerID, this.overall, this.reviewText);
	}
}
