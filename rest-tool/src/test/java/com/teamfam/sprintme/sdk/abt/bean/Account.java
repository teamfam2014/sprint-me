package com.teamfam.sprintme.sdk.abt.bean;

import java.math.BigInteger;

public class Account {
	private BigInteger accountId;
	private String firstName;
	private String lastName;
	public BigInteger getAccountId() {
		return accountId;
	}
	public void setAccountId(BigInteger accountId) {
		this.accountId = accountId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
}
