package api.servlet;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import all.beans.BulkMismatchBeans;
import all.beans.SentBoxBulkFiles;
import all.beans.SmppUser;
import api.daoImpl.SendEmail;
import api.daoImpl.Smpp_DaoImpl;
import common.database.DbConnectionMongo;
import mylibs.ApiCons;
import mylibs.Cons_msg;
import response_pojo.pojo_Status_Msg;
import response_pojo.pojo_admin_user_Complaints_count;

/**
 * Servlet implementation class AllUserServices
 */
@WebServlet("/AllUserServices_SmppSupport")
public class AllUserServices_SmppSupport extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ServerDatetime serverDatetime = new ServerDatetime();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AllUserServices_SmppSupport() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws Exception {
    	  response.setContentType("text/html;charset=UTF-8");
    	  PrintWriter out=response.getWriter();

    	 
    	  
    	//################################################################### all_users_count  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("all_users_count")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray = new JSONArray ();
        	  String requested_date = request.getParameter("requested_date");
        	          	  
        	  try 
        	  {
        		smpp_dao.all_users_count(requested_date,jsonArray,"");
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	 }
        //################################################################### all_users_countApi  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("all_users_countApi")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray = new JSONArray ();
        	  String requested_date = request.getParameter("requested_date");
        	  String company_name = request.getParameter("company_name");
        	  if(company_name!=null) {
        		  if(company_name.equalsIgnoreCase("null")) {
        			  company_name="";
        		  }
        	  }else {
        		  company_name="";
        	  }  
        	  try 
        	  {
        		smpp_dao.all_users_countApi(requested_date,jsonArray,company_name);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	 }
        //################################################################### all_users_count  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("all_users_count_spanel")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray = new JSONArray ();
        	  String requested_date = request.getParameter("requested_date");
        	  String company_name = request.getParameter("company_name");
        	  if(company_name!=null) {
        		  if(company_name.equalsIgnoreCase("null")) {
        			  company_name="";
        		  }
        	  }else {
        		  company_name="";
        	  }
        	  try 
        	  {
        		smpp_dao.all_users_countSpanelApi(requested_date,jsonArray,company_name);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	 }
          //################################################################### get_spanel_json_data  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("get_spanel_json_data")) 
          {
        	  Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
        	  MongoClient mongo = DbConnectionMongo.getInstance().getConnection();
      	    	DB db = mongo.getDB("count_sms");
      	    	DBCollection collection = db.getCollection("spaneldata");
        	  StringBuilder jsonBuff = new StringBuilder();
        	  String line = null;
        	  try {
        	      BufferedReader reader = request.getReader();
        	      while ((line = reader.readLine()) != null)
        	          jsonBuff.append(line);
        	  } catch (Exception e) { /*error*/ }

        	  //System.out.println("Request JSON string :" + jsonBuff.toString());
        	  JSONArray jsonArray=new JSONArray(jsonBuff.toString());
        	  for(int i=0;i<jsonArray.length();i++) {
        		  JSONObject jsonObject=jsonArray.getJSONObject(i);
        		  int companyid=Integer.parseInt(jsonObject.getString("companyid"));
        		  String req_date=jsonObject.getString("s_date");
        		  daoImpl.insertAllUserCountMongoApi(jsonObject,collection,req_date,companyid);
        	  }
        	 
        	  
        	  
          }
          //################################################################### get_trafic_data  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("get_trafic_data")) 
          {
        	  Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
        	  JSONArray jsonArray=new JSONArray();
        	  
        	  daoImpl.getTraficData(jsonArray);
        	 out.print(jsonArray.toString());
        	  
        	  
          }
          //################################################################### get_searchsmpp_data  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("get_searchsmpp_data")) 
          {
        	  Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
        	  JSONArray jsonArray=new JSONArray();
        	  String acc_name = request.getParameter("user_id");
        	  String date = request.getParameter("date").replace("-", "");
        	  System.out.println(date);
             // int new_date = Integer.parseInt(date.substring(7)) + 1;
             // System.out.println("new_datenew_datenew_datenew_datenew_date"+new_date);
             // String date_data = date.substring(0, 7) + String.valueOf(new_date);
              System.out.println("date_datadate_datadate_datadate_data"+date);
        	  String searchdata=request.getParameter("searchdata");
        	  String search_keyword = request.getParameter("mobdata");
        	  String acc_id=daoImpl.getResetSearchId(acc_name);
        	  System.out.println("searchdatasearchdatasearchdatasearchdata"+searchdata);
        	  System.out.println("search_keywordsearch_keywordsearch_keyword"+search_keyword);
        	  System.out.println("acc_idacc_idacc_idacc_idacc_idacc_idacc_idacc_idacc_id"+acc_id);
        	daoImpl.getSearchSmppData(jsonArray,acc_name,date,searchdata,search_keyword,acc_id,date);
        	 out.print(jsonArray.toString());
        	  
        	  
          }
        //################################################################### get_sentbox_data  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("get_sentbox_data")) 
          {
        	  Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
        	  JSONArray jsonArray=new JSONArray();
        	  String userName = request.getParameter("userName");
        	  String date = request.getParameter("date");
        	  System.out.println(date);
        	  String searchdata=request.getParameter("searchdata");
        	  String type = request.getParameter("type");
        	  String acc_id=daoImpl.getResetSearchId(userName);
        	  daoImpl.getSentBoxData(jsonArray,date,searchdata,type,acc_id);
        	 out.print(jsonArray.toString());
        	  
        	  
          }
        //################################################################### all_TesyncDbData  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("all_TesyncDbData")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray = new JSONArray ();
        	  String requested_date = request.getParameter("requested_date");
        	          	  
        	  try 
        	  {
        		smpp_dao.getTesyncDbData(jsonArray,requested_date,"");
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	  
        	 
        	  
        	  
          }
    	  
    	//################################################################### all_submitted_count  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("all_submitted_count")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray = new JSONArray ();
        	  String requested_date = request.getParameter("requested_date");
        	  String company_id = request.getParameter("company_id");
        	          	  
