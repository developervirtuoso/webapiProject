package api.servlet;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ServerDatetime {
	
	public String getServerDateTime(){
		
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String server_datetime = String.valueOf(sdf.format(cal.getTime()));
        System.out.println("server_datetime=>>"+server_datetime);
        
       cal.add(Calendar.HOUR, 5);
        cal.add(Calendar.MINUTE, 30);
        cal.add(Calendar.SECOND, 00);
        server_datetime = String.valueOf(sdf.format(cal.getTime()));
        System.out.println("server_datetime=>>"+server_datetime);
        
		return server_datetime;
	}
	
	
	
public String getServerDate(){
		
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String server_datetime = String.valueOf(sdf.format(cal.getTime()));
        System.out.println("server_date=>>"+server_datetime);
        
        cal.add(Calendar.HOUR, 5);
        cal.add(Calendar.MINUTE, 30);
        cal.add(Calendar.SECOND, 00);
        server_datetime = String.valueOf(sdf.format(cal.getTime()));
        System.out.println("server_date=>>"+server_datetime);
        
		return server_datetime;
	}


public String getServerDateOnly(){
	
	Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String server_datetime = String.valueOf(sdf.format(cal.getTime()));
    System.out.println("server_date=>>"+server_datetime);
    

    
	return server_datetime;
}


public String getServerTime(){
	
	Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
    String server_datetime = String.valueOf(sdf.format(cal.getTime()));
    System.out.println("server_time=>>"+server_datetime);
    
    cal.add(Calendar.HOUR, 5);
    cal.add(Calendar.MINUTE, 30);
    cal.add(Calendar.SECOND, 00);
    server_datetime = String.valueOf(sdf.format(cal.getTime()));
    System.out.println("server_time=>>"+server_datetime);
    
	return server_datetime;
}

}
