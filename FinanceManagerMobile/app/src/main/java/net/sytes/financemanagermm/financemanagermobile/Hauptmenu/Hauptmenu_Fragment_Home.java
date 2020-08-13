package net.sytes.financemanagermm.financemanagermobile.Hauptmenu;

import android.content.res.Resources;
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

import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.DataManagement;
import net.sytes.financemanagermm.financemanagermobile.Sign_In_Up.FinanceManagerMobileApplication;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Hauptmenu_Fragment_Home extends Fragment implements OnChartValueSelectedListener, Observer {
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
        FinanceManagerMobileApplication.getInstance().getDataManagement().registerView(this);

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

        generateAccountOverview(FinanceManagerMobileApplication.getInstance().getDataManagement().getAccounts());
        Ergebnisse_Abfragen();
        Überkategorie_Ergebnisse_Abfragen();

        return returnView;
    }

    private void generateAccountOverview(LinkedHashMap<Integer, Konto> accounts) {
        rcvKontenAufstellungAdapter  = new Hauptmenu_Fragment_Home_KontenRecyclerViewAdapter(getContext(), accounts);
        rcvKontenAufstellung.setAdapter(rcvKontenAufstellungAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rcvKontenAufstellung.setLayoutManager(layoutManager);
        rcvKontenAufstellungAdapter.notifyDataSetChanged();
    }

    private void Ergebnisse_Abfragen() {
        HashMap PostDataBuchungen = new HashMap();
        PostDataBuchungen.put("userid", String.valueOf(FinanceManagerMobileApplication.getInstance().getDataManagement().getCurrentUser().getUserId()));

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
        HashMap postDataHauptkategorien = new HashMap();
        postDataHauptkategorien.put("userid", String.valueOf(FinanceManagerMobileApplication.getInstance().getDataManagement().getCurrentUser().getUserId()));

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
        chartAusgaben.setCenterTextTypeface(Typeface.DEFAULT);
        chartAusgaben.setCenterText(generateCenterSpannableText());
        chartAusgaben.setDrawHoleEnabled(true);
        chartAusgaben.setHoleColor(Color.TRANSPARENT);
        chartAusgaben.setTransparentCircleColor(Color.WHITE);
        chartAusgaben.setTransparentCircleAlpha(110);
        chartAusgaben.setHoleRadius(50f);
        chartAusgaben.setTransparentCircleRadius(53f);
        chartAusgaben.getDescription().setEnabled(false);
        chartAusgaben.setCenterText(lblAusgaben.getText());
        chartAusgaben.setCenterTextColor(Color.BLACK);
        chartAusgaben.setCenterTextSize(18f);
        chartAusgaben.setCenterTextTypeface(Typeface.SANS_SERIF);
        chartAusgaben.setDrawCenterText(true);
        chartAusgaben.setRotationEnabled(false);
        chartAusgaben.setHighlightPerTapEnabled(true);
        chartAusgaben.setMaxAngle(360); // HALF CHART
        chartAusgaben.setRotationAngle(360);
        Legend l = chartAusgaben.getLegend();
        l.setYOffset(10f);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        l.setTextSize(14f);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);

        // entry label styling
        chartAusgaben.setEntryLabelColor(Color.BLACK);
        chartAusgaben.setEntryLabelTypeface(Typeface.SANS_SERIF);
        chartAusgaben.setEntryLabelTextSize(14f);
        chartAusgaben.setDrawEntryLabels(false);
        PieDataSet dataSet = new PieDataSet(AusgabenEntries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(12f);
        List<Integer> ausgabenColorList = Arrays.stream(getResources().getIntArray(R.array.AusgabenColorTemplate)).boxed().collect(Collectors.toList());
        dataSet.setColors(ColorTemplate.createColors(getResources().getIntArray(R.array.AusgabenColorTemplate)));
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.SANS_SERIF);
        chartAusgaben.setData(data);
        chartAusgaben.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        chartAusgaben.invalidate();


        //EinnahmenChart
        chartEinnahmen.setUsePercentValues(true);
        chartEinnahmen.getDescription().setEnabled(false);
        chartEinnahmen.setCenterTextTypeface(Typeface.DEFAULT);
        chartEinnahmen.setCenterText(lblEinnahmen.getText());
        chartEinnahmen.setCenterTextColor(Color.BLACK);
        chartEinnahmen.setCenterTextSize(18f);
        chartEinnahmen.setCenterTextTypeface(Typeface.SANS_SERIF);
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
        legendEinnahmen.setYOffset(10f);
        legendEinnahmen.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legendEinnahmen.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legendEinnahmen.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legendEinnahmen.setWordWrapEnabled(true);
        legendEinnahmen.setDrawInside(false);
        legendEinnahmen.setTextSize(14f);
        legendEinnahmen.setXEntrySpace(7f);
        legendEinnahmen.setYEntrySpace(0f);

        // entry label styling
        chartEinnahmen.setEntryLabelColor(Color.WHITE);
        chartEinnahmen.setEntryLabelTypeface(Typeface.DEFAULT);
        chartEinnahmen.setEntryLabelTextSize(13f);
        chartEinnahmen.setDrawEntryLabels(false);
        PieDataSet dataSetEinnahmen = new PieDataSet(EinnahmenEntries, "");
        dataSetEinnahmen.setDrawIcons(false);
        dataSetEinnahmen.setSliceSpace(3f);
        dataSetEinnahmen.setColors(ColorTemplate.PASTEL_COLORS);
        PieData Einnahmen = new PieData(dataSetEinnahmen);
        Einnahmen.setValueFormatter(new PercentFormatter());
        Einnahmen.setValueTextSize(13f);
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

    @Override
    public void update(Observable o, Object arg) {
        DataManagement dataManagement = (DataManagement) o;
        generateAccountOverview(dataManagement.getAccounts());
    }
}