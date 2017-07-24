package com.mattaniahbeezy.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import firebaseconnector.database.BaseFirebaseConnector;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "testActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseFirebaseConnector.initializeFirebase();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TefilaRecyclerAdapter("shacharis"));
    }


}
