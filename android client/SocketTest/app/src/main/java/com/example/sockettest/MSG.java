package com.example.sockettest;

/**
 * Created by suish on 2018/2/1.
 */

public class MSG {
    private String text;
    final static int RECEIVE_MESSAGE = 0;  //left
    final static int SENT_MESSAGE = 1; //right
    private int ori_flag;
    private int rec_flag;
    final static int HAVE_REC = 3;
    final static int NO_REC = 4;
    private int Rec_time;
    private String name;
    //here is place to hold the audio file
    public MSG(String text,int messageType,int Rec,int Rec_time,String name){
        this.text = text;
        ori_flag = messageType;
        rec_flag = Rec;
        this.Rec_time = Rec_time;
        this.name = name;
    }
    public String getContent(){
        return text;
    }
    public int getMessageType(){
        return ori_flag;
    }
    public int getRecFlag(){
        return rec_flag;
    }
    public void getRec(){
        return;
    }
    public int getRecTime(){
        return Rec_time;
    }
    public String getName(){
        return name;
    }
}
