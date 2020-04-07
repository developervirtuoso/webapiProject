package mylibs;

import api.daoImpl.Smpp_DaoImpl;

public class MyRunnable_SyncEmail implements Runnable {

    private String mobile_no;
    private String txt_msg;
    
  
    
    
    
    

    public MyRunnable_SyncEmail() {
       
        
       
    }

    public void run() {
    	Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
    	smpp_dao.checkNewEmail();
        // code in the other thread, can reference "var" variable
    }
}
