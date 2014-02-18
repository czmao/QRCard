package com.main.contact;

import java.util.ArrayList;

public class ContactInfo {
	private ContactName name;
	private ArrayList<ContactPhone> phoneList;
	private ArrayList<ContactEmail> emailList;
//	private ArrayList<ContactAddress> addressList;
	
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
	
//	public ArrayList<ContactAddress> getAddressList(){
//		if(addressList==null){
//			return new ArrayList<ContactAddress>();
//		}
//		return addressList;
//	}
//	
//	public void setAddressList(ArrayList<ContactAddress> addressList){
//		this.addressList = addressList;
//	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Name: %s", name.getValue()));
		sb.append("\n");
		
		if(!getPhoneList().isEmpty())
		{
			sb.append("Phone:");
			for(ContactPhone phone : phoneList)
			{
				sb.append(String.format("%s", phone.getValue()));
				sb.append("  ");
			}
		}
		
		if(!getEmailList().isEmpty())
		{
			sb.append("Email:");
			for(ContactEmail email : emailList)
			{
				sb.append(String.format("%s", email.getValue()));
				sb.append("  ");
			}
		}
		
		return sb.toString(); 
	}
}
