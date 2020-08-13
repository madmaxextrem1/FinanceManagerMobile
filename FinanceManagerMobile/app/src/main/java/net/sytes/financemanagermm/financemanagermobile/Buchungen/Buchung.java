package net.sytes.financemanagermm.financemanagermobile.Buchungen;

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

import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.DataManagement;
import net.sytes.financemanagermm.financemanagermobile.ServerCommunication.ServerCommunication;
import net.sytes.financemanagermm.financemanagermobile.ServerCommunication.ServerCommunicationInterface;
import net.sytes.financemanagermm.financemanagermobile.Sign_In_Up.FinanceManagerMobileApplication;
import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.FinanzbuchungPosition;
import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.Finanzbuchung_Buchung;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation_Adapter;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Finanzbuchungen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.Helper.DateConversionHelper;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.CustomAlertDialog;
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
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import es.dmoral.toasty.Toasty;

public class Buchung extends AppCompatActivity implements View.OnClickListener, Observer, Buchungszeile_Auswahl_Eintrag_ItemClickListener {
    private TextView lblActivityTitle;
    private TextInputLayout txtTitel;
    private TextInputLayout txtDatum;
    private ChipGroup txtMerkmale;
    private MaterialSpinner cboKooperation;
    private Kooperation_Adapter kooperationAdapter;
    private MaterialSpinner cboKonto;
    private Konto_Adapter accountsAdapter;

    private FloatingActionButton addCategorieButton;
    private ListView financialEntriesListView;
    private Buchungszeile_Auswahl_SwipeAdapter financialEntriesListAdapter;
    private ArrayList<FinanzbuchungToken> CheckedMerkmalListe = new ArrayList<FinanzbuchungToken>();

    private TextView lblZusammenfassung;
    private SwipeLayout swipeLayout;
    private AppCompatImageButton btnClose;
    private AppCompatImageButton btnBuchen;

