package com.rsc.bhopal.utills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SerialRangeValidation {
	private enum SerialObjects {
		PREVIOUS_START_SERIAL,
		START_SERIAL,
		PREVIOUS_CURRENT_SERIAL,
		CURRENT_SERIAL,
		PREVIOUS_END_SERIAL,
		END_SERIAL
	};
	private long prevCurrentSerial;
	private long prevStartSerial;
	private long prevEndSerial;
	private String currentSerial;
	private String startSerial;
	private String endSerial;
	private HashMap<SerialObjects, Long> hashMap;

	public SerialRangeValidation(long prevCurrentSerial, long prevStartSerial, long prevEndSerial,
			String currentSerial, String startSerial, String endSerial) {
		this.prevCurrentSerial = prevCurrentSerial;
		this.prevStartSerial = prevStartSerial;
		this.prevEndSerial = prevEndSerial;
		this.currentSerial = currentSerial.trim();
		this.startSerial = startSerial.trim();
		this.endSerial = endSerial.trim();

		hashMap = new HashMap<SerialObjects, Long>();
		hashMap.put(SerialObjects.PREVIOUS_START_SERIAL, this.prevStartSerial);
		hashMap.put(SerialObjects.START_SERIAL, Long.parseLong(this.startSerial));
		hashMap.put(SerialObjects.PREVIOUS_CURRENT_SERIAL, this.prevCurrentSerial);
		hashMap.put(SerialObjects.CURRENT_SERIAL, Long.parseLong(this.currentSerial));
		hashMap.put(SerialObjects.PREVIOUS_END_SERIAL, this.prevEndSerial);
		hashMap.put(SerialObjects.END_SERIAL, Long.parseLong(this.endSerial));
	}

	public boolean validate() {
		List<HashMap.Entry<SerialObjects, Long>> sortedHashMap = new ArrayList<HashMap.Entry<SerialObjects, Long>>(hashMap.entrySet());
		sortedHashMap.sort(HashMap.Entry.comparingByValue());
		sortedHashMap.forEach(System.out::println);

		return ((sortedHashMap.get(0).getKey() == SerialObjects.PREVIOUS_START_SERIAL) || (sortedHashMap.get(0).getKey() == SerialObjects.START_SERIAL))
				&&
				((sortedHashMap.get(1).getKey() == SerialObjects.PREVIOUS_START_SERIAL) || (sortedHashMap.get(1).getKey() == SerialObjects.START_SERIAL))
				&&
				((sortedHashMap.get(2).getKey() == SerialObjects.PREVIOUS_CURRENT_SERIAL) || (sortedHashMap.get(2).getKey() == SerialObjects.CURRENT_SERIAL))
				&&
				((sortedHashMap.get(3).getKey() == SerialObjects.PREVIOUS_CURRENT_SERIAL) || (sortedHashMap.get(3).getKey() == SerialObjects.CURRENT_SERIAL))
				&&
				((sortedHashMap.get(4).getKey() == SerialObjects.PREVIOUS_END_SERIAL) || (sortedHashMap.get(4).getKey() == SerialObjects.END_SERIAL))
				&&
				((sortedHashMap.get(5).getKey() == SerialObjects.PREVIOUS_END_SERIAL) || (sortedHashMap.get(5).getKey() == SerialObjects.END_SERIAL));
	}
}
