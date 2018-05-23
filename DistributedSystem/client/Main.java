package client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {

	static HashMap<String, ArrayList<Record>> hashMapDB = new HashMap<String, ArrayList<Record>>();
	static ArrayList<Record> alRecord = new ArrayList<Record>();
	static int count = 0;

	public static void main(String[] args) {
		createSRecord();
		createTRecord();
		getRecordCounts();
		// editRecords();
	}

	// Returns a substring containing all characters after a string.
	static String returnStringAfterDot(String value, String a) {
		int posA = value.lastIndexOf(a);
		if (posA == -1) {
			return "";
		}
		int adjustedPosA = posA + a.length();
		if (adjustedPosA >= value.length()) {
			return "";
		}
		return value.substring(adjustedPosA);
	}

	public static void getDataFromRunTimeClass(Object obj) {
		Class<?> objClass = obj.getClass();
		Field[] fields = objClass.getFields();
		for (Field field : fields) {
			String name = field.getName();
			Object value = null;
			try {
				value = field.get(obj);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			System.out.println(name + ": " + value.toString());
		}
		count = count + 1;
	}

	public static void createSRecord() {
		// prompt data from manager
		String lastname = "last";
		Date dateNow = new Date();
		Record objRecord = new Student("sFirst", "sLast", "sMasters", "sActive", dateNow);
		if (hashMapDB.containsKey(lastname.substring(0, 1))) {
			hashMapDB.get(lastname.substring(0, 1)).add(objRecord);
		} else {
			ArrayList<Record> alRecord = new ArrayList<Record>();
			alRecord.add(objRecord);
			hashMapDB.put(lastname.substring(0, 1), alRecord);
		}
		// alRecord.add(objRecord);
		System.out.println("\nCreated Student Object");
	}

	public static void createTRecord() {
		// prompt data from manager
		String teacherLastName = "last";
		Record objRecord = new Teacher("tFirst", "tLast", "tAddress", "1234567890", "tSpecial", "tLocation");
		if (hashMapDB.containsKey(teacherLastName.substring(0, 1))) {
			hashMapDB.get(teacherLastName.substring(0, 1)).add(objRecord);
		} else {
			ArrayList<Record> alRecord = new ArrayList<Record>();
			alRecord.add(objRecord);
			hashMapDB.put(teacherLastName.substring(0, 1), alRecord);
		}
		// alRecord.add(objRecord);
		System.out.println("\nCreated Teacher Object");
	}

	public static void getRecordCounts() {
		for (Map.Entry<String, ArrayList<Record>> map : hashMapDB.entrySet()) {

			for (Record eachRec : map.getValue()) {
				if (returnStringAfterDot(eachRec.getClass().getName(), ".").equals("Student")) {
					System.out.println("\nRetrieving Student Data");
					getDataFromRunTimeClass(eachRec);

				} else if (returnStringAfterDot(eachRec.getClass().getName(), ".").equals("Teacher")) {
					System.out.println("\nRetrieving Teacher Data");
					getDataFromRunTimeClass(eachRec);
				}
			}
		}
		System.out.println("\nTotal No.of records retrieved:" + count);
	}

}
