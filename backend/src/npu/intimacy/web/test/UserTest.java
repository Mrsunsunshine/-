package npu.intimacy.web.test;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import junit.framework.TestCase;


public class UserTest extends TestCase{
	
	public void testModifyController() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}  
		String url = "jdbc:mysql://10.128.51.203:3306/softwareengineer";
		String username = "root";
		String password = "0000";
		try { 
			Connection conn =  DriverManager.getConnection(url, username, password);
			//Statement statement = conn.createStatement();
			//String sql = "insert into location(id,username,currentip,currenttime,longitute,latitute) values(1,\"hkq\",\"127.0.0.1\",\"123\",38.0,38.0)";
			String sql ="select contact.username,portrait,sex,signature,area,currentip,currenttime,longitute,latitute from contact,location where contact.hostname = ? and contact.username = location.username";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, "lcc");
			ResultSet rs = st.executeQuery();
			System.out.println(1);
			while (rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getString(2)
                        + "\t\t" + rs.getInt(3) + "\t\t" + rs.getString(4));
            }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  void testNearBy() throws IOException {
		//nearbyPeaController near = new nearbyPeaController();
	}
}
