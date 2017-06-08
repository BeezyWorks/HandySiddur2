package firebaseconnector.database;

import firebaseconnector.models.Nusach;

/**
 * Created by Beezy Works Studios on 6/8/2017.
 */

public class UserPrefs {
    private static final UserPrefs instance = new UserPrefs();

    private boolean inIsrael;
    private boolean inJerusalem;
    private boolean laterAdditions;
    private boolean sfardAdditions;
    private boolean polinBirkosHashachar;
    private boolean originalMinim;

    public boolean isInIsrael() {
        return inIsrael;
    }

    public void setInIsrael(boolean inIsrael) {
        this.inIsrael = inIsrael;
    }

    public boolean isInJerusalem() {
        return inJerusalem;
    }

    public void setInJerusalem(boolean inJerusalem) {
        this.inJerusalem = inJerusalem;
    }

    public boolean isLaterAdditions() {
        return laterAdditions;
    }

    public void setLaterAdditions(boolean laterAdditions) {
        this.laterAdditions = laterAdditions;
    }

    public boolean isSfardAdditions() {
        return sfardAdditions;
    }

    public void setSfardAdditions(boolean sfardAdditions) {
        this.sfardAdditions = sfardAdditions;
    }

    public boolean isPolinBirkosHashachar() {
        return polinBirkosHashachar;
    }

    public void setPolinBirkosHashachar(boolean polinBirkosHashachar) {
        this.polinBirkosHashachar = polinBirkosHashachar;
    }

    public boolean isOriginalMinim() {
        return originalMinim;
    }

    public void setOriginalMinim(boolean originalMinim) {
        this.originalMinim = originalMinim;
    }

    public Nusach getNusach() {
        return nusach;
    }

    public void setNusach(Nusach nusach) {
        this.nusach = nusach;
    }

    private Nusach nusach;

    public static UserPrefs getInstance(){
        return instance;
    }

    private UserPrefs(){
    }


}
