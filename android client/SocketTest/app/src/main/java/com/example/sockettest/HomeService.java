package com.example.sockettest;

import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.DatagramPacket;

/**
 * Created by suish on 2018/2/6.
 */

public class HomeService extends Service {
    //private int rece_port;
    Data app;
    private socketJ socket; //rece_socket;
    public final static int HOME_PAGES = 0;
    public final static int CHAT_PAGES = 1;
    //private Binder binder;

    @Override
    public void onCreate() {
        Log.d("HomeService","into the onCreate");
        super.onCreate();
        app = (Data)getApplication();
        int port_app = app.getRecePort();
        Log.d("HomeService app=",""+port_app);
        socket = new socketJ(app.getRecePort());
        Log.d("HomeService","Service Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //socket.sendJ(app.getServerIp(),app.getServerPort(),"connection:"+app.getAccount());
        //socket.receiveJS();
        //if(socket.TimeOutFlag!=1){
        //    Intent intent = new
        //}
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("HomeService","onCreate");
                while(true){
                    Log.d("HomeService","Listening");
                    socket.receiveJS();
                    //final DatagramPacket packet = socket.getClr();
                    MyThread thread = new MyThread(socket.getClr()){
                        @Override
                        public void run() {
                            Log.d("HomeService",socketJ.getString(socket.getClr()));
                            String content = socketJ.getString(getPacket());
                            socket.sendBack(getPacket(),"confirm");
                            Log.d("HomeService","confirm sent");
                            Intent intent = new Intent("com.example.sockettest.HomeService_RECEIVE_PACKET");
                            intent.putExtra("content",content);
                            sendBroadcast(intent);
                            Log.d("HomeService","broadCast sent");
                        }
                    };
                    new Thread(thread).start();
                   /* new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("HomeService",socketJ.getString(socket.getClr()));
                            String content = socketJ.getString(getPacket());
                            socket.sendBack(getPacket(),"confirm");
                            Log.d("HomeService","confirm sent");
                            Intent intent = new Intent("com.example.sockettest.HomeService_RECEIVE_PACKET");
                            intent.putExtra("content",content);
                            sendBroadcast(intent);
                            Log.d("HomeService","broadCast sent");
                        }
                    }).start();*/
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        socket.close();
        super.onDestroy();
    }
}
