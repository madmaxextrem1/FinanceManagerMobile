package net.sytes.financemanagermm.financemanagermobile.ServerCommunication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungshauptkategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.LinkedHashMap;

import es.dmoral.toasty.Toasty;

public final class ServerCommunication implements ServerCommunicationInterface {
    private RequestQueue requestQueue;
    private Context context;

    public ServerCommunication (Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void authenticateUser(String eMail, String password, GeneralCommunicationCallback<User> callback) {
        HashMap<String, String> postData = new HashMap<>();
        postData.put("Username", eMail);
        postData.put("Password", password);

        String authenticationURL = context.getResources().getString(R.string.PHP_Scripts_Login);

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HashMap<String, String> returnData = new HashMap<>();

                // here you parse the json response
                try {
                    JSONObject jsonObj = response;

                    // Getting JSON Array node
                    JSONArray benutzer = jsonObj.getJSONArray("data");

                    //getting json object from the json array
                    JSONObject obj = benutzer.getJSONObject(0);

                    //getting the name from the json object and putting it inside string array
                    User returnUser = null;
                    if(obj != null) {
                        returnUser = new User(obj.getInt("ID"), obj.getString("email"), obj.getString("username"), obj.getString("pwd"));
                    }

                    callback.onRequestCompleted(returnUser);
                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                    Toasty.error(context,"Email-Adresse oder Passwort falsch",Toast.LENGTH_SHORT,true).show();
                } catch (Exception e) {
                    Toasty.error(context, e.getMessage(),Toast.LENGTH_SHORT,true).show();
                    Log.d("LoginError",e.getMessage());
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context,"Email-Adresse oder Passwort falsch",Toast.LENGTH_SHORT,true).show();
            }
        };

