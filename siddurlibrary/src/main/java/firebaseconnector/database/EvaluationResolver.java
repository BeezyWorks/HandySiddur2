package firebaseconnector.database;

import com.mattaniahbeezy.siddurlibrary.hebrewcalendar.HebrewDate;

import java.util.HashMap;
import java.util.Set;

import firebaseconnector.models.Evaluation;
import firebaseconnector.models.Evaluation.Operator;
import firebaseconnector.models.Nusach;
import firebaseconnector.models.Tefila;

/**
 * Created by Beezy Works Studios on 6/8/2017.
 */

final class EvaluationResolver {

    private static final EvaluationResolver instance = new EvaluationResolver();

    static EvaluationResolver getInstance() {
        return instance;
    }

    private EvaluationResolver() {
    }

    private Tefila currentTefila;
    private boolean wedding = false;
    private boolean bris = false;
    private boolean beisAvel = false;
    private boolean runningLate = false;

    public void setWedding(boolean wedding) {
        this.wedding = wedding;
    }

    public void setBris(boolean bris) {
        this.bris = bris;
    }

    public void setBeisAvel(boolean beisAvel) {
        this.beisAvel = beisAvel;
    }

    public void setRunningLate(boolean runningLate) {
        this.runningLate = runningLate;
    }

    boolean isEvaluationTrue(Evaluation evaluation) {
        if (evaluation == null)
            return true;

        Operator operator = evaluation.getOperator();

        boolean allTrue = true;
        boolean someTrue = false;
        for (HashMap<String, Object> element : evaluation.getElements()) {
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
        Operator operator = Operator.AND;
        if (element.containsKey("operator")) {
            operator = Operator.from((Long) element.get("operator"));
        }
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
            if (someTrue && operator == Evaluation.Operator.OR)
                return true;
            if (!allTrue && operator == Operator.AND)
                return false;
        }
        return operator == Operator.AND ? allTrue : someTrue;
    }

    private boolean evaluateField(String field, boolean desiredValue) {
        return fieldValue(field) == desiredValue;
    }

    void setCurrentTefila(Tefila currentTefila) {
        this.currentTefila = currentTefila;
    }

    private HebrewDate getHebrewDate() {
        if (currentTefila == null)
            return HebrewDate.getInstance();
        if (currentTefila.$key.equals("maariv"))
            return HebrewDate.getTomorrowInstance();
        if (currentTefila.$key.equals("bentching")) {
            return HebrewDate.getTimeSensitiveInstance();
        }
        return HebrewDate.getInstance();
    }

