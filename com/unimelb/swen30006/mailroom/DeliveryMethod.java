/* Name: Rishabh Sharma
 * Student Number: 694739
 */

package com.unimelb.swen30006.mailroom;

import java.util.ArrayList;
import java.util.Collections;


import com.unimelb.swen30006.mailroom.exceptions.SourceExhaustedException;

/**This delivery strategy is based on the remaining items in the box,
 * where it sorts all of the mailItems by floors to be delivered 
 * to in the current box, and delivers to the smallest floor available. 
 */

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
			//add the ones that did not get picked back into the box
			for(MailItem item: mailItems) {
				box.addItem(item);
			}
			Collections.sort(possibleDestinations);

		} catch (Exception e) {
			System.out.println(e);
		}


		// return the smallest floor available 
		return possibleDestinations.get(0);





	}


}
