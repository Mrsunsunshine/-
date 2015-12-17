package npu.intimacy.web.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="location")
public class Location {
	
	private int id;
	private String currentip;
	private long currenttime;
	private double latitute;
	private double longitute;
	private String username;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCurrentip() {
		return currentip;
	}
	public void setCurrentip(String currentip) {
		this.currentip = currentip;
	}
	public long getCurrenttime() {
		return currenttime;
	}
	public void setCurrenttime(long currenttime) {
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
