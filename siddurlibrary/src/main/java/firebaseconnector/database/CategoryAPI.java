package firebaseconnector.database;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import firebaseconnector.models.Category;

/**
 * Created by Beezy Works Studios on 6/7/2017.
 */

public class CategoryAPI extends BaseFirebaseConnector<Category> {
    @Override
    String getTypePath() {
        return "categories/";
    }

    @Override
    Category from(DataSnapshot dataSnapshot) {
        HashMap<String, Object> raw = (HashMap<String, Object>) dataSnapshot.getValue();
        Category category = new Category();
        BaseFirebaseConnector.preProcessReturn(category, dataSnapshot);
        category.name = (String) raw.get("name");
        category.tefilaKeys = (ArrayList<String>) raw.get("tefilaKeys");
        return category;
    }
}
