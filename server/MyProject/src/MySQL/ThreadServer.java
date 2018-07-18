package MySQL;

import java.net.DatagramPacket;
import java.util.*;
// the chat information will be directly handled by Thread rece
public class ThreadServer {
	private Thread rece;
	private Thread t1;
	private Thread t2;
	private Thread t3;
	socketJ s1;
	socketJ s2;
	socketJ s3;
	socketJ re;
	int p1,p2,p3,pc;
	int state[] = {0,0,0};
	List<DatagramPacket> a = new ArrayList<>();
	public ThreadServer(int a ,int b,int c,int d){
		pc=a;
		p1=b;
		p2=c;
		p3=d;
		re = new socketJ(pc);
		s1 = new socketJ(p1);
		s2 = new socketJ(p2);
		s3 = new socketJ(p3);
	}
	public void receThreadRun(){
		rece = new Thread(){
			public void run(){
				while(true){
					System.out.println("thread rece !!!");
					re.receiveJS();
					System.out.println(re.getString(re.getClr()));
					if(re.getString(re.getClr()).equals("connect")){
						a.add(re.getClr());
						re.sendBack(re.getClr(), "connection confirmed");
					}
					//need to make sure to receive the IP information after login in
					//use if logic
					else {
						//String IP = re.getString(re.getClr());
						int ppp=Integer.parseInt(re.getString(re.getClr()));
						for(int i=0;i<a.size();i++){
							if(re.getPort((DatagramPacket)a.get(i))==ppp){
								allocate(re.getClr());
								// allocate this host an thread and let him play with other player.
								System.out.println("allocate successfully");
							}
						}
					}
				}
			}
		};
	}
	
	public void allocate(DatagramPacket pb){
		if(state[0]==0) t1_run(re.getClr());
		else if(state[1]==0) t2_run(re.getClr());
		else if(state[2]==0) t3_run(re.getClr());
	}

	public void t1_start(DatagramPacket p){
		final DatagramPacket p1 = p;
		t1 = new Thread(){
			public void run(){
				System.out.println("Thread 111");
				s1.client1(p1);
				s1.sendBack(s1.getCl1(), "comfirm");
				s1.sendJ("127.0.0.1", Integer.parseInt(s1.getString(p1)), "request");
				while(true){
					s1.receiveJS();
					System.out.print("Thread 1:"+s1.getString(s1.getClr()));
					if(s1.getClr().equals("accept")){
						s1.client2(s1.getClr());
						s1.sendBack(s1.getCl1(),"success");
						break;
					}
				}
				while(true){
					s1.receiveJS();
					if(s1.getIP(s1.getClr()).equals(s1.getIP(s1.getCl1()))){
						if(s1.getString(s1.getClr()).equals("close")){
							System.out.println("Thread 1 from client1:"+s1.getString(s1.getClr()));
							s1.sendBack(s1.getCl2(), "client1 request closed");
							break;
						}
						else{ 
							System.out.println("Thread 1 from client1:"+s1.getString(s1.getClr()));
							s1.sendBack(s1.getCl2(), s1.getString(s1.getClr()));
						}
					}
					else if(s1.getIP(s1.getClr()).equals(s1.getIP(s1.getCl2()))){
						if(s1.getString(s1.getClr()).equals("close")){
							System.out.println("Thread 1 from client2:"+s1.getString(s1.getClr()));
							s1.sendBack(s1.getCl2(), "client2 request closed");
							break;
						}
						else{ 
							System.out.println("Thread 1 from client2:"+s1.getString(s1.getClr()));
							s1.sendBack(s1.getCl1(), s1.getString(s1.getClr()));
						}
					}
				}
				System.out.println("Thread 1 close");
			}
		};
	}
	
