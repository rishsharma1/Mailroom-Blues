package com.unimelb.swen30006.mailroom;

import java.util.HashMap;

import com.unimelb.swen30006.mailroom.StorageBox.Summary;
import com.unimelb.swen30006.mailroom.exceptions.NoBoxReadyException;

public class SelectionMethod implements SelectionStrategy {
	

	
	public SelectionMethod(SortingStrategy sortStrategy, int maxBoxes) {
		this.sortMethodOne = (SortingMethodOne) sortStrategy;
		this.maxBoxes = maxBoxes;
		deliverying = false;
		currentBoxStats = new HashMap<Integer,Integer>();
	}
	
	//current id of the box that is going to be delivered 
	private String currentBoxID;
	public SortingMethodOne sortMethodOne;
	private HashMap<Integer,Integer> currentBoxStats;
	private  static int maxBoxes;
	private boolean deliverying;
	
	
	@Override
	public String selectNextDelivery(Summary[] summaries) throws NoBoxReadyException {
		
		System.out.println("Summary Length: "+summaries.length);
		System.out.println("storageTracker: "+sortMethodOne.getStorageTracker().size());
		if(sortMethodOne.maxItems < 1) {
			
			if(summaries.length == 0) {
				deliverying = false;
			}
			else {
				deliverying = true;
			}
		}


		if((summaries.length == maxBoxes || deliverying)) {
			
			if(summaries.length == 1) {
				deliverying = false;
			}
			else {
				deliverying = true;
			}
			
			StorageBox.Summary maxBoxSummary = summaries[0];
			for(StorageBox.Summary summary : summaries) {
				
				if(summary.remainingUnits < maxBoxSummary.remainingUnits) {
					maxBoxSummary = summary;
				}
				
				
			}


			this.currentBoxID = maxBoxSummary.identifier;
			this.currentBoxStats = sortMethodOne.getStorageTracker().get(currentBoxID);
			

			sortMethodOne.getStorageTracker().remove(currentBoxID);
			//System.out.println("Box Selected: "+maxBoxSummary.identifier + "....");
			//System.out.println(maxBoxSummary);
			//System.out.println(sortMethodOne.getStorageTracker().size());

	
			return maxBoxSummary.identifier;
			
	
			
		}
		//System.out.println("Throwing the no box ready exception");
		
		/*for(StorageBox.Summary summary: summaries) {
			System.out.println(summary);
		}
		*/
		throw new NoBoxReadyException();
		
	}
	
	public HashMap<Integer,Integer> getCurrentBoxStats() {
		return currentBoxStats;
	}

	public String getCurrentBoxID() {
		return currentBoxID;
	}
	
	
	




}
