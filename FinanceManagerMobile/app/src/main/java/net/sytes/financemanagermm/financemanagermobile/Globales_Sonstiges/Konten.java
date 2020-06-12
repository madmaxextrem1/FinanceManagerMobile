package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class Konten {
    private static LinkedHashMap<Integer, Konto> konten;
    private static String kontenDatenURL;
    private static Context context;
    private static boolean kontenAlreadyLoaded = false;

    public static void initializeKonten(FinanceManagerCallback callback) {
        HashMap postData = new HashMap();
        postData.put("userid", String.valueOf(GlobaleVariablen.getInstance().getUserId()));

        context = ApplicationController.getInstance().getApplicationContext();
        kontenDatenURL =  context.getResources().getString(R.string.PHP_Scripts_Konten_Abfragen);

        // the response listener
        JsonObjectRequest kontenAbfragenRequest = new JsonObjectRequest(Request.Method.POST, kontenDatenURL, new JSONObject(postData),
                response -> {
                    kontenAlreadyLoaded = true;
                    callback.onDataUpdated(kontenDaten_Verarbeiten(response));
                },
                error -> Log.e("Konten_Abfragen_Error", error.getMessage()));

        ApplicationController.getInstance().addToRequestQueue(kontenAbfragenRequest);
        ApplicationController.getInstance().getRequestQueue().start();
    }
    public static LinkedHashMap<Integer, Konto> getAktiveKonten() {
        return konten.
                    values().
                    stream().
                    filter(Konto::getAktiv).
                    collect(Collectors.toMap(Konto::getIdentifier, konto -> konto, (prev, next) -> next, LinkedHashMap::new));
    }
    public static LinkedHashMap<Integer, Konto> getKonten() {
        return konten;
    }
    public static boolean getKontenInitialized() {return kontenAlreadyLoaded;}
    private static LinkedHashMap<Integer, Konto> kontenDaten_Verarbeiten(JSONObject kontenDaten) {
        LinkedHashMap<Integer, Konto> returnMap = new LinkedHashMap<Integer, Konto>();

        try {
                JSONObject jsonObj = kontenDaten;

                // Getting JSON Array node
                JSONArray kontenArray = jsonObj.getJSONArray("konten");

                //looping through all the elements in json array
                for (int i = 0; i < kontenArray.length(); i++) {
                    Konto newKonto = new Konto(kontenArray.getJSONObject(i));
                    returnMap.put(newKonto.getIdentifier(), newKonto);
                }

                konten = returnMap;
                return konten;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return null;
    }
    public static void resetInitialize() {
        konten = new LinkedHashMap<>();
        kontenAlreadyLoaded = false;
    }
    public static Konto getKontoById(int KontoId) {
        return konten.get(KontoId);
    }
}

