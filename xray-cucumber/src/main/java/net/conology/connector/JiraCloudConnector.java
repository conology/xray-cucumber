package net.conology.connector;

import static io.restassured.RestAssured.given;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import io.restassured.response.Response;

public class JiraCloudConnector {
	
	private String jiraUrl;
	private String username;
	private String accessToken;
	
	public JiraCloudConnector(String username, String accessToken, String jiraUrl) {
		this.jiraUrl = jiraUrl;
		this.username = username;
		this.accessToken = accessToken;
	}
	
	public void getAutomatedTestsofTestExecution(String testExecutionId) {
		
		JiraRestClient restClient = getJiraRestClient();
		Issue issue = restClient.getIssueClient().getIssue(testExecutionId).claim();
		// TODO
		
	}
	
	private JiraRestClient getJiraRestClient() {
		return new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(getJiraUri(), this.username,
				this.accessToken);
	}
	
	private URI getJiraUri() {
		return URI.create(this.jiraUrl);
	}
	
	
	public void registerWebhook() {
		
		// reading: https://developer.atlassian.com/cloud/jira/platform/rest/v3/#api-rest-api-3-webhook-refresh-put
		// webhooks are only allowed for apps build with atlassian!
		
		// authenticate
		String auth = new String(Base64.encodeBase64((username + ":" + accessToken).getBytes()));
		System.out.println(auth);
		
		// register dynamic webhook
		HashMap<String,String> headerMap = new HashMap<String,String>();

		headerMap.put("Authorization", "Basic "+auth);
		headerMap.put("Content-Type", "application/json");
		
		
		//get webhook from file
		String body = new String();
		try {
			body = new String(Files.readAllBytes(Paths.get("src/main/resources/webhooks/webhook_demo.json")));
		} catch( Exception e) {
			System.out.println(e.toString());
		}
		
		Response response = given().headers(headerMap).body(body).log().all().post(jiraUrl+"/rest/api/3/webhook");
		//Response response = given().headers(headerMap).log().all().get(jiraUrl+"/rest/api/2/issue/TPT-9");
		//Response response = given().headers(headerMap).log().all().get(jiraUrl+"/rest/api/3/webhook");
		System.out.println(response.body().prettyPrint());
	}

}
