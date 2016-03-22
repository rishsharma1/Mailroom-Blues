package com.unimelb.swen30006.mailroom;

import java.util.HashMap;

import com.unimelb.swen30006.mailroom.StorageBox.Summary;
import com.unimelb.swen30006.mailroom.exceptions.NoBoxReadyException;

public class SelectionMethod implements SelectionStrategy {
	

	
	public SelectionMethod(SortingStrategy sortStrategy) {
		this.sortMethodOne = (SortingMethodTwo) sortStrategy;

	}
	
	//current id of the box that is going to be delivered 
	private String currentBoxID;
	private SortingMethodTwo sortMethodOne;
	private HashMap<Integer,Integer> currentBoxStats;
	
	@Override
	public String selectNextDelivery(Summary[] summaries) throws NoBoxReadyException {
		
		
		if(summaries.length != 0) {
			

			StorageBox.Summary maxBoxSummary = summaries[0];
			for(StorageBox.Summary summary : summaries) {
				
				if(summary.remainingUnits < maxBoxSummary.remainingUnits) {
					maxBoxSummary = summary;
				}
				
			}


				this.currentBoxID = maxBoxSummary.identifier;
				this.currentBoxStats = sortMethodOne.getStorageTracker().get(currentBoxID);
				System.out.println(currentBoxStats);

				sortMethodOne.getStorageTracker().remove(currentBoxID);
				return maxBoxSummary.identifier;
			
	
			
		}
		throw new NoBoxReadyException();
		
	}
	
	public HashMap<Integer,Integer> getCurrentBoxStats() {
		return currentBoxStats;
	}

	public String getCurrentBoxID() {
		return currentBoxID;
	}
	
	




}
