package common.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class C3P0DataSource_All {
	private static C3P0DataSource_All dataSource;


	public static C3P0DataSource_All getInstance() {
		if (dataSource == null)
			dataSource = new C3P0DataSource_All();
		return dataSource;
	}

	
	
	//*******1******** http://sms.edealbus.com:6080 ---------OR--------- 49.50.64.122
	//*******2******** http://edealbus.com:8088     ---------OR--------- 49.50.105.175
	//*******0******** localhost                    ---------OR--------- 49.50.105.175
	
	public Connection getConnection(int server_to_deploy) {
		Connection dbconn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			if(server_to_deploy==1)
			{
				 dbconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Report_Data?autoReconnect=true", "root", "");	
			}
			
			else if(server_to_deploy==2)
			{
				dbconn = DriverManager.getConnection("jdbc:mysql://localhost:3130/report?autoReconnect=true", "reports", "");
			}
			else if(server_to_deploy==3)
			{
				dbconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/count_sms?autoReconnect=true", "root", "");
			}
			else
			{
				
				  dbconn = DriverManager.getConnection("jdbc:mysql://49.50.105.175:3306/report_panel?autoReconnect=true", "panel", "panel");
			}
			
			/*
			 * dbconn = DriverManager.getConnection(
			 * "jdbc:mysql://localhost:3306/smpp_support_mail?autoReconnect=true", "root",
			 * "");
			 */
			

			  
			  
			 
			 
			
			/*
			 * dbconn = DriverManager.getConnection(
			 * "jdbc:mysql://49.50.64.122:3130/report?autoReconnect=true", "smppuser",
			 * "smppreport@123");
			 */
			 
			/*
			 * dbconn = DriverManager.getConnection(
			 * "jdbc:mysql://49.50.64.122:3130/report?autoReconnect=true", "misreports",
			 * "misrep");
			 */
			 
			 
			 
			 
			 
			 
			 
		} catch (Exception ex) {
			System.out.println("Exception in DBConnection java file of fun" + ex);
		}
		return dbconn;

		/*
		 * Connection con = null; try { con = comboPooledDataSource.getConnection(); }
		 * catch (SQLException e) { e.printStackTrace(); } return con;
		 */
	}
}
