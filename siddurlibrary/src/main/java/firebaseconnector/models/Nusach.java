package firebaseconnector.models;

/**
 * Created by Beezy Works Studios on 6/7/2017.
 */

public enum Nusach {
    ASHKENAZ, SFARD, MIZRACH;

    public static Nusach from(String firebaseKey) {
        if (firebaseKey.equals(ASHKENAZ.firebaseKey())) {
            return ASHKENAZ;
        }
        if (firebaseKey.equals(SFARD.firebaseKey())) {
            return SFARD;
        }
        if(firebaseKey.equals(MIZRACH.firebaseKey())){
            return MIZRACH;
        }
        return null;
    }

    public String firebaseKey() {
        switch (this) {

            case ASHKENAZ:
                return "ashkenaz";
            case SFARD:
                return "sfard";
            case MIZRACH:
                return "mizrach";
        }
        return null;
    }
}
