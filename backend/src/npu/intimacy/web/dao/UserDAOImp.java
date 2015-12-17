package npu.intimacy.web.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONObject;

import sun.misc.BASE64Encoder;
import npu.intimacy.web.bean.contactBean;
import npu.intimacy.web.entity.Contact;
import npu.intimacy.web.entity.Location;
import npu.intimacy.web.entity.User;

public class UserDAOImp implements UserDAO {

	//建立数据库连接
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * 用户注册
	 */
	@Override
	public void userRegister(User user) {
		System.out.println(user.getPassword()+" "+user.getUsername()+" "+user.getId()+" 1"+user.getMailbox());
		sessionFactory.openSession().save(user);
	}
	
	/**
	 * 用户登录
	 */
	@Override
	public User userLogin(String username, String password,Location location) {
		System.out.println(username+" dao "+password);
		Session session = sessionFactory.openSession();
		Query query=session.createQuery("from User u where u.username=?");
		query.setString(0, username);
		List<User> users = query.list();
		if (users.isEmpty()) return null;
		User user = users.get(0);
		String pas = user.getPassword();
		if (!pas.equals(password)) return null;
		query=session.createQuery("from Location l where l.username=?");
		query.setString(0, username);
		List<Location> list = query.list();
		if(list.isEmpty()) session.save(location);
		else {
		System.out.println(location.getLongitute()+"---");
			query = session.createQuery("update Location l set l.currenttime=?,l.longitute=?,l.latitute=?,l.currentip=? where l.username=?");
			query.setLong(0, location.getCurrenttime());
			query.setDouble(1, location.getLongitute());//
			query.setDouble(2, location.getLatitute());//
			query.setString(3, location.getCurrentip());//
			query.setString(4, username);
			query.executeUpdate();
		}
		return user;
	}
	
	/**
	 * 判断用户是否存在
	 */
	@Override
	public boolean userCheck(String username) {
		System.out.println(username+" dao-check ");
		Session session = sessionFactory.openSession();
		Query query=session.createQuery("select password from User where username=?");
		query.setString(0, username);
		List<String> list = query.list();
		System.out.println(list.isEmpty());
		return list.isEmpty();
	}
	
	/**
	 * 找回密码
	 */
	@Override
	public String findbackPass(String username,String email) {
		Session session = sessionFactory.openSession();
		Query query=session.createQuery("from User u where u.username=?");
		query.setString(0, username);
		List<User> list = query.list();
		if (list.isEmpty()) return "empty";//0表示用户名不存在
		User user = list.get(0);
		if(user.getMailbox().equals(email)){
			return user.getPassword();
		}
		else return "emailwrong";//-1表示账户和邮箱不匹配
	}
	
	/**
	 * 获得hostname的好友列表
	 */
	@Override
	public List<Contact> getContacts(String hostname) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Contact c where c.hostname=?");
		query.setString(0, hostname);
		List<Contact> list = query.list();
		return list;
	}
	
	/**
	 * 修改用户信息
	 */
	@Override
	public boolean userModify(User user) {
		System.out.println(user.getIndividualitySignature()+user.getArea()+user.getSex()+" "+user.getUsername());
		String hql = "update User u set u.portrait=?,u.sex=?,u.area=?,u.individualitySignature=?,u.password=? where u.username=?";//,u.individualitySignature=?
		Query query = sessionFactory.openSession().createQuery(hql);
		query.setString(0, user.getPortrait());
		query.setInteger(1, user.getSex());
		query.setString(2, user.getArea());
		query.setString(3, user.getIndividualitySignature());
		query.setString(4, user.getPassword());
		query.setString(5, user.getUsername());
		return (query.executeUpdate() > 0);
	}
	
	/**
	 * 通过好友名称获得好友信息
	 */
	@Override
	public User findById(String username) {
		Session session = sessionFactory.openSession();
		Query query=session.createQuery("from User u where u.username=?");
		query.setString(0, username);
		List<User> users = query.list();
		if (users.isEmpty()) return null;
		User user = users.get(0);
		return user;
	}
	
	/**
	 * 获得好友信息，包括好友当前的位置信息
	 */
	@Override
	public List<contactBean> getLocations(String hostname) {
		/*hkq
		null
		1
		就是爱嘚瑟
		西安
		10.140.19.84
		1432649872821
		0.0
		0.0*/
		Session session = sessionFactory.openSession();
		String sql = "select contact.username,portrait,sex,signature,area,currentip,currenttime,longitute,latitute from contact,location where contact.hostname = ? and contact.username = location.username";
		SQLQuery query = session.createSQLQuery(sql);
		System.out.println(hostname);
		query.setString(0, hostname);

		List<Object> list = query.list();
		List<contactBean> contacts = new ArrayList<contactBean>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object[] obs = (Object[]) iterator.next();
			contactBean contact = new contactBean();
			for(int i=0;i<obs.length;i++) System.out.println(obs[i]);
			for(int i=0;i<obs.length;i++){
				contact.setUsername((String)obs[0]);
				
				String path = (String)obs[1];
				String portrait = pathToImage(path);
				contact.setPortrait(portrait);
				contact.setSex((int)obs[2]);
				contact.setSignature((String)obs[3]);
				contact.setArea((String)obs[4]);
				contact.setCurrentip((String)obs[5]);
				contact.setCurrenttime((BigInteger)obs[6]);
				contact.setLongitute((double)obs[7]);
				contact.setLatitute((double)obs[8]);
			}
			contacts.add(contact);
		}
		//Object[] objects = (Object[]) query.list().get(0);
        //for(int i=0;i<objects.length;i++) System.out.println(objects[i]);
		return contacts;
	}
	
	/**
	 * 将hostname与username互置为好友
	 */
	@Override
	public boolean addContact(String hostname,String username) {
		Session session = sessionFactory.openSession();
		User user1 = findById(hostname);
		User user2 = findById(username);
		//将user2添加到user1的联系人中
		Contact contact1 = new Contact();
		contact1.setArea(user2.getArea());
		contact1.setHostname(hostname);
		contact1.setUsername(username);
		contact1.setPortrait(user2.getPortrait());
		contact1.setSex(user2.getSex());
		contact1.setSignature(user2.getIndividualitySignature());
		session.save(contact1);
		
		//将user2添加到user1的联系人中
		Contact contact2 = new Contact();
		contact2.setArea(user1.getArea());
		contact2.setHostname(username);
		contact2.setUsername(hostname);
		contact2.setPortrait(user1.getPortrait());
		contact2.setSex(user1.getSex());
		contact2.setSignature(user1.getIndividualitySignature());
		session.save(contact2);
		return true;
	}
	
	/**
	 * 通过图片的存储路径，找到图片并将图片存储为string
	 * @param path
	 * @return
	 */
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
	/**
	 * 删除好友
	 */
	@Override
	public boolean deleteContact(String hostname, String friendname) {
		Session session = sessionFactory.openSession();
		Transaction trans=session.beginTransaction();
		System.out.println(hostname+friendname);
		String hql="delete from Contact con where con.username=? and con.hostname=?";
		Query query=session.createQuery(hql);
		query.setString(0, friendname);
		query.setString(1, hostname);
		int ret=query.executeUpdate();
		System.err.println("deletecontact......"+ret);
		trans.commit();
		return true;
	}
	

}
