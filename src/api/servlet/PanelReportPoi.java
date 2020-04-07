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
 * Servlet implementation class PanelReportPoi
 */
@WebServlet("/PanelReportPoi")
public class PanelReportPoi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PanelReportPoi() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String requested_date= request.getParameter("requested_date");
	  	 if(requested_date!=null){
	  		 if(requested_date.equalsIgnoreCase("null")) {
		  			requested_date=java.time.LocalDate.now().toString();
		  		 }
		  }else{
			  requested_date=java.time.LocalDate.now().toString();
		  }
	String Filename="SmppReport"+requested_date+".xlsx";
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		 
		response.setHeader("Content-Disposition", "attachment; filename="+Filename+"");

		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Datatypes in Java");
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
        JSONArray jsonArray = new JSONArray ();
       
        smpp_dao.all_users_count(requested_date,jsonArray,"");
  	 
  	  
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
	 
	 
  	for(int i=0; i<jsonArray.length();i++){
		 
		 
		 int percentage = 0;
		 
		 Row row = sheet.createRow(i+1);
			try{
				JSONObject showdata=jsonArray.getJSONObject(i);
				String current_companyname = showdata.getString("companyname");
				 int sub_count = Integer.parseInt(showdata.getString("submitted_count"));
				 int del_count = Integer.parseInt(showdata.getString("submitted_count"));
				 
				 Cell cell1 = row.createCell(0);
				 cell1.setCellValue((String) showdata.getString("requested_date"));
				 Cell cell2 = row.createCell(1);
				 cell2.setCellValue((String) showdata.getString("username"));
				 Cell cell3 = row.createCell(2);
				 cell3.setCellValue((String) showdata.getString("companyname"));
				 Cell cell4 = row.createCell(3);
				 cell4.setCellValue((String) showdata.getString("submitted_count"));
				 Cell cell5 = row.createCell(4);
				 cell5.setCellValue((String) showdata.getString("delivered_count"));
				percentage = Integer.parseInt(showdata.getString("delivered_count"))*100/Integer.parseInt(showdata.getString("submitted_count"));
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		
		 Cell cell6 = row.createCell(5);
		 cell6.setCellValue((String) (percentage+" %"));
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
