package com.example.sockettest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by suish on 2018/1/31.
 */

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.ViewHolder> {
    private List<String> list;

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textView = (TextView) itemView.findViewById(R.id.Home_Chat_Item_textView);
        }
    }

    public NameAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = list.get(position);
        holder.textView.setText(name);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something to open the chat activity
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
        String name = list.get(position);
        holder.textView.setText("name");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
