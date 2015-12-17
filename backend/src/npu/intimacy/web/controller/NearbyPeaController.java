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
public class NearbyPeaController {

	@Resource(name = "userManager")
	private UserManager userManager;
	private static int cnt = 0;

	/**
	 * 显示附近的用户
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/nearbypeople")
	@ResponseBody
	public void getPeople(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		String receive = request.getParameter("nearby");
		try {
			// 获得客户端发送过来的用户名
			
			JSONArray recArray = new JSONArray(receive);
			JSONObject recObject = recArray.getJSONObject(0);
			String hostname = recObject.optString("username");

			// 获得hostname所有好友的信息，包括位置信息
			List<contactBean> contacts = userManager.getLocations(hostname);
			System.out.println(hostname+" 想查看附件的人");
			JSONArray sendArray = new JSONArray();
			// 将好友列表封装入json包
			for (Iterator iterator = contacts.iterator(); iterator.hasNext();) {
				contactBean contact = (contactBean) iterator.next();
				JSONObject sendObject = new JSONObject();

				sendObject.put("username", contact.getUsername());
				sendObject.put("portrait", contact.getPortrait());
				sendObject.put("area", contact.getArea());
				sendObject.put("sex", contact.getSex());
				sendObject.put("signature", contact.getSignature());
				sendObject.put("currentip", contact.getCurrenttime());
				sendObject.put("currenttime", contact.getCurrenttime());
				sendObject.put("longitute", contact.getLongitute());
				sendObject.put("latitute", contact.getLatitute());

				sendArray.put(sendObject);
			}
			System.err.println(sendArray.length()+"---------");
			// 将json数据返还给客户端
			response.setHeader("Content-Type", "application/json;charset=UTF-8");
			response.getOutputStream().write(sendArray.toString().getBytes());
		} catch (JSONException e) {
			System.out.println("错误出现在---<NearbyPeaController>---");
			e.printStackTrace();
		}
		
	}
}
