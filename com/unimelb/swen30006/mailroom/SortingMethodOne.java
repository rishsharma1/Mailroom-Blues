package com.unimelb.swen30006.mailroom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.unimelb.swen30006.mailroom.exceptions.DuplicateIdentifierException;
import com.unimelb.swen30006.mailroom.exceptions.MailOverflowException;
import com.unimelb.swen30006.mailroom.exceptions.UnknownIdentifierException;
/* This sorting strategy will try to create a storageBox for each of
the floors, and store mail that belong to a specific floor together */
public class SortingMethodOne implements SortingStrategy {

	//Keeps track of what is stored where, The key is the floor, and the value
	//is a StorageBox
	private HashMap<String, HashMap<Integer, Integer>> storageTracker;
	private Integer ID;
	public SortingMethodOne() {
		storageTracker = new HashMap<String, HashMap<Integer, Integer>>();
		ID = 1;
	}


	@Override
	public String assignStorage(MailItem item, MailStorage storage) throws MailOverflowException {

		
		int deliveryFloor = item.floor;
		int maxCountOfFloorItem = -1;
		String boxID = null;

		//Finds the box with the highest distribution of items from current mail item's
		//floor 
		
		for(String id: storageTracker.keySet() ) {
			
			
			
			
			if(storageTracker.get(id).containsKey(deliveryFloor)) {
				
				int countOfFloorItem = storageTracker.get(id).get(deliveryFloor);
				
				if(countOfFloorItem > maxCountOfFloorItem) {
					boxID = id;
					maxCountOfFloorItem = countOfFloorItem;
				}
			}
		}
		
		StorageBox maxBox;
		if(boxID != null) {

			try {
				maxBox = storage.retrieveBox(boxID);

				if(maxBox.canHold(item)) {
					//increase count of of item in box by 1 in storage tracker
					storageTracker.get(boxID).put(deliveryFloor,
					storageTracker.get(boxID).get(deliveryFloor)+1);
					return boxID;
				}
			} catch (UnknownIdentifierException e1) {
				e1.printStackTrace();
				
				System.out.println("Box ID: "+boxID);


			}
		}
		


		
		// let's try creating a new box 
		try {
			
			if(!storage.isFull()) {
				String newBoxID = ID.toString();
				storage.createBox(newBoxID);
				ID++;
				HashMap<Integer,Integer> newBox = new HashMap<Integer,Integer>();
				newBox.put(deliveryFloor, 1);
				storageTracker.put(newBoxID, newBox);
				return newBoxID;
			}
			
		} catch (DuplicateIdentifierException e) {
			System.out.println(e);
			System.exit(0);
		}
		
		//String bestBox = null;
		//int distance = -1;
		
		/*
		for(String id: storageTracker.keySet()) {
			
			//int maxFloor = getMaxFloor(storageTracker.get(id));
			//int minFloor = getMinFloor(storageTracker.get(id));
			//System.out.println("minFloor: "+minFloor+" maxFloor: "+minFloor+ " Delivery: "+deliveryFloor);

			
			if(deliveryFloor <= maxFloor && deliveryFloor >= minFloor) {
				storageTracker.get(id).put(deliveryFloor, 1);
				return id;
			}
			
			int closestInBox = closestFloor(storageTracker.get(id),deliveryFloor);
			
			if(distance == -1 && bestBox == null) {
				
				//bestBox = getAvailableBox(storage,item);
				closestInBox = closestFloor(storageTracker.get(bestBox),deliveryFloor);
				distance = Math.abs(closestInBox-deliveryFloor);

			} else
				try {
					if(distance > (closestInBox-deliveryFloor) && storage.retrieveBox(id).canHold(item)) {
						distance = Math.abs(closestInBox-deliveryFloor);
						bestBox = id;
					}
				} catch (UnknownIdentifierException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		
		return bestBox;
		*/
		return null;
	}


	public HashMap<String, HashMap<Integer, Integer>> getStorageTracker() {
		return storageTracker;
	}


	public void setID(Integer iD) {
		ID = iD; 
	}
	
	private int getMinFloor(HashMap<Integer,Integer> box) {
		
		List<Integer> keyList = new ArrayList<Integer>(box.keySet());
		Collections.sort(keyList);
		
		return keyList.get(0);
		
		
	}
	
	private int getMaxFloor(HashMap<Integer,Integer> box) {
		
		List<Integer> keyList = new ArrayList<Integer>(box.keySet());
		Collections.sort(keyList);
		Collections.reverse(keyList);
		
		return keyList.get(0);
		
		
	}
	private int closestFloor(HashMap<Integer,Integer> box, int mailFloor) {
		
		int closestFloorDistance = -1;
		int closestFloor = 0;
		for(int floor: box.keySet()) {
			
			if(closestFloorDistance == -1 && closestFloor == 0 ) {
				closestFloorDistance = Math.abs(mailFloor-floor);
				closestFloor = floor;
			}
			else if(closestFloorDistance > Math.abs(mailFloor-floor)) {
				closestFloorDistance = mailFloor-floor;
				closestFloor = floor;
			}
			
		}
		return closestFloor;
	}
	/*
	private String getAvailableBox(MailStorage storage, MailItem item) throws MailOverflowException {
		
        StorageBox.Summary[] available = storage.retrieveSummaries();
        for(StorageBox.Summary summary : available){
            if(summary.remainingUnits >= item.size){
                return summary.identifier;
            }
            
        }
        throw new MailOverflow
	}
	*/
	


}
