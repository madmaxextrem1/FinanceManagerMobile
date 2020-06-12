package net.sytes.financemanagermm.financemanagermobile.Buchungen;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.tiper.MaterialSpinner;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Konten;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto_Adapter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Umbuchung extends AppCompatActivity {
    private MaterialSpinner cboKontoVon;
    private MaterialSpinner cboKontoNach;
    private Konto_Adapter kontoVon_auswahl_adapter;
    private Konto_Adapter kontoNach_auswahl_adapter;
    private TextInputLayout txtDatum;
    private TextInputLayout txtBetrag;
    private TextInputLayout txtTitel;
    private TextView lblErgebnis;
    private AppCompatImageButton btnBuchen;
    private AppCompatImageButton btnClose;
    private ListView lvNeueKontostände;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.umbuchung_activity);

        cboKontoVon = findViewById(R.id.cboKonto_Von);
        cboKontoNach = findViewById(R.id.cboKonto_Nach);
        btnBuchen = findViewById(R.id.Buchung_btn_Buchen);
        txtBetrag = findViewById(R.id.txtBetrag);
        txtDatum = findViewById(R.id.txtDatum);
        lvNeueKontostände = findViewById(R.id.lvKontostände);
        lblErgebnis = findViewById(R.id.lblErgebnis);
        lblErgebnis.setVisibility(View.INVISIBLE);
        btnClose = findViewById(R.id.Buchung_Toolbar_btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Objects.requireNonNull(getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        kontoVon_auswahl_adapter = new Konto_Adapter(this);
        kontoVon_auswahl_adapter.getLinkedMap().putAll(Konten.getAktiveKonten());
        kontoNach_auswahl_adapter = new Konto_Adapter(this, Konten.getAktiveKonten());
        kontoNach_auswahl_adapter.getLinkedMap().putAll(Konten.getAktiveKonten());

        cboKontoVon.setAdapter(kontoVon_auswahl_adapter);
        cboKontoNach.setAdapter(kontoNach_auswahl_adapter);

        kontoVon_auswahl_adapter.notifyDataSetChanged();
        kontoNach_auswahl_adapter.notifyDataSetChanged();

        cboKontoVon.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner materialSpinner) {

            }

            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, View view, int position, long l) {
                Konto eintrag = kontoVon_auswahl_adapter.getItem(position);
                kontoNach_auswahl_adapter.getLinkedMap().clear();
                kontoNach_auswahl_adapter.getLinkedMap().putAll(Konten.getAktiveKonten());
                kontoNach_auswahl_adapter.getLinkedMap().remove(eintrag.getIdentifier());
                kontoNach_auswahl_adapter.notifyDataSetChanged();

                cboKontoNach.getEditText().setText("");
                cboKontoNach.setTag(null);

                cboKontoVon.setTag(eintrag);
                cboKontoVon.getEditText().setText(eintrag.getKontoTitel());

                Ergebnis_Anzeigen();
            }
        });
        cboKontoNach.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner materialSpinner) {

            }

            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, View view, int position, long l) {
                Konto eintrag = kontoNach_auswahl_adapter.getItem(position);
                cboKontoNach.setTag(eintrag);
                cboKontoNach.getEditText().setText(eintrag.getKontoTitel());

                Ergebnis_Anzeigen();
            }
        });

        Objects.requireNonNull(txtDatum.getEditText()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText(R.string.DatePickerDialogTitle_Zeitpunkt);
                if (!Objects.requireNonNull(txtDatum.getEditText()).getText().toString().equals("")) {
                    try {
                        builder.setSelection(GlobaleVariablen.getInstance().getDE_DateFormat().parse(txtDatum.getEditText().getText().toString()).getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.setSelection(Calendar.getInstance().getTime().getTime());
                }

                MaterialDatePicker<?> picker = builder.build();

                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Object>() {
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

        Objects.requireNonNull(txtBetrag.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.equals("")) {
                    Ergebnis_Anzeigen();
                }
            }
        });
        txtBetrag.getEditText().setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        txtBetrag.getEditText().setKeyListener(DigitsKeyListener.getInstance(false, true));

        txtTitel = findViewById(R.id.txtTitel);
        btnBuchen = findViewById(R.id.btn_Buchen);
        btnBuchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Eingaben_Überprüfen()) {
                    Toasty.error(Umbuchung.this, "Überprüfen Sie ihre Eingaben.", Toast.LENGTH_SHORT, true).show();
                    return;
                }

                Integer KontoIDVon = ((Konto) cboKontoVon.getTag()).getIdentifier();
                Integer KontoIDNach = ((Konto) cboKontoNach.getTag()).getIdentifier();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(GlobaleVariablen.getInstance().getDE_DateFormatPattern());
                LocalDate date = LocalDate.parse(Objects.requireNonNull(txtDatum.getEditText().getText()).toString(), formatter);
                String Buchungsdatum = date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
                String Betrag = Objects.requireNonNull(txtBetrag.getEditText().getText()).toString();
                String Beschreibung = Objects.requireNonNull(Objects.requireNonNull(txtTitel.getEditText()).getText()).toString();

                GlobaleVariablen globaleVariablen = GlobaleVariablen.getInstance();
                HashMap BuchenData = new HashMap();

                BuchenData.put("BenutzerID", String.valueOf(globaleVariablen.getUserId()));
                BuchenData.put("KontoID1", String.valueOf(KontoIDVon));
                BuchenData.put("KontoID2", String.valueOf(KontoIDNach));
                BuchenData.put("Datum", Buchungsdatum);
                BuchenData.put("Betrag", Betrag);
                BuchenData.put("Beschreibung", Beschreibung);

                PostResponseAsyncTask BuchenTask =
                        new PostResponseAsyncTask(Umbuchung.this, BuchenData, false,
                                new AsyncResponse() {
                                    @Override
                                    public void processFinish(String s) {
                                        if (s.equals("success")) {
                                            finish();
                                            Toasty.success(Umbuchung.this, "Gebucht", Toast.LENGTH_SHORT, true).show();
                                        } else {
                                            Toasty.error(Umbuchung.this, s, Toast.LENGTH_SHORT, true).show();
                                        }
                                    }
                                });
                BuchenTask.execute("http://financemanagermm.sytes.net/fmclient/buchen_umbuchungen.php");
            }
        });
    }

    private boolean Eingaben_Überprüfen() {
        boolean Ergebnis = true;
        if (Objects.requireNonNull(txtTitel.getEditText().getText()).toString().equals("")) {
            Ergebnis = false;
        }
        if (Objects.requireNonNull(txtDatum.getEditText().getText()).toString().equals("")) {
            Ergebnis = false;
        }
        if (cboKontoVon.getTag() == null) {
            Ergebnis = false;
        }
        if (cboKontoNach.getTag() == null) {
            Ergebnis = false;
        }
        if (Objects.requireNonNull(txtBetrag.getEditText().getText()).toString().equals("")) {
            Ergebnis = false;
        } else {
            if (Double.valueOf(txtBetrag.getEditText().getText().toString()) <= 0) {
                Toasty.error(Umbuchung.this, "Betrag muss größer 0 sein.", Toast.LENGTH_SHORT, true).show();
                Ergebnis = false;
            }
        }


        return Ergebnis;
    }

    private void Ergebnis_Anzeigen() {

        if (cboKontoVon.getTag() != null && cboKontoNach.getTag() != null && !Objects.requireNonNull(txtBetrag.getEditText().getText()).toString().equals("")) {
            lvNeueKontostände.setVisibility(View.VISIBLE);
            lblErgebnis.setVisibility(View.VISIBLE);
            Umbuchung_NeueKontostände_Adapter kontostände_adapter = new Umbuchung_NeueKontostände_Adapter(Umbuchung.this, Float.parseFloat(Objects.requireNonNull(txtBetrag.getEditText().getText()).toString()));

            Konto kontoVonEintrag = (Konto) cboKontoVon.getTag();
            kontostände_adapter.getEintragListe().add(new Konto(kontoVonEintrag.getIdentifier(), kontoVonEintrag.getKontoTitel(), kontoVonEintrag.getKontoArt(), kontoVonEintrag.getBestand(), kontoVonEintrag.getAnfangsbestand(), kontoVonEintrag.getDatumAnfangsbestand(), kontoVonEintrag.getAktiv(), kontoVonEintrag.getAnzahlBewegungenAktMonat(), kontoVonEintrag.getSummeBewegungenAktMonat()));

            Konto kontoNachEintrag = (Konto) cboKontoNach.getTag();
            kontostände_adapter.getEintragListe().add(new Konto(kontoNachEintrag.getIdentifier(), kontoNachEintrag.getKontoTitel(), kontoNachEintrag.getKontoArt(), kontoNachEintrag.getBestand(), kontoNachEintrag.getAnfangsbestand(), kontoNachEintrag.getDatumAnfangsbestand(), kontoNachEintrag.getAktiv(), kontoNachEintrag.getAnzahlBewegungenAktMonat(), kontoNachEintrag.getSummeBewegungenAktMonat()));

            lvNeueKontostände.setAdapter(kontostände_adapter);
            kontostände_adapter.notifyDataSetChanged();

        } else {
            lvNeueKontostände.setVisibility(View.INVISIBLE);
            lblErgebnis.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
