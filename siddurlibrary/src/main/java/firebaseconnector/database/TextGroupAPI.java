package firebaseconnector.database;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.List;

import firebaseconnector.models.TextGroup;


/**
 * Created by Beezy Works Studios on 6/7/2017.
 */

public class TextGroupAPI extends BaseFirebaseConnector<TextGroup> {
    @Override
    String getTypePath() {
        return "text-groups/";
    }

    @Override
    TextGroup from(DataSnapshot dataSnapshot) {
        TextGroup textGroup = new TextGroup();
        HashMap<String, Object> raw = (HashMap<String, Object>) dataSnapshot.getValue();
        BaseFirebaseConnector.preProcessReturn(textGroup, dataSnapshot);
        textGroup.elements = BaseFirebaseConnector.toNusachMap((HashMap<String, List<String>>) raw.get("elements"));
        if (raw.containsKey(BaseFirebaseConnector.EVALUATION_KEY)) {
            textGroup.evaluation = BaseFirebaseConnector.from((HashMap<String, Object>) raw.get(BaseFirebaseConnector.EVALUATION_KEY));
        }
        return textGroup;
    }
}
