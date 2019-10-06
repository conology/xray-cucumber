package net.conology.config.model;

public class XrayProperties {
	
	private CloudProperties cloud;

	public CloudProperties getCloud() {
		return cloud;
	}

	public void setCloud(CloudProperties cloud) {
		this.cloud = cloud;
	}

	@Override
	public String toString() {
		return "XrayProperties [cloud=" + cloud + "]";
	}
	

}
