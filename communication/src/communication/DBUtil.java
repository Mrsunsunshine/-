package communication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    
 public static Connection getConn() {
        
        try {
            //1.加载mysql连接到数据库jar包，数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            //2.数据库所在位置以及要访问数据库的名字
            String url = "jdbc:mysql://10.128.51.203:3306/softwareengineer";
            //3.数据库的用户名，密码
            String username = "root";
            String password = "0000";
            //4.使用驱动管理器连接到数据库
            Connection conn = DriverManager.getConnection(url,username,password);
            return conn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        	return null;
    }

    

}