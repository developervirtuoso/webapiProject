package common.database;

import java.sql.Connection;
import java.sql.DriverManager;

import mylibs.ApiCons;

public class C3P0DataSource_ReportData {

	private static C3P0DataSource_ReportData dataSource;
	// private ComboPooledDataSource comboPooledDataSource;

	private C3P0DataSource_ReportData() {
		try {

			/*
			 * comboPooledDataSource = new ComboPooledDataSource(); comboPooledDataSource
			 * .setDriverClass("com.mysql.jdbc.Driver");
			 * 
			 * 
			 * 
			 * 
			 * comboPooledDataSource.setJdbcUrl(
			 * "jdbc:mysql://localhost:3306/feedback_updated");
			 * comboPooledDataSource.setUser("root"); comboPooledDataSource.setPassword("");
			 */

			/*
			 * comboPooledDataSource .setJdbcUrl(
			 * "jdbc:mysql://myhoro.cxezv9drtmnx.us-east-2.rds.amazonaws.com:3306/feedback_app"
			 * ); comboPooledDataSource.setUser("MyHoro");
			 * comboPooledDataSource.setPassword("jaihanuman");
			 * comboPooledDataSource.setMinPoolSize(2);
			 * comboPooledDataSource.setAcquireIncrement(5);
			 * comboPooledDataSource.setMaxPoolSize(60);
			 * comboPooledDataSource.setMaxStatements(150);
			 * comboPooledDataSource.setIdleConnectionTestPeriod(30);
			 * comboPooledDataSource.setTestConnectionOnCheckout(true);
			 * comboPooledDataSource.setPreferredTestQuery("SELECT 1");
			 */

			/*
			 * comboPooledDataSource.setMinPoolSize(200);
			 * comboPooledDataSource.setAcquireIncrement(500);
			 * comboPooledDataSource.setMaxPoolSize(6000);
			 * comboPooledDataSource.setMaxStatements(15000);
			 * comboPooledDataSource.setIdleConnectionTestPeriod(3000);
			 * comboPooledDataSource.setTestConnectionOnCheckout(true);
			 * comboPooledDataSource.setPreferredTestQuery("SELECT 1");
			 * comboPooledDataSource.setMaxIdleTimeExcessConnections(120);
			 * comboPooledDataSource.setAcquireRetryDelay(10000);
			 * comboPooledDataSource.setAutoCommitOnClose(true);
			 */

			/*
			 * comboPooledDataSource.setMinPoolSize(2);
			 * comboPooledDataSource.setAcquireIncrement(5);
			 * comboPooledDataSource.setMaxPoolSize(20);
			 * comboPooledDataSource.setMaxStatements(50);
			 * comboPooledDataSource.setIdleConnectionTestPeriod(300);
			 * comboPooledDataSource.setTestConnectionOnCheckout(true);
			 * comboPooledDataSource.setPreferredTestQuery("SELECT 1");
			 */

		}
		/*
		 * catch (PropertyVetoException ex1) { ex1.printStackTrace(); }
		 */
		catch (Exception ex1) {
			ex1.printStackTrace();
		}
	}

	public static C3P0DataSource_ReportData getInstance() {
		if (dataSource == null)
			dataSource = new C3P0DataSource_ReportData();
		return dataSource;
	}

	
	public static int server_to_deploy = 2;
	//*******1******** http://sms.edealbus.com:6080 ---------OR--------- 49.50.64.122
	//*******2******** http://edealbus.com:8088     ---------OR--------- 49.50.105.175
	//*******0******** localhost                    ---------OR--------- 49.50.105.175
	
	public Connection getConnection() {
		Connection dbconn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			if(server_to_deploy==1)
			{
				 dbconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/report_spanel?autoReconnect=true", "root", "");
			}
			
			else if(server_to_deploy==2)
			{
				if(ApiCons.mysqlAccess==0) {
					dbconn = DriverManager.getConnection("jdbc:mysql://localhost:3130/itextwebv2reportdata?autoReconnect=true", "reports", "");
				}else {
					//Spanel
					dbconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/itextwebv2reportdata?autoReconnect=true", "reports", "");
				// End Spanel
				}
				
				 
				
			}
			else if(server_to_deploy==3)
			{
				 dbconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/spanel?autoReconnect=true", "root", "");
			}
			else
			{
				
				  dbconn = DriverManager.getConnection("jdbc:mysql://49.50.105.175:3306/report_spanel?autoReconnect=true", "spanel", "spanel");
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
