package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.ApplicationController;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.Hauptmenu.Hauptmenu;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.CustomAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class Gemeinsame_Finanzen_Anfrage_BearbeitenDialog extends DialogFragment {
    private ListView lvBenutzer;
    private ListView lvKonten;
    private AppCompatImageButton btnClose;
    private AppCompatImageButton btnSpeichern;
    private Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Viewpager_Adapter fragmentPagerAdapter;
    private ViewPager viewPager;
    private TextInputEditText txtBeschreibung;
    private boolean bearbeiten;
    private Gemeinsame_Finanzen_Anfrage_BearbeitenCallback callback;
    private KooperationAnfrage anfrage;
    private boolean editMode;
    private Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Konten kontenFragment;
    private Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Benutzer benutzerFragment;

    Gemeinsame_Finanzen_Anfrage_BearbeitenDialog(boolean bearbeiten, KooperationAnfrage anfrage, Gemeinsame_Finanzen_Anfrage_BearbeitenCallback callback) {
        this.bearbeiten = bearbeiten;
        this.callback = callback;
        this.anfrage = anfrage;
        this.editMode = anfrage != null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gemeinsame_finanzen_anfrage_neueanfrage_dialog, container, false);

        btnSpeichern = view.findViewById(R.id.Toolbar_btnSpeichern);
        btnClose = view.findViewById(R.id.Toolbar_btnClose);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        lvKonten = view.findViewById(R.id.lvKonten);
        lvBenutzer = view.findViewById(R.id.lvBenutzer);

        fragmentPagerAdapter = new Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Viewpager_Adapter(getChildFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);

        tabLayout.setupWithViewPager(viewPager, true);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_account_balance_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_white_24dp);

        fragmentPagerAdapter.startUpdate(viewPager);
        kontenFragment = (Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Konten) fragmentPagerAdapter.instantiateItem(viewPager, 0);
        benutzerFragment = (Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Benutzer) fragmentPagerAdapter.instantiateItem(viewPager, 1);
        fragmentPagerAdapter.finishUpdate(viewPager);

        btnSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialog confirmationDialog = new CustomAlertDialog(getContext(), "Bestätigen", "Wollen Sie die Anfrage speichern?", "Speichern", "Abbrechen");
                confirmationDialog.setOkButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Eingaben_Überprüfen()) {
                            if (editMode) {
                                Anfrage_Update(anfrage.getId());
                                confirmationDialog.dismiss();
                            } else {
                                HashMap<String, String> postData = new HashMap<String, String>();
                                postData.put("BenutzerID", String.valueOf(GlobaleVariablen.getInstance().getUserId()));
                                postData.put("Beschreibung", kontenFragment.getBeschreibung());
                                postData.put("Mail", GlobaleVariablen.getInstance().getEmail());
                                postData.put("Gleichverteilung", (benutzerFragment.getGleichverteilung()) ? "1" : "0");

                                PostResponseAsyncTask AnfrageAnlegenTask = new PostResponseAsyncTask(getContext(),postData, false, new AsyncResponse() {
                                    @Override
                                    public void processFinish(String s) {
                                        Anfrage_Anlegen(Integer.parseInt(s));
                                        callback.onAnfrageEdited(new KooperationAnfrage(Integer.parseInt(s),
                                                GlobaleVariablen.getInstance().getUserId(),
                                                GlobaleVariablen.getInstance().getEmail(),
                                                kontenFragment.getBeschreibung(),
                                                benutzerFragment.getGleichverteilung(),
                                                Calendar.getInstance().getTime(),
                                                0,
                                                benutzerFragment.getBenutzerMap(),
                                                kontenFragment.getKontoMap()));
                                        //Meldung anzeigen
                                        Toasty.success(getContext(), "Gespeichert", Toast.LENGTH_SHORT, true).show();
                                        confirmationDialog.dismiss();
                                        dismiss();
                                    }
                                });
                                AnfrageAnlegenTask.execute("http://financemanagermm.sytes.net/fmclient/kooperation_anfrage_anlegen.php");
                            }
                        }
                    }
                });
                confirmationDialog.show();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onAnfrageEdited(anfrage);
                dismiss();
            }
        });

        return view;
    }

    private void Anfrage_Update(int anfrageId) {
        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("AnfrageID", String.valueOf(anfrageId));
        postData.put("Beschreibung", kontenFragment.getBeschreibung());
        postData.put("Gleichverteilung", (benutzerFragment.getGleichverteilung()) ? "1" : "0");

        PostResponseAsyncTask AnfrageUpdateTask = new PostResponseAsyncTask(getContext(),postData, false, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.equals("success")) {
                    boolean result = true;
                    kontenFragment.getKontoMap().values().forEach(konto -> {
                        HashMap<String, String> postData = new HashMap<String, String>();
                        postData.put("AnfrageID", String.valueOf(anfrageId));
                        postData.put("KontoID", String.valueOf(konto.getIdentifier()));
                        postData.put("KontoName", konto.getKontoTitel());
                        postData.put("KontoArt", konto.getKontoArt().toString());
                        postData.put("Anfangsbestand", String.valueOf(konto.getAnfangsbestand()));
                        postData.put("DatumAnfangsbestand", GlobaleVariablen.getInstance().getSQL_DateFormat().format(konto.getDatumAnfangsbestand()));

                        PostResponseAsyncTask AnfrageKontoAnlegenTask = new PostResponseAsyncTask(getContext(), postData, false, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if(s.equals("success")) {
                                    //Toasty.error(getContext(), "Gespeichert", Toast.LENGTH_SHORT, true).show();
                                } else {
                                    Toasty.error(Hauptmenu.context, "Anfragedaten Konten updaten: " + s, Toast.LENGTH_LONG, true).show();
                                }
                            }
                        });
                        AnfrageKontoAnlegenTask.execute("http://financemanagermm.sytes.net/fmclient/kooperation_anfrage_anlegen_konto.php");
                    });
                    benutzerFragment.getBenutzerMap().values().forEach(benutzer -> {
                        HashMap<String, String> postData = new HashMap<String, String>();
                        postData.put("AnfrageID", String.valueOf(anfrageId));
                        postData.put("BenutzerID", String.valueOf(benutzer.getBenutzerID()));
                        postData.put("BenutzerMail", benutzer.getMail());
                        postData.put("Status", benutzer.getStatus().toString());
                        postData.put("Verteilung", String.valueOf(benutzer.getVerteilung()));
                        postData.put("Datum",  "NULL");

                        PostResponseAsyncTask AnfrageBenutzerAnlegenTask = new PostResponseAsyncTask(getContext(), postData, false, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if(s.equals("success")) {
                                    //Toasty.error(getContext(), "Gespeichert", Toast.LENGTH_SHORT, true).show();
                                } else {
                                    Toasty.error(Hauptmenu.context, "Anfragedaten Benutzer updaten: " + s, Toast.LENGTH_LONG, true).show();
                                }
                            }
                        });
                        AnfrageBenutzerAnlegenTask.execute("http://financemanagermm.sytes.net/fmclient/kooperation_anfrage_anlegen_benutzer.php");
                    });
                } else {
                    Toasty.error(Hauptmenu.context, "Anfragedaten updaten: " + s, Toast.LENGTH_LONG, true).show();
                }
            }
        });
        AnfrageUpdateTask.execute("http://financemanagermm.sytes.net/fmclient/kooperation_anfrage_update.php");

        //Allgemeine Daten updaten
        anfrage.setBeschreibung(kontenFragment.getBeschreibung());
        anfrage.setGleichverteilung(benutzerFragment.getGleichverteilung());

        //Kontodaten updaten
        anfrage.setKontoMap(kontenFragment.getKontoMap());
        //Benutzerdaten updaten
        anfrage.setBenutzerMap(benutzerFragment.getBenutzerMap());

        //Meldung anzeigen
        Toasty.success(getContext(), "Gespeichert", Toast.LENGTH_SHORT, true).show();

        //Callback aktivieren
        callback.onAnfrageEdited(anfrage);

        //Dialogfenster schließen
        dismiss();
    }

    private void Anfrage_Anlegen(int anfrageId) {
        kontenFragment.getKontoMap().values().forEach(konto -> {
            HashMap<String, String> postData = new HashMap<String, String>();
            postData.put("AnfrageID", String.valueOf(anfrageId));
            postData.put("KontoID", String.valueOf(konto.getIdentifier()));
            postData.put("KontoName", konto.getKontoTitel());
            postData.put("KontoArt", konto.getKontoArt().toString());
            postData.put("Anfangsbestand", String.valueOf(konto.getAnfangsbestand()));
            postData.put("DatumAnfangsbestand", GlobaleVariablen.getInstance().getSQL_DateFormat().format(konto.getDatumAnfangsbestand()));

            PostResponseAsyncTask AnfrageKontoAnlegenTask = new PostResponseAsyncTask(getContext(), postData, false, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if(s.equals("success")) {
                        //Toasty.error(getContext(), "Gespeichert", Toast.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.error(Hauptmenu.context, "Anfragedaten Konten updaten: " + s, Toast.LENGTH_LONG, true).show();
                    }
                }
            });
            AnfrageKontoAnlegenTask.execute("http://financemanagermm.sytes.net/fmclient/kooperation_anfrage_anlegen_konto.php");
        });
        benutzerFragment.getBenutzerMap().values().forEach(benutzer -> {
            HashMap<String, String> postData = new HashMap<String, String>();
            postData.put("AnfrageID", String.valueOf(anfrageId));
            postData.put("BenutzerID", String.valueOf(benutzer.getBenutzerID()));
            postData.put("BenutzerMail", benutzer.getMail());
            postData.put("Status", benutzer.getStatus().toString());
            postData.put("Verteilung", String.valueOf(benutzer.getVerteilung()));
            postData.put("Datum",  "NULL");

            PostResponseAsyncTask AnfrageBenutzerAnlegenTask = new PostResponseAsyncTask(getContext(), postData, false, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if(s.equals("success")) {
                        //Toasty.error(getContext(), "Gespeichert", Toast.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.error(Hauptmenu.context, "Anfragedaten Benutzer updaten: " + s, Toast.LENGTH_LONG, true).show();
                    }
                }
            });
            AnfrageBenutzerAnlegenTask.execute("http://financemanagermm.sytes.net/fmclient/kooperation_anfrage_anlegen_benutzer.php");
        });
    }

    private boolean Eingaben_Überprüfen() {
        if (kontenFragment.getBeschreibung().equals("")) {
            Toasty.error(getContext(), "Keine Beschreibung vorhanden", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (kontenFragment.getKontoMap().size() == 0) {
            Toasty.error(getContext(), "Erstellen Sie mind. ein Konto", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (benutzerFragment.getBenutzerMap().size() == 1) {
            Toasty.error(getContext(), "Fügen Sie mind. einen weiteren Benutzer hinzu", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(Math.round(benutzerFragment.getBenutzerMap().values().stream().mapToDouble(KooperationAnfrageBenutzer::getVerteilung).sum())  * 100 != 100) {

            Toasty.error(getContext(), "Die Prozente der Verteilung müssen zusammen 100% ergeben", Toast.LENGTH_LONG, true).show();
            return false;
        }
        return true;
    }

    public KooperationAnfrage getAnfrage() {
        return anfrage;
    }

    public static void getUserFromServerByEmail(String EmailAdresse, Gemeinsame_Finanzen_Anfrage_BenutzerAbfragenCallback callback) {
        HashMap postData = new HashMap();
        postData.put("Email", EmailAdresse.trim().toLowerCase());

        Context context = ApplicationController.getInstance().getApplicationContext();
        String kontenDatenURL = context.getResources().getString(R.string.PHP_Scripts_Kooperation_Anfrage_Benutzer_Abfragen);

        // the response listener
        JsonObjectRequest benutzerAbfrageRequest = new JsonObjectRequest(Request.Method.POST, kontenDatenURL, new JSONObject(postData),
                response -> {
                    try {
                        // Getting JSON Array node
                        JSONArray benutzer = response.getJSONArray("Benutzer");

                        //getting json object from the json array
                        JSONObject obj = benutzer.getJSONObject(0);

                        //getting the name from the json object and putting it inside string array
                        int BenutzerID = obj.getInt("BenutzerID");
                        String Benutzername = obj.getString("Benutzername");
                        String eMail = obj.getString("Email");

                        callback.onBenutzerSuccessfullyQueried(new KooperationAnfrageBenutzer(
                                BenutzerID, Benutzername, eMail, 0, KooperationAnfrageBenutzer.AnfrageBenutzerStatus.OFFEN, Calendar.getInstance().getTime()
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onBenutzerSuccessfullyQueried(null);
                    }

                },
                error -> error.printStackTrace());

        ApplicationController.getInstance().addToRequestQueue(benutzerAbfrageRequest);
    }
}