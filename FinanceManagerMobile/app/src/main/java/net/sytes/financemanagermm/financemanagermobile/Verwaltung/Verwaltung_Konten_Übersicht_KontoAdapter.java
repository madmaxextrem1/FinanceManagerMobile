package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.LinkedHashMap_BaseAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;

public class Verwaltung_Konten_Übersicht_KontoAdapter extends LinkedHashMap_BaseAdapter<Integer, Konto> {

    public Verwaltung_Konten_Übersicht_KontoAdapter(Context context, LinkedHashMap<Integer, Konto> kontoMap) {
        super(context, kontoMap);
    }

    public Verwaltung_Konten_Übersicht_KontoAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.verwaltung_konten_uebersicht_kontencardviewitem, parent, false);
        }

        final Konto KontoItem = getItem(position);

        TextView lblTitel =  view.findViewById(R.id.Titel);
        ImageView kontoimage = view.findViewById(R.id.Image);
        TextView lblKontoArt = view.findViewById(R.id.Art);
        TextView lblBestand = view.findViewById(R.id.Bestand);
        TextView lblAnzahlBuchungen =  view.findViewById(R.id.Anzahl_Buchungen);


        // define a click listener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                Verwaltung_Konten_Detailansicht dialog = new Verwaltung_Konten_Detailansicht(getContext(), KontoItem, new FinanceManagerData_Edited_Interface<Konto>() {
                    @Override
                    public void onDataEdited(Konto data, boolean created) {
                        notifyDataSetChanged();
                    }
                }, true);
                dialog.show(fragmentManager, "fragment_edit_konto");
            }
        });

        Resources res = getContext().getResources();

        lblTitel.setText(getItem(position).getKontoTitel());
        lblKontoArt.setText(KontoItem.getKontoArt().toString());
        kontoimage.setImageDrawable(KontoItem.getKontoArt().getKonto_Image());

        String optionalPlusBestand = "";
        if (KontoItem.getBestand() > 0) {
            optionalPlusBestand = "+";
        }
        String optionalPlusEntwicklung = "";
        if (KontoItem.getSummeBewegungenAktMonat() > 0) {
            optionalPlusEntwicklung = "+";
        }

        lblAnzahlBuchungen.setText("Finanzbuchungen " +
                new SimpleDateFormat("MMM yyyy").format(Calendar.getInstance(Locale.getDefault()).getTime()) + ": " +
                KontoItem.getAnzahlBewegungenAktMonat() +
                " (" + optionalPlusEntwicklung + Globale_Funktionen.getCurrencyFormatWith0Digits().format(KontoItem.getSummeBewegungenAktMonat()) + ")");
        lblBestand.setText(optionalPlusBestand + Globale_Funktionen.getCurrencyFormatWith0Digits().format(KontoItem.getBestand()));

        int CardViewBackgroundColorInactive = res.getColor(R.color.CardViewInactiveBackground, null);
        int RestInactiveColor = res.getColor(R.color.CardViewInactiveRest, null);
        int CardViewBackgroundColorActive = res.getColor(R.color.white, null);
        int RestActiveColorHeaderItem = res.getColor(R.color.ListItem_HeaderTextColor, null);
        int RestActiveColorSubItem = res.getColor(R.color.ListItem_SubTextColor, null);
        int ImageActiveColor = res.getColor(R.color.white, null);

        if (!KontoItem.getAktiv()) {
            view.setBackgroundColor(CardViewBackgroundColorInactive);
            ((CardView) view).setRadius(4);
            kontoimage.setColorFilter(CardViewBackgroundColorInactive, PorterDuff.Mode.SRC_ATOP);
            lblAnzahlBuchungen.setTextColor(RestInactiveColor);
            lblTitel.setTextColor(RestInactiveColor);
            lblKontoArt.setTextColor(RestInactiveColor);
            lblBestand.setTextColor(RestInactiveColor);
        } else {
            view.setBackgroundColor(CardViewBackgroundColorActive);
            ((CardView) view).setRadius(4);
            kontoimage.setColorFilter(CardViewBackgroundColorActive, PorterDuff.Mode.SRC_ATOP);
            lblAnzahlBuchungen.setTextColor(RestActiveColorSubItem);
            lblTitel.setTextColor(RestActiveColorHeaderItem);
            lblKontoArt.setTextColor(RestActiveColorSubItem);
            lblBestand.setTextColor(RestActiveColorHeaderItem);
        }

        return view;
    }

}