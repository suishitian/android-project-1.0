package com.example.sockettest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by suish on 2018/2/1.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<MSG> msgList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private View view;
        LinearLayout left_layout;
        LinearLayout right_layout;
        TextView left_name;
        TextView right_name;
        TextView left_content;
        TextView right_content;
        Button left_button;
        Button right_button;
        public ViewHolder(View view){
            super(view);
            left_layout = (LinearLayout)view.findViewById(R.id.Chat_Content_Layout_LEFT);
            right_layout = (LinearLayout)view.findViewById(R.id.Chat_Content_Layout_RIGHT);
            left_name = (TextView)view.findViewById(R.id.Chat_Content_Sender_LEFT);
            right_name = (TextView)view.findViewById(R.id.Chat_Content_Sender_RIGHT);
            left_content = (TextView)view.findViewById(R.id.Chat_Content_MSG_LEFT);
            right_content = (TextView)view.findViewById(R.id.Chat_Content_MSG_RIGHT);
            left_button = (Button)view.findViewById(R.id.Chat_Content_REC_LEFT);
            right_button = (Button)view.findViewById(R.id.Chat_Content_REC_RIGHT);
        }
    }
    public MsgAdapter(List<MSG> msgList){
        this.msgList = msgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_content,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MSG msg = msgList.get(position);
        if(msg.getMessageType() == MSG.RECEIVE_MESSAGE){
            //RECEIVE = LEFT V // RIGHT X
            holder.left_layout.setVisibility(View.VISIBLE);
            holder.left_name.setVisibility(View.VISIBLE);
            holder.right_layout.setVisibility(View.GONE);
            holder.right_name.setVisibility(View.GONE);
            holder.left_content.setText(msg.getContent());
            holder.left_name.setText(msg.getName());
            if(msg.getRecFlag() == MSG.NO_REC){
                holder.right_button.setVisibility(View.GONE);
                holder.left_button.setVisibility(View.GONE);
            }else{
                holder.right_button.setVisibility(View.VISIBLE);
                holder.left_button.setVisibility(View.VISIBLE);
                //so something to make the REC work here
            }
        }else if(msg.getMessageType() == MSG.SENT_MESSAGE){
            //RECEIVE = LEFT X // RIGHT V
            holder.right_layout.setVisibility(View.VISIBLE);
            holder.right_name.setVisibility(View.VISIBLE);
            holder.left_layout.setVisibility(View.GONE);
            holder.left_name.setVisibility(View.GONE);
            holder.right_content.setText(msg.getContent());
            holder.right_name.setText(msg.getName());
            if(msg.getRecFlag() == MSG.NO_REC){
                holder.right_button.setVisibility(View.GONE);
                holder.left_button.setVisibility(View.GONE);
            }else {
                holder.right_button.setVisibility(View.VISIBLE);
                holder.left_button.setVisibility(View.VISIBLE);
                //so something to make the REC work here
            }
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }
}
