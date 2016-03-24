package com.unimelb.swen30006.mailroom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.unimelb.swen30006.mailroom.StorageBox.Summary;
import com.unimelb.swen30006.mailroom.exceptions.DuplicateIdentifierException;
import com.unimelb.swen30006.mailroom.exceptions.MailOverflowException;
import com.unimelb.swen30006.mailroom.exceptions.UnknownIdentifierException;
/* This sorting strategy will try to create a storageBox for each of
the floors, and store mail that belong to a specific floor together */
public class SortingMethodOne implements SortingStrategy {

	//Keeps track of what is stored where, The key is the floor, and the value
	//is a StorageBox
	static private Comparator<StorageBox.Summary>  boxesWithClosestFloors;

	private HashMap<String, HashMap<Integer, Integer>> storageTracker;
	private Integer ID;
	public int maxItems;
	private int deliveryFloor;
	


	

	public SortingMethodOne() {
		storageTracker = new HashMap<String, HashMap<Integer, Integer>>();
		ID = 1;
		maxItems = 1000;

	}


	@Override
	public String assignStorage(MailItem item, MailStorage storage) throws MailOverflowException {

		
		deliveryFloor = item.floor;
		String bestBox;
		System.out.println(storage.isFull());
		System.out.println(item);
		
		boxesWithClosestFloors = new Comparator<StorageBox.Summary>() {

			@Override
			public int compare(Summary o1, Summary o2) {
				return Integer.compare(smallestDifference(storageTracker.get(o1.identifier)), smallestDifference(storageTracker.get(o2.identifier)));
			}
			
		};
		
		StorageBox.Summary[] available = storage.retrieveSummaries();
		Arrays.sort(available,boxesWithClosestFloors);
		try {
			
			for(StorageBox.Summary summary: available) {
				

				bestBox = summary.identifier;
				HashMap<Integer,Integer> summaryBox = storageTracker.get(bestBox);
				StorageBox box = storage.retrieveBox(bestBox);

				
				if(box.canHold(item))  {
					
				
					maxItems--;
					if(summaryBox.containsKey(deliveryFloor)) {
						summaryBox.put(deliveryFloor, summaryBox.get(deliveryFloor)+1);
					}
					else {
						summaryBox.put(deliveryFloor, 1);
					}
					return bestBox;
				}
			}

			
		} catch(UnknownIdentifierException e) {
			
			 System.out.println(e);
             System.out.println("FATAL: Sort Strategy failed. Abort");
             System.exit(0);
		}



		// let's try creating a new box 
		try {
			
			if(!storage.isFull()) {

				String newBoxID = ID.toString();
				storage.createBox(newBoxID);
				ID++;
				maxItems--;
				HashMap<Integer,Integer> newBox = new HashMap<Integer,Integer>();
				newBox.put(deliveryFloor, 1);
				storageTracker.put(newBoxID, newBox);
				return newBoxID;
			}
			
		} catch (DuplicateIdentifierException e) {
			System.out.println(e);
			System.exit(0);
		}
		throw new MailOverflowException();

	}


	public HashMap<String, HashMap<Integer, Integer>> getStorageTracker() {
		return storageTracker;
	}


	public void setID(Integer iD) {
		ID = iD; 
	}
	
	private int smallestDifference(HashMap<Integer,Integer> box) {
		
		int closestFloorDistance = -1;
		
		
		for(int otherFloor: box.keySet()) {
			
			if(closestFloorDistance == -1) {
				closestFloorDistance = Math.abs(deliveryFloor-otherFloor);
			}
			else if(closestFloorDistance < Math.abs(deliveryFloor-otherFloor)) {
				closestFloorDistance = Math.abs(deliveryFloor-otherFloor);
			}
			
		}
		
		return closestFloorDistance;
	}


}
