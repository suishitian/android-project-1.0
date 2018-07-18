package com.example.sockettest;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suish on 2018/1/31.
 */

public class ChatFragment extends android.support.v4.app.Fragment{
    RecyclerView recyclerView;
    View view;
    List<String> list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat,container,false);
        initChat();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        NameAdapter adapter = new NameAdapter(list);
        recyclerView.setAdapter(adapter);
        return view;
    }
    public void initChat(){
        //here is to make a function to init this fragment first before the fragment created
        recyclerView = (RecyclerView)view.findViewById(R.id.Home_Chat_RecyclerView);
        list = new ArrayList<String>();
        String name = "name";
        for(int i=0;i<10;i++){
            list.add(name);
        }
    }
}
