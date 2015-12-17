package npu.intimacy.web.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import sun.misc.BASE64Encoder;
import npu.intimacy.web.bean.contactBean;
import npu.intimacy.web.dao.UserDAO;
import npu.intimacy.web.entity.Contact;
import npu.intimacy.web.entity.Location;
import npu.intimacy.web.entity.User;
import npu.intimacy.web.util.MailSenderUtil;
import npu.intimacy.web.util.SimpleMailSender;

public class UserManagerImp implements UserManager {

	
	private UserDAO userDao;
	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}
	
	/**
	 * 用户注册
	 */
	@Override
	public void userRegister(User user) {
		userDao.userRegister(user);
		String url = "http://192.168.43.214:8080/SoftwareEngineer/ensure?username="+user.getUsername();
		// 设置邮件服务器信息
		  MailSenderUtil mailInfo = new MailSenderUtil();
		  mailInfo.setMailServerHost("smtp.163.com");
		  mailInfo.setMailServerPort("25");
		  mailInfo.setValidate(true);
		  // 邮箱用户名
		  mailInfo.setUserName("hkq1993317@163.com");
		  // 邮箱密码
		  mailInfo.setPassword("2530650HKQ");
		  // 发件人邮箱
		  mailInfo.setFromAddress("hkq1993317@163.com");
		  // 收件人邮箱
		  mailInfo.setToAddress(user.getMailbox());
		  // 邮件标题
		  mailInfo.setSubject("谢谢你");
		  // 邮件内容
		  StringBuffer buffer = new StringBuffer();
		  buffer.append("点击下面链接完成注册： "+url+" 谢谢您的加入！");
		  mailInfo.setContent(buffer.toString());
		  
		  // 发送邮件
		  SimpleMailSender sms = new SimpleMailSender();
		  // 发送文体格式
		  sms.sendTextMail(mailInfo);
		  // 发送html格式
		  SimpleMailSender.sendHtmlMail(mailInfo);
	}
	
	/**
	 *用户登录
	 */
	@Override
	public User userLogin(String username, String password,Location location) {
		System.out.println(username+" service "+password+" location "+location.getLatitute());
		User user = userDao.userLogin(username, password,location);
		String path = user.getPortrait();
		String portrait = pathToImage(path);
		user.setPortrait(portrait);
		return user;
	}
	
	/**
	 * 检查是否有username的用户
	 */
	public boolean userCheck(String username){
		return userDao.userCheck(username);
	}
	
	/**
	 * 找回密码，将原密码发送至用户所绑定的邮箱
	 */
	@Override
	public String findBackPassword(String username, String email) {
		String res = userDao.findbackPass(username, email);
		if(res.equals("empty")) return "nouser";
		else if(res.equals("emailwrong")) return "noemail";
		else {
			 // 设置邮件服务器信息
			  MailSenderUtil mailInfo = new MailSenderUtil();
			  mailInfo.setMailServerHost("smtp.163.com");
			  mailInfo.setMailServerPort("25");
			  mailInfo.setValidate(true);
			  // 邮箱用户名
			  mailInfo.setUserName("hkq1993317@163.com");
			  // 邮箱密码
			  mailInfo.setPassword("2530650HKQ");
			  // 发件人邮箱
			  mailInfo.setFromAddress("hkq1993317@163.com");
			  // 收件人邮箱
			  mailInfo.setToAddress(email);
			  // 邮件标题
			  mailInfo.setSubject("找回密码");
			  // 邮件内容
			  StringBuffer buffer = new StringBuffer();
			  buffer.append("尊敬的用户： "+username+"，您的密码是： "+res+"\n请保存好密码。");
			  mailInfo.setContent(buffer.toString());
			  
			  // 发送邮件
			  SimpleMailSender sms = new SimpleMailSender();
			  // 发送文体格式
			  sms.sendTextMail(mailInfo);
			  // 发送html格式
			  SimpleMailSender.sendHtmlMail(mailInfo);
			  return "success";
		}
	}
	
	/**
	 * 获得hostname所有的好友信息
	 */
	@Override
	public List<Contact> getContacts(String hostname) {
		
		List<Contact> list = userDao.getContacts(hostname);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Contact contact = (Contact) iterator.next();
			String path = contact.getPortrait();
			String portrait = pathToImage(path);
			contact.setPortrait(portrait);
		}
		return list;
	}
	
	/**
	 * 修改用户信息
	 */
	@Override
	public boolean userModify(User user) {
		return userDao.userModify(user);
	}
	
	/**
	 * 通过用户名称获得用户的全部信息
	 */
	@Override
	public User findById(String username) {
		System.out.println("findById....."+username);
		User user = userDao.findById(username);
		String path = user.getPortrait();
		String portrait = pathToImage(path);
		user.setPortrait(portrait);
		return user;
	}
	
	/**
	 * 获得所有好友信息，包括好友当前的位置信息
	 */
	@Override
	public List<contactBean> getLocations(String hostname) {
		
		return userDao.getLocations(hostname);
	}
	
	/**
	 * 添加好友
	 */
	@Override
	public boolean addContact(String hostname,String username){
		return userDao.addContact(hostname, username);
	}
	
	/**
	 * 将制定路径的图片转化成string类型
	 */
	@Override
	public String pathToImage(String path) {

		byte[] data = null;
		// 读取图片字节数组
		try {
		InputStream in = new FileInputStream(path);
		data = new byte[in.available()];
		in.read(data);
		in.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
		
	}

	@Override
	public boolean deleteContact(String hostname, String friendname) {
		
		return userDao.deleteContact(hostname, friendname);
	}

}
