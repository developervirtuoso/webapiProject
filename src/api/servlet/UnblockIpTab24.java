package api.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import api.daoImpl.Smpp_DaoImpl;

/**
 * Servlet implementation class UnblockIpTab24
 */
@WebServlet("/UnblockIpTab24")
public class UnblockIpTab24 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UnblockIpTab24() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	try {
            String s = null;
            String ipAddress=request.getParameter("ipAddress");
            String dport=request.getParameter("dport");
            String toPort=request.getParameter("toPort");
            String comment=request.getParameter("comment");
            
            Process p = Runtime.getRuntime().exec("iptables -D PREROUTING -t nat -i eno2 -s "+ipAddress+" -p tcp --dport "+dport+" -m comment --comment "+comment+" -j REDIRECT --to-ports "+toPort+"");
            Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
            daoImpl.createIpTab24Oct();
	   response.sendRedirect("SconnIpTab24Oct.jsp");
    	}
    	
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            //System.exit(-1);
        
	   }
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request, response);
	}

}
