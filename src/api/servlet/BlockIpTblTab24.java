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
 * Servlet implementation class BlockIpTblTab24
 */
@WebServlet("/BlockIpTblTab24")
public class BlockIpTblTab24 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BlockIpTblTab24() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	try {
           
            String ipaddress=request.getParameter("ipaddress");
            String dport=request.getParameter("dport");
            String toport=request.getParameter("toport");
            String comment=request.getParameter("comment");
	   
    
	
			  String cmd = "iptables -A PREROUTING -t nat -i eno2 -s "+ipaddress+" -p tcp --dport "+dport+" -m comment --comment "+comment+" -j REDIRECT --to-ports "+toport+"";
            Process p = Runtime.getRuntime().exec(cmd);
           
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
