import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = {"pretty", "json:target/report/cucumber.json"},
		glue = {"stepdefs"},
		features = {"src/test/features"},
		monochrome = true)
public class CucumberTests {}