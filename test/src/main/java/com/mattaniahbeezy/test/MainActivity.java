package com.mattaniahbeezy.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import firebaseconnector.database.BaseFirebaseConnector;
import firebaseconnector.database.CategoryAPI;
import firebaseconnector.database.SectionAPI;
import firebaseconnector.database.TefilaAPI;
import firebaseconnector.database.TextElementAPI;
import firebaseconnector.database.TextGroupAPI;
import firebaseconnector.models.Category;
import firebaseconnector.models.Nusach;
import firebaseconnector.models.Section;
import firebaseconnector.models.Tefila;
import firebaseconnector.models.TextElement;
import firebaseconnector.models.TextGroup;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "testActivity";
    TextView mTextView;
    Nusach nusach = Nusach.ASHKENAZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseFirebaseConnector.initializeFirebase();
        mTextView = (TextView) findViewById(R.id.textView);
        final CategoryAPI categoryAPI = new CategoryAPI();
        categoryAPI.findByKey("brochos", new BaseFirebaseConnector.FirebaseCallback<Category>() {
            @Override
            public void dataAvailable(Category data) {
                getTefilot(data);
                categoryAPI.getString(data.name, new BaseFirebaseConnector.StringCallback() {
                    @Override
                    public void stringAvailable(String string) {
                        mTextView.setText(string);
                    }
                });
            }
        });
    }

    private void getTefilot(Category category) {
        TefilaAPI tefilaAPI = new TefilaAPI();
        for (String tefilaKey : category.tefilaKeys) {
            tefilaAPI.findByKey(tefilaKey, new BaseFirebaseConnector.FirebaseCallback<Tefila>() {
                @Override
                public void dataAvailable(Tefila data) {
                    getSections(data);
                }
            });
        }
    }

    private void getSections(Tefila tefila) {
        final SectionAPI sectionAPI = new SectionAPI();
        for (String sectionKey : tefila.sectionIds.get(nusach)) {
            sectionAPI.findByKey(sectionKey, new BaseFirebaseConnector.FirebaseCallback<Section>() {
                @Override
                public void dataAvailable(Section data) {
                    getTextGroups(data);
                }
            });
        }
    }

    private void getTextGroups(Section section) {
        TextGroupAPI textGroupAPI = new TextGroupAPI();
        for (String groupId : section.textGroupIds.get(nusach)) {
            textGroupAPI.findByKey(groupId, new BaseFirebaseConnector.FirebaseCallback<TextGroup>() {
                @Override
                public void dataAvailable(TextGroup data) {
                    getTextElements(data);
                }
            });
        }
    }

    private void getTextElements(TextGroup textGroup) {
        final TextElementAPI textElementAPI = new TextElementAPI();
        for (String elementId : textGroup.elements.get(nusach)) {
            textElementAPI.findByKey(elementId, new BaseFirebaseConnector.FirebaseCallback<TextElement>() {
                @Override
                public void dataAvailable(TextElement data) {
                    textElementAPI.getText(data, new BaseFirebaseConnector.StringCallback() {
                        @Override
                        public void stringAvailable(String string) {
                            mTextView.setText(string);
                        }
                    });
                }
            });
        }
    }
}
