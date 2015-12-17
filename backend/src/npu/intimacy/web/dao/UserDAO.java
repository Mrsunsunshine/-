package npu.intimacy.web.dao;

import java.util.List;

import npu.intimacy.web.bean.contactBean;
import npu.intimacy.web.entity.Contact;
import npu.intimacy.web.entity.Location;
import npu.intimacy.web.entity.User;

public interface UserDAO {
	
	public void userRegister(User user);
	public User userLogin(String username,String password,Location location);
	public boolean userCheck(String username);
	public String findbackPass(String username,String email);
	public List<Contact> getContacts(String hostname);
	public boolean userModify(User user);
	public User findById(String username);
	public List<contactBean> getLocations(String hostname);
	public boolean addContact(String hostname,String username);
	public boolean deleteContact(String hostname,String friendname);
	
}
