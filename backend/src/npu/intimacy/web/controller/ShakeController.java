package npu.intimacy.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import npu.intimacy.web.bean.contactBean;
import npu.intimacy.web.bean.shakingBean;
import npu.intimacy.web.entity.User;
import npu.intimacy.web.service.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ShakeController {

	@Resource(name = "userManager")
	private UserManager usermanager;

	private static final int INTERVAL = 10000;// 能够发生匹配的时间间隔

	private static LinkedList<shakingBean> shakes = new LinkedList<shakingBean>();

	@RequestMapping("/shake")
	@ResponseBody
	public void shake(HttpServletRequest request, HttpServletResponse response){

		String receive = request.getParameter("shake");
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(receive);
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			String username = jsonObject.optString("username");
			System.out.println("username:::" + username);
			long time = new Date().getTime();
			System.out.println(username+" 摇了一下");

			// 假如匹配成功了，将匹配的所有陌生人信息都返回给客户端
			if (match(time, username)) {
				
				// 遍历链表并把链表中除了自己的用户信息返回给客户端
				Map<String, Integer> map = new HashMap<String, Integer>();
				JSONArray sendArray = new JSONArray();
				for (Iterator iterator = shakes.iterator(); iterator.hasNext();) {
					shakingBean shakingBean = (shakingBean) iterator.next();
					String shakename = shakingBean.getUsername();
					if (shakename.equals(username)|| map.containsKey(shakename)) continue;
					User user = usermanager.findById(shakename);
					// 封装json数据包
					JSONObject sendObject = new JSONObject();
					sendObject.put("username", shakename);
					sendObject.put("sex", user.getSex());
					sendObject.put("portrait", user.getPortrait());
					sendObject.put("area", user.getArea());
					sendObject.put("signature",
							user.getIndividualitySignature());
					sendArray.put(sendObject);
					map.put(shakename, 1);
				}
				// 发送给客户端
				response.setHeader("Content-Type","application/json;charset=UTF-8");
				response.getOutputStream().write(sendArray.toString().getBytes());
				map.clear();
			}
			// 将这次摇动信息存入链表中

			shakingBean shake = new shakingBean();
			shake.setTime(time);
			shake.setUsername(username);
			shakes.add(shake);

		} catch (Exception e) {
			System.out.println("错误出现在---<ShakeController>---");
			e.printStackTrace();
		}

	}

	private boolean match(long time, String username) {

		if (shakes.isEmpty())
			return false; // 假如队列是空，一定匹配不到
		Iterator<shakingBean> iterator = shakes.iterator();

		while (iterator.hasNext()) {
			shakingBean shakingBean = iterator.next();
			if (time - shakingBean.getTime() >= INTERVAL) {
				iterator.remove();
				continue;
			}
			if (shakingBean.getUsername().equals(username))
				continue;
			return true;
		}
		System.err.println("shakes is........" + shakes.size());

		return false;
	}
}
