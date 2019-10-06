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

import org.apache.commons.codec.binary.Base64;

import io.restassured.response.Response;

public class XrayServerConnector implements IXrayConnector {

	private String auth;
	private String jiraXraylUrl;
	
	
	public XrayServerConnector(String user, String password, String jiraXrayUrl) {
		this.auth = new String(Base64.encodeBase64((user + ":" + password).getBytes()));
		this.jiraXraylUrl = jiraXrayUrl;
	}
	
	@Override
	public void getGherkinFromTestCloud(String id) {
		
		//create header
		HashMap<String,String> headerMap = new HashMap<String,String>();

		headerMap.put("Authorization", "Basic "+auth);
		headerMap.put("Content-Type", "application/json");
		
		// get Issue
		Response response = given().headers(headerMap).log().all().get(jiraXraylUrl+"/rest/raven/1.0/export/test?keys="+id);
		
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

	@Override
	public void publishResults() {
		
		//create header
				HashMap<String,String> headerMap = new HashMap<String,String>();

				headerMap.put("Authorization", "Basic "+auth);
				headerMap.put("Content-Type", "application/json");
		
		// read results
		String body = new String();
		try {
			body = new String(Files.readAllBytes(Paths.get("target/report/cucumber.json")));
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		System.out.println(body);

		// post results to xray
		Response response =given().headers(headerMap).body(body).log().all().post(jiraXraylUrl+"/rest/raven/1.0/import/execution/cucumber");
		
	}

}
