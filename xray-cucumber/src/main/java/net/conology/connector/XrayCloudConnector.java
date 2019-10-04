package net.conology.connector;

import static io.restassured.RestAssured.given;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.restassured.response.Response;

public class XrayCloudConnector {

	private String client_id;
	private String client_secret;
	private String jiraXrayUrl;
	private String accessToken;

	public XrayCloudConnector(String client_id, String client_secret, String jiraXrayUrl) {
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.jiraXrayUrl = jiraXrayUrl;
		authenticateXrayCloud();
	}

	public void authenticateXrayCloud() {

		// XRAY API URL
		//String xrayUrl = "https://xray.cloud.xpand-it.com/api/v1/authenticate";

		// XRAY Key a49df729a800d4936864b28f3aa2fcd6e519a62509376a624c9a30e1ec635142
		//String client_secrect = "a49df729a800d4936864b28f3aa2fcd6e519a62509376a624c9a30e1ec635142";

		// XRAY Customer ID 7D6A3E158E494C3F8DC9EC2BEBA00273
		//String client_id = "7D6A3E158E494C3F8DC9EC2BEBA00273";

		//POST authenticate
		HashMap<String,String> headerMapAuth = new HashMap<String,String>();
		headerMapAuth.put("Content-Type", "application/json");

		String body = "{ \"client_id\": \""+client_id+"\",\"client_secret\": \""+client_secret+"\" }";

		Response response = given().headers(headerMapAuth).body(body).post(jiraXrayUrl+"/api/v1/authenticate");

		// delete " at the beginning and the end
		StringBuilder builder 
		= new StringBuilder(response.getBody().asString()); 

		builder.deleteCharAt(builder.length()-1);
		builder.deleteCharAt(0);

		// return accessToken
		this.accessToken = builder.toString();
	}

	public void getGherkinFromTestCloud(String id) {

		// query xray to get feature of a test
		HashMap<String,String> headerMap = new HashMap<String,String>();

		headerMap.put("Content-Type", "application/json");
		headerMap.put("Authorization", "Bearer "+accessToken);

		// fetch result
		Response response =given().headers(headerMap).get(jiraXrayUrl+"/api/v1/export/cucumber?keys="+id);

		// write feature files to disk
		try {

			InputStream fis =  response.body().asInputStream();
			byte[] buffer = new byte[2048];


			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry ze;

			Path outDir = Paths.get("src/test/features/");


			while ((ze = zis.getNextEntry()) != null) {

				Path filePath = outDir.resolve(ze.getName());

				FileOutputStream fos = new FileOutputStream(filePath.toFile());
				BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);

				int len;
				while ((len = zis.read(buffer)) > 0) {
					bos.write(buffer, 0, len);
				}
				bos.flush();
				bos.close();
				fos.flush();
				fos.close();
			}
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public void publishResults() {
		
			// query xray to get feature of a test
				HashMap<String,String> headerMap = new HashMap<String,String>();

				headerMap.put("Content-Type", "application/json");
				headerMap.put("Authorization", "Bearer "+accessToken);
				
				// read results
				String body = new String();
				try {
					body = new String(Files.readAllBytes(Paths.get("target/report/cucumber.json")));
				} catch(Exception e) {
					System.out.println(e.toString());
				}
				System.out.println(body);

				// post results to xray
				Response response =given().headers(headerMap).body(body).log().all().post(jiraXrayUrl+"/api/v1/import/execution/cucumber");
		
	}

}