        	  try 
        	  {
        		smpp_dao.all_submitted_count(requested_date,company_id,jsonArray);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	  
        	 
        	  
        	  
          }
        //################################################################### all_totalcount  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("all_totalcount")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONObject jsonObject=new JSONObject();
        	  String requested_date = request.getParameter("requested_date");
        	          	  
        	  try 
        	  {
        		smpp_dao.all_submitted_totalcount(requested_date,jsonObject);
        		smpp_dao.all_delivered_totalcount(requested_date,jsonObject);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonObject.toString());  
        	  }
        	  
        	 
        	  
        	  
          }
        //################################################################### all_delivered_count  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("all_delivered_count")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray = new JSONArray ();
        	  String requested_date = request.getParameter("requested_date");
        	  String company_id = request.getParameter("company_id");
        	          	  
        	  try 
        	  {
        		smpp_dao.all_delivered_count(requested_date,company_id,jsonArray);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	  
        	 
        	  
        	  
          }
        //################################################################### all_users_count_byCompanyname  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("all_users_count_byCompanyname")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray = new JSONArray ();
        	  String requested_date = request.getParameter("requested_date");
        	  String companyname = request.getParameter("companyname");
        	          	  
        	  try 
        	  {
        		  smpp_dao.all_users_count_byCompanyname(requested_date,jsonArray,companyname);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	  
        	 
        	  
        	  
          }
        //################################################################### all_users_count_byCompanyname_user  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("all_users_count_byCompanyname_user")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray = new JSONArray ();
        	  String requested_date = request.getParameter("requested_date");
        	  String companyname = request.getParameter("companyname");
        	  String userid=request.getParameter("userid");      	  
        	  try 
        	  {
        		  smpp_dao.all_users_count1CompanyName(requested_date, jsonArray, userid, companyname); 		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	  
        	 
        	  
        	  
          }
        //################################################################### all_users_count_byCompanynameBydate  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("all_users_count_byCompanynameBydate")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray = new JSONArray ();
        	  String requested_date = request.getParameter("requested_date");
        	  String companyname = request.getParameter("companyname");
        	          	  
        	  try 
        	  {
        		  smpp_dao.all_users_count_byCompanynameBydate(requested_date,jsonArray,companyname);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	  
        	 
        	  
        	  
          }
        //################################################################### getUserDetailsData  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("getUserDetailsData")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray = new JSONArray ();
        	 
        	          	  
        	  try 
        	  {
        		  smpp_dao.getUserDetailsData(jsonArray);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	  
        	 
        	  
        	  
          }
        //################################################################### getMonthlySumittedCountByCompanyName  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("getMonthlySumittedCountByCompanyName")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONObject jsonObject=new JSONObject();
        	  String requested_date = request.getParameter("requested_date");
        	  String companyname = request.getParameter("companyname");
        	          	  
        	  try 
        	  {
        		  String monthliSubmitted[]=new String[2];
				  monthliSubmitted=smpp_dao.getMonthlySumittedCountByCompanyName(companyname,requested_date);
				  jsonObject.put("s_count", monthliSubmitted[0]);
				  jsonObject.put("d_count", monthliSubmitted[1]);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonObject.toString());  
        	  }
        	  
        	 
        	  
        	  
          }
        //################################################################### getMonthlySumittedCountByCompanyNameUser  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("getMonthlySumittedCountByCompanyNameUser")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONObject jsonObject=new JSONObject();
        	  String requested_date = request.getParameter("requested_date");
        	  String companyname = request.getParameter("companyname");
        	  String userid   =request.getParameter("userid");
        	  try 
        	  {
        		  String monthliSubmitted[]=new String[2];
				  monthliSubmitted=smpp_dao.getMonthlySumittedCountByCompanyNameUser(companyname,requested_date,userid);
				  jsonObject.put("s_count", monthliSubmitted[0]);
				  jsonObject.put("d_count", monthliSubmitted[1]);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonObject.toString());  
        	  }
        	  
        	 
        	  
        	  
          }
        //################################################################### getPanelCount  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("getPanelCount")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONObject jsonObject=new JSONObject();
        	  String requested_date = request.getParameter("requested_date");
        	          	  
        	  try 
        	  {
        		  smpp_dao.getPanelCount(jsonObject,requested_date);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonObject.toString());  
        	  }
        	 }
        //################################################################### AddSmppUser  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("AddSmppUser")) 
          {
        	  JSONObject jsonObject=new JSONObject();
        String name=request.getParameter("name");
      	String userpass=request.getParameter("userpass");
      	jsonObject.put("name", name);
      	jsonObject.put("userpass", userpass);
      	SmppUser smppUser=new SmppUser();
      	smppUser.setName(name);
      	smppUser.setPassword(userpass);
      	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
      	int i=daoImpl.AddSmppUser(smppUser);
      	if(i>0) {
      		jsonObject.put("message", "added user");
      		jsonObject.put("status", "true");
          }else {
        	  jsonObject.put("message", "not add user");
        		jsonObject.put("status", "false");
          }
      	out.print(jsonObject.toString());
      	}
        //################################################################### getAllUserData  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("getAllUserData")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray=new JSONArray();
        	          	  
        	  try 
        	  {
        		
        		  smpp_dao.getAllUserData(jsonArray);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	 }
        //################################################################### UpdateSmppUser  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("UpdateSmppUser")) 
          {
        	  JSONObject jsonObject=new JSONObject();
        	  Smpp_DaoImpl smpp_DaoImpl=new Smpp_DaoImpl();
              int status = 0;
              String userid = request.getParameter("userid");
              String name = request.getParameter("name");
              jsonObject.put("userid", userid);
              jsonObject.put("name", name);
              SmppUser smppUser=new SmppUser();
               smppUser.setName(name);
               smppUser.setId(Integer.parseInt(userid));
            
              	status=smpp_DaoImpl.UpdateSmppUser(smppUser);
      	if(status>0) {
      		jsonObject.put("message", "updated user");
      		jsonObject.put("status", "true");
          }else {
        	  jsonObject.put("message", "not update user");
        		jsonObject.put("status", "false");
          }
      	out.print(jsonObject.toString());
      	}
        //################################################################### DeleteSmppUser  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("DeleteSmppUser")) 
          {
        	  JSONObject jsonObject=new JSONObject();
        	  String userid=request.getParameter("userid");
        	  jsonObject.put("userid", userid);
          	Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
          	int i=daoImpl.DeleteSmppUser(userid);
	      	if(i>0) {
	      		jsonObject.put("message", "deleted user");
	      		jsonObject.put("status", "true");
	          }else {
	        	  jsonObject.put("message", "not delete user");
	        		jsonObject.put("status", "false");
	          }
      	out.print(jsonObject.toString());
      	}
        //################################################################### getPanelClient  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("getPanelClient")) 
          {
        	  Smpp_DaoImpl daoImpl = new Smpp_DaoImpl();
        	  JSONArray jsonArray=new JSONArray();
        	          	  
        	  try 
        	  {
        		
        		  daoImpl.getPanelClient(jsonArray);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	 }
        //################################################################### getPanelClientByName  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("getPanelClientByName")) 
          {
        	  Smpp_DaoImpl daoImpl = new Smpp_DaoImpl();
        	  JSONArray jsonArray=new JSONArray();
        	  String companyname=request.getParameter("companyname");       	  
        	  try 
        	  {
        		
        		  
        			daoImpl.getPanelClientByName(jsonArray,companyname);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	 }
        //################################################################### getPanelError  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("getPanelError")) 
          {
        	  Smpp_DaoImpl daoImpl = new Smpp_DaoImpl();
        	  JSONArray jsonArray=new JSONArray();
        	          	  
        	  try 
        	  {
        		
        			daoImpl.getPanelError(jsonArray);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	 }
        //################################################################### getPanelCountUser  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("getPanelCountUser")) 
          {
        	  Smpp_DaoImpl daoImpl = new Smpp_DaoImpl();
        	 JSONObject jsonObject=new JSONObject();
        	 String requested_date=request.getParameter("requested_date");
        	 String id=request.getParameter("id");
        	          	  
        	  try 
        	  {
        		
        		  daoImpl.getPanelCountUser(jsonObject,requested_date,id);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonObject.toString());  
        	  }
        	 }
        //################################################################### all_users_count_user  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("all_users_count_user")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray = new JSONArray ();
        	  String requested_date = request.getParameter("requested_date");
        	  String company_name = request.getParameter("company_name");
        	  String userid=request.getParameter("userid");
        	  if(company_name!=null) {
        		  if(company_name.equalsIgnoreCase("null")) {
        			  company_name="";
        		  }
        	  }else {
        		  company_name="";
        	  }
        	  try 
        	  {
        		smpp_dao.all_users_count1(requested_date, jsonArray, userid, company_name);;
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	 }
        //################################################################### onnetdata  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("onnetdata")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray = new JSONArray ();
        	  String tablename=request.getParameter("tablename");
        	  
        	  try 
        	  {
        		smpp_dao.getOnnetData(jsonArray,tablename);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	 }
        //################################################################### offnetdata  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("offnetdata")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray = new JSONArray ();
        	  String tablename=request.getParameter("tablename");
        	  
        	  try 
        	  {
        		smpp_dao.getOffnetData(jsonArray,tablename);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	 }
          //################################################################### on_offnet_opernator  ###################################################################//         
          if (request.getParameter("api_type").equalsIgnoreCase("on_offnet_opernator")) 
          {
        	  Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        	  JSONArray jsonArray = new JSONArray ();
        	  String tablename=request.getParameter("tablename");
        	  String date=request.getParameter("date");
        	  try 
        	  {
        		smpp_dao.getOnOffnetByOpertortData(jsonArray,tablename,date);
        		  
        	  }
        	  catch(Exception e) 
        	  {
        		  
        	  }
        	  finally
        	  {
        		out.println(jsonArray.toString());  
        	  }
        	 }
    	//################################################################### division_login  ###################################################################//         
        /*  else if (request.getParameter("api_type").equalsIgnoreCase("division_login")) {
            	
            	System.out.println("enter in DivisionLogin");
            //========================Dao============================//
            	 DivisionAdmin division = new DivisionAdmin();
            	 DivisionHeadDaoImpl divDaoImpl = new DivisionHeadDaoImpl();
               // userDaoImpl userDaoImpl = new UserDaoImpl();
                JSONObject jobj = new JSONObject();
                Map<String, String> strStrMap = new HashMap<String, String>();
                
                String referral_code=null;
                String password1=null;
                Boolean status=false;
                String mobile_no = request.getParameter("mobile_no");
                String password = request.getParameter("password");
              
                division.setContact_mobile(mobile_no);
                division.setPassword(password);
                
               
                 try {
              	 //========================Dao============================//
                	 status = divDaoImpl.checkUser(division);
                 	if(status==true){
                 		division.setStatus("true");
                 		division.setMessage(Cons_msg.user_login_truestatus);
                 		 
                 	 }else{ 
                 		 
                 		division.setStatus("false");
                 		division.setMessage(Cons_msg.user_login_falsestatus);
                 		
                 	 }
                 	
                } catch (Exception e) {
                    e.printStackTrace();
                }
                 finally
                 {
                	 Gson gson = new Gson();
    		    	 
    		    	 jobj= new JSONObject(gson.toJson(division));
    		         out.println(jobj.toString());
                 }
            }*/
             
             
             
         	//################################################################### dashboard  ###################################################################//         
             if (request.getParameter("api_type").equalsIgnoreCase("dashboard")) {
            	
            	 //========================Dao============================//
            	 String req_date = request.getParameter("req_date");	
            	 Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
               // userDaoImpl userDaoImpl = new UserDaoImpl();
                JSONObject jobj = new JSONObject();
                pojo_admin_user_Complaints_count  response_p = new pojo_admin_user_Complaints_count ();
               
                 try {
              	 //========================Dao============================//
                	 smpp_dao.dashboardCount(req_date,response_p);
                 
                 	
                } catch (Exception e) {
                    e.printStackTrace();
                }
                 finally
                 {
                		Gson gson = new Gson();
             	    	 
         		    	 jobj= new JSONObject(gson.toJson(response_p));
         		         out.println(jobj.toString());
                 }
            }
    	
           //################################################################### complaint_History  ###################################################################// 	
     		else if (request.getParameter("api_type").equalsIgnoreCase("complaint_History")) {
     			
     			  
     			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
     			 JSONArray jsonArray = new  JSONArray(); 
     			 String req_date = request.getParameter("req_date");
     		      String key_id=request.getParameter("key_id");
     		      int kid=Integer.parseInt(key_id);
     		      String count=request.getParameter("count");
     		      String resolved_status = request.getParameter("resolved_status");
     			  	try {
     			  
     			  		smpp_dao.complaint_History(jsonArray,req_date,kid,count,resolved_status); }
     			  catch (Exception e) { e.printStackTrace(); } finally {
     			  
     			  out.println(jsonArray.toString()); }
     			 }	
             
    	
             //################################################################### complaint_History  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("complaint_History")) 
       		{
       		       			  
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			 JSONArray jsonArray = new  JSONArray(); 
       			 String req_date = request.getParameter("req_date");
       		      String key_id=request.getParameter("key_id");
       		      int kid=Integer.parseInt(key_id);
       		      String count=request.getParameter("count");
       		      String resolved_status = request.getParameter("resolved_status");
       			  	try {
     			  
       			  		smpp_dao.complaint_History(jsonArray,req_date,kid,count,resolved_status); }
       			  catch (Exception e) { e.printStackTrace(); } finally {
       			  
       			  out.println(jsonArray.toString()); }
       			 }
             
             //################################################################### complaint_History  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("updateStatus")) 
       		{
      			
    			  
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
      			pojo_Status_Msg  response_p = new pojo_Status_Msg ();
      			JSONObject jobj = new JSONObject();

      			
      			  String complaint_id=request.getParameter("req_id");
      			  String resolved_status=request.getParameter("resolved_status");
      		     
      			  	try {
      			  
      			  	int update_status = smpp_dao.admin_user_UpdateComplaintStatus(complaint_id,resolved_status); 
      			  	if(update_status>0)
      			  	{
      			  	response_p.setStatus("true");
      			    response_p.setMsg(Cons_msg.admin_user_UpdateComplaintStatus_true_status);
      			  	}
      			  	else
      			  	{
      			  	response_p.setStatus("false");	
      			    response_p.setMsg(Cons_msg.admin_user_UpdateComplaintStatus_false_status);
      			  	}
      			  	
      			  	}
      			  catch (Exception e) { e.printStackTrace(); }  
      			  	finally
      	        {
               		Gson gson = new Gson();
      	    	 
      		    	 jobj= new JSONObject(gson.toJson(response_p));
      		         out.println(jobj.toString());
              }
      			 }
             //################################################################### fetch_esmereportdata  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("fetch_esmereportdata")) 
       		{
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			String clientid=request.getParameter("clientid");
       			if(clientid.equalsIgnoreCase("")) {
       				clientid="0";
       			}else if(clientid==null) {
       				clientid="0";
       			}
       			System.out.println("clientidclientidclientid"+clientid);
       			
       			String requestdate=request.getParameter("requestdate");
       			System.out.println("requestdaterequestdaterequestdaterequestdate"+requestdate);
       			smpp_dao.getEsmeReportData(jsonArray,clientid,requestdate);
       			out.print(jsonArray.toString());
       			
       		}
             //################################################################### fetch_reportdata  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("fetch_reportdata")) 
       		{
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			String clientid=request.getParameter("clientid");
       			if(clientid.equalsIgnoreCase("")) {
       				clientid="0";
       			}else if(clientid==null) {
       				clientid="0";
       			}
       			String requestdate=request.getParameter("requestdate");
       			smpp_dao.getReportData(jsonArray,clientid,requestdate);
       			out.print(jsonArray.toString());
       			
       		} //################################################################### panelcount  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("panelcount")) 
       		{
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			JSONArray tbl_submitted=new JSONArray();
       			JSONArray tbl_deliver=new JSONArray();
       			String requestdate=request.getParameter("requestdate");
       			smpp_dao.getPAnelCountData1(tbl_submitted,requestdate);
       			smpp_dao.getPAnelCountData2(tbl_deliver,requestdate);
       			for (int i = 0; i < tbl_submitted.length(); i++) {
					JSONObject jsonObject=tbl_submitted.getJSONObject(i);
					JSONObject sortJO=new JSONObject();
					int userid=jsonObject.getInt("userid");
					String s_count=jsonObject.getString("count");
					String username=jsonObject.getString("username");
					String d_count="0";
					for (int j = 0; j < tbl_deliver.length(); j++) {
						JSONObject jsonObject2=tbl_deliver.getJSONObject(j);
						int userid2=jsonObject2.getInt("userid");
						String username2=jsonObject2.getString("username");
						if(username.equalsIgnoreCase(username2)) {
							d_count=jsonObject2.getString("count");
						}
					}
					sortJO.put("userid", userid);
					sortJO.put("s_count", s_count);
					sortJO.put("d_count", d_count);
					sortJO.put("username", username);
					jsonArray.put(sortJO);
				}
       			out.print(jsonArray.toString());
       			
       		}
           //################################################################### checkallbulkfile  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("checkallbulkfile")) 
       		{
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			smpp_dao.getAllBulkFileName(jsonArray);
       			out.print(jsonArray.toString());
       			
       		}//################################################################### getapr20withAccountName  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("getapr20withAccountName")) 
       		{
       			String errorcode=request.getParameter("errorcode");
       			String reqdate=request.getParameter("reqdate");
       			String tablename=request.getParameter("tablename");
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			smpp_dao.getapr20DataWithAccountName(jsonArray,errorcode,reqdate,tablename);
       			out.print(jsonArray.toString());
       			
       		}
           //################################################################### getLrnLiveByDate  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("getLrnLiveByDate")) 
       		{
       			String errorcode=request.getParameter("errorcode");
       			String accountId=request.getParameter("accountId");
       			String tablename=request.getParameter("tablename");
       			String senderid=request.getParameter("senderid");
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			smpp_dao.getLrnLiveData(jsonArray,errorcode,accountId,tablename,senderid);
       			out.print(jsonArray.toString());
       			
       		}
           //################################################################### getapr20withSenderId  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("getapr20withSenderId")) 
       		{
       			String errorcode=request.getParameter("errorcode");
       			String account_name=request.getParameter("account_name");
       			String reqdate=request.getParameter("reqdate");
       			String tablename=request.getParameter("tablename");
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			smpp_dao.getapr20DataWithSenderId(jsonArray,errorcode,reqdate,tablename,account_name);
       			out.print(jsonArray.toString());
       			
       		}
           //################################################################### checkbulkfile  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("checkbulkfile")) 
       		{
       			String filename=request.getParameter("filename");
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONObject jsonObject=new JSONObject();
       			smpp_dao.getBulkFileName(filename,jsonObject);
       			out.print(jsonObject.toString());
       			
       		}
             //################################################################### checkbulkstatus  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("checkbulkstatus")) 
       		{
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONObject jsonObject=new JSONObject();
       			smpp_dao.getBulkFileStatus(jsonObject);
       			out.print(jsonObject.toString());
       			
       		}
           //################################################################### lastEntry  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("lastEntry")) 
       		{
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			String date = request.getParameter("date");
       			String sqlno = request.getParameter("sqlno");
       			if(sqlno.equals("1")) {
       				smpp_dao.getLastEntry1(jsonArray, date);
       			}else if(sqlno.equals("2")) {
       				smpp_dao.getLastEntry2(jsonArray, date);
       			}
       			
       			
       			out.print(jsonArray.toString());
       			
       		}
           //################################################################### gatewayAnalysis  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("gatewayAnalysis")) 
       		{
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			String date = request.getParameter("date");
       			String sqlno = request.getParameter("sqlno");
       			String type = request.getParameter("type");
       			if(sqlno.equals("1")) {
       				smpp_dao.getGateWayAnalysis1(jsonArray, date,type);
       			}else if(sqlno.equals("2")) {
       				smpp_dao.getGateWayAnalysis2(jsonArray, date,type);
       			}
       			out.print(jsonArray.toString());
       			
       		}
           //################################################################### failureAnalysis  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("failureAnalysis")) 
       		{
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			String date = request.getParameter("date");
       			String sqlno = request.getParameter("sqlno");
       			String type = request.getParameter("type");
       			String panel="1";
       			if(type.equals("1")) {
       				panel="1";
       			}else if(type.equals("2")) {
       				panel="2";
       			}else if(type.equals("3")) {
       				panel="3";
       			}
       			String sql="";
       			if(sqlno.equals("1")) {
       				
       				sql="select report.iddetails.username,report.giddetails.gatewayname,count(*) as count from report.iddetails,report.giddetails,sentbox partition ("+date+") where (report.giddetails.serverid like '"+panel+"' ) and report.giddetails.gatewayid=sentbox.GatewayId and report.iddetails.companyid=sentbox.AccountId and ErrorCode like '10' and Status like '%failed%' group by report.iddetails.username,report.giddetails.gatewayname;";
       			}else if(sqlno.equals("2")) {
       				sql="select report.iddetails.username,report.giddetails.gatewayname,count(*) as count from report.iddetails,report.giddetails,sentbox partition ("+date+") where (report.giddetails.serverid like '"+panel+"') and report.giddetails.gatewayid=sentbox.GatewayId and report.iddetails.companyid=sentbox.AccountId and ErrorCode like '196' and Status like '%failed%' group by report.iddetails.username,report.giddetails.gatewayname;";
       			}else if(sqlno.equals("3")) {
       				sql="select report.iddetails.username,report.giddetails.gatewayname,count(*) as count from report.iddetails,report.giddetails,inbounddlr partition ("+date+") where (report.giddetails.serverid like '"+panel+"') and report.giddetails.gatewayid=inbounddlr.GatewayId and report.iddetails.companyid=inbounddlr.SiteuserId and inbounddlr.ErrorCode like '1044' group by report.iddetails.username,report.giddetails.gatewayname;";
       			}else if(sqlno.equals("4")) {
       				sql="select report.iddetails.username,report.giddetails.gatewayname,count(*) as count from report.iddetails,report.giddetails,inbounddlr partition ("+date+") where (report.giddetails.serverid like '"+panel+"') and report.giddetails.gatewayid=inbounddlr.GatewayId and report.iddetails.companyid=inbounddlr.SiteuserId and (inbounddlr.ErrorCode like '88' or inbounddlr.ErrorCode like '088') group by report.iddetails.username,report.giddetails.gatewayname;";
       			}else if(sqlno.equals("5")) {
       				sql="select report.iddetails.username,report.giddetails.gatewayname,count(*) as count from report.iddetails,report.giddetails,sentbox partition ("+date+") where (report.giddetails.serverid like '"+panel+"') and report.giddetails.gatewayid=sentbox.GatewayId and report.iddetails.companyid=sentbox.AccountId and (ErrorCode like '1038' or ErrorCode like '1039') group by report.iddetails.username,report.giddetails.gatewayname;";
       			}
       			smpp_dao.getFailureAnalysis(jsonArray, sql);
       			out.print(jsonArray.toString());
       			
       		}
           //################################################################### trafficAnalysis  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("trafficAnalysis")) 
       		{
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			String date = request.getParameter("date");
       			String sqlno = request.getParameter("sqlno");
       			String sql="";
       			if(sqlno.equals("1")) {
       				sql="SELECT mid(date_add(SubmitDate, interval 5.30 hour_minute),1,13) as pushtime,count(distinct AliasMessageId) as msgcount from sentbox partition("+date+") group by mid(date_add(SubmitDate, interval 5.30 hour_minute),1,13) with rollup;";
       			}else if(sqlno.equals("2")) {
       				sql="SELECT mid(date_add(DoneDate, interval 5.30 hour_minute),1,13) as pushtime,count(distinct AliasMessageId) as msgcount from inbounddlr partition("+date+") where Status is not null group by mid(date_add(DoneDate, interval 5.30 hour_minute),1,13) with rollup;";
       			}
       			smpp_dao.getTrafficAnalysis(jsonArray, sql);
       			out.print(jsonArray.toString());
       			
       		}
           //################################################################### checkingMobileNo  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("checkingMobileNo")) 
       		{
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			String date = request.getParameter("date");
       			
       			smpp_dao.checkingMobileNo(jsonArray, date);
       			out.print(jsonArray.toString());
       			
       		}
           //################################################################### spamFailureAnalysis  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("spamFailureAnalysis")) 
       		{
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			String date = request.getParameter("date");
       			
       			smpp_dao.getSpanFailureAnalysis(jsonArray, date);
       			out.print(jsonArray.toString());
       			
       		}
           //################################################################### get_sentbox_bulkData  ###################################################################//         
             if (request.getParameter("api_type").equalsIgnoreCase("get_sentbox_bulkData")) 
             {
           	  Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
           	  String url = request.getParameter("url");
           	  String id = request.getParameter("id");
           	  String currentDate = request.getParameter("currentDate");
           	  String preDate = request.getParameter("preDate");
           	  String nextDate = request.getParameter("nextDate");
           	  String type = request.getParameter("type");
           	  String filename = request.getParameter("filename");
           	  System.out.println("urlurlurl==>>"+url);
           	  System.out.println("ididid==>>"+id);
           	  System.out.println("currentDate==>>"+currentDate);
	           	System.out.println("preDatepreDate==>>"+preDate);
	           	System.out.println("nextDatenextDate==>>"+nextDate);
	           	System.out.println("typetype==>>"+type);
	           	System.out.println("filenamefilename==>>"+filename);
           	
           	  try (BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
					  FileOutputStream fileOS = new FileOutputStream("/home/dummy_file.txt")) {
					    byte data[] = new byte[1024];
					    int byteContent;
					    while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
					        fileOS.write(data, 0, byteContent);
					    }
					    SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
					    boxBulkFiles.setId(id);
						boxBulkFiles.setStatus("2");
						boxBulkFiles.setProcess("10%");
						boxBulkFiles.setStatus_msg("downloaded file");
						boxBulkFiles.setGet_file("");
						boxBulkFiles.setRun_status("1");
					    daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
					} catch (IOException e) {
						 SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
						 boxBulkFiles.setId(id);
							boxBulkFiles.setStatus("0");
							boxBulkFiles.setProcess("0%");
							boxBulkFiles.setStatus_msg("failed download file");
							boxBulkFiles.setGet_file("");
							boxBulkFiles.setRun_status("0");
						    daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
					    e.printStackTrace();
					}
           	  		String table="dummy_table";
           	  		boolean truncatestatus=daoImpl.dropTable(table);
           	  		int i=daoImpl.createDummyTable(table,id);
           	  		int alert_i=daoImpl.alterTable(table,id);
           	  		boolean load_data_status=daoImpl.LoadSentBoxDateFile(table,id);
           	  		int update_i=daoImpl.updateSendBoxtable(table,id,currentDate,preDate,nextDate);
           	  		int update_i2=daoImpl.updateSendBoxtable2(table,id,currentDate,preDate,nextDate);
           	  		int update_i3=daoImpl.updateSendBoxtable3(table,id,currentDate,preDate,nextDate);
           	  		int update_i4=daoImpl.updateSendBoxtable4(table,id,currentDate,preDate,nextDate);
           	  		int update_i5=daoImpl.updateSendBoxtable5(table,id,currentDate,preDate,nextDate);
           	  		
           	  	boolean upload_file_status=daoImpl.uploadSentBoxFile(filename,table,id);
           	  	String uploadFile="";
			  		String fileurl="";
			  				//tpanel//usr/local/apache-tomcat-8.5.49/webapps/webapi/
       				if(type.equalsIgnoreCase("3") || type.equalsIgnoreCase("TPanel")){
       						fileurl= "/usr/local/apache-tomcat-8.5.49/webapps/webapi/uploaded/"+filename;
       						uploadFile=ApiCons.tpanelBaseUrl+"uploaded/"+filename;
       				}else if(type.equalsIgnoreCase("2") || type.equalsIgnoreCase("SPanel")){
       					fileurl= "/usr/local/apache-tomcat-8.5.37/webapps/webapi/uploaded/"+filename;
       					uploadFile=ApiCons.spanelBaseUrl+"uploaded/"+filename;
       				}else if(type.equalsIgnoreCase("1") || type.equalsIgnoreCase("Panel")){
       					fileurl= "/usr/local/apache-tomcat-8.5.37/webapps/webapi/uploaded/"+filename;
       					uploadFile=ApiCons.panelBaseUrl+"uploaded/"+filename;
       				}
       				 Path temp = Files.move
   						        (Paths.get("/tmp/"+filename), 
   						        Paths.get(fileurl));
				        if(temp != null)
				        {
				            System.out.println("File renamed and moved successfully");
				            System.out.println(uploadFile);
				            SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
				            boxBulkFiles.setId(id);
							boxBulkFiles.setStatus("7");
							boxBulkFiles.setProcess("100%");
							boxBulkFiles.setStatus_msg("Completed all process");
							boxBulkFiles.setGet_file(uploadFile);
							boxBulkFiles.setRun_status("0");
						    daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
				        }
				        else
				        {
				            System.out.println("Failed to move the file");
				            SentBoxBulkFiles boxBulkFiles=new SentBoxBulkFiles();
				            boxBulkFiles.setId(id);
							boxBulkFiles.setStatus("6");
							boxBulkFiles.setProcess("75%");
							boxBulkFiles.setStatus_msg("not uploaded your file to mysql");
							boxBulkFiles.setGet_file(uploadFile);
							boxBulkFiles.setRun_status("0");
						    daoImpl.callApiSendBoxBulkStatus(boxBulkFiles);
				        } 
				        JSONObject jobj = new JSONObject();
	       		    	jobj.put("truncatestatus", truncatestatus);
	       		    	jobj.put("load_data_status", load_data_status);
	       		    	jobj.put("upload_file_status", upload_file_status);
	       		    	jobj.put("filename", "uploaded/"+filename);
	       		    	out.println(jobj.toString());
           	  
           	  
             }
           //################################################################### get_bulk_mismatch  ###################################################################//         
             if (request.getParameter("api_type").equalsIgnoreCase("get_bulk_mismatch")) 
             {
           	  Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
           	  String id = request.getParameter("id");
           	  String currentDate = request.getParameter("currentDate");
           	  String preDate = request.getParameter("preDate");
           	  String nextDate = request.getParameter("nextDate");
           	  String type = request.getParameter("type");
           	  String filename = request.getParameter("filename");
           	  System.out.println("ididid==>>"+id);
           	  System.out.println("currentDate==>>"+currentDate);
	           	System.out.println("preDatepreDate==>>"+preDate);
	           	System.out.println("nextDatenextDate==>>"+nextDate);
	           	System.out.println("typetype==>>"+type);
	           	System.out.println("filenamefilename==>>"+filename);
           	
	           		String table="misdlranal";
           	  		boolean truncatestatus=daoImpl.dropTable(table);
           	  		int i=daoImpl.createMisDlranalTable(id);
           	  		
           	  		int alert_i=daoImpl.alterBulkMisTable(id);
           	  		int insert_i=daoImpl.insertBulkMis(id,preDate,currentDate,nextDate);
           	  		int update_i=daoImpl.updateBulkMis(id,preDate,currentDate,nextDate);
           	  		
           	  	boolean upload_file_status=daoImpl.uploadBulkMisFile(filename,id);
           	  	String uploadFile="";
			  		String fileurl="";
			  				//tpanel//usr/local/apache-tomcat-8.5.49/webapps/webapi/
       				if(type.equalsIgnoreCase("3") || type.equalsIgnoreCase("TPanel")){
       						fileurl= "/usr/local/apache-tomcat-8.5.49/webapps/webapi/uploaded/"+filename;
       						uploadFile=ApiCons.tpanelBaseUrl+"uploaded/"+filename;
       				}else if(type.equalsIgnoreCase("2") || type.equalsIgnoreCase("SPanel")){
       					fileurl= "/usr/local/apache-tomcat-8.5.37/webapps/webapi/uploaded/"+filename;
       					uploadFile=ApiCons.spanelBaseUrl+"uploaded/"+filename;
       				}else if(type.equalsIgnoreCase("1") || type.equalsIgnoreCase("Panel")){
       					fileurl= "/usr/local/apache-tomcat-8.5.37/webapps/webapi/uploaded/"+filename;
       					uploadFile=ApiCons.panelBaseUrl+"uploaded/"+filename;
       				}
       				 Path temp = Files.move
   						        (Paths.get("/tmp/"+filename), 
   						        Paths.get(fileurl));
				        if(temp != null)
				        {
				            System.out.println("File renamed and moved successfully");
				            BulkMismatchBeans bulkMismatchBeans=new BulkMismatchBeans();
			        	 	bulkMismatchBeans.setId(id);
			        	 	bulkMismatchBeans.setProcess("100%");
			        	 	bulkMismatchBeans.setResponse_msg("Completed all process");
							bulkMismatchBeans.setFile(uploadFile);
							bulkMismatchBeans.setRun_status(0);
						    daoImpl.callApiBulkMismatch(bulkMismatchBeans);
				           
				        }
				        else
				        {
				            System.out.println("Failed to move the file");
				            
						    System.out.println("File renamed and moved successfully");
				            BulkMismatchBeans bulkMismatchBeans=new BulkMismatchBeans();
			        	 	bulkMismatchBeans.setId(id);
			        	 	bulkMismatchBeans.setProcess("75%");
			        	 	bulkMismatchBeans.setResponse_msg("not uploaded your file to mysql");
							bulkMismatchBeans.setFile(uploadFile);
							bulkMismatchBeans.setRun_status(0);
						    daoImpl.callApiBulkMismatch(bulkMismatchBeans);
				        } 
				        JSONObject jobj = new JSONObject();
	       		    	jobj.put("truncatestatus", truncatestatus);
	       		    	jobj.put("upload_file_status", upload_file_status);
	       		    	jobj.put("filename", "uploaded/"+filename);
	       		    	out.println(jobj.toString());
           	  
           	  
             }
             //################################################################### sendbulkdata  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("sendbulkdata")) 
       		{
       			
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONObject jobj = new JSONObject();
       			String bytedata=request.getParameter("bytedata");
       			String date=request.getParameter("date");
       			String servername=request.getParameter("servername");
       			String filename=request.getParameter("filename");
       		 int checkdownloadfile1=smpp_dao.updateStatus("true");
       		    try {
       		    	
       				byte[] data = Base64.getDecoder().decode(bytedata);

       				try
       				{
       					OutputStream stream = new FileOutputStream("/root/temp.txt");
       				   stream.write(data);
       				}
       				catch (Exception e) 
       				{
       				   System.err.println("Couldn't write to file...");
       				}
       				System.out.println("bytedatabytedatabytedata"+bytedata);
       		    	String newdate="p"+date.replace("-", "");
       		    	
       		    	System.out.println("datedatedatedatedatedatedate"+date);
       		    
       		    	boolean truncatestatus=smpp_dao.TruncateTable();
       		    	boolean load_data_status=smpp_dao.LoadDateFile();
       		    	int update_status=smpp_dao.UpdateButTAble(newdate);
       			    	boolean checkstatus=false;
       			    	if(update_status>0) {
       			    		checkstatus=true;
       			        }else {
       			        	checkstatus=false;
       			        }
       			    	//SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");  
       				  //  Date date1 = new Date();  
       				   // String filename=formatter.format(date1)+"dlr_bpay.csv";
       			    	System.out.println(filename); 
       					  boolean upload_file_status=smpp_dao.uploadFile(filename);
       					  		String fileurl="";
       					  				//tpanel//usr/local/apache-tomcat-8.5.49/webapps/webapi/
				       					if(servername.equalsIgnoreCase("3") || servername.equalsIgnoreCase("TPanel")){
				       						fileurl= "/usr/local/apache-tomcat-8.5.49/webapps/webapi/uploaded/"+filename;
				       				}else {
				       					fileurl= "/usr/local/apache-tomcat-8.5.37/webapps/webapi/uploaded/"+filename;
				       				}
				       				 Path temp = Files.move
			       						        (Paths.get("/tmp/"+filename), 
			       						        Paths.get(fileurl));
       						        if(temp != null)
       						        {
       						            System.out.println("File renamed and moved successfully");
       						        }
       						        else
       						        {
       						            System.out.println("Failed to move the file");
       						        } 
       						        
       						        int checkfile=smpp_dao.insertFileName(filename);
       						        int checkdownloadfile=smpp_dao.updateStatus("false");
       		    	jobj.put("date", date);
       		    	jobj.put("truncatestatus", truncatestatus);
       		    	jobj.put("load_data_status", load_data_status);
       		    	jobj.put("update_status", checkstatus);
       		    	jobj.put("upload_file_status", upload_file_status);
       		    	jobj.put("filename", "uploaded/"+filename);
       		    	out.println(jobj.toString());
       		 } catch (Exception e) {
       	       e.printStackTrace();
       	   }
       		}
           //################################################################### file_upload_read  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("file_upload_read")) 
       		{
       	    	
       	    	
       	       
       	     try {	
       	    	Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       	    	 String url1 = "http://spanel.sms24hours.com:6080/webapi/uploaded/dlr_bpay.csv";
       	    	 response.sendRedirect("http://spanel.sms24hours.com:6080/webapi/uploaded/dlr_bpay.csv");
       	    	// /usr/local/apache-tomcat-8.5.37/webapps/webapi/uploaded/

       	    	// smpp_dao.downloadUsingStream(url, "/dlr_bpay.csv");
       	    	/*URL	url = new URL(url1);
				 BufferedInputStream bis = new BufferedInputStream(url.openStream());
			        FileOutputStream fis = new FileOutputStream("/dlr_bpay.csv");
			        //OutputStream outStream = response.getOutputStream();
			        byte[] buffer = new byte[1024];
			        int count=0;
			        while((count = bis.read(buffer,0,1024)) != -1)
			        {
			            fis.write(buffer, 0, count);
			        }
			        fis.close();
			        bis.close();*/
       	    	
       	     }catch(Exception e){
       	        	e.printStackTrace();
       	        }
       	    }
             //################################################################### fetch_tps_by_panel  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("fetch_tps_by_panel")) 
       		{
       			try {	
       				Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       				String date =request.getParameter("req_date");
       				String userid =request.getParameter("userid");
       				String count =request.getParameter("count");
       				String datetype =request.getParameter("datetype");
           			JSONArray jsonArray=new JSONArray();
           			smpp_dao.getTpcDatabyPanel(jsonArray,date,userid,count,datetype);
           			out.print(jsonArray.toString());
       	    	        	    	
       	     }catch(Exception e){
       	        	e.printStackTrace();
       	        }
       	    }
           //################################################################### send_mail_api   ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("send_mail_api")) 
       		{
       			try {	
       				Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       				String email =request.getParameter("email");
       				String subject =request.getParameter("subject");
       				String txt =request.getParameter("txt");
       				Thread thread=new Thread() {
       					public void run() {
       						SendEmail sendEmail=new SendEmail(email, subject, txt);
       					};
       				};
       				thread.start();
            	        	    	
       	     }catch(Exception e){
       	        	e.printStackTrace();
       	        }
       	    }
           //################################################################### checkingCharset  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("checkingCharset")) 
       		{

       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			String date = request.getParameter("date");
       			String type = request.getParameter("type");
       			String value = request.getParameter("value");
       			System.out.println("valuevalue 11 ==>"+value);
       			//smpp_dao.getCheckingCharset(jsonArray, date,type,value);
       			out.print(jsonArray.toString());
       		}
           //################################################################### getSubDlrReport  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("getSubDlrReport")) 
       		{
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONObject jsonObject=new JSONObject();
       			JSONArray subJsonArray=new JSONArray();
       			JSONArray dlrJsonArray=new JSONArray();
       			String fromDate = request.getParameter("fromDate");
       			String toDate = request.getParameter("toDate");
       			String type = request.getParameter("type");
       			smpp_dao.getSubReport(subJsonArray, fromDate,toDate,type);
       			smpp_dao.getDlrReport(dlrJsonArray, fromDate,toDate,type);
       			jsonObject.put("sub", subJsonArray);
       			jsonObject.put("dlr", dlrJsonArray);
       			out.print(jsonObject.toString());
       		}
           //################################################################### errorSubCodeByCompanyidAndDate  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("errorSubCodeByCompanyidAndDate")) 
       		{
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			String fromDate = request.getParameter("fromDate");
       			String toDate = request.getParameter("toDate");
       			String companyid = request.getParameter("companyid");
       			smpp_dao.getSubErrorCodeByCompanyIdAndDate(jsonArray, fromDate,toDate,companyid);
       			out.print(jsonArray.toString());
       		}
           //################################################################### errorDelCodeByCompanyidAndDate  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("errorDelCodeByCompanyidAndDate")) 
       		{
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			String fromDate = request.getParameter("fromDate");
       			String toDate = request.getParameter("toDate");
       			String companyid = request.getParameter("companyid");
       			smpp_dao.getDelErrorCodeByCompanyIdAndDate(jsonArray, fromDate,toDate,companyid);
       			out.print(jsonArray.toString());
       		}
           //################################################################### getSubDlrReportWithName195  ###################################################################// 	
       		else if (request.getParameter("api_type").equalsIgnoreCase("getSubDlrReportWithName195")) 
       		{
       			String fromDate = request.getParameter("fromDate");
       			String toDate = request.getParameter("toDate");
       			String name = request.getParameter("name");
       			String type = request.getParameter("type");
       			System.out.println("fromDate==>"+fromDate);
       			System.out.println("toDate==>"+toDate);
       			System.out.println("name==>"+name);
       			Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
       			JSONArray jsonArray=new JSONArray();
       			smpp_dao.getSubDlrReportWithName195(jsonArray,fromDate,toDate,name,type);
       			out.print(jsonArray.toString());
       			
       		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			processRequest(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			processRequest(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void destroy() {
        super.destroy(); //To change body of generated methods, choose Tools | Templates.
    }

}
