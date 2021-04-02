package api.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class TestCMD {

	public static void main(String[] args) {
		try {
			String commandLine="sshpass -p 'sakshi' scp -P 2115  /usr/smpp_data/SETUP4/sgc_message_log_2021-02-11*.csv* sgoyal@122.160.97.195:/root/Desktop/Neeraj";
			//System.out.println("commandLine  "+commandLine);
			String commandArr[]=commandLine.split(",");
			Process process = Runtime.getRuntime().exec(commandArr);
			
			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			int i=1;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
				System.out.println("==>"+line);
				//jsonArray.put(jsonObject);
				i++;
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println("Success11111!");
				//System.out.println(output);
				
			} else {
				//abnormal...
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
}
