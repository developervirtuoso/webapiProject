package common.database;

import java.sql.Connection;

public class DbConnection_Search {
private static DbConnection_Search single_instance = null;
	
	
	public static DbConnection_Search getInstance()
    {
        if (single_instance == null)
            single_instance = new DbConnection_Search();
 
        return single_instance;
    }
	
	public  Connection getConnection(){
    	
   	 Connection conn=null;
   	try {

   			conn = C3P0DataSource_Search.getInstance() .getConnection() ;
   		
                 
   
       }
       catch(Exception e){
           System.out.println(e);
       }
       return conn;
   }
}
