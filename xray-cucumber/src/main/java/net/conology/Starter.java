package net.conology;

import net.conology.connector.JiraCloudConnector;
import net.conology.connector.XrayCloudConnector;

public class Starter {

	public static void main(String[] args) {
		
		XrayCloudConnector con = new XrayCloudConnector("7D6A3E158E494C3F8DC9EC2BEBA00273",
				"a49df729a800d4936864b28f3aa2fcd6e519a62509376a624c9a30e1ec635142",
				"https://xray.cloud.xpand-it.com");
		
		con.getGherkinFromTestCloud("TPT-23");
		//con.publishResults();
		
		
		
		/*JiraCloudConnector con2 = new JiraCloudConnector("willm.tueting@conology.net",
				"DFkWHML2DYYnmiFAKPHa228F" ,
				"https://conology.atlassian.net");
		
		con2.registerWebhook();*/

	}

}
