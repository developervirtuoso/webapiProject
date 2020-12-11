package api.daoImpl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import response_pojo.pojo_Complaint;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import all.beans.BulkMismatchBeans;
import all.beans.Count_sms_user;
import all.beans.Count_sms_user1;
import all.beans.Pojo_Count_Submitted;
import all.beans.SentBoxBulkFiles;
import all.beans.SmppUser;
import all.beans.SmppUserDetails;
import api.servlet.MyRunnable;
import common.database.DbConnectionMongo;
import common.database.DbConnection_All;
import common.database.DbConnection_ReportData;
import common.database.DbConnection_Search;
import common.database.DbConnection_Smpp;
import common.database.DbConnection_SmppSpanel;
import common.database.DbConnection_Trafic;
import common.database.DbConnection_on_off;
import mylibs.ApiCons;
import mylibs.MyRunnable_SyncEmail;
import response_pojo.pojo_admin_user_Complaints_count;
import superAdmin.servlet.smppSignIn;





public class Smpp_DaoImpl {
	public static void main(String[] args) {
		Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
		String date="2020-12-08";
		String date1=daoImpl.getNextDate(date,1);
		String date2=daoImpl.getNextDate(date,2);
		String pdate="p"+date.replace("-", "")+",p"+date1.replace("-", "")+",p"+date2.replace("-", "");
		System.out.println(pdate);
	}

	final static Logger logger = Logger.getLogger(Smpp_DaoImpl.class);
	


