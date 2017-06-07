package firebaseconnector.database;

import com.google.firebase.database.DataSnapshot;

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

    public void getText(TextElement textElement, StringCallback stringCallback){
        getText(textElement, Translation.HEBREW, stringCallback);
    }

    public void getText(TextElement textElement, Translation translation, StringCallback callback){
        if(textElement.text!=null){
            getString(textElement.text, translation, callback);
        }
    }

    private static Style styleFrom(HashMap<String, Object> raw) {
        Style style = new Style();
        style.name = (String) raw.get("name");
        style.position = Style.Position.from((String) raw.get("position"));
        if (raw.containsKey("words")) {
            long longWords = (long) raw.get("words");
            style.words= (int) longWords;
        }
        return style;
    }
}