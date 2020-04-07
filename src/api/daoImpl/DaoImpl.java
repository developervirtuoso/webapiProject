package api.daoImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import common.database.DbConnection_Smpp;



public class DaoImpl {
	public void showTables(JSONArray jsonArray) {

		Connection con=DbConnection_Smpp.getInstance().getConnection();
		Statement smt=null;
		ResultSet rs=null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			smt=con.createStatement();
			rs=smt.executeQuery("Show tables");
			while(rs.next()){
				//System.out.println(rs.getString(1));
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("table_name", rs.getString(1));
				jsonArray.put(jsonObject);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


	}
	public  void InsertMongoDbByJson(DBCollection collection, JSONObject jsonObject){
	       String json = jsonObject.toString();
	            
	       DBObject dbObject = (DBObject)JSON.parse(json);
	     
	       collection.insert(dbObject);
	   }
	public void getColumnName(JSONArray jsonArray,String tablename) {

		Connection con=DbConnection_Smpp.getInstance().getConnection();
		Statement smt=null;
		ResultSet rs=null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			smt=con.createStatement();
			rs=smt.executeQuery("SHOW COLUMNS FROM "+tablename+"");
			while(rs.next()){
				//System.out.println(rs.getString(1));
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("Field", rs.getString("Field"));
				jsonArray.put(jsonObject);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


	}
	public void getTableData(JSONArray jsonArray,String tablename,String[] tcolumnname) {

		Connection con=DbConnection_Smpp.getInstance().getConnection();
		Statement smt=null;
		ResultSet rs=null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			smt=con.createStatement();
			rs=smt.executeQuery("Select * from "+tablename+"");
			while(rs.next()){
				//System.out.println(rs.getString(1));
				JSONObject jsonObject=new JSONObject();
				for(int i=0;i<tcolumnname.length;i++) {
					System.out.println("ddddd"+rs.getString(tcolumnname[i]));
					jsonObject.put(tcolumnname[i], rs.getString(tcolumnname[i])+" ");
				}
				
				jsonArray.put(jsonObject);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


	}
	public String getAllpanelDetails(String requested_date) {
		String jsonData="";
		try {
			HttpResponse<JsonNode> response = Unirest.post("http://49.50.64.122:6080/webapi/AllUserServices_SmppSupport?api_type=all_users_count_spanel")
					.header("Content-Type", "application/x-www-form-urlencoded")
					.field("requested_date", requested_date)
					.field("company_name", "")
					.asJson();
			jsonData=response.getBody().toString();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonData;
		
	}
	public String getAllSpanelDetails(String requested_date) {
		String jsonData="";
		try {
			HttpResponse<JsonNode> response = Unirest.post("http://49.50.86.152:6080/webapi/AllUserServices_SmppSupport?api_type=all_users_count_spanel")
					.header("Content-Type", "application/x-www-form-urlencoded")
					.field("requested_date", requested_date)
					.field("company_name", "")
					.asJson();
			jsonData=response.getBody().toString();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonData;
		
	} 
	
		public String getAllTpanelDetails(String requested_date,String companyname) {
		String jsonData="";
		try {
			HttpResponse<JsonNode> response = Unirest.post("http://182.18.144.234:6080/webapi/AllUserServices_SmppSupport?api_type=all_users_count_spanel")
					.header("Content-Type", "application/x-www-form-urlencoded")
					.field("requested_date", requested_date)
					.field("company_name", companyname)
					.asJson();
			jsonData=response.getBody().toString();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonData;
		
	}
	
}
