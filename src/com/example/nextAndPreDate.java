package com.example;



import java.math.BigInteger;
import java.text.*;
import java.util.Calendar;
import java.util.*;

public class nextAndPreDate {
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
		
		int year1 = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		System.out.println("month"+month);
		String nextdate=getNextDate("2019-11-22");
		String predate=getPreDate("2019-11-22");
		String date="2019-11-22";
		String ss=date.substring(0, 7);
		System.out.println(ss);
		System.out.println("nextdate = "+nextdate);
		System.out.println("predate = "+predate);
		String year=getYear();
		System.out.println(year);
		BigInteger bsum=new BigInteger("0");
		for (int i = 0; i < 15; i++) {
			String ssc="15";
			BigInteger bbss=new BigInteger(ssc);
			bsum=bsum.add(bbss);
			
		}
		System.out.println("ooooo==>"+bsum);
	}
	public static String getNextDate(String  curDate) {
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		  final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		  Date date=new Date();
		try {
			date = format.parse(curDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  final Calendar calendar = Calendar.getInstance();
		  calendar.setTime(date);
		  calendar.add(Calendar.DAY_OF_YEAR, 1);
		  return format.format(calendar.getTime()); 
		}
	public static String getPreDate(String  curDate) {
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		  final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		  Date date=new Date();
		try {
			date = format.parse(curDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  final Calendar calendar = Calendar.getInstance();
		  calendar.setTime(date);
		  calendar.add(Calendar.DAY_OF_YEAR, -1);
		  return format.format(calendar.getTime()); 
		}
	public static String getYear() {
		 Date date=new Date();  
		 int year=date.getYear(); 
		 
		 String currentYear=year+1900+"";
		  
		return currentYear;
		
	}
}
