package common.database;

import java.sql.Connection;

public class DbConnection_Trafic {
private static DbConnection_Trafic single_instance = null;
	
	
	public static DbConnection_Trafic getInstance()
    {
        if (single_instance == null)
            single_instance = new DbConnection_Trafic();
 
        return single_instance;
    }
	
	public  Connection getConnection(){
    	
   	 Connection conn=null;
   	try {

   			conn = C3P0DataSource_Trafic.getInstance() .getConnection() ;
   		
                 
   
       }
       catch(Exception e){
           System.out.println(e);
       }
       return conn;
   }
}
