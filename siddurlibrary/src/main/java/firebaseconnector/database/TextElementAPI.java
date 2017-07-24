package firebaseconnector.database;

import android.support.annotation.Nullable;
import android.text.SpannableString;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import firebaseconnector.models.Style;
import firebaseconnector.models.TextElement;
import firebaseconnector.models.Translation;

/**
 * Created by Beezy Works Studios on 6/7/2017.
 */

public class TextElementAPI extends BaseFirebaseConnector<TextElement> {

    @Override
    String getTypePath() {
        return "text-elements/";
    }

    @Nullable
    @Override
    TextElement getCached(String key) {
        return CachedFirebaseObjects.getInstance().getTextElement(key);
    }

    @Override
    void setCached(TextElement value) {
        CachedFirebaseObjects.getInstance().setTextElement(value);
    }

    @Override
    TextElement from(DataSnapshot dataSnapshot) {
        TextElement textElement = new TextElement();
        HashMap<String, Object> raw = (HashMap<String, Object>) dataSnapshot.getValue();
        BaseFirebaseConnector.preProcessReturn(textElement, dataSnapshot);
        if (raw.containsKey("book")) {
            textElement.book = (String) raw.get("book");
        }
        if (raw.containsKey("text")) {
            textElement.text = (String) raw.get("text");
        }
        if (raw.containsKey("chapter")) {
            long chapterLong = (long) raw.get("chapter");
            textElement.chapter = (int) chapterLong;
        }
        if (raw.containsKey("startVerse")) {
            long startVerseLong = (long) raw.get("startVerse");
            textElement.startVerse = (int) startVerseLong;
        }
        if (raw.containsKey("endVerse")) {
            long endVerseLong = (long) raw.get("endVerse");
            textElement.endVerse = (int) endVerseLong;
        }
        if (raw.containsKey("breakLineAtVerse")) {
            textElement.breakLineAtVerse = (boolean) raw.get("breakLineAtVerse");
        }
        if (raw.containsKey("styles")) {
            List<HashMap<String, Object>> rawStyles = (List<HashMap<String, Object>>) raw.get("styles");
            textElement.styles = new ArrayList<Style>();
            for (HashMap<String, Object> rawStyle : rawStyles) {
                textElement.styles.add(styleFrom(rawStyle));
            }
        }
        textElement.evaluation = BaseFirebaseConnector.from((HashMap<String, Object>) raw.get(BaseFirebaseConnector.EVALUATION_KEY));
        return textElement;
    }

    public void getText(TextElement textElement, SpannableStringCallback stringCallback) {
        getText(textElement, Translation.HEBREW, stringCallback);
    }

    private void stringToSpannable(TextElement textElement, Translation translation, String string, SpannableStringCallback callback) {
        SpannableString spannableString = new SpannableString(string);
        if (textElement.styles != null) {
            for (Style style : textElement.styles) {
                if (style != null) {
                    Object spannableObject = style.getSpannableObject();
                    if (spannableObject != null) {
                        spannableString.setSpan(spannableObject, 0, style.endIndex(string), 0);
                    }
                }
            }
        }
        CachedFirebaseObjects.getInstance().setSpanned(textElement, translation, spannableString);
        callback.spannableStringReady(spannableString);
    }

    public void getText(final TextElement textElement, final Translation translation, final SpannableStringCallback callback) {
        if(CachedFirebaseObjects.getInstance().getSpannedText(translation, textElement)!=null){
            callback.spannableStringReady(CachedFirebaseObjects.getInstance().getSpannedText(translation, textElement));
            return;
        }
        if (textElement.text != null) {
            getString(textElement.text, translation, new StringCallback() {
                @Override
                public void stringAvailable(String string) {
                    stringToSpannable(textElement, translation, string, callback);
                }
            });
        } else {
            FirebaseDatabase.getInstance().getReference("tanach/" + translation.getFirebaseNode() + textElement.book + "/" + (textElement.chapter - 1)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<String> pesukim = (ArrayList<String>) dataSnapshot.getValue();
                    String pasukEnd = " ";
                    int start = textElement.startVerse - 1;
                    if (start < 0) {
                        start = 0;
                    }
                    int end = textElement.endVerse;
                    if (end == -1 || end > pesukim.size())
                        end = pesukim.size();
                    StringBuilder stringBuilder = new StringBuilder();
                    int i = start;
                    do {
                        stringBuilder.append(pesukim.get(i));
                        stringBuilder.append(pasukEnd);
                        if (textElement.breakLineAtVerse) {
                            stringBuilder.append("\n");
                        }
                        i++;
                    } while (i < end);

                    stringToSpannable(textElement, translation, stringBuilder.toString(), callback);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private static Style styleFrom(HashMap<String, Object> raw) {
        Style style = new Style();
        style.name = (String) raw.get("name");
        if (style.name == null)
            return null;
        style.position = Style.Position.from((String) raw.get("position"));
        if (raw.containsKey("words")) {
            long longWords = (long) raw.get("words");
            style.words = (int) longWords;
        }
        return style;
    }

    public interface SpannableStringCallback {
        void spannableStringReady(SpannableString span);
    }
}
