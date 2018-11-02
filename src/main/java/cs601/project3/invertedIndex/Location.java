package cs601.project3.invertedIndex;

public class Location {
	private String locationCode;
	private int count;
	
	public Location(String locationCode, int count) {
		super();
		this.locationCode = locationCode;
		this.count = count;
	}
	
	public Location(String locationCode) {
		super();
		this.locationCode = locationCode;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocation(String locationCode) {
		this.locationCode = locationCode;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Location)) {
			return false;
		}
		Location l = (Location)o;
		if(this.getLocationCode().equals(l.getLocationCode())) {
			return true;
		}
		return false;
	}
}
