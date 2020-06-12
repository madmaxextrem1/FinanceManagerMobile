package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class FinanzbuchungTokens {
    private static ArrayList<FinanzbuchungToken> tokens;
    private static String tokenDatenURL;
    private static Context context;
    private static boolean tokensAlreadyLoaded = false;

    public static void initializeTokens(TokensCallback callback) {
        HashMap postData = new HashMap();
        postData.put("userid", String.valueOf(GlobaleVariablen.getInstance().getUserId()));

        context = ApplicationController.getInstance().getApplicationContext();
        tokenDatenURL =  context.getResources().getString(R.string.PHP_Scripts_Tokens_Abfragen);

        // the response listener
        JsonObjectRequest tokenAbfragenRequest = new JsonObjectRequest(Request.Method.POST, tokenDatenURL, new JSONObject(postData),
                response -> {
                    tokensAlreadyLoaded = true;
                    callback.onTokensSuccessfullyLoaded(tokenDaten_Verarbeiten(response));
                },
                error -> Log.e("Token_Abfragen_Error", error.getMessage()));

        ApplicationController.getInstance().addToRequestQueue(tokenAbfragenRequest);
    }
    public  static ArrayList<FinanzbuchungToken> getTokens() {
        return tokens;
    }
    public static boolean getTokensInitialized() {return tokensAlreadyLoaded;}
    private static ArrayList<FinanzbuchungToken> tokenDaten_Verarbeiten(JSONObject Tokendaten) {
        ArrayList<FinanzbuchungToken> returnListToken = new ArrayList<FinanzbuchungToken>();
        //Log.d("Test123!", Tokendaten.toString());
        try {
                // Getting JSON Array node
                JSONArray tokenArray = Tokendaten.getJSONArray("Merkmale");

                //looping through all the elements in json array
                for (int i = 0; i < tokenArray.length(); i++) {
                    returnListToken.add(new FinanzbuchungToken(tokenArray.getJSONObject(i)));
                }

                tokens = returnListToken;
                return tokens;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return null;
    }
    public static void resetInitialize() {
        tokens = null;
        tokensAlreadyLoaded = false;
    }
    public static FinanzbuchungToken getTokenById(int TokenID) {
        return tokens.stream().filter(token -> token.getId() == TokenID).findFirst().orElse(null);
    }
}