        // the response listener
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, authenticationURL, new JSONObject(postData), responseListener, errorListener);

        addToRequestQueue(loginRequest);
    }

    @Override
    public boolean registerUser(String eMail, String userName, String password) {
        return false;
    }

    @Override
    public void queryAccounts(int userId, int kooperationId, GeneralCommunicationCallback<LinkedHashMap<Integer, Konto>> callback) {
        HashMap<String, String> postData = new HashMap<>();
        postData.put("userId",  String.valueOf(userId));
        postData.put("kooperationId", String.valueOf(kooperationId));

        String URL = context.getResources().getString(R.string.PHP_Scripts_Konten_Abfragen);

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LinkedHashMap<Integer, Konto> returnMap = new LinkedHashMap<Integer, Konto>();

                try {
                    // Getting JSON Array node
                    JSONArray kontenArray = response.getJSONArray("konten");

                    //looping through all the elements in json array
                    for (int i = 0; i < kontenArray.length(); i++) {
                        Konto newKonto = new Konto(kontenArray.getJSONObject(i));
                        returnMap.put(newKonto.getIdentifier(), newKonto);
                    }

                    callback.onRequestCompleted(returnMap);
                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                    Toasty.error(context,"Konnte Kontendaten nicht verarbeiten: " + e.getMessage(),Toast.LENGTH_SHORT,true).show();
                } catch (Exception e) {
                    Toasty.error(context, e.getMessage(),Toast.LENGTH_SHORT,true).show();
                    Log.d("Kontendaten Laden",e.getMessage());
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context,"Konten konnten nicht geladen werden",Toast.LENGTH_SHORT,true).show();
            }
        };

        // the response listener
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

        addToRequestQueue(request);
    }

    @Override
    public void queryCategories(int userId, GeneralCommunicationCallback<LinkedHashMap<Integer, Buchungshauptkategorie>> callback) {
        HashMap<String, String> postData = new HashMap<>();
        postData.put("userId",  String.valueOf(userId));

        String URL =  context.getResources().getString(R.string.PHP_Scripts_Buchungskategorien_Abfragen);

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LinkedHashMap<Integer, Buchungshauptkategorie> returnMap = new LinkedHashMap<Integer, Buchungshauptkategorie>();

                try {
                    // Getting JSON Array node
                    JSONArray hauptKategorienArray = response.getJSONArray("Hauptkategorien");

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
                        returnMap.put(newHauptkategorie.getId(), newHauptkategorie);
                    }

                  callback.onRequestCompleted(returnMap);

                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                    Toasty.error(context,"Konnte Kategoriedaten nicht verarbeiten: " + e.getMessage(),Toast.LENGTH_SHORT,true).show();
                } catch (Exception e) {
                    Toasty.error(context, e.getMessage(),Toast.LENGTH_SHORT,true).show();
                    Log.d("Kategoriedaten Laden",e.getMessage());
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context,"Kategorien konnten nicht geladen werden",Toast.LENGTH_SHORT,true).show();
            }
        };

        // the response listener
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

        addToRequestQueue(request);
    }

    @Override
    public void queryCooperations(int userId, GeneralCommunicationCallback<LinkedHashMap<Integer, Kooperation>> callback) {
        HashMap<String, String> postData = new HashMap<>();
        postData.put("userId",  String.valueOf(userId));

        String URL = context.getResources().getString(R.string.PHP_Scripts_Kooperationen_Abfragen);

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LinkedHashMap<Integer, Kooperation> returnMap = new LinkedHashMap<Integer, Kooperation>();

                try {
                    // Getting JSON Array node
                    JSONArray kooperationenArray = response.getJSONArray("Kooperation");

                    //looping through all the elements in json array
                    for (int i = 0; i < kooperationenArray.length(); i++) {
                        //getting json object from the json array
                        Kooperation koop = new Kooperation(kooperationenArray.getJSONObject(i));
                        returnMap.put(koop.getId(), koop);
                    }

                    callback.onRequestCompleted(returnMap);
                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                    Toasty.error(context,"Konnte Kooperationsdaten nicht verarbeiten: " + e.getMessage(),Toast.LENGTH_SHORT,true).show();
                } catch (Exception e) {
                    Toasty.error(context, e.getMessage(),Toast.LENGTH_SHORT,true).show();
                    Log.d("Kooperationsdaten Laden",e.getMessage());
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context,"Kooperationen konnten nicht geladen werden",Toast.LENGTH_SHORT,true).show();
            }
        };

        // the response listener
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

        addToRequestQueue(request);
    }

    @Override
    public void queryTokens(int userId, GeneralCommunicationCallback<HashMap<Integer, FinanzbuchungToken>> callback) {
        HashMap<String, String> postData = new HashMap<>();
        postData.put("userId",  String.valueOf(userId));

        String URL = context.getResources().getString(R.string.PHP_Scripts_Tokens_Abfragen);

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HashMap<Integer, FinanzbuchungToken> returnMap = new HashMap<>();

                try {
                    // Getting JSON Array node
                    JSONArray tokenArray = response.getJSONArray("Merkmale");

                    //looping through all the elements in json array
                    for (int i = 0; i < tokenArray.length(); i++) {
                        FinanzbuchungToken newToken = new FinanzbuchungToken(tokenArray.getJSONObject(i));
                        returnMap.put(newToken.getId(), newToken);
                    }

                    callback.onRequestCompleted(returnMap);
                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                    Toasty.error(context,"Konnte Tokendaten nicht verarbeiten: " + e.getMessage(),Toast.LENGTH_SHORT,true).show();
                } catch (Exception e) {
                    Toasty.error(context, e.getMessage(),Toast.LENGTH_SHORT,true).show();
                    Log.d("Tokendaten Laden",e.getMessage());
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context,"Tokens konnten nicht geladen werden",Toast.LENGTH_SHORT,true).show();
            }
        };

        // the response listener
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

        addToRequestQueue(request);
    }


    private void addToRequestQueue(Request request) {
        if(!isNetworkAvailable()) {
            Toasty.error(context,"Keine Internetverbindung",Toast.LENGTH_SHORT,true).show();
            return;
        }

        this.requestQueue.add(request);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
