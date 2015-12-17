package npu.intimacy.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import npu.intimacy.web.entity.User;
import npu.intimacy.web.service.UserManager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
	
	@Resource(name="userManager")
	private UserManager userManager;
	
	@RequestMapping("/addUsersss")
	public String userRegister(User user,HttpServletRequest request) {
		userManager.userRegister(user);
		String res = "success!";
		System.out.println(res);
		request.setAttribute("result", res);
		return "/result";
	}
}
