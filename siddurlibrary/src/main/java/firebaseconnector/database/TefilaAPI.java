package firebaseconnector.database;

import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    @Nullable
    @Override
    Tefila getCached(String key) {
        return CachedFirebaseObjects.getInstance().getTefila(key);
    }

    @Override
    void setCached(Tefila value) {
        CachedFirebaseObjects.getInstance().setTefila(value);
    }

    public void getWatchTefilos(final KeysAvailable keysAvailable) {
        FirebaseDatabase.getInstance().getReference(rootNode + "watch-tefilos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> tefilaKeys = (List<String>) dataSnapshot.getValue();
                keysAvailable.TefilaKeysAvailable(tefilaKeys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface KeysAvailable {
        void TefilaKeysAvailable(List<String> tefilaKeys);
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
