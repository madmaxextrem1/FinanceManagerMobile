package net.sytes.financemanagermm.financemanagermobile.Buchungen;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.FinanzbuchungPosition;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.BuchungskategorienCallback;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.DecimalDigitsInputFilter;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Verwaltung_Kategorien_Übersicht;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.DialogFragment;

import es.dmoral.toasty.Toasty;

import static java.util.Currency.getInstance;

public class Buchung_Kategorie_Dialog extends DialogFragment implements View.OnClickListener {
    public Context c;
    public Dialog d;
    private ExpandableListView lv_Kategorien;
    private Buchung_Kategorie_Dialog_lvKategorien_Adapter adapter;
    private TextInputLayout txtBetrag;
    private Button btnHinzufügen;
    private RadioButton rdbEinnahme;
    private int lastExpandedPosition = -1;
    private Buchung_Kategorie_Dialog.Callback callback;
    private TextView DialogTitle;
    private AppCompatImageButton btnClose;
    private AppCompatImageButton btnEditKategorien;

    static Buchung_Kategorie_Dialog newInstance(String title, FinanzbuchungPosition eintrag, Integer anzahlVorhandenerBuchungszeilen) {
        Buchung_Kategorie_Dialog frag = new Buchung_Kategorie_Dialog();
        Bundle args = new Bundle();
        args.putInt("AnzahlBuchungszeilen", anzahlVorhandenerBuchungszeilen);
        args.putString("title", title);
        args.putParcelable("eintrag",eintrag);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogFragment);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buchung_kategorie_dialog, container,false);

        DialogTitle = view.findViewById(R.id.Kontendetails_lblDialogTitle);
        DialogTitle.setText(R.string.Kategorie_Auswahl_Dialog_Title);
        lv_Kategorien = view.findViewById(R.id.Kategorie_Dialog_lv_Kategorien);
        txtBetrag = view.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_txtBetrag);
        txtBetrag.getEditText().setFilters(new InputFilter[] {new DecimalDigitsInputFilter(10,2)});;
        txtBetrag.getEditText().setTextColor(
                getActivity().getResources().getColor(R.color.fm_Login_Background_Color,null));
        btnHinzufügen = view.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_btn_Hinzufügen);
        rdbEinnahme = view.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_rdbEinnahme);
        btnClose = view.findViewById(R.id.Kontendetails_btnDialogClose);
        btnEditKategorien = view.findViewById(R.id.btnEditKategorien);
        btnEditKategorien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Verwaltung_Kategorien_Übersicht.class);
                startActivity(intent);
                Verwaltung_Kategorien_Übersicht.setUpdateCallback(new Buchungskategorie_Update_Interface() {
                    @Override
                    public void onBuchungskategorieChanged(Buchungskategorie Kategorie) {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        btnClose.setOnClickListener(this);

        btnHinzufügen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int SelectedKategorieListID = lv_Kategorien.getSelectedItemPosition();
                int id = getArguments().getInt("AnzahlBuchungszeilen") + 1;
                Buchungskategorie kategorie = adapter.getChild(adapter.getSelectedGroupIndex(), adapter.getSelectedPosition());
                int KategorieID = kategorie.getId();

                if(txtBetrag.getEditText().getText().toString().trim().equals("") || !Globale_Funktionen.isNumeric(txtBetrag.getEditText().getText().toString().trim())) {
                    Toasty.error(getContext(),"Geben Sie einen Betrag größer 0 an", Toast.LENGTH_SHORT,true).show();
                    return;
                }

                double Betrag =   Double.valueOf(txtBetrag.getEditText().getText().toString());

                if(!rdbEinnahme.isChecked()) {
                   Betrag = Betrag * -1;
                }

                String Kategorie = kategorie.getBeschreibung();
                String Überkategorie = adapter.getHauptkategorieMap().get(kategorie.getÜKatId()).getBeschreibung();
                int Rot = kategorie.getRot();
                int Grün = kategorie.getGrün();
                int Blau = kategorie.getBlau();

                FinanzbuchungPosition NeuerEintrag = new FinanzbuchungPosition(id,KategorieID,Kategorie, Überkategorie,Betrag,Rot,Grün,Blau);

                callback.onActionClick(NeuerEintrag);
                dismiss();
            }
        });

        adapter = new Buchung_Kategorie_Dialog_lvKategorien_Adapter(c);
        lv_Kategorien.setAdapter(adapter);
        lv_Kategorien.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    lv_Kategorien.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
                lv_Kategorien.setSelectionFromTop(groupPosition,0);
            }
        });

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return view;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.Kontendetails_btnDialogClose:
                dismiss();
                break; }
    }
    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtBetrag.getEditText().removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
                    formatter.setCurrency(Currency.getInstance(Locale.getDefault()));
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    txtBetrag.getEditText().setText(formattedString);
                    txtBetrag.getEditText().setSelection(txtBetrag.getEditText().getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                txtBetrag.getEditText().addTextChangedListener(this);
            }
        };
    }
    public void setCallback(Buchung_Kategorie_Dialog.Callback callback) {
        this.callback = callback;
    }
    public interface Callback {
        void onActionClick(FinanzbuchungPosition Eintrag);
    }
}