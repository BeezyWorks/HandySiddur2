package firebaseconnector.models;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

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

    public boolean evaluate() {
        boolean allTrue = true;
        boolean someTrue = false;
        for (HashMap<String, Object> element : elements) {
            boolean elementValue = evaluateElement(element);
            if (elementValue) {
                someTrue = true;
            } else {
                allTrue = false;
            }
            if (someTrue && operator == Operator.OR) {
                return true;
            }
            if (!allTrue && operator == Operator.AND) {
                return false;
            }
        }
        return operator == Operator.AND ? allTrue : someTrue;
    }

    private boolean evaluateElement(HashMap<String, Object> element) {
        Operator operator = Operator.from((Long) element.get("operator"));
        boolean allTrue = true;
        boolean someTrue = false;
        Set<String> fields = element.keySet();
        fields.remove("operator");
        for (String field : fields) {
            boolean fieldValue = evaluateField(field, (boolean) element.get(field));
            if (fieldValue) {
                someTrue = true;
            } else {
                allTrue = false;
            }
            if (someTrue && operator == Operator.OR)
                return true;
            if (!allTrue && operator == Operator.AND)
                return false;
        }
        return operator == Operator.AND ? allTrue : someTrue;
    }

    private boolean evaluateField(String field, boolean desiredValue) {
//        TODO: this
        return true;
    }
}

