package npu.intimacy.web.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import npu.intimacy.web.bean.UserReq;
import npu.intimacy.web.entity.User;
import npu.intimacy.web.service.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import antlr.collections.impl.Vector;

@Controller
public class connectController {

	//保存所有“添加好友请求”
	private static LinkedList<UserReq> reqList = new LinkedList<UserReq>();
	
	//保存所有“回复消息”
	private static LinkedList<UserReq> repList = new LinkedList<UserReq>();
	
	@Resource(name="userManager")
	private UserManager usermanager;
	
	@RequestMapping("/addUser")
	@ResponseBody
	private void addUser(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		String receive = request.getParameter("addUser");
		JSONArray recArray;
		try {
			//解析从客户端发送来的数据
			recArray = new JSONArray(receive);
			JSONObject recObject = recArray.getJSONObject(0);
			
			//判断客户端发来的是那种请求
			int status = recObject.optInt("status");
			//System.out.println(status);
			
			//1表示用户发送添加好友的请求
			if (status==1) {
				String hostname = recObject.optString("hostname");
				String friendname = recObject.optString("friendname");
				UserReq req = new UserReq();
				req.setFriendname(friendname);
				req.setHostname(hostname);
				if (!hasItem(reqList,req)) {
					System.err.println(hostname+" "+friendname);
					reqList.add(req);
					JSONArray sendArray = new JSONArray();
					JSONObject sendObject = new JSONObject();
					sendObject.put("status", "success");
					sendArray.put(sendObject);
					response.setHeader("Content-Type", "application/json;charset=UTF-8");
					response.getOutputStream().write(sendArray.toString().getBytes());
					return ;
				}
			}
			
			//2用来建立服务器和客户端之间的联系
			if (status == 2) {
				String username = recObject.optString("username");
				String reqUser = getReqUserName(reqList,username);//获得请求
				String repUser = getReqUserName(repList, username);//获得回复
				//System.out.println("====="+reqUser);
				
				//假如有人请求加自己为好友
				if (reqUser != null) {
					System.out.println("reqUser");
					JSONArray sendArray = new JSONArray();
					JSONObject sendObject = new JSONObject();
					User user = usermanager.findById(reqUser);
					System.err.println(user.getIndividualitySignature()+"nimalegebi");
					sendObject.put("status", "request");
					sendObject.put("sex", user.getSex());
					sendObject.put("area", user.getArea());
					sendObject.put("signature", user.getIndividualitySignature());
					sendObject.put("portrait",  user.getPortrait());
					sendObject.put("username", reqUser);
					sendArray.put(sendObject);
					response.setHeader("Content-Type", "application/json;charset=UTF-8");
					response.getOutputStream().write(sendArray.toString().getBytes());
					return ;
				}
				
				//假如对方同意自己的好友添加请求
				if (repUser != null) {
					System.out.println("repUser:"+repUser);
					JSONArray sendArray = new JSONArray();
					JSONObject sendObject = new JSONObject();
					User user = usermanager.findById(repUser);
					sendObject.put("status", "response");
					sendObject.put("sex", user.getSex());
					sendObject.put("area", user.getArea());
					sendObject.put("signature", user.getIndividualitySignature());
					sendObject.put("portrait", user.getPortrait());
					sendObject.put("username", repUser);
					sendArray.put(sendObject);
					response.setHeader("Content-Type", "application/json;charset=UTF-8");
					response.getOutputStream().write(sendArray.toString().getBytes());
					return ;
				}
				
			}
			
			//3用来表示用户的回馈信息
			if (status == 3) {
				String hostname = recObject.optString("hostname");
				String friendname = recObject.optString("friendname");
				String content = recObject.optString("content");//记录被请求用户是否同意添加
				System.err.println(hostname+" "+friendname+" "+content+" adduser ");
				if (content.equals("false")) return;//不同意添加
				else{//同意添加
					UserReq resp = new UserReq();
					resp.setFriendname(friendname);
					resp.setHostname(hostname);
					if (!hasItem(repList,resp)) {
						repList.add(resp);
						usermanager.addContact(hostname, friendname);
						return;
					}
				}
				}
				
		} catch (JSONException e) {
			System.out.println("错误出现在---<AdduserController>---");
			e.printStackTrace();
		}
		return;
	}
	
	/**
	 * 判断请求/回复对队列中是不是有uq这条记录
	 * @param list
	 * @param uq
	 * @return
	 */
	private boolean hasItem(LinkedList<UserReq> list,UserReq uq){
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			UserReq userReq = (UserReq) iterator.next();
			if(userReq.equals(uq)) return true;
		}
		return false;
		
	}
	
	/**
	 * 得到请求/回复方用户的名称
	 * @param list
	 * @param username
	 * @return
	 */
	private String getReqUserName(LinkedList<UserReq> list,String username){
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			UserReq userReq = (UserReq) iterator.next();
			if (userReq.getFriendname().equals(username)) {
				String reqUser = userReq.getHostname();
				list.remove(userReq);
				return reqUser;
			}
		}
		return null;
	}
	
	
	
}
