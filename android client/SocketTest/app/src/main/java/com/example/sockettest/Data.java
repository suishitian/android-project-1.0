package com.example.sockettest;

import android.app.Application;
import android.util.Log;

/**
 * Created by suish on 2018/2/8.
 */

public class Data extends Application {
    private int rece_port;
    private int send_port;
    private String server_ip = "192.168.1.102";
    private int server_port = 8889;
    private String username = "yaoyao";
    private String account;
    //there should be an adapter of friend list(returned from server during launching)
    //there should be an adapter of offline line list (returned from server during launching)
    //
    @Override
    public void onCreate() {
        super.onCreate();
        init();
        Log.d("Login","app created");
    }
    protected void init(){
        send_port = 8899;
        rece_port = 9999;
        server_ip = "192.168.1.104";
        server_port = 8889;
        username = "yaoyao";
        account = "yaoyao";
        //send_socket = new socketJ(send_port);
        //rece_socket = new socketJ(rece_port);
    }
    public void  setSendPort(int num){
        send_port = num;
    }
    public int getServerPort() { return server_port; }
    public String getServerIp() { return server_ip; }
    public int getSendPort(){
        return send_port;
    }
    public void setRecePort(int num){
        rece_port = num;
    }
    public int getRecePort(){
        return rece_port;
    }
    public void setUserName(String us) { username = us; }
    public String getUserName() { return username; }
    public String getAccount() { return account; }
    public void setAccount(String mn) { account = mn; }
    //public socketJ getReceSocket() { return rece_socket; }
    //public socketJ getSendScoket() { return send_socket; }
    //public void setTimeOutRece(int num){
    //    rece_socket.setTimeOut(num);
    //}
    //public void setTimeOutSend(int num){
    //    send_socket.setTimeOut(num);
    //}
}
