package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public final class Kooperationen {
    private static LinkedHashMap<Integer, Kooperation> kooperationen = new LinkedHashMap<>();
    private static String kooperationDatenURL;
    private static Context context;
    private static boolean kooperationenAlreadyLoaded = false;

    public static void initializeKooperationen(KooperationenCallback callback) {
        HashMap postData = new HashMap();
        postData.put("userid", String.valueOf(GlobaleVariablen.getInstance().getUserId()));

        context = ApplicationController.getInstance().getApplicationContext();
        kooperationDatenURL = context.getResources().getString(R.string.PHP_Scripts_Kooperationen_Abfragen);

        // the response listener
        JsonObjectRequest kooperationAbfragenRequest = new JsonObjectRequest(Request.Method.POST, kooperationDatenURL, new JSONObject(postData),
                response -> {
                    kooperationenAlreadyLoaded = true;
                    callback.onKooperationenSuccessfullyLoaded(kooperationDaten_Verarbeiten(response));
                },
                error -> Log.e("Kooperation_Abfragen_Error", error.getMessage()));

        ApplicationController.getInstance().addToRequestQueue(kooperationAbfragenRequest);
    }

    public static LinkedHashMap<Integer, Kooperation> getAktiveKooperationen() {
        return kooperationen.values().stream().filter(kooperation -> kooperation.getBenutzerById(GlobaleVariablen.getInstance().getUserId()).getAktiv()).collect(
                Collectors.toMap(Kooperation::getId, Kooperation::getObject, (val1, val2) -> val1,LinkedHashMap<Integer, Kooperation>::new));
    }

    public static LinkedHashMap<Integer, Kooperation> getKooperationen() {
        return kooperationen;
    }

    public static boolean getKooperationenInitialized() {
        return kooperationenAlreadyLoaded;
    }

    private static LinkedHashMap<Integer, Kooperation> kooperationDaten_Verarbeiten(JSONObject Kooperationsdaten) {
        try {
            // Getting JSON Array node
            JSONArray kooperationenArray = Kooperationsdaten.getJSONArray("Kooperation");

            //looping through all the elements in json array
            for (int i = 0; i < kooperationenArray.length(); i++) {
                //getting json object from the json array
                Kooperation koop = new Kooperation(kooperationenArray.getJSONObject(i));
                kooperationen.put(koop.getId(), koop);
            }

            return kooperationen;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void resetInitialize() {
        kooperationen = null;
        kooperationenAlreadyLoaded = false;
    }

    public static Kooperation getKoopertionById(int KooperationID) {
        return kooperationen.values().stream().filter(koop -> koop.getId() == KooperationID).findFirst().orElse(null);
    }
}
