package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.KooperationAnfrage;
import net.sytes.financemanagermm.financemanagermobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public final class KooperationAnfragen {
    private static LinkedHashMap<Integer, KooperationAnfrage> anfragenMap;
    private static String anfragenDatenURL;
    private static Context context;
    private static boolean anfragenAlreadyLoaded = false;

    public static void initializeAnfragen(KooperationAnfragenCallback callback) {
        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("userid", String.valueOf(GlobaleVariablen.getInstance().getUserId()));

        context = ApplicationController.getInstance().getApplicationContext();
        anfragenDatenURL = context.getResources().getString(R.string.PHP_Scripts_Anfragen_Abfragen);

        // the response listener
        JsonObjectRequest kooperationAbfragenRequest = new JsonObjectRequest(Request.Method.POST, anfragenDatenURL, new JSONObject(postData),
                response -> {
                    anfragenAlreadyLoaded = true;
                    callback.onKooperationAnfragenSuccessfullyLoaded(anfrageDaten_Verarbeiten(response));
                },
                error -> Log.e("KooperationAnfragen_Abfragen_Error", error.getMessage()));

        ApplicationController.getInstance().addToRequestQueue(kooperationAbfragenRequest);
    }

    public static LinkedHashMap<Integer, KooperationAnfrage> getKooperationAnfragen() {
        return anfragenMap;
    }

    public static boolean getKooperationAnfragenInitialized() {
        return anfragenAlreadyLoaded;
    }

    private static LinkedHashMap<Integer, KooperationAnfrage> anfrageDaten_Verarbeiten(JSONObject Anfragedaten) {
        LinkedHashMap<Integer, KooperationAnfrage> returnMapAnfragen = new LinkedHashMap<Integer, KooperationAnfrage>();

        try {
            // Getting JSON Array node
            JSONArray kooperationenArray = Anfragedaten.getJSONArray("Anfragen");

            //looping through all the elements in json array
            for (int i = 0; i < kooperationenArray.length(); i++) {
                //getting json object from the json array
                KooperationAnfrage anfrage = new KooperationAnfrage(kooperationenArray.getJSONObject(i));
                returnMapAnfragen.put(anfrage.getIdentifier(), anfrage);
            }

            anfragenMap = returnMapAnfragen;
            return anfragenMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void resetInitialize() {
        anfragenMap = new LinkedHashMap<Integer, KooperationAnfrage>();
        anfragenAlreadyLoaded = false;
    }

    public static void addKooperationAnfrage(KooperationAnfrage Anfrage) {
        anfragenMap.put(Anfrage.getIdentifier(), Anfrage);
    }
}
