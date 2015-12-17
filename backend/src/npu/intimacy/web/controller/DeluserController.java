package npu.intimacy.web.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import npu.intimacy.web.bean.contactBean;
import npu.intimacy.web.entity.Contact;
import npu.intimacy.web.entity.Location;
import npu.intimacy.web.service.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DeluserController {
	
	@Resource(name="userManager")
	private UserManager userManager;

	/**
	 * 显示附近的用户
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public String delUser(HttpServletRequest request,HttpServletResponse response){
		
		String receive = request.getParameter("delete");
		boolean res = false;
		try {
			//从客户端接收数据
			JSONArray recArray = new JSONArray(receive);
			JSONObject recObject = recArray.getJSONObject(0);
			String friendname = recObject.optString("friendname");
			String hostname = recObject.optString("hostname");
			res = userManager.deleteContact(hostname, friendname);
			
			//发送数据到客户端
			JSONArray sendArray = new JSONArray();
			JSONObject sendObject = new JSONObject();
			if(res) sendObject.put("status", "success");
			else sendObject.put("status", "fail");
			sendArray.put(sendObject);
			return sendArray.toString();
			
		} catch (JSONException e) {
			System.out.println("错误出现在---<DeluserController>---");
			e.printStackTrace();
		}
		return null;
	}
}
