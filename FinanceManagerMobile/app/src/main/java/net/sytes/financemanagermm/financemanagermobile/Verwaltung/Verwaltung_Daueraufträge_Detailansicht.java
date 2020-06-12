package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.tiper.MaterialSpinner;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Umbuchung;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Benutzer;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Konten;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Daueraufträge;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.CustomAlertDialog;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Verwaltung_Daueraufträge_Detailansicht extends DialogFragment implements View.OnClickListener {
    private CardView TopCardView;
    private ImageView KontoArtImage;
    private TextView NächsteAusführung;
    private TextView ZuletztAusgeführt;
    private MaterialButton btnSpeichern;
    private AppCompatImageButton btnClose;
    private TextView DialogTitle;
    private ImageButton btnDelete;
    private Dauerauftrag dauerauftrag;
    private int kooperationID;
    private Dauerauftrag.Dauerauftrag_Art dauerauftragArt;
    private Dauerauftrag_Edited_Callback callback;
    private boolean updateMode;
    private boolean callFromKooperation;
    private Verwaltung_Daueraufträge_Fragment_Allgemein fragmentAllgemein;
    private Verwaltung_Daueraufträge_Fragment_Details fragmentDetails;

    public Verwaltung_Daueraufträge_Detailansicht(Dauerauftrag dauerauftrag, int kooperationID, Dauerauftrag.Dauerauftrag_Art dauerauftragArt, boolean updateMode, boolean callFromKooperation,Dauerauftrag_Edited_Callback callback) {
        this.dauerauftrag = dauerauftrag;
        this.kooperationID = kooperationID;
        this.dauerauftragArt = dauerauftragArt;
        this.callback = callback;
        this.updateMode = updateMode;
        this.callFromKooperation = callFromKooperation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verwaltung_dauerauftraege_detailansicht, container, false);

        DialogTitle = view.findViewById(R.id.Kontendetails_lblDialogTitle);
        KontoArtImage = view.findViewById(R.id.KontoArtImage);
        NächsteAusführung = view.findViewById(R.id.NächsteAusführung);
        ZuletztAusgeführt = view.findViewById(R.id.LetzteAusführung);
        btnClose = view.findViewById(R.id.Kontendetails_btnDialogClose);
        btnClose.setOnClickListener(this);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnSpeichern = view.findViewById(R.id.Kontendetails_btn_Speichern);
        TopCardView = view.findViewById(R.id.Dauerauftrag_Details_TopCardView);

        FragmentPagerAdapter adapter = new Verwaltung_Daueraufträge_Fragment_Viewpager_Adapter(
                getChildFragmentManager(), dauerauftrag, dauerauftragArt, kooperationID, callFromKooperation);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        adapter.startUpdate(viewPager);
        fragmentAllgemein = (Verwaltung_Daueraufträge_Fragment_Allgemein) adapter.instantiateItem(viewPager, 0);
        fragmentDetails = (Verwaltung_Daueraufträge_Fragment_Details) adapter.instantiateItem(viewPager, 1);
        adapter.finishUpdate(viewPager);

        SmartTabLayout viewPagerTab = (SmartTabLayout) view.findViewById(R.id.tab_layout);
        viewPagerTab.setViewPager(viewPager);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (!updateMode) {
            TopCardView.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            DialogTitle.setText("Dauerauftrag erstellen");
        } else {
            TopCardView.setVisibility(View.VISIBLE);
            DialogTitle.setText(dauerauftrag.getBeschreibung());
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomAlertDialog confirmationDialog = new CustomAlertDialog(getContext(), "Bestätigen", "Wollen Sie den Dauerauftrag wirklich löschen?", "Löschen", "Abbrechen");
                    confirmationDialog.setOkButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String tableName;
                            String deleteURL = getContext().getResources().getString(R.string.PHP_Scripts_Daueraufträge_Löschen);
                            if(dauerauftragArt == Dauerauftrag.Dauerauftrag_Art.NORMAL) {
                                tableName = "Dauerauftrag";
                            } else {
                                tableName = "Dauerauftrag_Umbuchung";
                            }
                            HashMap<String, String> data = new HashMap<>();
                            data.put("DauerauftragID", String.valueOf(dauerauftrag.getId()));
                            data.put("tableName", tableName);

                            PostResponseAsyncTask deleteTask =
                                    new PostResponseAsyncTask(getContext(), data, false, new AsyncResponse() {
                                        @Override
                                        public void processFinish(String s) {
                                            if (s.equals("success")) {
                                                Toasty.success(getContext(), "Gelöscht", Toast.LENGTH_SHORT, true).show();
                                                confirmationDialog.dismiss();
                                                dismiss();
                                                Daueraufträge.getDaueraufträge().remove(dauerauftrag.getIdentifier());
                                                callback.onDauerauftragEdited(null);
                                            } else {
                                                Toasty.error(getContext(), "Dauerauftrag löschen: " + s, Toast.LENGTH_LONG, true).show();
                                                confirmationDialog.dismiss();
                                            }
                                        }
                                    });
                            deleteTask.execute(deleteURL);


                        }
                    });
                    confirmationDialog.show();
                }
            });
            try {
                TopCardView_Füllen();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        btnSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Eingabe Check -> Beendet Methode vorzeitig
                if (Eingaben_Überprüfen()) {
                    String beschreibung = fragmentAllgemein.getBeschreibung();
                    int kontoID = 0;
                    int kategorieID = 0;
                    int kontoVonID = 0;
                    int kontoAufID = 0;
                    if(dauerauftragArt == Dauerauftrag.Dauerauftrag_Art.NORMAL) {
                        kontoID = fragmentAllgemein.getKonto().getIdentifier();
                        kategorieID = fragmentDetails.getKategorieId();
                    } else {
                        kontoVonID = fragmentAllgemein.getKontoVon().getIdentifier();
                        kontoAufID = fragmentAllgemein.getKontoAuf().getIdentifier();
                    }

                    double betrag = Double.parseDouble(fragmentAllgemein.getBetrag());
                    String rhythmus = fragmentAllgemein.getRhythmus();
                    boolean aktiv = fragmentAllgemein.isAktiv();

                    int tokenID = fragmentDetails.getTokenID();

                    Date nächsteAusführung = new Date();
                    try {
                         nächsteAusführung = GlobaleVariablen.getInstance().getDE_DateFormat().parse(fragmentDetails.getNächsteAusführung());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //Neuer Dauerauftrag anlegen
                    if (!updateMode) {
                        //Dauerauftrag erstellen
                        dauerauftrag = (dauerauftragArt == Dauerauftrag.Dauerauftrag_Art.NORMAL) ? new Dauerauftrag() : new Dauerauftrag_Umbuchung();
                        HashMap<String, String> DauerauftragDaten = new HashMap<String, String>();

                        DauerauftragDaten.put("UserID", String.valueOf(GlobaleVariablen.getInstance().getUserId()));
                        DauerauftragDaten.put("Beschreibung", beschreibung);
                        dauerauftrag.setBeschreibung(beschreibung);
                        DauerauftragDaten.put("Betrag", String.valueOf(betrag));
                        dauerauftrag.setBetrag(betrag);
                        DauerauftragDaten.put("TokenID", String.valueOf(tokenID));
                        dauerauftrag.setTokenID(tokenID);
                        DauerauftragDaten.put("KooperationID", String.valueOf(kooperationID));
                        dauerauftrag.setKooperationID(kooperationID);
                        DauerauftragDaten.put("Rhythmus", rhythmus);
                        dauerauftrag.setRhythmus(rhythmus);
                        DauerauftragDaten.put("NächsteAusführung", GlobaleVariablen.getInstance().getSQL_DateFormat().format(nächsteAusführung));
                        dauerauftrag.setNächsteAusführung(nächsteAusführung);
                        DauerauftragDaten.put("Aktiv", (aktiv) ? "1" : "0");
                        dauerauftrag.setAktiv(aktiv);

                        String createURL = "";
                        if(dauerauftragArt == Dauerauftrag.Dauerauftrag_Art.NORMAL) {
                            DauerauftragDaten.put("KategorieID", String.valueOf(kategorieID));
                            dauerauftrag.setKategorieID(kategorieID);
                            DauerauftragDaten.put("KontoID", String.valueOf(kontoID));
                            dauerauftrag.setKonto(fragmentAllgemein.getKonto());
                            createURL = getResources().getString(R.string.PHP_Scripts_Dauerauftrag_Anlegen);
                            dauerauftrag.setDauerauftragArt(Dauerauftrag.Dauerauftrag_Art.NORMAL);
                        } else {
                            DauerauftragDaten.put("KontoIDAuf", String.valueOf(kontoAufID));
                            ((Dauerauftrag_Umbuchung) dauerauftrag).setKontoAuf(fragmentAllgemein.getKontoAuf());
                            DauerauftragDaten.put("KontoIDVon", String.valueOf(kontoVonID));
                            ((Dauerauftrag_Umbuchung) dauerauftrag).setKontoVon(fragmentAllgemein.getKontoVon());
                            createURL = getResources().getString(R.string.PHP_Scripts_Dauerauftrag_Umbuchung_Anlegen);
                            dauerauftrag.setDauerauftragArt(Dauerauftrag.Dauerauftrag_Art.UMBUCHUNG);
                        }

                        PostResponseAsyncTask AnlegenTask =
                                new PostResponseAsyncTask(getContext(), DauerauftragDaten, false, new AsyncResponse() {
                                    @Override
                                    public void processFinish(String s) {
                                        if (Globale_Funktionen.isNumeric(s)) {
                                            Toasty.success(getContext(), "Gespeichert", Toast.LENGTH_SHORT, true).show();
                                            dismiss();
                                            dauerauftrag.setId(Integer.valueOf(s));
                                            callback.onDauerauftragEdited(dauerauftrag);
                                        } else {
                                            Toasty.error(getContext(), "Dauerauftrag erstellen: " + s, Toast.LENGTH_LONG, true).show();
                                        }
                                    }
                                });
                        AnlegenTask.execute(createURL);
                    } else {
                        //UPDATE
                        int DauerauftragID = dauerauftrag.getId(); //erst hier die DauerauftragID initialisieren, sonst Fehler da neuer Dauerauftrag noch keine ID hat
                        HashMap<String, String> DauerauftragDaten = new HashMap<String, String>();

                        DauerauftragDaten.put("DauerauftragID", String.valueOf(DauerauftragID));
                        DauerauftragDaten.put("Beschreibung", beschreibung);
                        dauerauftrag.setBeschreibung(beschreibung);
                        DauerauftragDaten.put("Betrag", String.valueOf(betrag));
                        dauerauftrag.setBetrag(betrag);
                        DauerauftragDaten.put("TokenID", String.valueOf(tokenID));
                        dauerauftrag.setTokenID(tokenID);
                        DauerauftragDaten.put("KooperationID", String.valueOf(kooperationID));
                        dauerauftrag.setKooperationID(kooperationID);
                        DauerauftragDaten.put("Rhythmus", rhythmus);
                        dauerauftrag.setRhythmus(rhythmus);
                        DauerauftragDaten.put("NächsteAusführung", GlobaleVariablen.getInstance().getSQL_DateFormat().format(nächsteAusführung));
                        dauerauftrag.setNächsteAusführung(nächsteAusführung);
                        try {
                            dauerauftrag.setAusgeführtAm(GlobaleVariablen.getInstance().getDE_DateFormat().parse("01.01.1900"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        DauerauftragDaten.put("Aktiv", (aktiv) ? "1" : "0");
                        dauerauftrag.setAktiv(aktiv);

                        String updateURL = "";
                        if(dauerauftragArt == Dauerauftrag.Dauerauftrag_Art.NORMAL) {
                            DauerauftragDaten.put("KategorieID", String.valueOf(kategorieID));
                            dauerauftrag.setKategorieID(kategorieID);
                            DauerauftragDaten.put("KontoID", String.valueOf(kontoID));
                            dauerauftrag.setKonto(fragmentAllgemein.getKonto());
                            updateURL = getResources().getString(R.string.PHP_Scripts_Daueraufträge_Update);
                        } else {
                            DauerauftragDaten.put("KontoIDAuf", String.valueOf(kontoAufID));
                            ((Dauerauftrag_Umbuchung) dauerauftrag).setKontoAuf(fragmentAllgemein.getKontoAuf());
                            DauerauftragDaten.put("KontoIDVon", String.valueOf(kontoVonID));
                            ((Dauerauftrag_Umbuchung) dauerauftrag).setKontoVon(fragmentAllgemein.getKontoVon());
                            updateURL = getResources().getString(R.string.PHP_Scripts_DaueraufträgeUmbuchungen_Update);
                        }

                        PostResponseAsyncTask UpdateTask =
                                new PostResponseAsyncTask(getContext(), DauerauftragDaten, false, new AsyncResponse() {
                                    @Override
                                    public void processFinish(String s) {
                                        if (s.equals("success")) {
                                            Toasty.success(getContext(), "Gespeichert", Toast.LENGTH_SHORT, true).show();
                                            dismiss();
                                            callback.onDauerauftragEdited(dauerauftrag);
                                        } else {
                                            Toasty.error(Objects.requireNonNull(getContext()), "Dauerauftrag updaten: " + s, Toast.LENGTH_LONG, true).show();
                                        }
                                    }
                                });
                        UpdateTask.execute(updateURL);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.Kontendetails_btnDialogClose:
                dismiss();
                break;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().requestWindowFeature(Window.FEATURE_ACTION_BAR);
        Toolbar toolbar = (Toolbar) getDialog().findViewById(R.id.toolbar);
        //toolbar.setTitle("Dialog title");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
            String title = getArguments().getString("title", "Enter Name");
            actionBar.setTitle(title);
        }

    }

    private boolean TopCardView_Füllen() throws ParseException {
        KontoArtImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_white_24dp, null));

        NächsteAusführung.setText("Nächste Ausführung: " + GlobaleVariablen.getInstance().getDE_DateFormat().format(dauerauftrag.getNächsteAusführung()));
        ZuletztAusgeführt.setText("Zuletzt gebucht: " + GlobaleVariablen.getInstance().getDE_DateFormat().format(dauerauftrag.getAusgeführtAm()));

        return true;
    }

    private boolean Eingaben_Überprüfen() {
        if (fragmentAllgemein.getBeschreibung().equals("")) {
            Toasty.error(getContext(), "Geben Sie eine Beschreibung an", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (dauerauftragArt == Dauerauftrag.Dauerauftrag_Art.NORMAL && fragmentAllgemein.getKonto() == null) {
            Toasty.error(getContext(), "Wählen Sie ein Konto aus", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (dauerauftragArt == Dauerauftrag.Dauerauftrag_Art.UMBUCHUNG && (fragmentAllgemein.getKontoVon() == null || fragmentAllgemein.getKontoAuf() == null)) {
            Toasty.error(getContext(), "Wählen Sie ein Abbuchungs - und Aufbuchungskonto aus", Toast.LENGTH_LONG, true).show();
            return false;
        }
        if (fragmentAllgemein.getBetrag().equals("") || !Globale_Funktionen.isNumeric(fragmentAllgemein.getBetrag())) {
            Toasty.error(getContext(), "Geben Sie einen Betrag an", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (fragmentAllgemein.getRhythmus().equals("")) {
            Toasty.error(getContext(), "Geben Sie einen Ausführungsrhythmus an", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (dauerauftragArt == Dauerauftrag.Dauerauftrag_Art.NORMAL && fragmentDetails.getKategorieId() == 0) {
            Toasty.error(getContext(), "Geben Sie eine Buchungskategorie an", Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (fragmentDetails.getNächsteAusführung().equals("") || !Globale_Funktionen.isGermanDateString(fragmentDetails.getNächsteAusführung())) {
            Toasty.error(getContext(), "Geben Sie ein Ausführungsdatum im Format 'dd.MM.yyyy' an", Toast.LENGTH_LONG, true).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    interface Dauerauftrag_Edited_Callback {
        void onDauerauftragEdited(Dauerauftrag dauerauftrag);
    }
    public void setKooperationID (int id) {
        this.kooperationID = id;
    }
}
