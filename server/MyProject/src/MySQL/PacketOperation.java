package MySQL;

import java.net.DatagramPacket;

public class PacketOperation {
	Packet packet;
	int operationID;
	int port;
	//static socketJ socket;
	public static DatagramPacket dp;
	final static int NOTHING = 0;
	final static int LOGIN = 1;
	final static int REGISTER = 2;
	final static int SUCCESS = 3;
	final static int ERROR_USRNAME_EXIST = 4;
	final static int ONLINE = 5;
	final static int OFFLINE = 6;
	final static int TEST_CHAT = 7;
	
	public PacketOperation(DatagramPacket p,int port){
		dp = p;
		packet = new Packet(socketJ.getString(dp));
		operationID = NOTHING;
		this.port = port;
		//socket = new socketJ(port);
	}
	/*public static synchronized void packetSendBack(String content){
		System.out.println("packetSendBack");
		socket.sendBack(dp,content);
		System.out.println("packetSendBack success");
	}*/
	public void packetAnalyze(){
		System.out.println("packetAnalyze");
		if(packet!=null){
			switch(packet.get(0)){
			case "login":
				operationID = LOGIN;
				break;
			case "register":
				operationID = REGISTER;
				break;
			case "chat":
				operationID = TEST_CHAT;
			default:
				break;
			}
		}
	}
	public void packetOperate(){
		System.out.println("packetOperate");
		if(packet!=null){
			switch(operationID){
			case NOTHING:
				break;
			case LOGIN:
				packetOperateLogin();
				break;
			case REGISTER:
				packetOperateRegister();
				break;
			case TEST_CHAT:
				packetOperateChatReceive();
			default:
				break;
			}
		}
	}
	public void packetOperateLogin(){
		System.out.println("packetOperationLogin");
		String[] data = packet.get(1).split("&");
		String password = mysqlJ.getSingleData("usr_info","usr_password","usr_account",data[0],"char");
		if(password.equals(data[1])){
			mysqlJ.updateSingleData("usr_info","status","5","usr_account",data[0]
					,new String[]{"int","char"});
			MultiThread.packetOperateSendBack(dp,"login success");
			MultiThread.packetOperateSetHashMap(data[0], dp);
		}else{
			MultiThread.packetOperateSendBack(dp,"PASSWORD_WRONG");
		}
	}
	public void packetOperateRegister(){
		System.out.println("packetOperateRegister");
		String[] data = packet.get(1).split("&");
		if(mysqlJ.getColDataSet("usr_info", "usr_account").contains(data[1])){
			MultiThread.packetOperateSendBack(dp,"ERROR_USRNAME_EXIST");
			//return ERROR_USRNAME_EXIST;
		}else{
			mysqlJ.insertData("usr_info",new String[]{"NULL",data[0],data[1],data[2],"NULL","NULL"}
				,new String[]{"int","char","char","char","int","int"});
			mysqlJ.createFriendsList(data[1]);
			MultiThread.packetOperateSendBack(dp, "success");
			//there should be a function to create Mysql List for the new user.
			//(1) offline message list
			//(2) Friends list
		}
	}
	public void packetOperateChatReceive(){
		System.out.println("packetOperateChatReceive");
		String[] data = packet.get(1).split("&");
		String content = data[0];
		String toAccount = data[1];
		String FromAccount = data[2];
		String FromUserName = mysqlJ.getSingleData("usr_info","usr_name",
				"usr_account",FromAccount,"char");
		DatagramPacket toP = MultiThread.packetOperateGetHashMap(toAccount);
		if(toP==null){
			System.out.println("no login packet");
			writeOfflineChat(toAccount,content);
		}else{
			System.out.println("send to: ip-"+toP.getAddress().toString()+" port-"+toP.getPort());
			int temp_port = MultiThread.getOnePort();
			System.out.println("get one port: "+temp_port);
			socketJ temp_socket = new socketJ(temp_port);
			temp_socket.setTimeOut(10000);
			String content2 = "chat:"+content+"&"+FromUserName+"&"+FromAccount;
			temp_socket.sendBack(toP,content2);
			System.out.println("chat send mid-packet: "+content2);
			temp_socket.receiveJS();
			if(temp_socket.TimeOutFlag==1){
				System.out.println("no response , kick off");
				writeOfflineChat(toAccount,content);
				//MultiThread.packetOperateDeleteHashMap(toAccount);
			}else{
				System.out.println(socketJ.getString(temp_socket.getClr()));
			}
			temp_socket.close();
			MultiThread.releaseOnePort(temp_port);
			System.out.println("release port");
		}
	}
	
	public void writeOfflineChat(String account,String content){
		
	}
	public boolean isHaveOfflineChat(String account){
		return false;
	}
	public void clearOfflineChat(String account){
		
	}
}
