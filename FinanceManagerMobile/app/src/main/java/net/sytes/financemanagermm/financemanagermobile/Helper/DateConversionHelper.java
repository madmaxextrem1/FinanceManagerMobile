package net.sytes.financemanagermm.financemanagermobile.Helper;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

public final class DateConversionHelper {
    private static final String US_DateFormatPattern = "yyyy.MM.dd";
    private static final String DE_DateFormatPattern = "dd.MM.yyyy";
    private static final String SQL_DateFormatPattern = "yyyy-MM-dd";
    private static SimpleDateFormat US_DateFormat = new SimpleDateFormat(US_DateFormatPattern, Locale.getDefault());
    private static SimpleDateFormat DE_DateFormat = new SimpleDateFormat(DE_DateFormatPattern, Locale.getDefault());
    private static SimpleDateFormat SQL_DateFormat = new SimpleDateFormat(SQL_DateFormatPattern, Locale.getDefault());

    public static String getUS_DateFormatPattern() {
        return US_DateFormatPattern;
    }

    public static String getDE_DateFormatPattern() {
        return DE_DateFormatPattern;
    }

    public static String getSQL_DateFormatPattern() {
        return SQL_DateFormatPattern;
    }

    public static SimpleDateFormat getUS_DateFormat() {
        US_DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return US_DateFormat;
    }

    public static SimpleDateFormat getDE_DateFormat() {
        DE_DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return DE_DateFormat;
    }

    public static SimpleDateFormat getSQL_DateFormat() {
        SQL_DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return SQL_DateFormat;
    }

    public static DateTimeFormatter getDEDateFormatter() {
        return DateTimeFormatter.ofPattern(DE_DateFormatPattern);
    }

    public static DateTimeFormatter getSQLDateFormatter() {
        return DateTimeFormatter.ofPattern(SQL_DateFormatPattern);
    }

    public static LocalDate convertToLocalDate(String dateString, SimpleDateFormat dateFormat) {
        LocalDate returnDate = null;
        try {
            returnDate = dateFormat.parse(dateString).toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDate();
        } catch (ParseException e) {
            Log.e(e.getMessage(), e.getStackTrace().toString());
        }
        return returnDate;
    }
}
