package net.sytes.financemanagermm.financemanagermobile.ServerCommunication;

import android.content.Context;
import android.graphics.Color;
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
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungshauptkategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie;
import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.Finanzbuchung;
import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.FinanzbuchungPosition;
import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.Finanzbuchung_Buchung;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.Helper.DateConversionHelper;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Sign_In_Up.FinanceManagerMobileApplication;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import es.dmoral.toasty.Toasty;

public final class ServerCommunication implements ServerCommunicationInterface {
    private RequestQueue requestQueue;
    private Context context;

    public ServerCommunication(Context context) {
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
                User returnUser = null;

                // here you parse the json response
                try {
                    JSONObject jsonObj = response;

                    // Getting JSON Array node
                    JSONArray benutzer = jsonObj.getJSONArray("data");

                    //getting json object from the json array
                    JSONObject obj = benutzer.getJSONObject(0);

                    //getting the name from the json object and putting it inside string array
                    if (obj != null) {
                        returnUser = new User(obj.getInt("ID"), obj.getString("email"), obj.getString("username"), obj.getString("pwd"));
                    }

                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                    Toasty.error(context, "Email-Adresse oder Passwort falsch", Toast.LENGTH_SHORT, true).show();
                } catch (Exception e) {
                    Toasty.error(context, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    Log.d("LoginError", e.getMessage());
                }

                callback.onRequestCompleted(returnUser);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context, "Email-Adresse oder Passwort falsch", Toast.LENGTH_SHORT, true).show();
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
        postData.put("userId", String.valueOf(userId));
        if (kooperationId != 0) postData.put("kooperationId", String.valueOf(kooperationId));

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

                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                    Toasty.error(context, "Konnte Kontendaten nicht verarbeiten: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                } catch (Exception e) {
                    Toasty.error(context, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    e.printStackTrace();
                    Log.d("Kontendaten Laden", e.getMessage());
                }

                callback.onRequestCompleted(returnMap);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context, "Konten konnten nicht geladen werden", Toast.LENGTH_SHORT, true).show();
            }
        };

        // the response listener
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

        addToRequestQueue(request);
    }

    @Override
    public void queryCategories(int userId, GeneralCommunicationCallback<LinkedHashMap<Integer, Buchungshauptkategorie>> callback) {
        HashMap<String, String> postData = new HashMap<>();
        postData.put("userId", String.valueOf(userId));

        String URL = context.getResources().getString(R.string.PHP_Scripts_Buchungskategorien_Abfragen);

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
                        LinkedHashMap<Integer, Buchungskategorie> unterkategorienMap = new LinkedHashMap<>();

                        for (int z = 0; z < unterKategorieArray.length(); z++) {
                            //getting json object from the json array
                            JSONObject unterkategorieJSONObject = unterKategorieArray.getJSONObject(z);
                            Buchungskategorie newBuchungskategorie = new Buchungskategorie(unterkategorieJSONObject);
                            unterkategorienMap.put(newBuchungskategorie.getId(), newBuchungskategorie);
                        }
                        Buchungshauptkategorie newHauptkategorie = new Buchungshauptkategorie(objHauptkategorie, unterkategorienMap);
                        returnMap.put(newHauptkategorie.getId(), newHauptkategorie);
                    }

                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                    Toasty.error(context, "Konnte Kategoriedaten nicht verarbeiten: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                } catch (Exception e) {
                    Toasty.error(context, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    Log.d("Kategoriedaten Laden", e.getMessage());
                }
                callback.onRequestCompleted(returnMap);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context, "Kategorien konnten nicht geladen werden", Toast.LENGTH_SHORT, true).show();
            }
        };

        // the response listener
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

        addToRequestQueue(request);
    }

    @Override
    public void queryCooperations(int userId, GeneralCommunicationCallback<LinkedHashMap<Integer, Kooperation>> callback) {
        HashMap<String, String> postData = new HashMap<>();
        postData.put("userId", String.valueOf(userId));

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

                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                    Toasty.error(context, "Konnte Kooperationsdaten nicht verarbeiten: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                } catch (Exception e) {
                    Toasty.error(context, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    Log.d("Kooperationsdaten Laden", e.getMessage());
                }
                callback.onRequestCompleted(returnMap);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context, "Kooperationen konnten nicht geladen werden", Toast.LENGTH_SHORT, true).show();
            }
        };

        // the response listener
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

        addToRequestQueue(request);
    }

    @Override
    public void queryTokens(int userId, GeneralCommunicationCallback<HashMap<Integer, FinanzbuchungToken>> callback) {
        HashMap<String, String> postData = new HashMap<>();
        postData.put("userId", String.valueOf(userId));

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
                        returnMap.put(newToken.getTokenId(), newToken);
                    }

                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                    Toasty.error(context, "Konnte Tokendaten nicht verarbeiten: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                } catch (Exception e) {
                    Toasty.error(context, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    Log.d("Tokendaten Laden", e.getMessage());
                }

                callback.onRequestCompleted(returnMap);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context, "Tokens konnten nicht geladen werden", Toast.LENGTH_SHORT, true).show();
            }
        };

        // the response listener
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

        addToRequestQueue(request);
    }

    @Override
    public void queryFinancialEntries(QueryFilter filter, GeneralCommunicationCallback<HashMap<Integer, Finanzbuchung>> callback) {
        HashMap<String, String> postData = new HashMap<>();
        postData.put("filter", String.valueOf(filter.getFilterString()));

        String URL = context.getResources().getString(R.string.PHP_Scripts_Finanzbuchungen_Abfragen);

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HashMap<Integer, Finanzbuchung> returnMap = new HashMap<>();

                try {
                    // Getting JSON Array node
                    JSONArray tokenArray = response.getJSONArray("Buchungen");
                    /*
                    //looping through all the elements in json array
                    for (int i = 0; i < tokenArray.length(); i++) {
                        FinanzbuchungToken newToken = new FinanzbuchungToken(tokenArray.getJSONObject(i));
                        returnMap.put(newToken.getTokenId(), newToken);
                    }
                    */
                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                    Toasty.error(context, "Konnte Buchungsdaten nicht verarbeiten: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                } catch (Exception e) {
                    Toasty.error(context, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    Log.d("Buchungsdaten Laden", e.getMessage());
                }

                callback.onRequestCompleted(returnMap);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context, "Buchungsdaten konnten nicht geladen werden", Toast.LENGTH_SHORT, true).show();
            }
        };

        // the response listener
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

        addToRequestQueue(request);
    }

    @Override
    public void postFinancialEntry(Finanzbuchung_Buchung entry, boolean editMode, GeneralCommunicationCallback<Finanzbuchung_Buchung> callback) {
        if (editMode) {
            //Zuerst die Finanzbuchung löschen, bevor sie geupdated werden kann
            deleteFinancialEntry(entry, new GeneralCommunicationCallback<Boolean>() {
                @Override
                public void onRequestCompleted(Boolean data) {
                    postFinancialEntry(entry, new GeneralCommunicationCallback<Finanzbuchung_Buchung>() {
                        @Override
                        public void onRequestCompleted(Finanzbuchung_Buchung data) {
                            callback.onRequestCompleted(data);
                        }
                    });
                }
            });
        }

        postFinancialEntry(entry, new GeneralCommunicationCallback<Finanzbuchung_Buchung>() {
            @Override
            public void onRequestCompleted(Finanzbuchung_Buchung data) {
                callback.onRequestCompleted(data);
            }
        });
    }

    private void postFinancialEntry(Finanzbuchung_Buchung entry, GeneralCommunicationCallback<Finanzbuchung_Buchung> callback) {
        int postCount = entry.getBuchungspositionen().size();
        ArrayList<Boolean> postSuccessProtocol = new ArrayList<Boolean>();

        if (entry.getIdentifier() == 0) {
            FinanzbuchungPosition firstPosition = entry.getBuchungspositionen().get(0);
            int kategorieId = firstPosition.getKategorieId();
            double betrag = firstPosition.getBetrag();

            HashMap<String, String> postData = new HashMap<String, String>();

            postData.put("BuchungID", String.valueOf(0));
            postData.put("Pos", String.valueOf(firstPosition.getId()));
            postData.put("BenutzerID", String.valueOf(FinanceManagerMobileApplication.getInstance().getDataManagement().getCurrentUser().getUserId()));
            postData.put("KatID", String.valueOf(kategorieId));
            postData.put("KontoID", String.valueOf(entry.getKontoId()));
            postData.put("Datum", entry.getDatum().format(DateConversionHelper.getSQLDateFormatter()));
            postData.put("Betrag", String.valueOf(betrag));
            postData.put("Beschreibung", entry.getBeschreibung());
            postData.put("KooperationID", String.valueOf(entry.getKooperationId()));

            String URL = context.getResources().getString(R.string.PHP_Scripts_Finanzbuchung_Post_Entry);

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (response.getString("Result").equals("success"))
                            postSuccessProtocol.add(true);
                        String responseString = response.getString("BuchungId");

                        if (Globale_Funktionen.isNumeric(responseString)) {
                            //Neue ID setzen und die übrigen Einträge verarbeiten
                            entry.setId(Integer.parseInt(responseString));

                            for (int i = 1; i < entry.getBuchungspositionen().size(); i++) {
                                FinanzbuchungPosition position = entry.getBuchungspositionen().get(i);
                                int kategorieId = position.getKategorieId();
                                double betrag = position.getBetrag();

                                HashMap<String, String> postData = new HashMap<String, String>();

                                postData.put("BuchungID", String.valueOf(entry.getIdentifier()));
                                postData.put("Pos", String.valueOf(position.getId()));
                                postData.put("BenutzerID", String.valueOf(FinanceManagerMobileApplication.getInstance().getDataManagement().getCurrentUser().getUserId()));
                                postData.put("KatID", String.valueOf(kategorieId));
                                postData.put("KontoID", String.valueOf(entry.getKontoId()));
                                postData.put("Datum", entry.getDatum().format(DateConversionHelper.getSQLDateFormatter()));
                                postData.put("Betrag", String.valueOf(betrag));
                                postData.put("Beschreibung", entry.getBeschreibung());
                                postData.put("KooperationID", String.valueOf(entry.getKooperationId()));

                                String URL = context.getResources().getString(R.string.PHP_Scripts_Finanzbuchung_Post_Entry);

                                Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            if (response.getString("Result").equals("success"))
                                                postSuccessProtocol.add(true);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                Response.ErrorListener errorListener = new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toasty.error(context, "Buchen fehlgeschlagen: " + error.getMessage(), Toast.LENGTH_LONG, true).show();
                                        postSuccessProtocol.add(false);
                                    }
                                };

                                // the response listener
                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

                                addToRequestQueue(request);
                            }

                            postTokenData(entry);
                            callback.onRequestCompleted(entry);
                        } else {
                            Toasty.error(context, "Buchungseintrag erzeugen Error: " + response, Toasty.LENGTH_LONG, true).show();
                            Log.e("Buchungseintrag erzeugen: ", response.toString());
                            postSuccessProtocol.add(false);
                        }
                    } catch (Exception e) {
                        Toasty.error(context, "Buchen fehlgeschlagen: " + e.getMessage(), Toast.LENGTH_LONG, true).show();
                        Log.e("Buchungseintrag erzeugen: ", e.getMessage());
                        postSuccessProtocol.add(false);
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toasty.error(context, "Buchen fehlgeschlagen: " + error.getMessage(), Toast.LENGTH_LONG, true).show();
                    postSuccessProtocol.add(false);
                }
            };

            // the response listener
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

            addToRequestQueue(request);
        } else {
            for (FinanzbuchungPosition position : entry.getBuchungspositionen()) {
                int kategorieId = position.getKategorieId();
                double betrag = position.getBetrag();

                HashMap<String, String> postData = new HashMap<String, String>();

                postData.put("BuchungID", String.valueOf(entry.getIdentifier()));
                postData.put("Pos", String.valueOf(position.getId()));
                postData.put("BenutzerID", String.valueOf(FinanceManagerMobileApplication.getInstance().getDataManagement().getCurrentUser().getUserId()));
                postData.put("KatID", String.valueOf(kategorieId));
                postData.put("KontoID", String.valueOf(entry.getKontoId()));
                postData.put("Datum", DateConversionHelper.getSQL_DateFormat().format(entry.getDatum()));
                postData.put("Betrag", String.valueOf(betrag));
                postData.put("Beschreibung", entry.getBeschreibung());
                postData.put("KooperationID", String.valueOf(entry.getKooperationId()));

                String URL = context.getResources().getString(R.string.PHP_Scripts_Finanzbuchung_Post_Entry);

                Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("Result").equals("success"))
                                postSuccessProtocol.add(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toasty.error(context, "Buchen fehlgeschlagen: " + error.getMessage(), Toast.LENGTH_LONG, true).show();
                        postSuccessProtocol.add(false);
                    }
                };

                // the response listener
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

                addToRequestQueue(request);
            }

            while (postSuccessProtocol.size() < postCount) {

            }
            //Falls eine Position nicht gebucht werden konnte -> Rollback
            if (postSuccessProtocol.stream().anyMatch(val -> !val)) {
                deleteFinancialEntry(entry, new GeneralCommunicationCallback<Boolean>() {
                    @Override
                    public void onRequestCompleted(Boolean data) {
                        Toasty.error(context, "Buchungsvorgang fehlgeschlagen", Toast.LENGTH_SHORT, true).show();
                    }
                });
            } else {
                postTokenData(entry);
                callback.onRequestCompleted(entry);
            }
        }
    }

    private void postTokenData(Finanzbuchung_Buchung entry) {
        for (FinanzbuchungToken eintrag : entry.getTokens()) {
            String URL = context.getResources().getString(R.string.PHP_Scripts_Finanzbuchung_Post_Token);

            HashMap<String, String> postData = new HashMap<String, String>();
            postData.put("BuchungID", String.valueOf(entry.getIdentifier()));
            postData.put("TokenID", String.valueOf(eintrag.getTokenId()));

            PostResponseAsyncTask TokenBuchenTask =
                    new PostResponseAsyncTask(context, postData, false,
                            new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {

                                }
                            });
            TokenBuchenTask.execute(URL);
        }
    }

    @Override
    public void deleteFinancialEntry(Finanzbuchung_Buchung entry, GeneralCommunicationCallback<Boolean> callback) {
        HashMap<String, String> postData = new HashMap<>();
        postData.put("BuchungID", String.valueOf(entry.getIdentifier()));

        String URL = context.getResources().getString(R.string.PHP_Scripts_Finanzbuchung_Delete);

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onRequestCompleted(true);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context, "Buchung konnte nicht gelöscht werden", Toast.LENGTH_SHORT, true).show();
            }
        };

        // the response listener
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

        addToRequestQueue(request);
    }

    @Override
    public void updateCategory(Buchungskategorie category, GeneralCommunicationCallback<Buchungskategorie> callback) {
        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("Id", String.valueOf(category.getId()));
        postData.put("üKatId", String.valueOf(category.getÜKatId()));
        postData.put("red", String.valueOf(category.getRot())) ;
        postData.put("green", String.valueOf(category.getGrün()));
        postData.put("blue", String.valueOf(category.getBlau()));
        postData.put("kategorieName", category.getBeschreibung());
        postData.put("buchtyp", String.valueOf(category.getBuchtyp().getDatabaseValue()));

        String URL = context.getResources().getString(R.string.PHP_Scripts_Kategorie_Update);

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("Result").equals("error")) {
                        Toasty.error(context, "Kategorie konnte nicht geupdated werden", Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                    Toasty.error(context, "Kategorie konnte nicht geupdated werden: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                } catch (Exception e) {
                    Toasty.error(context, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    e.printStackTrace();
                    Log.d("Kategorie update: ", e.getMessage());
                }

                callback.onRequestCompleted(category);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context, "Kategorie konnte nicht geupdated werden: " + error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        };

        // the response listener
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

        addToRequestQueue(request);
    }

    @Override
    public void createCategory(int userId, Buchungskategorie category, GeneralCommunicationCallback<Buchungskategorie> callback) {
        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("UserID", String.valueOf(userId));
        postData.put("üKatId", String.valueOf(category.getÜKatId()));
        postData.put("red", String.valueOf(category.getRot())) ;
        postData.put("green", String.valueOf(category.getGrün()));
        postData.put("blue", String.valueOf(category.getBlau()));
        postData.put("kategorieName", category.getBeschreibung());
        postData.put("buchtyp", String.valueOf(category.getBuchtyp().getDatabaseValue()));

        String URL = context.getResources().getString(R.string.PHP_Scripts_Kategorie_Anlegen);

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    int categoryId = response.getInt("Id");
                    category.setId(categoryId);
                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                    Toasty.error(context, "Kategorie konnte nicht erstellt werden: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                } catch (Exception e) {
                    Toasty.error(context, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    e.printStackTrace();
                    Log.d("Kategorie erstellen: ", e.getMessage());
                }

                callback.onRequestCompleted(category);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context, "Kategorie konnte nicht erstellt werden: " + error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        };

        // the response listener
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

        addToRequestQueue(request);
    }

    @Override
    public void deleteCategory(int categoryId, GeneralCommunicationCallback<Boolean> callback) {
        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("kategorieId", String.valueOf(categoryId));
        String URL = context.getResources().getString(R.string.PHP_Scripts_Kategorie_Löschen);

        JsonObjectRequest kategorieDeleteRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData),
                response -> {
                    try {
                        if (response.getInt("Result") == 0) {
                            callback.onRequestCompleted(false);
                        } else {
                            callback.onRequestCompleted(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toasty.error(context, "Kategorie konnte nicht gelöscht werden: " + error.getMessage(), Toasty.LENGTH_LONG, true).show();
                    Log.e("Kategorie_Delete_Error", error.getMessage());
                });

        addToRequestQueue(kategorieDeleteRequest);
    }

    @Override
    public void createToken(FinanzbuchungToken token, GeneralCommunicationCallback<FinanzbuchungToken> callback) {
        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("UserID", String.valueOf(userId));
        postData.put("üKatId", String.valueOf(category.getÜKatId()));
        postData.put("red", String.valueOf(category.getRot())) ;
        postData.put("green", String.valueOf(category.getGrün()));
        postData.put("blue", String.valueOf(category.getBlau()));
        postData.put("kategorieName", category.getBeschreibung());
        postData.put("buchtyp", String.valueOf(category.getBuchtyp().getDatabaseValue()));

        String URL = context.getResources().getString(R.string.PHP_Scripts_Kategorie_Anlegen);

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    int categoryId = response.getInt("Id");
                    category.setId(categoryId);
                } catch (JSONException e) {
                    Log.d("JsonException", e.getMessage());
                    Toasty.error(context, "Kategorie konnte nicht erstellt werden: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                } catch (Exception e) {
                    Toasty.error(context, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    e.printStackTrace();
                    Log.d("Kategorie erstellen: ", e.getMessage());
                }

                callback.onRequestCompleted(category);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context, "Kategorie konnte nicht erstellt werden: " + error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        };

        // the response listener
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(postData), responseListener, errorListener);

        addToRequestQueue(request);
    }

    @Override
    public void updateToken(FinanzbuchungToken token, GeneralCommunicationCallback<FinanzbuchungToken> callback) {

    }

    @Override
    public void deleteToken(int tokenId, GeneralCommunicationCallback<Boolean> callback) {

    }

    private void addToRequestQueue(Request request) {
        if (!isNetworkAvailable()) {
            Toasty.error(context, "Keine Internetverbindung", Toast.LENGTH_SHORT, true).show();
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
