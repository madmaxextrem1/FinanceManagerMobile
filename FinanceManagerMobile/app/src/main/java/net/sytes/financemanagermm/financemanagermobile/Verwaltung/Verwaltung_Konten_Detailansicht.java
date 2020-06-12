package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.tiper.MaterialSpinner;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.DecimalDigitsInputFilter;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Kooperationen;
import net.sytes.financemanagermm.financemanagermobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Verwaltung_Konten_Detailansicht extends DialogFragment implements View.OnClickListener {
    private CardView KontoCardView;
    private ImageView KontoArtImage;
    private ImageView KontoEntwicklungImage;
    private TextView KontoTitel;
    private TextView KontoBestand;
    private TextView KontoEntwicklung;
    private ArrayList<Konto> KontenListe = new ArrayList<Konto>();
    private TextInputLayout txtKontoname;
    private MaterialSpinner cboKontoArt;
    private TextInputLayout txtKooperation;
    private TextInputLayout txtAnfangsbestand;
    private TextInputLayout txtDatumAnfangsbestand;
    private AppCompatImageButton btnClose;
    private TextView DialogTitle;
    private MaterialButton btnSpeichern;
    private Verwaltung_Konten_Kontoart_Auswahl_Adapter adapterKontoArt;
    private FinanceManagerData_Edited_Interface callback;
    private SwitchMaterial swtAktiv;
    private Konto konto;
    private Context context;
    private boolean update;

    public Verwaltung_Konten_Detailansicht(Context context, Konto konto, FinanceManagerData_Edited_Interface<Konto> callback, boolean update) {
        this.konto = konto;
        this.callback = callback;
        this.context = context;
        this.update = update;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verwaltung_konten_detailansicht, container, false);

        KontoArtImage = view.findViewById(R.id.KontoArtImage);
        KontoEntwicklungImage = view.findViewById(R.id.KontoEntwicklungImage);
        KontoTitel = view.findViewById(R.id.KontoTitel);
        KontoBestand = view.findViewById(R.id.KontoBestand);
        KontoEntwicklung = view.findViewById(R.id.KontoEntwicklung);
        txtKontoname = view.findViewById(R.id.Konto_Titel);
        txtKooperation = view.findViewById(R.id.Kooperation);

        txtAnfangsbestand = view.findViewById(R.id.Kontendetails_txtAnfangsbestand);
        txtAnfangsbestand.getEditText().setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        txtAnfangsbestand.getEditText().setKeyListener(DigitsKeyListener.getInstance(false, true));

        txtDatumAnfangsbestand = view.findViewById(R.id.Kontendetails_txtDatumAnfangsbestand);
        btnSpeichern = view.findViewById(R.id.Kontendetails_btn_Speichern);
        KontoCardView = view.findViewById(R.id.Kontendetails_CardView);
        btnClose = view.findViewById(R.id.Kontendetails_btnDialogClose);
        btnClose.setOnClickListener(this);
        DialogTitle = view.findViewById(R.id.Kontendetails_lblDialogTitle);
        swtAktiv = view.findViewById(R.id.Kontendetails_swtAktiv);

        ArrayList<Konto.KontoArt> kontoArt_Liste =
                new ArrayList<Konto.KontoArt>(EnumSet.allOf(Konto.KontoArt.class));
        adapterKontoArt = new Verwaltung_Konten_Kontoart_Auswahl_Adapter(getContext(), kontoArt_Liste);
        cboKontoArt = view.findViewById(R.id.Kontendetails_cboKontoArt);
        cboKontoArt.setAdapter(adapterKontoArt);
        adapterKontoArt.notifyDataSetChanged();

        swtAktiv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    swtAktiv.setThumbTintList(ColorStateList.valueOf(Objects.requireNonNull(getContext()).getColor(R.color.primary)));
                } else {
                    swtAktiv.setThumbTintList(ColorStateList.valueOf(Objects.requireNonNull(getContext()).getColor(R.color.white)));
                }
            }
        });

        if (!update) {
            KontoCardView.setVisibility(View.GONE);
            DialogTitle.setText("Konto hinzufügen");
            swtAktiv.setChecked(true);
            txtKooperation.setVisibility(View.GONE);
        } else {
            if (konto.getKooperationID() == 0) {
                txtKooperation.setVisibility(View.GONE);
            } else {
                txtKooperation.getEditText().setText(Kooperationen.getKoopertionById(konto.getKooperationID()).getBeschreibung());
            }
            DialogTitle.setText(konto.getKontoTitel());
            if (TopCardView_Füllen()) {
                if (konto.getIdentifier() == 0) {
                    cboKontoArt.setSelection(-1);
                } else {
                    cboKontoArt.setSelection(adapterKontoArt.getEintragListe().indexOf(konto.getKontoArt()));
                }
            }
        }

        txtDatumAnfangsbestand.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText(R.string.DatePickerDialogTitle_Zeitpunkt);
                if (!txtDatumAnfangsbestand.getEditText().getText().toString().equals("")) {
                    try {
                        builder.setSelection(GlobaleVariablen.getInstance().getDE_DateFormat().parse(txtDatumAnfangsbestand.getEditText().getText().toString()).getTime());
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
                        txtDatumAnfangsbestand.getEditText().setText(GlobaleVariablen.getInstance().getDE_DateFormat().format(Datum));
                    }
                });
                picker.show(getChildFragmentManager(), "Test");
            }
        });
        btnSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Eingaben_Überprüfen()) {
                    if (konto == null) {
                        try {
                            konto = new Konto(0,
                                    txtKontoname.getEditText().getText().toString(),
                                    Konto.KontoArt.getKontoArtByName(cboKontoArt.getEditText().getText().toString()),
                                    Double.parseDouble(txtAnfangsbestand.getEditText().getText().toString()),
                                    Double.parseDouble(txtAnfangsbestand.getEditText().getText().toString()),
                                    GlobaleVariablen.getInstance().getDE_DateFormat().parse(txtDatumAnfangsbestand.getEditText().getText().toString()),
                                    true,
                                    0,
                                    0.0,
                                    0
                                    );
                            callback.onDataEdited(konto, true);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        konto.setKontoTitel(txtKontoname.getEditText().getText().toString());
                        konto.setKontoArt(Konto.KontoArt.getKontoArtByName(cboKontoArt.getEditText().getText().toString()));
                        konto.setAnfangsbestand(Double.parseDouble(txtAnfangsbestand.getEditText().getText().toString()));
                        try {
                            konto.setDatumAnfangsbestand(GlobaleVariablen.getInstance().getDE_DateFormat().parse(txtDatumAnfangsbestand.getEditText().getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        callback.onDataEdited(konto, false);
                    }
                    dismiss();
                } else {
                    Toasty.error(getContext(), "Bitte füllen Sie alle Felder aus.", Toast.LENGTH_SHORT, true).show();
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

    public void setCallback(FinanceManagerData_Edited_Interface callback) {
        this.callback = callback;
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

    private boolean TopCardView_Füllen() {
        Boolean Ergebnis = true;
        KontenListe.addAll(GlobaleVariablen.getInstance().getKontenListe());
        KontoArtImage.setImageDrawable(konto.getKontoArt().getKonto_Image());

        if (konto.getSummeBewegungenAktMonat() > 0) {
            KontoEntwicklungImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_upward_white_24dp, null));
        }
        if (konto.getSummeBewegungenAktMonat() < 0) {
            KontoEntwicklungImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_downward_white_24dp, null));
        }
        if (konto.getSummeBewegungenAktMonat() == 0) {
            KontoEntwicklungImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_forward_white_24dp, null));
        }

        txtKontoname.getEditText().setText(konto.getKontoTitel());
        String Datum = GlobaleVariablen.getInstance().getDE_DateFormat().format(konto.getDatumAnfangsbestand());
        txtDatumAnfangsbestand.getEditText().setText(Datum);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        String sBestand = formatter.format(konto.getBestand());
        String sSummeBewegungen = formatter.format(konto.getSummeBewegungenAktMonat());
        KontoBestand.setText("Bestand: " + sBestand);
        txtAnfangsbestand.getEditText().setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});
        ;

        txtAnfangsbestand.getEditText().setText(konto.getAnfangsbestand().toString());
        KontoEntwicklung.setText("Anzahl Bewegungen: " +
                konto.getAnzahlBewegungenAktMonat() + " (" +
                sSummeBewegungen + ")");

        if (konto.getAktiv()) {
            swtAktiv.setChecked(true);
        } else {
            swtAktiv.setChecked(false);
        }


        return Ergebnis;
    }

    private boolean Eingaben_Überprüfen() {
        Boolean Ergebnis = true;
        if (txtKontoname.getEditText().getText().toString().equals("")) {
            Ergebnis = false;
        }
        if (txtDatumAnfangsbestand.getEditText().getText().toString().equals("")) {
            Ergebnis = false;
        }
        if (txtAnfangsbestand.getEditText().getText().toString().equals("")) {
            Ergebnis = false;
        }
        if(cboKontoArt.getSelectedItem() == null) {
            Ergebnis = false;
        }
        return Ergebnis;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
