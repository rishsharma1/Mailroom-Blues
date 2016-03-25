package com.unimelb.swen30006.mailroom;

import java.util.HashMap;

public class StorageTracker {
	
	private HashMap<String, HashMap<Integer,Integer>> storageTracker;
	private Integer ID;
	private int itemsRemaining;
	private int deliveryFloor;
	
	public StorageTracker(int itemsRemaining) {
		initializeState(itemsRemaining);
		
	}
	
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

	
	public void itemCountIncrement(String boxID, int deliveryFloor) {
		
		HashMap<Integer, Integer> box = storageTracker.get(boxID);
		
		if(box.containsKey(deliveryFloor)) {
			box.put(deliveryFloor, box.get(deliveryFloor)+1);
		}
		else {
			box.put(deliveryFloor,1);
		}
		this.itemsRemaining -= 1;
		
	}
	
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
