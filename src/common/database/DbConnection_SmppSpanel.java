package common.database;

import java.sql.Connection;

public class DbConnection_SmppSpanel {
private static DbConnection_SmppSpanel single_instance = null;
	
	
	public static DbConnection_SmppSpanel getInstance()
    {
        if (single_instance == null)
            single_instance = new DbConnection_SmppSpanel();
 
        return single_instance;
    }
	
	public  Connection getConnection(){
    	
   	 Connection conn=null;
   	try {

   			conn = C3P0DataSource_SmppSpanel.getInstance().getConnection() ;
   		
                 
   
       }
       catch(Exception e){
           System.out.println(e);
       }
       return conn;
   }


}
