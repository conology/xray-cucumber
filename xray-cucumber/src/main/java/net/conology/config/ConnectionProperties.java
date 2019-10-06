package net.conology.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import net.conology.config.model.JiraProperties;
import net.conology.config.model.XrayProperties;

@Component
@ConfigurationProperties("connection")
public class ConnectionProperties {
	
	
	    private JiraProperties jira;
	    private XrayProperties xray;
	    
		public JiraProperties getJira() {
			return jira;
		}
		public void setJira(JiraProperties jira) {
			this.jira = jira;
		}
		public XrayProperties getXray() {
			return xray;
		}
		public void setXray(XrayProperties xray) {
			this.xray = xray;
		}
		
		@Override
		public String toString() {
			return "ConnectionProperties [jira=" + jira + ", xray=" + xray + "]";
		}

	}
