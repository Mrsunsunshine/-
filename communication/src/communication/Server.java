package communication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;













import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Server {

	//存放离线消息
	public static List<OfflineMsg> offlineMsgs = new LinkedList<OfflineMsg>();
	
	public static Map<String, String> userIp = new HashMap<String, String>();

	public static Connection conn;
	
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(9000); // 用端口号8888创建一个ServerSocket对象
		Socket s = null;
		conn = new DBUtil().getConn();
		while ((s = ss.accept()) != null) { // 循环监听来自客户端的请求，当监听到一个客户端的请求就创建一个线程
			System.out.println(s.getInetAddress().getHostAddress() + " 连入....");
			new ListenerThread(s).start();
		}
		ss.close();
	}
}

class ListenerThread extends Thread {
	private Socket hostsocket;

	public ListenerThread(Socket socket) {
		super();
		this.hostsocket = socket;
	}

	public void run() {

		InputStream cin;
		
		String receiverId = "";
		String receiverIp = "";
		
		BufferedReader br;
		String recMsg = ""; //从发送端发来的消息
		String sendMsg = ""; //发送给接收方的消息
		String[] messages; 
		try {
			//从发送方获得消息
			cin = hostsocket.getInputStream();
			br = new BufferedReader(new InputStreamReader(cin));
			recMsg = br.readLine();
			//System.out.println(recMsg+"hah");
			cin.close();
			hostsocket.close();
			messages = recMsg.split(" ");
			receiverId = messages[0];
			//receiverIp = getIp(receiverId);
			
			// 发送方发送上线消息，消息内容为当前ip和id
			System.err.println(receiverId+" "+hostsocket.getInetAddress().getHostAddress());
			if (receiverId.equals(hostsocket.getInetAddress().getHostAddress())) {
				receiverIp = receiverId;
				String username = messages[1];
				Server.userIp.put(username, receiverId);//存放在在线用户缓存中
				
				//给发送方发送属于他的离线消息
				Iterator<OfflineMsg> iterator = Server.offlineMsgs.iterator();
				JSONArray sendArray = new JSONArray();
				System.out.println("ziji!!");
				while (iterator.hasNext()) {
					OfflineMsg msg = iterator.next();
					if (msg.getReceiveId().equals(username)) {
						JSONObject sendObject = new JSONObject();
						sendObject.put("sendid", msg.getSendId());
						sendObject.put("recei  veid", msg.getReceiveId());
						//sendObject.put("message", msg.getMessage());//特殊处理
						sendObject.put("message", getMsg(msg.getMessage(), msg.getType()));
						sendObject.put("type", msg.getType());
						sendObject.put("time", msg.getTime());
						sendArray.put(sendObject);
						  
						iterator.remove();
					}	
				}
				if(sendArray.toString().equals("[]")) return;//假如没有离线消息，就不用给自己发送消息
				recMsg = receiverIp+" "+sendArray.toString();
			}
			else {//发送方发送消息
				receiverIp = getIp(receiverId);
				System.out.println("接收方的IP："+receiverIp);
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			
			Socket friendsocket = new Socket(/* ip, 8910 */);
			SocketAddress socketAddress = new InetSocketAddress(receiverIp, 8910);
			sendMsg = recMsg.substring(receiverId.length()+1);
			//System.out.println(sendMsg);
			friendsocket.connect(socketAddress, 3000);
			OutputStream out = friendsocket.getOutputStream();
			
			byte[] message2 = sendMsg.getBytes();
			
			out.write(message2);
			out.close();
			friendsocket.close();
		} catch (IOException e) {
			
			// 假如接收方离线
			System.out.println(receiverIp + " 不在线....");
			//先把接收来的消息解析出来
			try {
				//System.out.println(sendMsg+"-------------");
				JSONArray recArray = new JSONArray(sendMsg);
				JSONObject recObject = recArray.getJSONObject(0);
				//将信息填到离线消息这个对象中
				OfflineMsg offlineMsg = new OfflineMsg();
				//offlineMsg.setMessage(recObject.optString("message"));//特殊处理
				offlineMsg.setMessage(storeMsg(recObject.optString("message"), recObject.optInt("type")));
				offlineMsg.setReceiveId(recObject.optString("receiveid"));
				offlineMsg.setSendId(recObject.optString("sendid"));
				offlineMsg.setTime(recObject.optString("time"));
				offlineMsg.setType(recObject.optInt("type"));
				Server.offlineMsgs.add(offlineMsg);
				
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

		}
	}
	
	private String getIp(String username)
	{
		System.out.println(username);
		if(Server.userIp.containsKey(username)) return Server.userIp.get(username);
		try {
			//Connection conn = new DBUtil().getConn();
			String sql = "select currentip from location where username = ?";
			PreparedStatement statement = Server.conn.prepareStatement(sql);
			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) return rs.getString("currentip");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getMsg(String msg,int type)
	{
		//type=1表示文字发送，此时msg保存的为文字本身
		if(type==1) return msg;
		//type!=1表示其他发送，此时msg保存消息的存放路径
		
		try {
			//String path = msg.replaceAll("\\\\", "\\\\\\\\");
			File file=new File(msg);
			//file.delete();
	        FileInputStream fis;
			fis = new FileInputStream(file);
			byte[] buf = new byte[1024];
	        StringBuffer sb=new StringBuffer();
	        //String path = msg.replaceAll("\\\\","\\\\\\\\");
	        while((fis.read(buf))!=-1){
	            sb.append(new String(buf));    
	            buf=new byte[1024];//重新生成，避免和上次读取的数据重复
	        }
	        fis.close();
	        file.delete();
	        System.out.println(msg);
	        try {
				sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	private String storeMsg(String msg,int type)
	{
		//type=1表示文本消息，直接保存到缓存
		if(type==1) return msg;
		//type!=1表示其他消息，只保存路径信息
		File file = null;
		if(type==2){
			file = new File("F:/image/"+new Date().getTime()+".i");
		}
		if(type==3){
			file = new File("F:/voice/"+new Date().getTime()+".v");
		}
		if(type==4){
			file = new File("F:/file/"+new Date().getTime()+".f");
		}
        
		System.err.println(file.getPath());
		try {
			FileOutputStream out = new FileOutputStream(file,true);
	        StringBuffer sb=new StringBuffer(msg);
	        out.write(sb.toString().getBytes("utf-8"));  
	        out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}        
        return file.getPath(); 
		
	}

}
