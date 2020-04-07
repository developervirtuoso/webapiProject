package common.database;

import java.sql.Connection;

public class DbConnection_All {
private static DbConnection_All single_instance = null;
	
	
	public static DbConnection_All getInstance()
    {
        if (single_instance == null)
            single_instance = new DbConnection_All();
 
        return single_instance;
    }
	
	public  Connection getConnection(int server_to_deploy){
    	
   	 Connection conn=null;
   	try {

   			conn = C3P0DataSource_All.getInstance().getConnection(server_to_deploy) ;
   		
                 
   
       }
       catch(Exception e){
           System.out.println(e);
       }
       return conn;
   }
}
