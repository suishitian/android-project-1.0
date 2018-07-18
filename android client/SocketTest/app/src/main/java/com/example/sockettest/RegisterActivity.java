package com.example.sockettest;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class RegisterActivity extends AppCompatActivity {
    socketJ socket;
    EditText register_username;
    EditText register_account;
    EditText register_password;
    Button register_register;
    final static int REGISTER_FAILED = 0;
    final static int REGISTER_SUCCESSFUL = 1;
    MyHandler myHandler;
    Data app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myHandler = new MyHandler(RegisterActivity.this);
        init();
        register_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(register_account.getText().toString().equals("") || register_password.getText().toString().equals("") ||
                        register_username.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this,"please input all info",Toast.LENGTH_SHORT).show();
                }else{
                    final String content = "register:"+register_username.getText().toString()+"&"+
                            register_account.getText().toString()+"&"+register_password.getText().toString();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            socketJ temp = new socketJ(app.getSendPort());
                            temp.setTimeOut(10000);
                            temp.sendJ(app.getServerIp(),app.getServerPort(),content);
                            Log.d("register","message sent");
                            temp.receiveJS();
                            if(temp.TimeOutFlag==1) {
                                Message message = new Message();
                                message.what = REGISTER_FAILED;
                                myHandler.sendMessage(message);
                            }
                            else{
                                Log.d("register",temp.getString(temp.getClr()));
                                if(temp.getString(temp.getClr()).equals("success")){
                                    //Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                    //startActivity(intent);
                                    Message message = new Message();
                                    message.what = REGISTER_SUCCESSFUL;
                                    myHandler.sendMessage(message);
                                }
                            }
                            temp.close();
                        }
                    }).start();
                }
            }
        });
    }

    public void init(){
        register_username = (EditText)findViewById(R.id.register_username);
        register_account = (EditText)findViewById(R.id.register_account);
        register_password = (EditText)findViewById(R.id.register_password);
        register_register = (Button)findViewById(R.id.register_resgiter);
        //socket = new socketJ(8888);
        app = (Data)getApplication();
    }

    @Override
    protected void onDestroy() {
        socket.close();
        super.onDestroy();
    }

    class MyHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<RegisterActivity> mOuter;

        public MyHandler(RegisterActivity activity) {
            mOuter = new WeakReference<RegisterActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RegisterActivity outer = mOuter.get();
            if (outer != null) {
                // Do something with outer as your wish.
                switch (msg.what){
                    case REGISTER_SUCCESSFUL:
                        Log.d("Login","into the handler");
                        Intent intent1 = new Intent(outer,LoginActivity.class);
                        startActivity(intent1);
                        break;
                    case REGISTER_FAILED:
                        Log.d("register","timeout");
                        Toast.makeText(outer,"register failed",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
