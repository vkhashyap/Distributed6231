package client;

public class Teacher extends Record {

	public String tRecordId;
	public String firstname;
	public String lastName;
	public String address;
	public String phoneNum;
	public String specialization;
	public String location;

	public Teacher(String firstname, String lastName, String address, String phoneNum, String specialization,
			String location) {
		tRecordId = "TR100"; // Need to be incremented
		this.firstname = firstname;
		this.lastName = lastName;
		this.address = address;
		this.phoneNum = phoneNum;
		this.specialization = specialization;
		this.location = location;
		// System.out.println("Teacher Constructor");

	}

}
