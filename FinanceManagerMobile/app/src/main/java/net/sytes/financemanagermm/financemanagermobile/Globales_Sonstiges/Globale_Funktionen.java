package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.Patterns;
import android.util.TypedValue;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.FinanzbuchungPosition;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.FinanceManagerData_Edited_Interface;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Globale_Funktionen {
    private static GlobaleVariablen instance = new GlobaleVariablen();
    private float brightness;
    private double Buchungssumme;
    public float Helligkeit_Berechnen(int R, int G, int B) {
        brightness = Float.parseFloat(String.valueOf( Math.sqrt(0.199 * Math.pow(R, 2) + 0.687 * Math.pow(G, 2) + 0.114 * Math.pow(B, 2))));
        return brightness;
    }
    public double Buchungssumme_Berechnen(ArrayList<FinanzbuchungPosition> Kategorien) {
        Double Betrag = 0.00;
        for (int i = 0; i < Kategorien.size(); i++) {
            FinanzbuchungPosition Eintrag = Kategorien.get(i);
            if(Eintrag.getId() != 0)
            Betrag = Eintrag.getBetrag();
            Buchungssumme += Betrag;}
    
        return Buchungssumme;
    }
    public static float Float_dpToPixels(Context context, float dpValue){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  dpValue, metrics);
    }
    public static int Integer_dpToPixels(Context context, int dpValue) {
        Resources r = context.getResources();
        int px = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dpValue,r.getDisplayMetrics()));
        return px;
    }
    public static int Integer_spToPixels(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
    public final static boolean isValidEmail(CharSequence EmailAdresse) {
        return !TextUtils.isEmpty(EmailAdresse.toString().trim()) && Patterns.EMAIL_ADDRESS.matcher(EmailAdresse.toString().trim().toLowerCase()).matches();
    }
    public final static  boolean isValidPassword(CharSequence Password) {
        final String sPASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!#$%]).{6,20})";
        Pattern Password_Pattern = Pattern.compile(sPASSWORD_PATTERN);
        Matcher Password_Matcher = Password_Pattern.matcher(Password);

        return !TextUtils.isEmpty(Password) && Password_Matcher.matches();
    }

    public static NumberFormat getCurrencyFormatWith2Digits () {
        return NumberFormat.getCurrencyInstance();
    }
    public static NumberFormat getCurrencyFormatWith0Digits () {
        NumberFormat returnFormat = NumberFormat.getCurrencyInstance();
        returnFormat.setMaximumFractionDigits(0);
        return returnFormat;
    }
    public static NumberFormat getPercentFormatWith0Digits() {
        NumberFormat returnFormat = NumberFormat.getPercentInstance();
        returnFormat.setMaximumFractionDigits(0);
        return returnFormat;
    }
    public static NumberFormat getPercentFormatWith2Digits() {
        NumberFormat returnFormat = NumberFormat.getPercentInstance();
        returnFormat.setMaximumFractionDigits(2);
        return returnFormat;
    }

    public static void SQL_createNewKonto(Konto konto, FinanceManagerData_Edited_Interface<Konto> callback) {
            HashMap<String, String> KontoDaten = new HashMap<>();

            KontoDaten.put("UserID", String.valueOf(GlobaleVariablen.getInstance().getUserId()));
            KontoDaten.put("KoopID", String.valueOf(konto.getKooperationID()));
            KontoDaten.put("Kontoname", konto.getKontoTitel());
            KontoDaten.put("Kontoart", konto.getKontoArt().toString());
            KontoDaten.put("Anfangsbestand", String.valueOf(konto.getAnfangsbestand()));
            KontoDaten.put("Datum", GlobaleVariablen.getInstance().getSQL_DateFormat().format(konto.getDatumAnfangsbestand()));
            KontoDaten.put("Aktiv", (konto.getAktiv()) ? "1" : "0");

            PostResponseAsyncTask kontoAnlegenTask =
                    new PostResponseAsyncTask(ApplicationController.getInstance().getApplicationContext(), KontoDaten, false, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            konto.setId(Integer.parseInt(s));
                            callback.onDataEdited(konto, true);
                        }
                    });
            kontoAnlegenTask.execute("http://financemanagermm.sytes.net/fmclient/konto_anlegen.php");
    }

    public static void SQL_updateKonto(Konto konto, FinanceManagerData_Edited_Interface<Konto> callback) {
        HashMap<String, String> KontoDaten = new HashMap<>();

        KontoDaten.put("KontoID", String.valueOf(konto.getIdentifier()));
        KontoDaten.put("Kontoname", konto.getKontoTitel());
        KontoDaten.put("Kontoart", konto.getKontoArt().toString());
        KontoDaten.put("Anfangsbestand", String.valueOf(konto.getAnfangsbestand()));
        KontoDaten.put("Datum", GlobaleVariablen.getInstance().getSQL_DateFormat().format(konto.getDatumAnfangsbestand()));
        KontoDaten.put("Aktiv", (konto.getAktiv()) ? "1" : "0");

        PostResponseAsyncTask kontoAnlegenTask =
                new PostResponseAsyncTask(ApplicationController.getInstance().getApplicationContext(), KontoDaten, false, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        callback.onDataEdited(konto, false);
                    }
                });
        kontoAnlegenTask.execute("http://financemanagermm.sytes.net/fmclient/konto_updaten.php");
    }


    public static MaterialDatePicker getMaterialDatePicker(String title, Optional<Date> customOpenDate, MaterialPickerOnPositiveButtonClickListener acceptListener) {
        long selection = customOpenDate.orElse(new Date()).getTime();
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(title);

        builder.setSelection(selection);

        CalendarConstraints.Builder constraintsBuilder =
                Globale_Funktionen.setupConstraintsBuilder(
                        Globale_Funktionen.MaterialDatePicker_BoundsChoice.NONE,
                        Globale_Funktionen.MaterialDatePicker_OpeningChoice.CUSTOM,
                        Globale_Funktionen.MaterialDatePicker_ValidationCoice.NONE,
                        Optional.of(new Date(selection)));
        builder.setCalendarConstraints(constraintsBuilder.build());
        MaterialDatePicker<?> picker = builder.build();

        picker.addOnPositiveButtonClickListener(acceptListener);

        return picker;
    }

    public static CalendarConstraints.Builder setupConstraintsBuilder(
            MaterialDatePicker_BoundsChoice boundsChoice,
            MaterialDatePicker_OpeningChoice openingChoice,
            MaterialDatePicker_ValidationCoice validationChoice,
            Optional<Date> customOpeningDate) {

        long today;
        long nextMonth;
        long customMonth;
        long janThisYear;
        long decThisYear;
        long oneYearForward;
        Pair<Long, Long> todayPair;
        Pair<Long, Long> nextMonthPair;

        today = MaterialDatePicker.thisMonthInUtcMilliseconds();
        Calendar calendar = getClearedUtc();
        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.MONTH, 1);
        nextMonth = calendar.getTimeInMillis();

        calendar.setTime(customOpeningDate.orElse(new Date()));
        customMonth = calendar.getTime().getTime();

        calendar.setTimeInMillis(today);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);

        janThisYear = calendar.getTimeInMillis();
        calendar.setTimeInMillis(today);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        decThisYear = calendar.getTimeInMillis();

        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.YEAR, 1);
        oneYearForward = calendar.getTimeInMillis();

        todayPair = new Pair<>(today, today);
        nextMonthPair = new Pair<>(nextMonth, nextMonth);


        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        if (boundsChoice == MaterialDatePicker_BoundsChoice.CURRENT_YEAR) {
            constraintsBuilder.setStart(janThisYear);
            constraintsBuilder.setEnd(decThisYear);
        } else if (boundsChoice == MaterialDatePicker_BoundsChoice.NEXT_YEAR) {
            constraintsBuilder.setStart(today);
            constraintsBuilder.setEnd(oneYearForward);
        }

        if (openingChoice == MaterialDatePicker_OpeningChoice.CURRENT_MONTH) {
            constraintsBuilder.setOpenAt(today);
        } else if (openingChoice == MaterialDatePicker_OpeningChoice.NEXT_MONTH) {
            constraintsBuilder.setOpenAt(nextMonth);
        } else if (openingChoice == MaterialDatePicker_OpeningChoice.CUSTOM) {
            constraintsBuilder.setOpenAt(customMonth);
        }

        if (validationChoice == MaterialDatePicker_ValidationCoice.TODAY_ANWARD) {
            constraintsBuilder.setValidator(DateValidatorPointForward.now());
        } else if (validationChoice == MaterialDatePicker_ValidationCoice.WEEKDAYS) {
            constraintsBuilder.setValidator(new DateValidatorWeekdays());
        } else if ((validationChoice == MaterialDatePicker_ValidationCoice.LAST_TWO_WEEKS)) {
            Calendar lowerBoundCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            lowerBoundCalendar.add(Calendar.DAY_OF_MONTH, -14);
            long lowerBound = lowerBoundCalendar.getTimeInMillis();
            List<CalendarConstraints.DateValidator> validators = new ArrayList<>();
            validators.add(DateValidatorPointForward.from(lowerBound));
            validators.add(new DateValidatorWeekdays());

            constraintsBuilder.setValidator(CompositeDateValidator.allOf(validators));
        }

        return constraintsBuilder;
    }

    private static Calendar getClearedUtc() {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.clear();
        return utc;
    }

    public enum MaterialDatePicker_BoundsChoice {
        CURRENT_YEAR,
        NONE,
        NEXT_YEAR;
    }
    public enum MaterialDatePicker_OpeningChoice {
        CURRENT_MONTH,
        NEXT_MONTH,
        CUSTOM;
    }
    public enum MaterialDatePicker_ValidationCoice {
        TODAY_ANWARD,
        WEEKDAYS,
        LAST_TWO_WEEKS,
        NONE;
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public static boolean isGermanDateString(String str) {
        try {
            GlobaleVariablen.getInstance().getDE_DateFormat().parse(str);
            return true;
        } catch(ParseException e){
            return false;
        }
    }
 }
