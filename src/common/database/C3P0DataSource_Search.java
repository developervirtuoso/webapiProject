package common.database;

import java.sql.Connection;
import java.sql.DriverManager;

import mylibs.ApiCons;

public class C3P0DataSource_Search {
	private static C3P0DataSource_Search dataSource;
	// private ComboPooledDataSource comboPooledDataSource;


	public static C3P0DataSource_Search getInstance() {
		if (dataSource == null)
			dataSource = new C3P0DataSource_Search();
		return dataSource;
	}

	
	
	//*******1******** http://sms.edealbus.com:6080 ---------OR--------- 49.50.64.122
	//*******2******** http://edealbus.com:8088     ---------OR--------- 49.50.105.175
	//*******0******** localhost                    ---------OR--------- 49.50.105.175
	
	public Connection getConnection() {
		Connection dbconn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			if(ApiCons.mysqlAccess==0) {
				dbconn = DriverManager.getConnection("jdbc:mysql://localhost:3130/itextwebv2smpp", "reports", "");
			}else {
				//Spanel
				dbconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/itextwebv2smpp?autoReconnect=true", "reports", "");
			// End Spanel
			}
			
			 
			
			 
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
