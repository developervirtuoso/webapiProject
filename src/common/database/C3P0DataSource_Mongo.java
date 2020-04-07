package common.database;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class C3P0DataSource_Mongo {

	private static C3P0DataSource_Mongo dataSource;
	// private ComboPooledDataSource comboPooledDataSource;

	
		

	public static C3P0DataSource_Mongo getInstance() {
		if (dataSource == null)
			dataSource = new C3P0DataSource_Mongo();
		return dataSource;
	}

	
	
	public MongoClient getConnection() {
		MongoClient	 dbconn = null;
		try {
			
			dbconn = new MongoClient("localhost", 27017);
		
			 
				
			 
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
