package common.database;

import java.sql.Connection;

public class DbConnection_ReportData {
private static DbConnection_ReportData single_instance = null;
	
	
	public static DbConnection_ReportData getInstance()
    {
        if (single_instance == null)
            single_instance = new DbConnection_ReportData();
 
        return single_instance;
    }
	
	public  Connection getConnection(){
    	
   	 Connection conn=null;
   	try {

   			conn = C3P0DataSource_ReportData.getInstance().getConnection() ;
   		
                 
   
       }
       catch(Exception e){
           System.out.println(e);
       }
       return conn;
   }


}
