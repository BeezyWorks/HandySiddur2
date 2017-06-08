package firebaseconnector.models;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Beezy Works Studios on 6/5/2017.
 */

public class Evaluation {
    public enum Operator {
        AND, OR;

        public static Operator from(long l) {
            if (l == 0)
                return AND;
            if (l == 1)
                return OR;
            return null;
        }
    }

    public int omerDay;
    public String parsha;
    public Operator operator;
    public List<HashMap<String, Object>> elements;

    public int getOmerDay() {
        return omerDay;
    }

    public String getParsha() {
        return parsha;
    }

    public Operator getOperator() {
        return operator;
    }

    public List<HashMap<String, Object>> getElements() {
        return elements;
    }
}

