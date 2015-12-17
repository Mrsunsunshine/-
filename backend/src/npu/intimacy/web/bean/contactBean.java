package npu.intimacy.web.bean;

import java.math.BigInteger;

public class contactBean {

	private String username;
	private String portrait;
	private int sex;
	private String signature;
	private String area;
	private String currentip;
	private BigInteger currenttime;
	private double longitute;
	private double latitute;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCurrentip() {
		return currentip;
	}
	public void setCurrentip(String currentip) {
		this.currentip = currentip;
	}
	public BigInteger getCurrenttime() {
		return currenttime;
	}
	public void setCurrenttime(BigInteger currenttime) {
		this.currenttime = currenttime;
	}
	public double getLongitute() {
		return longitute;
	}
	public void setLongitute(double longitute) {
		this.longitute = longitute;
	}
	public double getLatitute() {
		return latitute;
	}
	public void setLatitute(double latitute) {
		this.latitute = latitute;
	}
	
}
