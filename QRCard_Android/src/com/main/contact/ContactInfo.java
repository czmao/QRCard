package com.main.contact;

import java.util.ArrayList;

public class ContactInfo {
	private String name;
	private ArrayList<String> phoneList;
	private ArrayList<String> emailList;
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
}
