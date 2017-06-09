package com.mattaniahbeezy.handysiddur.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.activity.WearableActivity;

import com.mattaniahbeezy.handysiddur.R;
import com.mattaniahbeezy.handysiddur.adapters.TefilaRecyclerAdapter;

/**
 * Created by Beezy Works Studios on 6/9/2017.
 */

public class DaveningActivity extends WearableActivity {
    public static String DAVENING_KEY = "key_davening";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_davening);
        recyclerView = (RecyclerView) findViewById(R.id.davening_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        processIntent(getIntent());
        setAmbientEnabled();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        if(intent==null || !intent.hasExtra(DAVENING_KEY)) return;
        String daveningKey =intent.getStringExtra(DAVENING_KEY);
        TefilaRecyclerAdapter adapter = new TefilaRecyclerAdapter(daveningKey);
        recyclerView.setAdapter(adapter);
    }
}
