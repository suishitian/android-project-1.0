package com.example.sockettest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{
    EditText editText;
    Button send;
    Button record;
    RecyclerView recyclerView;
    MsgAdapter adapter;
    List<MSG> msgList;
    //socketJ socket;
    MyHandler handler;
    final static int RECEIVE_CHAT = 0;
    final static int SEND_CHAT = 1;
    private IntentFilter intentFilter;
    private MyReceiver receiver;
    private Data app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MSG msg1 = new MSG("nihao a ",MSG.RECEIVE_MESSAGE,MSG.NO_REC,0,"other people");
        MSG msg2 = new MSG("wo buhao fear",MSG.SENT_MESSAGE,MSG.NO_REC,0,"me");
        msgList.add(msg1);
        msgList.add(msg2);
        adapter = new MsgAdapter(msgList);
        recyclerView.setAdapter(adapter);
        //intentFilter = new IntentFilter();
        //intentFilter.addAction("com.example.sockettest.HomeService_RECEIVE_PACKET");
        //receiver = new MyReceiver();
        //registerReceiver(receiver,intentFilter);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    socket.receiveJS();
                    Log.d("CHAT",socketJ.getString(socket.getClr()));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            PacketOperation po = new PacketOperation(socket.getClr(),0);
                            MSG msg = po.packetOperateReceiveChat();
                            Message message = new Message();
                            message.what = RECEIVE_CHAT;
                            message.obj = msg;
                            handler.sendMessage(message);
                        }
                    }).start();
                }
            }
        }).start();*/
    }
    public void init(){
        editText = (EditText)findViewById(R.id.Chat_EditText);
        send = (Button)findViewById(R.id.Chat_Send);
        record = (Button)findViewById(R.id.Chat_Record);
        recyclerView = (RecyclerView)findViewById(R.id.Chat_RecyclerView);
        msgList = new ArrayList<>();
        //socket = new socketJ(8899);
        handler = new MyHandler(this);
        send.setOnClickListener(this);
        record.setOnClickListener(this);
        app = (Data)getApplication();
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.Chat_Send:
                final String content = editText.getText().toString();
                final String account = "yaoyao";
                MSG msg = new MSG(content,MSG.SENT_MESSAGE,MSG.NO_REC,0,"me");
                msgList.add(msg);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        socketJ socket = new socketJ(app.getSendPort());
                        socket.setTimeOut(100000);
                        String data = "chat:"+content+"&"+account+"&"+app.getUserName();//chat:content&account(to)&account(from)
                        socket.sendJ(app.getServerIp(),app.getServerPort(),data);
                        Log.d("Chat",data);
                        socket.close();
                    }
                }).start();
                adapter.notifyItemInserted(msgList.size()-1);
                recyclerView.scrollToPosition(msgList.size()-1);
                editText.setText("");
                break;
            default:
                break;
        }
    }
    class MyHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<ChatActivity> mOuter;

        public MyHandler(ChatActivity activity) {
            mOuter = new WeakReference<ChatActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            ChatActivity outer = mOuter.get();
            if (outer != null) {
                // Do something with outer as your wish.
                switch (msg.what){
                    case RECEIVE_CHAT:
                        MSG msg1 = (MSG)msg.obj;
                        Log.d("chat",msg1.getName());
                        msgList.add(msg1);
                        adapter.notifyItemInserted(msgList.size()-1);
                        recyclerView.scrollToPosition(msgList.size()-1);
                    /*case SEND_CHAT:
                        MSG msg2 = (MSG)msg.obj;
                        Log.d("chat",msg2.getName());
                        msgList.add(msg2);
                        adapter.notifyItemInserted(msgList.size()-1);
                        recyclerView.scrollToPosition(msgList.size()-1);*/
                    default:
                        break;
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        //socket.close();
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String content = (String)intent.getExtras().get("content");
            //Toast.makeText(context,"BroadCast received",Toast.LENGTH_SHORT).show();
            Log.d("ChatActivityBBB:",content);
            String name = "";
            String text = "";
            String[] data = content.split(":");
            if(data[0].equals("chat")){
                text = data[1].split("&")[0];
                name = data[1].split("&")[1];
            }
            MSG msg = new MSG(text,MSG.RECEIVE_MESSAGE,MSG.NO_REC,0,name);
            Message message = new Message();
            message.what = RECEIVE_CHAT;
            message.obj = msg;
            handler.sendMessage(message);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.sockettest.HomeService_RECEIVE_PACKET");
        receiver = new MyReceiver();
        registerReceiver(receiver,intentFilter);
    }
}
