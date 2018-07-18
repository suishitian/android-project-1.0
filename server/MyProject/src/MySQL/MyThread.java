package MySQL;

import java.net.DatagramPacket;

public class MyThread implements Runnable{
	protected DatagramPacket p;
	public MyThread(DatagramPacket p){
		this.p = p;
	}
	@Override
	public void run(){}
}
