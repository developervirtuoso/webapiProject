package common.database;

import java.sql.Connection;

public class DbConnection_on_off {
private static DbConnection_on_off single_instance = null;
	
	
	public static DbConnection_on_off getInstance()
    {
        if (single_instance == null)
            single_instance = new DbConnection_on_off();
 
        return single_instance;
    }
	
	public  Connection getConnection(){
    	
   	 Connection conn=null;
   	try {

   			conn = C3P0DataSource_on_off.getInstance() .getConnection() ;
   		
                 
   
       }
       catch(Exception e){
           System.out.println(e);
       }
       return conn;
   }
}
