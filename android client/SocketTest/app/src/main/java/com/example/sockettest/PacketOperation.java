package com.example.sockettest;

import java.net.DatagramPacket;

/**
 * Created by suish on 2018/2/2.
 */

public class PacketOperation {
    Packet packet;
    int operationID;
    int port;
    //static socketJ socket;
    public static DatagramPacket dp;
    final static int NOTHING = 0;
    final static int LOGIN = 1;
    final static int REGISTER = 2;
    final static int CHAT = 3;

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
                case "chat":
                    operationID = CHAT;
                    break;
                case "register":
                    operationID = REGISTER;
                    break;
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
                case CHAT:
                    packetOperateReceiveChat();
                    break;
                default:
                    break;
            }
        }
    }
    public MSG packetOperateReceiveChat(){
        String[] data = packet.get(1).split("&");
        MSG msg = new MSG(data[0],MSG.RECEIVE_MESSAGE,MSG.NO_REC,0,data[1]);
        return msg;
    }
}