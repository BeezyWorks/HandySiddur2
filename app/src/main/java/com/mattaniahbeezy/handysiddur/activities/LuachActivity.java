package com.mattaniahbeezy.handysiddur.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;

/**
 * Created by Beezy Works Studios on 6/9/2017.
 */

public class LuachActivity extends WearableActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAmbientEnabled();

    }
}
