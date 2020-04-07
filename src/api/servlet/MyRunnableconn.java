package api.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.TimerTask;

import api.daoImpl.Smpp_DaoImpl;

public class MyRunnableconn extends TimerTask implements Runnable {

	 private volatile String value;

     @Override
     public void run() {
    	 Random rand = new Random();
    	 int id = rand.nextInt(10000);
        value = id+"";
     }

     public String getValue(String id) throws IOException {
    	 String s = null;
		 String[] cl_ip={"218.248.82.6","218.248.82.3","218.248.82.2","59.91.63.147"}; 
		 for(int i=0;i<=3;i++){

    	 String[] cmd = {"/bin/sh", "-c", "netstat -tlnap |grep "+cl_ip[i]+" |grep ESTABLISHED  |wc -l"};
    	           Process p = Runtime.getRuntime().exec(cmd);
    	           
    	           BufferedReader stdInput = new BufferedReader(new
    	                InputStreamReader(p.getInputStream()));

    	           BufferedReader stdError = new BufferedReader(new
    	                InputStreamReader(p.getErrorStream()));
    	s = stdInput.readLine();
		 }
         return id+"="+value;
     }
    
}

