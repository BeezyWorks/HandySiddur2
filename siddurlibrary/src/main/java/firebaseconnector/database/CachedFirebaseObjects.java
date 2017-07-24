package firebaseconnector.database;

import android.support.annotation.Nullable;
import android.text.SpannableString;

import java.util.HashMap;

import firebaseconnector.models.Section;
import firebaseconnector.models.Tefila;
import firebaseconnector.models.TextElement;
import firebaseconnector.models.TextGroup;
import firebaseconnector.models.Translation;

/**
 * Created by Beezy Works Studios on 6/11/2017.
 */

class CachedFirebaseObjects {
    private static final CachedFirebaseObjects ourInstance = new CachedFirebaseObjects();

    static CachedFirebaseObjects getInstance() {
        return ourInstance;
    }

    private CachedFirebaseObjects() {
    }

    private HashMap<String, Tefila> cachedTefilos = new HashMap<>();
    private HashMap<String, Section> cachedSections = new HashMap<>();
    private HashMap<String, TextGroup> cachedTextGroups = new HashMap<>();
    private HashMap<String, TextElement> cachedTextElement = new HashMap<>();
    private HashMap<String, SpannableString> cachedTextSpan = new HashMap<>();
    private HashMap<String, String> cachedStrings = new HashMap<>();

    @Nullable
    Tefila getTefila(String key) {
        if (!cachedTefilos.containsKey(key))
            return null;
        return cachedTefilos.get(key);
    }

    @Nullable
    Section getSection(String key) {
        if (!cachedSections.containsKey(key))
            return null;
        return cachedSections.get(key);
    }

    @Nullable
    TextGroup getTextGroup(String key) {
        if (!cachedTextGroups.containsKey(key))
            return null;
        return cachedTextGroups.get(key);
    }

    @Nullable
    TextElement getTextElement(String key) {
        if (!cachedTextElement.containsKey(key))
            return null;
        return cachedTextElement.get(key);
    }

    @Nullable
    SpannableString getSpannedText(Translation translation, TextElement textElement) {
        if (!cachedTextSpan.containsKey(translation.getFirebaseNode() + textElement.$key))
            return null;
        return cachedTextSpan.get(translation.getFirebaseNode() + textElement.$key);
    }

    @Nullable
    String getString(Translation translation, String key) {
        if (!cachedStrings.containsKey(translation.getFirebaseNode() + key))
            return null;
        return cachedStrings.get(translation.getFirebaseNode() + key);
    }

    void setTefila(Tefila tefila) {
        cachedTefilos.put(tefila.$key, tefila);
    }

    void setSection(Section section) {
        cachedSections.put(section.$key, section);
    }

    void setTextGroup(TextGroup textGroup) {
        cachedTextGroups.put(textGroup.$key, textGroup);
    }

    void setTextElement(TextElement textElement) {
        cachedTextElement.put(textElement.$key, textElement);
    }

    void setString(String key, Translation translation, String string){
        cachedStrings.put(translation.getFirebaseNode()+key, string);
    }

    void setSpanned(TextElement textElement, Translation translation, SpannableString span){
        cachedTextSpan.put(translation.getFirebaseNode()+textElement.$key, span);
    }
}
