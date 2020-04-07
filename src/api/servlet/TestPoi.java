package api.servlet;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.daoImpl.Smpp_DaoImpl;

/**
 * Servlet implementation class TestPoi
 */
@WebServlet("/TestPoi")
public class TestPoi extends HttpServlet {
private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestPoi() {
        super();
        // TODO Auto-generated constructor stub
    }

/**
* @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
*/
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
String requested_date= request.getParameter("requested_date");
  if(requested_date!=null){
 
 }else{
 requested_date=java.time.LocalDate.now().toString();
 }
String Filename="SmppReport"+requested_date+".xlsx";
response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
response.setHeader("Content-Disposition", "attachment; filename="+Filename+"");
Smpp_DaoImpl smpp_dao = new Smpp_DaoImpl();
//JSONArray jsonArray = new JSONArray ();
//smpp_dao.all_users_count(requested_date,jsonArray);
//smpp_dao.getUsernameAndId(jsonArray);
String arraydata[]= {"glomex","mobisoft","mobisoft1","mobisoft2","mobisoft_tr","mobisoft_pr","mobtexting","routemobile","sphere_p1","sphere_p2","sphere_p3","sphere_p4","sphere_t1","sphere_t2","sphere_t3","sphere_t4","sphere_t5","tagg","routemobile_t1","routemobile_t2","routemobile_t3"};
XSSFWorkbook workbook = new XSSFWorkbook();
Smpp_DaoImpl daoImpl=new Smpp_DaoImpl();
for(int i=0; i<1;i++) {
	try {
		//JSONObject jsonObject=arraydata.getJSONObject(i);
		String companyid=arraydata[i];
	XSSFSheet sheet = workbook.createSheet(arraydata[i]);
       /* Object[][] datatypes = {
                {"Datatype", "Type", "Size(in bytes)"},
                {"int", "Primitive", 2},
                {"float", "Primitive", 4},
                {"double", "Primitive", 8},
                {"char", "Primitive", 1},
                {"String", "Non-Primitive", "No fixed size"}
        };*/

        int rowNum = 0;
       
			Row hrow = sheet.createRow(0);
			CellStyle cellStyle = hrow.getSheet().getWorkbook().createCellStyle();
			 cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			 cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());  
			 cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);  
			Cell hcell1 = hrow.createCell(0);
			hcell1.setCellValue((String) "Date");
			sheet.addMergedRegion(new CellRangeAddress(0,2,0,0));
			hcell1.setCellStyle(cellStyle);
			cellStyle = hrow.getSheet().getWorkbook().createCellStyle();
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());  
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			Cell hcell2 = hrow.createCell(1);
			hcell2.setCellValue((String) "Onnet Details");
			hcell2.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(0,0,1,15));  
			Cell hcell3 = hrow.createCell(16);
			hcell3.setCellValue((String) "Offnet Details");
			hcell3.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(0,0,16,30));
			
			Row title = sheet.createRow(1);
			CellStyle tcellStyle = title.getSheet().getWorkbook().createCellStyle();
			tcellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			Cell t1 = title.createCell(1);
			t1.setCellValue((String) "");
			Cell t2 = title.createCell(2);
			t2.setCellValue((String) "Delivery");
			t2.setCellStyle(tcellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,2,3));
			Cell t3 = title.createCell(4);
			t3.setCellValue((String) "Absent/Unknown Subscriber");
			t3.setCellStyle(tcellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,4,5));
			Cell t4 = title.createCell(6);
			t4.setCellValue((String) "Facility not Supported");
			t4.setCellStyle(tcellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,6,7));
			Cell t5 = title.createCell(8);
			t5.setCellValue((String) "FSM");
			t5.setCellStyle(tcellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,8,9));
			Cell t6 = title.createCell(10);
			t6.setCellValue((String) "SRI");
			t6.setCellStyle(tcellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,10,11));
			Cell t7 = title.createCell(12);
			t7.setCellValue((String) "Undelivered");
			t7.setCellStyle(tcellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,12,13));
			Cell t8 = title.createCell(14);
			t8.setCellValue((String) "System Failure");
			t8.setCellStyle(tcellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,14,15));
			Cell t9 = title.createCell(16);
			t9.setCellValue((String) "");
			t9.setCellStyle(tcellStyle);
			Cell t10 = title.createCell(17);
			t10.setCellValue((String) "Delivery");
			t10.setCellStyle(tcellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,17,18));
			Cell t11 = title.createCell(19);
			t11.setCellValue((String) "Absent/Unknown Subscriber");
			t11.setCellStyle(tcellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,19,20));
			Cell t12 = title.createCell(21);
			t12.setCellValue((String) "Facility not Supported");
			t12.setCellStyle(tcellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,21,22));
			Cell t13 = title.createCell(23);
			t13.setCellValue((String) "FSM");
			t13.setCellStyle(tcellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,23,24));
			Cell t14 = title.createCell(25);
			t14.setCellValue((String) "SRI");
			t14.setCellStyle(tcellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,25,26));
			Cell t15 = title.createCell(27);
			t15.setCellValue((String) "Undelivered");
			t15.setCellStyle(tcellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,27,28));
			Cell t16 = title.createCell(29);
			t16.setCellValue((String) "System Failure");
			t16.setCellStyle(tcellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,29,30));
			
			Row type = sheet.createRow(2);
			CellStyle tys = type.getSheet().getWorkbook().createCellStyle();
			tys.setAlignment(tys.ALIGN_CENTER);
			tys.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());  
			tys.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			Cell ty1 = type.createCell(1);
			ty1.setCellValue((String) "Total Pushes");
			ty1.setCellStyle(tys);
			Cell ty2 = type.createCell(2);
			ty2.setCellValue((String) "Counts");
			ty2.setCellStyle(tys);
			Cell ty3 = type.createCell(3);
			ty3.setCellValue((String) "Ratio");
			Cell ty4 = type.createCell(4);
			ty4.setCellValue((String) "Counts");
			Cell ty5 = type.createCell(5);
			ty5.setCellValue((String) "Ratio");
			Cell ty6 = type.createCell(6);
			ty6.setCellValue((String) "Counts");
			Cell ty7 = type.createCell(7);
			ty7.setCellValue((String) "Ratio");
			Cell ty8 = type.createCell(8);
			ty8.setCellValue((String) "Counts");
			Cell ty9 = type.createCell(9);
			ty9.setCellValue((String) "Ratio");
			
			Cell ty10 = type.createCell(10);
			ty10.setCellValue((String) "Counts");
			Cell ty11 = type.createCell(11);
			ty11.setCellValue((String) "Ratio");
			Cell ty12 = type.createCell(12);
			ty12.setCellValue((String) "Counts");
			Cell ty13 = type.createCell(13);
			ty13.setCellValue((String) "Ratio");
			Cell ty14 = type.createCell(14);
			ty14.setCellValue((String) "Counts");
			Cell ty15 = type.createCell(15);
			ty15.setCellValue((String) "Ratio");
			Cell ty31 = type.createCell(16);
			ty31.setCellValue((String) "Total Pushes");
			Cell ty16 = type.createCell(17);
			ty16.setCellValue((String) "Counts");
			Cell ty17 = type.createCell(18);
			ty17.setCellValue((String) "Ratio");
			Cell ty18 = type.createCell(19);
			ty18.setCellValue((String) "Counts");
			Cell ty19 = type.createCell(20);
			ty19.setCellValue((String) "Ratio");
			Cell ty20 = type.createCell(21);
			ty20.setCellValue((String) "Counts");
			Cell ty21 = type.createCell(22);
			ty21.setCellValue((String) "Ratio");
			Cell ty22 = type.createCell(23);
			ty22.setCellValue((String) "Counts");
			Cell ty23 = type.createCell(24);
			ty23.setCellValue((String) "Ratio");
			Cell ty24 = type.createCell(25);
			ty24.setCellValue((String) "Counts");
			Cell ty25 = type.createCell(26);
			ty25.setCellValue((String) "Ratio");
			Cell ty26 = type.createCell(27);
			ty26.setCellValue((String) "Counts");
			Cell ty27 = type.createCell(27);
			ty27.setCellValue((String) "Ratio");
			Cell ty28 = type.createCell(28);
			ty28.setCellValue((String) "Counts");
			Cell ty29 = type.createCell(29);
			ty29.setCellValue((String) "Ratio");
			Cell ty30 = type.createCell(30);
			ty30.setCellValue((String) "Counts");
			ty3.setCellStyle(tys);
			ty4.setCellStyle(tys);
			ty5.setCellStyle(tys);
			ty6.setCellStyle(tys);
			ty7.setCellStyle(tys);
			ty8.setCellStyle(tys);
			ty9.setCellStyle(tys);
			ty10.setCellStyle(tys);
			ty11.setCellStyle(tys);
			ty12.setCellStyle(tys);
			ty13.setCellStyle(tys);
			ty14.setCellStyle(tys);
			ty15.setCellStyle(tys);
			ty16.setCellStyle(tys);
			ty17.setCellStyle(tys);
			ty18.setCellStyle(tys);
			ty19.setCellStyle(tys);
			ty20.setCellStyle(tys);
			ty21.setCellStyle(tys);
			ty22.setCellStyle(tys);
			ty23.setCellStyle(tys);
			ty24.setCellStyle(tys);
			ty25.setCellStyle(tys);
			ty26.setCellStyle(tys);
			ty27.setCellStyle(tys);
			ty28.setCellStyle(tys);
			ty29.setCellStyle(tys);
			ty30.setCellStyle(tys);
			ty31.setCellStyle(tys);
			
		   	//String startdate=daoImpl.createBeforeMonthDate();
		   //	String currentdate=java.time.LocalDate.now().toString();
		 	String startdate="2019-09-01";
		   	String currentdate="2019-09-30";
			LocalDate start = LocalDate.parse(startdate),end  = LocalDate.parse(currentdate);
				  
			LocalDate next = start.minusDays(1);
			
			int il=4;
			while ((next = next.plusDays(1)).isBefore(end.plusDays(1))) {
				Row rowl = sheet.createRow(il);
				CellStyle cellStylel = rowl.getSheet().getWorkbook().createCellStyle();
				cellStylel.setAlignment(cellStylel.ALIGN_CENTER);
				cellStylel.setFillForegroundColor(IndexedColors.AQUA.getIndex());  
				cellStylel.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				String dates=next+"";
				JSONObject jsonObject2=new JSONObject();
				smpp_dao.getReportDataByDate(dates,jsonObject2,companyid);
				Cell l1 = rowl.createCell(0);
				l1.setCellValue((String) dates);
				l1.setCellStyle(cellStylel);
				
				Cell l2 = rowl.createCell(1);
				l2.setCellValue((String) jsonObject2.getString("onpusher"));
				l2.setCellStyle(cellStylel);
				Cell l3 = rowl.createCell(2);
				l3.setCellValue((String) jsonObject2.getString("ondelivercount"));
				l3.setCellStyle(cellStylel);
				Cell l4 = rowl.createCell(3);
				l4.setCellValue((String) jsonObject2.getString("ondeliverratio"));
				l4.setCellStyle(cellStylel);
				Cell l5 = rowl.createCell(4);
				l5.setCellValue((String) jsonObject2.getString("onabsentcount"));
				l5.setCellStyle(cellStylel);
				Cell l6 = rowl.createCell(5);
				l6.setCellValue((String) jsonObject2.getString("onabsentratio"));
				l6.setCellStyle(cellStylel);
				Cell l7 = rowl.createCell(6);
				l7.setCellValue((String) jsonObject2.getString("onfaccount"));
				l7.setCellStyle(cellStylel);
				Cell l8 = rowl.createCell(7);
				l8.setCellValue((String) jsonObject2.getString("onfacratio"));
				l8.setCellStyle(cellStylel);
				Cell l9 = rowl.createCell(8);
				l9.setCellValue((String) jsonObject2.getString("onfsmcount"));
				l9.setCellStyle(cellStylel);
				Cell l10 = rowl.createCell(9);
				l10.setCellValue((String) jsonObject2.getString("onfsmratio"));
				l10.setCellStyle(cellStylel);
				Cell l11 = rowl.createCell(10);
				l11.setCellValue((String) jsonObject2.getString("onsricount"));
				l11.setCellStyle(cellStylel);
				Cell l12 = rowl.createCell(11);
				l12.setCellValue((String) jsonObject2.getString("onsriratio"));
				l12.setCellStyle(cellStylel);
				Cell l13 = rowl.createCell(12);
				l13.setCellValue((String) jsonObject2.getString("onundelivercount"));
				l13.setCellStyle(cellStylel);
				Cell l14 = rowl.createCell(13);
				l14.setCellValue((String) jsonObject2.getString("onundeliverratio"));
				l14.setCellStyle(cellStylel);
				Cell l15 = rowl.createCell(14);
				l15.setCellValue((String) jsonObject2.getString("onsystemfailcount"));
				l15.setCellStyle(cellStylel);
				Cell l16 = rowl.createCell(15);
				l16.setCellValue((String) jsonObject2.getString("onsystemfailratio"));
				l16.setCellStyle(cellStylel);
				
				Cell l17 = rowl.createCell(16);
				l17.setCellValue((String) jsonObject2.getString("offpusher"));
				l17.setCellStyle(cellStylel);
				Cell l18 = rowl.createCell(17);
				l18.setCellValue((String) jsonObject2.getString("offdelivercount"));
				l18.setCellStyle(cellStylel);
				Cell l19 = rowl.createCell(18);
				l19.setCellValue((String) jsonObject2.getString("offdeliverratio"));
				l19.setCellStyle(cellStylel);
				Cell l20 = rowl.createCell(19);
				l20.setCellValue((String) jsonObject2.getString("offabsentcount"));
				l20.setCellStyle(cellStylel);
				Cell l21 = rowl.createCell(20);
				l21.setCellValue((String) jsonObject2.getString("offabsentratio"));
				l21.setCellStyle(cellStylel);
				Cell l22 = rowl.createCell(21);
				l22.setCellValue((String) jsonObject2.getString("offfaccount"));
				l22.setCellStyle(cellStylel);
				Cell l23 = rowl.createCell(22);
				l23.setCellValue((String) jsonObject2.getString("offfacratio"));
				l23.setCellStyle(cellStylel);
				Cell l24 = rowl.createCell(23);
				l24.setCellValue((String) jsonObject2.getString("offfsmcount"));
				l24.setCellStyle(cellStylel);
				Cell l25 = rowl.createCell(24);
				l25.setCellValue((String) jsonObject2.getString("offfsmratio"));
				l25.setCellStyle(cellStylel);
				Cell l26 = rowl.createCell(25);
				l26.setCellValue((String) jsonObject2.getString("onsricount"));
				l26.setCellStyle(cellStylel);
				Cell l27 = rowl.createCell(26);
				l27.setCellValue((String) jsonObject2.getString("offsriratio"));
				l27.setCellStyle(cellStylel);
				Cell l28 = rowl.createCell(27);
				l28.setCellValue((String) jsonObject2.getString("offundelivercount"));
				l28.setCellStyle(cellStylel);
				Cell l29 = rowl.createCell(28);
				l29.setCellValue((String) jsonObject2.getString("offundeliverratio"));
				l29.setCellStyle(cellStylel);
				Cell l30 = rowl.createCell(29);
				l30.setCellValue((String) jsonObject2.getString("offsystemfailcount"));
				l30.setCellStyle(cellStylel);
				Cell l31 = rowl.createCell(30);
				l31.setCellValue((String) jsonObject2.getString("offsystemfailratio"));
				l31.setCellStyle(cellStylel);
				
			   // System.out.println(next);
			    il++;
			}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			  /*for(int i=0; i<jsonArray.length();i++){
			
			
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
			  } */
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
			// XSSFSheet sheet = wb.createSheet();
			//XSSFRow row = sheet.createRow(0);
			// XSSFCell cell = row.createCell(0);
			// cell.setCellValue("Some text");
			
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
