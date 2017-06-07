package firebaseconnector.models;

/**
 * Created by Beezy Works Studios on 6/7/2017.
 */

public class Style {
    public enum Position {
        FIRST, LAST, ALL;

        public static Position from(String firebaseKey) {
            if (firebaseKey.equals(FIRST.firebaseKey()))
                return FIRST;
            if (firebaseKey.equals(ALL.firebaseKey()))
                return ALL;
            if (firebaseKey.equals(LAST.firebaseKey()))
                return LAST;
            return null;
        }

        public String firebaseKey() {
            switch (this) {

                case FIRST:
                    return "First";
                case LAST:
                    return "Last";
                case ALL:
                    return "All";
            }
            return null;
        }
    }

    public Position position;
    public int words = -1;
    public String name;
}
