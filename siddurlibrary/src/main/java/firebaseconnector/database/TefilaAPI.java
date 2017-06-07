package firebaseconnector.database;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.List;

import firebaseconnector.models.Tefila;

/**
 * Created by Beezy Works Studios on 6/7/2017.
 */

public class TefilaAPI extends BaseFirebaseConnector<Tefila> {
    @Override
    String getTypePath() {
        return "tefilot/";
    }

    @Override
    Tefila from(DataSnapshot dataSnapshot) {
        Tefila tefila = new Tefila();
        HashMap<String, Object> raw = (HashMap<String, Object>) dataSnapshot.getValue();
       BaseFirebaseConnector.preProcessReturn(tefila, dataSnapshot);
        tefila.name = (String) raw.get("name");
        tefila.sectionIds = BaseFirebaseConnector.toNusachMap((HashMap<String, List<String>>) raw.get("sectionIds"));
        return tefila;
    }
}
