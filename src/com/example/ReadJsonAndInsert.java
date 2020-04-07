package com.example;

import java.sql.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.daoImpl.Smpp_DaoImpl;

public class ReadJsonAndInsert {
	public static void main(String[] args) {
		String JsonData="{\n" + 
				"	\"JSON\": {\n" + 
				"		\"draw\": 0,\n" + 
				"		\"recordsTotal\": 10,\n" + 
				"		\"recordsFiltered\": 10,\n" + 
				"		\"data\": [{\n" + 
				"			\"GatewayId\": 0,\n" + 
				"			\"GatewayName\": \"testR4\",\n" + 
				"			\"Reportdate\": \"2019-11-12T15:49:35.2941914\",\n" + 
				"			\"TotalCount\": 0,\n" + 
				"			\"Percentage\": \"0\",\n" + 
				"			\"MessageCount\": 0,\n" + 
				"			\"sReportdate\": \"11-12-2019\",\n" + 
				"			\"GatewayDetail\": \"testR4 <i class='fa fa-flag' style='color:red'></i>\"\n" + 
				"		}, {\n" + 
				"			\"GatewayId\": 0,\n" + 
				"			\"GatewayName\": \"vnsvns4\",\n" + 
				"			\"Reportdate\": \"2019-11-12T15:49:35.2947427\",\n" + 
				"			\"TotalCount\": 7414097,\n" + 
				"			\"Percentage\": \"73\",\n" + 
				"			\"MessageCount\": 5430941,\n" + 
				"			\"sReportdate\": \"11-12-2019\",\n" + 
				"			\"GatewayDetail\": \"vnsvns4 <i class='fa fa-flag' style='color:#c1c125'></i>\"\n" + 
				"		}, {\n" + 
				"			\"GatewayId\": 0,\n" + 
				"			\"GatewayName\": \"vnsvns5\",\n" + 
				"			\"Reportdate\": \"2019-11-12T15:49:35.2951409\",\n" + 
				"			\"TotalCount\": 0,\n" + 
				"			\"Percentage\": \"0\",\n" + 
				"			\"MessageCount\": 0,\n" + 
				"			\"sReportdate\": \"11-12-2019\",\n" + 
				"			\"GatewayDetail\": \"vnsvns5 <i class='fa fa-flag' style='color:red'></i>\"\n" + 
				"		}, {\n" + 
				"			\"GatewayId\": 0,\n" + 
				"			\"GatewayName\": \"TEST2\",\n" + 
				"			\"Reportdate\": \"2019-11-12T15:49:35.2955647\",\n" + 
				"			\"TotalCount\": 0,\n" + 
				"			\"Percentage\": \"0\",\n" + 
				"			\"MessageCount\": 0,\n" + 
				"			\"sReportdate\": \"11-12-2019\",\n" + 
				"			\"GatewayDetail\": \"TEST2 <i class='fa fa-flag' style='color:red'></i>\"\n" + 
				"		}, {\n" + 
				"			\"GatewayId\": 0,\n" + 
				"			\"GatewayName\": \"vnvns_dnd\",\n" + 
				"			\"Reportdate\": \"2019-11-12T15:49:35.2959335\",\n" + 
				"			\"TotalCount\": 5463,\n" + 
				"			\"Percentage\": \"56\",\n" + 
				"			\"MessageCount\": 3067,\n" + 
				"			\"sReportdate\": \"11-12-2019\",\n" + 
				"			\"GatewayDetail\": \"vnvns_dnd <i class='fa fa-flag' style='color:orange'></i>\"\n" + 
				"		}, {\n" + 
				"			\"GatewayId\": 0,\n" + 
				"			\"GatewayName\": \"bsnl_eb_smpp\",\n" + 
				"			\"Reportdate\": \"2019-11-12T15:49:35.2963048\",\n" + 
				"			\"TotalCount\": 0,\n" + 
				"			\"Percentage\": \"0\",\n" + 
				"			\"MessageCount\": 0,\n" + 
				"			\"sReportdate\": \"11-12-2019\",\n" + 
				"			\"GatewayDetail\": \"bsnl_eb_smpp <i class='fa fa-flag' style='color:red'></i>\"\n" + 
				"		}, {\n" + 
				"			\"GatewayId\": 0,\n" + 
				"			\"GatewayName\": \"vd\",\n" + 
				"			\"Reportdate\": \"2019-11-12T15:49:35.2967084\",\n" + 
				"			\"TotalCount\": 0,\n" + 
				"			\"Percentage\": \"0\",\n" + 
				"			\"MessageCount\": 0,\n" + 
				"			\"sReportdate\": \"11-12-2019\",\n" + 
				"			\"GatewayDetail\": \"vd <i class='fa fa-flag' style='color:red'></i>\"\n" + 
				"		}, {\n" + 
				"			\"GatewayId\": 0,\n" + 
				"			\"GatewayName\": \"vnsoftvf_tr2 \",\n" + 
				"			\"Reportdate\": \"2019-11-12T15:49:35.2970114\",\n" + 
				"			\"TotalCount\": 103049,\n" + 
				"			\"Percentage\": \"93\",\n" + 
				"			\"MessageCount\": 95956,\n" + 
				"			\"sReportdate\": \"11-12-2019\",\n" + 
				"			\"GatewayDetail\": \"vnsoftvf_tr2  <i class='fa fa-flag' style='color:green'></i>\"\n" + 
				"		}, {\n" + 
				"			\"GatewayId\": 0,\n" + 
				"			\"GatewayName\": \"akton\",\n" + 
				"			\"Reportdate\": \"2019-11-12T15:49:35.2975635\",\n" + 
				"			\"TotalCount\": 0,\n" + 
				"			\"Percentage\": \"0\",\n" + 
				"			\"MessageCount\": 0,\n" + 
				"			\"sReportdate\": \"11-12-2019\",\n" + 
				"			\"GatewayDetail\": \"akton <i class='fa fa-flag' style='color:red'></i>\"\n" + 
				"		}, {\n" + 
				"			\"GatewayId\": 0,\n" + 
				"			\"GatewayName\": \"Videocon_tr\",\n" + 
				"			\"Reportdate\": \"2019-11-12T15:49:35.2982137\",\n" + 
				"			\"TotalCount\": 0,\n" + 
				"			\"Percentage\": \"0\",\n" + 
				"			\"MessageCount\": 0,\n" + 
				"			\"sReportdate\": \"11-12-2019\",\n" + 
				"			\"GatewayDetail\": \"Videocon_tr <i class='fa fa-flag' style='color:red'></i>\"\n" + 
				"		}]\n" + 
				"	},\n" + 
				"	\"Response payload\": {\n" + 
				"		\"EDITOR_CONFIG\": {\n" + 
				"			\"text\": \"{\\\"draw\\\":0,\\\"recordsTotal\\\":10,\\\"recordsFiltered\\\":10,\\\"data\\\":[{\\\"GatewayId\\\":0,\\\"GatewayName\\\":\\\"testR4\\\",\\\"Reportdate\\\":\\\"2019-11-12T15:49:35.2941914\\\",\\\"TotalCount\\\":0,\\\"Percentage\\\":\\\"0\\\",\\\"MessageCount\\\":0,\\\"sReportdate\\\":\\\"11-12-2019\\\",\\\"GatewayDetail\\\":\\\"testR4 <i class='fa fa-flag' style='color:red'></i>\\\"},{\\\"GatewayId\\\":0,\\\"GatewayName\\\":\\\"vnsvns4\\\",\\\"Reportdate\\\":\\\"2019-11-12T15:49:35.2947427\\\",\\\"TotalCount\\\":7414097,\\\"Percentage\\\":\\\"73\\\",\\\"MessageCount\\\":5430941,\\\"sReportdate\\\":\\\"11-12-2019\\\",\\\"GatewayDetail\\\":\\\"vnsvns4 <i class='fa fa-flag' style='color:#c1c125'></i>\\\"},{\\\"GatewayId\\\":0,\\\"GatewayName\\\":\\\"vnsvns5\\\",\\\"Reportdate\\\":\\\"2019-11-12T15:49:35.2951409\\\",\\\"TotalCount\\\":0,\\\"Percentage\\\":\\\"0\\\",\\\"MessageCount\\\":0,\\\"sReportdate\\\":\\\"11-12-2019\\\",\\\"GatewayDetail\\\":\\\"vnsvns5 <i class='fa fa-flag' style='color:red'></i>\\\"},{\\\"GatewayId\\\":0,\\\"GatewayName\\\":\\\"TEST2\\\",\\\"Reportdate\\\":\\\"2019-11-12T15:49:35.2955647\\\",\\\"TotalCount\\\":0,\\\"Percentage\\\":\\\"0\\\",\\\"MessageCount\\\":0,\\\"sReportdate\\\":\\\"11-12-2019\\\",\\\"GatewayDetail\\\":\\\"TEST2 <i class='fa fa-flag' style='color:red'></i>\\\"},{\\\"GatewayId\\\":0,\\\"GatewayName\\\":\\\"vnvns_dnd\\\",\\\"Reportdate\\\":\\\"2019-11-12T15:49:35.2959335\\\",\\\"TotalCount\\\":5463,\\\"Percentage\\\":\\\"56\\\",\\\"MessageCount\\\":3067,\\\"sReportdate\\\":\\\"11-12-2019\\\",\\\"GatewayDetail\\\":\\\"vnvns_dnd <i class='fa fa-flag' style='color:orange'></i>\\\"},{\\\"GatewayId\\\":0,\\\"GatewayName\\\":\\\"bsnl_eb_smpp\\\",\\\"Reportdate\\\":\\\"2019-11-12T15:49:35.2963048\\\",\\\"TotalCount\\\":0,\\\"Percentage\\\":\\\"0\\\",\\\"MessageCount\\\":0,\\\"sReportdate\\\":\\\"11-12-2019\\\",\\\"GatewayDetail\\\":\\\"bsnl_eb_smpp <i class='fa fa-flag' style='color:red'></i>\\\"},{\\\"GatewayId\\\":0,\\\"GatewayName\\\":\\\"vd\\\",\\\"Reportdate\\\":\\\"2019-11-12T15:49:35.2967084\\\",\\\"TotalCount\\\":0,\\\"Percentage\\\":\\\"0\\\",\\\"MessageCount\\\":0,\\\"sReportdate\\\":\\\"11-12-2019\\\",\\\"GatewayDetail\\\":\\\"vd <i class='fa fa-flag' style='color:red'></i>\\\"},{\\\"GatewayId\\\":0,\\\"GatewayName\\\":\\\"vnsoftvf_tr2 \\\",\\\"Reportdate\\\":\\\"2019-11-12T15:49:35.2970114\\\",\\\"TotalCount\\\":103049,\\\"Percentage\\\":\\\"93\\\",\\\"MessageCount\\\":95956,\\\"sReportdate\\\":\\\"11-12-2019\\\",\\\"GatewayDetail\\\":\\\"vnsoftvf_tr2  <i class='fa fa-flag' style='color:green'></i>\\\"},{\\\"GatewayId\\\":0,\\\"GatewayName\\\":\\\"akton\\\",\\\"Reportdate\\\":\\\"2019-11-12T15:49:35.2975635\\\",\\\"TotalCount\\\":0,\\\"Percentage\\\":\\\"0\\\",\\\"MessageCount\\\":0,\\\"sReportdate\\\":\\\"11-12-2019\\\",\\\"GatewayDetail\\\":\\\"akton <i class='fa fa-flag' style='color:red'></i>\\\"},{\\\"GatewayId\\\":0,\\\"GatewayName\\\":\\\"Videocon_tr\\\",\\\"Reportdate\\\":\\\"2019-11-12T15:49:35.2982137\\\",\\\"TotalCount\\\":0,\\\"Percentage\\\":\\\"0\\\",\\\"MessageCount\\\":0,\\\"sReportdate\\\":\\\"11-12-2019\\\",\\\"GatewayDetail\\\":\\\"Videocon_tr <i class='fa fa-flag' style='color:red'></i>\\\"}]}\",\n" + 
				"			\"mode\": \"application/json\"\n" + 
				"		}\n" + 
				"	}\n" + 
				"}";
		try {
			JSONObject jsonObject=new JSONObject(JsonData);
			JSONObject JSON=jsonObject.getJSONObject("JSON");
			JSONArray data=JSON.getJSONArray("data");
			Smpp_DaoImpl smpp_DaoImpl=new Smpp_DaoImpl();
			for (int i = 0; i < data.length(); i++) {
				JSONObject valuej = data.getJSONObject(i);
				String GatewayName = valuej.getString("GatewayName");
				String Reportdate   = valuej.getString("Reportdate");
				int TotalCount = valuej.getInt("TotalCount");
				String Percentage   = valuej.getString("Percentage");
				int MessageCount = valuej.getInt("MessageCount");
				System.out.println(GatewayName + "==="+Reportdate +"==="+TotalCount+"==="+Percentage+"==="+MessageCount+"");
				
				smpp_DaoImpl.insertJsonData(GatewayName,Reportdate,TotalCount,Percentage,MessageCount);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
