package api.servlet;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import api.daoImpl.Smpp_DaoImpl;

/**
 * Servlet implementation class TesynncReportpoi
 */
@WebServlet("/TesynncReportpoi")
public class TesynncReportpoi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TesynncReportpoi() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String requested_date= request.getParameter("requested_date");
		 String companyname1=request.getParameter("companyname");
	  	 if(requested_date!=null){
	  		 if(requested_date.equalsIgnoreCase("null")) {
	  			requested_date=java.time.LocalDate.now().toString();
	  		 }
			  
		  }else{
			  requested_date=java.time.LocalDate.now().toString();
		  }
	 	if(companyname1!=null){
	 		 if(companyname1.equalsIgnoreCase("null")) {
	 			companyname1="";
		  		 }
	 	  }else{
	 		 companyname1="";
	 		  
	 	  }
	  	 String Filename="SmppTesyncReport"+requested_date+".xlsx";
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		 
		response.setHeader("Content-Disposition", "attachment; filename="+Filename+"");

		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Tesync");
       /* Object[][] datatypes = {
                {"Datatype", "Type", "Size(in bytes)"},
                {"int", "Primitive", 2},
                {"float", "Primitive", 4},
                {"double", "Primitive", 8},
                {"char", "Primitive", 1},
                {"String", "Non-Primitive", "No fixed size"}
        };*/

        int rowNum = 0;
        Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
        
        JSONArray jsonArray1 = new JSONArray ();
     
        smpp_dao.getTesyncDbData(jsonArray1,requested_date,companyname1);
  	 System.out.println("nnnnnnnnnnnnnnnnn"+requested_date+""+jsonArray1.toString());
  	  
	 Row hrow = sheet.createRow(0);
	 Cell hcell1 = hrow.createCell(0);
	 hcell1.setCellValue((String) "Requested Date");
	 Cell hcell2 = hrow.createCell(1);
	 hcell2.setCellValue((String) "Username");
	 Cell hcell3 = hrow.createCell(2);
	 hcell3.setCellValue((String) "Company name");
	 Cell hcell4 = hrow.createCell(3);
	 hcell4.setCellValue((String) "Submitted Count");
	 Cell hcell5 = hrow.createCell(4);
	 hcell5.setCellValue((String) "Delivered Count");
	 Cell hcell6 = hrow.createCell(5);
	 hcell6.setCellValue((String) "Percentage");
	 int j=0;
	 for(int i=0; i<jsonArray1.length();i++){
		 
		 
			
		
			try{
				JSONObject showtsdata=jsonArray1.getJSONObject(i);
				  if(showtsdata.getString("client").equalsIgnoreCase("valueF") || showtsdata.getString("client").equalsIgnoreCase("vfpromo") || showtsdata.getString("client").equalsIgnoreCase("vnsoft_tr") || showtsdata.getString("client").equalsIgnoreCase("vnsoft_tr1") || showtsdata.getString("client").equalsIgnoreCase("vnsoftvt_pr")
						  || showtsdata.getString("client").equalsIgnoreCase("vnsvns") || showtsdata.getString("client").equalsIgnoreCase("netxcell")
						 || showtsdata.getString("client").equalsIgnoreCase("vfirst") || showtsdata.getString("client").equalsIgnoreCase("vnsvns2") || showtsdata.getString("client").equalsIgnoreCase("vnsvns3")
						 || showtsdata.getString("client").equalsIgnoreCase("VmobiT1") || showtsdata.getString("client").equalsIgnoreCase("VmobiT2") || showtsdata.getString("client").equalsIgnoreCase("VmobiT3") || showtsdata.getString("client").equalsIgnoreCase("VmobiPD1") || showtsdata.getString("client").equalsIgnoreCase("VmobiPD2")
						 ) {
					  Row row = sheet.createRow(i+1);
						 j++;
						 Cell cell1 = row.createCell(0);
						 cell1.setCellValue((String) showtsdata.getString("requested_date"));
						 Cell cell2 = row.createCell(1);
						 cell2.setCellValue((String) showtsdata.getString("client"));
						 Cell cell3 = row.createCell(2);
						 cell3.setCellValue((String) showtsdata.getString("client"));
						 Cell cell4 = row.createCell(3);
						 cell4.setCellValue((String) showtsdata.getString("submited"));
						 Cell cell5 = row.createCell(4);
						 cell5.setCellValue((String) showtsdata.getString("delivered"));
						 Cell cell6 = row.createCell(5);
						 cell6.setCellValue((String) showtsdata.getString("delivered_in_per"));
				  }
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		
		
 	}
  
  	 
        System.out.println("Creating excel");
      /*  for (Object[] datatype : datatypes) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }
        }*/

        try {
            /*FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            workbook.close();*/
        	ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        	workbook.write(outByteStream);

    		byte[] outArray = outByteStream.toByteArray();
    		OutputStream outStream = response.getOutputStream();
    		outStream.write(outArray);
    		outStream.flush();
    		//response.sendRedirect("view/userIndex.jsp?requested_date=2"+requested_date+"");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		//XSSFWorkbook wb = new XSSFWorkbook();
	//	XSSFSheet sheet = wb.createSheet();
		//XSSFRow row = sheet.createRow(0);
	//	XSSFCell cell = row.createCell(0);
	//	cell.setCellValue("Some text");

		/*ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		wb.write(outByteStream);

		byte[] outArray = outByteStream.toByteArray();
		OutputStream outStream = response.getOutputStream();
		outStream.write(outArray);
		outStream.flush();*/
    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
