package com.mattaniahbeezy.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.widget.TextView;

import java.util.List;

import firebaseconnector.database.BaseFirebaseConnector;
import firebaseconnector.database.TefilaAssembler;
import firebaseconnector.models.Nusach;
import firebaseconnector.models.Translation;

public class MainActivity extends AppCompatActivity implements TefilaAssembler.TefilaCallback {
    private static final String TAG = "testActivity";
    TextView mTextView;
    Nusach nusach = Nusach.ASHKENAZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseFirebaseConnector.initializeFirebase();
        mTextView = (TextView) findViewById(R.id.textView);
        TefilaAssembler assembler = new TefilaAssembler(nusach, "bentching", this, null);
    }

    @Override
    public void tefilaReady(List<TefilaAssembler.ResolvedSection> resolvedSections) {
        for (TefilaAssembler.ResolvedSection resolvedSection : resolvedSections) {
            Spanned string = resolvedSection.getResolvedTranslations().get(Translation.HEBREW);
            mTextView.setText(string);
        }
    }
}
