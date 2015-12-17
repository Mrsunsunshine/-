package npu.intimacy.web.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import npu.intimacy.web.entity.Contact;
import npu.intimacy.web.entity.Location;
import npu.intimacy.web.entity.User;
import npu.intimacy.web.service.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
	
	@Resource(name="userManager")
	private UserManager userManager;
	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/login")
	@ResponseBody
	public void login(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		User res=null;
		
		//解析从客户端发送来的参数
		String receive = request.getParameter("login");
		JSONArray jsonArray = new JSONArray(receive);
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		//将需要返回给客户端的数据封装成json包
		JSONArray sendArray = new JSONArray();
		JSONObject sendObject = new JSONObject();
		try {
			String username = jsonObject.optString("username");
			String password = jsonObject.optString("password");
			double longitute = jsonObject.optDouble("longitute");
			double latitute = jsonObject.optDouble("latitute");
			String ip = jsonObject.optString("currentip");
			
			//记录用户登录时刻所在网络的IP地址
			Location location = new Location();
			location.setUsername(username);
			location.setCurrentip(ip);
			location.setCurrenttime(new Date().getTime());
			location.setLatitute(latitute);
			location.setLongitute(longitute);
			res = userManager.userLogin(username,password,location);
		
			
				
			//用户登录成功，将个人信息返回给客户端
				if (res!=null) {
					System.out.println(res.getUsername()+res.getSex()+res.getIndividualitySignature());
					sendObject.put("status", "success");
					sendObject.put("username",res.getUsername() );
					sendObject.put("area", res.getArea());
					sendObject.put("sex", res.getSex());
					sendObject.put("comment",res.getIndividualitySignature());
					sendObject.put("portrait", res.getPortrait());
					sendArray.put(sendObject);
					System.out.println(username+" 上线了....");
				}
			//用户登录失败
				else  {
					sendObject.put("status", "fail");
					sendArray.put(sendObject);
				}
			response.setHeader("Content-Type", "application/json;charset=UTF-8");
			response.getOutputStream().write(sendArray.toString().getBytes());
		} catch (Exception e) {
			System.out.println("错误出现在---<LoginController>---");
			sendObject.put("status", "fail");
			sendArray.put(sendObject);
			response.setHeader("Content-Type", "application/json;charset=UTF-8");
			response.getOutputStream().write(sendArray.toString().getBytes());
			e.printStackTrace();
		}
			
	}
	
	/**
	 * 获得当前用户的所有好友列表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/contactlist")
	@ResponseBody
	public void contactlist(HttpServletRequest request,HttpServletResponse response){
		
		String receive = request.getParameter("friendsInformation");
		
		try {
			JSONArray recArray = new JSONArray(receive);
			JSONObject recObject = recArray.getJSONObject(0);
			String hostname = recObject.optString("username");
			
			JSONArray sendaArray = new JSONArray();
			//将好友列表封装入json包
			List<Contact> contactlist = userManager.getContacts(hostname);
			for (Iterator iterator = contactlist.iterator(); iterator.hasNext();) {
				Contact contact = (Contact) iterator.next();
				JSONObject sendObject = new JSONObject();
				sendObject.put("username",contact.getUsername() );
				sendObject.put("area", contact.getArea());
				sendObject.put("sex", contact.getSex());
				sendObject.put("signature",contact.getSignature());
				sendObject.put("portrait", contact.getPortrait());
				sendaArray.put(sendObject);
			}
			//将json数据返还给客户端
			response.setHeader("Content-Type", "application/json;charset=UTF-8");
			response.getOutputStream().write(sendaArray.toString().getBytes());
		} catch (Exception e) {
			System.out.println("错误出现在---<LoginController>---");
			e.printStackTrace();
		}
	}
	
	
	
}
