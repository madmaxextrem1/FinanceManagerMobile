package net.sytes.financemanagermm.financemanagermobile.Buchungen;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.ApplicationController;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.BuchungskategorienCallback;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Buchungskategorien {
    private static LinkedHashMap<Integer, Buchungshauptkategorie> mapHauptkategorien;
    private static String kategorieDatenURL;
    private static Context context;
    private static boolean kategorienAlreadyLoaded = false;

    public static void initializeBuchungskategorien(BuchungskategorienCallback callback) {
        HashMap postData = new HashMap();
        postData.put("userid", String.valueOf(GlobaleVariablen.getInstance().getUserId()));

        context = ApplicationController.getInstance().getApplicationContext();
        kategorieDatenURL =  context.getResources().getString(R.string.PHP_Scripts_Buchungskategorien_Abfragen);

        // the response listener
        JsonObjectRequest kategorienAbfragenRequest = new JsonObjectRequest(Request.Method.POST, kategorieDatenURL, new JSONObject(postData),
                response -> {
                    kategorienAlreadyLoaded = true;
                    callback.onBuchungskategorienSuccessfullyLoaded(kategorieDaten_Verarbeiten(response));
                },
                error -> Log.e("Kategorien_Abfragen_Error", error.getMessage()));

        ApplicationController.getInstance().addToRequestQueue(kategorienAbfragenRequest);
        ApplicationController.getInstance().getRequestQueue().start();
    }

    public static LinkedHashMap<Integer, Buchungshauptkategorie> getBuchungskategorien() {
        return mapHauptkategorien;
    }
    public static boolean getBuchungskategorienInitialized() {return kategorienAlreadyLoaded;}
    private static LinkedHashMap<Integer, Buchungshauptkategorie> kategorieDaten_Verarbeiten(JSONObject Kategoriedaten) {
        LinkedHashMap<Integer, Buchungshauptkategorie> returnListKategorien = new LinkedHashMap<>();

        try {
            JSONObject jsonObj = Kategoriedaten;

            // Getting JSON Array node
            JSONArray hauptKategorienArray = jsonObj.getJSONArray("Hauptkategorien");

            //looping through all the elements in json array
            for (int i = 0; i < hauptKategorienArray.length(); i++) {
                //getting json object from the json array
                JSONObject objHauptkategorie = hauptKategorienArray.getJSONObject(i);

                JSONArray unterKategorieArray = objHauptkategorie.getJSONArray("Unterkategorien");
                LinkedHashMap<Integer, Buchungskategorie> unterkategorienMap = new LinkedHashMap<> ();

                for (int z = 0; z < unterKategorieArray.length(); z++) {
                    //getting json object from the json array
                    JSONObject unterkategorieJSONObject = unterKategorieArray.getJSONObject(z);
                    Buchungskategorie newBuchungskategorie = new Buchungskategorie(unterkategorieJSONObject);
                    unterkategorienMap.put(newBuchungskategorie.getId(), newBuchungskategorie);
                }
                Buchungshauptkategorie newHauptkategorie = new Buchungshauptkategorie(objHauptkategorie, unterkategorienMap);
                returnListKategorien.put(newHauptkategorie.getId(), newHauptkategorie);
            }

            mapHauptkategorien  = returnListKategorien;
            return mapHauptkategorien;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static void resetInitialize() {
        mapHauptkategorien = null;
        kategorienAlreadyLoaded = false;
    }
    public static Buchungskategorie getBuchungskategorieById(int KategorieID) {
        List<Buchungskategorie> list = new ArrayList<Buchungskategorie>();
        list.addAll(mapHauptkategorien.values()
                .stream().flatMap(hauptkategorie -> hauptkategorie.getUnterkategorien().values().stream()).collect(Collectors.toSet()));
        list.addAll( mapHauptkategorien.values()
                .stream().map(hauptkategorie -> (Buchungskategorie) hauptkategorie).collect(Collectors.toSet()));

        return list.stream().filter(kategorie -> kategorie.getId() == KategorieID).findFirst().orElse(null);
    }
    public static void rebuildKategorieHierarchy(Buchungskategorie_Update_Interface callback) {
        Set<Buchungskategorie> buchungungskategorien = mapHauptkategorien
                .values()
                .stream()
                .flatMap(hauptkategorie -> hauptkategorie.getUnterkategorien().values().stream()).collect(Collectors.toSet());
        mapHauptkategorien.values().forEach(Buchungshauptkategorie::clearUnterkategorien);
        buchungungskategorien.stream().sorted(Comparator.comparing(Buchungskategorie::getBeschreibung)).forEach(unterkategorie ->
                mapHauptkategorien.get(unterkategorie.getÜKatId()).getUnterkategorien().put(unterkategorie.getId(), unterkategorie)
        );

        callback.onBuchungskategorieChanged(null);
    }
}
