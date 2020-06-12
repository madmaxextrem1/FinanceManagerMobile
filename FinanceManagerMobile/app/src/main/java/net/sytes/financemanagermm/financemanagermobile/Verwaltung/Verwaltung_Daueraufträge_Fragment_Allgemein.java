package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.tiper.MaterialSpinner;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Konten;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Verwaltung_Daueraufträge_Fragment_Allgemein extends Fragment {
    private ArrayList<String> RhythmusAuswahl = new ArrayList<String>();
    private TextInputLayout txtBeschreibung;
    private MaterialSpinner cboKonto;
    private MaterialSpinner cboKontoAuf;
    private MaterialSpinner cboKontoVon;
    private TextInputLayout txtBetrag;
    private MaterialSpinner cboRhythmus;
    private SwitchMaterial swtAktiv;
    private Rhythmus_Auswahl_Adapter RhythmusAuswahlAdapter;
    private Konto_Adapter KontoAuswahlAdapter;
    private Dauerauftrag dauerauftrag;
    private Dauerauftrag.Dauerauftrag_Art dauerauftragArt;

    public Verwaltung_Daueraufträge_Fragment_Allgemein(Dauerauftrag dauerauftrag, Dauerauftrag.Dauerauftrag_Art dauerauftragArt) {
        this.dauerauftrag = dauerauftrag;
        this.dauerauftragArt = dauerauftragArt;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ReturnView = inflater.inflate(R.layout.verwaltung_dauerauftraege_fragment_allgemein, container, false);

        txtBeschreibung = ReturnView.findViewById(R.id.txtBeschreibung);
        cboKonto = ReturnView.findViewById(R.id.cboKonto);
        cboKontoVon = ReturnView.findViewById(R.id.cboKontoVon);
        cboKontoAuf = ReturnView.findViewById(R.id.cboKontoAuf);
        txtBetrag = ReturnView.findViewById(R.id.txtBetrag);
        cboRhythmus = ReturnView.findViewById(R.id.cboRhythmus);
        swtAktiv = ReturnView.findViewById(R.id.swtAktiv);

        KontoAuswahlAdapter = new Konto_Adapter(getContext(), Konten.getAktiveKonten());

        cboKonto.setAdapter(KontoAuswahlAdapter);
        KontoAuswahlAdapter.notifyDataSetChanged();
        cboKonto.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
                cboKonto.setTag(KontoAuswahlAdapter.getItem(i));
                Objects.requireNonNull(cboKonto.getEditText()).setText(KontoAuswahlAdapter.getItem(i).getKontoTitel());
            }

            @Override
            public void onNothingSelected(MaterialSpinner materialSpinner) {

            }
        });

        Konto_Adapter kontoVonAdapter = new Konto_Adapter(getContext(), Konten.getAktiveKonten());

        Konto_Adapter kontoAufAdapter = new Konto_Adapter(getContext());
        kontoAufAdapter.getLinkedMap().putAll(Konten.getAktiveKonten());

        cboKontoVon.setAdapter(kontoVonAdapter);
        cboKontoAuf.setAdapter(kontoAufAdapter);

        kontoVonAdapter.notifyDataSetChanged();
        kontoAufAdapter.notifyDataSetChanged();

        cboKontoVon.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
                Konto eintrag = kontoVonAdapter.getItem(i);
                kontoAufAdapter.getLinkedMap().clear();
                kontoAufAdapter.getLinkedMap().putAll(Konten.getAktiveKonten());
                kontoAufAdapter.getLinkedMap().remove(eintrag.getIdentifier());
                kontoAufAdapter.notifyDataSetChanged();

                cboKontoAuf.getEditText().setText("");
                cboKontoAuf.setTag(null);

                cboKontoVon.setTag(eintrag);
                cboKontoVon.getEditText().setText(eintrag.getKontoTitel());
            }

            @Override
            public void onNothingSelected(MaterialSpinner materialSpinner) {

            }
        });
        cboKontoAuf.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
                Konto eintrag = kontoAufAdapter.getItem(i);
                cboKontoAuf.setTag(eintrag);
                cboKontoAuf.getEditText().setText(eintrag.getKontoTitel());
            }

            @Override
            public void onNothingSelected(MaterialSpinner materialSpinner) {

            }
        });

        Objects.requireNonNull(txtBetrag.getEditText()).setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        txtBetrag.getEditText().setKeyListener(DigitsKeyListener.getInstance(true, true));

        RhythmusAuswahl.addAll(Arrays.asList(getResources().getStringArray(R.array.Daueraufträge_Rhythmus_Auswahl)));
        RhythmusAuswahlAdapter = new Rhythmus_Auswahl_Adapter(getContext(), RhythmusAuswahl);
        cboRhythmus.setAdapter(RhythmusAuswahlAdapter);
        RhythmusAuswahlAdapter.notifyDataSetChanged();
        cboRhythmus.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
                cboRhythmus.setTag(RhythmusAuswahlAdapter.getItem(i));
                cboRhythmus.getEditText().setText(RhythmusAuswahlAdapter.getItem(i));
            }

            @Override
            public void onNothingSelected(MaterialSpinner materialSpinner) {

            }
        });

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

        if (dauerauftrag != null) {
            Objects.requireNonNull(txtBeschreibung.getEditText()).setText(dauerauftrag.getBeschreibung());

            txtBetrag.getEditText().setText(dauerauftrag.getBetrag().toString());

            cboRhythmus.setSelection(RhythmusAuswahlAdapter.getEintragListe().indexOf(dauerauftrag.getRhythmus()));

            if (dauerauftrag.isAktiv()) {
                swtAktiv.setChecked(true);
            } else {
                swtAktiv.setChecked(false);
            }

            if (dauerauftragArt == Dauerauftrag.Dauerauftrag_Art.NORMAL) {
                cboKontoAuf.setVisibility(View.GONE);
                cboKontoVon.setVisibility(View.GONE);
                cboKonto.setSelection(KontoAuswahlAdapter.getItemPositionById(dauerauftrag.getId()));
            } else {
                cboKonto.setVisibility(View.GONE);
                cboKontoVon.setSelection(kontoVonAdapter.getItemPositionById(((Dauerauftrag_Umbuchung) dauerauftrag).getKontoVon().getIdentifier()));
                cboKontoAuf.setSelection(kontoAufAdapter.getItemPositionById(((Dauerauftrag_Umbuchung) dauerauftrag).getKontoAuf().getIdentifier()));
            }
        } else {
            if (dauerauftragArt == Dauerauftrag.Dauerauftrag_Art.NORMAL) {
                cboKontoAuf.setVisibility(View.GONE);
                cboKontoVon.setVisibility(View.GONE);
            } else {
                cboKonto.setVisibility(View.GONE);
            }
            swtAktiv.setChecked(true);
        }

        return ReturnView;
    }

    public String getBeschreibung() {
        return txtBeschreibung.getEditText().getText().toString().trim();
    }
    public String getRhythmus() {
        return cboRhythmus.getTag().toString().trim();
    }
    public String getBetrag() {
        return txtBetrag.getEditText().getText().toString().trim();
    }
    public Konto getKonto() {
        return (Konto) cboKonto.getTag();
    }
    public Konto getKontoVon() {
        return (Konto) cboKontoVon.getTag();
    }
    public Konto getKontoAuf() {
        return (Konto) cboKontoAuf.getTag();
    }

    public boolean isAktiv() {
        return swtAktiv.isChecked();
    }
}