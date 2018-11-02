package cs601.project3.invertedIndex;

public abstract class Product {
	protected String locationCode;
	protected String asin;

	public Product(String locationCode, String asin) {
		super();
		this.locationCode = locationCode;
		this.asin = asin;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	
	public String getAsin() {
		return asin;
	}
	

	public void setAsin(String asin) {
		this.asin = asin;
	}
}
