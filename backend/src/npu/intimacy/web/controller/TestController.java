package npu.intimacy.web.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import npu.intimacy.web.bean.UserReq;
import npu.intimacy.web.service.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import antlr.collections.impl.Vector;

@Controller
public class TestController {

	private static LinkedList<UserReq> reqList = new LinkedList<UserReq>();
	
	@Resource(name="userManager")
	private UserManager usermanager;
	
	@RequestMapping("/test")
	@ResponseBody
	private String addUser(HttpServletRequest request) {

		String receive = request.getParameter("test");
		JSONArray recArray;
		try {
			recArray = new JSONArray(receive);
			JSONObject recObject = recArray.getJSONObject(0);
			//String imgstr = recObject.optString("test");
			//System.out.println(imgstr);
			String imgFilePath = "F:\\image\\qb.jpg";
			String imgStr = GetImageStr(imgFilePath);
			//GenerateImage(imgstr, imgFilePath);
			JSONArray sendArray = new JSONArray();
			JSONObject sendObject = new JSONObject();
			sendObject.put("test",imgStr);
			sendArray.put(sendObject);
			return sendArray.toString();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String GetImageStr(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		byte[] data = null;

		// 读取图片字节数组
		try {
		InputStream in = new FileInputStream(imgFilePath);
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