	public void all_delivered_count(String req_date,String company_id,JSONArray jsonArray)
	{
		
		
		try {
			
	       
	    	   Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	   	Statement stmt = null;
	   	   	Smpp_DaoImpl smpp_DaoImpl=new Smpp_DaoImpl();
	   	   	ResultSet rs = null;

	   	   	try {
	   	        
	   	       stmt=connection.createStatement();
	   	      
	   			/*
	   			 * String query =
	   			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
	   			 * +req_date+"'  group by companyid;;";
	   			 */
	   	       String query = "select s.count as s_count,s.errorcode, e.Description   from tbl_delivered s  left join errorcode e on s.errorcode=e.Error_Code where  s.date  like '"+req_date+"%' and s.companyid ="+company_id+"  and s.errorcode!=0;";
	   	       rs = stmt.executeQuery(query);
	   	       Gson gson = new Gson();
	   	       ////logger.info("delivered query"+query);
	   	      
	   	        if(rs.isBeforeFirst()){
	   	       	while (rs.next()) {
	   	       		JSONObject jsonObject=new JSONObject();
	   	       		Pojo_Count_Submitted response_p = new Pojo_Count_Submitted();
	   	       	response_p.setCount(rs.getInt("s_count")+"");
	   	       	response_p.setError_code(rs.getString("errorcode"));
	   	       
	   	 	if(rs.getString("Description")!=null) {
	   	       	response_p.setError_des(rs.getString("Description"));
	   	     jsonObject.put("error_des", rs.getString("Description"));
	   	       	}
	   	       	else
	   	       	{
	   	       		response_p.setError_des("");
	   	       	jsonObject.put("error_des", "");
	   	       	}
	   	     jsonObject.put("error_code", rs.getString("errorcode"));
				jsonObject.put("count", rs.getInt("s_count")+"");
				jsonObject.put("req_date", req_date);
				jsonObject.put("company_id", company_id);
				
	   	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	   	       
	   	   
	   	        jsonArray.put(jobj);
	   	       	
	   	    }
	   	           }else{
	   	           
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
	   		

	   	   	}
	       
		} catch (Exception e) {
			// TODO: handle exception
		}
	   
	   }
	public void Panel_deliver_DeleteMongiDbData(String req_date, String company_id, DBCollection collection) {
		try {
			BasicDBObject document = new BasicDBObject();
			document.put("req_date", req_date);
			document.put("company_id", company_id);
			collection.remove(document);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public void all_delivered_countSpanel(String req_date,String company_id,JSONArray jsonArray)
	{
		
		Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
		boolean checkmongodata=false;
		 MongoClient mongo = DbConnectionMongo.getInstance().getConnection();
		 DB db = mongo.getDB("count_sms");
		 DBCollection collection = db.getCollection("DeliveredError");
		 DBCursor cursor1 = collection.find(new BasicDBObject().append("company_id", company_id).append("req_date", req_date));
		try {
			
		
	       while(cursor1.hasNext()) {
	    	   checkmongodata=true;
	         //  //logger.info(cursor1.next());
	    	   String data=cursor1.next().toString();
	    	   ////logger.info(data);
	          JSONObject jsonObject1;
			try {
				jsonObject1 = new JSONObject(data);
		         
		         
		          JSONObject jsonObject=new JSONObject();
	         		jsonObject.put("error_code", jsonObject1.getString("error_code"));
					jsonObject.put("count", jsonObject1.getString("count"));
					jsonObject.put("error_des", jsonObject1.getString("error_des"));
					
					jsonArray.put(jsonObject);
		        
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
	       }
	       if(checkmongodata==false) {
	    		Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
	    	   	Statement stmt = null;
	    	   	Smpp_DaoImpl smpp_DaoImpl=new Smpp_DaoImpl();
	    	   	ResultSet rs = null;

	    	   	try {
	    	        
	    	       stmt=connection.createStatement();
	    	      
	    			/*
	    			 * String query =
	    			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
	    			 * +req_date+"'  group by companyid;;";
	    			 */
	    	       String query = "select s.count as s_count,s.errorcode, e.Description   from tbl_delivered s  left join errorcode e on s.errorcode=e.Error_Code where  s.date  like '"+req_date+"%' and s.companyid ="+company_id+"  and s.errorcode!=0;";
	    	       rs = stmt.executeQuery(query);
	    	       Gson gson = new Gson();
	    	       ////logger.info("delivered query"+query);
	    	       
	    	       if(rs !=null) {
	    	    	   spanel_deliver_mongodb(req_date,company_id,collection);
	    	       }
	    	        if(rs.isBeforeFirst()){
	    	       	while (rs.next()) {
	    	       		JSONObject jsonObject=new JSONObject();
	    	       		Pojo_Count_Submitted response_p = new Pojo_Count_Submitted();
	    	       	response_p.setCount(rs.getInt("s_count")+"");
	    	       	response_p.setError_code(rs.getString("errorcode"));
	    	       	if(rs.getString("Description")!=null) {
	    	   	       	response_p.setError_des(rs.getString("Description"));
	    	   	     jsonObject.put("error_des", rs.getString("Description"));
	    	   	       	}
	    	   	       	else
	    	   	       	{
	    	   	       		response_p.setError_des("");
	    	   	       	jsonObject.put("error_des", "");
	    	   	       	}
	    	   	     jsonObject.put("error_code", rs.getString("errorcode"));
	    				jsonObject.put("count", rs.getInt("s_count")+"");
	    				jsonObject.put("req_date", req_date);
	    				jsonObject.put("company_id", company_id);
	    				
	    			 	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	    	   	     daoImpl.all_submitted_countMongo(jsonObject,collection,req_date,company_id);
	    	       
	    	      
	    	        jsonArray.put(jobj);
	    	       	
	    	    }
	    	           }else{
	    	           
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
	    		

	    	   	}
	       }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {try {
			if(cursor1!=null)
				cursor1.close();

				// mongo.close();

				} catch (Exception e2) {
				// TODO: handle exception
				}
				
				try {
				if(mongo!=null)
				mongo.close();

				// mongo.close();

				} catch (Exception e2) {
				// TODO: handle exception
				}
}
	   
	   }
	public void spanel_deliver_mongodb(String req_date, String company_id, DBCollection collection) {
	
		try {
			BasicDBObject document = new BasicDBObject();
			document.put("req_date", req_date);
			document.put("company_id", company_id);
			collection.remove(document);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void getUserDetailsData(JSONArray jsonArray) {

	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;
	   	ResultSet rs = null;

	   	try {
	        
	       stmt=connection.createStatement();
	      
		
	       String query = "SELECT * FROM userdetails;";
	       rs = stmt.executeQuery(query);
	       Gson gson = new Gson();
	       //logger.info("delivered query"+query);
	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		
	       		SmppUserDetails response_p = new SmppUserDetails();
	       	response_p.setId(rs.getString("id"));
	       	response_p.setCompanyid(rs.getString("companyid"));
	       	response_p.setParentid(rs.getString("parentid"));
	       	response_p.setUsername(rs.getString("username"));
	       	response_p.setCompanyname(rs.getString("companyname"));
	       	response_p.setColor(rs.getString("color"));
	       	response_p.setTeam(rs.getString("team"));
	       	response_p.setUserid(rs.getString("userid"));
	       	response_p.setAdminid(rs.getString("userid"));
	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	        jsonArray.put(jobj);
	       	
	    }
	           }else{
	           
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
		

	   	}
	   
	   
	}
	public void getAllUserData(JSONArray jsonArray) {

	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;
	   	ResultSet rs = null;

	   	try {
	        
	       stmt=connection.createStatement();
	      
		
	       String query = "SELECT * FROM username_tb;";
	       rs = stmt.executeQuery(query);
	       Gson gson = new Gson();
	       //logger.info("delivered query"+query);
	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		
	       		SmppUser response_p = new SmppUser();
	       	response_p.setId(rs.getInt("id"));
	       	response_p.setName(rs.getString("name"));
	       	response_p.setPassword(rs.getString("password"));
	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	        jsonArray.put(jobj);
	       	
	    }
	           }else{
	           
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
		

	   	}
	   
	   
	}
	public void getAllUserDetailsData(JSONArray jsonArray,String userid) {

	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;
	   	ResultSet rs = null;

	   	try {
	        
	       stmt=connection.createStatement();
	      
		
	       String query = "SELECT * FROM userdetails where userid="+userid+";";
	       rs = stmt.executeQuery(query);
	       Gson gson = new Gson();
	       //logger.info("delivered query"+query);
	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		
	       		SmppUserDetails response_p = new SmppUserDetails();
	       	response_p.setId(rs.getString("id"));
	       	response_p.setCompanyid(rs.getString("companyid"));
	       	response_p.setParentid(rs.getString("parentid"));
	       	response_p.setUsername(rs.getString("username"));
	       	response_p.setCompanyname(rs.getString("companyname"));
	       	response_p.setColor(rs.getString("color"));
	       	response_p.setTeam(rs.getString("team"));
	       	response_p.setUserid(rs.getString("userid"));
	       	response_p.setAdminid(rs.getString("adminid"));
	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	        jsonArray.put(jobj);
	       	
	    }
	           }else{
	           
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
		

	   	}
	   
	   
	}
	public String getAllUserDetailsData(String userid) {

	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;
	   	ResultSet rs = null;
	   	String username="";
	   	try {
	        
	       stmt=connection.createStatement();
	      
		
	       String query = "SELECT * FROM username_tb where id="+userid+";";
	       rs = stmt.executeQuery(query);
	       //logger.info("getAllUserDetailsData query"+query);
	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		username=rs.getString("name");
	       	
	    }
	           }else{
	           
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
		

	   	}
		return username;
	   
	   
	}
	public boolean checkUserPassword(String userid,String oldpass) {

	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;
	   	ResultSet rs = null;
	   	boolean checkpass=false;
	   	try {
	        
	       stmt=connection.createStatement();
	      
		
	       String query = "SELECT * FROM username_tb where id="+userid+" and password='"+oldpass+"';";
	       rs = stmt.executeQuery(query);
	       Gson gson = new Gson();
	       //logger.info("delivered query"+query);
	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		checkpass=true;
	       		
	       	}
	           }else{
	           
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
		

	   	}
		return checkpass;
	   
	   
	}
	
	
	public void getallUsernameData(JSONArray jsonArray) {

	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;
	   	ResultSet rs = null;

	   	try {
	        
	       stmt=connection.createStatement();
	      
		
	       String query = "SELECT * FROM username_tb;";
	       rs = stmt.executeQuery(query);
	       Gson gson = new Gson();
	       //logger.info("delivered query"+query);
	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		
	       		SmppUser response_p = new SmppUser();
	       	response_p.setId(rs.getInt("id"));
	       	response_p.setName(rs.getString("name"));
	       	response_p.setPassword(rs.getString("password"));
	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	        jsonArray.put(jobj);
	       	
	    }
	           }else{
	           
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
		

	   	}
	   
	   
	}
	public String createBeforeMonthDate() {
		   Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, -30);
			Date tomorrow = calendar.getTime();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String yesterdayAsString = dateFormat.format(tomorrow);
			return yesterdayAsString;
		}
	
	
	public String getErrorDes(int errorcode) {
	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;
	   	Smpp_DaoImpl smpp_DaoImpl=new Smpp_DaoImpl();
	   	ResultSet rs = null;
	   	String errordescription="";
	   	try {
	        
	       stmt=connection.createStatement();
	      
			/*
			 * String query =
			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
			 * +req_date+"'  group by companyid;;";
			 */
	       String query = "select *   from errorcode where  Error_Code="+errorcode+"";
	       rs = stmt.executeQuery(query);
	      

	       	while (rs.next()) {
	       		
	       		errordescription=rs.getString("Description");
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
		

	   	}
	   return errordescription;
	   }




	public void all_submitted_count(String req_date,String company_id,JSONArray jsonArray)
	{
		
		try {
			
		
	       
	    	   Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	   	Statement stmt = null;
	   	   
	   	 	ResultSet rs = null;

  	   	try {
	   	        
	   	       stmt=connection.createStatement();
	   	      
	   			/*
	   			 * String query =
	   			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
	   			 * +req_date+"'  group by companyid;;";
	   			 */
	   	       String query = "select s.count as s_count,s.errorcode,e.Description   from tbl_submitted s left join errorcode e on s.errorcode=e.Error_Code  where  s.date  like '"+req_date+"%' and s.companyid ="+company_id+"  and s.errorcode!=0;";
	   	       rs = stmt.executeQuery(query);
	   	       Gson gson = new Gson();

	   	       
	   	       //logger.info("submitted query"+query);
	   	     
	   	        if(rs.isBeforeFirst()){
	   	       	while (rs.next()) {
	   	       		JSONObject jsonObject=new JSONObject();
	   	       		Pojo_Count_Submitted response_p = new Pojo_Count_Submitted();
	   	       	response_p.setCount(rs.getInt("s_count")+"");
	   	       	response_p.setError_code(rs.getString("errorcode"));
	   	       	if(rs.getString("Description")!=null) {
	   	       	response_p.setError_des(rs.getString("Description"));
	   	     jsonObject.put("error_des", rs.getString("Description"));
	   	       	}
	   	       	else
	   	       	{
	   	       		response_p.setError_des("");
	   	       	jsonObject.put("error_des", "");
	   	       	}
	   	     jsonObject.put("error_code", rs.getString("errorcode"));
				jsonObject.put("count", rs.getInt("s_count")+"");
				jsonObject.put("req_date", req_date);
				jsonObject.put("company_id", company_id);
				
	   	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	   	       jsonArray.put(jobj);
	   	       	
	   	    }
	   	           }else{
	   	           
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
	   		

	   	   	} 
	       
		} catch (Exception e) {
			// TODO: handle exception
		}
	   	
	   
	   }
	public void Panel_submitted_DeleteMongiDbData(String req_date, String company_id, DBCollection collection) {
		try {
			BasicDBObject document = new BasicDBObject();
			document.put("req_date", req_date);
			document.put("company_id", company_id);
			collection.remove(document);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public void all_submitted_countMongo(JSONObject jsonObject, DBCollection collection, String req_date,
			String company_id) {
		String json=jsonObject.toString();
		DBObject dbObject = (DBObject)JSON.parse(json);
	     
	       collection.insert(dbObject);
		 //  collection.update(new BasicDBObject().append("company_id", company_id).append("req_date", req_date), dbObject, true, false  );

		// TODO Auto-generated method stub
		
	}
	public void all_submitted_countSpanel(String req_date,String company_id,JSONArray jsonArray)
	{
		
		Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
		boolean checkmongodata=false;
		 MongoClient mongo = DbConnectionMongo.getInstance().getConnection();
		 DB db = mongo.getDB("count_sms");
		 DBCollection collection = db.getCollection("submittedError");
		 DBCursor cursor1 = collection.find(new BasicDBObject().append("company_id", company_id).append("req_date", req_date));
		try {
			
		
	       while(cursor1.hasNext()) {
	    	   checkmongodata=true;
	         //  //logger.info(cursor1.next());
	    	   String data=cursor1.next().toString();
	    	   ////logger.info(data);
	          JSONObject jsonObject1;
			try {
				jsonObject1 = new JSONObject(data);
		         
		         
		          JSONObject jsonObject=new JSONObject();
	         		jsonObject.put("error_code", jsonObject1.getString("error_code"));
					jsonObject.put("count", jsonObject1.getString("count"));
					jsonObject.put("error_des", jsonObject1.getString("error_des"));
					
					jsonArray.put(jsonObject);
		        
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
	       }
	       if(checkmongodata==false) {
	    	   Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
	   	   	Statement stmt = null;
	   	   
	   	   	ResultSet rs = null;

	   	   	try {
	   	        
	   	       stmt=connection.createStatement();
	   	      
	   			/*
	   			 * String query =
	   			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
	   			 * +req_date+"'  group by companyid;;";
	   			 */
	   	       String query = "select s.count as s_count,s.errorcode,e.Description   from tbl_submitted s left join errorcode e on s.errorcode=e.Error_Code  where  s.date  like '"+req_date+"%' and s.companyid ="+company_id+"  and s.errorcode!=0;";
	   	       rs = stmt.executeQuery(query);
	   	       Gson gson = new Gson();

	   	       
	   	       ////logger.info("submitted query"+query);
	   	       if(rs !=null) {
	   	    	   Spanel_submit_deletemongodb(req_date,company_id,collection);
	   	       }
	   	       
	   	        if(rs.isBeforeFirst()){
	   	       	while (rs.next()) {
	   	     	JSONObject jsonObject=new JSONObject();
	   	       		Pojo_Count_Submitted response_p = new Pojo_Count_Submitted();
	   	       	response_p.setCount(rs.getInt("s_count")+"");
	   	       	response_p.setError_code(rs.getString("errorcode"));
	   	       	if(rs.getString("Description")!=null) {
	   	       	
	   	       	if(rs.getString("Description")!=null) {
	   	   	       	response_p.setError_des(rs.getString("Description"));
	   	   	     jsonObject.put("error_des", rs.getString("Description"));
	   	   	       	}
	   	   	       	else
	   	   	       	{
	   	   	       		response_p.setError_des("");
	   	   	       	jsonObject.put("error_des", "");
	   	   	       	}
	   	   	     jsonObject.put("error_code", rs.getString("errorcode"));
	   				jsonObject.put("count", rs.getInt("s_count")+"");
	   				jsonObject.put("req_date", req_date);
	   				jsonObject.put("company_id", company_id);
	   				
	   			 	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	   	   	     daoImpl.all_submitted_countMongo(jsonObject,collection,req_date,company_id);
	   	      
	   	        jsonArray.put(jobj);
	   	       	
	   	    }
	   	       	}  }else{
	   	           
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
	   		

	   	   	}
	    	   }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {try {
			if(cursor1!=null)
				cursor1.close();

				// mongo.close();

				} catch (Exception e2) {
				// TODO: handle exception
				}
				
				try {
				if(mongo!=null)
				mongo.close();

				// mongo.close();

				} catch (Exception e2) {
				// TODO: handle exception
				}}
	   	
	   
	   }
	
	
	
	public void Spanel_submit_deletemongodb(String req_date, String company_id, DBCollection collection) {
		
			try {
				BasicDBObject document = new BasicDBObject();
				document.put("req_date", req_date);
				document.put("company_id", company_id);
				collection.remove(document);
			} catch (Exception e) {
				// TODO: handle exception
			}
		
	}
	public static int max_Email_Number()
	{
		int return_value = 0;
	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;

	   	ResultSet rs = null;

	   	try {
	        
	       stmt=connection.createStatement();
	       String query = "select max(email_num) as count from table_mail;";
	       rs = stmt.executeQuery(query);
	      
	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		return_value = rs.getInt("count");
	       	
	    }
	           }else{
	           
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
		

	   	}
	   	
	   	return return_value;
	   
	   }
	
	public void all_users_count_byCompanyname(String req_date,JSONArray jsonArray,String companyname)
	{
	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;
	   	Statement stmt1 = null;
	   	ResultSet rs = null;
	   	ResultSet rs1 = null;
	   	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
	   	String beforemonthdate=daoImpl.createBeforeMonthDate();
	   	try {
	        
	       stmt=connection.createStatement();
	       stmt1=connection.createStatement();
			/*
			 * String query =
			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
			 * +req_date+"'  group by companyid;;";
			 */
	     
           String query = "select sum(s.count) as s_count,s.DATE as s_date,u.companyid, u.parentid,s.id, u.username, u.companyname,u.color,ser.servername  from tbl_submitted s join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  u.companyname='"+companyname+"'  and s.errorcode=0 and (s.date  BETWEEN '"+beforemonthdate+"' AND '"+req_date+"')  group by date order by date desc;";	       
	       //logger.info("=========query=========="+query);
	       rs = stmt.executeQuery(query);
	       Gson gson = new Gson();

	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		Count_sms_user response_p = new Count_sms_user();
      		
	       
	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	       	response_p.setId(rs.getInt("id")+"");
	       	response_p.setCompanyname(rs.getString("companyname"));
	       	response_p.setColor(rs.getString("color"));
	       	response_p.setUsername(rs.getString("username"));
	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	       	response_p.setRequested_date(rs.getString("s_date"));
	       	if(rs.getString("servername")!=null)
	       	response_p.setServername(rs.getString("servername"));
	       	else
	       		response_p.setServername("NA");	
	       	
					/*
					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
					 */ 
	       	query = "select sum(d.count) as d_count from tbl_delivered d join  userdetails u on u.companyid = d.companyid where d.date  like '"+rs.getString("s_date")+"%' and d.errorcode=0 and u.companyname = '"+response_p.getCompanyname()+"';"; 
	       	
	       	//logger.info("=========query1=========="+query);
	       	rs1  = stmt1.executeQuery(query);
	        if(rs1.isBeforeFirst()){
		       	while (rs1.next()) {
		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
		       	}
	        }
	       	
	       	
	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	        jsonArray.put(jobj);
	       	
	    }
	           }else{
	           
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
	
	public void all_users_count_byCompanynameSpanel(String req_date,JSONArray jsonArray,String companyname)
	{
	   	Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
	   	Statement stmt = null;
	   	Statement stmt1 = null;
	   	ResultSet rs = null;
	   	ResultSet rs1 = null;
	   	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
	   	String beforemonthdate=daoImpl.createBeforeMonthDate();
	   	try {
	        
	       stmt=connection.createStatement();
	       stmt1=connection.createStatement();
			/*
			 * String query =
			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
			 * +req_date+"'  group by companyid;;";
			 */
	     
           String query = "select sum(s.count) as s_count,s.DATE as s_date,u.companyid, u.parentid,s.id, u.username, u.companyname,u.color,ser.servername  from tbl_submitted s join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  u.companyname='"+companyname+"'  and s.errorcode=0 and (s.date  BETWEEN '"+beforemonthdate+"' AND '"+req_date+"')  group by date order by date desc;";	       
	       //logger.info("=========querySSSSS=========="+query);
	       rs = stmt.executeQuery(query);
	       Gson gson = new Gson();

	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		Count_sms_user response_p = new Count_sms_user();
      		
	       
	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	       	response_p.setId(rs.getInt("id")+"");
	       	response_p.setCompanyname(rs.getString("companyname"));
	       	response_p.setColor(rs.getString("color"));
	       	response_p.setUsername(rs.getString("username"));
	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	       	response_p.setRequested_date(rs.getString("s_date"));
	       	if(rs.getString("servername")!=null)
	       	response_p.setServername(rs.getString("servername"));
	       	else
	       		response_p.setServername("NA");	
	       	
					/*
					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
					 */ 
	       	query = "select sum(d.count) as d_count from tbl_delivered d join  userdetails u on u.companyid = d.companyid where d.date  like '"+rs.getString("s_date")+"%' and d.errorcode=0 and u.companyname = '"+response_p.getCompanyname()+"';"; 
	       	
	       	//logger.info("=========query1=========="+query);
	       	rs1  = stmt1.executeQuery(query);
	        if(rs1.isBeforeFirst()){
		       	while (rs1.next()) {
		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
		       	}
	        }
	       	
	       	
	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	        jsonArray.put(jobj);
	       	
	    }
	           }else{
	           
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
	public void all_users_count_byCompanynameBydate(String req_date,JSONArray jsonArray,String companyname)
	{
	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;
	   	Statement stmt1 = null;
	   	ResultSet rs = null;
	   	ResultSet rs1 = null;
	   	Smpp_DaoImpl smpp_dao=new Smpp_DaoImpl();
	   	smpp_dao.all_users_count_byCompanynameBydateSpanel(req_date,jsonArray,companyname);
	   	try {
	        
	       stmt=connection.createStatement();
	       stmt1=connection.createStatement();
			
	       String query = "SELECT sum(s.count) as s_count,s.DATE as s_date, u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway where  s.date='"+req_date+"' AND s.errorcode=0 AND u.companyname='"+companyname+"' group by companyid; ";
	       //logger.info("=========query====vvvv111======"+query);
	       rs = stmt.executeQuery(query);
	       Gson gson = new Gson();

	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		Count_sms_user response_p = new Count_sms_user();
      		
	       
	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	       	response_p.setCompanyname(rs.getString("companyname"));
	       	response_p.setColor(rs.getString("color"));
	       	response_p.setUsername(rs.getString("username"));
	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	       	response_p.setRequested_date(rs.getString("s_date"));
	       	if(rs.getString("servername")!=null)
	       	response_p.setServername(rs.getString("servername"));
	       	else
	       		response_p.setServername("NA");	
	       	
					/*
					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
					 */
	       	query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+rs.getString("s_date")+"%' and d.errorcode=0 and d.companyid = "+response_p.getCompanyid()+";"; 
	       	
	       	//logger.info("=========query1=========="+query);
	       	rs1  = stmt1.executeQuery(query);
	        if(rs1.isBeforeFirst()){
		       	while (rs1.next()) {
		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
		       	}
	        }
	       	
	       	
	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	        jsonArray.put(jobj);
	       	
	    }
	           }else{
	           
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
	public void all_users_count_byCompanynameBydateSpanel(String req_date,JSONArray jsonArray,String companyname)
	{
		////logger.info("11111111111111111111111nnnnnnnnnnnnn"); 
	   	Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
	   //	//logger.info("2222222222222222222222222nnnnnnnnnnnnn"); 
	   	Statement stmt = null;
	   	Statement stmt1 = null;
	   	ResultSet rs = null;
	   	ResultSet rs1 = null;
	   
	   	try {
	        
	       stmt=connection.createStatement();
	       stmt1=connection.createStatement();
			
	       String query = "SELECT sum(s.count) as s_count,s.DATE as s_date, u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway where  s.date='"+req_date+"' AND s.errorcode=0 AND u.companyname='"+companyname+"' group by companyid; ";
	       //logger.info("=========query===ssss======="+query);
	       rs = stmt.executeQuery(query);
	       Gson gson = new Gson();

	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		Count_sms_user response_p = new Count_sms_user();
      		
	       
	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	       	response_p.setCompanyname(rs.getString("companyname"));
	       	response_p.setColor(rs.getString("color"));
	       	response_p.setUsername(rs.getString("username"));
	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	       	response_p.setRequested_date(rs.getString("s_date"));
	       	if(rs.getString("servername")!=null)
	       	response_p.setServername(rs.getString("servername"));
	       	else
	       		response_p.setServername("NA");	
	       	
					/*
					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
					 */
	       	query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+rs.getString("s_date")+"%' and d.errorcode=0 and d.companyid = "+response_p.getCompanyid()+";"; 
	       	
	       	//logger.info("=========query1=========="+query);
	       	rs1  = stmt1.executeQuery(query);
	        if(rs1.isBeforeFirst()){
		       	while (rs1.next()) {
		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
		       	}
	        }
	       	
	       	
	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	        jsonArray.put(jobj);
	       	
	    }
	           }else{
	           
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
	public void All_tesync_byCompanyName(String req_date, JSONArray jsonArray, String companyname) {
	
	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;

	   	ResultSet rs = null;
	   	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
	   	String beforeMonthDate=daoImpl.createBeforeMonthDate();
	   	try {
	        
	       stmt=connection.createStatement();
	       String query = "select *  from tesync_table where client='"+companyname+"' and (requested_date BETWEEN '"+beforeMonthDate+"' AND '"+req_date+"');";
	       //logger.info("query="+query);
	       rs = stmt.executeQuery(query);
	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		
		    	JSONObject jobj = new JSONObject();
		    	jobj.put("companyid", rs.getString("id"));
		    	jobj.put("client", rs.getString("client"));
		    	jobj.put("submited", rs.getString("submited"));
		    	jobj.put("delivered", rs.getString("delivered"));
		    	jobj.put("delivered_in_per", rs.getString("delivered_in_per"));
		    	jobj.put("undelivered", rs.getString("undelivered"));
		    	jobj.put("undelivered_in_per", rs.getString("undelivered_in_per"));
		    	jobj.put("error_1", rs.getString("error_1"));
		    	jobj.put("error_6", rs.getString("error_6"));
		    	jobj.put("error_21", rs.getString("error_21"));
		    	jobj.put("error_32", rs.getString("error_32"));
		    	jobj.put("error_34", rs.getString("error_34"));
		    	jobj.put("error_69", rs.getString("error_69"));
		    	jobj.put("error_253", rs.getString("error_253"));
		    	jobj.put("error_254", rs.getString("error_254"));
		    	jobj.put("requested_date", rs.getString("requested_date"));
		        jsonArray.put(jobj);
	    }
	           }else{
	           
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
		

	   	}
	   	
	   	
	   
	   }
	
	public void All_tesync_byCompanyNameAdmin(String req_date, JSONArray jsonArray, String companyname) {
		
	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;

	   	ResultSet rs = null;
	   	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
	   	String beforeMonthDate=daoImpl.createBeforeMonthDate();
	   	try {
	        
	       stmt=connection.createStatement();
	       String query="";
	       if(companyname.equalsIgnoreCase("valudemobi")) {
	    	   query="select *  from tesync_table where (client='VmobiT2' or client='VmobiT3' or client='VmobiT1' or client='VmobiPD1' or client='VmobiPD2') and (requested_date BETWEEN '"+beforeMonthDate+"' AND '"+req_date+"');";
	       }else if(companyname.equalsIgnoreCase("netxcell")) {
	    	   query="select *  from tesync_table where (client='netxcell' or client='vnsvns') and (requested_date BETWEEN '"+beforeMonthDate+"' AND '"+req_date+"');";
	       }else if(companyname.equalsIgnoreCase("sphere")) {
	    	   query="select *  from tesync_table where (client='sphere_p1s' or client='sphere_p3s' or client='sphere_t2s' or client='sphere_t3s' or client='sphere_t5s') and (requested_date BETWEEN '"+beforeMonthDate+"' AND '"+req_date+"');";
	       }else if(companyname.equalsIgnoreCase("movitel")) {
	    	   query="select *  from tesync_table where (client='movil3' or client='movil' or client='movil2') and (requested_date BETWEEN '"+beforeMonthDate+"' AND '"+req_date+"');";
	       }
	      //  query = "select *  from tesync_table where client='"+companyname+"' and (requested_date BETWEEN '"+beforeMonthDate+"' AND '"+req_date+"');";
	      
	       //logger.info("query="+query);
	       rs = stmt.executeQuery(query);
	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		
		    	JSONObject jobj = new JSONObject();
		    	jobj.put("companyid", rs.getString("id"));
		    	jobj.put("client", rs.getString("client"));
		    	jobj.put("submited", rs.getString("submited"));
		    	jobj.put("delivered", rs.getString("delivered"));
		    	jobj.put("delivered_in_per", rs.getString("delivered_in_per"));
		    	jobj.put("undelivered", rs.getString("undelivered"));
		    	jobj.put("undelivered_in_per", rs.getString("undelivered_in_per"));
		    	jobj.put("error_1", rs.getString("error_1"));
		    	jobj.put("error_6", rs.getString("error_6"));
		    	jobj.put("error_21", rs.getString("error_21"));
		    	jobj.put("error_32", rs.getString("error_32"));
		    	jobj.put("error_34", rs.getString("error_34"));
		    	jobj.put("error_69", rs.getString("error_69"));
		    	jobj.put("error_253", rs.getString("error_253"));
		    	jobj.put("error_254", rs.getString("error_254"));
		    	jobj.put("requested_date", rs.getString("requested_date"));
		        jsonArray.put(jobj);
	    }
	           }else{
	           
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
		

	   	}
	   	
	   	
	   
	   }
public void All_tesync_byCompanyName1(String req_date, JSONArray jsonArray, String companyname) {
		
	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;

	   	ResultSet rs = null;
	   	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
	   	String beforeMonthDate=daoImpl.createBeforeMonthDate();
	   	try {
	        
	       stmt=connection.createStatement();
	       String query="";
	       if(companyname.equalsIgnoreCase("valudemobi")) {
	    	   query="select *  from tesync_table where (client='VmobiT2' or client='VmobiT3' or client='VmobiT1' or client='VmobiPD1' or client='VmobiPD2') and (requested_date BETWEEN '"+beforeMonthDate+"' AND '"+req_date+"');";
	       }else if(companyname.equalsIgnoreCase("netxcell")) {
	    	   query="select *  from tesync_table where (client='netxcell' or client='vnsvns') and (requested_date BETWEEN '"+beforeMonthDate+"' AND '"+req_date+"');";
	       }else if(companyname.equalsIgnoreCase("sphere")) {
	    	   query="select *  from tesync_table where (client='sphere_p1s' or client='sphere_p3s' or client='sphere_t2s' or client='sphere_t3s' or client='sphere_t5s') and (requested_date BETWEEN '"+beforeMonthDate+"' AND '"+req_date+"');";
	       }else if(companyname.equalsIgnoreCase("movitel")) {
	    	   query="select *  from tesync_table where (client='movil3' or client='movil' or client='movil2') and (requested_date BETWEEN '"+beforeMonthDate+"' AND '"+req_date+"');";
	       }else if(companyname.equalsIgnoreCase("valuef")) {
	    	   query="select *  from tesync_table where (client='valueF' or client='vnsoft_tr1' or client='vnsoft_tr' or client='vfpromo' or client='vnsoftvf_pr') and (requested_date BETWEEN '"+beforeMonthDate+"' AND '"+req_date+"');";
	       }
	      //  query = "select *  from tesync_table where client='"+companyname+"' and (requested_date BETWEEN '"+beforeMonthDate+"' AND '"+req_date+"');";
	      
	       //logger.info("query="+query);
	       rs = stmt.executeQuery(query);
	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		
		    	JSONObject jobj = new JSONObject();
		    	jobj.put("companyid", rs.getString("id"));
		    	jobj.put("client", rs.getString("client"));
		    	jobj.put("submited", rs.getString("submited"));
		    	jobj.put("delivered", rs.getString("delivered"));
		    	jobj.put("delivered_in_per", rs.getString("delivered_in_per"));
		    	jobj.put("undelivered", rs.getString("undelivered"));
		    	jobj.put("undelivered_in_per", rs.getString("undelivered_in_per"));
		    	jobj.put("error_1", rs.getString("error_1"));
		    	jobj.put("error_6", rs.getString("error_6"));
		    	jobj.put("error_21", rs.getString("error_21"));
		    	jobj.put("error_32", rs.getString("error_32"));
		    	jobj.put("error_34", rs.getString("error_34"));
		    	jobj.put("error_69", rs.getString("error_69"));
		    	jobj.put("error_253", rs.getString("error_253"));
		    	jobj.put("error_254", rs.getString("error_254"));
		    	jobj.put("requested_date", rs.getString("requested_date"));
		        jsonArray.put(jobj);
	    }
	           }else{
	           
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
		

	   	}
	   	
	   	
	   
	   }
	public void All_userdata_byCompanyName(String req_date, JSONArray jsonArray, String companyname,String id) {
	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;
	   	Statement stmt1 = null;
	   	ResultSet rs = null;
	   	ResultSet rs1 = null;
	   	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
	   	String beforemonthdate=daoImpl.createBeforeMonthDate();
	   	try {
	        
	       stmt=connection.createStatement();
	       stmt1=connection.createStatement();
			/*
			 * String query =
			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
			 * +req_date+"'  group by companyid;;";
			 */
	     
           String query = "select sum(s.count) as s_count,s.DATE as s_date,u.companyid, u.parentid,s.id, u.username, u.companyname,u.color,ser.servername  from tbl_submitted s join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  u.companyname='"+companyname+"'  and s.errorcode=0 and (s.date  BETWEEN '"+beforemonthdate+"' AND '"+req_date+"') and u.userid="+id+" group by date order by date desc;";	       
	       //logger.info("=========query=========="+query);
	       rs = stmt.executeQuery(query);
	       Gson gson = new Gson();

	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		Count_sms_user response_p = new Count_sms_user();
      		
	       
	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	       	response_p.setId(rs.getInt("id")+"");
	       	response_p.setCompanyname(rs.getString("companyname"));
	       	response_p.setColor(rs.getString("color"));
	       	response_p.setUsername(rs.getString("username"));
	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	       	response_p.setRequested_date(rs.getString("s_date"));
	       	if(rs.getString("servername")!=null)
	       	response_p.setServername(rs.getString("servername"));
	       	else
	       		response_p.setServername("NA");	
	       	
					/*
					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
					 */ 
	       	query = "select sum(d.count) as d_count from tbl_delivered d join  userdetails u on u.companyid = d.companyid where d.date  like '"+rs.getString("s_date")+"%' and d.errorcode=0 and u.companyname = '"+response_p.getCompanyname()+"';"; 
	       	
	       	//logger.info("=========query1=========="+query);
	       	rs1  = stmt1.executeQuery(query);
	        if(rs1.isBeforeFirst()){
		       	while (rs1.next()) {
		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
		       	}
	        }
	       	
	       	
	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	        jsonArray.put(jobj);
	       	
	    }
	           }else{
	           
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
	public void All_userdata_byCompanyNameSpanel(String req_date, JSONArray jsonArray, String companyname,String id) {
	   	Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
	   	Statement stmt = null;
	   	Statement stmt1 = null;
	   	ResultSet rs = null;
	   	ResultSet rs1 = null;
	   	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
	   	String beforemonthdate=daoImpl.createBeforeMonthDate();
	   	try {
	        
	       stmt=connection.createStatement();
	       stmt1=connection.createStatement();
			/*
			 * String query =
			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
			 * +req_date+"'  group by companyid;;";
			 */
	     
           String query = "select sum(s.count) as s_count,s.DATE as s_date,u.companyid, u.parentid,s.id, u.username, u.companyname,u.color,ser.servername  from tbl_submitted s join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  u.companyname='"+companyname+"'  and s.errorcode=0 and (s.date  BETWEEN '"+beforemonthdate+"' AND '"+req_date+"') and u.userid="+id+" group by date order by date desc;";	       
	       //logger.info("=========query=========="+query);
	       rs = stmt.executeQuery(query);
	       Gson gson = new Gson();

	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		Count_sms_user response_p = new Count_sms_user();
      		
	       
	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	       	response_p.setId(rs.getInt("id")+"");
	       	response_p.setCompanyname(rs.getString("companyname"));
	       	response_p.setColor(rs.getString("color"));
	       	response_p.setUsername(rs.getString("username"));
	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	       	response_p.setRequested_date(rs.getString("s_date"));
	       	if(rs.getString("servername")!=null)
	       	response_p.setServername(rs.getString("servername"));
	       	else
	       		response_p.setServername("NA");	
	       	
					/*
					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
					 */ 
	       	query = "select sum(d.count) as d_count from tbl_delivered d join  userdetails u on u.companyid = d.companyid where d.date  like '"+rs.getString("s_date")+"%' and d.errorcode=0 and u.companyname = '"+response_p.getCompanyname()+"';"; 
	       	
	       	//logger.info("=========query1=========="+query);
	       	rs1  = stmt1.executeQuery(query);
	        if(rs1.isBeforeFirst()){
		       	while (rs1.next()) {
		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
		       	}
	        }
	       	
	       	
	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	        jsonArray.put(jobj);
	       	
	    }
	           }else{
	           
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
	public void getSPanelCount(JSONObject jsonObject1,String req_date) {

		
		Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
		long submittedcount=0;
		long deliverCount=0;
		boolean checkmongodata=false;
		 MongoClient mongo = DbConnectionMongo.getInstance().getConnection();
		 DB db = mongo.getDB("count_sms");
		 DBCollection collection = db.getCollection("spaneldata");
		 DBCursor cursor1 = collection.find(new BasicDBObject().append("s_date", new BasicDBObject("$regex", req_date)));
		try {
			
		
	       while(cursor1.hasNext()) {
	    	   checkmongodata=true;
	         //  //logger.info(cursor1.next());
	    	   String data=cursor1.next().toString();
	    	   
	    	   //logger.info("nnnnnnnn"+data);
	    	   JSONObject jsonObject;

	    	   jsonObject = new JSONObject(data);
	    	         logger.info("nnnnnnnn"+data);
	         
			try {
				submittedcount=submittedcount+jsonObject.getInt("s_count");
				deliverCount=deliverCount+jsonObject.getInt("d_count");
		         
		        
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
	       }
	       if(checkmongodata==false) {
	    	   //logger.info("falsefalsefalsefalsefalsefalsefalse");
	    	Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
	   	   	Statement stmt = null;
	   	   	Statement stmt1 = null;
	   	   	ResultSet rs = null;
	   	   	ResultSet rs1 = null;
	   	   	try {
	   	        
	   	       stmt=connection.createStatement();
	   	       stmt1=connection.createStatement();
	   			/*
	   			 * String query =
	   			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
	   			 * +req_date+"'  group by companyid;;";
	   			 */
	   	       String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"%' and s.errorcode=0  group by companyid order by team,companyname;;";
	   	       
	   	       //logger.info("=========query=========="+query);
	   	       rs = stmt.executeQuery(query);
	   	       Gson gson = new Gson();
	   	   
	   	        if(rs.isBeforeFirst()){
	   	       	while (rs.next()) {
	   	       	
	   	       	
	   	       submittedcount=submittedcount+rs.getInt("s_count");
	   	       
	   					/*
	   					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
	   					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
	   					 */
	   	       	query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+req_date+"%' and d.errorcode=0 and d.companyid = "+rs.getInt("companyid")+";"; 
	   	       	
	   	       	//logger.info("=========query1=========="+query);
	   	       	rs1  = stmt1.executeQuery(query);
	   	        if(rs1.isBeforeFirst()){
	   		       	while (rs1.next()) {
	   		       		deliverCount =deliverCount+rs1.getInt("d_count");
	   		       	
	   		       	}
	   	        }
	   	       	
	   	       	
	   	       	
	   	    }
	   	           }else{
	   	           
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
	       
			jsonObject1.put("submittedcount", submittedcount);
			jsonObject1.put("deliverCount", deliverCount);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {try {
			if(cursor1!=null)
				cursor1.close();

				// mongo.close();

				} catch (Exception e2) {
				// TODO: handle exception
				}
				
				try {
				if(mongo!=null)
				mongo.close();

				// mongo.close();

				} catch (Exception e2) {
				// TODO: handle exception
				}
}
	      
	   
	   
	}
	public void getPanelCount(JSONObject jsonObject1,String req_date) {
		long submittedcount=0;
		long deliverCount=0;
		
		try {
			
		
	      
	    	   logger.info("falsefalsefalsefalsefalsefalsefalse");
	    	   Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	   	Statement stmt = null;
	   	   	Statement stmt1 = null;
	   	   	ResultSet rs = null;
	   	   	ResultSet rs1 = null;
	   	   	try {
	   	        
	   	       stmt=connection.createStatement();
	   	       stmt1=connection.createStatement();
	   			/*
	   			 * String query =
	   			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
	   			 * +req_date+"'  group by companyid;;";
	   			 */
	   	       String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"%' and s.errorcode=0  group by companyid order by team,companyname;;";
	   	       
	   	       //logger.info("=========query=========="+query);
	   	       rs = stmt.executeQuery(query);
	   	       Gson gson = new Gson();
	   	   
	   	        if(rs.isBeforeFirst()){
	   	       	while (rs.next()) {
	   	       	
	   	       	
	   	       submittedcount=submittedcount+rs.getInt("s_count");
	   	       
	   					/*
	   					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
	   					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
	   					 */
	   	       	query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+req_date+"%' and d.errorcode=0 and d.companyid = "+rs.getInt("companyid")+";"; 
	   	       	
	   	       	//logger.info("=========query1=========="+query);
	   	       	rs1  = stmt1.executeQuery(query);
	   	        if(rs1.isBeforeFirst()){
	   		       	while (rs1.next()) {
	   		       		deliverCount =deliverCount+rs1.getInt("d_count");
	   		       	
	   		       	}
	   	        }
	   	       	
	   	       	
	   	       	
	   	    }
	   	           }else{
	   	           
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
	       
	       
			jsonObject1.put("submittedcount", submittedcount);
			jsonObject1.put("deliverCount", deliverCount);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
	   
	   
	}
	public String[] getMonthlySumittedCountByCompanyName(String companyname,String req_date1) {
		String monthliSubmitted[]=new String[2];
		String req_date=req_date1.substring(0, 7);
		BigInteger monthly_s=new BigInteger("0");
		BigInteger monthly_d=new BigInteger("0");
		try {
			
	    	   //logger.info("falsefalsefalsefalsefalsefalsefalse");
	    	   Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	   	Statement stmt = null;
	   	   	Statement stmt1 = null;
	   	   	ResultSet rs = null;
	   	   	ResultSet rs1 = null;
	   	   	try {
	   	        
	   	       stmt=connection.createStatement();
	   	       stmt1=connection.createStatement();
	   			/*
	   			 * String query =
	   			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
	   			 * +req_date+"'  group by companyid;;";
	   			 */
	   	       String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"%' and u.companyname = '"+companyname+"' and s.errorcode=0  group by companyid order by team,companyname;;";
	   	       
	   	       //logger.info("=========query=========="+query);
	   	       rs = stmt.executeQuery(query);
	   	       Gson gson = new Gson();
	   	   
	   	        if(rs.isBeforeFirst()){
	   	       	while (rs.next()) {
	   	     //  	monthliSubmitted[0]=monthliSubmitted[0]+rs.getInt("s_count");
	   	       	BigInteger ssss=new BigInteger(rs.getInt("s_count")+"");
	   	     monthly_s=monthly_s.add(ssss);
	   	     query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+req_date+"%' and d.errorcode=0 and d.companyid = "+rs.getString("companyid")+";"; 
	   	       	
	   	       	//logger.info("=========query1=========="+query);
	   	       	rs1  = stmt1.executeQuery(query);
	   	        if(rs1.isBeforeFirst()){
	   		       	while (rs1.next()) {
	   		     //	monthliSubmitted[1]=monthliSubmitted[1]+rs1.getInt("d_count");
	   		       	BigInteger dddd=new BigInteger(rs1.getInt("d_count")+"");
	   		       	monthly_d=monthly_d.add(dddd);
	   		       
	   		       	}
	   	        }
	   	       	
	   	    }
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
	       

	       monthliSubmitted[0]=monthly_s+"";
	      	       monthliSubmitted[1]=monthly_d+"";
	      	       //logger.info("ssssss"+monthliSubmitted[0]);
	      	        //logger.info("dddddddd"+monthliSubmitted[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
	      
		return monthliSubmitted;
	   
	   
	}
	public String[] getMonthlySumittedCountByCompanyNameUser(String companyname,String req_date1,String userid) {
		
		String monthliSubmitted[]=new String[2];
		String req_date=req_date1.substring(0, 7);
		BigInteger monthly_s=new BigInteger("0");
		BigInteger monthly_d=new BigInteger("0");
		try {
			
	    	   //logger.info("falsefalsefalsefalsefalsefalsefalse");
	    	   Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	   	Statement stmt = null;
	   	   	Statement stmt1 = null;
	   	   	ResultSet rs = null;
	   	   	ResultSet rs1 = null;
	   	   	try {
	   	        
	   	       stmt=connection.createStatement();
	   	       stmt1=connection.createStatement();
	   			/*
	   			 * String query =
	   			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
	   			 * +req_date+"'  group by companyid;;";
	   			 */
	   	       String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"%' and u.userid="+userid+" and u.companyname = '"+companyname+"%' and s.errorcode=0  group by companyid order by team,companyname;;";
	   	       
	   	       //logger.info("=========query=========="+query);
	   	       rs = stmt.executeQuery(query);
	   	       Gson gson = new Gson();
	   	   
	   	        if(rs.isBeforeFirst()){
	   	       	while (rs.next()) {
	   	     //  	monthliSubmitted[0]=monthliSubmitted[0]+rs.getInt("s_count");
	   	       	BigInteger ssss=new BigInteger(rs.getInt("s_count")+"");
	   	     monthly_s=monthly_s.add(ssss);
	   	     query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+req_date+"%' and d.errorcode=0 and d.companyid = "+rs.getString("companyid")+";"; 
	   	       	
	   	       	//logger.info("=========query1=========="+query);
	   	       	rs1  = stmt1.executeQuery(query);
	   	        if(rs1.isBeforeFirst()){
	   		       	while (rs1.next()) {
	   		     //	monthliSubmitted[1]=monthliSubmitted[1]+rs1.getInt("d_count");
	   		       	BigInteger dddd=new BigInteger(rs1.getInt("d_count")+"");
	   		       	monthly_d=monthly_d.add(dddd);
	   		       
	   		       	}
	   	        }
	   	       	
	   	    }
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
	       

	       monthliSubmitted[0]=monthly_s+"";
	      	       monthliSubmitted[1]=monthly_d+"";
	      	       //logger.info("ssssss"+monthliSubmitted[0]);
	      	        //logger.info("dddddddd"+monthliSubmitted[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}
	      
		return monthliSubmitted;
	   
	   
	}
	public void all_users_count_Mysql_to_mongo_by_date(String req_date,JSONArray jsonArray) {

		
		Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
		 
		MongoClient mongo = DbConnectionMongo.getInstance().getConnection();
		 DB db = mongo.getDB("count_sms");
		
		 DBCollection collection = db.getCollection("smppdata");
		 try {
		  
			Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	   	Statement stmt = null;
	   	   	Statement stmt1 = null;
	   	   	ResultSet rs = null;
	   	   	ResultSet rs1 = null;
	   	   	try {
	   	        
	   	       stmt=connection.createStatement();
	   	       stmt1=connection.createStatement();
	   			/*
	   			 * String query =
	   			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
	   			 * +req_date+"'  group by companyid;;";
	   			 */
	   	       String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"%'  and s.errorcode=0  group by companyid order by team,companyname;;";
	   	       
	   	       //logger.info("=========query=========="+query);
	   	       rs = stmt.executeQuery(query);
	   	       Gson gson = new Gson();
	   	       if(rs != null) {
	   	    	PanelDeleteMongiDbData(req_date,collection);
	   	       }
	   	        
	   	       	while (rs.next()) {
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
	   	       	
	   	       	//logger.info("=========query1=========="+query);
	   	       	rs1  = stmt1.executeQuery(query);
	   	        if(rs1.isBeforeFirst()){
	   		       	while (rs1.next()) {
	   		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
	   		     jsonObject.put("d_count", rs1.getInt("d_count")+"");
	   		       	}
	   	        }
	   	       	
	   	       	
	   	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	   	       	int companyid=rs.getInt("companyid");
	   	       
	   	        daoImpl.insertAllUserCountMongo(jsonObject,collection,req_date,companyid);
	   	     
	   	        jsonArray.put(jobj);
	   	       	
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
	       
		 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				
				
				try {
					if(mongo!=null)
						mongo.close();
					
				//	mongo.close();
					
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
				
			}
		 
	   
	}
	public void all_users_countApi(String req_date,JSONArray jsonArray,String companyname)
	{
		 try {
		
	      
	    	
	    	   Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	   	Statement stmt = null;
	   	   	Statement stmt1 = null;
	   	   	ResultSet rs = null;
	   	   	ResultSet rs1 = null;
	   	   	try {
	   	        
	   	       stmt=connection.createStatement();
	   	       stmt1=connection.createStatement();
	   			 String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername ,s.date as date  from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"%' and u.companyname like '"+companyname+"%' and s.errorcode=0  group by companyid order by team,companyname;;";
	   	       
	   	       rs = stmt.executeQuery(query);
	   	       Gson gson = new Gson();
	   	     
	   	        
	   	       	while (rs.next()) {
	   	       		Count_sms_user1 response_p = new Count_sms_user1();
	   	       	 JSONObject jsonObject=new JSONObject();
	   	       
	   	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	   	       	response_p.setCompanyname(rs.getString("companyname"));
	   	       	response_p.setColor(rs.getString("color"));
	   	       	response_p.setUsername(rs.getString("username"));
	   	       	response_p.setS_count(rs.getInt("s_count")+"");
	   	       	response_p.setS_date(rs.getString("date"));
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
		   	  jsonObject.put("s_date", rs.getString("date"));
	   					/*
	   					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
	   					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
	   					 */
	   	       	query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+req_date+"%' and d.errorcode=0 and d.companyid = "+response_p.getCompanyid()+";"; 
	   	       	
	   	       	//logger.info("=========query1=========="+query);
	   	       	rs1  = stmt1.executeQuery(query);
	   	        if(rs1.isBeforeFirst()){
	   		       	while (rs1.next()) {
	   		       	response_p.setD_count(rs1.getInt("d_count")+"");
	   		     jsonObject.put("d_count", rs1.getInt("d_count")+"");
	   		       	}
	   	        }
	   	       	
	   	       	
	   	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	   	       	int companyid=rs.getInt("companyid");
	   	       
	   	       
	   	     
	   	        jsonArray.put(jobj);
	   	       	
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
	       
		 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
	   }
	public void all_users_count(String req_date,JSONArray jsonArray,String companyname)
	{
		
		Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
		boolean checkmongodata=false;
		 
		MongoClient mongo = DbConnectionMongo.getInstance().getConnection();
		 DB db = mongo.getDB("count_sms");
		
		 DBCollection collection = db.getCollection("smppdata");
		 DBCursor cursor1 = collection.find(new BasicDBObject().append("s_date", new BasicDBObject("$regex", req_date)).append("companyname", new BasicDBObject("$regex", companyname)));
		 try {
		
	       while(cursor1.hasNext()) {
	    	   checkmongodata=true;
	         //  //logger.info(cursor1.next());
	    	   String data=cursor1.next().toString();
	    	   logger.info("all_users_countall_users_countall_users_count"+data);
	          JSONObject jsonObject1;
			
				jsonObject1 = new JSONObject(data);
		         
		         
		          JSONObject jsonObject=new JSONObject();
	         		jsonObject.put("companyid", jsonObject1.getInt("companyid")+"");
					jsonObject.put("submitted_count", jsonObject1.getInt("s_count")+"");
					jsonObject.put("delivered_count", jsonObject1.getInt("d_count")+"");
					jsonObject.put("color", jsonObject1.getString("color"));
					jsonObject.put("companyname", jsonObject1.getString("companyname"));
					jsonObject.put("username", jsonObject1.getString("username")+"");
					jsonObject.put("requested_date", jsonObject1.getString("s_date"));
					jsonObject.put("servername", "NA");
					
					jsonArray.put(jsonObject);
	       }
			
	         
	       
	       if(checkmongodata==false) {
	    	   logger.info("falsefalsefalsefalsefalsefalsefalse all_users_countall_users_count");
	    	   Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	   	Statement stmt = null;
	   	   	Statement stmt1 = null;
	   	   	ResultSet rs = null;
	   	   	ResultSet rs1 = null;
	   	   	try {
	   	        
	   	       stmt=connection.createStatement();
	   	       stmt1=connection.createStatement();
	   			/*
	   			 * String query =
	   			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
	   			 * +req_date+"'  group by companyid;;";
	   			 */
	   	       String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername ,s.date as date  from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"%' and u.companyname like '"+companyname+"%' and s.errorcode=0  group by companyid order by team,companyname;;";
	   	       
	   	       //logger.info("=========query=========="+query);
	   	       rs = stmt.executeQuery(query);
	   	       Gson gson = new Gson();
	   	       if(rs != null) {
	   	    	PanelDeleteMongiDbData(req_date,collection);
	   	       }
	   	        
	   	       	while (rs.next()) {
	   	       		Count_sms_user response_p = new Count_sms_user();
	   	       	 JSONObject jsonObject=new JSONObject();
	   	       
	   	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	   	       	response_p.setCompanyname(rs.getString("companyname"));
	   	       	response_p.setColor(rs.getString("color"));
	   	       	response_p.setUsername(rs.getString("username"));
	   	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	   	       	response_p.setRequested_date(rs.getString("date"));
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
		   	  jsonObject.put("s_date", rs.getString("date"));
	   					/*
	   					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
	   					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
	   					 */
	   	       	query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+req_date+"%' and d.errorcode=0 and d.companyid = "+response_p.getCompanyid()+";"; 
	   	       	
	   	       	//logger.info("=========query1=========="+query);
	   	       	rs1  = stmt1.executeQuery(query);
	   	        if(rs1.isBeforeFirst()){
	   		       	while (rs1.next()) {
	   		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
	   		     jsonObject.put("d_count", rs1.getInt("d_count")+"");
	   		       	}
	   	        }
	   	       	
	   	       	
	   	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	   	       	int companyid=rs.getInt("companyid");
	   	       
	   	        daoImpl.insertAllUserCountMongo(jsonObject,collection,req_date,companyid);
	   	     
	   	        jsonArray.put(jobj);
	   	       	
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
		 } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				try {
					if(cursor1!=null)
					cursor1.close();
					
				//	mongo.close();
					
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
				try {
					if(mongo!=null)
						mongo.close();
					
				//	mongo.close();
					
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
				
			}
		 
	   }
	public void PanelDeleteMongiDbData(String req_date, DBCollection collection) {
		
		try {
			BasicDBObject document = new BasicDBObject();
			document.put("s_date", new BasicDBObject("$regex", req_date));
			collection.remove(document);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public void insertAllUserCountMongo(JSONObject jobj, DBCollection collection, String req_date, int companyid) {
		
		String json=jobj.toString();
		DBObject dbObject = (DBObject)JSON.parse(json);
		System.out.print("222222222222222"+jobj.toString());
	     collection.insert(dbObject);
	 	System.out.print("nnnnnnnnnnnn1111111111"+jobj.toString());
		 //  collection.update(new BasicDBObject().append("companyid", companyid).append("requested_date", req_date), dbObject, true, false  );
	}
public void insertAllUserCountMongoApi(JSONObject jobj, DBCollection collection, String req_date, int companyid) {
		
		String json=jobj.toString();
		DBObject dbObject = (DBObject)JSON.parse(json);
		System.out.print("222222222222222"+jobj.toString());
	   //  collection.insert(dbObject);
	 	System.out.print("nnnnnnnnnnnn1111111111"+jobj.toString());
		collection.update(new BasicDBObject().append("companyid", companyid).append("requested_date", req_date), dbObject, true, false  );
}
	public void all_users_countSpanel_Mysql_to_mongo_by_date(String req_date,JSONArray jsonArray)
	{
		Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
		boolean checkmongodata=false;
	    MongoClient mongo = DbConnectionMongo.getInstance().getConnection();
	    DB db = mongo.getDB("count_sms");
		 DBCollection collection = db.getCollection("spaneldata");
		 try {
			
	    	Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
	   	   	Statement stmt = null;
	   	   	Statement stmt1 = null;
	   	   	ResultSet rs = null;
	   	   	ResultSet rs1 = null;
	   	   	try {
	   	        
	   	       stmt=connection.createStatement();
	   	       stmt1=connection.createStatement();
	   			
	   	       String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"%'  and s.errorcode=0  group by companyid order by team,companyname";
	   	     Gson gson = new Gson();
	   	       ////logger.info("=========query=========="+query);
	   	       //logger.info("fffffff"+query);
	   	       
	   	    	rs = stmt.executeQuery(query);
			
	   	       if(rs !=null) {
	   	    	   SpnaelDeleteMongoDb(req_date,collection);
	   	       }
	   	       
	   	       	while (rs.next()) {
	   	       	 //logger.info("dddddddddd");
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
	   	       	
	   	       	//logger.info("=========query1=========="+query);
	   	       	rs1  = stmt1.executeQuery(query);
	   	        if(rs1.isBeforeFirst()){
	   		       	while (rs1.next()) {
	   		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
	   		     jsonObject.put("d_count", rs1.getInt("d_count")+"");
	   		       	}
	   	        }
	   	       	
	   	       	
	   	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	   	       	int companyid=rs.getInt("companyid");
	   	     daoImpl.insertAllUserCountMongo(jsonObject,collection,req_date,companyid);
	   	     
	   	        jsonArray.put(jobj);
	   	       	
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
	       
		 } catch (Exception e) {
				// TODO: handle exception
			}finally {
					
					try {
					if(mongo!=null)
					mongo.close();

					// mongo.close();

					} catch (Exception e2) {
					// TODO: handle exception
					}}
	     
	   
	   }
	public void all_users_countSpanelApi(String req_date,JSONArray jsonArray,String companyname) {
		
		 try {
		
	       
	    	   //logger.info("fffffffffffffffffffffffffffff");
	    	   Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
	   	   	Statement stmt = null;
	   	   	Statement stmt1 = null;
	   	   	ResultSet rs = null;
	   	   	ResultSet rs1 = null;
	   	   	try {
	   	        
	   	       stmt=connection.createStatement();
	   	       stmt1=connection.createStatement();
	   			
	   	       String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername,s.date as date   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"%' and u.companyname like '"+companyname+"%' and s.errorcode=0  group by companyid order by team,companyname";
	   	     Gson gson = new Gson();
	   	     
	   	       
	   	    	rs = stmt.executeQuery(query);
				while (rs.next()) {
	   	       	 //logger.info("dddddddddd");
	   	       	Count_sms_user1 response_p = new Count_sms_user1();
	   	      	response_p.setCompanyid(rs.getInt("companyid")+"");
	   	       	response_p.setCompanyname(rs.getString("companyname"));
	   	       	response_p.setColor(rs.getString("color"));
	   	       	response_p.setUsername(rs.getString("username"));
	   	       	response_p.setS_count(rs.getInt("s_count")+"");
	   	       	response_p.setS_date(rs.getString("date"));
	   	     
	   	       	
	   	 	if(rs.getString("servername")!=null) {
	   	       	response_p.setServername(rs.getString("servername"));
	   	    
	   	       	}
	   	       	else {
	   	       		response_p.setServername("NA");	
	   	       
	   	       	}
	   	     	query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+req_date+"%' and d.errorcode=0 and d.companyid = "+response_p.getCompanyid()+";"; 
	   	       	
	   	       	//logger.info("=========query1=========="+query);
	   	       	rs1  = stmt1.executeQuery(query);
	   	        if(rs1.isBeforeFirst()){
	   		       	while (rs1.next()) {
	   		       	response_p.setD_count(rs1.getInt("d_count")+"");
	   		   
	   		       	}
	   	        }
	   	       	
	   	       	
	   	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	   	       	int companyid=rs.getInt("companyid");
	   	       	jsonArray.put(jobj);
	   	       	
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
	       
		 } catch (Exception e) {
				// TODO: handle exception
			}
	     
	   
	   }
	public void all_users_countSpanel(String req_date,JSONArray jsonArray,String companyname)
	{
		Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
		boolean checkmongodata=false;
	    MongoClient mongo = DbConnectionMongo.getInstance().getConnection();
	    DB db = mongo.getDB("count_sms");
		 DBCollection collection = db.getCollection("spaneldata");
		 DBCursor cursor1 = collection.find(new BasicDBObject().append("s_date", new BasicDBObject("$regex", req_date)).append("companyname", new BasicDBObject("$regex", companyname)));
		 try {
			
		
	       while(cursor1.hasNext()) {
	    	   checkmongodata=true;
	         //  //logger.info(cursor1.next());
	    	   String data=cursor1.next().toString();
	    	   ////logger.info(data);
	          JSONObject jsonObject1;
			try {
				jsonObject1 = new JSONObject(data);
		         
		         
		          JSONObject jsonObject=new JSONObject();
		          	jsonObject.put("companyid", jsonObject1.getInt("companyid")+"");
					jsonObject.put("submitted_count", jsonObject1.getInt("s_count")+"");
					jsonObject.put("delivered_count", jsonObject1.getInt("d_count")+"");
					jsonObject.put("color", jsonObject1.getString("color"));
					jsonObject.put("companyname", jsonObject1.getString("companyname"));
					jsonObject.put("username", jsonObject1.getString("username")+"");
					jsonObject.put("requested_date", jsonObject1.getString("s_date"));
					jsonObject.put("servername", "NA");
					
					jsonArray.put(jsonObject);
		        
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
	       }
	       if(checkmongodata==false) {
	    	   //logger.info("fffffffffffffffffffffffffffff");
	    	   Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
	   	   	Statement stmt = null;
	   	   	Statement stmt1 = null;
	   	   	ResultSet rs = null;
	   	   	ResultSet rs1 = null;
	   	   	try {
	   	        
	   	       stmt=connection.createStatement();
	   	       stmt1=connection.createStatement();
	   			
	   	       String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername,s.date as date   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"%' and u.companyname like '"+companyname+"%' and s.errorcode=0  group by companyid order by team,companyname";
	   	     Gson gson = new Gson();
	   	       ////logger.info("=========query=========="+query);
	   	       //logger.info("fffffff"+query);
	   	       
	   	    	rs = stmt.executeQuery(query);
			
	   	       if(rs !=null) {
	   	    	   SpnaelDeleteMongoDb(req_date,collection);
	   	       }
	   	       
	   	       	while (rs.next()) {
	   	       	 //logger.info("dddddddddd");
	   	       		Count_sms_user response_p = new Count_sms_user();
	   	      	 JSONObject jsonObject=new JSONObject();
	   	       
	   	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	   	       	response_p.setCompanyname(rs.getString("companyname"));
	   	       	response_p.setColor(rs.getString("color"));
	   	       	response_p.setUsername(rs.getString("username"));
	   	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	   	       	response_p.setRequested_date(rs.getString("date"));
	   	     
	   	       	
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
		   	  jsonObject.put("s_date", rs.getString("date"));
	   					/*
	   					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
	   					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
	   					 */
	   	       	query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+req_date+"%' and d.errorcode=0 and d.companyid = "+response_p.getCompanyid()+";"; 
	   	       	
	   	       	//logger.info("=========query1=========="+query);
	   	       	rs1  = stmt1.executeQuery(query);
	   	        if(rs1.isBeforeFirst()){
	   		       	while (rs1.next()) {
	   		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
	   		     jsonObject.put("d_count", rs1.getInt("d_count")+"");
	   		       	}
	   	        }
	   	       	
	   	       	
	   	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	   	       	int companyid=rs.getInt("companyid");
	   	     daoImpl.insertAllUserCountMongo(jsonObject,collection,req_date,companyid);
	   	     
	   	        jsonArray.put(jobj);
	   	       	
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
		 } catch (Exception e) {
				// TODO: handle exception
			}finally {try {
				if(cursor1!=null)
					cursor1.close();

					// mongo.close();

					} catch (Exception e2) {
					// TODO: handle exception
					}
					
					try {
					if(mongo!=null)
					mongo.close();

					// mongo.close();

					} catch (Exception e2) {
					// TODO: handle exception
					}}
	     
	   
	   }
	public void SpnaelDeleteMongoDb(String req_date, DBCollection collection) {
		
		try {
			BasicDBObject document = new BasicDBObject();
			document.put("s_date", new BasicDBObject("$regex", req_date));
			collection.remove(document);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public void all_admin_users_count(String req_date,JSONArray jsonArray)
	{
	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;
	   	Statement stmt1 = null;
	   	ResultSet rs = null;
	   	ResultSet rs1 = null;
	   	try {
	        
	       stmt=connection.createStatement();
	       stmt1=connection.createStatement();
			/*
			 * String query =
			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
			 * +req_date+"'  group by companyid;;";
			 */
	       String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername,s.date as s_date   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"%'and u.adminid=1 and s.errorcode=0  group by companyid order by team,companyname;;";
	       
	       //logger.info("=========query=========="+query);
	       rs = stmt.executeQuery(query);
	       Gson gson = new Gson();

	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		Count_sms_user response_p = new Count_sms_user();
      		
	       
	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	       	response_p.setCompanyname(rs.getString("companyname"));
	       	response_p.setColor(rs.getString("color"));
	       	response_p.setUsername(rs.getString("username"));
	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	       	response_p.setRequested_date(rs.getString("s_date"));
	       	if(rs.getString("servername")!=null)
	       	response_p.setServername(rs.getString("servername"));
	       	else
	       		response_p.setServername("NA");	
	       	
					/*
					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
					 */
	       	query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+req_date+"%' and d.errorcode=0 and d.companyid = "+response_p.getCompanyid()+";"; 
	       	
	       	//logger.info("=========query1=========="+query);
	       	rs1  = stmt1.executeQuery(query);
	        if(rs1.isBeforeFirst()){
		       	while (rs1.next()) {
		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
		       	}
	        }
	       	
	       	
	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	        jsonArray.put(jobj);
	       	
	    }
	           }else{
	           
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
	
	
	public double roundToNDigits(double value, int nDigits) {
		return Math.round(value * (10 ^ nDigits)) / (double) (10 ^ nDigits);
		}
	public String[] getMonthlySumittedCountByCompanyName_SPANEL(String companyname,String req_date1) {
		String monthliSubmitted[]=new String[2];
		BigInteger monthly_s=new BigInteger("0");
		BigInteger monthly_d=new BigInteger("0");
		String req_date=req_date1.substring(0, 7);
		Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
		boolean checkmongodata=false;
	    MongoClient mongo = DbConnectionMongo.getInstance().getConnection();
	    DB db = mongo.getDB("count_sms");
		 DBCollection collection = db.getCollection("spaneldata");
		 DBCursor cursor1 = collection.find(new BasicDBObject().append("s_date", "/ "+req_date+"/").append("companyname", "/ "+companyname+"/"));
		try {
			
		
	       while(cursor1.hasNext()) {
	    	   checkmongodata=true;
	         //  //logger.info(cursor1.next());
	    	   String data=cursor1.next().toString();
	    	   //logger.info("nnnnnnnn"+data);
	          JSONObject jsonObject1;
			try {
				jsonObject1 = new JSONObject(data);
				
				BigInteger ssss=new BigInteger(jsonObject1.getInt("s_count")+"");
				BigInteger dddd=new BigInteger(jsonObject1.getInt("d_count")+"");
				monthly_s=monthly_s.add(ssss);
				monthly_d=monthly_d.add(dddd);
				
				
		        
		        
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
	       }
	       if(checkmongodata==false) {
	    	   //logger.info("falsefalsefalsefalsefalsefalsefalse");
	    	   Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
	   	   	Statement stmt = null;
	   	   	Statement stmt1 = null;
	   	   	ResultSet rs = null;
	   	   	ResultSet rs1 = null;
	   	   	try {
	   	        
	   	       stmt=connection.createStatement();
	   	       stmt1=connection.createStatement();
	   			/*
	   			 * String query =
	   			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
	   			 * +req_date+"'  group by companyid;;";
	   			 */
	   	       String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"%' and u.companyname like '"+companyname+"%' and s.errorcode=0  group by companyid order by team,companyname;;";
	   	       
	   	       //logger.info("=========query=========="+query);
	   	       rs = stmt.executeQuery(query);
	   	       Gson gson = new Gson();
	   	   
	   	        if(rs.isBeforeFirst()){
	   	       	while (rs.next()) {
	   	   
	   	     	BigInteger ssss=new BigInteger(rs.getInt("s_count")+"");
				
				monthly_s=monthly_s.add(ssss);
				
		   	     query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+req_date+"%' and d.errorcode=0 and d.companyid = "+rs.getString("companyid")+";"; 
		   	       	
		   	       	//logger.info("=========query1=========="+query);
		   	       	rs1  = stmt1.executeQuery(query);
		   	        if(rs1.isBeforeFirst()){
		   		       	while (rs1.next()) {
		   		     	
		   		       		BigInteger dddd=new BigInteger(rs1.getInt("d_count")+"");
		   		       		monthly_d=monthly_d.add(dddd);
		   		     	////logger.info("ssssss"+monthliSubmitted[0]);
				     //   //logger.info("dddddddd"+monthliSubmitted[1]);
		   		       	}
		   	        }
	   	       	
	   	    }
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
	       

	       monthliSubmitted[0]=monthly_s+"";
	      	       monthliSubmitted[1]=monthly_d+"";
	      	       //logger.info("ssssss"+monthliSubmitted[0]);
	      	        //logger.info("dddddddd"+monthliSubmitted[1]);
		} catch (Exception e) {
			// TODO: handle exception
		}finally {try {
			if(cursor1!=null)
				cursor1.close();

				// mongo.close();

				} catch (Exception e2) {
				// TODO: handle exception
				}
				
				try {
				if(mongo!=null)
				mongo.close();

				// mongo.close();

				} catch (Exception e2) {
				// TODO: handle exception
				}
}
	       
	     
		return monthliSubmitted;
	   
	   
	}
	public void all_admin_users_count_Spanel(String req_date,JSONArray jsonArray)
	{
	   	Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
	   	Statement stmt = null;
	   	Statement stmt1 = null;
	   	ResultSet rs = null;
	   	ResultSet rs1 = null;
	   	try {
	        
	       stmt=connection.createStatement();
	       stmt1=connection.createStatement();
			/*
			 * String query =
			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
			 * +req_date+"'  group by companyid;;";
			 */
	       String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,ser.servername,s.date as s_date   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"%'and u.adminid=1 and s.errorcode=0  group by companyid order by team,companyname;;";
	       
	       //logger.info("=========query=========="+query);
	       rs = stmt.executeQuery(query);
	       Gson gson = new Gson();

	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		Count_sms_user response_p = new Count_sms_user();
      		
	       
	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	       	response_p.setCompanyname(rs.getString("companyname"));
	       	response_p.setColor(rs.getString("color"));
	       	response_p.setUsername(rs.getString("username"));
	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	       	response_p.setRequested_date(rs.getString("s_date"));
	       	if(rs.getString("servername")!=null)
	       	response_p.setServername(rs.getString("servername"));
	       	else
	       		response_p.setServername("NA");	
	       	
					/*
					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
					 */
	       	query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+req_date+"%' and d.errorcode=0 and d.companyid = "+response_p.getCompanyid()+";"; 
	       	
	       	//logger.info("=========query1=========="+query);
	       	rs1  = stmt1.executeQuery(query);
	        if(rs1.isBeforeFirst()){
		       	while (rs1.next()) {
		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
		       	}
	        }
	       	
	       	
	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	        jsonArray.put(jobj);
	       	
	    }
	           }else{
	           
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
	public void all_users_count1(String req_date,JSONArray jsonArray,String userid,String companyname)
	//db.userdetails.aggregate([ {$match: {userid: "1"}},  {$lookup: {    from: "username_tb",    localField: "useid",    foreignField: "id",    as: "username_tb"  }} ])
	
	{
			 Connection connection=DbConnection_Smpp.getInstance().getConnection();
	     	   	Statement stmt = null;
	     	   	Statement stmt1 = null;
	     	   	ResultSet rs = null;
	     	   	ResultSet rs1 = null;
	     	   	try {
	     	        
	     	       stmt=connection.createStatement();
	     	       stmt1=connection.createStatement();
	     			/*
	     			 * String query = 
	     			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
	     			 * +req_date+"'  group by companyid;;";
	     			 */
	     	       String query = "select sum(s.count) as s_count,u.companyid, ser.servername, u.parentid, u.username, u.companyname,u.color,s.date   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date like'"+req_date+"%' and u.userid="+userid+" and u.companyname like '"+companyname+"%' and s.errorcode=0  group by companyid order by team,companyname;";
	     	       
	     	       //logger.info("=========query=========="+query);
	     	       rs = stmt.executeQuery(query);
	     	       Gson gson = new Gson();

	     	        if(rs.isBeforeFirst()){
	     	       	while (rs.next()) {
	     	       		JSONObject jsonObject=new JSONObject();
	     	       		Count_sms_user response_p = new Count_sms_user();
	           		
	     	       
	     	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	     	       	response_p.setCompanyname(rs.getString("companyname"));
	     	       	response_p.setColor(rs.getString("color"));
	     	       	response_p.setUsername(rs.getString("username"));
	     	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	     	       	response_p.setRequested_date(rs.getString("date"));
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
	     	       	query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"+rs.getString("date")+"' and d.errorcode=0 and d.companyid = "+response_p.getCompanyid()+";"; 
	     	       	
	     	       	//logger.info("=========query1=========="+query);
	     	       	rs1  = stmt1.executeQuery(query);
	     	        if(rs1.isBeforeFirst()){
	     		       	while (rs1.next()) {
	     		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
	     		       jsonObject.put("d_count", rs1.getInt("d_count")+"");
	     		       	}
	     	        }
	     	       	
	     	       	
	     	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
		   	       	int companyid=rs.getInt("companyid");
		   	        jsonArray.put(jobj);
	     	       	
	     	    }
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
	public void all_users_count1CompanyName(String req_date,JSONArray jsonArray,String userid,String companyname1)
	//db.userdetails.aggregate([ {$match: {userid: "1"}},  {$lookup: {    from: "username_tb",    localField: "useid",    foreignField: "id",    as: "username_tb"  }} ])
	
	{
		Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
		String beforemonthdate=daoImpl.createBeforeMonthDate();
		//String beforemonthdate="2019-10-01";
	   	System.out.println(req_date+"beforemonthdatebeforemonthdatebeforemonthdate"+beforemonthdate);
		
	         try {
				
			
	        	 System.out.println("ffffffffffffffffffffffffff");
	        	 
	        	 Connection connection=DbConnection_Smpp.getInstance().getConnection();
	     	   	Statement stmt = null;
	     	   	Statement stmt1 = null;
	     	   	ResultSet rs = null;
	     	   	ResultSet rs1 = null;
	     	   	try {
	     	        
	     	       stmt=connection.createStatement();
	     	       stmt1=connection.createStatement();
	     			/*
	     			 * String query = 
	     			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
	     			 * +req_date+"'  group by companyid;;";
	     			 */
	     	       String query = "select sum(s.count) as s_count,u.companyid, ser.servername, u.parentid, u.username, u.companyname,u.color,s.date   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where u.companyname='"+companyname1+"' and  (s.date  BETWEEN '"+beforemonthdate+"' AND '"+req_date+"') and u.userid="+userid+" and s.errorcode=0  group by companyid order by team,companyname;";
	     	       System.out.println("queryqueryqueryqueryquery"+query);
	     	       //logger.info("=========query=========="+query);
	     	       rs = stmt.executeQuery(query);
	     	       Gson gson = new Gson();

	     	        if(rs.isBeforeFirst()){
	     	       	while (rs.next()) {
	     	       		JSONObject jsonObject=new JSONObject();
	     	       		Count_sms_user response_p = new Count_sms_user();
	           		
	     	       
	     	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	     	       	response_p.setCompanyname(rs.getString("companyname"));
	     	       	response_p.setColor(rs.getString("color"));
	     	       	response_p.setUsername(rs.getString("username"));
	     	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	     	       	response_p.setRequested_date(rs.getString("date"));
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
	     	       	query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"+rs.getString("date")+"' and d.errorcode=0 and d.companyid = "+response_p.getCompanyid()+";"; 
	     	       	
	     	       	//logger.info("=========query1=========="+query);
	     	       	rs1  = stmt1.executeQuery(query);
	     	        if(rs1.isBeforeFirst()){
	     		       	while (rs1.next()) {
	     		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
	     		       jsonObject.put("d_count", rs1.getInt("d_count")+"");
	     		       	}
	     	        }
	     	       	
	     	       	
	     	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
		   	       	int companyid=rs.getInt("companyid");
		   	       
	     	        jsonArray.put(jobj);
	     	       	
	     	    }
	     	           }else{
	     	           
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
	         
	         } catch (Exception e) {
					// TODO: handle exception
				}
		
	}
	public void all_users_count1Spanel(String req_date,JSONArray jsonArray,String userid)
	{
		Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
		boolean checkmongodata=false;
		  MongoClient mongo = DbConnectionMongo.getInstance().getConnection();
		  DB db = mongo.getDB("count_sms");
		 DBCollection collection = db.getCollection("userdetails");
		 DBCollection smppdataCollection = db.getCollection("userdetails");
		 DBObject lookupOperation = (DBObject)new BasicDBObject(
	      		    "$lookup", new BasicDBObject("from", "spaneldata")
	      		        .append("localField", "companyid")
	      		        .append("foreignField", "companyid")
	      		        .append("as", "smppdata")       
	      		);
	       DBObject match = (DBObject)new BasicDBObject(
	     		    "$match", new BasicDBObject()
	  		        .append("userid", "5"));
	      // DBCursor cursor = collection.find();
	       List<DBObject> pipeline = Arrays.asList(match, lookupOperation);
	       AggregationOutput output = collection.aggregate(pipeline);
	         //logger.info("bbbbb"+output.toString());
	         try {
				
			
	         for (DBObject result : output.results()) {
	 			//numberOfTriples = Double.valueOf(result.get("sum").toString());
	        	// //logger.info(result.toString());
	        	 JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result.toString());
					JSONArray jsonArray1=jsonObject.getJSONArray("smppdata");
		        	 for(int i=0;i<jsonArray1.length(); i++) {
		        		 JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				         	JSONObject addjsonObject=new JSONObject();
				          	addjsonObject.put("companyid", jsonObject1.getString("companyid"));
				          	addjsonObject.put("submitted_count", jsonObject1.getString("s_count"));
				          	addjsonObject.put("delivered_count", jsonObject1.getString("d_count"));
				          	addjsonObject.put("color", jsonObject1.getString("color"));
				          	addjsonObject.put("companyname", jsonObject1.getString("companyname"));
				          	addjsonObject.put("requested_date", jsonObject1.getString("s_date"));
							addjsonObject.put("servername", "NA");
							
							jsonArray.put(addjsonObject);
		        		 
		        	 }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	 
	 		}
	         if(checkmongodata==false) {
	        	 Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
	     	   	Statement stmt = null;
	     	   	Statement stmt1 = null;
	     	   	ResultSet rs = null;
	     	   	ResultSet rs1 = null;
	     	   	try {
	     	        
	     	       stmt=connection.createStatement();
	     	       stmt1=connection.createStatement();
	     			/*
	     			 * String query =
	     			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
	     			 * +req_date+"'  group by companyid;;";
	     			 */
	     	       String query = "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname,u.color,s.date   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date like'"+req_date+"%' and u.userid="+userid+" and s.errorcode=0  group by companyid order by team,companyname;";
	     	       
	     	       //logger.info("=========query=========="+query);
	     	       rs = stmt.executeQuery(query);
	     	       Gson gson = new Gson();

	     	        if(rs.isBeforeFirst()){
	     	       	while (rs.next()) {
	     	       		Count_sms_user response_p = new Count_sms_user();
	           		
	     	       
	     	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	     	       	response_p.setCompanyname(rs.getString("companyname"));
	     	       	response_p.setColor(rs.getString("color"));
	     	       	response_p.setUsername(rs.getString("username"));
	     	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	     	       	response_p.setRequested_date(rs.getString("date"));
	     					/*
	     					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
	     					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
	     					 */
	     	       	query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"+rs.getString("date")+"' and d.errorcode=0 and d.companyid = "+response_p.getCompanyid()+";"; 
	     	       	
	     	       	//logger.info("=========query1=========="+query);
	     	       	rs1  = stmt1.executeQuery(query);
	     	        if(rs1.isBeforeFirst()){
	     		       	while (rs1.next()) {
	     		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
	     		       	}
	     	        }
	     	       	
	     	       	
	     	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	     	        jsonArray.put(jobj);
	     	       	
	     	    }
	     	           }else{
	     	           
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
	         } catch (Exception e) {
					// TODO: handle exception
				}finally {
					
						
						try {
						if(mongo!=null)
						mongo.close();

						// mongo.close();

						} catch (Exception e2) {
						// TODO: handle exception
						}
				}
		
	}
	
	
	public void CheckMaxC(String req_date,JSONArray jsonArray)
	{
	   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;
	   	Statement stmt1 = null;
	   	ResultSet rs = null;
	   	ResultSet rs1 = null;
	   	try {
	        
	       stmt=connection.createStatement();
	       stmt1=connection.createStatement();
			/*
			 * String query =
			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
			 * +req_date+"'  group by companyid;;";
			 */
	       String query = "select s.count as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"+req_date+"' and s.errorcode=0  group by companyid;;";
	       rs = stmt.executeQuery(query);
	       Gson gson = new Gson();

	        if(rs.isBeforeFirst()){
	       	while (rs.next()) {
	       		Count_sms_user response_p = new Count_sms_user();
      		
	       
	       	response_p.setCompanyid(rs.getInt("companyid")+"");
	       	response_p.setCompanyname(rs.getString("companyname"));
	       	response_p.setUsername(rs.getString("username"));
	       	response_p.setSubmitted_count(rs.getInt("s_count")+"");
	       	response_p.setRequested_date(req_date);
					/*
					 * query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"
					 * +req_date+"' and d.companyid = "+response_p.getCompanyid()+";";
					 */
	       	query = "select d.count as d_count from tbl_delivered d where d.date='"+req_date+"' and d.errorcode=0 and d.companyid = "+response_p.getCompanyid()+";"; 
	       	rs1  = stmt1.executeQuery(query);
	        if(rs1.isBeforeFirst()){
		       	while (rs1.next()) {
		       	response_p.setDelivered_count(rs1.getInt("d_count")+"");
		       	}
	        }
	       	
	       	
	       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
	        jsonArray.put(jobj);
	       	
	    }
	           }else{
	           
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
	
	public static void insertNewEmail(String email_num ,String subject,String email_from,String email_text,String email_time,String email_time_r)
	{
		Connection conn=DbConnection_Smpp.getInstance().getConnection();
		Statement stmt=null;
		ResultSet rs = null;
		PreparedStatement  ps =  null;
		
		try 
		{
			stmt=conn.createStatement();
			  ps=conn.prepareStatement("insert into table_mail(email_num,Subject,email_from,email_text,email_time,email_time_r) values (?,?,?,?,?,?);");
			  ps.setString(1,email_num );
			  ps.setString(2, subject);
			  ps.setString(3, email_from);
			  ps.setString(4, email_text);
			  ps.setString(5, email_time);
			  ps.setString(6, email_time_r);
			  ps.executeUpdate();
			
		}
		catch(Exception e)
		{
			
		}
		finally
        {
       	 try {
       	         if (conn != null)
       	      	conn.close();
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
       	         if (ps != null)
       	        	 ps.close();
       	      } catch (SQLException ignore) {} // no point handling
       	   
       	 }
  	  
		
	}
	
	
	public void dashboardCount(String req_date,pojo_admin_user_Complaints_count  response_p)
	{
	
		SyncEmail();

				 	Connection connection=DbConnection_Smpp.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	try {

					   		
				        stmt=connection.createStatement();
			         	String query_count = "SELECT \r\n" + 
				         			"   COUNT(CASE WHEN  `email_time` like '"+req_date+"%' THEN 1 END) AS total_count,\r\n" + 
				         			"   COUNT(CASE WHEN `resolved_status` = 0 and `email_time` like '"+req_date+"%'  THEN 1 END) AS pending_count,\r\n" + 
				         			"   COUNT(CASE WHEN `resolved_status` = 1 and `email_time` like '"+req_date+"%' THEN 1 END) AS process_count,\r\n" + 
				         			"   COUNT(CASE WHEN `resolved_status` = 2 and `email_time` like '"+req_date+"%' THEN 1 END) AS completed_count\r\n" + 
				         			"FROM `table_mail` ; ";			         	
			         	
			         	
			         	//logger.info("***********query count**********"+query_count);
			         	rs = stmt.executeQuery(query_count);
			         	 while (rs.next()) {
			         		response_p.setTotal_count(rs.getInt("total_count")+"");
			         		response_p.setPending_count(rs.getInt("pending_count")+"");
			         		response_p.setProcess_count(rs.getInt("process_count")+"");
			         		response_p.setCompleted_count(rs.getInt("completed_count")+"");
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

				   	}
				   
				   
	
	
	}
	
	public void checkNewEmail() {

		 Connection conn=DbConnection_Smpp.getInstance().getConnection();
		 Statement stmt=null;
		 Statement stmt1=null;
		 ResultSet rs = null;
		 ResultSet rs1 = null;
		 PreparedStatement  ps1 =  null;

	   	  try {
	   		  
	   		  //======================================Check new email=========================================================//
	   	      String host = "pop.gmail.com";// change accordingly
	   	      String mailStoreType = "pop3";
	   	      String username = "vns.techsupport@virtuosonetsoft.in";// change accordingly
	   	      String password = "VNStechsupport@123";// change accordingly

	   	      check(host, mailStoreType, username, password);
	   	      
	   		  
			/*
			 * stmt=conn.createStatement(); String query =
			 * "select count(*) from admin_division where contact_mobile='"
			 * +contact_mobile+"' and password='"+password+"'"; rs =
			 * stmt.executeQuery(query); while (rs.next()) { count=rs.getInt(1); }
			 * if(count!=0){ stmt1=conn.createStatement();
			 * 
			 * //===get user details====// String query1 =
			 * "select * from admin_division where contact_mobile='"
			 * +contact_mobile+"' and password='"+password+"'"; rs1 =
			 * stmt1.executeQuery(query1); while (rs1.next()) {
			 * extractUserFromResultSet(rs1,division); division.setOtp_verified("2");
			 * status=true;
			 * 
			 * } ps1.executeUpdate();
			 * 
			 * ps1=conn.prepareStatement("update user set otp_verified=2");
			 * ps1.executeUpdate();
			 * 
			 * 
			 * }else{ status=false; } conn.close();
			 */

	         } catch (Exception e) {
	             e.printStackTrace();
	         }finally
	         {
	        	 try {
	        	         if (conn != null)
	        	      	conn.close();
	        	      } catch (SQLException ignore) {} // no point handling

	        	      try {
	        	         if (stmt != null)
	        	             stmt.close();
	        	      } catch (SQLException ignore) {} // no point handling

	        	   try {
	        	         if (stmt1 != null)
	        	        	 stmt1.close();
	        	      } catch (SQLException ignore) {} // no point handling
	        	   try {
	        	         if (rs != null)
	        	        	 rs.close();
	        	      } catch (SQLException ignore) {} // no point handling
	        	   try {
	        	         if (rs1 != null)
	        	        	 rs1.close();
	        	      } catch (SQLException ignore) {} // no point handling
	        	   
	        	   try {
	        	         if (ps1 != null)
	        	        	 ps1.close();
	        	      } catch (SQLException ignore) {} // no point handling
	        	   
	        	 }
	   	  
	   	  
	   	}

	
	String user_type,auth_key;
	//################ admin_user_Complaints_count ################//
 public void smpp_Complaints_count(pojo_admin_user_Complaints_count  response_p,String from_date,String to_date){
	   	
	 	Connection connection=DbConnection_Smpp.getInstance().getConnection();
	   	Statement stmt = null;
	   	ResultSet rs = null;
	   	try {
//	   		SELECT 
//	   	   COUNT(CASE WHEN  email_time > '2019-08-09' and email_time < '2019-08-12' THEN 1 END) AS total_count,
//	   	   COUNT(CASE WHEN `resolve_status` = 0 and email_time > '2019-08-09' and email_time < '2019-08-12'  THEN 1 END) AS pending_count,
//	   	   COUNT(CASE WHEN `resolve_status` = 1 and email_time > '2019-08-09' and email_time < '2019-08-12' THEN 1 END) AS process_count,
//	   	   COUNT(CASE WHEN `resolve_status` = 2 and email_time > '2019-08-09' and email_time < '2019-08-12' THEN 1 END) AS completed_count
//	   	   FROM `table_mail` ; 
		   		
	        stmt=connection.createStatement();
         	String query_count = "SELECT \r\n" + 
         			"   COUNT(CASE WHEN  complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"' THEN 1 END) AS total_count,\r\n" + 
         			"   COUNT(CASE WHEN `resolved_status` = 0 and complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"'  THEN 1 END) AS pending_count,\r\n" + 
         			"   COUNT(CASE WHEN `resolved_status` = 1 and complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"' THEN 1 END) AS process_count,\r\n" + 
         			"   COUNT(CASE WHEN `resolved_status` = 2 and complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"' THEN 1 END) AS completed_count\r\n" + 
         			"FROM `user_complaint` join admin_user on admin_user.depots_id =  user_complaint.depots_id where admin_user.auth_key='"+auth_key+"'; ";
         	
         	
         	if(user_type.equalsIgnoreCase("1"))
         	{
         		query_count = "SELECT \r\n" + 
	         			"   COUNT(CASE WHEN  complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"' THEN 1 END) AS total_count,\r\n" + 
	         			"   COUNT(CASE WHEN `resolved_status` = 0 and complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"'  THEN 1 END) AS pending_count,\r\n" + 
	         			"   COUNT(CASE WHEN `resolved_status` = 1 and complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"' THEN 1 END) AS process_count,\r\n" + 
	         			"   COUNT(CASE WHEN `resolved_status` = 2 and complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"' THEN 1 END) AS completed_count\r\n" + 
	         			"FROM `user_complaint` ; ";

         	}
         	
         	else if(user_type.equalsIgnoreCase("2"))
         	{
         		query_count = "SELECT \r\n" + 
	         			"   COUNT(CASE WHEN  complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"' THEN 1 END) AS total_count,\r\n" + 
	         			"   COUNT(CASE WHEN `resolved_status` = 0 and complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"'  THEN 1 END) AS pending_count,\r\n" + 
	         			"   COUNT(CASE WHEN `resolved_status` = 1 and complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"' THEN 1 END) AS process_count,\r\n" + 
	         			"   COUNT(CASE WHEN `resolved_status` = 2 and complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"' THEN 1 END) AS completed_count\r\n" + 
	         			"FROM `user_complaint` where depots_type_id = (select depot_type_id from admin_user where auth_key = '"+auth_key+"') ;  ; ";

         	}
         	else if(user_type.equalsIgnoreCase("3"))
         	{
         		
         	}
         	else if(user_type.equalsIgnoreCase("4"))
         	{
         		query_count = "SELECT \r\n" + 
	         			"   COUNT(CASE WHEN  complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"' THEN 1 END) AS total_count,\r\n" + 
	         			"   COUNT(CASE WHEN `resolved_status` = 0 and complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"'  THEN 1 END) AS pending_count,\r\n" + 
	         			"   COUNT(CASE WHEN `resolved_status` = 1 and complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"' THEN 1 END) AS process_count,\r\n" + 
	         			"   COUNT(CASE WHEN `resolved_status` = 2 and complaint_date > '"+from_date+"' and complaint_date < '"+to_date+"' THEN 1 END) AS completed_count\r\n" + 
	         			"FROM `user_complaint` join admin_user on admin_user.depots_id =  user_complaint.depots_id where admin_user.auth_key='"+auth_key+"'; ";

         	}
         	
         	//logger.info("***********query count**********"+query_count);
         	rs = stmt.executeQuery(query_count);
         	 while (rs.next()) {
         		response_p.setTotal_count(rs.getInt("total_count")+"");
         		response_p.setPending_count(rs.getInt("pending_count")+"");
         		response_p.setProcess_count(rs.getInt("process_count")+"");
         		response_p.setCompleted_count(rs.getInt("completed_count")+"");
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

	   	}
	   
	   }
	
	
	public int changeUserName( String userdetailsid, String usernameid) {
	       int status1 = 0;
			
			Connection connection=DbConnection_Smpp.getInstance().getConnection();
			PreparedStatement  ps1 = null;
			Statement stmt1 = null;
			ResultSet rs1 = null;
		     try{
		    	 stmt1 = connection.createStatement();
		         ps1=connection.prepareStatement("update userdetails set userid=? where id=?");
		         ps1.setString(1, usernameid);
		         ps1.setString(2, userdetailsid);
		         status1 = ps1.executeUpdate();
		     }
		     catch(Exception e){
		         //logger.info(e);
		     }finally{
		    	 
		    	
		 		try {
			   	       if (connection != null)
			   	    	connection.close();
			   	    } catch (SQLException ignore) {} // no point handling
		 		try {
			   	       if (ps1 != null)
			   	    	ps1.close();
			   	    } catch (SQLException ignore) {} // no point handling
		 		try {
			   	       if (stmt1 != null)
			   	    	stmt1.close();
			   	    } catch (SQLException ignore) {} // no point handling
		 		try {
			   	       if (rs1 != null)
			   	    	rs1.close();
			   	    } catch (SQLException ignore) {} // no point handling
		 		
			}
			
			
		return status1;
		}
	
	public int changeUserpassword(String  userid,String newpass) {
	       int status1 = 0;
			
			Connection connection=DbConnection_Smpp.getInstance().getConnection();
			PreparedStatement  ps1 = null;
			Statement stmt1 = null;
			ResultSet rs1 = null;
		     try{
		    	 stmt1 = connection.createStatement();
		         ps1=connection.prepareStatement("update username_tb set password=? where id=?");
		         ps1.setString(1, newpass);
		         ps1.setInt(2, Integer.parseInt(userid));
		         status1 = ps1.executeUpdate();
		     }
		     catch(Exception e){
		         //logger.info(e);
		     }finally{
		    	 
		    	
		 		try {
			   	       if (connection != null)
			   	    	connection.close();
			   	    } catch (SQLException ignore) {} // no point handling
		 		try {
			   	       if (ps1 != null)
			   	    	ps1.close();
			   	    } catch (SQLException ignore) {} // no point handling
		 		try {
			   	       if (stmt1 != null)
			   	    	stmt1.close();
			   	    } catch (SQLException ignore) {} // no point handling
		 		try {
			   	       if (rs1 != null)
			   	    	rs1.close();
			   	    } catch (SQLException ignore) {} // no point handling
		 		
			}
			
			
		return status1;
		}
	
	
	
	public static void check(String host, String storeType, String user,
		      String password) 
		   {
		      try {

		      //create properties field
		      Properties properties = new Properties();
		      properties.put("mail.pop3.host", host);
		      properties.put("mail.pop3.port", "995");
		      properties.put("mail.pop3.starttls.enable", "true");
		      Session emailSession = Session.getDefaultInstance(System.getProperties());
		      //create the POP3 store object and connect with the pop server
		      //Store store = emailSession.getStore("pop3s");
			   Store store = emailSession.getStore("imaps");
		      store.connect(host, user, password);
		      //create the folder object and open it
		      Folder emailFolder = store.getFolder("INBOX");
		      emailFolder.open(Folder.READ_ONLY);
		      // retrieve the messages from the folder in an array and print it
		      Message[] messages = emailFolder.getMessages();
		      //logger.info("messages.length---" + messages.length);
		      
		      
		      	int max_email_no = max_Email_Number();
		      	
		      	//logger.info("----------------return from here1---" + messages.length);
		      	if(max_email_no>=messages.length)
		      	{
		      		//logger.info("----------------return from here2---" + messages.length);
		      		return;
		      	}
		      
		      for (int i = messages.length-1, n = messages.length-5; i > n; i--) {
		    	  
		    	  
		    	  if(max_email_no>=messages.length)
			      	{
		    		  //logger.info("----------------return from here3---" + messages.length);
			      		return;
			      	}
		    	  //logger.info("----------------return from here4---" + messages.length);
		         Message message = messages[i];
		         //logger.info("---------------------------------");
		         //logger.info("Email Number " + (i + 1));
		         //logger.info("Subject: " + message.getSubject());
		         //logger.info("From: " + message.getFrom()[0]);
				 //logger.info("Date: " + message.getReceivedDate().toString());
		         //logger.info("Text: " + message.toString());
		         
		         String date_time = message.getReceivedDate().toString();
		         
		         //Mon Aug 12 15:17:48 IST 2019 2019-08-12 17:05:16
		         String date_format[]=date_time.split(" ");
		         String date_time_formatted = date_format[date_format.length-1]+"-"+getMonth(date_format[1])+"-"+date_format[2]+" "+date_format[3];
		        
		         

		         
		         insertNewEmail((i + 1)+"",message.getSubject(),message.getFrom()[0].toString().replaceAll("<","").replaceAll(">","")+"",message.toString(),date_time_formatted,date_time);
		        
		      }
		      //close the store and folder objects
		      emailFolder.close(false);
		      store.close();

		      } catch (NoSuchProviderException e) {
		         e.printStackTrace();
		      } catch (MessagingException e) {
		         e.printStackTrace();
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
		   }
	
	static String getMonth(String month)
	{
		String return_val="";
		if(month.equalsIgnoreCase("jan"))
		{
			return_val = "01";	
		}
		else if(month.equalsIgnoreCase("feb"))
		{
			return_val = "02";	
		}
		else if(month.equalsIgnoreCase("mar"))
		{
			return_val = "03";	
		}
		else if(month.equalsIgnoreCase("apr"))
		{
			return_val = "04";	
		}
		else if(month.equalsIgnoreCase("may"))
		{
			return_val = "05";	
		}
		else if(month.equalsIgnoreCase("jun"))
		{
			return_val = "06";	
		}
		else if(month.equalsIgnoreCase("jul"))
		{
			return_val = "07";	
		}
		else if(month.equalsIgnoreCase("aug"))
		{
			return_val = "08";	
		}
		else if(month.equalsIgnoreCase("sep"))
		{
			return_val = "09";	
		}
		else if(month.equalsIgnoreCase("oct"))
		{
			return_val = "10";	
		}
		else if(month.equalsIgnoreCase("nov"))
		{
			return_val = "11";	
		}
		else if(month.equalsIgnoreCase("dec"))
		{
			return_val = "12";	
		}
		
		return return_val;
		
	}
	
	
	
	
	
	//################ complaint_History ################//
			 public void complaint_History(JSONArray jsonArray,String req_date,int kid, String count,String resolved_status){
				   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	try {
				       stmt=connection.createStatement();
				       
				       String query = "";
				       if(kid>0)
				       {
				    	query = "select * from table_mail  where email_time like '"+req_date+"%' and resolved_status=0  and id<"+kid+"  order by id desc LIMIT "+count+"; ";    
				       }
				       else
				       {
				    	 
				        query = "select * from table_mail  where email_time like '"+req_date+"%' and resolved_status="+resolved_status+"  order by id desc LIMIT "+count+"; ";
				       }
	    	           
				       
	    	           //logger.info("************query for query***********"+query);
	    	           rs = stmt.executeQuery(query);
				       Gson gson = new Gson();

				        if(rs.isBeforeFirst()){
				       	while (rs.next()) {
				       	pojo_Complaint response_p = new pojo_Complaint();
				       	
				       	response_p.setId(rs.getInt("id")+"");
				       	response_p.setEmail_num(rs.getInt("email_num")+"");
				       	response_p.setSubject(rs.getString("subject")+"");
				       	response_p.setEmail_from(rs.getString("email_from"));
				       	response_p.setEmail_text(rs.getString("email_text"));
				       	response_p.setEmail_time(rs.getString("email_time"));
				    	response_p.setEmail_time_r(rs.getString("email_time_r"));
				       	response_p.setResolved_status(rs.getInt("resolved_status")+"");
				       	
				       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
				        jsonArray.put(jobj);
				       	
				    }
				           }else{
				           
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

				   	}
				   
				   }		
	
	
	
	
	
					//################ admin_user_UpdateComplaintStatus ################//	 
				public int admin_user_UpdateComplaintStatus( String complaint_id, String resolved_status) {
				       int status1 = 0;
						
						Connection connection=DbConnection_Smpp.getInstance().getConnection();
						PreparedStatement  ps1 = null;
						Statement stmt1 = null;
						ResultSet rs1 = null;
					     try{
					    	 stmt1 = connection.createStatement();
					    	 //logger.info("******admin_user_UpdateComplaintStatus*********1");
					         ps1=connection.prepareStatement("update table_mail set resolved_status=? where id=?");
					         ps1.setString(1, resolved_status);
					         ps1.setString(2, complaint_id);
					         status1 = ps1.executeUpdate();
					         if(status1>0){
					        	 //logger.info("******admin_user_UpdateComplaintStatus*********2");
					         }else{
					        	 //logger.info("******admin_user_UpdateComplaintStatus*********3");
					         }
					         
	        
					    }
					     catch(Exception e){
					         //logger.info(e);
					     }finally{
					    	 
					    	
					 		try {
						   	       if (connection != null)
						   	    	connection.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (ps1 != null)
						   	    	ps1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (stmt1 != null)
						   	    	stmt1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (rs1 != null)
						   	    	rs1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		
						}
						
						
					return status1;
					}	
	
	
	
				static Thread t = null;
				//===========Common to send notification==============//
				public void SyncEmail()
				{
					if(t==null) {
					MyRunnable_SyncEmail myRunnable = new MyRunnable_SyncEmail();
				
					t = new Thread(myRunnable);
					}
					if(t.isAlive())
					{
						//logger.info("*********************************Thread is alive");
					}
					else
					{
						MyRunnable_SyncEmail myRunnable = new MyRunnable_SyncEmail();
						
						t = new Thread(myRunnable);
						//logger.info("*********************************Thread is  not alive");
			        
						t.start();
					}
					
				}




				public Boolean checkSmppUser(SmppUser smppUser) {//					TrippleDes td= new TrippleDes();
					
					 Boolean status =false;
					 int count=0;
					 int id=0;
					 Connection conn=DbConnection_Smpp.getInstance().getConnection();
					 Statement stmt=null;
					 Statement stmt1=null;
					 ResultSet rs = null;
					 ResultSet rs1 = null;
					 
				   	 
				   	  try {
				         	
				         	stmt=conn.createStatement();
				         	String query = "select count(*) from username_tb where name='"+smppUser.getName()+"' and password='"+smppUser.getPassword()+"'";
				         	rs = stmt.executeQuery(query);
				         	 while (rs.next()) {
				         		 count=rs.getInt(1);
				         	 }
				         		 if(count!=0){
				         			stmt1=conn.createStatement();
				         			String query1 = "select * from username_tb where name='"+smppUser.getName()+"' and password='"+smppUser.getPassword()+"'";
				         			rs1 = stmt1.executeQuery(query1);
				                	 while (rs1.next()) {
				                		 
				                		 id=rs1.getInt("id");
				                		 smppUser.setId(id);
				         		 
				         		 status=true;
				         		 
				                	 }
				         		 }else{
				         			 status=false;
				         		 }
				    			conn.close();

				         } catch (Exception e) {
				             e.printStackTrace();
				         }finally
				         {
				        	 try {
				        	         if (conn != null)
				        	      	conn.close();
				        	      } catch (SQLException ignore) {} // no point handling

				        	      try {
				        	         if (stmt != null)
				        	             stmt.close();
				        	      } catch (SQLException ignore) {} // no point handling

				        	   try {
				        	         if (stmt1 != null)
				        	        	 stmt1.close();
				        	      } catch (SQLException ignore) {} // no point handling
				        	 }
				   	  
				   	  
				     return status; }
				public void InsertUpdateTesyncTable(String client,String submited,String delivered,String delivered_in_per,String undelivered,String undelivered_in_per,String error_1,String error_6,String error_21,String error_32,String error_34,String error_69,String error_253,String error_254,String requested_date) {
					boolean checkTesynTable=checkTesynTable(client,requested_date);

				       int status1 = 0;
						
						Connection connection=DbConnection_Smpp.getInstance().getConnection();
						PreparedStatement  ps1 = null;
						Statement stmt1 = null;
						ResultSet rs1 = null;
					     try{
					    	 stmt1 = connection.createStatement();
					    	 //logger.info("******InsertUpdateTesyncTable*********"+checkTesynTable);
					    	 if(checkTesynTable==true) {
					    		 ps1=connection.prepareStatement("update tesync_table set submited=?,delivered=?,delivered_in_per=?,undelivered=?,undelivered_in_per=?,error_1=?,error_6=?,error_21=?,error_32=?,error_34=?,error_69=?,error_253=?,error_254=? where client=? and requested_date=?");
					    	 }else {
					    		 ps1=connection.prepareStatement("insert into tesync_table  (submited,delivered,delivered_in_per,undelivered,undelivered_in_per,error_1,error_6,error_21,error_32,error_34,error_69,error_253,error_254,client,requested_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					    	 }
					        
					         ps1.setString(1, submited);
					         ps1.setString(2, delivered);
					         ps1.setString(3, delivered_in_per);
					         ps1.setString(4, undelivered);
					         ps1.setString(5, undelivered_in_per);
					         ps1.setString(6, error_1);
					         ps1.setString(7, error_6);
					         ps1.setString(8, error_21);
					         ps1.setString(9, error_32);
					         ps1.setString(10, error_34);
					         ps1.setString(11, error_69);
					         ps1.setString(12, error_253);
					         ps1.setString(13, error_254);
					         ps1.setString(14, client);
					         ps1.setString(15, requested_date);
					         status1 = ps1.executeUpdate();
					         if(status1>0) {
					        	 //logger.info("status1status1"+status1);
					         }else {
					        	 //logger.info("status1status1"+status1);
					         }
					         
	        
					    }
					     catch(Exception e){
					         //logger.info(e);
					     }finally{
					    	 
					    	
					 		try {
						   	       if (connection != null)
						   	    	connection.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (ps1 != null)
						   	    	ps1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (stmt1 != null)
						   	    	stmt1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (rs1 != null)
						   	    	rs1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		
						}
						
					
				}




				public boolean checkTesynTable(String client, String requested_date) {//					TrippleDes td= new TrippleDes();
					
					 Boolean status =false;
					
					 Connection conn=DbConnection_Smpp.getInstance().getConnection();
					 Statement stmt=null;
					ResultSet rs = null;
					 
				   	 
				   	  try {
				         	
				         	stmt=conn.createStatement();
				         	String query = "select * from tesync_table where client='"+client+"' and requested_date='"+requested_date+"'";
				         	rs = stmt.executeQuery(query);
				         	 while (rs.next()) {
				         		 //logger.info("query"+query);
				         		status=true;
				         	 }
				         		
				    			conn.close();

				         } catch (Exception e) {
				             e.printStackTrace();
				         }finally
				         {
				        	 try {
				        	         if (conn != null)
				        	      	conn.close();
				        	      } catch (SQLException ignore) {} // no point handling

				        	      try {
				        	         if (stmt != null)
				        	             stmt.close();
				        	      } catch (SQLException ignore) {} // no point handling

				        	    // no point handling
				        	 }
				   	  
				   	  
				     return status; }
				public void getTesyncvalue(JSONArray tesyncarray) {
					String body="";
					try {
						URL url = new URL("http://smsc.tesync.net:8089/temtapp/index.php/dashboard/getClientReport");
										//   http://smsc.tesync.net:8089/temtapp/index.php/dashboard
						URLConnection con = url.openConnection();
						InputStream in = con.getInputStream();
						String encoding = con.getContentEncoding();
						encoding = encoding == null ? "UTF-8" : encoding;
						body = IOUtils.toString(in, encoding);
						////logger.info(body);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				 String html = "<html><head><title>First parse</title></head><table>"+body+"</table> <body><p>Parsed HTML into a doc.</p></body></html>";
				 Document doc = Jsoup.parse(html);
				 Element table = doc.select("table").get(0); //select the first table.
		         Elements rows = table.select("tr");
		         //logger.info("sssssss"+rows.size());
		         if(rows.size()>0) {
		        	 String s_date1=java.time.LocalDate.now().toString();
		        	 
		        	TysncDeleteMongiDbData(s_date1);
		        for (int i = 0; i < rows.size(); i++) { //first row is the col names so skip it.
		        	JSONObject jsonObject=new JSONObject();
		             Element row = rows.get(i);
		             Elements cols = row.select("td");
		             String client=cols.get(0).text();
	                 String submited=cols.get(1).text();
	                 String delivered=cols.get(2).text();
	                 String delivered_in_per=cols.get(3).text();
	                 String undelivered=cols.get(4).text();
	                 String undelivered_in_per=cols.get(5).text();
	                 String error_1=cols.get(6).text();
	                 String error_6=cols.get(7).text();
	                 String error_21=cols.get(8).text();
	                 String error_32=cols.get(9).text();
	                 String error_34=cols.get(10).text();
	                 String error_69=cols.get(11).text();
	                 String error_253=cols.get(12).text();
	                 String error_254=cols.get(13).text();
	                 String requested_date=java.time.LocalDate.now().toString();
	                 try {
						jsonObject.put("client", client);
						jsonObject.put("submited", submited);
						jsonObject.put("delivered", delivered);
						jsonObject.put("delivered_in_per", delivered_in_per);
						jsonObject.put("undelivered", undelivered);
						jsonObject.put("undelivered_in_per", undelivered_in_per);
						jsonObject.put("error_1", error_1);
						jsonObject.put("error_6", error_6);
						jsonObject.put("error_21", error_21);
						jsonObject.put("error_32", error_32);
						jsonObject.put("error_34", error_34);
						jsonObject.put("error_69", error_69);
						jsonObject.put("error_253", error_253);
						jsonObject.put("error_254", error_254);
						jsonObject.put("requested_date", requested_date);
						if(client.equalsIgnoreCase("valueF") || client.equalsIgnoreCase("vfpromo") || client.equalsIgnoreCase("vnsoft_tr") || client.equalsIgnoreCase("vnsoft_tr1") || client.equalsIgnoreCase("vnsoftvt_pr") || 
								client.equalsIgnoreCase("vnsvns") || client.equalsIgnoreCase("netxcell")
								|| client.equalsIgnoreCase("vfirst") || client.equalsIgnoreCase("vnsvns2") || client.equalsIgnoreCase("vnsvns3")
								|| client.equalsIgnoreCase("VmobiT1") || client.equalsIgnoreCase("VmobiT2") || client.equalsIgnoreCase("VmobiT3") || client.equalsIgnoreCase("VmobiPD1") || client.equalsIgnoreCase("VmobiPD2")) {
							MyRunnable myRunnable=new MyRunnable(client,submited,delivered,delivered_in_per,undelivered,undelivered_in_per,error_1,error_6,error_21,error_32,error_34,error_69,error_253,error_254,requested_date);
			                 Thread t = new Thread(myRunnable);
			 	            t.start();
						}
						if(client.equalsIgnoreCase("vnsvns") || client.equalsIgnoreCase("vnsvns2") || client.equalsIgnoreCase("vnsvns3") || client.equalsIgnoreCase("vnsvns4") || client.equalsIgnoreCase("vnsvns5")
				   				|| client.equalsIgnoreCase("movil") || client.equalsIgnoreCase("movil2") || client.equalsIgnoreCase("movil3")
				   				|| jsonObject.getString("client").equalsIgnoreCase("VmobiT2") || jsonObject.getString("client").equalsIgnoreCase("VmobiT3") || jsonObject.getString("client").equalsIgnoreCase("VmobiT1") || jsonObject.getString("client").equalsIgnoreCase("VmobiPD1") || jsonObject.getString("client").equalsIgnoreCase("VmobiPD2")
				   				|| jsonObject.getString("client").equalsIgnoreCase("sphere_p1s") || jsonObject.getString("client").equalsIgnoreCase("sphere_p3s") || jsonObject.getString("client").equalsIgnoreCase("sphere_t2s") || jsonObject.getString("client").equalsIgnoreCase("sphere_t3s") || jsonObject.getString("client").equalsIgnoreCase("sphere_t5s")
								) {
							MyRunnable myRunnable=new MyRunnable(client,submited,delivered,delivered_in_per,undelivered,undelivered_in_per,error_1,error_6,error_21,error_32,error_34,error_69,error_253,error_254,requested_date);
			                 Thread t = new Thread(myRunnable);
			 	            t.start();
						}
						
						
						tesyncarray.put(jsonObject);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }}
				}
				public void TysncDeleteMongiDbData(String s_date1) {
					  MongoClient mongo = DbConnectionMongo.getInstance().getConnection();
					  DB db = mongo.getDB("count_sms");
					  DBCollection collection = db.getCollection("tesync_table");
					try {
						BasicDBObject document = new BasicDBObject();
						document.put("requested_date", s_date1);
						collection.remove(document);
					} catch (Exception e) {
						// TODO: handle exception
					}finally {
						
							
							try {
							if(mongo!=null)
							mongo.close();

							// mongo.close();

							} catch (Exception e2) {
							// TODO: handle exception
							}}
					
					
				}
				public void getTesyncvaluePanel(JSONArray tesyncarray) {
					String body="";
					try {
						URL url = new URL("http://smsc.tesync.net:8089/temtapp/index.php/dashboard/getClientReport");
										//   http://smsc.tesync.net:8089/temtapp/index.php/dashboard
						URLConnection con = url.openConnection();
						InputStream in = con.getInputStream();
						String encoding = con.getContentEncoding();
						encoding = encoding == null ? "UTF-8" : encoding;
						body = IOUtils.toString(in, encoding);
						////logger.info(body);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				 String html = "<html><head><title>First parse</title></head><table>"+body+"</table> <body><p>Parsed HTML into a doc.</p></body></html>";
				 Document doc = Jsoup.parse(html);
				 Element table = doc.select("table").get(0); //select the first table.
		         Elements rows = table.select("tr");
		         //logger.info("sssssss"+rows.size());
		        for (int i = 0; i < rows.size(); i++) { //first row is the col names so skip it.
		        	JSONObject jsonObject=new JSONObject();
		             Element row = rows.get(i);
		             Elements cols = row.select("td");
		             String client=cols.get(0).text();
	                 String submited=cols.get(1).text();
	                 String delivered=cols.get(2).text();
	                 String delivered_in_per=cols.get(3).text();
	                 String undelivered=cols.get(4).text();
	                 String undelivered_in_per=cols.get(5).text();
	                 String error_1=cols.get(6).text();
	                 String error_6=cols.get(7).text();
	                 String error_21=cols.get(8).text();
	                 String error_32=cols.get(9).text();
	                 String error_34=cols.get(10).text();
	                 String error_69=cols.get(11).text();
	                 String error_253=cols.get(12).text();
	                 String error_254=cols.get(13).text();
	                 String requested_date=java.time.LocalDate.now().toString();
	                 try {
						jsonObject.put("client", client);
						jsonObject.put("submited", submited);
						jsonObject.put("delivered", delivered);
						jsonObject.put("delivered_in_per", delivered_in_per);
						jsonObject.put("undelivered", undelivered);
						jsonObject.put("undelivered_in_per", undelivered_in_per);
						jsonObject.put("error_1", error_1);
						jsonObject.put("error_6", error_6);
						jsonObject.put("error_21", error_21);
						jsonObject.put("error_32", error_32);
						jsonObject.put("error_34", error_34);
						jsonObject.put("error_69", error_69);
						jsonObject.put("error_253", error_253);
						jsonObject.put("error_254", error_254);
						jsonObject.put("requested_date", requested_date);
						tesyncarray.put(jsonObject);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
				}
				public void getTysncCount(JSONObject tjsonObject,String requested_date) {

					
					Smpp_DaoImpl smpp_dao=new Smpp_DaoImpl();
					 if(requested_date.equalsIgnoreCase(java.time.LocalDate.now().toString())){
						 smpp_dao.getTesyncvalueCount(tjsonObject);
						 
					 }else {
						 long submittedcount=0;
						 long deliverCount=0;
						 boolean checkmongodata=false;
						  MongoClient mongo = DbConnectionMongo.getInstance().getConnection();
						  DB db = mongo.getDB("count_sms");
					
						 DBCollection collection = db.getCollection("tesync_table");
						 DBCursor cursor1 = collection.find(new BasicDBObject().append("requested_date", new BasicDBObject("$regex", requested_date)));
						//logger.info("fffffffffffffffffnnnnnnnnnnnn");
						try {
							
						
					       while(cursor1.hasNext()) {
					    		
					    	   checkmongodata=true;
					         //  //logger.info(cursor1.next());
					    	   String data=cursor1.next().toString();
					    	   ////logger.info(data);
					          JSONObject jsonObject1;
							try {
								jsonObject1 = new JSONObject(data);
						        
						         submittedcount=submittedcount+Integer.parseInt(jsonObject1.getString("submited"));
						         deliverCount=deliverCount+Integer.parseInt(jsonObject1.getString("delivered"));
					         		
						        
							} catch (JSONException e) {
								e.printStackTrace();
							}
					         
					       }
					       if(checkmongodata==false) {
					    	   Connection conn=DbConnection_Smpp.getInstance().getConnection();
								 
								 
								 Statement stmt=null;
								ResultSet rs = null;
								try {
									stmt=conn.createStatement();
									String query = "select * from tesync_table where requested_date like '"+requested_date+"%'";
						         	rs = stmt.executeQuery(query);
						         	 while (rs.next()) {
						         		submittedcount=submittedcount+Integer.parseInt(rs.getString("submited"));
								         deliverCount=deliverCount+Integer.parseInt(rs.getString("delivered"));
						         		
						         		 
						         	 }
								} catch ( Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					       }
					       
								tjsonObject.put("submittedcount", submittedcount);
								tjsonObject.put("deliverCount", deliverCount);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}finally {try {
								if(cursor1!=null)
									cursor1.close();

									// mongo.close();

									} catch (Exception e2) {
									// TODO: handle exception
									}
									
									try {
									if(mongo!=null)
									mongo.close();

									// mongo.close();

									} catch (Exception e2) {
									// TODO: handle exception
									}
}
					 }
				
				}
				public void getTesyncvalueCount(JSONObject tjsonObject) {
					
					String body="";
					long submittedcount=0;
					long deliverCount=0;
					try {
						URL url = new URL("http://smsc.tesync.net:8089/temtapp/index.php/dashboard/getClientReport");
										//   http://smsc.tesync.net:8089/temtapp/index.php/dashboard
						URLConnection con = url.openConnection();
						InputStream in = con.getInputStream();
						String encoding = con.getContentEncoding();
						encoding = encoding == null ? "UTF-8" : encoding;
						body = IOUtils.toString(in, encoding);
						////logger.info(body);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				 String html = "<html><head><title>First parse</title></head><table>"+body+"</table> <body><p>Parsed HTML into a doc.</p></body></html>";
				 Document doc = Jsoup.parse(html);
				 Element table = doc.select("table").get(0); //select the first table.
		         Elements rows = table.select("tr");
		         //logger.info("sssssss"+rows.size());
		        for (int i = 0; i < rows.size(); i++) { //first row is the col names so skip it.
		        	
		             Element row = rows.get(i);
		             Elements cols = row.select("td");
		             String client=cols.get(0).text();
	                 String submited=cols.get(1).text();
	                 String delivered=cols.get(2).text();
	                 String delivered_in_per=cols.get(3).text();
	                 String undelivered=cols.get(4).text();
	                 String undelivered_in_per=cols.get(5).text();
	                 String error_1=cols.get(6).text();
	                 String error_6=cols.get(7).text();
	                 String error_21=cols.get(8).text();
	                 String error_32=cols.get(9).text();
	                 String error_34=cols.get(10).text();
	                 String error_69=cols.get(11).text();
	                 String error_253=cols.get(12).text();
	                 String error_254=cols.get(13).text();
	                 String requested_date=java.time.LocalDate.now().toString();
	                 try {
						
						if(client.equalsIgnoreCase("valueF") || client.equalsIgnoreCase("vfpromo") || client.equalsIgnoreCase("vnsoft_tr") || client.equalsIgnoreCase("vnsoft_tr1") || client.equalsIgnoreCase("vnsoftvt_pr") || 
								client.equalsIgnoreCase("vnsvns") || client.equalsIgnoreCase("netxcell")
								|| client.equalsIgnoreCase("vfirst") || client.equalsIgnoreCase("vnsvns2") || client.equalsIgnoreCase("vnsvns3")
								|| client.equalsIgnoreCase("VmobiT1") || client.equalsIgnoreCase("VmobiT2") || client.equalsIgnoreCase("VmobiT3") || client.equalsIgnoreCase("VmobiPD1") || client.equalsIgnoreCase("VmobiPD2")) {
							
							submittedcount=submittedcount+Integer.parseInt(submited);
							deliverCount=deliverCount+Integer.parseInt(delivered);
						}
						if(client.equalsIgnoreCase("vnsvns") || client.equalsIgnoreCase("vnsvns2") || client.equalsIgnoreCase("vnsvns3") || client.equalsIgnoreCase("vnsvns4") || client.equalsIgnoreCase("vnsvns5")
				   				|| client.equalsIgnoreCase("movil") || client.equalsIgnoreCase("movil2") || client.equalsIgnoreCase("movil3")
				   				|| client.equalsIgnoreCase("VmobiT2") || client.equalsIgnoreCase("VmobiT3") || client.equalsIgnoreCase("VmobiT1") || client.equalsIgnoreCase("VmobiPD1") || client.equalsIgnoreCase("VmobiPD2")
				   				|| client.equalsIgnoreCase("sphere_p1s") || client.equalsIgnoreCase("sphere_p3s") || client.equalsIgnoreCase("sphere_t2s") || client.equalsIgnoreCase("sphere_t3s") || client.equalsIgnoreCase("sphere_t5s")
								) {
							submittedcount=submittedcount+Integer.parseInt(submited);
							deliverCount=deliverCount+Integer.parseInt(delivered);
						}
						
						
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
				try {
					tjsonObject.put("submittedcount", submittedcount);
					tjsonObject.put("deliverCount", deliverCount);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				}
				public void getTesyncDbData(JSONArray tesyncarray,String requested_date,String companyname) {
					
					Smpp_DaoImpl smpp_dao=new Smpp_DaoImpl();
					 if(requested_date.equalsIgnoreCase(java.time.LocalDate.now().toString())){
						 smpp_dao.getTesyncvalue(tesyncarray);
						 
					 }else {
						 boolean checkmongodata=false;
						 MongoClient mongo = DbConnectionMongo.getInstance().getConnection();
						 DB db = mongo.getDB("count_sms");
					
						 DBCollection collection = db.getCollection("tesync_table");
						 DBCursor cursor1 = collection.find(new BasicDBObject().append("requested_date", requested_date).append("client", "/ "+companyname+"/"));
						//logger.info("fffffffffffffffffnnnnnnnnnnnn");
						try {
							
						
					       while(cursor1.hasNext()) {
					    		
					    	   checkmongodata=true;
					         //  //logger.info(cursor1.next());
					    	   String data=cursor1.next().toString();
					    	   ////logger.info(data);
					          JSONObject jsonObject1;
							try {
								jsonObject1 = new JSONObject(data);
						         JSONObject jsonObject=new JSONObject();
					         		jsonObject.put("client", jsonObject1.getString("client"));
									jsonObject.put("submited", jsonObject1.getString("submited"));
									jsonObject.put("delivered", jsonObject1.getString("delivered"));
									jsonObject.put("delivered_in_per", jsonObject1.getString("delivered_in_per"));
									jsonObject.put("undelivered", jsonObject1.getString("undelivered"));
									jsonObject.put("undelivered_in_per", jsonObject1.getString("undelivered_in_per"));
									jsonObject.put("error_1", jsonObject1.getString("error_1"));
									jsonObject.put("error_6", jsonObject1.getString("error_6"));
									jsonObject.put("error_21", jsonObject1.getString("error_21"));
									jsonObject.put("error_32", jsonObject1.getString("error_32"));
									jsonObject.put("error_34", jsonObject1.getString("error_34"));
									jsonObject.put("error_69", jsonObject1.getString("error_69"));
									jsonObject.put("error_253", jsonObject1.getString("error_253"));
									jsonObject.put("error_254", jsonObject1.getString("error_254"));
									jsonObject.put("requested_date", jsonObject1.getString("requested_date"));
									tesyncarray.put(jsonObject);
						        
							} catch (JSONException e) {
								e.printStackTrace();
							}
					         
					       }
					       if(checkmongodata==false) {
					    	   Connection conn=DbConnection_Smpp.getInstance().getConnection();
								 
								 
								 Statement stmt=null;
								ResultSet rs = null;
								try {
									stmt=conn.createStatement();
									String query = "select * from tesync_table where requested_date like '"+requested_date+"%' and client like '"+companyname+"%'";
						         	rs = stmt.executeQuery(query);
						         	 while (rs.next()) {
						         		JSONObject jsonObject=new JSONObject();
						         		jsonObject.put("client", rs.getString("client"));
										jsonObject.put("submited", rs.getString("submited"));
										jsonObject.put("delivered", rs.getString("delivered"));
										jsonObject.put("delivered_in_per", rs.getString("delivered_in_per"));
										jsonObject.put("undelivered", rs.getString("undelivered"));
										jsonObject.put("undelivered_in_per", rs.getString("undelivered_in_per"));
										jsonObject.put("error_1", rs.getString("error_1"));
										jsonObject.put("error_6", rs.getString("error_6"));
										jsonObject.put("error_21", rs.getString("error_21"));
										jsonObject.put("error_32", rs.getString("error_32"));
										jsonObject.put("error_34", rs.getString("error_34"));
										jsonObject.put("error_69", rs.getString("error_69"));
										jsonObject.put("error_253", rs.getString("error_253"));
										jsonObject.put("error_254", rs.getString("error_254"));
										jsonObject.put("requested_date", rs.getString("requested_date"));
										tesyncarray.put(jsonObject);
						         		 
						         	 }
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					       }
						} catch (Exception e) {
							// TODO: handle exception
						}finally {try {
							if(cursor1!=null)
								cursor1.close();

								// mongo.close();

								} catch (Exception e2) {
								// TODO: handle exception
								}
								
								try {
								if(mongo!=null)
								mongo.close();

								// mongo.close();

								} catch (Exception e2) {
								// TODO: handle exception
								}
}
					      
					 }
				}




				public void all_submitted_totalcount(String req_date, JSONObject jsonObject) throws JSONException {

				   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
				   	Statement stmt = null;
				   
				   	ResultSet rs = null;
				    int submittotalcount=0;
				   	try {
				        
				       stmt=connection.createStatement();
				      
						/*
						 * String query =
						 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
						 * +req_date+"'  group by companyid;;";
						 */
				       String query = "select sum(s.count) as s_count  from tbl_submitted s  left join server_d ser on ser.server_id=s.gateway  where  s.date  like '"+req_date+"' and s.errorcode=0 ;";
				       rs = stmt.executeQuery(query);
				      
				       //logger.info("submitted query"+query);
				       
				        if(rs.isBeforeFirst()){
				       	while (rs.next()) {
				       		
				       		submittotalcount=rs.getInt("s_count");
				       		
				       		}
				       	
				           }else{
				           
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
					

				   	}
				   
				   
					jsonObject.put("submittotalcount", submittotalcount+"");
				}




				public void all_delivered_totalcount(String req_date, JSONObject jsonObject) throws JSONException {
				   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	int deliveredtotalcount=0;
				   	try {
				        
				       stmt=connection.createStatement();
				      
						/*
						 * String query =
						 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
						 * +req_date+"'  group by companyid;;";
						 */
				       String query = "select sum(d.count) as d_count from tbl_delivered d where d.date  like '"+req_date+"' and d.errorcode=0 ;";
				       rs = stmt.executeQuery(query);
				       Gson gson = new Gson();
				       //logger.info("delivered query"+query);
				        if(rs.isBeforeFirst()){
				       	while (rs.next()) {
				       		
				       		deliveredtotalcount=rs.getInt("d_count");
				       
				       	
				    }}
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
					

				   	}
				   jsonObject.put("deliveredtotalcount", deliveredtotalcount+"");
				   }
				public void getUsernameAndId(JSONArray jsonArray) {
				   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	try {
				        
				       stmt=connection.createStatement();
				      
				       String query = "SELECT userdetails.companyid,userdetails.username FROM userdetails GROUP BY username;";
				       rs = stmt.executeQuery(query);
				      
				       //logger.info("delivered query"+query);
				       
				       	while (rs.next()) {
				       JSONObject jsonObject=new JSONObject();
				       jsonObject.put("username", rs.getString("username"));
				       jsonObject.put("companyid", rs.getString("companyid"));
				       jsonArray.put(jsonObject);
				       		
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
					

				   	}
				   
				   }
				
				public void getReportDataByDate(String dates, JSONObject jsonObject2, String companyid) {
					Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
					String ds=dates.replaceAll("-","_");
					//String tables="backup_data_"+ds;
					String tables="alldetails_0919";
					//boolean checktable=daoImpl.checkTable(tables);
					boolean checktable=true;
					if(checktable==true) {
						
						try {
							String sql="select substr(IndianDatetime,1,10) ,count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator like 'BSNL' group by substr(IndianDatetime,1,10);";
							JSONArray onpusherarray=new JSONArray();
							String onpusher=daoImpl.getReportCount(sql);
							jsonObject2.put("onpusher", onpusher);
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator like 'BSNL' and Status like 'DELIVRD' group by substr(IndianDatetime,1,10);";
							String ondelivercount=daoImpl.getReportCount(sql);
							jsonObject2.put("ondelivercount", ondelivercount);
							if(onpusher.equalsIgnoreCase("0") || ondelivercount.equalsIgnoreCase("0")) {
								jsonObject2.put("ondeliverratio", "0");
							}else {
								long ondeliverratio=Long.parseLong(ondelivercount)*100/Long.parseLong(onpusher);
								jsonObject2.put("ondeliverratio", ondeliverratio+"");
							}
							
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator like 'BSNL' and Status like '%subscriber%' group by substr(IndianDatetime,1,10);";
							String onabsentcount=daoImpl.getReportCount(sql);
							jsonObject2.put("onabsentcount", onabsentcount);
							
							if(onpusher.equalsIgnoreCase("0") || onabsentcount.equalsIgnoreCase("0")) {
								jsonObject2.put("onabsentratio", "0");
							}else {
								long onabsentratio=Long.parseLong(onabsentcount)*100/Long.parseLong(onpusher);
								jsonObject2.put("onabsentratio", onabsentratio+"");
							}
							
							
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator like 'BSNL' and Status like '%facility%' group by substr(IndianDatetime,1,10);";
							String onfaccount=daoImpl.getReportCount(sql);
							jsonObject2.put("onfaccount", onfaccount);
							if(onpusher.equalsIgnoreCase("0") || onfaccount.equalsIgnoreCase("0")) {
								jsonObject2.put("onfacratio", "0");
							}else {
								long onfacratio=Long.parseLong(onfaccount)*100/Long.parseLong(onpusher);
								jsonObject2.put("onfacratio", onfacratio+"");
							}
							
							
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator like 'BSNL' and Status like '%FSM%' group by substr(IndianDatetime,1,10);";
							String onfsmcount=daoImpl.getReportCount(sql);
							jsonObject2.put("onfsmcount", onfsmcount);
							if(onpusher.equalsIgnoreCase("0") || onfsmcount.equalsIgnoreCase("0")) {
								jsonObject2.put("onfsmratio", "0");
							}else {
								long onfsmratio=Long.parseLong(onfsmcount)*100/Long.parseLong(onpusher);
								jsonObject2.put("onfsmratio", onfsmratio+"");
							}
							
							
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator like 'BSNL' and Status like '%SRI%' group by substr(IndianDatetime,1,10);";
							String onsricount=daoImpl.getReportCount(sql);
							jsonObject2.put("onsricount", onsricount);
							
							if(onpusher.equalsIgnoreCase("0") || onsricount.equalsIgnoreCase("0")) {
								jsonObject2.put("onsriratio", "0");
							}else {
								long onsriratio=Long.parseLong(onsricount)*100/Long.parseLong(onpusher);
								jsonObject2.put("onsriratio", onsriratio+"");
							}
							
							
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator like 'BSNL' and Status like 'UNDELIV' group by substr(IndianDatetime,1,10);";
							String onundelivercount=daoImpl.getReportCount(sql);
							jsonObject2.put("onundelivercount", onundelivercount);
							if(onpusher.equalsIgnoreCase("0") || onundelivercount.equalsIgnoreCase("0")) {
								jsonObject2.put("onundeliverratio", "0");
							}else {
								long onundeliverratio=Long.parseLong(onundelivercount)*100/Long.parseLong(onpusher);
								jsonObject2.put("onundeliverratio", onundeliverratio+"");
							}
							
							
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator like 'BSNL' and Status like '%failure%' group by substr(IndianDatetime,1,10);";
							String onsystemfailcount=daoImpl.getReportCount(sql);
							jsonObject2.put("onsystemfailcount", onsystemfailcount);
							if(onpusher.equalsIgnoreCase("0") || onsystemfailcount.equalsIgnoreCase("0")) {
								jsonObject2.put("onsystemfailratio", "0");
							}else {
								long onsystemfailratio=Long.parseLong(onsystemfailcount)*100/Long.parseLong(onpusher);
								jsonObject2.put("onsystemfailratio", onsystemfailratio+"");
							}
							
							
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"'  and operator not like 'BSNL' group by substr(IndianDatetime,1,10);";
							String offpusher=daoImpl.getReportCount(sql);
							jsonObject2.put("offpusher", offpusher);
							
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator not like 'BSNL' and Status like 'DELIVRD' group by substr(IndianDatetime,1,10);";
							String offdelivercount=daoImpl.getReportCount(sql);
							jsonObject2.put("offdelivercount", offdelivercount);
							
							if(offpusher.equalsIgnoreCase("0") || offdelivercount.equalsIgnoreCase("0")) {
								jsonObject2.put("offdeliverratio", "0");
							}else {
								long offdeliverratio=Long.parseLong(offdelivercount)*100/Long.parseLong(offpusher);
								jsonObject2.put("offdeliverratio", offdeliverratio+"");
							}
							
							
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator not like 'BSNL' and Status like '%subscriber%' group by substr(IndianDatetime,1,10);";
							String offabsentcount=daoImpl.getReportCount(sql);
							jsonObject2.put("offabsentcount", offabsentcount);
							if(offpusher.equalsIgnoreCase("0") || offabsentcount.equalsIgnoreCase("0")) {
								jsonObject2.put("offabsentratio", "0");
							}else {
								long offabsentratio=Long.parseLong(offabsentcount)*100/Long.parseLong(offpusher);
								jsonObject2.put("offabsentratio", offabsentratio+"");
							}
							
							
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator not like 'BSNL' and Status like '%facility%' group by substr(IndianDatetime,1,10);";
							String offfaccount=daoImpl.getReportCount(sql);
							jsonObject2.put("offfaccount", offfaccount);
							if(offpusher.equalsIgnoreCase("0") || offfaccount.equalsIgnoreCase("0")) {
								jsonObject2.put("offfacratio", "0");
							}else {
								long offfacratio=Long.parseLong(offfaccount)*100/Long.parseLong(onpusher);
								jsonObject2.put("offfacratio", offfacratio+"");
							}
							
							
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator not like 'BSNL' and Status like '%FSM%' group by substr(IndianDatetime,1,10);";
							String offfsmcount=daoImpl.getReportCount(sql);
							jsonObject2.put("offfsmcount", offfsmcount);
							if(offpusher.equalsIgnoreCase("0") || offfsmcount.equalsIgnoreCase("0")) {
								jsonObject2.put("offfsmratio", "0");
							}else {
								long offfsmratio=Long.parseLong(offfsmcount)*100/Long.parseLong(onpusher);
								jsonObject2.put("offfsmratio", offfsmratio+"");
							}
							
							
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator not like 'BSNL' and Status like '%SRI%' group by substr(IndianDatetime,1,10);";
							String offsricount=daoImpl.getReportCount(sql);
							jsonObject2.put("offsricount", offsricount);
							if(offpusher.equalsIgnoreCase("0") || offsricount.equalsIgnoreCase("0")) {
								jsonObject2.put("offsriratio", "0");
							}else {
								long offsriratio=Long.parseLong(offsricount)*100/Long.parseLong(onpusher);
								jsonObject2.put("offsriratio", offsriratio+"");
							}
							
							
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator not like 'BSNL' and Status like 'UNDELIV' group by substr(IndianDatetime,1,10);";
							String offundelivercount=daoImpl.getReportCount(sql);
							jsonObject2.put("offundelivercount", offundelivercount);
							if(offpusher.equalsIgnoreCase("0") || offundelivercount.equalsIgnoreCase("0")) {
								jsonObject2.put("offundeliverratio", "0");
							}else {
								long offundeliverratio=Long.parseLong(offundelivercount)*100/Long.parseLong(onpusher);
								jsonObject2.put("offundeliverratio", offundeliverratio+"");
							}
							
							
							sql="select substr(IndianDatetime,1,10),count(*) as count from "+tables+" where AccountId like '"+companyid+"' and substr(IndianDatetime,1,10)='"+dates+"' and operator not like 'BSNL' and Status like '%failure%' group by substr(IndianDatetime,1,10);";
							String offsystemfailcount=daoImpl.getReportCount(sql);
							jsonObject2.put("offsystemfailcount", offsystemfailcount);
							if(offpusher.equalsIgnoreCase("0") || offsystemfailcount.equalsIgnoreCase("0")) {
								jsonObject2.put("offsystemfailratio", "0");
							}else {
								long offsystemfailratio=Long.parseLong(offsystemfailcount)*100/Long.parseLong(onpusher);
								jsonObject2.put("offsystemfailratio", offsystemfailratio+"");
							}
							
							
							
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}else{
						try {
							jsonObject2.put("onpusher", "0");
							jsonObject2.put("ondelivercount", "0");
							jsonObject2.put("ondeliverratio", "0");
							jsonObject2.put("onabsentcount", "0");
							jsonObject2.put("onabsentratio", "0");
							jsonObject2.put("onfaccount", "0");
							jsonObject2.put("onfacratio", "0");
							jsonObject2.put("onfsmcount", "0");
							jsonObject2.put("onfsmratio", "0");
							jsonObject2.put("onsricount", "0");
							jsonObject2.put("onsriratio", "0");
							jsonObject2.put("onundelivercount", "0");
							jsonObject2.put("onundeliverratio", "0");
							jsonObject2.put("onsystemfailcount", "0");
							jsonObject2.put("onsystemfailratio", "0");
							
							jsonObject2.put("offpusher", "0");
							jsonObject2.put("offdelivercount", "0");
							jsonObject2.put("offdeliverratio", "0");
							jsonObject2.put("offabsentcount", "0");
							jsonObject2.put("offabsentratio", "0");
							jsonObject2.put("offfaccount", "0");
							jsonObject2.put("offfacratio", "0");
							jsonObject2.put("offfsmcount", "0");
							jsonObject2.put("offfsmratio", "0");
							jsonObject2.put("offsricount", "0");
							jsonObject2.put("offsriratio", "0");
							jsonObject2.put("offundelivercount", "0");
							jsonObject2.put("offundeliverratio", "0");
							jsonObject2.put("offsystemfailcount", "0");
							jsonObject2.put("offsystemfailratio", "0");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
				public String getReportCount(String sql) {
				   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	String count="0";
				   	boolean check=false;
				   	
				   				   	try {
				        
				       stmt=connection.createStatement();
				      
				       String query = sql;
				       //logger.info("getReportCount"+query);
				       rs = stmt.executeQuery(query);
				      
				       ////logger.info("delivered query"+query);
				       
				       	while (rs.next()) {
				       	count=rs.getString("count");
				       	check=true;
				       }
				       	if(check==false) {
				       		count="0";
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
					

				   	}
					return count;
				   
				   }
				public boolean checkTable(String tables) {
				   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	boolean checktable=false;
				   	String db="LRN_SEPT";
				   	try {
				        
				       stmt=connection.createStatement();
				      
				       String query = "SELECT table_name AS `Table`, round(((data_length + index_length) / 1024 / 1024), 2) `Size in MB` FROM information_schema.TABLES  WHERE table_schema = '"+db+"' AND table_name = '"+tables+"';";
				       rs = stmt.executeQuery(query);
				      
				       ////logger.info("delivered query"+query);
				       
				       	while (rs.next()) {
				       checktable=true;
				       		
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
					

				   	}
					return checktable;
				   
				   }
				public int UpdateSmppUser(SmppUser smppUser) {
					

				       int status1 = 0;
						
						Connection connection=DbConnection_Smpp.getInstance().getConnection();
						PreparedStatement  ps1 = null;
						Statement stmt1 = null;
						ResultSet rs1 = null;
					     try{
					    	 stmt1 = connection.createStatement();
					    		 ps1=connection.prepareStatement("update username_tb set name=? where id=? ");
					    		
					        
					         ps1.setString(1, smppUser.getName());
					         ps1.setInt(2, smppUser.getId());
					         
					         status1 = ps1.executeUpdate();
					         if(status1>0) {
					        	 //logger.info("Updated Smpp User");
					         }else {
					        	 //logger.info("Not Update Smpp User");
					         }
					         if(status1>0) {
					        	 //logger.info("status1status1"+status1);
					         }else {
					        	 //logger.info("status1status1"+status1);
					         }
					         
	        
					    }
					     catch(Exception e){
					         //logger.info(e);
					     }finally{
					    	 
					    	
					 		try {
						   	       if (connection != null)
						   	    	connection.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (ps1 != null)
						   	    	ps1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (stmt1 != null)
						   	    	stmt1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (rs1 != null)
						   	    	rs1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		
						}
						return status1;
						
					
				}
				public int AddSmppUser(SmppUser smppUser) {
					

				       int status1 = 0;
						
						Connection connection=DbConnection_Smpp.getInstance().getConnection();
						PreparedStatement  ps1 = null;
						Statement stmt1 = null;
						ResultSet rs1 = null;
					     try{
					    	 stmt1 = connection.createStatement();
					    		 ps1=connection.prepareStatement("insert into username_tb (name,password) values(?,?)");
					    		
					        
					         ps1.setString(1, smppUser.getName());
					         ps1.setString(2,smppUser.getPassword()); 
					         
					         status1 = ps1.executeUpdate();
					         if(status1>0) {
					        	 //logger.info("Added Smpp User");
					         }else {
					        	 //logger.info("Not Add Smpp User");
					         }
					         if(status1>0) {
					        	 //logger.info("status1status1"+status1);
					         }else {
					        	 //logger.info("status1status1"+status1);
					         }
					         
	        
					    }
					     catch(Exception e){
					         //logger.info(e);
					     }finally{
					    	 
					    	
					 		try {
						   	       if (connection != null)
						   	    	connection.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (ps1 != null)
						   	    	ps1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (stmt1 != null)
						   	    	stmt1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (rs1 != null)
						   	    	rs1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		
						}
						return status1;
						
					
				}
				public int DeleteSmppUser(String userid) {
					int id=Integer.parseInt(userid);
					int status=0;
				     Connection connection=DbConnection_Smpp.getInstance().getConnection();
				     PreparedStatement  ps1=null;
				     try{
				    	 //logger.info("enter in try");
				    	 ps1=connection.prepareStatement("DELETE FROM username_tb WHERE id=? ");
				         ps1.setInt(1,id);
				           
				         status = ps1.executeUpdate();
				         if(status>0){
				        	 //logger.info("Deleted Smpp User");
				         }else{
				        	 //logger.info("Not Add Smpp User");
				         }
				        
						 
				    }
				     catch(Exception e){
				         //logger.info(e);
				     }finally{
				 		try {
					   	       if (connection != null)
					   	    	connection.close();
					   	    if(ps1 != null)
					   	    	   ps1.close();
				 		} catch (SQLException ignore) {} // no point handling

					}
					return status;
				}
				public void getPanelClient(JSONArray jsonArray) {

				   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;

				   	try {
				        
				       stmt=connection.createStatement();
				      
					
				       String query = "SELECT count(id) as count ,  companyname FROM userdetails group by companyname;";
				       rs = stmt.executeQuery(query);
				       Gson gson = new Gson();
				       //logger.info("delivered query"+query);
				        if(rs.isBeforeFirst()){
				       	while (rs.next()) {
				       		
				       	JSONObject jobj = new JSONObject();
				       	jobj.put("companyname", rs.getString("companyname"));
				     	jobj.put("count", rs.getString("count"));
				        jsonArray.put(jobj);
				       	
				    }
				           }else{
				           
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
					

				   	}
				   
				   
				}
				public void getPanelClientByName(JSONArray jsonArray,String companyname) {

				   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;

				   	try {
				        
				       stmt=connection.createStatement();
				      
					
				       String query = "SELECT * FROM userdetails where companyname='"+companyname+"'";
				       rs = stmt.executeQuery(query);
				       Gson gson = new Gson();
				       //logger.info("delivered query"+query);
				        if(rs.isBeforeFirst()){
				       	while (rs.next()) {
				       		
				       		SmppUserDetails response_p = new SmppUserDetails();
				       	response_p.setId(rs.getString("id"));
				       	response_p.setCompanyid(rs.getString("companyid"));
				       	response_p.setParentid(rs.getString("parentid"));
				       	response_p.setUsername(rs.getString("username"));
				       	response_p.setCompanyname(rs.getString("companyname"));
				       	response_p.setColor(rs.getString("color"));
				       	response_p.setTeam(rs.getString("team"));
				       	response_p.setUserid(rs.getString("userid"));
				       	response_p.setAdminid(rs.getString("userid"));
				       	JSONObject jobj = new JSONObject(gson.toJson(response_p));
				        jsonArray.put(jobj);
				       	
				    }
				           }else{
				           
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
					

				   	}
				   
				   
				}
				public void getPanelError(JSONArray jsonArray) {

				   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;

				   	try {
				        
				       stmt=connection.createStatement();
				      
					
				       String query = "SELECT * FROM errorcode;";
				       rs = stmt.executeQuery(query);
				       Gson gson = new Gson();
				       //logger.info("delivered query"+query);
				        if(rs.isBeforeFirst()){
				       	while (rs.next()) {
				       		
				       	JSONObject jobj = new JSONObject();
				       	jobj.put("Description", rs.getString("Description"));
				     	jobj.put("Error_Code", rs.getString("Error_Code"));
				        jsonArray.put(jobj);
				       	
				    }
				           }else{
				           
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
					

				   	}
				   
				   
				}
				public void getPanelCountUser(JSONObject jsonObject1,String req_date,String userid) {
					long submittedcount=0;
					long deliverCount=0;
					 
				         try {
							 Connection connection=DbConnection_Smpp.getInstance().getConnection();
				     	   	Statement stmt = null;
				     	   	Statement stmt1 = null;
				     	   	ResultSet rs = null;
				     	   	ResultSet rs1 = null;
				     	   	try {
				     	        
				     	       stmt=connection.createStatement();
				     	       stmt1=connection.createStatement();
				     			/*
				     			 * String query = 
				     			 * "select sum(s.count) as s_count,u.companyid, u.parentid, u.username, u.companyname   from tbl_submitted s  join userdetails u on u.companyid = s.companyid  where  s.date='"
				     			 * +req_date+"'  group by companyid;;";
				     			 */
				     	       String query = "select sum(s.count) as s_count,u.companyid, ser.servername, u.parentid, u.username, u.companyname,u.color,s.date   from tbl_submitted s  join userdetails u on u.companyid = s.companyid left join server_d ser on ser.server_id=s.gateway  where  s.date like'"+req_date+"%' and u.userid="+userid+" and s.errorcode=0  group by companyid order by team,companyname;";
				     	       
				     	       //logger.info("=========query=========="+query);
				     	       rs = stmt.executeQuery(query);
				     	       Gson gson = new Gson();

				     	        if(rs.isBeforeFirst()){
				     	       	while (rs.next()) {
				     	       		
				     	       	 submittedcount=submittedcount+rs.getInt("s_count");
				     					
				     	       	query = "select sum(d.count) as d_count from tbl_delivered d where d.date='"+rs.getString("date")+"' and d.errorcode=0 and d.companyid = "+rs.getInt("companyid")+";"; 
				     	       	
				     	       	//logger.info("=========query1=========="+query);
				     	       	rs1  = stmt1.executeQuery(query);
				     	        if(rs1.isBeforeFirst()){
				     		       	while (rs1.next()) {
				     		       	deliverCount =deliverCount+rs1.getInt("d_count");
				     		       	}
				     	        }
				     	       	
				     	       	
				     	      
				     	       	
				     	    }
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
				         
				         jsonObject1.put("submittedcount", submittedcount);
							jsonObject1.put("deliverCount", deliverCount);
				         } catch (Exception e) {
								// TODO: handle exception
							}
					
				}
				public void getOnnetData(JSONArray jsonArray, String tablename) {

				   	Connection connection=DbConnection_on_off.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;

				   	try {
				        
				       stmt=connection.createStatement();
				      
					
				       String query = "select date,account_name,sum(count) as count from "+tablename+" where network like 'Onnet' group by date,account_name;";
				       rs = stmt.executeQuery(query);
				        if(rs.isBeforeFirst()){
				       	while (rs.next()) {
				       		
				       	JSONObject jobj = new JSONObject();
				       	jobj.put("date", rs.getString("date"));
				     	jobj.put("account_name", rs.getString("account_name"));
				    	jobj.put("count", rs.getString("count"));
				        jsonArray.put(jobj);
				       	
				    }
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
					

				   	}
				   
				   
				}
				public void getOffnetData(JSONArray jsonArray, String tablename) {

				   	Connection connection=DbConnection_on_off.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;

				   	try {
				        
				       stmt=connection.createStatement();
				      
					
				       String query = "select date,account_name,sum(count) as count from "+tablename+" where network like 'Offnet'  group by date,account_name ;";
				       rs = stmt.executeQuery(query);
				        if(rs.isBeforeFirst()){
				       	while (rs.next()) {
				       		
				       	JSONObject jobj = new JSONObject();
				       	jobj.put("date", rs.getString("date"));
				     	jobj.put("account_name", rs.getString("account_name"));
				    	jobj.put("count", rs.getString("count"));
				        jsonArray.put(jobj);
				       	
				    }
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
					

				   	}
				   
				   
				}
				public void getOnOffnetByOpertortData(JSONArray jsonArray, String tablename, String date) {

				   	Connection connection=DbConnection_on_off.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;

				   	try {
				        
				       stmt=connection.createStatement();
				      
					
				       String query = "select sum(count) as count,operator from "+tablename+"  where date like '"+date+"' group by operator";
				       rs = stmt.executeQuery(query);
				        if(rs.isBeforeFirst()){
				       	while (rs.next()) {
				       		
				       	JSONObject jobj = new JSONObject();
				     	jobj.put("operator", rs.getString("operator"));
				    	jobj.put("count", rs.getString("count"));
				        jsonArray.put(jobj);
				       	
				    }
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
					

				   	}
				   
				   
				}
				public void InsertUpdateTesyncTableMongo(String client, String submited, String delivered,
						String delivered_in_per, String undelivered, String undelivered_in_per, String error_1,
						String error_6, String error_21, String error_32, String error_34, String error_69,
						String error_253, String error_254, String requested_date) {
					
					 MongoClient mongo = DbConnectionMongo.getInstance().getConnection();
					 DB db = mongo.getDB("count_sms");
				      DBCollection collection = db.getCollection("tesync_table");
					  try {
						 
					
					      BasicDBObject document = new BasicDBObject();
					      document.put("client", client);
					      document.put("submited", submited);
					      document.put("delivered", delivered);
					      document.put("delivered_in_per", delivered_in_per);
					      document.put("undelivered", undelivered);
					      document.put("undelivered_in_per", undelivered_in_per);
					      document.put("error_1", error_1);
					      document.put("error_6", error_6);
					      document.put("error_21", error_21);
					      document.put("error_32", error_32);
					      document.put("error_34", error_34);
					      document.put("error_69", error_69);
					      document.put("error_253", error_253);
					      document.put("error_254", error_254);
					      document.put("requested_date", requested_date);
					    
					    
					     // collection.insert(document);
					      collection.update(new BasicDBObject().append("client", client).append("requested_date", requested_date), document, true, false  );

					} catch (Exception e) {
						// TODO: handle exception
					}finally {
							
							try {
							if(mongo!=null)
							mongo.close();

							// mongo.close();

							} catch (Exception e2) {
							// TODO: handle exception
							}}
				      
					
				}
				public void getIpConnection(JSONArray jsonArray) {
				   	Connection connection=DbConnection_Smpp.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	try {
				        
				       stmt=connection.createStatement();
				      
				       String query = "Select * from esmedetail;";
				       rs = stmt.executeQuery(query);
				      
				       //logger.info("delivered query"+query);
				       
				       	while (rs.next()) {
				       JSONObject jsonObject=new JSONObject();
				       jsonObject.put("id", rs.getString("id"));
				       jsonObject.put("EsmeDetailId", rs.getString("EsmeDetailId"));
				       jsonObject.put("AccountDetailId", rs.getString("AccountDetailId"));
				       jsonObject.put("SmppUsername", rs.getString("SmppUsername"));
				       jsonObject.put("SmppPassword", rs.getString("SmppPassword"));
				       jsonObject.put("IpAddress", rs.getString("IpAddress"));
				       jsonObject.put("TransmitterCount", rs.getString("TransmitterCount"));
				       jsonObject.put("ReceiverCount", rs.getString("ReceiverCount"));
				       jsonObject.put("GatewayId", rs.getString("GatewayId"));
				       jsonArray.put(jsonObject);
				       		
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
					

				   	}
					
				   
				   }

				public static List<String> readFileInList(String fileName) 
				  { 
				  
				    List<String> lines = Collections.emptyList(); 
				    try
				    { 
				      lines = 
				       Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8); 
				    } 
				  
				    catch (IOException e) 
				    { 
				  
				      // do something 
				      e.printStackTrace(); 
				    } 
				    return lines; 
				  } 
				public void getSconnIpTab24OctData(JSONArray jsonArray,String filepath) {
					String s = null;
					Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
				
					List l = daoImpl.readFileInList(filepath); 
					 Iterator<String> itr = l.iterator(); 
					 while (itr.hasNext()) {
				    	 String txtline=itr.next();
				     	 boolean isFound = txtline.contains("-A PREROUTING -s");
				     	 if(isFound==true) {
				     		// System.out.print(txtline.substring(txtline.indexOf("-s")+3 , txtline.indexOf("/32")));
				     	    // System.out.print(" "+txtline.substring(txtline.indexOf("--dport")+8 , txtline.indexOf("-j")));
				     	    // System.out.print(" "+txtline.substring(txtline.indexOf("--to-ports")+11 ));
				     	    String ipaddress=txtline.substring(txtline.indexOf("-s")+3 , txtline.indexOf("/32"));
				     	       
				     	    String dport=txtline.substring(txtline.indexOf("--dport")+8 , txtline.indexOf("--dport")+12);
				     	    String toPort=txtline.substring(txtline.indexOf("--to-ports")+11 );
				     	   String comment="";
				     	    int checkcomment=txtline.indexOf("--comment");
				     	    if(checkcomment>0){
				     	    	  comment=txtline.substring(txtline.indexOf("--comment")+10, txtline.indexOf("-j")-1);
				     	    }
				     	  
				     	       //  out.println("ipaddress"+ipaddress+" dport"+dport+" toPort"+toPort);
				     		 //logger.info(" "+txtline); 
				     		try {
				     		
				     			
				     				  String[] cmd = {"/bin/sh", "-c", "netstat -tlnap |grep "+ipaddress+" |grep ESTABLISHED |grep :8585 |wc -l"};
				     		            Process p = Runtime.getRuntime().exec(cmd);
				     		            
				     		            BufferedReader stdInput = new BufferedReader(new 
				     		                 InputStreamReader(p.getInputStream()));

				     		            BufferedReader stdError = new BufferedReader(new 
				     		                 InputStreamReader(p.getErrorStream()));

				     		            // read the output from the command
				     		            
				     		            //logger.info("Here is the standard output of the command:\n");
				     		           
				     		            while ((s = stdInput.readLine()) != null) {
				     		               if(s.equalsIgnoreCase("0")){ 
				     		            	JSONObject jsonObject=new JSONObject();
				     		            	jsonObject.put("ipaddress", ipaddress);
				     		            	jsonObject.put("dport", dport);
				     		            	jsonObject.put("toPort", toPort);
				     		            	jsonObject.put("comment", comment);
				     		            	jsonObject.put("scon", s);
				     		            	jsonArray.put(jsonObject);
				     		              
				     					}else {
				     						JSONObject jsonObject=new JSONObject();
				     		            	jsonObject.put("ipaddress", ipaddress);
				     		            	jsonObject.put("dport", dport);
				     		            	jsonObject.put("toPort", toPort);
				     		            	jsonObject.put("comment", comment);
				     		            	jsonObject.put("scon", s);
				     		            	jsonArray.put(jsonObject);
				     					
				     					}
				     		            }
				     		       
				     			
				     			
				     		} catch (Exception e) {
				     			// TODO Auto-generated catch block
				     			e.printStackTrace();
				     		}
				     	 }
				    	 
				     }
					
				}
				public void createIpTab24Oct() {
					try {
				          String [] commands = { "bash", "-c", "iptables-save > /home/iptab24oct.txt" };
				                Runtime.getRuntime().exec(commands);
				          // Process p = Runtime.getRuntime().exec("iptables-save > /home/iptab24oct.txt");
				                try {
									Thread.sleep(3000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				         
				}
				       catch (IOException e) {
				           //logger.info("exception happened - here's what I know: ");
				           e.printStackTrace();
				           //System.exit(-1);
				       
				  }
					
				}
				public void insertJsonData(String gatewayName, String reportdate, int totalCount, String percentage,
						int messageCount) {
					////logger.info("1111111111111111111");
					Connection conn=DbConnection_Smpp.getInstance().getConnection();
				
					ResultSet rs = null;
					PreparedStatement  ps =  null;
					
					try 
					{
						
						
						//logger.info("22222222222222");
						  ps=conn.prepareStatement("insert into gateway(gatewayName,reportdate,totalCount,percentage,messageCount) values (?,?,?,?,?);");
						  ps.setString(1,gatewayName );
						  ps.setString(2, reportdate);
						  ps.setInt(3, totalCount);
						  ps.setString(4, percentage);
						  ps.setInt(5, messageCount);
						  //logger.info("333333333333333");
						 int i= ps.executeUpdate();
						 //logger.info("444444444444");
						 //logger.info("iiiiiiiiiiiiii"+i);
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					finally
			        {
			       	 try {
			       	         if (conn != null)
			       	      	conn.close();
			       	      } catch (SQLException ignore) {} // no point handling

			       	    

			       	  
			       	   try {
			       	         if (rs != null)
			       	        	 rs.close();
			       	      } catch (SQLException ignore) {} // no point handling
			       	  
			       	   
			       	   try {
			       	         if (ps != null)
			       	        	 ps.close();
			       	      } catch (SQLException ignore) {} // no point handling
			       	   
			       	 }
			  	  
					
				}
				public  String getNextDate(String  curDate) {
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
				public  String getPreDate(String  curDate) {
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
				public  String getYear() {
					 Date date=new Date();  
					 int year=date.getYear(); 
					 
					 String currentYear=year+1900+"";
					  
					return currentYear;
					
				}
				public void getTraficData(JSONArray jsonArray) {
				   	Connection connection=DbConnection_Trafic.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="SELECT  s.account,(SELECT f.traffic FROM traffic f Where s.account=f.account ORDER BY datetime DESC LIMIT 1) as last, (SELECT t.traffic FROM traffic t Where s.account=t.account ORDER BY datetime DESC LIMIT 1,1) 2nd_last,s.server FROM traffic s GROUP BY account";
				       rs = stmt.executeQuery(query);
				      
				       //logger.info("delivered query"+query);
				       
				       	while (rs.next()) {
				       JSONObject jsonObject=new JSONObject();
				       jsonObject.put("server", rs.getString(4));
				       jsonObject.put("MobileNumber", rs.getString(1));
				       jsonObject.put("SenderId", rs.getString(2));
				       jsonObject.put("Message", rs.getString(3));
				       int diff = Integer.parseInt(rs.getString(2)) - Integer.parseInt(rs.getString(3));
				       jsonObject.put("difference", diff);
				       jsonObject.put("differencedergree", diff/360);
				       jsonArray.put(jsonObject);
				       		
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
					

				   	}
					
				   
				   }
				public String getResetSearchId(String acc_name) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	String acc_id="";
				   	try {
				        
				       stmt=connection.createStatement();
				      // System.out.println("qqqqqqqqqq11111= select companyid from report.userdetails where username like '" + acc_name + "'");
				       String query="select companyid from report.userdetails where username like '" + acc_name + "'";
		               
				       rs = stmt.executeQuery(query);
				      
				       //logger.info("delivered query"+query);
				       
				       	while (rs.next()) {
				       	 acc_id = rs.getString(1);
				       		
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
					

				   	}
					return acc_id;
					
				   
				   }
				public void getSearchSmppData(JSONArray jsonArray, String acc_name, String date, String searchdata,
						String search_keyword, String acc_id, String date_data) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
				       if (searchdata == null || searchdata.equals("")) {
		                   
		                    int last = search_keyword.lastIndexOf(",");
		                    System.out.println("lastlastlastlastlastlastlastlast"+last);
		                    if(last!=-1)
		                    search_keyword = search_keyword.substring(0, last - 1);
		                    System.out.println("search_keywordsearch_keywordsearch_keywordsearch_keyword"+search_keyword);
		                    query = "SELECT SQL_CALC_FOUND_ROWS * FROM (SELECT s.MobileNumber,s.SenderId,s.Message,(case WHEN i.Status is null THEN s.Status ELSE i.Status end) as sStatus,(case WHEN i.ErrorCode is null THEN s.ErrorCode ELSE i.ErrorCode end) as eErrorCode,(case WHEN i.MessageId is null THEN s.MessageId ELSE i.MessageId end) as MessageId,s.AliasMessageId, DATE_ADD(s.SubmitDate, INTERVAL 5.30 HOUR_MINUTE ) as SubmitDate,(case WHEN DATE_ADD(i.DoneDate, INTERVAL 5.30 HOUR_MINUTE ) is null THEN DATE_ADD(s.DoneDate, INTERVAL 5.30 HOUR_MINUTE ) ELSE DATE_ADD(i.DoneDate, INTERVAL 5.30 HOUR_MINUTE ) end) as DoneDate,s.GatewayId as GatewayId FROM sentbox partition(p" + date_data + ") s left join inbounddlr partition(p" + date_data + ") i on s.messageid=i.messageid WHERE accountid = " + acc_id + " and s.InterfaceType not in (0) and (s.MobileNumber IN (" + search_keyword + ")  ) and 1=1 order by s.id desc limit 0,98) s";
		                    System.out.println(query);
		                } else {
		                    query = "SELECT SQL_CALC_FOUND_ROWS * FROM (SELECT s.MobileNumber,s.SenderId,s.Message, (case WHEN i.Status is null THEN s.Status ELSE i.Status end) as sStatus,(case WHEN i.ErrorCode is null THEN s.ErrorCode ELSE i.ErrorCode end) as eErrorCode,(case WHEN i.MessageId is null THEN s.MessageId ELSE i.MessageId end) as MessageId,s.AliasMessageId, DATE_ADD(s.SubmitDate, INTERVAL 5.30 HOUR_MINUTE ) as SubmitDate,(case WHEN DATE_ADD(i.DoneDate, INTERVAL 5.30 HOUR_MINUTE ) is null THEN DATE_ADD(s.DoneDate, INTERVAL 5.30 HOUR_MINUTE ) ELSE DATE_ADD(i.DoneDate, INTERVAL 5.30 HOUR_MINUTE ) end) as DoneDate,s.GatewayId as GatewayId FROM sentbox partition(p" + date_data + ") s left join inbounddlr partition(p" + date_data + ") i on s.messageid=i.messageid WHERE accountid = " + acc_id + " and s.InterfaceType not in (0) and (s.MobileNumber LIKE '%" + search_keyword + "%' OR s.SenderId LIKE '%" + search_keyword + "%' OR s.Message LIKE '%" + search_keyword + "%' OR Ifnull(i.Status, s.Status) LIKE '%" + search_keyword + "%' ) and 1=1 order by s.id desc limit 0,98) s";
		                    System.out.println(query);

		                }
		               
				       rs = stmt.executeQuery(query);
				      
				       //logger.info("delivered query"+query);
				       
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("MobileNumber", rs.getString(1));
		                    jsonObject.put("SenderId", rs.getString(2));
		                    jsonObject.put("Message", rs.getString(3));
		                    jsonObject.put("Status", rs.getString(4));
		                    jsonObject.put("ErrorCode", rs.getString(5));
		                    jsonObject.put("MessageId", rs.getString(6));
		                    jsonObject.put("AliasMessageId", rs.getString(7));
		                    jsonObject.put("SubmitDate", rs.getString(8));
		                    jsonObject.put("DoneDate", rs.getString(9));
		                    jsonObject.put("GatewayId", rs.getString(10));
		                    jsonArray.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public boolean dropTable(String table) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	boolean truncatestatus=false;
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="drop table "+table+";";
		               System.out.println(query);
				        stmt.executeUpdate(query);
				      
				       //logger.info("delivered query"+query);
				       
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
					

				   	}
					return truncatestatus;
					
				   
				   }
				public boolean TruncateTable() {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	boolean truncatestatus=false;
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="truncate table but_14_sep";
		               
				        stmt.executeQuery(query);
				      
				       //logger.info("delivered query"+query);
				       
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
					

				   	}
					return truncatestatus;
					
				   
				   }
				public boolean LoadDateFile() {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	boolean load_data_status=false;
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="load data local infile '/root/temp.txt' into table but_14_sep(msgid);";
		               
				       rs = stmt.executeQuery(query);
				      
				       //logger.info("delivered query"+query);
				       
				       	while (rs.next()) {
				       		load_data_status=true;
				       		
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
					

				   	}
					return load_data_status;
					
				   
				   }
				public int UpdateButTAble(String newdate) {
						int status1 = 0;
						Connection connection=DbConnection_Search.getInstance().getConnection();
						PreparedStatement  ps1 = null;
						Statement stmt1 = null;
						ResultSet rs1 = null;
					     try{
					    	 stmt1 = connection.createStatement();
					    	 System.out.println("update but_14_sep,itextwebv2smpp.inbounddlr  partition("+newdate+") set but_14_sep.GatewayId=itextwebv2smpp.inbounddlr.GatewayId,but_14_sep.Status=itextwebv2smpp.inbounddlr.Status,but_14_sep.ErrorCode=itextwebv2smpp.inbounddlr.ErrorCode,but_14_sep.SubmitDate=DATE_ADD(itextwebv2smpp.inbounddlr.SubmitDate, INTERVAL 5.30 HOUR_MINUTE ),but_14_sep.DoneDate=DATE_ADD(itextwebv2smpp.inbounddlr.DoneDate, INTERVAL 5.30 HOUR_MINUTE ) where but_14_sep.msgid=itextwebv2smpp.inbounddlr.AliasMessageId;");
					    		 ps1=connection.prepareStatement("update but_14_sep,itextwebv2smpp.inbounddlr  partition("+newdate+") set but_14_sep.GatewayId=itextwebv2smpp.inbounddlr.GatewayId,but_14_sep.Status=itextwebv2smpp.inbounddlr.Status,but_14_sep.ErrorCode=itextwebv2smpp.inbounddlr.ErrorCode,but_14_sep.SubmitDate=DATE_ADD(itextwebv2smpp.inbounddlr.SubmitDate, INTERVAL 5.30 HOUR_MINUTE ),but_14_sep.DoneDate=DATE_ADD(itextwebv2smpp.inbounddlr.DoneDate, INTERVAL 5.30 HOUR_MINUTE ) where but_14_sep.msgid=itextwebv2smpp.inbounddlr.AliasMessageId;");
					    		
					    		
					         status1 = ps1.executeUpdate();
					        
					         
	        
					    }
					     catch(Exception e){
					         //logger.info(e);
					     }finally{
					    	 
					    	
					 		try {
						   	       if (connection != null)
						   	    	connection.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (ps1 != null)
						   	    	ps1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (stmt1 != null)
						   	    	stmt1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		try {
						   	       if (rs1 != null)
						   	    	rs1.close();
						   	    } catch (SQLException ignore) {} // no point handling
					 		
						}
						return status1;
						
					
				}
				public boolean uploadFile(String filename) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	boolean upload_file_status=false;
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select *  into outfile '/tmp/"+filename+"' fields terminated by ',' from but_14_sep ;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		upload_file_status=true;
				       		
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
					

				   	}
					return upload_file_status;
					
				   
				   }
				public static void downloadUsingStream(String urlStr, String file) {
			       
					try {
						
						 
						URL	url = new URL(urlStr);
						 BufferedInputStream bis = new BufferedInputStream(url.openStream());
					        FileOutputStream fis = new FileOutputStream(file);
					        byte[] buffer = new byte[1024];
					        int count=0;
					        while((count = bis.read(buffer,0,1024)) != -1)
					        {
					            fis.write(buffer, 0, count);
					        }
					        fis.close();
					        bis.close();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			       
			    }
				public int updateStatus(String status) {
					int status1 = 0;
					Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
					PreparedStatement  ps1 = null;
					Statement stmt1 = null;
					ResultSet rs1 = null;
				     try{
				    	 stmt1 = connection.createStatement();
				    	 System.out.println("");
				    		 ps1=connection.prepareStatement("update downloadstatus set status=?");
				    		 ps1.setString(1, status);
				    		
				         status1 = ps1.executeUpdate();
				        
				         
        
				    }
				     catch(Exception e){
				         //logger.info(e);
				     }finally{
				    	 
				    	
				 		try {
					   	       if (connection != null)
					   	    	connection.close();
					   	    } catch (SQLException ignore) {} // no point handling
				 		try {
					   	       if (ps1 != null)
					   	    	ps1.close();
					   	    } catch (SQLException ignore) {} // no point handling
				 		try {
					   	       if (stmt1 != null)
					   	    	stmt1.close();
					   	    } catch (SQLException ignore) {} // no point handling
				 		try {
					   	       if (rs1 != null)
					   	    	rs1.close();
					   	    } catch (SQLException ignore) {} // no point handling
				 		
					}
					return status1;
					
				
			}
				public int insertFileName(String filename) {
					int status1 = 0;
					Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
					PreparedStatement  ps1 = null;
					Statement stmt1 = null;
					ResultSet rs1 = null;
				     try{
				    	 stmt1 = connection.createStatement();
				    	 System.out.println("");
				    		 ps1=connection.prepareStatement("insert into downloadcsv (filename,status) values (?,?)");
				    		 ps1.setString(1, filename);
				    		 ps1.setString(2, "true");
				    		
				         status1 = ps1.executeUpdate();
				        
				         
        
				    }
				     catch(Exception e){
				         //logger.info(e);
				     }finally{
				    	 
				    	
				 		try {
					   	       if (connection != null)
					   	    	connection.close();
					   	    } catch (SQLException ignore) {} // no point handling
				 		try {
					   	       if (ps1 != null)
					   	    	ps1.close();
					   	    } catch (SQLException ignore) {} // no point handling
				 		try {
					   	       if (stmt1 != null)
					   	    	stmt1.close();
					   	    } catch (SQLException ignore) {} // no point handling
				 		try {
					   	       if (rs1 != null)
					   	    	rs1.close();
					   	    } catch (SQLException ignore) {} // no point handling
				 		
					}
					return status1;
					
				
			}
				public void getReportData( JSONArray jsonArray,String clientid,String requestdate) {
				   	Connection connection=DbConnection_ReportData.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="";
				       if(clientid.equalsIgnoreCase("0")) {
				    	   query="select AccountId, DeliveredCount, SubmittedCount, TotalCount, ReportDate from reportdata where ReportDate like '"+requestdate+"%' GROUP BY AccountId order by id desc;"; 
				       }else {
				        query="select AccountId, DeliveredCount, SubmittedCount, TotalCount, ReportDate from reportdata where AccountId="+clientid+" and ReportDate like '"+requestdate+"%' GROUP BY AccountId order by id desc;";
				   	}
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("AccountId", rs.getString("AccountId")+"");
				       		jsonObject.put("DeliveredCount", rs.getString("DeliveredCount")+"");
				       		jsonObject.put("SubmittedCount", rs.getString("SubmittedCount")+"");
				       		jsonObject.put("TotalCount", rs.getString("TotalCount")+"");
				       		jsonObject.put("ReportDate", rs.getString("ReportDate")+"");
				       		jsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getEsmeReportData( JSONArray jsonArray,String clientid,String requestdate) {
			   	Connection connection=DbConnection_ReportData.getInstance().getConnection();
			   	Statement stmt = null;
			   	ResultSet rs = null;
			  
			   	try {
			        
			       stmt=connection.createStatement();
			       String query="";
			       if(clientid.equalsIgnoreCase("0")) {
				    	query="select DeliveredCount,SubmittedCount,TotalCount,ReportDate,SiteUserId as EsmeAccountId from esmereportdata where ReportDate like '"+requestdate+"%' GROUP BY SiteUserId order by id desc;";
			       }else {
			    	query="select DeliveredCount,SubmittedCount,TotalCount,ReportDate,SiteUserId as EsmeAccountId from esmereportdata where SiteUserId="+clientid+"  and ReportDate like '"+requestdate+"%' GROUP BY SiteUserId order by id desc;";
			       }
			       System.out.println("query="+query);
			       rs = stmt.executeQuery(query);
			      
			       	while (rs.next()) {
			       		JSONObject jsonObject=new JSONObject();
			       		jsonObject.put("DeliveredCount", rs.getString("DeliveredCount")+"");
			       		jsonObject.put("SubmittedCount", rs.getString("SubmittedCount")+"");
			       		jsonObject.put("TotalCount", rs.getString("TotalCount")+"");
			       		jsonObject.put("ReportDate", rs.getString("ReportDate")+"");
			       		jsonObject.put("EsmeAccountId", rs.getString("EsmeAccountId")+"");
			       		jsonArray.put(jsonObject);
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
				

			   	}
				
			   
			   }
				public void getAllBulkFileName( JSONArray jsonArray) {
				   	Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select *  from downloadcsv order by id desc limit 6 ;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("id", rs.getString("id"));
				       		jsonObject.put("filename", rs.getString("filename"));
				       		jsonObject.put("status", rs.getString("status"));
				       		jsonObject.put("reg_date", rs.getString("reg_date"));
				       		jsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getapr20DataWithAccountName( JSONArray jsonArray, String errorcode, String reqdate, String tablename) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(1);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select account_name,sum(count) as count from "+tablename+" where  ErrorCode like '"+errorcode+"' and date like '"+reqdate+"%' group by account_name;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("account_name", rs.getString("account_name"));
				       		jsonObject.put("count", rs.getString("count"));
				       		jsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getapr20DataWithSenderId( JSONArray jsonArray, String errorcode, String reqdate, String tablename,String account_name) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(1);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select senderid,sum(count) as count,account_name from "+tablename+" where  ErrorCode like '"+errorcode+"' and account_name like '%"+account_name+"%' and date like '"+reqdate+"%' group by senderid;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("senderid", rs.getString("senderid"));
				       		jsonObject.put("count", rs.getString("count"));
				       		jsonObject.put("account_name", rs.getString("account_name"));
				       		jsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getBulkFileName(String filename, JSONObject jsonObject) {
				   	Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select *  from downloadcsv where filename='"+filename+"';";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		jsonObject.put("id", rs.getString("id"));
				       		jsonObject.put("filename", rs.getString("filename"));
				       		jsonObject.put("status", rs.getString("status"));
				       		jsonObject.put("reg_date", rs.getString("reg_date"));
				       		
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
					

				   	}
					
				   
				   }
				public void getBulkFileStatus( JSONObject jsonObject) {
				   	Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select *  from downloadstatus ;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		jsonObject.put("id", rs.getString("id"));
				       		jsonObject.put("status", rs.getString("status"));
				       		jsonObject.put("reg_date", rs.getString("reg_date"));
				       		
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
					

				   	}
					
				   
				   }
				public void getTpcDatabyPanel(JSONArray jsonArray, String date, String userid, String count, String datetype) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(4);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="";
				       if(datetype.equalsIgnoreCase("Latest")) {
				    	   query="SELECT mid(date_add(ServerRequestReceivedDate, interval 5.30 hour_minute),1,16) as pushtime,count(*) as msgcount, report.iddetails.username from sentbox partition("+date+"),report.iddetails where report.iddetails.companyid=sentbox.CompanyId and report.iddetails.userid like '"+userid+"' group by pushtime ,report.iddetails.username  order by sentbox.Id desc limit "+count+"; ";  
				       }else {
				    	   query="SELECT mid(date_add(ServerRequestReceivedDate, interval 5.30 hour_minute),1,19) as pushtime ,count(*) as msgcount, report.iddetails.username from sentbox partition("+date+"),report.iddetails where report.iddetails.companyid=sentbox.CompanyId and report.iddetails.userid like '"+userid+"' group by EXTRACT(HOUR_SECOND FROM ServerRequestReceivedDate),report.iddetails.username  order by count(*) desc limit "+count+";";
				       }
				       System.out.println("query = "+query);
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("pushtime", rs.getString("pushtime"));
				       		jsonObject.put("msgcount", rs.getString("msgcount"));
				       		jsonObject.put("username", rs.getString("username"));
				       		jsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getPAnelCountData1(JSONArray jsonArray, String requestdate) {
				   	Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select iddetails.username,iddetails.userid,sum(count) as count from iddetails,tbl_submitted where iddetails.companyid=tbl_submitted.companyid and tbl_submitted.date like '"+requestdate+"%' group by iddetails.username;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("userid", rs.getInt("userid"));
				       		jsonObject.put("count", rs.getString("count"));
				       		jsonObject.put("username", rs.getString("username"));
				       		jsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getPAnelCountData2(JSONArray jsonArray, String requestdate) {
				   	Connection connection=DbConnection_SmppSpanel.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select iddetails.username,iddetails.userid,sum(count) as count from iddetails,tbl_delivered where tbl_delivered.errorcode like '0' and iddetails.companyid=tbl_delivered.companyid and tbl_delivered.date like '"+requestdate+"%' group by iddetails.username;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("userid", rs.getInt("userid"));
				       		jsonObject.put("count", rs.getString("count"));
				       		jsonObject.put("username", rs.getString("username"));
				       		jsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getLrnLiveData(JSONArray jsonArray, String errorcode, String accountId, String tablename, String senderid) {

				   	Connection connection=DbConnection_All.getInstance().getConnection(5);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select SenderId,LRN,operator,Circle,count(*) as count from "+tablename+" where AccountId like '"+accountId+"' and ErrorCode like '"+errorcode+"' and SenderId like '"+senderid+"%' group by SenderId,LRN,operator,Circle;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		if(rs.getString("SenderId")!=null) {
				       			jsonObject.put("senderid", rs.getString("SenderId"));
				       		}else {
				       			jsonObject.put("senderid", "");
				       		}
				       		if(rs.getString("LRN")!=null) {
				       			jsonObject.put("lrn", rs.getString("LRN"));
				       		}else {
				       			jsonObject.put("lrn", "0");
				       		}
				       		if(rs.getString("operator")!=null) {
				       			jsonObject.put("operator", rs.getString("operator"));
				       		}else {
				       			jsonObject.put("operator", "");
				       		}
				       		if(rs.getString("Circle")!=null) {
				       			jsonObject.put("circle", rs.getString("Circle"));
				       		}else {
				       			jsonObject.put("circle", "");
				       		}
				       		if(rs.getString("count")!=null) {
				       			jsonObject.put("count", rs.getString("count"));
				       		}else {
				       			jsonObject.put("count", "0");
				       		}
				       		
				       		jsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   
				}
				public void getGateWayAnalysis1(JSONArray jsonArray, String date, String type) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
				       if(type.equals("1")) {
				    	   query = "select gatewayname,count(AliasMessageId) as tot,count(distinct AliasMessageId) as dist from sentbox partition ("+date+"),report.giddetails where report.giddetails.serverid like '1' and sentbox.gatewayid=report.giddetails.gatewayid group by gatewayname;";
						}else if(type.equals("2")) {
							query = "select gatewayname,count(AliasMessageId) as tot,count(distinct AliasMessageId) as dist from sentbox partition ("+date+"),report.giddetails where report.giddetails.serverid like '2' and sentbox.gatewayid=report.giddetails.gatewayid group by gatewayname;";
						}else if(type.equals("3")) {
							query = "select gatewayname,count(AliasMessageId) as tot,count(distinct AliasMessageId) as dist from sentbox partition ("+date+"),report.giddetails where report.giddetails.serverid like '3' and sentbox.gatewayid=report.giddetails.gatewayid group by gatewayname;";
						}
	                    
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("gatewayname", rs.getString("gatewayname"));
		                    jsonObject.put("tot", rs.getString("tot"));
		                    jsonObject.put("dist", rs.getString("dist"));
		                    jsonArray.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public void getFailureAnalysis(JSONArray jsonArray, String sql) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	try {
				        
				       stmt=connection.createStatement();
		               rs = stmt.executeQuery(sql);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("date", rs.getString("date"));
		                    jsonObject.put("username", rs.getString("username"));
		                    jsonObject.put("sub", rs.getString("tot_sub"));
		                    jsonObject.put("count", rs.getString("err"));
		                    jsonArray.put(jsonObject);
				       		
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
					

				   	}
					}
				public void getFailureAnalysis_InbondDlr(JSONArray jsonArray, String sql) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	try {
				        
				       stmt=connection.createStatement();
		               rs = stmt.executeQuery(sql);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("date", rs.getString("date"));
		                    jsonObject.put("username", rs.getString("username"));
		                    jsonObject.put("gatewayname", rs.getString("gatewayname"));
		                    jsonObject.put("count", rs.getString("err"));
		                    jsonObject.put("sub", rs.getString("sub"));
		                    jsonArray.put(jsonObject);
				       		
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
					

				   	}
				}
				public void getFailureAnalysis_sentBoxSub(JSONArray jsonArray, String sql) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	try {
				        
				       stmt=connection.createStatement();
		               rs = stmt.executeQuery(sql);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("date", rs.getString("date"));
		                    jsonObject.put("username", rs.getString("username"));
		                    jsonObject.put("gatewayname", rs.getString("gatewayname"));
		                    jsonObject.put("sub", rs.getString("sub"));
		                    jsonArray.put(jsonObject);
				       		
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
					

				   	}
				}
				public void getTrafficAnalysis(JSONArray jsonArray, String sql) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	try {
				        
				       stmt=connection.createStatement();
		               rs = stmt.executeQuery(sql);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("pushtime", rs.getString("pushtime"));
		                    jsonObject.put("msgcount", rs.getString("msgcount"));
		                    jsonArray.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public void getGateWayAnalysis2(JSONArray jsonArray, String date, String type) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
				       if(type.equals("1")) {
				    	   query = "select gatewayname,count(AliasMessageId) as tot,count(distinct AliasMessageId) as dist from inbounddlr partition ("+date+"),report.giddetails where report.giddetails.serverid like '1' and inbounddlr.gatewayid=report.giddetails.gatewayid group by gatewayname;";
						}else if(type.equals("2")) {
							query = "select gatewayname,count(AliasMessageId) as tot,count(distinct AliasMessageId) as dist from inbounddlr partition ("+date+"),report.giddetails where report.giddetails.serverid like '2' and inbounddlr.gatewayid=report.giddetails.gatewayid group by gatewayname;";
						}else if(type.equals("3")) {
							query = "select gatewayname,count(AliasMessageId) as tot,count(distinct AliasMessageId) as dist from inbounddlr partition ("+date+"),report.giddetails where report.giddetails.serverid like '3' and inbounddlr.gatewayid=report.giddetails.gatewayid group by gatewayname;";
						} 
				       rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("gatewayname", rs.getString("gatewayname"));
		                    jsonObject.put("tot", rs.getString("tot"));
		                    jsonObject.put("dist", rs.getString("dist"));
		                    jsonArray.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public void getLastEntry1(JSONArray jsonArray, String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select date_add(submitdate, interval 5.30 hour_minute) as submit,date_add(donedate, interval 5.30 hour_minute) as receive,Status,MobileNumber,AliasMessageId,report.iddetails.username,sentbox.GatewayId from sentbox partition("+date+"),report.iddetails where sentbox.AccountId=report.iddetails.companyid order by submit desc limit 5;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("submit", rs.getString("submit"));
		                    jsonObject.put("receive", rs.getString("receive"));
		                    jsonObject.put("status", rs.getString("Status"));
		                    jsonObject.put("mobileNumber", rs.getString("MobileNumber"));
		                    jsonObject.put("aliasMessageId", rs.getString("AliasMessageId"));
		                    jsonObject.put("username", rs.getString("username"));
		                    jsonObject.put("gatewayId", rs.getString("GatewayId"));
		                    jsonArray.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public void getLastEntry2(JSONArray jsonArray, String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select date_add(submitdate, interval 5.30 hour_minute) as submit,date_add(donedate, interval 5.30 hour_minute) as receive,Status,AliasMessageId,report.iddetails.username,inbounddlr.GatewayId from inbounddlr partition("+date+"),report.iddetails where inbounddlr.SiteUserId=report.iddetails.companyid order by receive desc limit 5;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("submit", rs.getString("submit"));
		                    jsonObject.put("receive", rs.getString("receive"));
		                    jsonObject.put("status", rs.getString("Status"));
		                    jsonObject.put("mobileNumber", "");
		                    jsonObject.put("aliasMessageId", rs.getString("AliasMessageId"));
		                    jsonObject.put("username", rs.getString("username"));
		                    jsonObject.put("gatewayId", rs.getString("GatewayId"));
		                    jsonArray.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public void getSentBoxData(JSONArray jsonArray, String date, String searchdata, String type,
						String acc_id, String searchtype) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
				       if (searchtype.equalsIgnoreCase("mobileNo")) {
		                  query = "select inbounddlr.GatewayId,report.giddetails.gatewayname,report.iddetails.username,sentbox.AccountId,sentbox.MobileNumber,date_add(sentbox.submitdate,interval 5.30 hour_minute) as SentDate,date_add(inbounddlr.donedate,interval 5.30 hour_minute) as RecvDate,sentbox.AliasMessageId,sentbox.MessageId,sentbox.SenderId,inbounddlr.Status,inbounddlr.ErrorCode,sentbox.Message from sentbox partition ("+date+"),inbounddlr partition ("+date+"),report.giddetails,report.iddetails where report.giddetails.gatewayid=inbounddlr.GatewayId and report.iddetails.companyid=sentbox.AccountId and sentbox.MobileNumber ='"+searchdata+"' and report.giddetails.serverid like '"+type+"'  and sentbox.AliasMessageId=inbounddlr.AliasMessageId;";
		                }else if (searchtype.equalsIgnoreCase("aliasId")) {
		                    query = "select inbounddlr.GatewayId,report.giddetails.gatewayname,report.iddetails.username,sentbox.AccountId,sentbox.MobileNumber,date_add(sentbox.submitdate,interval 5.30 hour_minute) as SentDate,date_add(inbounddlr.donedate,interval 5.30 hour_minute) as RecvDate,sentbox.AliasMessageId,sentbox.MessageId,sentbox.SenderId,inbounddlr.Status,inbounddlr.ErrorCode,sentbox.Message from sentbox partition ("+date+"),inbounddlr partition ("+date+"),report.giddetails,report.iddetails where report.giddetails.gatewayid=inbounddlr.GatewayId and report.iddetails.companyid=sentbox.AccountId and  sentbox.AliasMessageId like '"+searchdata+"' and report.giddetails.serverid like '"+type+"'  and sentbox.AliasMessageId=inbounddlr.AliasMessageId;";
		                   // System.out.println("==>"+query);
		                }
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("gatewayId", rs.getLong("GatewayId"));
		                    jsonObject.put("gatewayname", rs.getString("gatewayname"));
		                    jsonObject.put("username", rs.getString("username"));
		                    jsonObject.put("accountId", rs.getString("AccountId"));
		                    jsonObject.put("mobileNumber", rs.getString("MobileNumber"));
		                    jsonObject.put("sentDate", rs.getString("SentDate"));
		                    jsonObject.put("recvDate", rs.getString("RecvDate"));
		                    jsonObject.put("aliasMessageId", rs.getString("AliasMessageId"));
		                    jsonObject.put("messageId", rs.getString("MessageId"));
		                    jsonObject.put("senderId", rs.getString("SenderId"));
		                    jsonObject.put("status", rs.getString("Status"));
		                    jsonObject.put("errorCode", rs.getString("ErrorCode"));
		                    jsonObject.put("message", rs.getString("Message"));
		                    jsonArray.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public void callApiSendBoxBulkStatus(SentBoxBulkFiles boxBulkFiles) {
					//Thread thread = new Thread(){
					  //  public void run(){
					    	String jsonData="";
							try {
								HttpResponse<JsonNode> response = Unirest.post(ApiCons.cwcBaseUrl+"sendbulkstatus")
										.header("Content-Type", "application/x-www-form-urlencoded")
										.field("id", boxBulkFiles.getId())
										.field("status", boxBulkFiles.getStatus())
										.field("process", boxBulkFiles.getProcess())
										.field("status_msg", boxBulkFiles.getStatus_msg())
										.field("get_file", boxBulkFiles.getGet_file())
										.field("run_status", boxBulkFiles.getRun_status())
										.asJson();
								jsonData=response.getBody().toString();
							} catch (UnirestException e) {
								e.printStackTrace();
							}
					      
					//    }
					 // };

					 // thread.start();
					
				}
				
				public int createDummyTable(String table, String id) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	int i=0;
				   	try {
				        
				       stmt=connection.createStatement();
				       String sql = "create table "+table+" (AliasMessageId varchar(36));"; 
				       System.out.println(sql);
				        i=stmt.executeUpdate(sql);
				        if(i>0) {
				        	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
				        	 SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
							 	boxBulkFiles.setId(id);
								boxBulkFiles.setStatus("3");
								boxBulkFiles.setProcess("15%");
								boxBulkFiles.setStatus_msg("Created tables");
								boxBulkFiles.setGet_file("");
								boxBulkFiles.setRun_status("0");
							    daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
				        }
		               
				      
				       //logger.info("delivered query"+query);
				       
				     }catch(Exception e){
				    	 Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
			        	 SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						 	boxBulkFiles.setId(id);
							boxBulkFiles.setStatus("0");
							boxBulkFiles.setProcess("10%");
							boxBulkFiles.setStatus_msg("not create table"+e.getMessage());
							boxBulkFiles.setGet_file("");
							boxBulkFiles.setRun_status("0");
						    daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
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
					

				   	}
					return i;
					
				   
				   }
				public int alterTable(String table, String id) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	int i=0;
				   	try {
				        
				       stmt=connection.createStatement();
				       String sql = "alter table dummy_table add (MobileNumber varchar(12),SenderId varchar(10),MessageId varchar(36),SentbStatus varchar(50),DLRStatus varchar(50),ErrorCode varchar(20),AccountId varchar(10),GatewayId varchar(10),SentDate datetime,RecvDate datetime,TimeDiff varchar(10),Message varchar(100));"; 
				       System.out.println(sql);
				       i=stmt.executeUpdate(sql);
				        sql = "alter table dummy_table add index(MobileNumber);"; 
				       System.out.println(sql);
				       i=stmt.executeUpdate(sql);
				       sql = "alter table dummy_table add index(AliasMessageId);"; 
				       System.out.println(sql);
				       i=stmt.executeUpdate(sql);
				       sql = "alter table dummy_table add index(MessageId);"; 
				       System.out.println(sql);
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table dummy_table add index(DLRStatus);"; 
				       System.out.println(sql);
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table dummy_table add index(ErrorCode);"; 
				       System.out.println(sql);
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table dummy_table add index(SenderId);"; 
				       System.out.println(sql);
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table dummy_table add index(RecvDate);"; 
				       System.out.println(sql);
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table dummy_table add index(GatewayId);"; 
				       System.out.println(sql);
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table dummy_table add index(Message);"; 
				       System.out.println(sql);
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table dummy_table add index(SentbStatus);"; 
				       System.out.println(sql);
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table dummy_table add index(SentDate);"; 
				       System.out.println(sql);
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table dummy_table add index(SentDate);"; 
				       System.out.println(sql);
				       i=stmt.executeUpdate(sql);
				      
				        if(i>0) {
				        	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
				        	 SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
							 	boxBulkFiles.setId(id);
								boxBulkFiles.setStatus("4");
								boxBulkFiles.setProcess("25%");
								boxBulkFiles.setStatus_msg("Alter tables");
								boxBulkFiles.setGet_file("");
								boxBulkFiles.setRun_status("1");
							    daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
				        }else {
				        	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
				        	 SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
							 	boxBulkFiles.setId(id);
								boxBulkFiles.setStatus("0");
								boxBulkFiles.setProcess("15%");
								boxBulkFiles.setStatus_msg("not Alter tables");
								boxBulkFiles.setGet_file("");
								boxBulkFiles.setRun_status("0");
							    daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
				        }
		               
				      
				       //logger.info("delivered query"+query);
				       
				     }catch(Exception e){
				    	 Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
			        	 SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						 	boxBulkFiles.setId(id);
							boxBulkFiles.setStatus("0");
							boxBulkFiles.setProcess("15%");
							boxBulkFiles.setStatus_msg("not Alter tables"+e.getMessage());
							boxBulkFiles.setGet_file("");
							boxBulkFiles.setRun_status("0");
						    daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
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
					

				   	}
					return i;
					
				   
				   }
				public boolean LoadSentBoxDateFile(String table, String id) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	boolean load_data_status=false;
				   	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="load data local infile '/home/dummy_file.txt' into table "+table+" (AliasMessageId);";
				       //String query="load data local infile 'C://Users//Dell//Desktop//VNS War file//CWC//dummy_file.txt' into table "+table+" (AliasMessageId);";
				       System.out.println(query);
				       int i = stmt.executeUpdate(query);
				       if(i>0) {
				    	   SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						 	boxBulkFiles.setId(id);
							boxBulkFiles.setStatus("5");
							boxBulkFiles.setProcess("45%");
							boxBulkFiles.setStatus_msg("Loaded files");
							boxBulkFiles.setGet_file("");
							boxBulkFiles.setRun_status("1");
							daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
				       }else {
				    	   SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						 	boxBulkFiles.setId(id);
							boxBulkFiles.setStatus("0");
							boxBulkFiles.setProcess("25%");
							boxBulkFiles.setStatus_msg("Failed lod file");
							boxBulkFiles.setGet_file("");
							boxBulkFiles.setRun_status("0");
						    daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
				       }
				       //logger.info("delivered query"+query);
				       
				       	
				       
				     }catch(Exception e){
				    	 SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						 	boxBulkFiles.setId(id);
							boxBulkFiles.setStatus("0");
							boxBulkFiles.setProcess("25%");
							boxBulkFiles.setStatus_msg("Failed lod file "+ e.getMessage());
							boxBulkFiles.setGet_file("");
							boxBulkFiles.setRun_status("0");
						    daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
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
					

				   	}
					return load_data_status;
					
				   
				   }
				public int updateSendBoxtable(String table, String id, String currentDate, String preDate,
						String nextDate) {
					Connection conn=DbConnection_Search.getInstance().getConnection();
					Statement stmt=null;
					ResultSet rs = null;
					PreparedStatement  ps =  null;
					Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
					int i=0;
					try 
					{
						stmt=conn.createStatement();
						  ps=conn.prepareStatement("update "+table+",sentbox partition ("+preDate+","+currentDate+","+nextDate+") set "+table+".GatewayId=sentbox.GatewayId,"+table+".MessageId=sentbox.MessageId,"+table+".MobileNumber=sentbox.MobileNumber,"+table+".AccountId=sentbox.AccountId,"+table+".SentDate=sentbox.SubmitDate,"+table+".SentbStatus=sentbox.Status,"+table+".Message=sentbox.Message,"+table+".SenderId=sentbox.SenderId where "+table+".AliasMessageId=sentbox.AliasMessageId;");
						  System.out.println(ps.toString());
						  i= ps.executeUpdate();
						 SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						  if(i>0) {
							  boxBulkFiles.setId(id);
								boxBulkFiles.setStatus("6");
								boxBulkFiles.setProcess("70%");
								boxBulkFiles.setStatus_msg("Updated tables");
								boxBulkFiles.setGet_file("");
								boxBulkFiles.setRun_status("1");
								daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
						  }else {
							
							 	boxBulkFiles.setId(id);
								boxBulkFiles.setStatus("0");
								boxBulkFiles.setProcess("45%");
								boxBulkFiles.setStatus_msg("failed Update tables");
								boxBulkFiles.setGet_file("");
								boxBulkFiles.setRun_status("0");
								daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
						  }
						 	
					}
					catch(Exception e)
					{
						 SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						 	boxBulkFiles.setId(id);
							boxBulkFiles.setStatus("0");
							boxBulkFiles.setProcess("45%");
							boxBulkFiles.setStatus_msg("failed Update tables "+e.getMessage());
							boxBulkFiles.setGet_file("");
							boxBulkFiles.setRun_status("0");
							daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
							e.printStackTrace();
					}
					finally
			        {
			       	 try {
			       	         if (conn != null)
			       	      	conn.close();
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
			       	         if (ps != null)
			       	        	 ps.close();
			       	      } catch (SQLException ignore) {} // no point handling
			       	   
			       	 }
					return i;
			  	  
					
				}
				public int updateSendBoxtable2(String table, String id, String currentDate, String preDate,
						String nextDate) {
					Connection conn=DbConnection_Search.getInstance().getConnection();
					Statement stmt=null;
					ResultSet rs = null;
					PreparedStatement  ps =  null;
					Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
					int i=0;
					try 
					{
						stmt=conn.createStatement();
						  ps=conn.prepareStatement("update "+table+",inbounddlr partition ("+preDate+","+currentDate+","+nextDate+") set "+table+".RecvDate=inbounddlr.DoneDate,"+table+".DLRStatus=inbounddlr.Status,"+table+".ErrorCode=inbounddlr.ErrorCode where "+table+".AliasMessageId=inbounddlr.AliasMessageId;");
						  System.out.println(ps.toString());
						  i= ps.executeUpdate();
						   SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						  if(i>0) {
							  boxBulkFiles.setId(id);
								boxBulkFiles.setStatus("6");
								boxBulkFiles.setProcess("72%");
								boxBulkFiles.setStatus_msg("Updated tables");
								boxBulkFiles.setGet_file("");
								boxBulkFiles.setRun_status("1");
								daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
						  }else {
							
							 	boxBulkFiles.setId(id);
								boxBulkFiles.setStatus("0");
								boxBulkFiles.setProcess("45%");
								boxBulkFiles.setStatus_msg("failed Update tables");
								boxBulkFiles.setGet_file("");
								boxBulkFiles.setRun_status("0");
								daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
						  }
						 	
					}
					catch(Exception e)
					{
						 SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						 	boxBulkFiles.setId(id);
							boxBulkFiles.setStatus("0");
							boxBulkFiles.setProcess("45%");
							boxBulkFiles.setStatus_msg("failed Update tables "+e.getMessage());
							boxBulkFiles.setGet_file("");
							boxBulkFiles.setRun_status("0");
							daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
							e.printStackTrace();
					}
					finally
			        {
			       	 try {
			       	         if (conn != null)
			       	      	conn.close();
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
			       	         if (ps != null)
			       	        	 ps.close();
			       	      } catch (SQLException ignore) {} // no point handling
			       	   
			       	 }
					return i;
			  	  
					
				}
				public int updateSendBoxtable3(String table, String id, String currentDate, String preDate,
						String nextDate) {
					Connection conn=DbConnection_Search.getInstance().getConnection();
					Statement stmt=null;
					ResultSet rs = null;
					PreparedStatement  ps =  null;
					Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
					int i=0;
					try 
					{
						stmt=conn.createStatement();
						 ps=conn.prepareStatement("update "+table+" set RecvDate=(date_add(RecvDate, interval 5.30 hour_minute));");
						 System.out.println(ps.toString());
						 i= ps.executeUpdate();
						  SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						  if(i>0) {
							  boxBulkFiles.setId(id);
								boxBulkFiles.setStatus("6");
								boxBulkFiles.setProcess("74%");
								boxBulkFiles.setStatus_msg("Updated tables");
								boxBulkFiles.setGet_file("");
								boxBulkFiles.setRun_status("1");
								daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
						  }else {
							
							 	boxBulkFiles.setId(id);
								boxBulkFiles.setStatus("0");
								boxBulkFiles.setProcess("45%");
								boxBulkFiles.setStatus_msg("failed Update tables");
								boxBulkFiles.setGet_file("");
								boxBulkFiles.setRun_status("0");
								daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
						  }
						 	
					}
					catch(Exception e)
					{
						 SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						 	boxBulkFiles.setId(id);
							boxBulkFiles.setStatus("0");
							boxBulkFiles.setProcess("45%");
							boxBulkFiles.setStatus_msg("failed Update tables "+e.getMessage());
							boxBulkFiles.setGet_file("");
							boxBulkFiles.setRun_status("0");
							daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
							e.printStackTrace();
					}
					finally
			        {
			       	 try {
			       	         if (conn != null)
			       	      	conn.close();
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
			       	         if (ps != null)
			       	        	 ps.close();
			       	      } catch (SQLException ignore) {} // no point handling
			       	   
			       	 }
					return i;
			  	  
					
				}
				public int updateSendBoxtable4(String table, String id, String currentDate, String preDate,
						String nextDate) {
					Connection conn=DbConnection_Search.getInstance().getConnection();
					Statement stmt=null;
					ResultSet rs = null;
					PreparedStatement  ps =  null;
					Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
					int i=0;
					try 
					{
						stmt=conn.createStatement();
						  ps=conn.prepareStatement("update "+table+" set SentDate=(date_add(SentDate, interval 5.30 hour_minute));");
						  System.out.println(ps.toString());
						  i= ps.executeUpdate();
						  SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						  if(i>0) {
							  boxBulkFiles.setId(id);
								boxBulkFiles.setStatus("6");
								boxBulkFiles.setProcess("76%");
								boxBulkFiles.setStatus_msg("Updated tables");
								boxBulkFiles.setGet_file("");
								boxBulkFiles.setRun_status("1");
								daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
						  }else {
							
							 	boxBulkFiles.setId(id);
								boxBulkFiles.setStatus("0");
								boxBulkFiles.setProcess("45%");
								boxBulkFiles.setStatus_msg("failed Update tables");
								boxBulkFiles.setGet_file("");
								boxBulkFiles.setRun_status("0");
								daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
						  }
						 	
					}
					catch(Exception e)
					{
						 SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						 	boxBulkFiles.setId(id);
							boxBulkFiles.setStatus("0");
							boxBulkFiles.setProcess("45%");
							boxBulkFiles.setStatus_msg("failed Update tables "+e.getMessage());
							boxBulkFiles.setGet_file("");
							boxBulkFiles.setRun_status("0");
							daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
							e.printStackTrace();
					}
					finally
			        {
			       	 try {
			       	         if (conn != null)
			       	      	conn.close();
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
			       	         if (ps != null)
			       	        	 ps.close();
			       	      } catch (SQLException ignore) {} // no point handling
			       	   
			       	 }
					return i;
			  	  
					
				}
				public int updateSendBoxtable5(String table, String id, String currentDate, String preDate,
						String nextDate) {
					Connection conn=DbConnection_Search.getInstance().getConnection();
					Statement stmt=null;
					ResultSet rs = null;
					PreparedStatement  ps =  null;
					Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
					int i=0;
					try 
					{
						stmt=conn.createStatement();
						 ps=conn.prepareStatement("update "+table+" set TimeDiff= TIMESTAMPDIFF(minute,dummy_table.SentDate,dummy_table.RecvDate);");
						 System.out.println(ps.toString());
						 i= ps.executeUpdate();
						  SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						  if(i>0) {
							  boxBulkFiles.setId(id);
								boxBulkFiles.setStatus("6");
								boxBulkFiles.setProcess("78%");
								boxBulkFiles.setStatus_msg("Updated tables");
								boxBulkFiles.setGet_file("");
								boxBulkFiles.setRun_status("1");
								daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
						  }else {
							
							 	boxBulkFiles.setId(id);
								boxBulkFiles.setStatus("0");
								boxBulkFiles.setProcess("45%");
								boxBulkFiles.setStatus_msg("failed Update tables");
								boxBulkFiles.setGet_file("");
								boxBulkFiles.setRun_status("0");
								daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
						  }
						 	
					}
					catch(Exception e)
					{
						 SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						 	boxBulkFiles.setId(id);
							boxBulkFiles.setStatus("0");
							boxBulkFiles.setProcess("45%");
							boxBulkFiles.setStatus_msg("failed Update tables "+e.getMessage());
							boxBulkFiles.setGet_file("");
							boxBulkFiles.setRun_status("0");
							daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
							e.printStackTrace();
					}
					finally
			        {
			       	 try {
			       	         if (conn != null)
			       	      	conn.close();
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
			       	         if (ps != null)
			       	        	 ps.close();
			       	      } catch (SQLException ignore) {} // no point handling
			       	   
			       	 }
					return i;
			  	  
					
				}
				public boolean uploadSentBoxFile(String filename, String table, String id) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	boolean upload_file_status=false;
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select *  into outfile '/tmp/"+filename+"' fields terminated by ',' from "+table+" ;";
				       System.out.println(query);
				       rs = stmt.executeQuery(query);
				      // int i = stmt.executeUpdate(query);
				      
				       
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
					

				   	}
					return upload_file_status;
					
				   
				   }
				public void checkingMobileNo(JSONArray jsonArray, String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select report.iddetails.username,count(*) as count from sentbox partition ("+date+"),report.iddetails where sentbox.CompanyId=report.iddetails.CompanyId and MobileNumber not like '91__________' group by report.iddetails.username ;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("username", rs.getString("username"));
		                    jsonObject.put("count", rs.getString("count"));
		                    jsonArray.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public void getSpanFailureAnalysis(JSONArray jsonArray, String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select AccountId,SenderId,count(Message) as cnt, Message from sentbox partition ("+date+") where SenderId like '00%' group by SenderId,Message order by cnt desc limit 10;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("accountId", rs.getString("AccountId"));
		                    jsonObject.put("senderId", rs.getString("SenderId"));
		                    jsonObject.put("cnt", rs.getString("cnt"));
		                    jsonObject.put("message", rs.getString("Message"));
		                    jsonArray.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public int createMisDlranalTable(String id) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	int i=0;
				   	try {
				        
				       stmt=connection.createStatement();
				       String sql = "CREATE TABLE IF NOT EXISTS `chk_bulkmis` (\n" + 
				       		"  `MobileNumber` varchar(12) DEFAULT NULL,\n" + 
				       		"  `SubmitDate` datetime DEFAULT NULL,\n" + 
				       		"  `DoneDate` datetime DEFAULT NULL,\n" + 
				       		"  `AliasMessageId` varchar(36) DEFAULT NULL,\n" + 
				       		"  `SenderId` varchar(10) DEFAULT NULL,\n" + 
				       		"  `MessageId` varchar(36) DEFAULT NULL,\n" + 
				       		"  `status` varchar(50) DEFAULT NULL,\n" + 
				       		"  `CompanyId` int(10) unsigned NOT NULL,\n" + 
				       		"  `GatewayId` int(10) unsigned NOT NULL,\n" + 
				       		"  `ErrorCode` varchar(50) DEFAULT NULL,\n" + 
				       		"  `matched` varchar(10) DEFAULT NULL,\n" + 
				       		"  KEY `MobileNumber` (`MobileNumber`),\n" + 
				       		"  KEY `SubmitDate` (`SubmitDate`),\n" + 
				       		"  KEY `DoneDate` (`DoneDate`),\n" + 
				       		"  KEY `AliasMessageId` (`AliasMessageId`),\n" + 
				       		"  KEY `SenderId` (`SenderId`),\n" + 
				       		"  KEY `status` (`status`),\n" + 
				       		"  KEY `MessageId` (`MessageId`),\n" + 
				       		"  KEY `CompanyId` (`CompanyId`),\n" + 
				       		"  KEY `GatewayId` (`GatewayId`),\n" + 
				       		"  KEY `ErrorCode` (`ErrorCode`),\n" + 
				       		"  KEY `matched` (`matched`)\n" + 
				       		");"; 

				        i=stmt.executeUpdate(sql);
				        if(i>0) {
				        	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
				        	BulkMismatchBeans bulkMismatchBeans=new BulkMismatchBeans();
				        	 	bulkMismatchBeans.setId(id);
								bulkMismatchBeans.setProcess("10%");
								bulkMismatchBeans.setResponse_msg("Created tables");
								bulkMismatchBeans.setFile("");
								bulkMismatchBeans.setRun_status(1);
							    daoImpl.callApiBulkMismatch(bulkMismatchBeans);
				        }
		                
				     }catch(Exception e){
				    	 Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
				    	 BulkMismatchBeans bulkMismatchBeans=new BulkMismatchBeans();
			        	 	bulkMismatchBeans.setId(id);
							bulkMismatchBeans.setProcess("0%");
							bulkMismatchBeans.setResponse_msg("Not Create Table "+e.getMessage());
							bulkMismatchBeans.setFile("");
							bulkMismatchBeans.setRun_status(0);
						    daoImpl.callApiBulkMismatch(bulkMismatchBeans);
						 
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
					

				   	}
					return i;
					
				   
				   }
				public void callApiBulkMismatch(BulkMismatchBeans bulkMismatchBeans) {
					
					    	String jsonData="";
							try {
								HttpResponse<JsonNode> response = Unirest.post(ApiCons.cwcBaseUrl+"get_bulk_mismatch")
										.header("Content-Type", "application/x-www-form-urlencoded")
										.field("id", bulkMismatchBeans.getId())
										.field("process", bulkMismatchBeans.getProcess())
										.field("response_msg", bulkMismatchBeans.getResponse_msg())
										.field("file", bulkMismatchBeans.getFile())
										.field("run_status", bulkMismatchBeans.getRun_status())
										.asJson();
								jsonData=response.getBody().toString();
							} catch (UnirestException e) {
								e.printStackTrace();
							}
					      
					
					
				}
				public int alterBulkMisTable(String id) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	int i=0;
				   	try {
				        
				       stmt=connection.createStatement();
				       String sql = "alter table misdlranal add column matched varchar(10);"; 
				       i=stmt.executeUpdate(sql);
				       sql = "alter table misdlranal add index(MobileNumber);"; 
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table misdlranal add index(SubmitDate);"; 
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table misdlranal add index(DoneDate);"; 
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table misdlranal add index(AliasMessageId);"; 
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table misdlranal add index(SenderId);"; 
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table misdlranal add index(status);"; 
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table misdlranal add index(MessageId);"; 
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table misdlranal add index(CompanyId);"; 
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table misdlranal add index(GatewayId);"; 
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table misdlranal add index(ErrorCode);"; 
				       i=stmt.executeUpdate(sql);
				       
				       sql = "alter table misdlranal add index(matched);"; 
				       i=stmt.executeUpdate(sql);
				        if(i>0) {
				        	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
				        	BulkMismatchBeans bulkMismatchBeans=new BulkMismatchBeans();
			        	 	bulkMismatchBeans.setId(id);
							bulkMismatchBeans.setProcess("15%");
							bulkMismatchBeans.setResponse_msg("Alter tables");
							bulkMismatchBeans.setFile("");
							bulkMismatchBeans.setRun_status(1);
						    daoImpl.callApiBulkMismatch(bulkMismatchBeans);
				        	 
				        }
		               
				      
				       //logger.info("delivered query"+query);
				       
				     }catch(Exception e){
				    	 Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
				    	 BulkMismatchBeans bulkMismatchBeans=new BulkMismatchBeans();
			        	 	bulkMismatchBeans.setId(id);
							bulkMismatchBeans.setProcess("10%");
							bulkMismatchBeans.setResponse_msg("not Alter tables "+e.getMessage());
							bulkMismatchBeans.setFile("");
							bulkMismatchBeans.setRun_status(0);
						    daoImpl.callApiBulkMismatch(bulkMismatchBeans);
			        	 SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
			        	 
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
					

				   	}
					return i;
					
				   
				   }
				public int insertBulkMis(String id, String preDate, String currentDate, String nextDate) {
					Connection conn=DbConnection_Search.getInstance().getConnection();
					Statement stmt=null;
					ResultSet rs = null;
					PreparedStatement  ps =  null;
					Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
					int i=0;
					try 
					{
						stmt=conn.createStatement();
						  ps=conn.prepareStatement("insert into chk_bulkmis(MobileNumber,AliasMessageId,MessageId,SubmitDate,DoneDate,SenderId,CompanyId,GatewayId,Status,ErrorCode) select MobileNumber,AliasMessageId,MessageId,SubmitDate,DoneDate,SenderId,CompanyId,GatewayId,Status,ErrorCode from sentbox partition("+currentDate+") where AliasMessageId not in(select AliasMessageId from inbounddlr partition("+currentDate+")) ;");
						  i= ps.executeUpdate();
						  
						  if(i>0) {
					        	BulkMismatchBeans bulkMismatchBeans=new BulkMismatchBeans();
				        	 	bulkMismatchBeans.setId(id);
								bulkMismatchBeans.setProcess("50%");
								bulkMismatchBeans.setResponse_msg("Inserted data");
								bulkMismatchBeans.setFile("");
								bulkMismatchBeans.setRun_status(1);
							    daoImpl.callApiBulkMismatch(bulkMismatchBeans);
					        	 
					        }else {
						        	BulkMismatchBeans bulkMismatchBeans=new BulkMismatchBeans();
					        	 	bulkMismatchBeans.setId(id);
									bulkMismatchBeans.setProcess("15%");
									bulkMismatchBeans.setResponse_msg("Failed Insert data");
									bulkMismatchBeans.setFile("");
									bulkMismatchBeans.setRun_status(1);
								    daoImpl.callApiBulkMismatch(bulkMismatchBeans);
						        	 
						       
					        }
					}
					catch(Exception e)
					{
						BulkMismatchBeans bulkMismatchBeans=new BulkMismatchBeans();
		        	 	bulkMismatchBeans.setId(id);
						bulkMismatchBeans.setProcess("15%");
						bulkMismatchBeans.setResponse_msg("Failed Insert data" +e.getMessage());
						bulkMismatchBeans.setFile("");
						bulkMismatchBeans.setRun_status(0);
					    daoImpl.callApiBulkMismatch(bulkMismatchBeans);
							e.printStackTrace();
					}
					finally
			        {
			       	 try {
			       	         if (conn != null)
			       	      	conn.close();
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
			       	         if (ps != null)
			       	        	 ps.close();
			       	      } catch (SQLException ignore) {} // no point handling
			       	   
			       	 }
					return i;
			  	  
					
				}
				public int updateBulkMis(String id, String preDate, String currentDate, String nextDate) {
					Connection conn=DbConnection_Search.getInstance().getConnection();
					Statement stmt=null;
					ResultSet rs = null;
					PreparedStatement  ps =  null;
					Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
					int i=0;
					try 
					{
						stmt=conn.createStatement();
						  ps=conn.prepareStatement("update chk_bulkmis,inbounddlr partition("+preDate+","+currentDate+","+nextDate+") set chk_bulkmis.matched='yes' where chk_bulkmis.AliasMessageId=inbounddlr.AliasMessageId;");
						  i= ps.executeUpdate();
						  
						  if(i>0) {
					        	BulkMismatchBeans bulkMismatchBeans=new BulkMismatchBeans();
				        	 	bulkMismatchBeans.setId(id);
								bulkMismatchBeans.setProcess("75%");
								bulkMismatchBeans.setResponse_msg("Updated data");
								bulkMismatchBeans.setFile("");
								bulkMismatchBeans.setRun_status(1);
							    daoImpl.callApiBulkMismatch(bulkMismatchBeans);
					        	 
					        }else {
						        	BulkMismatchBeans bulkMismatchBeans=new BulkMismatchBeans();
					        	 	bulkMismatchBeans.setId(id);
									bulkMismatchBeans.setProcess("50%");
									bulkMismatchBeans.setResponse_msg("Failed Update data");
									bulkMismatchBeans.setFile("");
									bulkMismatchBeans.setRun_status(1);
								    daoImpl.callApiBulkMismatch(bulkMismatchBeans);
						        	 
						       
					        }
					}
					catch(Exception e)
					{
						BulkMismatchBeans bulkMismatchBeans=new BulkMismatchBeans();
		        	 	bulkMismatchBeans.setId(id);
						bulkMismatchBeans.setProcess("50%");
						bulkMismatchBeans.setResponse_msg("Failed Update data" +e.getMessage());
						bulkMismatchBeans.setFile("");
						bulkMismatchBeans.setRun_status(0);
					    daoImpl.callApiBulkMismatch(bulkMismatchBeans);
							e.printStackTrace();
					}
					finally
			        {
			       	 try {
			       	         if (conn != null)
			       	      	conn.close();
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
			       	         if (ps != null)
			       	        	 ps.close();
			       	      } catch (SQLException ignore) {} // no point handling
			       	   
			       	 }
					return i;
			  	  
					
				}
				public boolean uploadBulkMisFile(String filename, String id) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	boolean upload_file_status=false;
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select GatewayId,status,ErrorCode,report.iddetails.username,chk_bulkmis.CompanyId,count(*) as count into outfile '/tmp/"+filename+"' fields terminated by ',' from chk_bulkmis,report.iddetails where chk_bulkmis.matched is null and chk_bulkmis.CompanyId=report.iddetails.CompanyId group by GatewayId,status,ErrorCode,report.iddetails.username,chk_bulkmis.CompanyId ;";
				       rs = stmt.executeQuery(query);
				      // int i = stmt.executeUpdate(query);
				      
				       
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
					

				   	}
					return upload_file_status;
					
				   
				   }
				public void getCheckingCharset(JSONArray jsonArray, String date, String type, String value) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
				       if(type.equals("1")) {
				    	   query = "select report.giddetails.gatewayname,count(Message) as count from sentbox partition ("+date+"),report.giddetails where Message like '%"+value+"%' and sentbox.gatewayid=report.giddetails.gatewayid and report.giddetails.serverid='1' group by report.giddetails.gatewayname;";
						}else if(type.equals("2")) {
					    	   query = "select report.giddetails.gatewayname,count(Message) as count from sentbox partition ("+date+"),report.giddetails where Message like '%"+value+"%' and sentbox.gatewayid=report.giddetails.gatewayid and report.giddetails.serverid='2' group by report.giddetails.gatewayname;";
						}else if(type.equals("3")) {
					    	   query = "select report.giddetails.gatewayname,count(Message) as count from sentbox partition ("+date+"),report.giddetails where Message like '%"+value+"%' and sentbox.gatewayid=report.giddetails.gatewayid and report.giddetails.serverid='3' group by report.giddetails.gatewayname;";
						}
	                    System.out.println("query==>"+query);
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("gatewayname", rs.getString("gatewayname"));
		                    jsonObject.put("count", rs.getString("count"));
		                    jsonArray.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public void getLiveSubReport(JSONArray subJsonArray, String date, String type) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(4);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   	String date1=this.getNextDate(date,1);
					String date2=this.getNextDate(date,2);
					String pdate="p"+date.replace("-", "")+",p"+date1.replace("-", "");
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select report.iddetails.username,report.iddetails.companyid,report.iddetails.companyname,count(*) as count from report.iddetails,sentbox partition ("+pdate+") where report.iddetails.companyid=sentbox.AccountId and date_add(submitdate,interval 5.30 hour_minute) like '"+date+"%' group by report.iddetails.username;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("accountName", rs.getString("username"));
				       		jsonObject.put("count", rs.getString("count"));
				       		jsonObject.put("companyid", rs.getString("companyid"));
				       		jsonObject.put("companyname", rs.getString("companyname"));
				       		jsonObject.put("type",type);
				       		subJsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getLiveDelReport(JSONArray delJsonArray, String date, String type) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(4);
				   	Statement stmt = null;
				   	ResultSet rs = null;
					String date1=this.getNextDate(date,1);
					String date2=this.getNextDate(date,2);
					String pdate="p"+date.replace("-", "")+",p"+date1.replace("-", "");
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select report.iddetails.username,report.iddetails.companyid,report.iddetails.companyname,count(*) as count from report.iddetails,inbounddlr partition ("+pdate+") where report.iddetails.companyid=inbounddlr.SiteuserId  and ErrorCode like '000' and date_add(submitdate,interval 5.30 hour_minute) like '"+date+"%' group by report.iddetails.username;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("accountName", rs.getString("username"));
				       		jsonObject.put("count", rs.getString("count"));
				       		jsonObject.put("companyid", rs.getString("companyid"));
				       		jsonObject.put("companyname", rs.getString("companyname"));
				       		jsonObject.put("type",type);
				       		delJsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getLiveDel12Report(JSONArray dlr12JsonArray, String date, String type) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(4);
				   	Statement stmt = null;
				   	ResultSet rs = null;
					String date1=this.getNextDate(date,1);
					String date2=this.getNextDate(date,2);
					String pdate="p"+date.replace("-", "")+",p"+date1.replace("-", "")+",p"+date2.replace("-", "");
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select report.iddetails.username,report.iddetails.companyid,report.iddetails.companyname,count(*) as count from report.iddetails,inbounddlr partition ("+pdate+") where report.iddetails.companyid=inbounddlr.SiteuserId  and ErrorCode like '000' and date_add(submitdate,interval 5.30 hour_minute) like '"+date+"%' group by report.iddetails.username;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("accountName", rs.getString("username"));
				       		jsonObject.put("count", rs.getString("count"));
				       		jsonObject.put("companyid", rs.getString("companyid"));
				       		jsonObject.put("companyname", rs.getString("companyname"));
				       		jsonObject.put("type",type);
				       		dlr12JsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getSubReport(JSONArray subJsonArray, String fromDate, String toDate, String type) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(2);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select iddetails.username as AccountName,sum(count) as count,iddetails.companyid as companyid from iddetails,tbl_submitted where iddetails.companyid=tbl_submitted.companyid and tbl_submitted.date  between cast('"+fromDate+"' as date) and cast('"+toDate+"' as date) group by iddetails.username;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("accountName", rs.getString("AccountName"));
				       		jsonObject.put("count", rs.getString("count"));
				       		jsonObject.put("companyid", rs.getString("companyid"));
				       		jsonObject.put("type",type);
				       		subJsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				
				public void getDlrReport(JSONArray dlrJsonArray, String fromDate, String toDate, String type) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(2);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select iddetails.username as AccountName,sum(count) as count,iddetails.companyid as companyid from iddetails,tbl_delivered where errorcode like '0' and iddetails.companyid=tbl_delivered.companyid and tbl_delivered.date between cast('"+fromDate+"' as date) and cast('"+toDate+"' as date) group by iddetails.username;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("accountName", rs.getString("AccountName"));
				       		jsonObject.put("count", rs.getString("count"));
				       		jsonObject.put("companyid", rs.getString("companyid"));
				       		jsonObject.put("type",type);
				       		dlrJsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getUnDlrReport(JSONArray dlrJsonArray, String fromDate, String toDate, String type) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(2);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select iddetails.username as AccountName,sum(count) as count,iddetails.companyid as companyid from iddetails,tbl_delivered where errorcode not like '0' and iddetails.companyid=tbl_delivered.companyid and tbl_delivered.date between cast('"+fromDate+"' as date) and cast('"+toDate+"' as date) group by iddetails.username;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("accountName", rs.getString("AccountName"));
				       		jsonObject.put("count", rs.getString("count"));
				       		jsonObject.put("companyid", rs.getString("companyid"));
				       		jsonObject.put("type",type);
				       		dlrJsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getSubDlrReportWithName195(JSONArray jsonArray, String fromDate, String toDate,
						String name, String type) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(6);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="";
				       if(type.equals("195-4")) {
				    	   	query="select sum(total_submitted) as sub,sum(Total_delivered) as del,AccountName from table_for_analysis4 where IndianDatetime between cast('"+fromDate+"' as date) and cast('"+toDate+"' as date) and AccountName = '"+name+"' group by AccountName;";
			           }else if(type.equals("195-5")){
			            	query="select sum(total_submitted) as sub,sum(Total_delivered) as del,AccountName from table_for_analysis5 where IndianDatetime between cast('"+fromDate+"' as date) and cast('"+toDate+"' as date) and AccountName = '"+name+"' group by AccountName;";   
				       }else if(type.equals("195-6")){
			            	query="select sum(total_submitted) as sub,sum(Total_delivered) as del,AccountName from table_for_analysis6 where IndianDatetime between cast('"+fromDate+"' as date) and cast('"+toDate+"' as date) and AccountName = '"+name+"' group by AccountName;";   
				       }
				       System.out.println("query==>"+query);
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("sub", rs.getString("sub"));
				       		jsonObject.put("del", rs.getString("del"));
				       		jsonObject.put("type",type);
				       		jsonObject.put("accountName", rs.getString("AccountName"));
				       		jsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getSubErrorCodeByCompanyIdAndDate(JSONArray subJsonArray, String fromDate, String toDate,
						String companyid) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(2);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select sum(s.count) as count,s.errorcode,e.Description   from tbl_submitted s left join errorcode e on s.errorcode=e.Error_Code  where  s.date  between cast('"+fromDate+"' as date) and cast('"+toDate+"' as date) and s.companyid ="+companyid+" group by s.errorcode;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("count", rs.getString("count"));
				       		jsonObject.put("errorcode", rs.getString("errorcode"));
				       		jsonObject.put("description", rs.getString("Description")+"");
				       		subJsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getDelErrorCodeByCompanyIdAndDate(JSONArray jsonArray, String fromDate, String toDate,
						String companyid) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(2);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select sum(s.count) as count,s.errorcode,e.Description  from tbl_delivered s left join errorcode e on s.errorcode=e.Error_Code where  s.date  between cast('"+fromDate+"' as date) and cast('"+toDate+"' as date) and s.companyid ="+companyid+" group by s.errorcode;";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("count", rs.getString("count"));
				       		jsonObject.put("errorcode", rs.getString("errorcode"));
				       		jsonObject.put("description", rs.getString("Description")+"");
				       		jsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getGatewayDetails(JSONArray jsonArray, String serverid, String type) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(2);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select * from report.giddetails where serverid="+serverid+";";
		               
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("gatewayname", rs.getString("gatewayname"));
				       		jsonObject.put("serverid", rs.getString("serverid"));
				       		jsonObject.put("tps", rs.getString("TPS"));
				       		jsonObject.put("username",rs.getString("username"));
				       		jsonObject.put("ip",rs.getString("IP"));
				       		jsonObject.put("gatewayid",rs.getString("gatewayid"));
				       		jsonObject.put("vendorId",rs.getString("VendorId"));
				       		jsonObject.put("gatewaytype",rs.getString("gatewaytype"));
				       		jsonObject.put("companyname",rs.getString("companyname"));
				       		jsonObject.put("zone",rs.getString("zone"));
				       		jsonObject.put("type",type);
				       		jsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public void getAccountDetails(JSONArray jsonArray, String serverid, String type) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(2);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="select * from report.iddetails where serverid="+serverid+";";
				       if(serverid.equals("4") || serverid.equals("5") || serverid.equals("6") || serverid.equals("7") || serverid.equals("9")) {
				    	   query="select * from report.iddetails_new where serverid="+serverid+";";
				    	   rs = stmt.executeQuery(query);
					       while (rs.next()) {
					       		JSONObject jsonObject=new JSONObject();
					       		jsonObject.put("id", rs.getString("id"));
					       		jsonObject.put("companyid", rs.getString("companyid"));
					       		jsonObject.put("serverid", rs.getString("serverid"));
					       		jsonObject.put("username",rs.getString("username"));
					       		jsonObject.put("companyname",rs.getString("companyname"));
					       		jsonObject.put("team",rs.getString("team"));
					       		jsonObject.put("userid",rs.getString("userid"));
					       		jsonObject.put("type",type);
					       		jsonObject.put("charset", rs.getString("Charset"));
					       		jsonObject.put("dcs",rs.getString("DCS"));
					       		jsonArray.put(jsonObject);
					       }
				       }else {
				    	   query="select * from report.iddetails where serverid="+serverid+";";
				    	   rs = stmt.executeQuery(query);
					       while (rs.next()) {
					       		JSONObject jsonObject=new JSONObject();
					       		jsonObject.put("id", rs.getString("id"));
					       		jsonObject.put("companyid", rs.getString("companyid"));
					       		jsonObject.put("charset", rs.getString("Charset"));
					       		jsonObject.put("serverid", rs.getString("serverid"));
					       		jsonObject.put("username",rs.getString("username"));
					       		jsonObject.put("companyname",rs.getString("companyname"));
					       		jsonObject.put("team",rs.getString("team"));
					       		jsonObject.put("userid",rs.getString("userid"));
					       		jsonObject.put("dcs",rs.getString("DCS"));
					       		jsonObject.put("type",type);
					       		jsonArray.put(jsonObject);
					       }
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
					

				   	}
					
				   
				   }
				public void getDateMismatchAnalysisSubmitDate_submission(String date, String type, JSONArray submitDateJson) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
			    	   query = "select substr(DATE_ADD(SubmitDate, INTERVAL 5.30 HOUR_MINUTE ),1,13) as substr,count(*) as count,GatewayId from sentbox partition ("+date+") group by substr(DATE_ADD(SubmitDate, INTERVAL 5.30 HOUR_MINUTE ),1,13),GatewayId;";

				      
	                    System.out.println("query==>"+query);
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("substr", rs.getString("substr"));
		                    jsonObject.put("count", rs.getString("count"));
		                    jsonObject.put("gatewayId", rs.getString("GatewayId"));
		                    submitDateJson.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public void getDateMismatchAnalysisDoneDate_submission(String date, String type,
						JSONArray doneDateJson) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
			    	   query = "select substr(DATE_ADD(DoneDate, INTERVAL 5.30 HOUR_MINUTE ),1,13) as substr,count(*) as count,GatewayId from sentbox partition ("+date+") group by substr(DATE_ADD(DoneDate, INTERVAL 5.30 HOUR_MINUTE ),1,13),GatewayId;";

				      
	                    System.out.println("query==>"+query);
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("substr", rs.getString("substr"));
		                    jsonObject.put("count", rs.getString("count"));
		                    jsonObject.put("gatewayId", rs.getString("GatewayId"));
		                    doneDateJson.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public void getDateMismatchAnalysisSubmitDate_delivered(String date, String type,
						JSONArray submitDateJson) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
			    	   query = "select substr(DATE_ADD(SubmitDate, INTERVAL 5.30 HOUR_MINUTE ),1,13) as substr,count(*)	as count,GatewayId from inbounddlr partition ("+date+") group by substr(DATE_ADD(SubmitDate, INTERVAL 5.30 HOUR_MINUTE ),1,13),GatewayId;";
			    	   System.out.println("query==>"+query);
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("substr", rs.getString("substr"));
		                    jsonObject.put("count", rs.getString("count"));
		                    jsonObject.put("gatewayId", rs.getString("GatewayId"));
		                    submitDateJson.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public void getDateMismatchAnalysisDoneDate_delivered(String date, String type,
						JSONArray doneDateJson) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
			    	   query = "select substr(DATE_ADD(DoneDate, INTERVAL 5.30 HOUR_MINUTE ),1,13) as substr,count(*) as count,GatewayId from inbounddlr partition ("+date+") group by substr(DATE_ADD(DoneDate, INTERVAL 5.30 HOUR_MINUTE ),1,13),GatewayId;";
			    	   System.out.println("query==>"+query);
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("substr", rs.getString("substr"));
		                    jsonObject.put("count", rs.getString("count"));
		                    jsonObject.put("gatewayId", rs.getString("GatewayId"));
		                    doneDateJson.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public void getTpcDatabyPanelHours(JSONArray jsonArray, String date, String date_p, String companyName,
						String count, String tps_by) {
				   	Connection connection=DbConnection_All.getInstance().getConnection(4);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				  
				   	try {
				        
				       stmt=connection.createStatement();
				       String query="";
				       if(tps_by.equalsIgnoreCase("submitDate")) {
				    	   query="SELECT mid(date_add(SubmitDate, interval 5.30 hour_minute),1,19) as pushtime,count(*) as msgcount,report.iddetails.username  from sentbox partition("+date_p+") ,report.iddetails where sentbox.AccountId=report.iddetails.companyid and report.iddetails.companyname like '%"+companyName+"%' and date_add(SubmitDate,interval 5.30 hour_minute) like '"+date+"%' group by pushtime,report.iddetails.username order by msgcount desc limit "+count+";";  
				       }else if(tps_by.equalsIgnoreCase("server")) {
				    	   query="SELECT mid(date_add(ServerRequestReceivedDate, interval 5.30 hour_minute),1,19) as pushtime,count(*) as msgcount,report.iddetails.username  from sentbox partition("+date_p+") ,report.iddetails where sentbox.AccountId=report.iddetails.companyid and report.iddetails.companyname like '%"+companyName+"%' and date_add(ServerRequestReceivedDate,interval 5.30 hour_minute) like '"+date+"%' group by pushtime,report.iddetails.username order by msgcount desc limit "+count+";";  
				       }else if(tps_by.equalsIgnoreCase("doneDate")) {
				    	   query="SELECT mid(date_add(DoneDate, interval 5.30 hour_minute),1,19) as pushtime,count(*) as msgcount,report.iddetails.username  from sentbox partition("+date_p+") ,report.iddetails where sentbox.AccountId=report.iddetails.companyid and report.iddetails.companyname like '%"+companyName+"%' and date_add(DoneDate,interval 5.30 hour_minute) like '"+date+"%' group by pushtime,report.iddetails.username order by msgcount desc limit "+count+";";  
				       }
				       System.out.println("query = "+query);
				       rs = stmt.executeQuery(query);
				      
				       	while (rs.next()) {
				       		JSONObject jsonObject=new JSONObject();
				       		jsonObject.put("pushtime", rs.getString("pushtime"));
				       		jsonObject.put("msgcount", rs.getString("msgcount"));
				       		jsonObject.put("username", rs.getString("username"));
				       		jsonArray.put(jsonObject);
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
					

				   	}
					
				   
				   }
				public String maxDateSubmitWeb1(String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateSubmit="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(submitdate,interval 5.30 hour_minute)) as maxDateSubmit from sentbox partition ("+date+"),report.iddetails where iddetails.Charset  like 'WEB' and sentbox.AccountId=report.iddetails.companyid;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateSubmit=rs.getString("maxDateSubmit");
				       		
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
					

				   	}
					return maxDateSubmit;
					
				   }
				public String maxDateDoneWeb1(String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateDone="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(donedate,interval 5.30 hour_minute)) as maxDateDone from sentbox partition ("+date+"),report.iddetails where iddetails.Charset  like 'WEB' and sentbox.AccountId=report.iddetails.companyid;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateDone=rs.getString("maxDateDone");
				       		
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
					

				   	}
					return maxDateDone;
					
					
				   
				   }
				public String maxDateSubmitWeb2(String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateSubmit="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(submitdate,interval 5.30 hour_minute)) as maxDateSubmit from inbounddlr partition ("+date+"),report.iddetails where iddetails.Charset like 'WEB' and report.iddetails.companyid=inbounddlr.SiteuserId;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateSubmit=rs.getString("maxDateSubmit");
				       		
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
					

				   	}
					return maxDateSubmit;
					
					
				   
				   }
				public String maxDateDoneWeb2(String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateDone="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(donedate,interval 5.30 hour_minute)) as maxDateDone from inbounddlr partition ("+date+"),report.iddetails where iddetails.Charset like 'WEB' and report.iddetails.companyid=inbounddlr.SiteuserId;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateDone=rs.getString("maxDateDone");
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
					

				   	}
					return maxDateDone;
					
					
				   
				   }
				public String maxDateSubmitNotWeb1(String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateSubmit="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(submitdate,interval 5.30 hour_minute)) as maxDateSubmit from sentbox partition ("+date+"),report.iddetails where iddetails.Charset not like 'WEB' and sentbox.AccountId=report.iddetails.companyid;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateSubmit=rs.getString("maxDateSubmit");
				       		
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
					

				   	}
					return maxDateSubmit;
					
				   }
				public String maxDateDoneNotWeb1(String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateDone="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(donedate,interval 5.30 hour_minute)) as maxDateDone from sentbox partition ("+date+"),report.iddetails where iddetails.Charset not like 'WEB' and sentbox.AccountId=report.iddetails.companyid;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateDone=rs.getString("maxDateDone");
				       		
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
					

				   	}
					return maxDateDone;
					
					
				   
				   }
				public String maxDateSubmitNotWeb2(String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateSubmit="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(submitdate,interval 5.30 hour_minute)) as maxDateSubmit from inbounddlr partition ("+date+"),report.iddetails where iddetails.Charset not like 'WEB' and report.iddetails.companyid=inbounddlr.SiteuserId;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateSubmit=rs.getString("maxDateSubmit");
				       		
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
					

				   	}
					return maxDateSubmit;
					
					
				   
				   }
				public String maxDateDoneNotWeb2(String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateDone="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(donedate,interval 5.30 hour_minute)) as maxDateDone from inbounddlr partition ("+date+"),report.iddetails where iddetails.Charset not like 'WEB' and report.iddetails.companyid=inbounddlr.SiteuserId;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateDone=rs.getString("maxDateDone");
				       		
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
					

				   	}
					return maxDateDone;
					
					
				   
				   }
				public String maxDateSubmit1(String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateSubmit="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(submitdate,interval 5.30 hour_minute)) as maxDateSubmit from sentbox partition ("+date+") ;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateSubmit=rs.getString("maxDateSubmit");
				       		
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
					

				   	}
					return maxDateSubmit;
					
					
				   
				   }
				public String maxDateSubmitByUserName1(String date,String username) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateSubmit="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(submitdate,interval 5.30 hour_minute)) as maxDateSubmit from sentbox partition ("+date+"),report.iddetails where sentbox.AccountId=report.iddetails.companyid and report.iddetails.username like '%"+username+"%';";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateSubmit=rs.getString("maxDateSubmit");
				       		
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
					

				   	}
					return maxDateSubmit;
					
					
				   
				   }
				public String maxDateDone1(String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateDone="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(donedate,interval 5.30 hour_minute)) as maxDateDone from sentbox partition ("+date+") ;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateDone=rs.getString("maxDateDone");
				       		
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
					

				   	}
					return maxDateDone;
					
					
				   
				   }
				public String maxDateDoneUsername1(String date,String username) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateDone="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(donedate,interval 5.30 hour_minute)) as maxDateDone from sentbox partition ("+date+"),report.iddetails where sentbox.AccountId=report.iddetails.companyid and report.iddetails.username like '%"+username+"%';";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateDone=rs.getString("maxDateDone");
				       		
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
					

				   	}
					return maxDateDone;
					
					
				   
				   }
				public String maxDateSubmit2(String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateSubmit="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(submitdate,interval 5.30 hour_minute)) as maxDateSubmit from inbounddlr partition ("+date+") ;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateSubmit=rs.getString("maxDateSubmit");
				       		
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
					

				   	}
					return maxDateSubmit;
					
					
				   
				   }
				public String maxDateSubmitUsername2(String date,String username) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateSubmit="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(submitdate,interval 5.30 hour_minute)) as maxDateSubmit from inbounddlr partition ("+date+"),report.iddetails where inbounddlr.SiteUserId=report.iddetails.companyid and report.iddetails.username like '%"+username+"%';";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateSubmit=rs.getString("maxDateSubmit");
				       		
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
					

				   	}
					return maxDateSubmit;
					
					
				   
				   }
				public String maxDateDone2(String date) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateDone="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(donedate,interval 5.30 hour_minute)) as maxDateDone from inbounddlr partition ("+date+") ;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateDone=rs.getString("maxDateDone");
				       		
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
					

				   	}
					return maxDateDone;
					
					
				   
				   }
				public String maxDateDoneUsername2(String date,String username) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   String maxDateDone="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select max(date_add(donedate,interval 5.30 hour_minute)) as maxDateDone from inbounddlr partition ("+date+"),report.iddetails where inbounddlr.SiteUserId=report.iddetails.companyid and report.iddetails.username like '%"+username+"%';";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				       		maxDateDone=rs.getString("maxDateDone");
				       		
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
					

				   	}
					return maxDateDone;
					
					
				   
				   }
				public void getDlrMismatchSentBox(JSONArray sentbox, String date, String companyname, String date_p) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select report.iddetails.username,count(*) as count from sentbox partition ("+date_p+"),report.iddetails where sentbox.AccountId=report.iddetails.companyid and report.iddetails.companyname like '%"+companyname+"%' and date_add(submitdate,interval 5.30 hour_minute ) like '"+date+"%' group by report.iddetails.username;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("username", rs.getString("username"));
		                    jsonObject.put("count", rs.getString("count"));
		                    sentbox.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public void getDlrMismatchInbounddlr(JSONArray inbounddlr, String date, String companyname,
						String date_p) {
				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
	                    query = "select report.iddetails.username,count(*) as count from inbounddlr partition ("+date_p+"),report.iddetails  where inbounddlr.SiteUserId=report.iddetails.companyid and report.iddetails.companyname like '%"+companyname+"%' and date_add(submitdate,interval 5.30 hour_minute ) like '"+date+"%' group by report.iddetails.username;";
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("username", rs.getString("username"));
		                    jsonObject.put("count", rs.getString("count"));
		                    inbounddlr.put(jsonObject);
				       		
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
					

				   	}
					
					
				   
				   }
				public void runLinuxCommandService(JSONArray jsonArray, String commandLine) {
					try {
						
						Process process = Runtime.getRuntime().exec(commandLine);
						StringBuilder output = new StringBuilder();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(process.getInputStream()));
						String line;
						int i=1;
						while ((line = reader.readLine()) != null) {
							output.append(line + "\n");
							JSONObject jsonObject=new JSONObject();
							jsonObject.put("line", line);
							//System.out.println("==>"+line);
							jsonArray.put(jsonObject);
							i++;
						}

						int exitVal = process.waitFor();
						if (exitVal == 0) {
							System.out.println("Success11111!");
							//System.out.println(output);
							
						} else {
							//abnormal...
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
						}
				public void runLinuxCommand(JSONArray jsonArray, String commandLine) {
					try {
						//System.out.println("commandLine  "+commandLine);
						String commandArr[]=commandLine.split(",");
						Process process = Runtime.getRuntime().exec(commandArr);

						StringBuilder output = new StringBuilder();

						BufferedReader reader = new BufferedReader(
								new InputStreamReader(process.getInputStream()));

						String line;
						int i=1;
						while ((line = reader.readLine()) != null) {
							output.append(line + "\n");
							JSONObject jsonObject=new JSONObject();
							jsonObject.put("line", line);
							//System.out.println("==>"+line);
							jsonArray.put(jsonObject);
							i++;
						}

						int exitVal = process.waitFor();
						if (exitVal == 0) {
							System.out.println("Success11111!");
							//System.out.println(output);
							
						} else {
							//abnormal...
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						}
				public void getSenderIDTraffic(String pdate, String date, String companyName, JSONArray jsonArray, String userName, String selectBy) {

				   	Connection connection=DbConnection_Search.getInstance().getConnection();
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
				       if(selectBy.equals("1")) {
				    	   query = "select substr(DATE_ADD(sentbox.submitdate, INTERVAL 5.30 HOUR_MINUTE ),1,10) as date,sentbox.AccountId,report.iddetails.username,SenderId,count(sentbox.AliasMessageId) as submission,sum(if(inbounddlr.ErrorCode='000',1,0)) as Delivered  from sentbox partition ("+pdate+"),inbounddlr partition ("+pdate+"),report.iddetails where sentbox.AccountId=report.iddetails.companyid and sentbox.AliasMessageId=inbounddlr.AliasMessageId and  report.iddetails.companyname like '%"+companyName+"%' and date_add(sentbox.submitdate,interval 5.30 hour_minute ) like '"+date+"%' group by SenderId,report.iddetails.username order by submission desc limit 50;";   
				       }else  if(selectBy.equals("2")) {
				    	   query = "select substr(DATE_ADD(sentbox.submitdate, INTERVAL 5.30 HOUR_MINUTE ),1,10) as date,sentbox.AccountId,report.iddetails.username,SenderId,count(sentbox.AliasMessageId) as submission,sum(if(inbounddlr.ErrorCode='000',1,0)) as Delivered  from sentbox partition ("+pdate+"),inbounddlr partition ("+pdate+"),report.iddetails where sentbox.AccountId=report.iddetails.companyid and sentbox.AliasMessageId=inbounddlr.AliasMessageId and  report.iddetails.companyname like '%"+companyName+"%' and iddetails.username like '%"+userName+"%' and date_add(sentbox.submitdate,interval 5.30 hour_minute ) like '"+date+"%' group by SenderId,report.iddetails.username order by submission desc limit 50;";   
				       }
			    	   
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("date", rs.getString("date"));
		                    jsonObject.put("accountId", rs.getString("AccountId"));
		                    jsonObject.put("username", rs.getString("username"));
		                    jsonObject.put("senderId", rs.getString("SenderId"));
		                    jsonObject.put("submission", rs.getLong("submission"));
		                    jsonObject.put("delivered", rs.getLong("Delivered"));
		                    jsonArray.put(jsonObject);
				       		
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
					

				   	}
				}
				public void getSenderIDTraffic2(String pdate, String date, String companyName, JSONArray jsonArray, String userName, String selectBy) {

				   	Connection connection=DbConnection_All.getInstance().getConnection(7);
				   	Statement stmt = null;
				   	ResultSet rs = null;
				   String query="";
				   	try {
				        
				       stmt=connection.createStatement();
				       if(selectBy.equals("1")) {
				    	   query = "select substr(DATE_ADD(sentbox.submitdate, INTERVAL 5.30 HOUR_MINUTE ),1,10) as date,sentbox.AccountId,report.iddetails.username,SenderId,count(sentbox.AliasMessageId) as submission,sum(if(inbounddlr.ErrorCode='000',1,0)) as Delivered  from sentbox partition ("+pdate+"),inbounddlr partition ("+pdate+"),report.iddetails where sentbox.AccountId=report.iddetails.companyid and sentbox.AliasMessageId=inbounddlr.AliasMessageId and  report.iddetails.companyname like '%"+companyName+"%' and date_add(sentbox.submitdate,interval 5.30 hour_minute ) like '"+date+"%' group by SenderId,report.iddetails.username order by submission desc limit 50;";   
				       }else  if(selectBy.equals("2")) {
				    	   query = "select substr(DATE_ADD(sentbox.submitdate, INTERVAL 5.30 HOUR_MINUTE ),1,10) as date,sentbox.AccountId,report.iddetails.username,SenderId,count(sentbox.AliasMessageId) as submission,sum(if(inbounddlr.ErrorCode='000',1,0)) as Delivered  from sentbox partition ("+pdate+"),inbounddlr partition ("+pdate+"),report.iddetails where sentbox.AccountId=report.iddetails.companyid and sentbox.AliasMessageId=inbounddlr.AliasMessageId and  report.iddetails.companyname like '%"+companyName+"%' and iddetails.username like '%"+userName+"%' and date_add(sentbox.submitdate,interval 5.30 hour_minute ) like '"+date+"%' group by SenderId,report.iddetails.username order by submission desc limit 50;";   
				       }
			    	   
		               rs = stmt.executeQuery(query);
				       	while (rs.next()) {
				      
		                    JSONObject jsonObject=new JSONObject();
		                    jsonObject.put("date", rs.getString("date"));
		                    jsonObject.put("accountId", rs.getString("AccountId"));
		                    jsonObject.put("username", rs.getString("username"));
		                    jsonObject.put("senderId", rs.getString("SenderId"));
		                    jsonObject.put("submission", rs.getLong("submission"));
		                    jsonObject.put("delivered", rs.getLong("Delivered"));
		                    jsonArray.put(jsonObject);
				       		
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
					}
				}
				public String getDateWithAddDay(int number) {
					Calendar calendar = Calendar.getInstance(); 
					calendar.add(Calendar.DAY_OF_MONTH, number);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
					Date date =calendar.getTime();
					String newdate=formatter.format(date);
					return newdate;
				}
				public String getDateWithAddDay(int number,String format) {
					Calendar calendar = Calendar.getInstance(); 
					calendar.add(Calendar.DAY_OF_MONTH, number);
					SimpleDateFormat formatter = new SimpleDateFormat(format);  
					Date date =calendar.getTime();
					String newdate=formatter.format(date);
					return newdate;
				}
				public  String getNextDate(String  curDate,int day) {
					 String genDate="";
					 
					try {
						 final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						 Date date = format.parse(curDate);
						 final Calendar calendar = Calendar.getInstance();
						  calendar.setTime(date);
						  calendar.add(Calendar.DAY_OF_YEAR, day);
						  genDate =format.format(calendar.getTime());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					 
					  return genDate; 
					}
}
