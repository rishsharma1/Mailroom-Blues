package com.unimelb.swen30006.mailroom;

import java.util.ArrayList;
import java.util.Collections;


import com.unimelb.swen30006.mailroom.exceptions.SourceExhaustedException;

public class DeliveryMethod implements DeliveryStrategy {
	
	

	@Override
	public int chooseNextFloor(int currentFloor, StorageBox box) throws SourceExhaustedException {
		

		ArrayList<Integer> possibleDestinations = new ArrayList<Integer>();
		ArrayList<MailItem> mailItems = new ArrayList<MailItem>();
		
		try {
			
			while(!box.isEmpty()) {
			
				MailItem item = box.popItem();
				possibleDestinations.add(item.floor);
				mailItems.add(item);
			
			}
			for(MailItem item: mailItems) {
				box.addItem(item);
			}
			Collections.sort(possibleDestinations);
			
		} catch (Exception e) {
			System.out.println(e);
		}

		

		return possibleDestinations.get(0);

		

	
		
	}
	

}
