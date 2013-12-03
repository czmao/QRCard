package com.main.contact;

import java.util.ArrayList;

public class ContactInfo {
	private ContactName name;
	private ArrayList<ContactPhone> phoneList;
	private ArrayList<ContactEmail> emailList;
	private ArrayList<ContactAddress> addresslList;
	
	public ContactName getName(){
		return name;
	}
	
	public void setName(ContactName name){
		this.name = name;
	}
	
	public ArrayList<ContactPhone> getPhoneList(){
		if(phoneList==null){
			return new ArrayList<ContactPhone>();
		}
		return phoneList;
	}
	
	public void setPhoneList(ArrayList<ContactPhone> phoneList){
		this.phoneList = phoneList;
	}
	
	public ArrayList<ContactEmail> getEmailList(){
		if(emailList==null){
			return new ArrayList<ContactEmail>();
		}
		return emailList;
	}
	
	public void setEmailList(ArrayList<ContactEmail> emailList){
		this.emailList = emailList;
	}
	
	public ArrayList<ContactAddress> getAddressList(){
		if(addresslList==null){
			return new ArrayList<ContactAddress>();
		}
		return addresslList;
	}
	
	public void setAddressList(ArrayList<ContactAddress> addresslList){
		this.addresslList = addresslList;
	}
}
