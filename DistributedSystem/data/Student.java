package client;

import java.util.Date;

public class Student extends Record {

	public String strRecordId;
	public String firstname;
	public String lastName;
	public String courseRegistered;
	public String status;
	public Date statusDate;

	public Student(String firstname, String lastName, String courseRegistered, String status, Date date) {
		strRecordId = "SR100"; // Need to be incremented
		this.firstname = firstname;
		this.lastName = lastName;
		this.courseRegistered = courseRegistered;
		this.status = status;
		this.statusDate = date;
		// System.out.println("Student Constructor");

	}

}
