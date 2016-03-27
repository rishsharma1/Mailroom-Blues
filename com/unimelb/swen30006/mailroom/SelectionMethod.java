/* Name: Rishabh Sharma
 * Student Number: 694739
 */
package com.unimelb.swen30006.mailroom;


import com.unimelb.swen30006.mailroom.StorageBox.Summary;
import com.unimelb.swen30006.mailroom.exceptions.NoBoxReadyException;


/**
 * This selection strategy tries to ensure the maximum number of boxes are
 * ready to be delivered, otherwise a NoBoxReadyException is thrown, it then 
 * selects the box that is the most fullest to be the next delivery.
 */

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
	
	//status of current delivery
	private boolean deliverying;

	
	/**
	 * Selects the next box to be ready for delivery
	 * @param summaries:
	 * @throws NoBoxReadyException: is thrown if all the boxes have not been used
	 * @return  the id of the next box for delivery
	 */
	@Override
	public String selectNextDelivery(Summary[] summaries) throws NoBoxReadyException {
		
		
		/*In the case where items to be delivered
		 * have ran out, and they have not filled up
		 * maxBoxes.
		 */
		if(getRemainingItems() < 1) {
			
			//we can stop
			if(summaries.length == 0) {
				
				deliverying = false;
			}
			else {
				deliverying = true;
			}
		}

		/* We can start selecting boxes if we reached our maxBox limit,
		 * otherwise if we are in the process of delivering the maxBoxes,
		 * we give them an opportunity to be selected. 
		 */
		if((summaries.length == maxBoxes || deliverying)) {
			
			// one box remaining, delivering stops afterwards 
			if(summaries.length == 1) {
				deliverying = false;
			}
			else {
				deliverying = true;
			}
			
			// select the box with the least amount of space available 
			StorageBox.Summary maxBoxSummary = summaries[0];
			for(StorageBox.Summary summary : summaries) {
				
				if(summary.remainingUnits < maxBoxSummary.remainingUnits) {
					maxBoxSummary = summary;
				}
				
				
			}


			this.currentBoxID = maxBoxSummary.identifier;
			// remove the box from storageTracker
			removeBoxTracker();
	
			return maxBoxSummary.identifier;
			
	
			
		}
		
		// otherwise no box is ready at the moment
		throw new NoBoxReadyException();
		
	}
	

	//get current boxID 
	public String getCurrentBoxID() {
		return currentBoxID;
	}
	
	
	/**
	 * Get the remaining amount of mail left to deliver
	 * @return amount of mail left to deliver
	 */
	public int getRemainingItems() {
		
		if(sortStrategy1 == null) {
			return sortStrategy2.getItemsRemaining();
		}
		return sortStrategy1.getItemsRemaining();
	}
	
	/**
	 * Removes the box that gets selected from the storageTracker
	 */
	public void removeBoxTracker() {
		
		if(sortStrategy1 == null) {
			sortStrategy2.getStorageTracker().remove(currentBoxID);
		}
		else {
			sortStrategy1.getStorageTracker().remove(currentBoxID);
		}
	}




}
