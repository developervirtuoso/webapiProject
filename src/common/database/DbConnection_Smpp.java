package common.database;

import java.sql.Connection;

public class DbConnection_Smpp {
private static DbConnection_Smpp single_instance = null;
	
	
	public static DbConnection_Smpp getInstance()
    {
        if (single_instance == null)
            single_instance = new DbConnection_Smpp();
 
        return single_instance;
    }
	
	public  Connection getConnection(){
    	
   	 Connection conn=null;
   	try {

   			conn = C3P0DataSource_Smpp.getInstance() .getConnection() ;
   		
                 
   
       }
       catch(Exception e){
           System.out.println(e);
       }
       return conn;
   }
}
