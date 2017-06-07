package firebaseconnector.models;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Beezy Works Studios on 6/7/2017.
 */

public class TextGroup extends FirebaseModel {
    public HashMap<Nusach, List<String>> elements;
    public Evaluation evaluation;
}
