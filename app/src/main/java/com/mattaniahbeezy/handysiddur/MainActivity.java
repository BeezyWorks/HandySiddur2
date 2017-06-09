package com.mattaniahbeezy.handysiddur;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.CurvedChildLayoutManager;
import android.support.wearable.view.WearableRecyclerView;

import com.mattaniahbeezy.handysiddur.adapters.NavigationRecyclerAdapter;

public class MainActivity extends WearableActivity {

    //private static final String Tag = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WearableRecyclerView navigationRecyclerView = (WearableRecyclerView) findViewById(R.id.navigation_recycler);
        navigationRecyclerView.setLayoutManager(new CurvedChildLayoutManager(this));
        navigationRecyclerView.setAdapter(new NavigationRecyclerAdapter());
        navigationRecyclerView.setCircularScrollingGestureEnabled(false);
        navigationRecyclerView.setCenterEdgeItems(true);
        setAmbientEnabled();
    }
}
