package firebaseconnector.models;

import java.util.List;

/**
 * Created by Beezy Works Studios on 6/7/2017.
 */

public class TextElement extends FirebaseModel {
    public String text;
    public Evaluation evaluation;
    public int chapter = -1;
    public int startVerse = -1;
    public int endVerse = -1;
    public String book;
    public List<Style> styles;
}
