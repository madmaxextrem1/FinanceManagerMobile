package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Daueraufträge;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.CustomConfirmationDialog;

import java.util.ArrayList;

public class Verwaltung_Daueraufträge_Übersicht extends AppCompatActivity {

    RecyclerView rcvDauerauftragListe;
    ArrayList<Dauerauftrag> DauerauftragListe = new ArrayList<Dauerauftrag>();
    Verwaltung_Daueraufträge_Übersicht_DaueraufträgeRecyclerViewAdapter DauerauftragAdapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verwaltung_dauerauftraege_uebersicht);


        rcvDauerauftragListe = (RecyclerView) findViewById(R.id.Hauptmenu_fragment_home_rcvKontenAufstellung);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DauerauftragAdapter = new Verwaltung_Daueraufträge_Übersicht_DaueraufträgeRecyclerViewAdapter(this, Daueraufträge.getDaueraufträge());
        rcvDauerauftragListe.setAdapter(DauerauftragAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvDauerauftragListe.setLayoutManager(layoutManager);
        DauerauftragAdapter.notifyDataSetChanged();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View view) {
                ArrayList<Pair<Integer, String>> choiceList = new ArrayList<>();
                choiceList.add(new Pair<Integer, String>(1, "Standard"));
                choiceList.add(new Pair<Integer, String>(2, "Umbuchung"));

                CustomConfirmationDialog<Integer> ccd = new CustomConfirmationDialog<Integer>(
                        Verwaltung_Daueraufträge_Übersicht.this,
                        "Art auswählen",
                        "Auswählen",
                        "Abbrechen",
                        choiceList,
                        new CustomConfirmationDialog.CustomConfirmationDialog_Finished<Integer>() {
                            @Override
                            public void onCustomConfirationDialog_Finished(Integer result) {
                                switch (result) {
                                    case 1:
                                        DetailsAufrufen(null, Dauerauftrag.Dauerauftrag_Art.NORMAL, 0);
                                        break;
                                    case 2:
                                        DetailsAufrufen(null, Dauerauftrag.Dauerauftrag_Art.UMBUCHUNG, 0);
                                        break;
                                }
                            }
                        }
                );
                ccd.show();
            }
        });

        getWindow().setExitTransition(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void DetailsAufrufen(Dauerauftrag eintrag, Dauerauftrag.Dauerauftrag_Art dauerauftragArt, int kooperationID) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Verwaltung_Daueraufträge_Detailansicht dialog = new Verwaltung_Daueraufträge_Detailansicht(eintrag,
                kooperationID,
                dauerauftragArt,
                (eintrag != null),
                false,
                new Verwaltung_Daueraufträge_Detailansicht.Dauerauftrag_Edited_Callback() {
                    @Override
                    public void onDauerauftragEdited(Dauerauftrag dauerauftrag) {
                        if (dauerauftrag != null) {
                            Daueraufträge.getDaueraufträge().put(dauerauftrag.getIdentifier(), dauerauftrag);
                        }
                        DauerauftragAdapter.notifyDataSetChanged();
                    }
                }
        );
        dialog.show(fragmentManager, "fragment_edit_dauerauftrag");
    }
}
