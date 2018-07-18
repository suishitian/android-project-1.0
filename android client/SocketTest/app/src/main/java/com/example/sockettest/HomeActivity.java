package com.example.sockettest;

        import android.app.Fragment;
        import android.app.FragmentManager;
        import android.app.FragmentTransaction;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.os.Handler;
        import android.os.Message;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

        import java.lang.ref.WeakReference;
        import java.net.DatagramPacket;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private Button Friends;
    private Button Chat;
    private Button Team;
    private Button Moments;
    private socketJ socket;
    private MyHandler handler;
    public static socketJ socket_send;
    private IntentFilter intentFilter;
    private MyReceiver receiver;
    //final static int RECEIVE_CHAT = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.sockettest.HomeService_RECEIVE_PACKET");
        receiver = new MyReceiver();
        registerReceiver(receiver,intentFilter);
    }
    public void init(){
        Friends = (Button)findViewById(R.id.Home_Friends);
        Chat = (Button)findViewById(R.id.Home_Chat);
        Team = (Button)findViewById(R.id.Home_Team);
        Moments = (Button)findViewById(R.id.Home_Moments);
        socket_send = new socketJ(8888);
        socket = new socketJ(8889);
        handler = new MyHandler(this);
        Friends.setOnClickListener(this);
        Chat.setOnClickListener(this);
        Team.setOnClickListener(this);
        Moments.setOnClickListener(this);
    }

    /*public static synchronized void threadSendBack(DatagramPacket dp, String content){
        HomeActivity.socket_send.sendBack(dp,content);
    }*/

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.Home_Friends:
                Log.d("Home","into the handler");
                replaceFragment(new FriendsFragment());
                break;
            case R.id.Home_Chat:
                replaceFragment(new ChatFragment());
                break;
            case R.id.Home_Team:
                replaceFragment(new TeamFragment());
                break;
            case R.id.Home_Moments:
                //replaceFragment(new MomentsFragment());
                Intent intent = new Intent(HomeActivity.this,ChatActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    private void replaceFragment(android.support.v4.app.Fragment fragment){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction tran = fragmentManager.beginTransaction();
        tran.replace(R.id.Home_Frame,fragment);
        tran.commit();
    }

    class MyHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<HomeActivity> mOuter;

        public MyHandler(HomeActivity activity) {
            mOuter = new WeakReference<HomeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HomeActivity outer = mOuter.get();
            if (outer != null) {
                // Do something with outer as your wish.
                switch (msg.what){
                    default:
                        break;
                }
            }
        }
    }
    class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String content = (String)intent.getExtras().get("content");
            Toast.makeText(context,"BroadCast received",Toast.LENGTH_SHORT).show();
            Log.d("HomeActivityBBB:",content);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.sockettest.HomeService_RECEIVE_PACKET");
        receiver = new MyReceiver();
        registerReceiver(receiver, intentFilter);
    }
}
