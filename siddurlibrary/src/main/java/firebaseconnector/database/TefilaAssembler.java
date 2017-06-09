package firebaseconnector.database;

import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import firebaseconnector.models.Nusach;
import firebaseconnector.models.Section;
import firebaseconnector.models.Tefila;
import firebaseconnector.models.TextElement;
import firebaseconnector.models.TextGroup;
import firebaseconnector.models.Translation;

/**
 * Created by Beezy Works Studios on 6/8/2017.
 */

public class TefilaAssembler {
    private Nusach nusach;
    private Tefila resolvedTefila;
    private List<Translation> translations;
    private TefilaCallback callback;

    private HashMap<String, Section> sections = new HashMap<>();
    private HashMap<String, TextGroup> textGroups = new HashMap<>();
    private HashMap<String, TextElement> textElements = new HashMap<>();
    private HashMap<TextElement, HashMap<Translation, SpannableString>> elementValues = new HashMap<>();

    private SectionAPI sectionAPI = new SectionAPI();
    private TextGroupAPI textGroupAPI = new TextGroupAPI();
    private TextElementAPI textElementAPI = new TextElementAPI();

    private SectionAvailable sectionAvailable = new SectionAvailable();
    private TextGroupAvailable textGroupAvailable = new TextGroupAvailable();
    private TextElementAvailable textElementAvailable = new TextElementAvailable();

    public TefilaAssembler(final Nusach nusach, TefilaCallback callback, @Nullable List<Translation> translations) {
        this.nusach = nusach;
        this.callback = callback;
        if (translations == null) {
            translations = new ArrayList<>();
            translations.add(Translation.HEBREW);
        }
        this.translations = translations;
    }

    public void assemble(String tefilaKey) {
        sections.clear();
        TefilaAPI tefilaAPI = new TefilaAPI();
        tefilaAPI.findByKey(tefilaKey, new BaseFirebaseConnector.FirebaseCallback<Tefila>() {
            @Override
            public void dataAvailable(Tefila data) {
                resolvedTefila = data;
                for (String sectionKey : data.sectionIds.get(nusach)) {
                    if (!sections.containsKey(sectionKey)) {
                        sections.put(sectionKey, null);
                        sectionAPI.findByKey(sectionKey, sectionAvailable);
                    }
                }
            }
        });
    }

    public void setCallback(TefilaCallback callback) {
        this.callback = callback;
        attemptResolveTefila();
    }

    private class SectionAvailable implements BaseFirebaseConnector.FirebaseCallback<Section> {

        @Override
        public void dataAvailable(Section section) {
            sections.put(section.$key, section);
            for (String textGroupKey : section.textGroupIds.get(nusach)) {
                if (!textGroups.containsKey(textGroupKey)) {
                    textGroups.put(textGroupKey, null);
                    textGroupAPI.findByKey(textGroupKey, textGroupAvailable);
                }
            }
        }
    }

    private class TextGroupAvailable implements BaseFirebaseConnector.FirebaseCallback<TextGroup> {

        @Override
        public void dataAvailable(TextGroup textGroup) {
            if (textGroup.elements.containsKey(nusach)) {
                textGroups.put(textGroup.$key, textGroup);
                for (String elementKey : textGroup.elements.get(nusach)) {
                    if (!textElements.containsKey(elementKey)) {
                        textElements.put(elementKey, null);
                        textElementAPI.findByKey(elementKey, textElementAvailable);
                    }
                }
            }
        }
    }

    int callbackCount = 0;

    private class TextElementAvailable implements BaseFirebaseConnector.FirebaseCallback<TextElement> {

        @Override
        public void dataAvailable(final TextElement textElement) {
            textElements.put(textElement.$key, textElement);
            if (!elementValues.containsKey(textElement)) {
                elementValues.put(textElement, new HashMap<Translation, SpannableString>());
                for (final Translation translation : translations) {
                    callbackCount++;
                    textElementAPI.getText(textElement, translation, new TextElementAPI.SpannableStringCallback() {
                        @Override
                        public void spannableStringReady(SpannableString span) {
                            callbackCount --;
                            elementValues.get(textElement).put(translation, span);
                            if(callbackCount<10);
                            attemptResolveTefila();
                        }
                    });
                }
            }
        }
    }

    private void attemptResolveTefila() {
        EvaluationResolver.getInstance().setCurrentTefila(resolvedTefila);
        List<ResolvedSection> resolvedSections = new ArrayList<>();
        for (String sectionKey : resolvedTefila.sectionIds.get(nusach)) {
            if (sections.containsKey(sectionKey) && sections.get(sectionKey) != null && EvaluationResolver.getInstance().isEvaluationTrue(sections.get(sectionKey).evaluation)) {
                Section section = sections.get(sectionKey);
                ResolvedSection resolvedSection = new ResolvedSection();
                resolvedSection.setSection(section);
                for (String textGroupKey : section.textGroupIds.get(nusach)) {
                    if (textGroups.containsKey(textGroupKey) && textGroups.get(textGroupKey) != null && EvaluationResolver.getInstance().isEvaluationTrue(textGroups.get(textGroupKey).evaluation)) {
                        TextGroup textGroup = textGroups.get(textGroupKey);
                        for (String elementKey : textGroup.elements.get(nusach)) {
                            if (textElements.containsKey(elementKey) && textElements.get(elementKey) != null && EvaluationResolver.getInstance().isEvaluationTrue(textElements.get(elementKey).evaluation)) {
                                for (Translation translation : translations) {
                                    SpannableString span = elementValues.get(textElements.get(elementKey)).get(translation);
                                    if (span != null) {
                                        if (resolvedSection.getForTranslation(translation) == null) {
                                            resolvedSection.setForTranslation(translation, span);
                                        } else {
                                            resolvedSection.setForTranslation(translation, (Spanned) TextUtils.concat(resolvedSection.getForTranslation(translation), span));
                                        }
                                    }
                                }
                            }
                        }
                        // add linebreak at end of group
                        for (Translation translation : translations) {
                            if (resolvedSection.getForTranslation(translation) != null) {
                                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(resolvedSection.getForTranslation(translation));
                                spannableStringBuilder.append("\n");
                                resolvedSection.setForTranslation(translation, spannableStringBuilder);
                            }
                        }
                    }
                }
                resolvedSections.add(resolvedSection);
            }
        }
        if (resolvedSections.size() > 0)
            callback.tefilaReady(resolvedSections);
    }

    public interface TefilaCallback {
        void tefilaReady(List<ResolvedSection> resolvedSections);
    }

    public class ResolvedSection {
        private Section section;
        private HashMap<Translation, Spanned> resolvedTranslations = new HashMap<>();

        public Section getSection() {
            return section;
        }

        void setSection(Section section) {
            this.section = section;
        }

        public HashMap<Translation, Spanned> getResolvedTranslations() {
            return resolvedTranslations;
        }


        void setForTranslation(Translation translation, Spanned spanned) {
            resolvedTranslations.put(translation, spanned);
        }

        Spanned getForTranslation(Translation translation) {
            if (!resolvedTranslations.containsKey(translation)) return null;
            return resolvedTranslations.get(translation);
        }
    }
}
