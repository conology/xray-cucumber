package net.conology.config.model;

public class CloudProperties {
	
	private String user;
	private String pass;
	private String url;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return "CloudProperties [user=" + user + ", pass=" + pass + ", url=" + url + "]";
	}
	
	

}
