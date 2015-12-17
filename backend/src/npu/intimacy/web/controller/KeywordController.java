package npu.intimacy.web.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import npu.intimacy.web.entity.User;
import npu.intimacy.web.service.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class KeywordController {
	
	@Resource(name="userManager")
	private UserManager userManager;
	
	/**
	 * 通过关键字搜索陌生人
	 * @param request
	 * @param response
	 * @return string
	 * @throws IOException
	 */
	@RequestMapping("/keyword")
	@ResponseBody
	public void keyword(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		User res=null;//记录检索数据库之后的结果，正确返回ip所对应的用户，不对返回null
		String receive = request.getParameter("keyword");//获得包含有关键字的json包
		try {
			//从客户端接收数据
			JSONArray jsonArray = new JSONArray(receive);
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			String username = jsonObject.optString("username");
			res = userManager.findById(username);//记录搜索结果正确返回该用户基本信息，否则返回空	
			
			//发送数据到客户端
			JSONArray jsonArray1 = new JSONArray();
			JSONObject status = new JSONObject();
			response.setHeader("Content-Type", "application/json;charset=UTF-8");
			//查询结果不为空时
			if (res!=null) {
				System.out.println(res.getUsername()+res.getSex()+res.getIndividualitySignature());
				status.put("status", "success");
				status.put("username",res.getUsername() );
				status.put("area", res.getArea());
				status.put("sex", res.getSex());
				status.put("portrait", res.getPortrait());
				status.put("comment",res.getIndividualitySignature());
				jsonArray1.put(status);
				
				//将查询结果返回到客户端
				response.setHeader("Content-Type", "application/json;charset=UTF-8");
				response.getOutputStream().write(jsonArray1.toString().getBytes());
			}
			//查询结果为空
			else  {
				status.put("status", "fail");
				jsonArray1.put(status);
			}
		} catch (JSONException e) {
			System.out.println("错误出现在---<keywordController>---");
			e.printStackTrace();
		}	
			
	}

}
