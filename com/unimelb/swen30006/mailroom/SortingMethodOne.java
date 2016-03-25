package com.unimelb.swen30006.mailroom;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;


import com.unimelb.swen30006.mailroom.StorageBox.Summary;
import com.unimelb.swen30006.mailroom.exceptions.DuplicateIdentifierException;
import com.unimelb.swen30006.mailroom.exceptions.MailOverflowException;
import com.unimelb.swen30006.mailroom.exceptions.UnknownIdentifierException;
/* This sorting strategy will try to create a storageBox for each of
the floors, and store mail that belong to a specific floor together */
public class SortingMethodOne extends StorageTracker implements SortingStrategy {

	//Keeps track of what is stored where, The key is the floor, and the value
	//is a StorageBox
	private Comparator<StorageBox.Summary>  boxesWithClosestFloors;



	public SortingMethodOne(int maxItems) {
		
		super(maxItems);
		
		boxesWithClosestFloors = new Comparator<StorageBox.Summary>() {

			@Override
			public int compare(Summary o1, Summary o2) {
				return Integer.compare(smallestDifference(o1.identifier), smallestDifference(o2.identifier));
			}
			
		};

	}


	@Override
	public String assignStorage(MailItem item, MailStorage storage) throws MailOverflowException {

		super.setDeliveryFloor(item.floor);
		String bestBox;
		

		StorageBox.Summary[] available = storage.retrieveSummaries();
		Arrays.sort(available,boxesWithClosestFloors);
				
		try {
			
			for(StorageBox.Summary summary: available) {
				
				bestBox = summary.identifier;
				StorageBox box = storage.retrieveBox(bestBox);

					
				if(box.canHold(item)) {
					
					super.itemCountIncrement(bestBox, super.getDeliveryFloor());
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

				String newBoxID = super.createBoxTracker();
				storage.createBox(newBoxID);
				return newBoxID;
			}
			
		} catch (DuplicateIdentifierException e) {
			System.out.println(e);
			System.exit(0);
		}
		throw new MailOverflowException();
	}


	private int smallestDifference(String id) {
		
		HashMap<Integer,Integer> box = super.getBox(id);
		int closestFloorDistance = -1;
		
		
		for(int otherFloor: box.keySet()) {
			
			if(closestFloorDistance == -1) {
				closestFloorDistance = Math.abs(super.getDeliveryFloor()-otherFloor);
			}
			else if(closestFloorDistance < Math.abs(super.getDeliveryFloor()-otherFloor)) {
				closestFloorDistance = Math.abs(super.getDeliveryFloor()-otherFloor);
			}
			
		}
		
		return closestFloorDistance;
	}
	



}
