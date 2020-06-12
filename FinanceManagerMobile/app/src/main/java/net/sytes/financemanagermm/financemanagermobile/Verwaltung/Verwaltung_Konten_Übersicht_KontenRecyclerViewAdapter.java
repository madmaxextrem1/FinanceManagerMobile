package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.LinkedHashMap_BaseRecyclerAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class Verwaltung_Konten_Übersicht_KontenRecyclerViewAdapter extends LinkedHashMap_BaseRecyclerAdapter<Integer, Konto> {
    private View view;

    public Verwaltung_Konten_Übersicht_KontenRecyclerViewAdapter(Context context, LinkedHashMap<Integer, Konto> kontoMap) {
        super(context, kontoMap);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.verwaltung_konten_uebersicht_kontencardviewitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        System.out.println(holder.getAdapterPosition());
        System.out.println(position);

        final Konto kontoItem = getItem(position);

        ViewCompat.setTransitionName(holder.itemView, kontoItem.getKontoTitel());

        // define a click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                Verwaltung_Konten_Detailansicht dialog = new Verwaltung_Konten_Detailansicht(getContext(), kontoItem, new FinanceManagerData_Edited_Interface<Konto>() {
                    @Override
                    public void onDataEdited(Konto data, boolean created) {
                        Globale_Funktionen.SQL_updateKonto(data, new FinanceManagerData_Edited_Interface<Konto>() {
                            @Override
                            public void onDataEdited(Konto data, boolean created) {
                                Toasty.success(getContext(), "Gespeichert", Toast.LENGTH_SHORT, true ).show();
                                notifyDataSetChanged();
                            }
                        });
                    }
                }, true);
                dialog.show(fragmentManager, "fragment_edit_konto");
            }
        });

        Resources res = getContext().getResources();

        mHolder.KontoArtImage.setImageDrawable(kontoItem.getKontoArt().getKonto_Image());
        mHolder.KontoArt.setText(kontoItem.getKontoArt().toString());

        mHolder.KontoTitel.setText(kontoItem.getKontoTitel());
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        String sBestand = formatter.format(kontoItem.getBestand());
        String sSummeBewegungen = formatter.format(kontoItem.getSummeBewegungenAktMonat());
        mHolder.KontoBestand.setText(sBestand);

        String optionalPlusBestand = "";
        if (kontoItem.getBestand() > 0) {
            optionalPlusBestand = "+";
        }
        String optionalPlusEntwicklung = "";
        if (kontoItem.getSummeBewegungenAktMonat() > 0) {
            optionalPlusEntwicklung = "+";
        }
        mHolder.KontoEntwicklung.setText("Finanzbuchungen " +
                new SimpleDateFormat("MMM yyyy").format(Calendar.getInstance(Locale.getDefault()).getTime()) + ": " +
                kontoItem.getAnzahlBewegungenAktMonat() +
                " (" + optionalPlusEntwicklung + NumberFormat.getCurrencyInstance().format(kontoItem.getSummeBewegungenAktMonat()) + ")");
        mHolder.KontoBestand.setText(optionalPlusBestand + Globale_Funktionen.getCurrencyFormatWith0Digits().format(kontoItem.getBestand()));

        int CardViewBackgroundColorInactive = res.getColor(R.color.CardViewInactiveBackground, null);
        int RestInactiveColor = res.getColor(R.color.CardViewInactiveRest, null);
        int CardViewBackgroundColorActive = res.getColor(R.color.white, null);
        int RestActiveColorHeaderItem = res.getColor(R.color.ListItem_HeaderTextColor, null);
        int RestActiveColorSubItem = res.getColor(R.color.ListItem_SubTextColor, null);
        int ImageActiveColor = res.getColor(R.color.white, null);

        if (!kontoItem.getAktiv()) {
            mHolder.itemView.setBackgroundColor(CardViewBackgroundColorInactive);
            ((CardView) mHolder.itemView).setRadius(4);
            mHolder.KontoArtImage.setColorFilter(CardViewBackgroundColorInactive, PorterDuff.Mode.SRC_ATOP);
            mHolder.KontoEntwicklung.setTextColor(RestInactiveColor);
            mHolder.KontoTitel.setTextColor(RestInactiveColor);
            mHolder.KontoArt.setTextColor(RestInactiveColor);
            mHolder.KontoBestand.setTextColor(RestInactiveColor);
        } else {
            mHolder.itemView.setBackgroundColor(CardViewBackgroundColorActive);
            ((CardView) mHolder.itemView).setRadius(4);
            mHolder.KontoArtImage.setColorFilter(CardViewBackgroundColorActive, PorterDuff.Mode.SRC_ATOP);
            mHolder.KontoEntwicklung.setTextColor(RestActiveColorSubItem);
            mHolder.KontoTitel.setTextColor(RestActiveColorHeaderItem);
            mHolder.KontoArt.setTextColor(RestActiveColorSubItem);
            mHolder.KontoBestand.setTextColor(RestActiveColorHeaderItem);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView KontoArtImage;
        private TextView KontoTitel;
        private TextView KontoBestand;
        private TextView KontoArt;
        private TextView KontoEntwicklung;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            KontoArtImage = itemView.findViewById(R.id.Image);
            KontoTitel = itemView.findViewById(R.id.Titel);
            KontoBestand = itemView.findViewById(R.id.Bestand);
            KontoArt = itemView.findViewById(R.id.Art);
            KontoEntwicklung = itemView.findViewById(R.id.Anzahl_Buchungen);
        }
    }
}