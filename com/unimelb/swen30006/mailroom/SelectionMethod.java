package com.unimelb.swen30006.mailroom;


import com.unimelb.swen30006.mailroom.StorageBox.Summary;
import com.unimelb.swen30006.mailroom.exceptions.NoBoxReadyException;

public class SelectionMethod implements SelectionStrategy {
	

	
	public SelectionMethod(SortingStrategy sortStrategy1,SortingStrategy sortStrategy2, int maxBoxes) {
		
		this.sortStrategy1 = (SortingMethodOne) sortStrategy1;
		this.sortStrategy2 = (SortingMethodTwo) sortStrategy2;
		this.maxBoxes = maxBoxes;
		deliverying = false;
		
	}
	
	//current id of the box that is going to be delivered 
	private String currentBoxID;
	private SortingMethodOne sortStrategy1;
	private SortingMethodTwo sortStrategy2;
	private  int maxBoxes;
	private boolean deliverying;

	
	
	@Override
	public String selectNextDelivery(Summary[] summaries) throws NoBoxReadyException {
		
		
		
		if(getRemainingItems() < 1) {
			
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
			removeBoxTracker();
	
			return maxBoxSummary.identifier;
			
	
			
		}

		throw new NoBoxReadyException();
		
	}
	


	public String getCurrentBoxID() {
		return currentBoxID;
	}
	
	public int getRemainingItems() {
		
		if(sortStrategy1 == null) {
			return sortStrategy2.getItemsRemaining();
		}
		return sortStrategy1.getItemsRemaining();
	}
	
	public void removeBoxTracker() {
		
		if(sortStrategy1 == null) {
			sortStrategy2.getStorageTracker().remove(currentBoxID);
		}
		else {
			sortStrategy1.getStorageTracker().remove(currentBoxID);
		}
	}
	




}
