package communication;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import junit.framework.TestCase;

public class Test extends TestCase{
	
public void testgetId(){
		
	String username = "hkq";
	String result = "";
		try {
			Connection conn = new DBUtil().getConn();
			String sql = "select currentip from location where username = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) result = rs.getString("currentip");//return rs.getString("currentip");
			System.out.println(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
public void testMap(){
	HashMap<String, String> map = new HashMap<String, String>();
	map.put("1", "1");
	map.put("1", "2");
	map.put("1", "3");
	System.err.println(map.get("1")+" "+map.size());
}

public void testdelefile(){
	String path = "F:\\file\\1434000795152.f";
	File file=new File(path);
    file.delete();
}
}
