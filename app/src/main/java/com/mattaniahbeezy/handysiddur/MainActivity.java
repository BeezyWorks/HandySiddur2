package com.mattaniahbeezy.handysiddur;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spanned;
import android.widget.TextView;

import java.util.List;

import firebaseconnector.database.TefilaAssembler;
import firebaseconnector.models.Nusach;
import firebaseconnector.models.Translation;

public class MainActivity extends Activity implements TefilaAssembler.TefilaCallback {

    //private static final String Tag = "MainActivity";

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text_view);
        TefilaAssembler assembler = new TefilaAssembler(Nusach.ASHKENAZ, "bentching", this, null);
    }

    @Override
    public void tefilaReady(List<TefilaAssembler.ResolvedSection> resolvedSections) {
        for (TefilaAssembler.ResolvedSection resolvedSection : resolvedSections) {
            Spanned string = resolvedSection.getResolvedTranslations().get(Translation.HEBREW);
            mTextView.setText(string);
        }
    }


}
