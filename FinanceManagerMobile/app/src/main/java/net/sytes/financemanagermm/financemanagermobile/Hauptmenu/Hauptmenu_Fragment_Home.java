package net.sytes.financemanagermm.financemanagermobile.Hauptmenu;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorien;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.BuchungskategorienCallback;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.DauerauftragCallback;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Daueraufträge;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.FinanceManagerCallback;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.FinanzbuchungTokens;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Konten;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Kooperationen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.KooperationenCallback;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.TokensCallback;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Dauerauftrag;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungshauptkategorie;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Hauptmenu_Fragment_Home extends Fragment implements OnChartValueSelectedListener {
    private PieChart chartAusgaben;
    private PieChart chartEinnahmen;
    private TextView lblErgebnis;
    private TextView lblAusgaben;
    private TextView lblEinnahmen;
    private RecyclerView rcvKontenAufstellung;
    private ArrayList<Hauptmenu_Fragment_Home_Ueberkategorie_Ergebnis_Eintrag> überkategorie_ergebnisse = new ArrayList<Hauptmenu_Fragment_Home_Ueberkategorie_Ergebnis_Eintrag>();
    private Hauptmenu_Fragment_Home_KontenRecyclerViewAdapter rcvKontenAufstellungAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View returnView = (View) inflater.inflate(R.layout.hauptmenu_fragment_home, container, false);

        lblErgebnis = (TextView) returnView.findViewById(R.id.lblGesamtergebnis);
        lblEinnahmen = (TextView) returnView.findViewById(R.id.lblEinnahmen);
        lblAusgaben = (TextView) returnView.findViewById(R.id.lblAusgaben);

        rcvKontenAufstellung = (RecyclerView) returnView.findViewById(R.id.Hauptmenu_fragment_home_rcvKontenAufstellung);

        chartAusgaben = returnView.findViewById(R.id.chartAusgaben);
        chartAusgaben.setUsePercentValues(true);
        chartAusgaben.getDescription().setEnabled(false);
        chartAusgaben.setExtraOffsets(0, 0, 0, 0);
        chartAusgaben.setBackgroundColor(Color.TRANSPARENT);

        chartEinnahmen = returnView.findViewById(R.id.chartEinnahmen);
        chartEinnahmen.setUsePercentValues(true);
        chartEinnahmen.getDescription().setEnabled(false);
        chartEinnahmen.setExtraOffsets(0, 0, 0, 0);
        chartEinnahmen.setBackgroundColor(Color.TRANSPARENT);

        if(!Konten.getKontenInitialized()) {
            Konten.initializeKonten(new FinanceManagerCallback<Integer, Konto>() {
                @Override
                public void onDataUpdated(LinkedHashMap<Integer, Konto> linkedHashMap) {
                    if(!Daueraufträge.getDaueraufträgeInitialized()) {
                        Daueraufträge.initializeDaueraufträge(new DauerauftragCallback() {
                            @Override
                            public void onDaueraufträgeSuccessfullyLoaded(LinkedHashMap<Integer, Dauerauftrag> daueraufträge) {

                            }
                        });
                    }
                    KontenAufstellungGenerieren();
                }
            });
        } else {
            KontenAufstellungGenerieren();
        }

        if(!Buchungskategorien.getBuchungskategorienInitialized()) {
            Buchungskategorien.initializeBuchungskategorien(new BuchungskategorienCallback() {
                @Override
                public void onBuchungskategorienSuccessfullyLoaded(LinkedHashMap<Integer, Buchungshauptkategorie> Buchungskategorien) {

                }
            });
        }

        if(!Kooperationen.getKooperationenInitialized()) {
            Kooperationen.initializeKooperationen(new KooperationenCallback() {
                @Override
                public void onKooperationenSuccessfullyLoaded(LinkedHashMap<Integer, Kooperation> Kooperationen) {

                }
            });
        }

        if(!FinanzbuchungTokens.getTokensInitialized()) {
            FinanzbuchungTokens.initializeTokens(new TokensCallback() {
                @Override
                public void onTokensSuccessfullyLoaded(ArrayList<FinanzbuchungToken> Tokens) {

                }
            });
        }

        Ergebnisse_Abfragen();
        Überkategorie_Ergebnisse_Abfragen();

        return returnView;
    }

    private void KontenAufstellungGenerieren() {
        rcvKontenAufstellungAdapter  = new Hauptmenu_Fragment_Home_KontenRecyclerViewAdapter(getContext(), Konten.getKonten());
        rcvKontenAufstellung.setAdapter(rcvKontenAufstellungAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rcvKontenAufstellung.setLayoutManager(layoutManager);
        rcvKontenAufstellungAdapter.notifyDataSetChanged();
    }
    private void Ergebnisse_Abfragen() {
        GlobaleVariablen globaleVariablen = GlobaleVariablen.getInstance();
        HashMap PostDataBuchungen = new HashMap();
        PostDataBuchungen.put("userid", String.valueOf(globaleVariablen.getUserId()));
        PostDataBuchungen.put("txtUsername", globaleVariablen.getUserName());
        PostDataBuchungen.put("txtPassword", globaleVariablen.getPwd());

        PostResponseAsyncTask task1 = new PostResponseAsyncTask(getContext(), PostDataBuchungen, false, new AsyncResponse() {
            @Override
            public void processFinish(String s) {

                try {
                    JSONObject jsonObj = new JSONObject(s);

                    // Getting JSON Array node
                    JSONArray Ergebnisse = jsonObj.getJSONArray("Ergebnisse");

                    //looping through all the elements in json array
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getIntegerInstance(Locale.getDefault());
                    formatter.setCurrency(Currency.getInstance(Locale.getDefault()));

                    for (int i = 0; i < Ergebnisse.length(); i++) {
                        //getting json object from the json array
                        JSONObject obj = Ergebnisse.getJSONObject(i);

                        Long Ergebnis = Math.round(obj.getDouble("Ergebnis"));
                        String sErgebnis = formatter.format(Ergebnis) + " "+ Currency.getInstance(Locale.getDefault()).getSymbol();

                         switch (obj.getString("ErgebnisKz")) {
                             case "Gesamt":
                                 lblErgebnis.setText(sErgebnis);
                                 break;
                             case "Ausgaben":
                                 lblAusgaben.setText(sErgebnis);
                                 break;
                             case "Einnahmen":
                                 lblEinnahmen.setText(sErgebnis);
                                 break;
                         }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        task1.execute("http://financemanagermm.sytes.net/fmclient/hauptmenu_ergebnisse_abfragen.php");

    }
    private SpannableString generateCenterSpannableText() {
       SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }
    private void Überkategorie_Ergebnisse_Abfragen() {
        GlobaleVariablen globaleVariablen = GlobaleVariablen.getInstance();
        HashMap postDataHauptkategorien = new HashMap();
        postDataHauptkategorien.put("userid", String.valueOf(globaleVariablen.getUserId()));
        postDataHauptkategorien.put("txtUsername", globaleVariablen.getUserName());
        postDataHauptkategorien.put("txtPassword", globaleVariablen.getPwd());

        PostResponseAsyncTask ÜberKategorien_Abfragen_Task = new PostResponseAsyncTask(getContext(), postDataHauptkategorien,false, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                try {
                    JSONObject jsonObj = new JSONObject(s);

                    // Getting JSON Array node
                    JSONArray Überkategorien = jsonObj.getJSONArray("data");

                    //looping through all the elements in json array
                    for (int i = 0; i < Überkategorien.length(); i++) {
                        überkategorie_ergebnisse.add(new Hauptmenu_Fragment_Home_Ueberkategorie_Ergebnis_Eintrag(Überkategorien.getJSONObject(i)));
                    }
                    setData(überkategorie_ergebnisse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }});
        ÜberKategorien_Abfragen_Task.execute("http://financemanagermm.sytes.net/fmclient/überkategorien_ergebnisse_abfragen.php");
    }

    private void setData(ArrayList<Hauptmenu_Fragment_Home_Ueberkategorie_Ergebnis_Eintrag> ErgebnisListe) {
        ArrayList<PieEntry> AusgabenEntries = new ArrayList<>();
        ArrayList<PieEntry> EinnahmenEntries = new ArrayList<>();
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (Hauptmenu_Fragment_Home_Ueberkategorie_Ergebnis_Eintrag ergebnis:ErgebnisListe) {
            if(ergebnis.getErgebnis()<0) {
                AusgabenEntries.add(new PieEntry(Float.parseFloat(String.valueOf(ergebnis.getErgebnis() * -1)),ergebnis.getBeschreibung()));
            } else {
                EinnahmenEntries.add(new PieEntry(Float.parseFloat(String.valueOf(ergebnis.getErgebnis())),ergebnis.getBeschreibung()));
            }
        }

        chartAusgaben.setUsePercentValues(true);
        chartAusgaben.getDescription().setEnabled(false);
        chartAusgaben.setCenterTextTypeface(Typeface.DEFAULT);
        chartAusgaben.setCenterText(generateCenterSpannableText());
        chartAusgaben.setDrawHoleEnabled(true);
        chartAusgaben.setHoleColor(Color.TRANSPARENT);
        chartAusgaben.setTransparentCircleColor(Color.WHITE);
        chartAusgaben.setTransparentCircleAlpha(110);
        chartAusgaben.setHoleRadius(68f);
        chartAusgaben.setTransparentCircleRadius(71f);
        chartAusgaben.setDrawCenterText(false);
        chartAusgaben.setRotationEnabled(false);
        chartAusgaben.setHighlightPerTapEnabled(true);
        chartAusgaben.setMaxAngle(360); // HALF CHART
        chartAusgaben.setRotationAngle(360);
        chartAusgaben.setCenterTextOffset(0, -20);
        Legend l = chartAusgaben.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(190f);
        // entry label styling
        chartAusgaben.setEntryLabelColor(Color.BLACK);
        chartAusgaben.setEntryLabelTypeface(Typeface.DEFAULT);
        chartAusgaben.setEntryLabelTextSize(12f);
        chartAusgaben.setDrawEntryLabels(false);
        PieDataSet dataSet = new PieDataSet(AusgabenEntries, "Ausgaben");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(10f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //dataSet.setSelectionShift(0f);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(Typeface.DEFAULT);
        chartAusgaben.setData(data);
        chartAusgaben.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        chartAusgaben.invalidate();


        //EinnahmenChart
        chartEinnahmen.setUsePercentValues(true);
        chartEinnahmen.getDescription().setEnabled(false);
        chartEinnahmen.setCenterTextTypeface(Typeface.DEFAULT);
        chartEinnahmen.setCenterText("Einnahmen");
        chartEinnahmen.setDrawHoleEnabled(true);
        chartEinnahmen.setHoleColor(Color.WHITE);
        chartEinnahmen.setTransparentCircleColor(Color.WHITE);
        chartEinnahmen.setTransparentCircleAlpha(110);
        chartEinnahmen.setHoleRadius(50f);
        chartEinnahmen.setTransparentCircleRadius(53f);
        chartEinnahmen.setDrawCenterText(true);
        chartEinnahmen.setRotationEnabled(false);
        chartEinnahmen.setHighlightPerTapEnabled(true);
        chartEinnahmen.setMaxAngle(360); // HALF CHART
        chartEinnahmen.setRotationAngle(360);
        chartEinnahmen.setCenterTextOffset(0, 0);
        Legend legendEinnahmen = chartEinnahmen.getLegend();
        chartEinnahmen.getLegend().mNeededHeight = 0;
        legendEinnahmen.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legendEinnahmen.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legendEinnahmen.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legendEinnahmen.setDrawInside(false);
        legendEinnahmen.setFormSize(0f);
        legendEinnahmen.setXEntrySpace(7f);
        legendEinnahmen.setYEntrySpace(0f);
        legendEinnahmen.setYOffset(0f);
        legendEinnahmen.setEnabled(false);

        // entry label styling
        chartEinnahmen.setEntryLabelColor(Color.WHITE);
        chartEinnahmen.setEntryLabelTypeface(Typeface.DEFAULT);
        chartEinnahmen.setEntryLabelTextSize(12f);
        PieDataSet dataSetEinnahmen = new PieDataSet(EinnahmenEntries, "Einnahmen");
        dataSetEinnahmen.setDrawIcons(false);
        dataSetEinnahmen.setSliceSpace(3f);
        dataSetEinnahmen.setSelectionShift(10f);
        dataSetEinnahmen.setColors(ColorTemplate.PASTEL_COLORS);
        //dataSet.setSelectionShift(0f);
        PieData Einnahmen = new PieData(dataSetEinnahmen);
        Einnahmen.setValueFormatter(new PercentFormatter());
        Einnahmen.setValueTextSize(11f);
        Einnahmen.setValueTextColor(Color.WHITE);
        Einnahmen.setValueTypeface(Typeface.DEFAULT);
        chartEinnahmen.setData(Einnahmen);
        chartEinnahmen.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        chartEinnahmen.invalidate();
    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }
    @Override
    public void onNothingSelected() {

    }
}