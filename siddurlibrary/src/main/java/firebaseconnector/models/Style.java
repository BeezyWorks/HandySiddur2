package firebaseconnector.models;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

/**
 * Created by Beezy Works Studios on 6/7/2017.
 */

public class Style {
    private static int SPECIAL_TEXT_COLOR = Color.parseColor("#FF9800");

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

    public int endIndex(CharSequence charSequence) {
        if (position == Position.ALL) {
            return charSequence.length() - 1;
        }
        String str = charSequence.toString();
        String[] splited = str.split("\\s+");
        int start = 0;
        int charCount = 0;
        while (start < words) {
            charCount += splited[start].length();
            start++;
        }
        return charCount+words;
    }

    public Object getSpannableObject() {
        switch (name) {
            case "Bold":
                return new StyleSpan(Typeface.BOLD);
            case "SpecialColor":
                return new ForegroundColorSpan(SPECIAL_TEXT_COLOR);
            case "Small":
                return new RelativeSizeSpan(0.8f);
            case "Large":
                return new RelativeSizeSpan(1.2f);
        }
        return null;
    }
}
