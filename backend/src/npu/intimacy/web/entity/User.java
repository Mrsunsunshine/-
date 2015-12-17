package npu.intimacy.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "user")
public class User {

	private String id;
	@Column(length = 45)
	private String username;

	@Column(length = 45)
	private String password;

	private String portrait;
	
	private int sex;
	
	private int phonenumber;
	
	private String area;
	
	private String mailbox;
	
	private String individualitySignature;
	
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

	public int getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(int phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getIndividualitySignature() {
		return individualitySignature;
	}

	public void setIndividualitySignature(String individualitySignature) {
		this.individualitySignature = individualitySignature;
	}

	

	@Id
	@GeneratedValue
	// @GenericGenerator(name = "system-uuid", strategy = "uuid")
	// @Column(length=32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
