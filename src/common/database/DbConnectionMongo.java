package common.database;

import com.mongodb.*;

public class DbConnectionMongo {
private static DbConnectionMongo single_instance = null;
	
	
	public static DbConnectionMongo getInstance()
    {
        if (single_instance == null)
            single_instance = new DbConnectionMongo();
 
        return single_instance;
    }
	
	public  MongoClient getConnection(){
    	
		MongoClient conn=null;
   	try {

   			conn = C3P0DataSource_Mongo.getInstance() .getConnection() ;
   		
                 
   
       }
       catch(Exception e){
           System.out.println(e);
       }
       return conn;
   }
}
