package com.unimelb.swen30006.mailroom;


import java.util.Arrays;
import java.util.Comparator;


import com.unimelb.swen30006.mailroom.StorageBox.Summary;
import com.unimelb.swen30006.mailroom.exceptions.DuplicateIdentifierException;
import com.unimelb.swen30006.mailroom.exceptions.MailOverflowException;
import com.unimelb.swen30006.mailroom.exceptions.UnknownIdentifierException;

public class SortingMethodTwo extends StorageTracker implements SortingStrategy {

	private Comparator<StorageBox.Summary>  numDestComparator;

	

	public SortingMethodTwo(int maxItems) {
		super(maxItems);
		
		numDestComparator = new Comparator<StorageBox.Summary>() {

			@Override
			public int compare(Summary o1, Summary o2) {
				return Integer.compare(o1.numDests, o2.numDests);
			}
			
		};
	}
	
	
	@Override
	public String assignStorage(MailItem item, MailStorage storage) throws MailOverflowException {
		
		
		
		StorageBox.Summary[] available = storage.retrieveSummaries();
		Arrays.sort(available, numDestComparator);
		
		
		for(StorageBox.Summary summary: available) {
			
			String boxID = summary.identifier;
			try {
				StorageBox box = storage.retrieveBox(boxID);
				
				if(box.canHold(item)) {
					super.itemCountIncrement(boxID, super.getDeliveryFloor());
					return boxID;
				}
				
			} catch (UnknownIdentifierException e) {
				 System.out.println(e);
	             System.out.println("FATAL: Sort Strategy failed. Abort");
	             System.exit(0);
			}

		}
		

		//Let's try creating a new box for the item

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
	
	public void initializeState(int itemsRemaining) {
		super.initializeState(itemsRemaining);
	}
	


}
