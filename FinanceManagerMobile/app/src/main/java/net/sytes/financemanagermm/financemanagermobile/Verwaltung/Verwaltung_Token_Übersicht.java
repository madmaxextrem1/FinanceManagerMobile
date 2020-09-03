package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Konten;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Sign_In_Up.FinanceManagerMobileApplication;

import es.dmoral.toasty.Toasty;

public class Verwaltung_Token_Übersicht extends AppCompatActivity {
    private FloatingActionButton btnKontoHinzufügen;
    private RecyclerView lvKonten;
    private Verwaltung_Konten_Übersicht_KontenRecyclerViewAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verwaltung_konten_uebersicht);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lvKonten = findViewById(R.id.lvKonto);
        btnKontoHinzufügen = (FloatingActionButton) findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new Verwaltung_Konten_Übersicht_KontenRecyclerViewAdapter(this, FinanceManagerMobileApplication.getInstance().getDataManagement().getAccounts());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        lvKonten.setLayoutManager(layoutManager);
        lvKonten.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btnKontoHinzufügen.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Verwaltung_Konten_Detailansicht dialog = new Verwaltung_Konten_Detailansicht(Verwaltung_Token_Übersicht.this, null, new FinanceManagerData_Edited_Interface<Konto>() {
                    @Override
                    public void onDataEdited(Konto data, boolean created) {
                        if(created) {
                            Globale_Funktionen.SQL_createNewKonto(data, new FinanceManagerData_Edited_Interface<Konto>() {
                                @Override
                                public void onDataEdited(Konto data, boolean created) {
                                    Konten.getKonten().put(data.getIdentifier(), data);
                                    adapter.notifyDataSetChanged();
                                    Toasty.success(Verwaltung_Token_Übersicht.this, "Konto angelegt", Toast.LENGTH_SHORT, true).show();
                                }
                            });
                        }
                    }
                }, false);
                dialog.show(fragmentManager, "fragment_create_konto");
            }
        });
    }
    @Override
    public void onBackPressed(){
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
}
