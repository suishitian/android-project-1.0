package com.example.sockettest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private socketJ socket;
    private EditText editText;
    private Button button;
    public TextView textView;
    private MyHandler handler;
    public static final int NEW_MESSAGE_RECEIVED = 1;
    public static final int NEW_MESSAGE_SENT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        socket = new socketJ(8888);
        handler = new MyHandler(this);
        button = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        button.setOnClickListener(this);
        Log.d("MainActivity","oncreate");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    socket.receiveJS();
                    Message message = new Message();
                    message.what = NEW_MESSAGE_RECEIVED;
                    message.obj = socket.getString(socket.getClr());
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Button","Button");
                        String sent = editText.getText().toString();
                        socket.sendJ("192.168.3.4", 8889,sent);
                        Message message = new Message();
                        message.what = NEW_MESSAGE_SENT;
                        message.obj = sent;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    class MyHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<MainActivity> mOuter;

        public MyHandler(MainActivity activity) {
            mOuter = new WeakReference<MainActivity>(activity);
        }

        @Override
        public synchronized void handleMessage(Message msg) {
            MainActivity outer = mOuter.get();
            if(outer != null) {
                switch (msg.what){
                    case NEW_MESSAGE_RECEIVED:
                        String content_r = (String)msg.obj;
                        String text_r = textView.getText().toString();
                        text_r += "Server:\n\t"+content_r+'\n';
                        textView.setText(text_r);
                        //Log.d("RECEIVED",(String)msg.obj);
                        break;
                    case NEW_MESSAGE_SENT:
                        String content_s = (String)msg.obj;
                        String text_s = textView.getText().toString();
                        text_s += "Client:\n\t"+content_s+'\n';
                        textView.setText(text_s);
                        //Log.d("SENT",(String)msg.obj);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
