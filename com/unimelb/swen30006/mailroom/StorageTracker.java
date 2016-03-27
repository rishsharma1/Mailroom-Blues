/* Name: Rishabh Sharma
 * Student Number: 694739
 */

package com.unimelb.swen30006.mailroom;

import java.util.HashMap;

/**
 * StorageTracker takes care of storing the state of the sorting strategy
 * by creating helping methods and data structures that keep track of the 
 * count of Mail Items in each box  by their delivery floor. 
 */
public class StorageTracker {
	
	/**hash map storing box id as key and value as hash map which contains
	* keys for delivery floors and values as the count of items being delivered 
	to such floor*/
	
	private HashMap<String, HashMap<Integer,Integer>> storageTracker;
	// ID for the boxes 
	private Integer ID;
	// Mail Items remaining to deliver 
	private int itemsRemaining;
	// delivery floor for the item 
	private int deliveryFloor;
	
	public StorageTracker(int itemsRemaining) {
		initializeState(itemsRemaining);
		
	}
	
	/*
	 * Method for initializing the state of the storage tracker
	 */
	public void initializeState(int itemsRemaining) {
		this.ID = 1;
		storageTracker = new HashMap<String, HashMap<Integer,Integer>>();
		this.itemsRemaining = itemsRemaining;
		
	}
	

	public HashMap<String, HashMap<Integer, Integer>> getStorageTracker() {
		return storageTracker;
	}

	public int getDeliveryFloor() {
		return deliveryFloor;
	}

	public void setDeliveryFloor(int deliveryFloor) {
		this.deliveryFloor = deliveryFloor;
	}

	public int getID() {
		return ID;
	}
	
	
	/*
	 * Increments the item count in the box at the delivery floor of 
	 * the mail item
	 */
	public void itemCountIncrement(String boxID, int deliveryFloor) {
		
		HashMap<Integer, Integer> box = storageTracker.get(boxID);
		
		//check to see if the item has already been in the box
		if(box.containsKey(deliveryFloor)) {
			box.put(deliveryFloor, box.get(deliveryFloor)+1);
		}
		else {
			box.put(deliveryFloor,1);
		}
		this.itemsRemaining -= 1;
		
	}
	
	//create a new box in the storage tracker 
	public String createBoxTracker() {
		
		String boxID = this.ID.toString();
		HashMap<Integer,Integer> box = new HashMap<Integer,Integer>();
		box.put(this.deliveryFloor, 1);
		storageTracker.put(boxID, box);
		this.ID++;
		this.itemsRemaining -= 1;
		return boxID;
	}
	
	public int getItemsRemaining() {
		return itemsRemaining;
	}

	public HashMap<Integer,Integer> getBox(String id) {
		
		return storageTracker.get(id);
	}
	
		
		
		
		

}
