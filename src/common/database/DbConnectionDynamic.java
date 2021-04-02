package common.database;

import java.sql.Connection;

public class DbConnectionDynamic {

private static DbConnectionDynamic single_instance = null;
	
	
	public static DbConnectionDynamic getInstance()
    {
        if (single_instance == null)
            single_instance = new DbConnectionDynamic();
 
        return single_instance;
    }
	
	public  Connection getConnection(String url,String user,String pass){
    	
   	 Connection conn=null;
   	try {

   			conn = C3P0DataSourceDynamic.getInstance().getConnection(url,user,pass) ;
   		
                 
   
       }
       catch(Exception e){
           System.out.println(e);
       }
       return conn;
   }


}
