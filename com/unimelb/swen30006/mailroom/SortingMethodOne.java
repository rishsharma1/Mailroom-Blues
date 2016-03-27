/* Name: Rishabh Sharma
 * Student Number: 694739
 */

package com.unimelb.swen30006.mailroom;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;


import com.unimelb.swen30006.mailroom.StorageBox.Summary;
import com.unimelb.swen30006.mailroom.exceptions.DuplicateIdentifierException;
import com.unimelb.swen30006.mailroom.exceptions.MailOverflowException;
import com.unimelb.swen30006.mailroom.exceptions.UnknownIdentifierException;


/**
 * This sorting strategy will try to group mail with other mail that are closer
 * in terms of delivery floor. It will do this by sorting the available boxes
 * lowest difference with the current delivery floor. If no box is available, 
 * it will try and create a new box, if a box cannot be created a MailOverflowException 
 * will be thrown. 
 */
public class SortingMethodOne extends StorageTracker implements SortingStrategy {

	//Keeps track of what is stored where, The key is the floor, and the value
	//is a StorageBox
	private Comparator<StorageBox.Summary>  boxesWithClosestFloors;



	public SortingMethodOne(int maxItems) {
		
		super(maxItems);
		
		// comparer for the sorting strategy based on smallest difference 
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
		// sort the boxes 
		Arrays.sort(available,boxesWithClosestFloors);
				
		try {
			
			for(StorageBox.Summary summary: available) {
				
				bestBox = summary.identifier;
				StorageBox box = storage.retrieveBox(bestBox);

					
				if(box.canHold(item)) {
					
					// increase the count of the item in the box
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
				
				// create a box in the storage tracker
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
	
	
	/**
	 * finds the smallest difference in the box with the current floor
	 * @param id
	 * @return the smallest difference in box with current floor 
	 */
	private int smallestDifference(String id) {
		
		HashMap<Integer,Integer> box = super.getBox(id);
		int closestFloorDistance = -1;
		
		
		for(int otherFloor: box.keySet()) {
			
			// initialize the variable 
			if(closestFloorDistance == -1) {
				closestFloorDistance = Math.abs(super.getDeliveryFloor()-otherFloor);
			}
			// found a smaller distance 
			else if(closestFloorDistance < Math.abs(super.getDeliveryFloor()-otherFloor)) {
				closestFloorDistance = Math.abs(super.getDeliveryFloor()-otherFloor);
			}
			
		}
		
		return closestFloorDistance;
	}
	

	



}
