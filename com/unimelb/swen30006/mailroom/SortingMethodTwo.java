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

public class SortingMethodTwo implements SortingStrategy {

	static private Comparator<StorageBox.Summary>  numDestComparator;
	private HashMap<String, HashMap<Integer, Integer>> storageTracker;
	private Integer ID;
	
	static {
		numDestComparator = new Comparator<StorageBox.Summary>() {

			@Override
			public int compare(Summary o1, Summary o2) {
				return Integer.compare(o1.numDests, o2.numDests);
			}
			
		};
	}
	
	public SortingMethodTwo() {
		storageTracker = new HashMap<String, HashMap<Integer, Integer>>();
		ID = 1;
	}
	
	
	@Override
	public String assignStorage(MailItem item, MailStorage storage) throws MailOverflowException {
		
		int deliveryFloor = item.floor;
		
		StorageBox.Summary[] available = storage.retrieveSummaries();
		Arrays.sort(available, numDestComparator);
		
		
		for(StorageBox.Summary summary: available) {
			
			String boxID = summary.identifier;
			HashMap<Integer,Integer> summaryBox = storageTracker.get(boxID);
			if(summary.remainingUnits >= item.size) {
				
				if(summaryBox.containsKey(deliveryFloor)) {
					summaryBox.put(deliveryFloor, summaryBox.get(deliveryFloor)+1);
				}
				else {
					summaryBox.put(deliveryFloor, 1);
				}
				return boxID;
			}
		}
		
		/*
		for(StorageBox.Summary summary: available) {
			
			HashMap<Integer,Integer> box = storageTracker.get(summary.identifier);

			int minFloor = getMinFloor(box);
			int maxFloor = getMaxFloor(box);
			
			if((deliveryFloor >= minFloor && deliveryFloor <= maxFloor)) {
				
				try {
					StorageBox boxFromStorage = storage.retrieveBox(summary.identifier);
					
					if(boxFromStorage.canHold(item)) {
						
						if(box.containsKey(deliveryFloor)) {
							box.put(deliveryFloor, box.get(deliveryFloor)+1);
						}
						else {
							box.put(deliveryFloor, 1);
						}
						//System.out.println(storageTracker);

						return summary.identifier;
					}
				} catch (UnknownIdentifierException e) {
	                // Strategy has not correctly identified box
	                System.out.println(e);
	                System.out.println("FATAL: Sort Strategy failed. Abort");
	                System.exit(0);
				}
			}
		}
		*/
		//Let's try creating a new box for the item
		
		if(!storage.isFull()) {
			
			String newBoxID = ID.toString();
			
			try {
				storage.createBox(newBoxID);
			} catch (DuplicateIdentifierException e) {
				System.out.println(e);
				System.exit(0);
			}
			
			ID++;
			
			HashMap<Integer,Integer> newBox = new HashMap<Integer,Integer>();
			newBox.put(deliveryFloor, 1);
			storageTracker.put(newBoxID, newBox);
			return newBoxID;
		}
		
		System.out.println(storageTracker);
		System.out.println(item);
		System.exit(0);
		//Otherwise mailitem needs to wait for empty box
		throw new MailOverflowException();
		
		
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
	public HashMap<String, HashMap<Integer, Integer>> getStorageTracker() {
		return storageTracker;
	}

		

}
