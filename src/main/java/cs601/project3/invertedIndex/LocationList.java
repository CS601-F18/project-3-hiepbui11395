package cs601.project3.invertedIndex;

import java.util.ArrayList;
import java.util.Comparator;

public class LocationList {
	private ArrayList<Location> listLocation;

	public LocationList(ArrayList<Location> listLocation) {
		super();
		this.listLocation = listLocation;
	}

	public ArrayList<Location> getListLocation() {
		return listLocation;
	}

	public void setListLocation(ArrayList<Location> listLocation) {
		this.listLocation = listLocation;
	}
	
	public void addListLocation(LocationList locationList) {
		for(Location location : locationList.getListLocation()) {
			int index = 0;
			if((index = this.listLocation.indexOf(location)) == -1) {
				location.setCount(1);
				this.listLocation.add(location);
			} else {
				Location l = this.listLocation.get(index);
				l.setCount(l.getCount() + location.getCount());
			}
		}
	}
	

	/**
	 * Add Location to the list
	 * 
	 * @param location
	 */
	public void addToList(Location location) {
		this.listLocation.add(location);
	}

	/**
	 * 
	 * Sort the list of location by using Count in Location
	 * 
	 * @return ArrayList<Location> return a list of location after sorting
	 */
	public ArrayList<Location> sortByCount() {

		this.listLocation.sort(new Comparator<Location>() {

			@Override
			public int compare(Location o1, Location o2) {
				return Integer.compare(o2.getCount(),o1.getCount());
			}
		});
		return this.listLocation;
	}
}