    private boolean fieldValue(String field) {
        HebrewDate hebrewDate = getHebrewDate();
        switch (field) {
            case "YaaleVYavo":
                return hebrewDate.isYaalehVyavo();
            case "Chanuka":
                return hebrewDate.isChanuka();
            case "RoshChodesh":
                return hebrewDate.isRoshChodesh();
            case "Mussaf":
                return hebrewDate.isMussaf();
            case "IsShabbos":
                return hebrewDate.isShabbos();
            case "Tachanun":
                return hebrewDate.isTachanun() && !beisAvel;
            case "TachanunDate":
                return hebrewDate.isTachanun();
            case "MinchaTachanun":
                return hebrewDate.isMinchaTachanun() && !beisAvel;
            case "MinchaTachanunDate":
                return hebrewDate.isMinchaTachanun();
            case "LongTachanun":
                return hebrewDate.isLongTachanun();
            case "Lamnatzeach":
                return hebrewDate.isLamnateach();
            case "KriasHaTorah":
                return hebrewDate.isKriasHatorah();
            case "YomHaAtzmaut":
                return hebrewDate.isYomHaAtzmaut();
            case "YomYerushaleim":
                return hebrewDate.isYomYerushaleim();
            case "PurimKatan":
                return hebrewDate.isPurimKatan();
            case "Purim": // takes location into account
                return hebrewDate.isPurim();
            case "Pesach1":
                return hebrewDate.isPesachOne();
            case "Pesach2":
                return hebrewDate.isPesachTwo();
            case "Pesach3":
                return hebrewDate.isPesachThree();
            case "Pesach4":
                return hebrewDate.isPesachFour();
            case "Pesach5":
                return hebrewDate.isPesachFive();
            case "Pesach6":
                return hebrewDate.isPesachSix();
            case "Pesach7":
                return hebrewDate.isPesachSeven();
            case "Pesach8":
                return hebrewDate.isPesachEight();
            case "Mincha":
                return currentTefila != null && currentTefila.$key.equals("mincha");
            case "Maariv":
                return currentTefila != null && currentTefila.$key.equals("maariv");
            case "Shacharis":
                return currentTefila != null && currentTefila.$key.equals("shachari");
            case "Hallel":
                return hebrewDate.isHallel();
            case "FullHallel":
                return hebrewDate.isFullHallel();
            case "CandleLighting":
                return hebrewDate.isCandleLight();
            case "FastDay":
                return hebrewDate.isFastDay();
            case "IsSummer":
                return hebrewDate.isSummer();
            case "IsSummerNew":
                return hebrewDate.isSummerNew();
            case "IsRain":
                return hebrewDate.isRain();
            case "IsRainNew":
                return hebrewDate.isRainNew();
            case "IsYomTov":
                return hebrewDate.isYomTov();
            case "ErevPesach":
                return hebrewDate.isErevPesach();
            case "Havdalah":
                return hebrewDate.isHavdala();
            case "Motzash":
                return hebrewDate.isSunday();
            case "Omer":
                return hebrewDate.isOmer();
            case "YomHaShoah":
                return hebrewDate.isYomHaShoah();
            case "YomHaZikaron":
                return hebrewDate.isYomHaZikaron();
            case "LagBomer":
                return hebrewDate.isLogBomer();
            case "Shavuos":
                return hebrewDate.isShavuos();
            case "TzomTaamuz":
                return hebrewDate.isTzomTaamuz();
            case "NineDays":
                return hebrewDate.isNineDays();
            case "ErevTishaBAv":
                return hebrewDate.isErevTishaBav();
            case "TishaBAv":
                return hebrewDate.isTishaBAv();
            case "TuBAv":
                return hebrewDate.isTuBav();
            case "Ldovid":
                return hebrewDate.isLDovid();
            case "ErevRoshHaShana":
                return hebrewDate.isErevRoshHashana();
            case "RoshHashana":
                return hebrewDate.isRoshHashana();
            case "EseresYemeiTeshuva":
                return hebrewDate.isEseresYemeiTeshuva();
            case "TzomGedalia":
                return hebrewDate.isTzomGedaliah();
            case "ErevYomKippur":
                return hebrewDate.isErevYomKippur();
            case "YomKippur":
                return hebrewDate.isYomKippur();
            case "Succos1":
                return hebrewDate.isSuccosOne();
            case "Succos2":
                return hebrewDate.isSuccosTwo();
            case "Succos3":
                return hebrewDate.isSuccosThree();
            case "Succos4":
                return hebrewDate.isSuccosFour();
            case "Succos5":
                return hebrewDate.isSuccosFive();
            case "Succos6":
                return hebrewDate.isSuccosSix();
            case "Succos":
                return hebrewDate.isSuccos();
            case "HoshanaRaba":
                return hebrewDate.isHoshanaRaba();
            case "SimchasTorah":
                return hebrewDate.isSimchasTorah();
            case "SheminiAtzeres":
                return hebrewDate.isShminiAtzeres();
            case "ErevChanuka":
                return hebrewDate.isErevChanuka();
            case "TzomTevs":
                return hebrewDate.isTzomTeves();
            case "TaanisEsther":
                return hebrewDate.isTaanisEsther();
            case "Sunday":
                return hebrewDate.isSunday();
            case "Monday":
                return hebrewDate.isMonday();
            case "Tuesday":
                return hebrewDate.isTuesday();
            case "Wednesday":
                return hebrewDate.isWednesday();
            case "Thursday":
                return hebrewDate.isThursday();
            case "Friday":
                return hebrewDate.isFriday();
            case "HebrewLeapYear":
                return hebrewDate.isHebrewLeapYear();
            case "Ashkenaz":
                return UserPrefs.getInstance().getNusach() == Nusach.ASHKENAZ;
            case "Sfard":
                return UserPrefs.getInstance().getNusach() == Nusach.SFARD;
            case "Mizrach":
                return UserPrefs.getInstance().getNusach() == Nusach.MIZRACH;
            case "InIsrael":
                return UserPrefs.getInstance().isInIsrael();
            case "InJerusalem":
                return UserPrefs.getInstance().isInJerusalem();
            case "LaterAdditions":
                return UserPrefs.getInstance().isLaterAdditions();
            case "SfardAdditions":
                return UserPrefs.getInstance().isSfardAdditions();
            case "OriginalMinim":
                return UserPrefs.getInstance().isOriginalMinim();
            case "BirkosHaShacharPolin":
                return UserPrefs.getInstance().isPolinBirkosHashachar();
            case "Wedding":
                return wedding;
            case "Bris":
                return bris;
            case "Avel":
                return beisAvel;
            case "RunningLate":
                return runningLate;
        }
        return true;
    }
}
