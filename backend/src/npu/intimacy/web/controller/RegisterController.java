package npu.intimacy.web.controller;

import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import npu.intimacy.web.entity.User;
import npu.intimacy.web.service.UserManager;
import npu.intimacy.web.util.MailSenderUtil;
import npu.intimacy.web.util.SimpleMailSender;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.misc.BASE64Decoder;

import com.sun.mail.handlers.image_gif;

@Controller
public class RegisterController {
	@Resource(name = "userManager")
	private UserManager userManager;

	/**
	 * 用户注册
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/register")
	@ResponseBody
	public String register(HttpServletRequest request,HttpServletResponse response) {

		boolean res = true;
		String receive = request.getParameter("regist");

		try {
			// 获得客户端发来的注册所需信息
			JSONArray recArray = new JSONArray(receive);
			JSONObject recObject = recArray.optJSONObject(0);
			String username = recObject.optString("username");
			if (userManager.userCheck(username)) {

				// 获得注册所需的信息
				String password = recObject.optString("password");
				int phoneNum = recObject.optInt("phonenumber");
				String mailbox = recObject.optString("email");
				String portrait = recObject.optString("image");

				// 头像所存储的地址
				String imgFilePath = "F:\\image\\" + username + ".jpg";
				GenerateImage(portrait, imgFilePath);

				// 将注册信息封装到User对象中，存入数据库
				System.err.println(username + phoneNum + password + mailbox);
				User user = new User();
				user.setUsername(username);
				user.setPassword("*" + password);
				user.setPhonenumber(phoneNum);
				user.setMailbox(mailbox);
				user.setPortrait(imgFilePath);
				userManager.userRegister(user);

			} else
				res = false;

			// 向客户端发送信息
			JSONArray sendArray = new JSONArray();
			JSONObject sendObject = new JSONObject();
			if (res)
				sendObject.put("status", "success");// 注册成功
			else
				sendObject.put("status", "fail");// 注册失败

			// 将注册结果返回给客户端
			sendArray.put(sendObject);
			return sendArray.toString();
		} catch (JSONException e) {
			System.out.println("错误出现在---<RegisterController>---");
			e.printStackTrace();
		}
		return null;

	}

	@RequestMapping("/ensure")
	@ResponseBody
	public void ensure(HttpServletRequest request, HttpServletResponse response) {

		String username = request.getParameter("username");
		User user = userManager.findById(username);
		// System.out.println(user.getPortrait());
		user.setPassword(user.getPassword().substring(1));
		user.setPortrait("F:\\image\\" + username + ".jpg");
		userManager.userModify(user);
	}

	private boolean GenerateImage(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] bytes = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
