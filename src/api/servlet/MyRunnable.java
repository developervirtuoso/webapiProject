package api.servlet;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import api.daoImpl.Smpp_DaoImpl;

public class MyRunnable implements Runnable {

    private String client;
    private String submited;
    private String delivered;
    private String delivered_in_per;
    private String undelivered;
    private String undelivered_in_per;
    private String error_1;
    private String error_6;
    private String error_21;
    private String error_32;
    private String error_34;
    private String error_69;
    private String error_253;
    private String error_254;
    private String requested_date;
  
    
    
    
    

    public MyRunnable(String client,String submited,String delivered,String delivered_in_per,String undelivered,String undelivered_in_per,String error_1,String error_6,String error_21,String error_32,String error_34,String error_69,String error_253,String error_254,String requested_date) {
        this.client = client;
        this.submited = submited;
        this.delivered = delivered;
        this.delivered_in_per = delivered_in_per;
        this.undelivered = undelivered;
        this.undelivered_in_per = undelivered_in_per;
        this.error_1 = error_1;
        this.error_6 = error_6;
        this.error_21 = error_21;
        this.error_32 = error_32;
        this.error_34 = error_34;
        this.error_69 = error_69;
        this.error_253 = error_253;
        this.error_254 = error_254;
        this.requested_date = requested_date;
     
    }

    public void run() {
    	 
      Smpp_DaoImpl smpp_DaoImpl=new Smpp_DaoImpl(); 
      smpp_DaoImpl.InsertUpdateTesyncTable(client,submited,delivered,delivered_in_per,undelivered,undelivered_in_per,error_1,error_6,error_21,error_32,error_34,error_69,error_253,error_254,requested_date);
     smpp_DaoImpl.InsertUpdateTesyncTableMongo(client,submited,delivered,delivered_in_per,undelivered,undelivered_in_per,error_1,error_6,error_21,error_32,error_34,error_69,error_253,error_254,requested_date);
    
    }
    
}

