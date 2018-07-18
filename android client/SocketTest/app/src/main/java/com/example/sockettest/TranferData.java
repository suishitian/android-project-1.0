package com.example.sockettest;

import android.util.Log;

import java.net.DatagramPacket;

/**
 * Created by suish on 2018/1/24.
 */

public class TranferData {
    private socketJ socket;
    private String ip;
    private int target_port;
    private volatile DatagramPacket response;
    public TranferData(socketJ socket,String ip,int port){
        this.socket = socket;
        this.ip = ip;
        target_port = port;
    }
    public void setTimeOut(int i){
        socket.setTimeOut(i);
    }
    public String sendAndWaitResponse(String content){
        final String data = content;
        new Thread(new Runnable() {
            @Override
            public void run() {
                socket.sendJ(ip,target_port,data);
                Log.d("Login","Send OK");
                socket.receiveJS();
                Log.d("Login","Receive OK");
                response = socket.getClr();
            }
        }).start();
        if(socket.TimeOutFlag == 1){
            return "TimeOut";
        }
        else if(socket.getClr()!=null){
            return socket.getString(socket.getClr());
        }else return "Error";
    }
}
