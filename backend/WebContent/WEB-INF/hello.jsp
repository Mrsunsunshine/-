<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" class="no-js">
    <head>     
    <title>LogIn</title>
        <script type="text/javascript">
        function addUser(){
        	var form = document.forms[0];
        	form.action="/SoftwareEngineer/nearbyPeople";
        	form.method="post";
        	form.submit();
        }
        </script>

    </head>
		<form name="userForm" action="/SoftwareEngineer/nearbyPeople" method="get">
			姓名：<input type="text" class="username" name="username">
			密码：<input type="password" class="password" name="password">
			<button type="submit" class="btn btn-orange" tabindex="3">登录</button>
		</form> 
		
    <body>
		<!-- <img src="/SoftwareEngineer/img/class3.jpg"> -->
    </body>

</html>