	public void t1_run(DatagramPacket p){
		t1_start(p);
		t1.start();
		state[0]=1;
	}
	public void t2_start(DatagramPacket p){
		final DatagramPacket p1 = p;
		t2 = new Thread(){
			public void run(){
				System.out.println("Thread 222");
				s2.client1(p1);
				s2.sendBack(s2.getCl1(), "comfirm");
				s2.sendJ(s2.getIP(p1), s2.getPort(p1), "request");
				while(true){
					//every while loop should have limit time of waiting , if cannot rece the info from client ,kill it
					s2.receiveJS();
					System.out.print(s2.getString(s2.getClr()));
					if(s2.getClr().equals("accept")){
						s2.client2(s2.getClr());
						s2.sendBack(s2.getCl1(),"success");
						break;
					}
				}
				while(true){
					s2.receiveJS();
					if(s2.getIP(s2.getClr()).equals(s2.getIP(s2.getCl1()))){
						if(s2.getString(s2.getClr()).equals("close")){
							System.out.println("Thread 2 from client1:"+s2.getString(s2.getClr()));
							s2.sendBack(s2.getCl2(), "client1 request closed");
							break;
						}
						else{ 
							System.out.println("Thread 2 from client1:"+s2.getString(s2.getClr()));
							s2.sendBack(s2.getCl2(), s2.getString(s2.getClr()));
						}
					}
					else if(s2.getIP(s2.getClr()).equals(s2.getIP(s2.getCl2()))){
						if(s2.getString(s2.getClr()).equals("close")){
							System.out.println("Thread 2 from client2:"+s2.getString(s2.getClr()));
							s2.sendBack(s2.getCl2(), "client2 request closed");
							break;
						}
						else{ 
							System.out.println("Thread 2 from client2:"+s2.getString(s2.getClr()));
							s2.sendBack(s2.getCl1(), s2.getString(s2.getClr()));
						}
					}
				}
				System.out.println("Thread 2 close");
			}
		};
	}
	
	public void t2_run(DatagramPacket p){
		t2_start(p);
		t2.start();
		state[1]=1;
	}
	public void t3_start(DatagramPacket p){
		final DatagramPacket p1 = p;
		t2 = new Thread(){
			public void run(){
				System.out.println("Thread 333");
				s3.client1(p1);
				s3.sendBack(s3.getCl1(), "comfirm");
				s3.sendJ(s3.getIP(p1), s3.getPort(p1), "request");
				while(true){
					//every while loop should have limit time of waiting , if cannot rece the info from client ,kill it
					s3.receiveJS();
					System.out.print(s3.getString(s3.getClr()));
					if(s3.getClr().equals("accept")){
						s3.client2(s3.getClr());
						s3.sendBack(s3.getCl1(),"success");
						break;
					}
				}
				while(true){
					s3.receiveJS();
					if(s3.getIP(s3.getClr()).equals(s3.getIP(s3.getCl1()))){
						if(s3.getString(s3.getClr()).equals("close")){
							System.out.println("Thread 3 from client1:"+s3.getString(s3.getClr()));
							s3.sendBack(s3.getCl2(), "client1 request closed");
							break;
						}
						else{ 
							System.out.println("Thread 3 from client1:"+s3.getString(s3.getClr()));
							s3.sendBack(s3.getCl2(), s3.getString(s3.getClr()));
						}
					}
					else if(s3.getIP(s3.getClr()).equals(s3.getIP(s3.getCl2()))){
						if(s3.getString(s3.getClr()).equals("close")){
							System.out.println("Thread 3 from client2:"+s3.getString(s3.getClr()));
							s3.sendBack(s3.getCl2(), "client2 request closed");
							break;
						}
						else{ 
							System.out.println("Thread 3 from client2:"+s3.getString(s3.getClr()));
							s3.sendBack(s3.getCl1(), s3.getString(s3.getClr()));
						}
					}
				}
				System.out.println("Thread 3 close");
			}
		};
	}
	
	public void t3_run(DatagramPacket p){
		t3_start(p);
		t3.start();
		state[2]=1;
	}
	public void process(){
		receThreadRun();
		rece.start();
	}
}