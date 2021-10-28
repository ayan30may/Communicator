package com.example.myapplicationchat.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplicationchat.R;
import com.example.myapplicationchat.model.CallList;
import com.example.myapplicationchat.Adapter.CallListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CallsFragment extends Fragment {
    public CallsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_calls, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<CallList> list = new ArrayList<>();

        list.add(new CallList("11",
                "Dhoni",
                "12/02/12,05:12 p.m.",
                "https://cdn.dnaindia.com/sites/default/files/styles/full/public/2020/07/07/912274-ms-dhoni-file.jpg",
                "missed"));

        list.add(new CallList("22",
                "kohli",
                "12/02/12,05:15 p.m.",
                "https://c.ndtvimg.com/2020-05/tkqluj48_virat-kohli-afp_625x300_30_May_20.jpg",
                "income"));

        list.add(new CallList("11",
                "csk",
                "12/02/12,05:20 p.m.",
                "https://upload.wikimedia.org/wikipedia/en/thumb/2/2b/Chennai_Super_Kings_Logo.svg/1200px-Chennai_Super_Kings_Logo.svg.png",
                "out"));

        recyclerView.setAdapter(new CallListAdapter(list,getContext()));

        return view;
    }

}