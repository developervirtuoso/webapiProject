package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;

import com.google.gson.Gson;

import all.beans.Count_sms_user;
import common.database.DbConnection_SmppSpanel;

public class test1 {

	public static void main(String[] args) {
String req_date="2019-10-05";
String companyname="";
 	   System.out.println("fffffffffffffffffffffffffffff");
 	   Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
	   	Statement stmt = null;
	   	Statement stmt1 = null;
	   	ResultSet rs = null;
	   	ResultSet rs1 = null;
	   	try {
	        
	       stmt=connection.createStatement();
	       stmt1=connection.createStatement();
			
	       String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"%' and u.companyname like '"+companyname+"%' and s.errorcode=0  group by companyid order by team,companyname";
	     Gson gson = new Gson();
	       //logger.info("=========query=========="+query);
	       System.out.println("fffffff"+query);
	       
	    	rs = stmt.executeQuery(query);
		
	       
	       
	       	while (rs.next()) {
	       	 System.out.println("dddddddddd");
	       		Count_sms_user response_p = new Count_sms_user();
	      	 JSONObject jsonObject=new JSONObject();
	       
	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	       	response_p.setCompanyname(rs.getString("companyname"));
	       	response_p.setColor(rs.getString("color"));
	       	response_p.setUsername(rs.getString("username"));
	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	       	response_p.setRequested_date(req_date);
	     
	       	
	 	if(rs.getString("servername")!=null) {
	       	response_p.setServername(rs.getString("servername"));
	     jsonObject.put("servername", rs.getString("servername"));
	       	}
	       	else {
	       		response_p.setServername("NA");	
	         jsonObject.put("servername", "NA");
	       	}
	     jsonObject.put("companyid", rs.getInt("companyid")+"");
	   	  jsonObject.put("companyname", rs.getString("companyname"));
	   	  jsonObject.put("color", rs.getString("color"));
	   	  jsonObject.put("username", rs.getString("username"));
	   	  jsonObject.put("s_count", rs.getInt("s_count")+"");
	   	  jsonObject.put("s_date", req_date);
					/*
					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
					 */
	       	query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+req_date+"%' and d.errorcode=0 and d.companyid = "+response_p.getCompanyid()+";"; 
	       	
	   
	       	rs1  = stmt1.executeQuery(query);
	        if(rs1.isBeforeFirst()){
		       	while (rs1.next()) {
		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
		     jsonObject.put("d_count", rs1.getInt("d_count")+"");
		       	}
	        }
	       	
	       	
	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	       	int companyid=rs.getInt("companyid");
	   
	       	
	    }
	           
	     }catch(Exception e){
	     	e.printStackTrace();
	     }finally{
	   	try {
	   	        if (connection != null)
	   	     	connection.close();
	   	     } catch (SQLException ignore) {} // no point handling
		try {
   	        if (stmt != null)
   	        	stmt.close();
   	     } catch (SQLException ignore) {} // no point handling
		try {
   	        if (rs != null)
   	        	rs.close();
   	     } catch (SQLException ignore) {} // no point handling
		try {
   	        if (rs1 != null)
   	        	rs1.close();
   	     } catch (SQLException ignore) {} // no point handling

	   	}
    
	}
}
