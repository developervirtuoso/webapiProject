package superAdmin.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import all.beans.SmppUser;
import all.beans.SuperAdmin;
import api.daoImpl.Smpp_DaoImpl;


/**
 * Servlet implementation class smppSignIn
 */
@WebServlet("/smppSignIn")
public class smppSignIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 final static Logger logger = Logger.getLogger(smppSignIn.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public smppSignIn() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        logger.info("111111111111");
        int id=0;
        Smpp_DaoImpl smpp_DaoImpl=new Smpp_DaoImpl();
        Boolean status = false;
        
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        SmppUser smppUser=new SmppUser();
         smppUser.setName(username);
         smppUser.setPassword(password);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a");
        String logintime = String.valueOf(sdf.format(cal.getTime()));
        try {
        	logger.info("2222222222222");
      	status = smpp_DaoImpl.checkSmppUser(smppUser);
      	System.out.println("status"+status);
      	logger.info("33333333333");
        	
        	
        	 if (status == true) {
        		 logger.info("444444444444");
        		 
        		 logger.info("55555555555");
                 HttpSession session = request.getSession();

                 session.setAttribute("id", smppUser.getId());
                 session.setAttribute("username", username);
                 session.setAttribute("password", password);
                 
                 response.sendRedirect("index1.jsp");
             
               out.println("</body>");
               out.println("</html>");
        		 
        	 }else{ 
        		 
        		 status = false;
        		 
        		 String message = "Login Failed,Please try again";
        		 request.setAttribute("message", message);
                 request.getRequestDispatcher("smppLogin.jsp").include(request, response);
                 
                   out.println("</body>");
                   out.println("</html>");
        	 }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

}
