package npu.intimacy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping({"/index","/"})
	public String toAddUser(String username,String password) {
		//System.out.println(username+"-----"+password);
		return "hello";
	}
	
}
