package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputLayout;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.Calendar;
import java.util.LinkedHashMap;

public class Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Benutzer extends Fragment {
    private Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Benutzer_Adapter lvBenutzerAdapter;
    private FloatingActionButton btnBenutzerHinzufügen;
    private ListView lvBenutzer;
    private RadioGroup rdbGrpAusgabenVerteilung;
    private MaterialRadioButton rdbGleichverteilung;
    private MaterialRadioButton rdbIndividuelleVerteilung;
    private Gemeinsame_Finanzen_Anfrage_BearbeitenDialog parent;
    private KooperationAnfrage anfrage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ReturnView =  inflater.inflate(R.layout.gemeinsame_finanzen_anfrage_neueanfrage_dialog_fragment_benutzer, container, false);
        parent = (Gemeinsame_Finanzen_Anfrage_BearbeitenDialog) getParentFragment();
        anfrage = parent.getAnfrage();

        lvBenutzer = ReturnView.findViewById(R.id.lvBenutzer);
        TextInputLayout txtBeschreibungLayout = (TextInputLayout) ReturnView.findViewById(R.id.txtAnfrage_Beschreibung);
        rdbGrpAusgabenVerteilung = (RadioGroup) ReturnView.findViewById(R.id.rdbGrpAusgabenverteilung);
        rdbGleichverteilung = ReturnView.findViewById(R.id.rdbGleichverteilung);
        rdbIndividuelleVerteilung = ReturnView.findViewById(R.id.rdbIndividuell);
        btnBenutzerHinzufügen = ReturnView.findViewById(R.id.btnBenutzer_Hinzufügen);

        rdbGleichverteilung.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lvBenutzerAdapter.setDistributeEven(true);
            }
        });
        rdbIndividuelleVerteilung.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lvBenutzerAdapter.setDistributeEven(false);
            }
        });
        btnBenutzerHinzufügen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gemeinsame_Finanzen_Anfrage_NeueAnfrage_InviteUserDialog dialog = new Gemeinsame_Finanzen_Anfrage_NeueAnfrage_InviteUserDialog(getContext(), 0, rdbGleichverteilung.isChecked(), lvBenutzerAdapter.getCount(), new Gemeinsame_Finanzen_Anfrage_BenutzerAbfragenCallback() {
                    @Override
                    public void onBenutzerSuccessfullyQueried(KooperationAnfrageBenutzer Benutzer) {
                        lvBenutzerAdapter.getLinkedMap().put(Benutzer.getMail(), Benutzer);
                        lvBenutzerAdapter.notifyDataSetChanged();
                        if(rdbGleichverteilung.isChecked()) lvBenutzerAdapter.distributeEven();
                    }
                });
                dialog.show();

            }
        });

        if(anfrage != null) {
            lvBenutzerAdapter = new Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Benutzer_Adapter(
                    getContext(), anfrage.isGleichverteilung());
            anfrage.getBenutzerMap().values().forEach(benutzer ->
                    lvBenutzerAdapter.getLinkedMap().put(benutzer.getMail(), benutzer.getCopy()));
            if(anfrage.isGleichverteilung()) {
                rdbGleichverteilung.setChecked(true);
            } else {
                rdbIndividuelleVerteilung.setChecked(true);
            }
        } else {
            lvBenutzerAdapter = new Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Benutzer_Adapter(
                    getContext(),true);
            //Sich selbst als Benutzer der Map hinzufügen
            lvBenutzerAdapter.getLinkedMap().put(GlobaleVariablen.getInstance().getEmail(),
                    new KooperationAnfrageBenutzer(
                            GlobaleVariablen.getInstance().getUserId(),
                            GlobaleVariablen.getInstance().getUserName(),
                            GlobaleVariablen.getInstance().getEmail(),
                            1.0,
                            KooperationAnfrageBenutzer.AnfrageBenutzerStatus.BESTÄTIGT,
                            Calendar.getInstance().getTime()
                            ));
            rdbGleichverteilung.setChecked(true);
        }

        lvBenutzer.setAdapter(lvBenutzerAdapter);
        lvBenutzerAdapter.notifyDataSetChanged();

        return ReturnView;
    }

    public boolean getGleichverteilung() {return rdbGleichverteilung.isChecked();}
    public LinkedHashMap<String, KooperationAnfrageBenutzer> getBenutzerMap() {return lvBenutzerAdapter.getLinkedMap();}
}