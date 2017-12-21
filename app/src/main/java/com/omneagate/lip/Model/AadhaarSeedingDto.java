package com.omneagate.lip.Model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class AadhaarSeedingDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Primary Key column. */
	private Long id;

	private Long beneficiaryID;

	private Integer serialNum;

	private String channel;
	
	/** created  on date and time of sms Template*/
	private Date createdDate;
	
	/**to send the ration-card number*/
	String rationCardNumber;
		
	/** getting from adhar QR code*/
	String uid;
	
	String name;

	Character gender;

	Long yob;

	String co;

	String house;

	String street ;

	String lm;

	String loc;

	String vtc;

	String po;

	String dist;

	String subdist;

	String state;

	String pc;

    Long dob;

}
