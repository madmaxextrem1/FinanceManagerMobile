package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Finanzbuchung_Buchung;
import net.sytes.financemanagermm.financemanagermobile.Hauptmenu.Hauptmenu_Fragment_Buchungen_Filter;
import net.sytes.financemanagermm.financemanagermobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Finanzbuchungen {
    private static ArrayList<Finanzbuchung_Buchung> buchungen = new ArrayList<>();
    private static String buchungenDatenURL;
    private static Context context;
    private static boolean buchungenAlreadyLoaded = false;

    public static void initializeFinanzbuchungen(FinanzbuchungenCallback callback) {
        buchungen = new ArrayList<>();

        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("userid", String.valueOf(GlobaleVariablen.getInstance().getUserId()));
        postData.put("OptionalFilter", Hauptmenu_Fragment_Buchungen_Filter.getFilterString());
        postData.put("DatumVon",Hauptmenu_Fragment_Buchungen_Filter.getZeitraumVonUSFormat());
        postData.put("DatumBis", Hauptmenu_Fragment_Buchungen_Filter.getZeitraumBisUSFormat());

        context = ApplicationController.getInstance().getApplicationContext();
        buchungenDatenURL =  context.getResources().getString(R.string.PHP_Scripts_Finanzbuchungen_Abfragen);

        // the response listener
        JsonObjectRequest buchungenAbfragenRequest = new JsonObjectRequest(Request.Method.POST, buchungenDatenURL, new JSONObject(postData),
                response -> {
                    buchungenAlreadyLoaded = true;
                    callback.onFinanzbuchungenSuccessfullyLoaded(finanzbuchungsDaten_Verarbeiten(response));
                },
                error -> Log.e("Buchungen_Abfragen_Error", error.getMessage()));

        ApplicationController.getInstance().addToRequestQueue(buchungenAbfragenRequest);
        ApplicationController.getInstance().getRequestQueue().start();
    }

    public  static ArrayList<Finanzbuchung_Buchung> getFinanzbuchungen() {
        return buchungen;
    }
    public static boolean getFinanzbuchungenInitialized() {return buchungenAlreadyLoaded;}
    private static ArrayList<Finanzbuchung_Buchung> finanzbuchungsDaten_Verarbeiten(JSONObject Finanzbuchungsdaten) {
        try {
                JSONObject jsonObj = Finanzbuchungsdaten;

                // Getting JSON Array node
                JSONArray buchungenArray = jsonObj.getJSONArray("Finanzbuchungen");

                //looping through all the elements in json array
                for (int i = 0; i < buchungenArray.length(); i++) {
                    buchungen.add(new Finanzbuchung_Buchung(buchungenArray.getJSONObject(i)));
                }

                return buchungen;
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        return null;
    }
    public static void resetInitialize() {
        buchungen = null;
        buchungenAlreadyLoaded = false;
    }
    public static Finanzbuchung_Buchung getFinanzbuchungById(int FinanzbuchungID) {
        return buchungen.stream().filter(buchung -> buchung.getId() == FinanzbuchungID).findFirst().orElse(null);
    }

    public static void addBuchung(Finanzbuchung_Buchung buchung) {
        buchungen.add(buchung);
        reorderBuchungen();
    }
    public static void reorderBuchungen() {
        ArrayList<Finanzbuchung_Buchung> copiedList = new ArrayList<>();
        copiedList.addAll(buchungen);
        buchungen.clear();
        copiedList.stream().
                sorted(Comparator.comparing(
                        Finanzbuchung_Buchung::getDatum,
                        Comparator.nullsFirst(Comparator.reverseOrder()))).forEach(buchung ->
                            buchungen.add(buchung));
    }

    public static void removeBuchungById(int buchungID) {
        for(Finanzbuchung_Buchung buchung:buchungen) {
            if(buchung.getId() == buchungID) {
                buchungen.remove(buchung);
                break;
            }
        }
    }
    //public static resetAndInitialize()
}

