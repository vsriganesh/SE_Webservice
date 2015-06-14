package com.iiitb.tr.workflow.util;

public class PasswordCreation {
	
	public String createPassword(String userName)
	{
		String part = Long.toHexString(Double.doubleToLongBits(Math.random()));
		
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		
		 byte[] bytes = userName.getBytes();
		   for (int i=0; i<bytes.length; i++) {
		        sb.append(String.format("%02X ",bytes[i]));
		    }
		
		   String[] temp = sb.toString().split(" ");
		   for(int i =0 ;i<temp.length;i++)
		    {
		    	sb1.append(temp[i].charAt(0));
		    }
		   
		   return part+sb1.toString();
	}
	


}
