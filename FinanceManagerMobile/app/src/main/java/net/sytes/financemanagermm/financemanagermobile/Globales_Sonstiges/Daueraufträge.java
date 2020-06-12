package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Dauerauftrag;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Dauerauftrag_Umbuchung;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Daueraufträge {

    private static LinkedHashMap<Integer, Dauerauftrag> daueraufträge;
    private static String dauerauftragDatenURL;
    private static String dauerauftragUmbuchungDatenURL;
    private static Context context;
    private static boolean daueraufträgeAlreadyLoaded = false;

    public static void initializeDaueraufträge(DauerauftragCallback callback) {
        HashMap postData = new HashMap();
        postData.put("userid", String.valueOf(GlobaleVariablen.getInstance().getUserId()));

        daueraufträge = new LinkedHashMap<Integer, Dauerauftrag>();
        context = ApplicationController.getInstance().getApplicationContext();
        dauerauftragDatenURL =  context.getResources().getString(R.string.PHP_Scripts_Daueraufträge_Abfragen);
        dauerauftragUmbuchungDatenURL = context.getResources().getString(R.string.PHP_Scripts_DaueraufträgeUmbuchungen_Abfragen);

        // the response listener
        JsonObjectRequest dauerauftragAbfragenRequest = new JsonObjectRequest(Request.Method.POST, dauerauftragDatenURL, new JSONObject(postData),
                response -> {
                    dauerauftragDaten_Verarbeiten(response, new DauerauftragCallback() {
                        @Override
                        public void onDaueraufträgeSuccessfullyLoaded(LinkedHashMap<Integer, Dauerauftrag> daueraufträge) {
                            // the response listener
                            JsonObjectRequest dauerauftragAbfragenRequest = new JsonObjectRequest(Request.Method.POST, dauerauftragUmbuchungDatenURL, new JSONObject(postData),
                                    responseUmbuchung -> {
                                        daueraufträgeAlreadyLoaded = true;
                                        callback.onDaueraufträgeSuccessfullyLoaded(dauerauftragUmbuchungDaten_Verarbeiten(responseUmbuchung));
                                    },
                                    error -> Log.e("Daueraufträge_Abfragen_Error", error.getMessage()));

                            ApplicationController.getInstance().addToRequestQueue(dauerauftragAbfragenRequest);
                        }
                    });

                },
                error -> Log.e("Daueraufträge_Umbuchung_Abfragen_Error", error.getMessage()));

        ApplicationController.getInstance().addToRequestQueue(dauerauftragAbfragenRequest);
        ApplicationController.getInstance().getRequestQueue().start();
    }
    public  static LinkedHashMap<Integer, Dauerauftrag> getDaueraufträge() {
        return daueraufträge;
    }
    public static boolean getDaueraufträgeInitialized() {return daueraufträgeAlreadyLoaded;}
    private static void dauerauftragDaten_Verarbeiten(JSONObject dauerauftragDaten, DauerauftragCallback callback) {
        try {
            // Getting JSON Array node
            JSONArray daueraufträgeArray = dauerauftragDaten.getJSONArray("dauerauftraege");

            //looping through all the elements in json array
            for (int i = 0; i < daueraufträgeArray.length(); i++) {
                Dauerauftrag dauerauftrag = new Dauerauftrag(daueraufträgeArray.getJSONObject(i));
                daueraufträge.put(dauerauftrag.getIdentifier(), dauerauftrag);
            }

            callback.onDaueraufträgeSuccessfullyLoaded(daueraufträge);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private static LinkedHashMap<Integer, Dauerauftrag> dauerauftragUmbuchungDaten_Verarbeiten(JSONObject dauerauftragDaten) {
        try {
            // Getting JSON Array node
            JSONArray daueraufträgeArray = dauerauftragDaten.getJSONArray("dauerauftraege");

            //looping through all the elements in json array
            for (int i = 0; i < daueraufträgeArray.length(); i++) {
                Dauerauftrag_Umbuchung dauerauftrag = new Dauerauftrag_Umbuchung(daueraufträgeArray.getJSONObject(i));
                daueraufträge.put(dauerauftrag.getIdentifier(), dauerauftrag);
            }

            return daueraufträge;
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Dauerauftrag getDauerauftragById(int DauerauftragId) {
        return daueraufträge.get(DauerauftragId);
    }
    public static void resetInitialize() {
        daueraufträge = null;
        daueraufträgeAlreadyLoaded = false;
    }
}
