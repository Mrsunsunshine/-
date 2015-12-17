package npu.intimacy.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import npu.intimacy.web.service.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PasswordController {

	@Resource(name = "userManager")
	private UserManager userManager;

	/**
	 * 找回密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/password")
	@ResponseBody
	public String getbackPass(HttpServletRequest request,HttpServletResponse response) {
		String receive = request.getParameter("forgetPassword");

		try {
			// 获得用户的ID、邮箱地址
			JSONArray recArray = new JSONArray(receive);
			JSONObject jsonObject = recArray.getJSONObject(0);
			String username = jsonObject.optString("username");
			String email = jsonObject.optString("email");
			String res = userManager.findBackPassword(username, email);

			JSONArray sendArray = new JSONArray();
			JSONObject sendObject = new JSONObject();

			if (res.equals("nouser")) sendObject.put("result", "nouser");// 用户名错误
			if (res.equals("noemail")) sendObject.put("result", "noemail");// 邮箱为非绑定用户邮箱
			if (res.equals("success")) sendObject.put("result", "success");// 成功找回密码
			sendArray.put(sendObject);
			return sendArray.toString();
		} catch (JSONException e) {
			System.out.println("错误出现在---<PasswordController>---");
			e.printStackTrace();
		}
		return null;
	}
}
