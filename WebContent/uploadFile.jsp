<%@page import="java.nio.file.*"%>
<%@page import="java.nio.file.Path"%>
<%@page import="api.daoImpl.Smpp_DaoImpl"%>
<%@page import="java.io.*"%>
<%@page import="java.util.Base64"%>
<%@page import="org.json.*"%>
<script src="https://www.gstatic.com/firebasejs/4.9.0/firebase.js"></script>
<script>
   // Initialize Firebase
var firebaseConfig = {
		  apiKey: "AIzaSyDPhLn9TP1vs6TUymAdq84Gh_gRL6MjSrc",
		    authDomain: "smpp-da54e.firebaseapp.com",
		    databaseURL: "https://smpp-da54e.firebaseio.com",
		    projectId: "smpp-da54e",
		    storageBucket: "smpp-da54e.appspot.com",
		    messagingSenderId: "341055895701",
		    appId: "1:341055895701:web:b5120e15ef7aeee3c3c39c",
		    measurementId: "G-RB7Z8EVV5R"
  };
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);
  firebase.analytics();
 
 </script>

<%

	JSONObject jobj = new JSONObject();
	String bytedata=request.getParameter("bytedata");
	String date=request.getParameter("date");
	String servername="1";
	String filename=request.getParameter("filename");
   try {
   	
		byte[] data = Base64.getDecoder().decode(bytedata);

		try
		{
			OutputStream stream = new FileOutputStream("/root/temp.txt");
		   stream.write(data);
		}
		catch (Exception e) 
		{
		   System.err.println("Couldn't write to file...");
		}
		System.out.println("bytedatabytedatabytedata"+bytedata);
   	String newdate="p"+date.replace("-", "");
   	
   	System.out.println("datedatedatedatedatedatedate"+date);
   	Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
   	boolean truncatestatus=smpp_dao.TruncateTable();
   	boolean load_data_status=smpp_dao.LoadDateFile();
   	int update_status=smpp_dao.UpdateButTAble(newdate);
	    	boolean checkstatus=false;
	    	if(update_status>0) {
	    		checkstatus=true;
	        }else {
	        	checkstatus=false;
	        }
	    	//SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");  
		  //  Date date1 = new Date();  
		   // String filename=formatter.format(date1)+"dlr_bpay.csv";
	    	System.out.println(filename); 
			  boolean upload_file_status=smpp_dao.uploadFile(filename);
			  Path temp = Files.move
				        (Paths.get("/tmp/"+filename), 
				        Paths.get("/usr/local/apache-tomcat-8.5.37/webapps/webapi/uploaded/"+filename));
				  
				        if(temp != null)
				        {
				            System.out.println("File renamed and moved successfully");
				        }
				        else
				        {
				            System.out.println("Failed to move the file");
				        } 
				        
				        int checkfile=smpp_dao.insertFileName(filename);
				        
   
   	
   		jobj.put("date", date);
   	   	jobj.put("truncatestatus", truncatestatus);
   	   	jobj.put("load_data_status", load_data_status);
   	   	jobj.put("update_status", checkstatus);
   	   	jobj.put("upload_file_status", upload_file_status);
   	   	jobj.put("filename", "uploaded/"+filename);
   	   	out.println(jobj.toString());
   	
} catch (Exception e) {
  e.printStackTrace();
}finally{
	if(servername.equalsIgnoreCase("1") || servername.equalsIgnoreCase("Panel")){
		out.print("1111111");
   		%>
   		<script type="text/javascript">
   		var databaseRef = firebase.database().ref('smppdb/panel/');
   	   var data = {
   		 			   status: false
   		 			   }
   		 			   
   		 			   var updates = {};
   		 			   updates['/smppdb/panel/panel/'] = data;
   		 			   firebase.database().ref().update(updates);
		</script>
   		<%
   	}else if(servername.equalsIgnoreCase("2") || servername.equalsIgnoreCase("SPanel")){
   		%>
   		<script type="text/javascript">
   		var databaseRef = firebase.database().ref('smppdb/spanel/');
   	   var data = {
   		 			   status: false
   		 			   }
   		 			   
   		 			   var updates = {};
   		 			   updates['/smppdb/spanel/spanel/'] = data;
   		 			   firebase.database().ref().update(updates);
		</script>
   		<%
   	}else if(servername.equalsIgnoreCase("3") || servername.equalsIgnoreCase("TPanel")){
   		%>
   		<script type="text/javascript">
   		var databaseRef = firebase.database().ref('smppdb/tpanel/');
   	   var data = {
   		 			   status: false
   		 			   }
   		 			   
   		 			   var updates = {};
   		 			   updates['/smppdb/tpanel/tpanel/'] = data;
   		 			   firebase.database().ref().update(updates);
		</script>
   		<%
   	}
}

%>