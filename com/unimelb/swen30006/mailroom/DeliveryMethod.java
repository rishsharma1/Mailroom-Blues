package com.unimelb.swen30006.mailroom;

import java.util.HashMap;

import com.unimelb.swen30006.mailroom.exceptions.SourceExhaustedException;

public class DeliveryMethod implements DeliveryStrategy {
	
	private SelectionMethod selectionMethod;
	
	public DeliveryMethod(SelectionStrategy selectionStrategy) {
		this.selectionMethod = (SelectionMethod) selectionStrategy;
	}

	@Override
	public int chooseNextFloor(int currentFloor, StorageBox box) throws SourceExhaustedException {
		
		int itemsAbove = 0;
		int itemsBelow = 0;
		int closestFloorAbove = 0;
		int closestFloorBelow = 0;
				
		HashMap<Integer,Integer> currentBoxStats = selectionMethod.getCurrentBoxStats();
		
		for(Integer floor: currentBoxStats.keySet()) {
			
			
			if(currentBoxStats.get(floor) > currentFloor){
	
				itemsAbove += currentBoxStats.get(floor);
				
				if(closestFloorAbove == 0) {
					closestFloorAbove = floor;
					
				}
				else if(floor < closestFloorAbove) {
					closestFloorAbove = floor;
				}
			}
			else {
				itemsBelow += currentBoxStats.get(floor);
				
				if(closestFloorBelow == 0) {
					closestFloorBelow = floor;
				}
				else if(floor > closestFloorBelow) {
					closestFloorBelow = floor;
				}
			}
		}

		if(itemsAbove > itemsBelow) {
			currentBoxStats.remove(closestFloorAbove);


			return closestFloorAbove;
		}
		else if(itemsAbove < itemsBelow) {
			currentBoxStats.remove(closestFloorBelow);


			return closestFloorBelow;
		}
		
		return -1;
		
		
	}
	

}
