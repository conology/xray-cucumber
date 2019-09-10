package net.conology.stepdefs;

import static io.restassured.RestAssured.given;

import java.util.UUID;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DemoStepDefinitions {
	
	private Response response;
	private RequestSpecification request;
	private UUID activityId;
	
	@Given("^an activityId of (.*)")
	public void an_activityId_of(UUID activityId) {
		this.activityId = activityId;
		request = given();
		System.out.println("GIVEN "+activityId.toString());
	}
	
	@When("^the order gets fetched from (.*)")
	public void the_order_gets_fetched_from(String component) {
		response = request.when().log().all().get(findURL(component)+"/connectivityOrders/"+activityId);
		System.out.println("WHEN");
	}
	
	
	@Then("^the response contains")
	public void the_response_contains() {
		
	}
	
	
	
	// *********
	// Utils
	
	
	
	private String findURL(String component) {
		
		if (component == "WOM") return "http://wholesaleordermanagement-tst-193.blw-02.eu-de.containers.appdomain.cloud/";
				
				/*
				 * 1. fetch componentname from jira (eg. send WOM receive wholesaleordermanagement
				 * 2. fetch namespace from xray?? (tst-193)
				 * 3. fetch environment (e.g. IBM or Telekom
				 */
		
		else return "Zonk";
	}
	
	
	

}
