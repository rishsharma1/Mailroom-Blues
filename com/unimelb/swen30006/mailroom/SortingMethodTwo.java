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
	public int maxItems;
	
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
		maxItems = 1000;
	}
	
	
	@Override
	public String assignStorage(MailItem item, MailStorage storage) throws MailOverflowException {
		
		
		int deliveryFloor = item.floor;
		
		StorageBox.Summary[] available = storage.retrieveSummaries();
		Arrays.sort(available, numDestComparator);
		
		
		for(StorageBox.Summary summary: available) {
			
			String boxID = summary.identifier;
			HashMap<Integer,Integer> summaryBox = storageTracker.get(boxID);
			try {
				StorageBox box = storage.retrieveBox(boxID);
				
				if(box.canHold(item)) {
					maxItems--;
					if(summaryBox.containsKey(deliveryFloor)) {
						summaryBox.put(deliveryFloor, summaryBox.get(deliveryFloor)+1);
					}
					else {
						summaryBox.put(deliveryFloor, 1);
					}
					return boxID;
				}
				
			} catch (UnknownIdentifierException e) {
				 System.out.println(e);
	             System.out.println("FATAL: Sort Strategy failed. Abort");
	             System.exit(0);
			}

		}
		

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
			maxItems--;
			return newBoxID;
		}
		
		//System.out.println("All items: "+storageTracker.size());
		//System.out.println(item);
		//System.exit(0);
		//Otherwise mailitem needs to wait for empty box
		throw new MailOverflowException();
		
		
	}
	

	public HashMap<String, HashMap<Integer, Integer>> getStorageTracker() {
		return storageTracker;
	}

		

}
