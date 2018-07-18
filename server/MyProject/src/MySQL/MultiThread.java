package MySQL;
import java.net.DatagramPacket;
import java.util.*;

public class MultiThread {
	private Thread rece;
	private static int port;
	private static int start_port;
	private int current_port;
	private volatile int current_num;
	private socketJ server;
	public static socketJ thread_use;
	//private ArrayList<String> current_login;
	public static HashMap<String,DatagramPacket> current_login;
	public static volatile int[] port_array;
	public static final int USER_ONLINE = 1;
	public static final int USER_OFFLINE = 2;
	public static final int LOGIN_LIMIT = 100;
	
	public static synchronized void packetOperateSetHashMap(String account,DatagramPacket dp){
		if(current_login.containsKey(account)){
			current_login.remove(account);
			current_login.put(account,dp);
			System.out.println("current_login put:"+account);
		}else{
			current_login.put(account,dp);
			System.out.println("current_login put:"+account);
		}
	}
	public static synchronized DatagramPacket packetOperateGetHashMap(String account){
		if(!current_login.containsKey(account)) return null;
		else return current_login.get(account);
	}
	public static synchronized void packetOperateSendBack(DatagramPacket dp, String content){
		System.out.println("threadSendBack");
		thread_use.sendBack(dp,content);
		System.out.println("threadSendBack success");
	}
	public static synchronized void packetOperateDeleteHashMap(String account){
		current_login.remove(account);
		System.out.println("delete login packet: "+account);
	}
	public MultiThread(int port){
		this.port = port;
		start_port = 9000;
		port_array = new int[100];
		for(int i=0;i<port_array.length;i++){
			port_array[i] = 0;
		}
		server = new socketJ(port);
		thread_use = new socketJ(port+1);
		current_login = new HashMap<>();
		current_num = 0;
	}
	
	public static synchronized int getOnePort(){
		for(int i=0;i<port_array.length;i++){
			if(port_array[i]==0){
				port_array[i] = 1;
				return start_port+1+i;
			}
		}
		return -1;
	}
	public static synchronized void releaseOnePort(int num){
		int index = num-start_port-1;
		if(port_array[index]==1) port_array[index] = 0;
	}
	
	public void receThreadRun(){
		rece = new Thread(new Runnable(){
			@Override
			public void run(){
				while(true){
					System.out.println("waiting");
					server.receiveJS();
					//here is the analyze of packet info
					System.out.println(socketJ.getString(server.getClr()));
					//server.sendBack(server.getClr(), socketJ.getString(server.getClr()));
					//System.out.println("sendBack");
					MyThread thread = new MyThread(server.getClr()){
						@Override
						public void run(){
							System.out.println("start new thread");
							PacketOperation operation = new PacketOperation(p,port+1);
							operation.packetAnalyze();
							operation.packetOperate();
						}
					};
					new Thread(thread).start();
				}
			}
		});
		rece.start();
	}
	
	public void createNewThread(int port,String username){
		final String user = username;
		final int port_num = port;
		
		Thread user_thread = new Thread(new Runnable(){
			volatile int flag = USER_OFFLINE;
			int port = port_num;
			DatagramPacket user_packet = current_login.get(user);
			socketJ user_socket;
			
			@Override
			public void run(){
				user_socket = new socketJ(port);
				user_socket.setTimeOut(1000);
				user_packet = current_login.get(user);
				current_num++;
				
				Thread watch_dog = new Thread(){
					@Override
					public void run(){
						super.run();
						while(!this.isInterrupted()){
							//watch_dog
						}
					}
				};
				watch_dog.start();
				while(flag==USER_ONLINE){
					//receive
					//if need to send something -> new thread
					//before extract packet of cl_r
					//call if(cl_r==null) to test if the socket receive the packet or time out
				}
				watch_dog.interrupt();
				current_num--;
			}
		});
		user_thread.start();
	}
}
