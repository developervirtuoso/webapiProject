package com.example;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import api.daoImpl.Smpp_DaoImpl;
import common.database.DbConnection_Smpp;
import java.util.HashMap;
import java.util.Map;



public class SconnExample {
public static void main(String[] args) throws UnirestException {
	
	HttpResponse<JsonNode> jsonResponse = Unirest.post("http://49.50.86.152:6001/WebAdmin/Smpp/GetGatewayTcpStates")
			.header("Content-Type", "application/x-www-form-urlencoded")
			.field("UserName", "VIRTUOSOFT")
	        .field("Password", "Virtuo#soft2net")
	        .asJson();

	    System.out.println("nnnnnn"+jsonResponse.getBody().toString());
	    System.out.println(jsonResponse.getStatus());
	/*Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
	JSONArray jsonArray=new JSONArray();
	daoImpl.getIpConnection(jsonArray);
	for (int i = 0; i < jsonArray.length(); i++) {
		try {
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			String ipaddress=jsonObject.getString("IpAddress");
			String SmppUsername=jsonObject.getString("SmppUsername");
			
			String[] values = ipaddress.split(",");
			for (int j = 0; j < values.length; j++) {
				System.out.println(SmppUsername+" = "+values[j]);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
	SconnExample example=new SconnExample();
	try {
		//String livedata=example.getLiveData();
		POSTRequest();
		//System.out.println(livedata);
		//getdata1();
	}  catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
private String getLiveData() throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.post("http://49.50.86.152:6001/User/Login?ReturnUrl=%2FWebAdmin%2FSmpp%2FGetGatewayTcpStates")
				 .header("content-type", "application/x-www-form-urlencoded")
				 .queryString("ReturnUrl", "%2FWebAdmin%2FSmpp%2FGetGatewayTcpStates")
				.field("UserName", "VIRTUOSOFT")
				.field("Password", "Virtuo#soft2net")
				.asJson();
		String jsonData=response.getBody().toString();
		System.out.println("nnnn"+jsonData);
		return jsonData;
	}


public static  void POSTRequest() throws IOException {
   // final String POST_PARAMS = "ReturnUrl=%2FWebAdmin%2FSmpp%2FGetGatewayTcpStates";
  //  System.out.println(POST_PARAMS);
    URL obj = new URL("http://49.50.86.152:6001/WebAdmin/Smpp/GetGatewayTcpStates");
    HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
    postConnection.setRequestMethod("POST");
    postConnection.setRequestProperty("UserName", "a1bcdefgh");
    postConnection.setRequestProperty("Password", "Virtuo#soft2net");
    postConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    postConnection.setDoOutput(true);
    OutputStream os = postConnection.getOutputStream();
  //  os.write(POST_PARAMS.getBytes());
    os.flush();
    os.close();
    int responseCode = postConnection.getResponseCode();
    System.out.println("POST Response Code :  " + responseCode);
    System.out.println("POST Response Message : " + postConnection.getResponseMessage());
    if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
        BufferedReader in = new BufferedReader(new InputStreamReader(
            postConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in .readLine()) != null) {
            response.append(inputLine);
        } in .close();
        // print result
        System.out.println(response.toString());
    } else {
        System.out.println("POST NOT WORKED");
    }
}
}
