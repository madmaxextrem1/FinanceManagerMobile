package net.sytes.financemanagermm.financemanagermobile.Buchungen;

import android.app.Dialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentManager;

import com.daimajia.swipe.SwipeLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.tiper.MaterialSpinner;

import net.sytes.financemanagermm.financemanagermobile.Sign_In_Up.FinanceManagerMobileApplication;
import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.FinanzbuchungPosition;
import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.Finanzbuchung_Buchung;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation_Adapter;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Finanzbuchungen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Kooperationen;
import net.sytes.financemanagermm.financemanagermobile.Helper.DateConversionHelper;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto_Adapter;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import es.dmoral.toasty.Toasty;

public class Buchung extends AppCompatActivity implements View.OnClickListener, Buchungszeile_Auswahl_Eintrag_ItemClickListener {
    private TextInputLayout txtDatum;
    private TextInputLayout txtTitel;
    private ListView lvBuchungHinzufügen;
    private Buchungszeile_Auswahl_SwipeAdapter BuchungszeileAdapter;
    private Konto_Adapter kontoAdapter;
    private ArrayList<FinanzbuchungToken> CheckedMerkmalListe = new ArrayList<FinanzbuchungToken>();
    private ChipGroup txtMerkmale;
    private MaterialSpinner cboKonto;
    private MaterialSpinner cboKooperation;
    private Kooperation_Adapter kooperationAdapter;
    private TextView lblZusammenfassung;
    private SwipeLayout swipeLayout;
    private AppCompatImageButton btnClose;
    private AppCompatImageButton btnBuchen;
    private FloatingActionButton KategorieHinzfügenFab;
    private Boolean editMode = false;
    private Finanzbuchung_Buchung buchungEintrag;
    private TextView lblActivityTitle;
    private static Buchung_Created_Interface callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buchung_activity);

        txtDatum = findViewById(R.id.Buchung_txtDatum);
        txtTitel = findViewById(R.id.Buchung_txtTitel);
        btnBuchen = findViewById(R.id.Buchung_btn_Buchen);
        btnClose = findViewById(R.id.Buchung_Toolbar_btnClose);
        btnClose.setOnClickListener(this);
        lvBuchungHinzufügen = (ListView) findViewById(R.id.Buchung_lvBuchungszeilen);
        cboKonto = (MaterialSpinner) findViewById(R.id.Buchung_cboKonto);
        cboKooperation = findViewById(R.id.cboKooperation);
        KategorieHinzfügenFab = (FloatingActionButton) findViewById(R.id.fab_kategorie_hinzufügen);
        lblZusammenfassung = (TextView) findViewById(R.id.Buchung_SummeZusammenfassung);
        lblActivityTitle = (TextView) findViewById(R.id.Buchung_Toolbar_Title);

        kooperationAdapter = new Kooperation_Adapter(this);
        kooperationAdapter.getLinkedMap().put(0, new Kooperation());
        kooperationAdapter.getLinkedMap().putAll(FinanceManagerMobileApplication.getInstance().getDataManagement().getCooperations());
        cboKooperation.setAdapter(kooperationAdapter);
        kooperationAdapter.notifyDataSetChanged();
        cboKooperation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
                if (kooperationAdapter.getItem(i).getIdentifier() == 0) {
                    cboKooperation.setTag(null);
                    cboKooperation.setSelection(-1);
                } else {
                    cboKooperation.setTag(kooperationAdapter.getItem(i));
                    cboKooperation.getEditText().setText(kooperationAdapter.getItem(i).getBeschreibung());
                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });

        //falls der EditMode aktiviert ist, werden die alten Daten geladen
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            editMode = intent.getExtras().getBoolean("EditMode");
            buchungEintrag = (Finanzbuchung_Buchung) intent.getExtras().getParcelable("BuchungEintrag");
        }

        btnBuchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Eingaben_Überprüfen()) {
                    return;
                }

                if (editMode) {
                    Dialog dialog = new Dialog(Buchung.this);

                    dialog.setContentView(R.layout.gemeinsame_finanzen_fragment_anfrage_alertdialog);

                    TextView txtTitel = (TextView) dialog.findViewById(R.id.Titel);
                    txtTitel.setText("Buchung überschreiben?");
                    TextView txtMessage = (TextView) dialog.findViewById(R.id.Message);
                    txtMessage.setText("Wollen Sie die bestehende Buchung tatsächlich überschreiben?");

                    MaterialButton speichernButton = (MaterialButton) dialog.findViewById(R.id.btnLöschen);
                    speichernButton.setText("Überschreiben");
                    speichernButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap BuchungDaten = new HashMap();
                            BuchungDaten.put("BuchungID", String.valueOf(buchungEintrag.getId()));

                            PostResponseAsyncTask LöschenTask =
                                    new PostResponseAsyncTask(Buchung.this, BuchungDaten, false, new AsyncResponse() {
                                        @Override
                                        public void processFinish(String s) {
                                            if (!s.equals("success")) {
                                                Toasty.error(Buchung.this, "Error: " + s, Toast.LENGTH_SHORT, true).show();
                                            } else {
                                                Buchen(buchungEintrag.getId());
                                            }
                                        }
                                    });
                            LöschenTask.execute("http://financemanagermm.sytes.net/fmclient/buchung_löschen.php");
                            dialog.dismiss();
                        }
                    });

                    MaterialButton abbrechenButton = (MaterialButton) dialog.findViewById(R.id.btnAbbrechen);
                    abbrechenButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    Buchen(0);
                }
            }
        });
        btnBuchen.requestFocus();

        KategorieHinzfügenFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Buchung_Kategorie_Dialog dialog = Buchung_Kategorie_Dialog.newInstance("Kategorie hinzufügen", null, BuchungszeileAdapter.getCount());
                dialog.setCallback(new Buchung_Kategorie_Dialog.Callback() {
                    @Override
                    public void onActionClick(FinanzbuchungPosition Eintrag) {
                        if (Eintrag != null) {
                            BuchungszeileAdapter.getEintragListe().add(Eintrag);
                            BuchungszeileAdapter.notifyDataSetChanged();
                            lvBuchungHinzufügen.smoothScrollToPosition(0);
                        }
                    }
                });
                dialog.show(fragmentManager, "fragment_kategorie_auswahl");
            }
        });
        View SwipeView = getLayoutInflater().inflate(R.layout.buchung_hinzufuegen_lvbuchungszeilen_lvitem, null);
        swipeLayout = (SwipeLayout) SwipeView.findViewById(R.id.buchung_buchungszeile_lvItem_SwipeLayout);

        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_wrapper));
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
        cboKonto.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner materialSpinner) {

            }

            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
                materialSpinner.getEditText().setText(kontoAdapter.getItem(i).getKontoTitel());
                materialSpinner.setTag(kontoAdapter.getItem(i));
            }
        });

        BuchungszeileAdapter = new Buchungszeile_Auswahl_SwipeAdapter(this, lvBuchungHinzufügen, Buchung.this::onBuchungszeileItemClicked);
        lvBuchungHinzufügen.setAdapter(BuchungszeileAdapter);
        kontoAdapter = new Konto_Adapter(this, FinanceManagerMobileApplication.getInstance().getDataManagement().getActiveAccounts());
        cboKonto.setAdapter(kontoAdapter);
        kontoAdapter.notifyDataSetChanged();

        BuchungszeileAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                GesamtsummeAnzeigen();
            }
        });

        txtMerkmale = (ChipGroup) findViewById(R.id.Buchung_chgMerkmale);
        for (FinanzbuchungToken eintrag : FinanceManagerMobileApplication.getInstance().getDataManagement().getTokens().values()) {
            Chip chip = new Chip(Buchung.this);
            chip.setText(eintrag.getBeschreibung());
            chip.setChipBackgroundColorResource(R.color.ChipBackGroundUnchecked);
            chip.setCheckable(true);
            chip.setTag(eintrag);
            chip.setTextColor(getResources().getColor(R.color.white, null));
            chip.setCheckedIconVisible(false);
            chip.setCloseIconVisible(false);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        chip.setChipBackgroundColorResource(R.color.ChipBackGroundChecked);
                        CheckedMerkmalListe.add((FinanzbuchungToken) chip.getTag());
                    } else {
                        chip.setChipBackgroundColorResource(R.color.ChipBackGroundUnchecked);
                        CheckedMerkmalListe.remove((FinanzbuchungToken) chip.getTag());
                    }
                }
            });
            switch (eintrag.getTyp()) {
                case PERSOENLICH:
                    chip.setChipIcon(getResources().getDrawable(R.drawable.ic_person_white_24dp));
                    chip.setChipIconTintResource(R.color.white);
                    break;
                case GRUPPE:
                    chip.setChipIcon(getResources().getDrawable(R.drawable.ic_group_white_24dp));
                    chip.setChipIconTintResource(R.color.white);
                    break;
            }

            txtMerkmale.addView(chip);
        }
        txtDatum.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long selection = 0;
                if (!txtDatum.getEditText().getText().toString().equals("")) {
                    try {
                        Date currentDate = GlobaleVariablen.getInstance().getDE_DateFormat().parse(txtDatum.getEditText().getText().toString().trim());
                        selection = currentDate.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    selection = Calendar.getInstance().getTime().getTime();
                }

                MaterialDatePicker<?> picker = Globale_Funktionen.getMaterialDatePicker(getString(R.string.DatePickerDialogTitle_Zeitpunkt), Optional.of(new Date(selection)), new MaterialPickerOnPositiveButtonClickListener<Object>() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Long selectedDate = (Long) selection;
                        Date Datum = new Date(selectedDate);
                        txtDatum.getEditText().setText(GlobaleVariablen.getInstance().getDE_DateFormat().format(Datum));
                    }
                });
                picker.show(getSupportFragmentManager(), "Test");
            }
        });

        if (editMode) {
            Objects.requireNonNull(txtTitel.getEditText()).setText(buchungEintrag.getBeschreibung());
            LocalDate date = buchungEintrag.getDatum();

            lblActivityTitle.setText("Buchung bearbeiten");
            txtDatum.getEditText().setText(date.format(DateConversionHelper.getDEDateFormatter()));
            cboKonto.setSelection(kontoAdapter.getItemPositionById(buchungEintrag.getKontoId()));
            BuchungszeileAdapter.getEintragListe().addAll(buchungEintrag.getBuchungspositionen());
            for (FinanzbuchungToken token : buchungEintrag.getTokens()) {
                for (int z = 0; z < txtMerkmale.getChildCount(); z++) {
                    FinanzbuchungToken eintrag = (FinanzbuchungToken) txtMerkmale.getChildAt(z).getTag();
                    Chip chip = (Chip) txtMerkmale.getChildAt(z);
                    if (eintrag.getTokenId() == token.getTokenId()) {
                        chip.setChecked(true);
                        break;
                    }
                }
            }
            BuchungszeileAdapter.notifyDataSetChanged();
            cboKooperation.setSelection((buchungEintrag.getKooperationId() == 0) ? -1 : kooperationAdapter.getItemPositionById(buchungEintrag.getKooperationId()));
        }
        GesamtsummeAnzeigen();
    }

    private void GesamtsummeAnzeigen() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        String sSumme = formatter.format(BuchungszeileAdapter.getGesamtsumme());
        if (BuchungszeileAdapter.getGesamtsumme() > 0) {
            lblZusammenfassung.setTextColor(Color.GREEN);
        }
        if (BuchungszeileAdapter.getGesamtsumme() < 0) {
            lblZusammenfassung.setTextColor(Color.RED);
        } else {
            lblZusammenfassung.setTextColor(getResources().getColor(R.color.buchung_view_headerlabel_textcolor, null));
        }
        lblZusammenfassung.setText(sSumme);
    }

    @Override
    public void onBuchungszeileItemClicked(int pos, FinanzbuchungPosition BuchungszeileEintrag, View shareCardView) {
        if (BuchungszeileEintrag.getId() == 0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Buchung_Kategorie_Dialog dialog = Buchung_Kategorie_Dialog.newInstance("Kategorie hinzufügen", BuchungszeileEintrag, BuchungszeileAdapter.getCount());
            dialog.setCallback(new Buchung_Kategorie_Dialog.Callback() {
                @Override
                public void onActionClick(FinanzbuchungPosition Eintrag) {
                    if (Eintrag != null) {
                        BuchungszeileAdapter.getEintragListe().add(Eintrag);
                        BuchungszeileAdapter.notifyDataSetChanged();
                        lvBuchungHinzufügen.smoothScrollToPosition(0);
                    }
                }
            });
            dialog.show(fragmentManager, "fragment_kategorie_auswahl");
        }
    }

    private boolean Eingaben_Überprüfen() {
        if (txtTitel.getEditText().getText().toString().equals("")) {
            Toasty.error(Buchung.this, "Geben Sie einen Buchungstitel an", Toasty.LENGTH_SHORT, true).show();
            return false;
        }
        if (txtDatum.getEditText().getText().toString().trim().equals("") || !Globale_Funktionen.isGermanDateString(txtDatum.getEditText().getText().toString().trim())) {
            Toasty.error(Buchung.this, "Geben Sie ein Buchungsdatum an", Toasty.LENGTH_SHORT, true).show();
            return false;
        }

        if (cboKonto.getTag() == null) {
            Toasty.error(Buchung.this, "Wählen Sie ein Konto aus", Toasty.LENGTH_SHORT, true).show();
            return false;
        }

        if (lvBuchungHinzufügen.getAdapter().getCount() == 0) {
            Toasty.error(Buchung.this, "Wählen Sie mind. eine Buchungskategorie aus", Toasty.LENGTH_SHORT, true).show();
            return false;
        }

        return true;
    }

    private void Buchen(final int buchungID) {
        //Allgemeine Angaben zur Buchung speichern
        final Konto konto = (Konto) cboKonto.getTag();
        final String beschreibung = txtTitel.getEditText().getText().toString().trim();
        final int kooperationID = (cboKooperation.getTag() == null) ? 0 : ((Kooperation) cboKooperation.getTag()).getIdentifier();
        Date buchungsdatum = Calendar.getInstance().getTime();
        try {
            buchungsdatum = (GlobaleVariablen.getInstance().getDE_DateFormat().parse(txtDatum.getEditText().getText().toString().trim()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (buchungID == 0) {
            FinanzbuchungPosition firstPosition = BuchungszeileAdapter.getItem(0);
            Integer kategorieId = firstPosition.getKategorieId();
            double betrag = firstPosition.getBetrag();

            HashMap<String, String> data = new HashMap<String, String>();

            data.put("BuchungID", String.valueOf(buchungID));
            data.put("Pos", String.valueOf(firstPosition.getId()));
            data.put("BenutzerID", String.valueOf(GlobaleVariablen.getInstance().getUserId()));
            data.put("KatID", String.valueOf(kategorieId));
            data.put("KontoID", String.valueOf(konto.getIdentifier()));
            data.put("Datum", GlobaleVariablen.getInstance().getSQL_DateFormat().format(buchungsdatum));
            data.put("Betrag", String.valueOf(betrag));
            data.put("Beschreibung", beschreibung);
            data.put("KooperationID", String.valueOf(kooperationID));

            PostResponseAsyncTask BuchenTask =
                    new PostResponseAsyncTask(Buchung.this, data, false, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if(Globale_Funktionen.isNumeric(s)) {
                                Buchen(Integer.parseInt(s));
                            } else {
                                Toasty.error(Buchung.this, "Buchen Error: " + s, Toasty.LENGTH_LONG, true).show();
                            }
                        }
                    });
            BuchenTask.execute("http://financemanagermm.sytes.net/fmclient/buchen_kategorien.php");
        } else {
            for(int i = (editMode) ? 0 : 1; i < BuchungszeileAdapter.getCount(); i++) {
                FinanzbuchungPosition position = BuchungszeileAdapter.getItem(i);
                Integer kategorieId = position.getKategorieId();
                double betrag = position.getBetrag();

                HashMap<String, String> data = new HashMap<String, String>();

                data.put("BuchungID", String.valueOf(buchungID));
                data.put("Pos", String.valueOf(position.getId()));
                data.put("BenutzerID", String.valueOf(GlobaleVariablen.getInstance().getUserId()));
                data.put("KatID", String.valueOf(kategorieId));
                data.put("KontoID", String.valueOf(konto.getIdentifier()));
                data.put("Datum", GlobaleVariablen.getInstance().getSQL_DateFormat().format(buchungsdatum));
                data.put("Betrag", String.valueOf(betrag));
                data.put("Beschreibung", beschreibung);
                data.put("KooperationID", String.valueOf(kooperationID));

                PostResponseAsyncTask BuchenTask =
                        new PostResponseAsyncTask(Buchung.this, data, false, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if(!s.equals("success")) {
                                    Toasty.error(Buchung.this, "Buchen Error: " + s, Toasty.LENGTH_LONG, true).show();
                                }
                            }
                        });
                BuchenTask.execute("http://financemanagermm.sytes.net/fmclient/buchen_kategorien.php");
            }

            for (FinanzbuchungToken eintrag : CheckedMerkmalListe) {
                HashMap TokenData = new HashMap();
                TokenData.put("BuchungID", String.valueOf(buchungID));
                TokenData.put("TokenID", String.valueOf(eintrag.getTokenId()));

                PostResponseAsyncTask TokenBuchenTask =
                        new PostResponseAsyncTask(Buchung.this, TokenData, false,
                                new AsyncResponse() {
                                    @Override
                                    public void processFinish(String s) {

                                    }
                                });
                TokenBuchenTask.execute("http://financemanagermm.sytes.net/fmclient/buchen_tokens.php");
            }

            callback.onBuchungCreated(updateBuchung(buchungEintrag, buchungID));
            setResult(RESULT_OK);
            finish();
            Toasty.success(this, "Gebucht", Toast.LENGTH_SHORT, true).show();
        }
    }
    private Finanzbuchung_Buchung updateBuchung (Finanzbuchung_Buchung buchung, int buchungID) {
        //Falls noch kein Finanzbuchungsobjekt erzeugt wurde
        if(buchung==null) {
            buchung = new Finanzbuchung_Buchung(buchungID);
        }

        if(editMode) {
            buchung = Finanzbuchungen.getFinanzbuchungById(buchungID);
        }

        //Allgemeine Angaben zur Buchung speichern
        final Konto konto = (Konto) cboKonto.getTag();
        final String beschreibung = txtTitel.getEditText().getText().toString().trim();
        final int kooperationID = (cboKooperation.getTag() == null) ? 0 : ((Kooperation) cboKooperation.getTag()).getIdentifier();
        LocalDate buchungsdatum = DateConversionHelper.convertToLocalDate(txtDatum.getEditText().getText().toString().trim(), DateConversionHelper.getDE_DateFormat());


        buchung.setBeschreibung(beschreibung);
        buchung.setBetrag(BuchungszeileAdapter.getGesamtsumme());
        ArrayList<FinanzbuchungPosition> positionList = new ArrayList<FinanzbuchungPosition>();
        positionList.addAll(BuchungszeileAdapter.getEintragListe());
        buchung.setBuchungspositionen(positionList);
        buchung.setDatum(buchungsdatum);
        buchung.setKontoId(konto.getIdentifier());
        buchung.setKooperationId(kooperationID);
        ArrayList<FinanzbuchungToken> tokenList = new ArrayList<FinanzbuchungToken>();
        tokenList.addAll(CheckedMerkmalListe);
        buchung.setTokens(tokenList);

        return buchung;
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.Buchung_Toolbar_btnClose:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface Buchung_Created_Interface {
        void onBuchungCreated(Finanzbuchung_Buchung buchung);
    }
    public static void setBuchungCreatedCallback (Buchung_Created_Interface callback) {
        Buchung.callback = callback;
    }
}