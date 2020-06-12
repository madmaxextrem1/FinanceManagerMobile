package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchung_Kategorie_Dialog_lvKategorien_Adapter;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungshauptkategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie_Update_Interface;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorien;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.BuchungskategorienCallback;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Konten;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class Verwaltung_Kategorien_Übersicht extends AppCompatActivity {
    private ExpandableListView lvKategorien;
    private FloatingActionButton fab;
    private int lastExpandedPosition = -1;
    private static Buchungskategorie_Update_Interface callback;
    private Verwaltung_Kategorien_Übersicht_KategorieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verwaltung_kategorien_uebersicht);

        lvKategorien = findViewById(R.id.lvKategorien);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(!Buchungskategorien.getBuchungskategorienInitialized()) {
            Buchungskategorien.initializeBuchungskategorien(new BuchungskategorienCallback() {
                @Override
                public void onBuchungskategorienSuccessfullyLoaded(LinkedHashMap<Integer, Buchungshauptkategorie> Buchungskategorien) {
                    adapter = new Verwaltung_Kategorien_Übersicht_KategorieAdapter(Verwaltung_Kategorien_Übersicht.this);
                    lvKategorien.setAdapter(adapter);
                    lvKategorien.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int groupPosition) {
                            if (lastExpandedPosition != -1
                                    && groupPosition != lastExpandedPosition) {
                                lvKategorien.collapseGroup(lastExpandedPosition);
                            }
                            lastExpandedPosition = groupPosition;
                            lvKategorien.setSelectionFromTop(groupPosition,0);
                        }
                    });
                    adapter.notifyDataSetChanged();
                }
            });
        } else {
            adapter = new Verwaltung_Kategorien_Übersicht_KategorieAdapter(Verwaltung_Kategorien_Übersicht.this);
            lvKategorien.setAdapter(adapter);
            lvKategorien.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    if (lastExpandedPosition != -1
                            && groupPosition != lastExpandedPosition) {
                        lvKategorien.collapseGroup(lastExpandedPosition);
                    }
                    lastExpandedPosition = groupPosition;
                    lvKategorien.setSelectionFromTop(groupPosition,0);
                }
            });
            adapter.notifyDataSetChanged();
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View view) {
                showEditDialog(null, false);
            }
        });

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finishAfterTransition();
        if (callback != null) {
            callback.onBuchungskategorieChanged(null);
        }
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
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showEditDialog(Buchungskategorie kategorie, boolean update) {
        Verwaltung_Kategorien_Übersicht_Bearbeiten_Dialog dialog = new Verwaltung_Kategorien_Übersicht_Bearbeiten_Dialog(kategorie, update, new Buchungskategorie_Update_Interface() {
            @Override
            public void onBuchungskategorieChanged(Buchungskategorie Kategorie) {
                Buchungskategorien.rebuildKategorieHierarchy(new Buchungskategorie_Update_Interface() {
                    @Override
                    public void onBuchungskategorieChanged(Buchungskategorie Kategorie) {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        dialog.show(fragmentManager, "dialog_kategorie_auswahl");
    }

    public static void setUpdateCallback(Buchungskategorie_Update_Interface Callback) {
        callback = Callback;
    }
}
