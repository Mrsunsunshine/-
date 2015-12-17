package npu.intimacy.web.controller;

import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import npu.intimacy.web.entity.User;
import npu.intimacy.web.service.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.misc.BASE64Decoder;

import com.fasterxml.jackson.databind.annotation.JsonNaming;


@Controller
public class ModifyController {

	@Resource(name="userManager")
	private UserManager userManager;
	
	/**
	 * 修改用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/modify")
	@ResponseBody
	public String modify(HttpServletRequest request){
		
		String receive = request.getParameter("modify");
		boolean res= true;
		try {
			//获取当前用户修改之后的信息
			JSONArray jsonArray = new JSONArray(receive);
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			//portrait；sex；comment；area username
			
			User user = new User();
			String username = jsonObject.optString("username");
			user.setUsername(username);
			
			String portrait = jsonObject.optString("portrait");		
			//头像所存储的地址
			String imgFilePath = "F:\\image\\"+username+".jpg";
			GenerateImage(portrait, imgFilePath);
			
			user.setArea(jsonObject.optString("area"));
			user.setPortrait(imgFilePath);
			user.setIndividualitySignature(jsonObject.optString("comment"));
			user.setPassword(jsonObject.optString("password"));
			user.setSex(jsonObject.optInt("sex"));
			res = userManager.userModify(user);
			System.out.println(res);
			
			//返回修改结果
			JSONArray array = new JSONArray();
			JSONObject object = new JSONObject();
			if(res) object.put("result", "success");
			else object.put("result", "fail");
			array.put(object);
			return array.toString();
		} catch (JSONException e) {
			System.out.println("错误出现在---<ModifyController>---");
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 将imgstr中存放的图片保存到imgFilePath指定的路径
	 * @param imgStr
	 * @param imgFilePath
	 * @return
	 */
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
