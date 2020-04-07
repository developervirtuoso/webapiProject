package common.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class C3P0DataSource_Trafic {
	private static C3P0DataSource_Trafic dataSource;
	// private ComboPooledDataSource comboPooledDataSource;


	public static C3P0DataSource_Trafic getInstance() {
		if (dataSource == null)
			dataSource = new C3P0DataSource_Trafic();
		return dataSource;
	}

	
	
	//*******1******** http://sms.edealbus.com:6080 ---------OR--------- 49.50.64.122
	//*******2******** http://edealbus.com:8088     ---------OR--------- 49.50.105.175
	//*******0******** localhost                    ---------OR--------- 49.50.105.175
	
	public Connection getConnection() {
		Connection dbconn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			 dbconn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/report", "reports", "danish");
			 
			 
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
