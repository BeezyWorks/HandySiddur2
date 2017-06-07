package firebaseconnector.models;

/**
 * Created by Beezy Works Studios on 6/7/2017.
 */

public enum Translation {
    HEBREW, ENGLISH, TRANSLITERATED;

    public String getFirebaseNode() {
        switch (this) {

            case HEBREW:
                return "hebrew/";
            case ENGLISH:
                return "english/";
            case TRANSLITERATED:
                return "transliterated";
        }
    return "ERROR";
    }
}
