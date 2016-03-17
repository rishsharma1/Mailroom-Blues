package com.unimelb.swen30006.mailroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.unimelb.swen30006.mailroom.exceptions.DuplicateIdentifierException;
import com.unimelb.swen30006.mailroom.exceptions.MailOverflowException;
import com.unimelb.swen30006.mailroom.exceptions.UnknownIdentifierException;
/* This sorting strategy will try to create a storageBox for each of
the floors, and store mail that belong to a specific floor together */
public class SortingMethodOne implements SortingStrategy {

	//Keeps track of what is stored where, The key is the floor, and the value
	//is a StorageBox
	private HashMap<Integer, ArrayList<String>> storageTracker;

	public SortingMethodOne() {
		storageTracker = new HashMap<Integer, ArrayList<String>>();
	}


	@Override
	public String assignStorage(MailItem item, MailStorage storage) throws MailOverflowException {

		//Grab the floor the item needs to be delivered to
		int itemFloor = item.floor;


		if(storageTracker.containsKey(itemFloor)) {
			
			//an array list of boxes that belong to a certain floor 
			ArrayList<String> boxIdentifiers = storageTracker.get(itemFloor);
			StorageBox box;
				
			for(int i=0;i<boxIdentifiers.size();i++) {
				
					
				try {
					box = storage.retrieveBox(boxIdentifiers.get(i));
					
					if(box.canHold(item)) {
						return boxIdentifiers.get(i);
					}
				} catch (UnknownIdentifierException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

					
			}
				

				

		}
		
		//There is not a box for a floor, let's create one 
		String id = UUID.randomUUID().toString();
		try {
			storage.createBox(id);
			ArrayList<String> boxIdentifiers = new ArrayList<String>();
			boxIdentifiers.add(id);
			storageTracker.put(itemFloor, boxIdentifiers);
			return id;
		} catch (DuplicateIdentifierException e) {
            System.out.println(e);
            System.exit(0);
		}
		
		return null;
		

	}

}
