package application;
/**
 * Class for Objects that are place in priority queue 
 * @author Erin Jagt & Micah Weiberg
 * @version 05/13/19
 */
public class LandItem implements Comparable<LandItem>{
	/**Holds index of the item in the gameBoard array*/
	private int index;
	/**Holds the price of current path to land */
	private int price;
	
	/**
	 * Constructor
	 */
	LandItem(int newIndex,int newPrice){
		index=newIndex;
		price=newPrice;
	}
	/**
	 * Getter for price
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Compares LandItem objects
	 */
	public int compareTo(LandItem item) {
		if(item.getPrice()>price)
			return -1;
		else if(item.getPrice()<price)
			return 1;
		else
			return 0;
	}
	/**
	 * Getter for index
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
}