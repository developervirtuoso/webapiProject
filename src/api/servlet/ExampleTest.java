package api.servlet;

import java.io.File;
import java.sql.Blob;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ExampleTest {

	public static void main(String[] args) {
		try {
		/*	Blob blob = "blob:http://localhost:8080/ce4b15e5-f50b-4aa1-913f-4747cb4ad394";
			   BufferedInputStream is = new BufferedInputStream(blob.getBinaryStream());
			   FileOutputStream fos = new FileOutputStream(file);
			   // you can set the size of the buffer
			   byte[] buffer = new byte[2048];
			   int r = 0;
			   while((r = is.read(buffer))!=-1) {
			      fos.write(buffer, 0, r);
			   }
			   fos.flush();
			   fos.close();
			   is.close();
			   blob.free();*/
			HttpResponse<JsonNode> response = Unirest.post("http://localhost:8080/webapi/uploadFile.jsp")
					.field("upload", new File("JNlu9bvLmtrbehqC4cSmmJj0Bodtt3I76Li8jORASKvKLzNZ5S6M7F0fTR8Thw5I8pPny8RlXLl8nLi9ktXfbQkdm7xXsGDN9qr6aKudxWEiUEskNjf8AxlFqqelY7NavhyMXje2uiBsA49"))
					.field("date", "2019-12-18")
					.field("servername", "panel")
					.asJson();
			String jsonData=response.getBody().toString();
			System.out.println(jsonData);
			
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
