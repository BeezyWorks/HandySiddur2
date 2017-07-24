package firebaseconnector.database;

import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import firebaseconnector.models.Evaluation;
import firebaseconnector.models.FirebaseModel;
import firebaseconnector.models.Nusach;
import firebaseconnector.models.Translation;

/**
 * Created by Beezy Works Studios on 6/5/2017.
 */

public abstract class BaseFirebaseConnector<T extends FirebaseModel> {
    static final String EVALUATION_KEY = "evaluation";
    static final String rootNode = "public/";

    public static void initializeFirebase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    abstract String getTypePath();

    String getPath() {
        return rootNode + getTypePath();
    }

    public static void syncAll() {
        FirebaseDatabase.getInstance().getReference(rootNode);
    }

    abstract
    @Nullable
    T getCached(String key);

    abstract void setCached(T value);

    void findByKey(String key, final FirebaseCallback<T> callback) {
        if (getCached(key) != null) {
            callback.dataAvailable(getCached(key));
            return;
        }
        FirebaseDatabase.getInstance().getReference(getPath() + key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    callback.dataAvailable(from(dataSnapshot));
                    setCached(from(dataSnapshot));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getString(String key, StringCallback callback) {
        getString(key, Translation.HEBREW, callback);
    }

    void getString(final String key, final Translation translation, final StringCallback callback) {
        if (CachedFirebaseObjects.getInstance().getString(translation, key) != null) {
            callback.stringAvailable(CachedFirebaseObjects.getInstance().getString(translation, key));
            return;
        }
        FirebaseDatabase.getInstance().getReference(rootNode + translation.getFirebaseNode() + key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CachedFirebaseObjects.getInstance().setString(key, translation, (String) dataSnapshot.getValue());
                if (dataSnapshot.exists())
                    callback.stringAvailable((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    static HashMap<Nusach, List<String>> toNusachMap(HashMap<String, List<String>> rawValue) {
        HashMap<Nusach, List<String>> returnMap = new HashMap<>();
        if (rawValue == null)
            return returnMap;
        for (String key : rawValue.keySet()) {
            returnMap.put(Nusach.from(key), rawValue.get(key));
        }
        return returnMap;
    }

    protected static Evaluation from(HashMap<String, Object> raw) {
        if (raw == null) return null;
        Evaluation evaluation = new Evaluation();
        evaluation.operator = Evaluation.Operator.AND;
        if (raw.containsKey("operator"))
            evaluation.operator = Evaluation.Operator.from((Long) raw.get("operator"));
        evaluation.elements = (List<HashMap<String, Object>>) raw.get("elements");
        return evaluation;
    }

    abstract T from(DataSnapshot dataSnapshot);

    static void preProcessReturn(FirebaseModel firebaseModel, DataSnapshot dataSnapshot) {
        firebaseModel.$key = dataSnapshot.getKey();
        HashMap<String, Object> raw = (HashMap<String, Object>) dataSnapshot.getValue();
        if (raw.containsKey("lastUpdated")) {
            firebaseModel.lastUpdated = (long) raw.get("lastUpdated");
        }
    }

    interface FirebaseCallback<T> {
        void dataAvailable(T data);
    }

    interface StringCallback {
        void stringAvailable(String string);
    }
}
