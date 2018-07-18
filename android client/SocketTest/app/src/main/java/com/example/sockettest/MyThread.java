package com.example.sockettest;

import java.net.DatagramPacket;

/**
 * Created by suish on 2018/2/11.
 */

public class MyThread implements Runnable{
    private DatagramPacket packet;
    public MyThread(DatagramPacket dp){
        packet = dp;
    }
    public DatagramPacket getPacket(){
        return packet;
    }
    @Override
    public void run() {

    }
}