    private Boolean editMode = false;
    private Finanzbuchung_Buchung buchungEintrag;
    private ServerCommunication serverCommunication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buchung_activity);
        FinanceManagerMobileApplication.getInstance().getDataManagement().registerView(this);
        serverCommunication = new ServerCommunication(this);

        txtDatum = findViewById(R.id.Buchung_txtDatum);
        txtTitel = findViewById(R.id.Buchung_txtTitel);
        btnBuchen = findViewById(R.id.Buchung_btn_Buchen);
        btnClose = findViewById(R.id.Buchung_Toolbar_btnClose);
        btnClose.setOnClickListener(this);
        financialEntriesListView = (ListView) findViewById(R.id.Buchung_lvBuchungszeilen);
        cboKonto = (MaterialSpinner) findViewById(R.id.Buchung_cboKonto);
        cboKooperation = findViewById(R.id.cboKooperation);
        addCategorieButton = (FloatingActionButton) findViewById(R.id.fab_kategorie_hinzufügen);
        lblZusammenfassung = (TextView) findViewById(R.id.Buchung_SummeZusammenfassung);
        lblActivityTitle = (TextView) findViewById(R.id.Buchung_Toolbar_Title);

        //falls der EditMode aktiviert ist, werden die alten Daten geladen
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            editMode = intent.getExtras().getBoolean("EditMode");
            buchungEintrag = (Finanzbuchung_Buchung) intent.getExtras().getParcelable("BuchungEintrag");
        }

        btnBuchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInputData()) {
                    return;
                }

                buchungEintrag = assembleFinancialEntry();

                if (editMode) {
                    CustomAlertDialog dialog = new CustomAlertDialog(Buchung.this,
                            "Buchung überschreiben?",
                            "Wollen Sie die bestehende Buchung tatsächlich überschreiben?",
                            "Überschreiben",
                            "Abbrechen"
                            );
                    dialog.setOkButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            postFinancialEntry(buchungEintrag);
                        }
                    });

                    dialog.show();
                } else {
                    postFinancialEntry(buchungEintrag);
                }
            }
        });

        btnBuchen.requestFocus();

        addCategorieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Buchung_Kategorie_Dialog dialog = Buchung_Kategorie_Dialog.newInstance("Kategorie hinzufügen", null, financialEntriesListAdapter.getCount());
                dialog.setCallback(new Buchung_Kategorie_Dialog.Callback() {
                    @Override
                    public void onActionClick(FinanzbuchungPosition Eintrag) {
                        if (Eintrag != null) {
                            financialEntriesListAdapter.getEintragListe().add(Eintrag);
                            financialEntriesListAdapter.notifyDataSetChanged();
                            financialEntriesListView.smoothScrollToPosition(0);
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
                materialSpinner.getEditText().setText(accountsAdapter.getItem(i).getKontoTitel());
                materialSpinner.setTag(accountsAdapter.getItem(i));
            }
        });

        financialEntriesListAdapter = new Buchungszeile_Auswahl_SwipeAdapter(this, financialEntriesListView, Buchung.this::onBuchungszeileItemClicked);
        financialEntriesListView.setAdapter(financialEntriesListAdapter);

        financialEntriesListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                GesamtsummeAnzeigen();
            }
        });

        txtMerkmale = (ChipGroup) findViewById(R.id.Buchung_chgMerkmale);
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

        //Update the View Content
        update(FinanceManagerMobileApplication.getInstance().getDataManagement(), null);

        if (editMode) {
            Objects.requireNonNull(txtTitel.getEditText()).setText(buchungEintrag.getBeschreibung());
            LocalDate date = buchungEintrag.getDatum();

            lblActivityTitle.setText("Buchung bearbeiten");
            txtDatum.getEditText().setText(date.format(DateConversionHelper.getDEDateFormatter()));
            cboKonto.setSelection(accountsAdapter.getItemPositionById(buchungEintrag.getKontoId()));
            financialEntriesListAdapter.getEintragListe().addAll(buchungEintrag.getBuchungspositionen());
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
            financialEntriesListAdapter.notifyDataSetChanged();
            cboKooperation.setSelection((buchungEintrag.getKooperationId() == 0) ? -1 : kooperationAdapter.getItemPositionById(buchungEintrag.getKooperationId()));
        }
        GesamtsummeAnzeigen();
    }

    private void GesamtsummeAnzeigen() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        String sSumme = formatter.format(financialEntriesListAdapter.getGesamtsumme());
        if (financialEntriesListAdapter.getGesamtsumme() > 0) {
            lblZusammenfassung.setTextColor(Color.GREEN);
        }
        if (financialEntriesListAdapter.getGesamtsumme() < 0) {
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
            Buchung_Kategorie_Dialog dialog = Buchung_Kategorie_Dialog.newInstance("Kategorie hinzufügen", BuchungszeileEintrag, financialEntriesListAdapter.getCount());
            dialog.setCallback(new Buchung_Kategorie_Dialog.Callback() {
                @Override
                public void onActionClick(FinanzbuchungPosition Eintrag) {
                    if (Eintrag != null) {
                        financialEntriesListAdapter.getEintragListe().add(Eintrag);
                        financialEntriesListAdapter.notifyDataSetChanged();
                        financialEntriesListView.smoothScrollToPosition(0);
                    }
                }
            });
            dialog.show(fragmentManager, "fragment_kategorie_auswahl");
        }
    }

    private boolean checkInputData() {
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

        if (financialEntriesListView.getAdapter().getCount() == 0) {
            Toasty.error(Buchung.this, "Wählen Sie mind. eine Buchungskategorie aus", Toasty.LENGTH_SHORT, true).show();
            return false;
        }

        return true;
    }

    private Finanzbuchung_Buchung assembleFinancialEntry () {
        //Falls noch kein Finanzbuchungsobjekt erzeugt wurde
        if(!editMode) {
            buchungEintrag = new Finanzbuchung_Buchung(0);
        }

        //Allgemeine Angaben zur Buchung speichern
        final Konto konto = (Konto) cboKonto.getTag();
        final String beschreibung = txtTitel.getEditText().getText().toString().trim();
        final int kooperationID = (cboKooperation.getTag() == null) ? 0 : ((Kooperation) cboKooperation.getTag()).getIdentifier();
        LocalDate buchungsdatum = DateConversionHelper.convertToLocalDate(txtDatum.getEditText().getText().toString().trim(), DateConversionHelper.getDE_DateFormat());

        buchungEintrag.setBeschreibung(beschreibung);
        buchungEintrag.setBetrag(financialEntriesListAdapter.getGesamtsumme());
        ArrayList<FinanzbuchungPosition> positionList = new ArrayList<FinanzbuchungPosition>();
        positionList.addAll(financialEntriesListAdapter.getEintragListe());
        buchungEintrag.setBuchungspositionen(positionList);
        buchungEintrag.setDatum(buchungsdatum);
        buchungEintrag.setKontoId(konto.getIdentifier());
        buchungEintrag.setKooperationId(kooperationID);
        ArrayList<FinanzbuchungToken> tokenList = new ArrayList<FinanzbuchungToken>();
        tokenList.addAll(CheckedMerkmalListe);
        buchungEintrag.setTokens(tokenList);

        return buchungEintrag;
    }

    private void postFinancialEntry(Finanzbuchung_Buchung entry) {
        serverCommunication.postFinancialEntry(entry, editMode, new ServerCommunicationInterface.GeneralCommunicationCallback<Finanzbuchung_Buchung>() {
            @Override
            public void onRequestCompleted(Finanzbuchung_Buchung data) {
                FinanceManagerMobileApplication.getInstance().getDataManagement().addFinancialEntry(data);
                Toasty.success(Buchung.this, "Gebucht", Toast.LENGTH_SHORT, true).show();
                finish();
            }
        });
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

    @Override
    public void update(Observable o, Object arg) {
        if(o != null) {
            DataManagement dataManagement = (DataManagement) o;

            kooperationAdapter = new Kooperation_Adapter(this);
            kooperationAdapter.getLinkedMap().put(0, new Kooperation());
            kooperationAdapter.getLinkedMap().putAll(dataManagement.getCooperations());
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

            for (FinanzbuchungToken eintrag : dataManagement.getTokens().values()) {
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

            accountsAdapter = new Konto_Adapter(this, dataManagement.getActiveAccounts());
            cboKonto.setAdapter(accountsAdapter);
            accountsAdapter.notifyDataSetChanged();
        }
    }
}