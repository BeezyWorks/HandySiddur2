package firebaseconnector.database;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.List;

import firebaseconnector.models.Section;

/**
 * Created by Beezy Works Studios on 6/7/2017.
 */

public class SectionAPI extends BaseFirebaseConnector<Section> {
    @Override
    String getTypePath() {
        return "sections/";
    }

    @Override
    Section from(DataSnapshot dataSnapshot) {
        Section section = new Section();
        BaseFirebaseConnector.preProcessReturn(section, dataSnapshot);
        HashMap<String, Object> raw = (HashMap<String, Object>) dataSnapshot.getValue();
        section.textGroupIds = BaseFirebaseConnector.toNusachMap((HashMap<String, List<String>>) raw.get("textGroupIds"));
        section.evaluation = BaseFirebaseConnector.from((HashMap<String, Object>) raw.get(BaseFirebaseConnector.EVALUATION_KEY));
        return section;
    }
}
