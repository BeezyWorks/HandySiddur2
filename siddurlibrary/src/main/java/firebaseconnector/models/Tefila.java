package firebaseconnector.models;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Beezy Works Studios on 6/5/2017.
 */

public class Tefila extends FirebaseModel {
    public String name;
    public HashMap<Nusach, List<String>> sectionIds;
}
