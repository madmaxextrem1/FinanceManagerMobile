package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Gemeinsame_Finanzen extends AppCompatActivity {
    private ViewPager viewPager;
    private Gemeinsame_Finanzen_Viewpager_Adapter fragmentPagerAdapter;
    private FloatingActionButton btnNeueAnfrage;
    private Gemeinsame_Finanzen_Fragment_Anfragen fragmentAnfragen;
    private Gemeinsame_Finanzen_Fragment_Kooperationen fragmentKooperationen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gemeinsame_finanzen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentPagerAdapter = new Gemeinsame_Finanzen_Viewpager_Adapter(
                getSupportFragmentManager(),this);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        btnNeueAnfrage = findViewById(R.id.fab);
        btnNeueAnfrage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("hello");
                    Gemeinsame_Finanzen_Anfrage_BearbeitenDialog bearbeitenDialog = new Gemeinsame_Finanzen_Anfrage_BearbeitenDialog(false, null, new Gemeinsame_Finanzen_Anfrage_BearbeitenCallback() {
                        @Override
                        public void onAnfrageEdited(KooperationAnfrage Anfrage) {
                            Gemeinsame_Finanzen_Fragment_Kooperationen kooperationFragment = (Gemeinsame_Finanzen_Fragment_Kooperationen)
                                    fragmentPagerAdapter.getRegisteredFragment(0);

                            if (kooperationFragment != null) {
                                // If article frag is available, we're in two-pane layout...

                                // Call a method in the ArticleFragment to update its content
                                kooperationFragment.Kooperationen_Aktualisieren();
                                viewPager.setCurrentItem(0, true);
                            }
                        }
                    });
                    bearbeitenDialog.show(getSupportFragmentManager(), "AnfrageBearbeitenDialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        viewPager.setAdapter(fragmentPagerAdapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.tab_layout);
        viewPagerTab.setViewPager(viewPager);

        fragmentPagerAdapter.startUpdate(viewPager);
        fragmentKooperationen = (Gemeinsame_Finanzen_Fragment_Kooperationen) fragmentPagerAdapter.instantiateItem(viewPager, 0);
        fragmentAnfragen = (Gemeinsame_Finanzen_Fragment_Anfragen) fragmentPagerAdapter.instantiateItem(viewPager, 1);
        fragmentPagerAdapter.finishUpdate(viewPager);
        viewPager.setCurrentItem(0);
    }

    public Gemeinsame_Finanzen_Fragment_Anfragen getFragmentAnfragen() {
        return fragmentAnfragen;
    }

    public Gemeinsame_Finanzen_Fragment_Kooperationen getFragmentKooperationen() {
        return fragmentKooperationen;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    private void Benutzer_NeueAnfrage_Abfragen(){
        ArrayList<Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag> benutzerListe =
                new ArrayList<Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag>();

        GlobaleVariablen globaleVariablen = GlobaleVariablen.getInstance();
        HashMap postData = new HashMap();
        postData.put("userid", String.valueOf(globaleVariablen.getUserId()));

        PostResponseAsyncTask BenutzerAbfragenTask =
                new PostResponseAsyncTask(Gemeinsame_Finanzen.this, postData,
                        new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                try {
                                    JSONObject jsonObj = new JSONObject(s);

                                    // Getting JSON Array node
                                    JSONArray benutzer = jsonObj.getJSONArray("Benutzer");

                                    //looping through all the elements in json array
                                    for (int i = 0; i < benutzer.length(); i++) {

                                        //getting json object from the json array
                                        JSONObject obj = benutzer.getJSONObject(i);

                                        //getting the name from the json object and putting it inside string array
                                        int BenutzerID = obj.getInt("BenutzerID");
                                        String Benutzername = obj.getString("Benutzername");
                                        String eMail = obj.getString("Email");
                                        Log.d("Test123",eMail);
                                        benutzerListe.add(
                                                new Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag(
                                                        BenutzerID, Benutzername, eMail));
                                    }

                                    GlobaleVariablen.getInstance().setAnfragenBenutzerListe(benutzerListe);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
        BenutzerAbfragenTask.execute("http://financemanagermm.sytes.net/fmclient/kooperation_anfrage_benutzer_abfragen.php");
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof Gemeinsame_Finanzen_Fragment_Anfragen) {
            Gemeinsame_Finanzen_Fragment_Anfragen anfrageFragment = (Gemeinsame_Finanzen_Fragment_Anfragen) fragment;
        }
    }

    @Override
    public void onBackPressed(){
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
}
