package com.example.sockettest;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.net.DatagramPacket;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText login_account;
    EditText login_password;
    CheckBox checkBox;
    Button login_login;
    Button login_register;
    socketJ temp;
    Data app;
    final static int LOGIN_SUCCESSFUL = 0;
    final static int LOGIN_FAILED = 1;
    MyHandler myHandler;
    int port1 = 8888;
    int port2 = 8889;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        login_login.setOnClickListener(this);
        login_register.setOnClickListener(this);
        myHandler = new MyHandler(LoginActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_login1:
                Log.d("login", "hahaha");
                DatagramPacket ans = null;
                final String packet = "login:" + login_account.getText().toString() + "&" + login_password.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int port_c = app.getRecePort();
                        temp = new socketJ(port_c);
                        Log.d("Login : port",""+app.getRecePort());
                        temp.setTimeOut(10000);
                        temp.sendJ(app.getServerIp(), app.getServerPort(), packet);
                        //zushe UI
                        temp.receiveJS();
                        if (temp.TimeOutFlag == 1) {
                            Log.d("login", "timeout");
                            Message message = new Message();
                            message.what = LOGIN_FAILED;
                            myHandler.sendMessage(message);
                        }
                        else {
                            if (temp.getString(temp.getClr()).equals("login success")) {
                                //Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                //startActivity(intent);
                                Log.d("login", temp.getString(temp.getClr()));
                                Message message = new Message();
                                message.what = LOGIN_SUCCESSFUL;
                                myHandler.sendMessage(message);
                            }
                        }
                        temp.close();
                    }
                }).start();
                break;
            case R.id.login_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                Log.d("login", "default");
                break;
        }
    }

    public void init() {
        app = (Data)getApplication();
        login_account = (EditText) findViewById(R.id.login_account);
        login_password = (EditText) findViewById(R.id.login_password);
        checkBox = (CheckBox) findViewById(R.id.login_checkBox);
        login_login = (Button) findViewById(R.id.login_login1);
        login_register = (Button) findViewById(R.id.login_register);
        //temp = new socketJ(port2);
    }

    @Override
    protected void onDestroy() {
        //temp.close();
        myHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    class MyHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<LoginActivity> mOuter;

        public MyHandler(LoginActivity activity) {
            mOuter = new WeakReference<LoginActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity outer = mOuter.get();
            if (outer != null) {
                // Do something with outer as your wish.
                switch (msg.what){
                    case LOGIN_SUCCESSFUL:
                        Log.d("Login","into the handler");
                        //there should also launch a watch dog service(send message, after server receive watchdog_message
                        // create a new long-time thread holding socket to maintain watchdog function)
                        Intent intent_service = new Intent(outer,HomeService.class);
                        startService(intent_service);
                        Intent intent1 = new Intent(outer,HomeActivity.class);
                        startActivity(intent1);
                        break;
                    case LOGIN_FAILED:
                        Toast.makeText(outer,"login failed",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
