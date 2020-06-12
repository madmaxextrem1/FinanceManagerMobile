package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.LinkedHashMap_BaseRecyclerAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

public class Verwaltung_Daueraufträge_Übersicht_DaueraufträgeRecyclerViewAdapter extends LinkedHashMap_BaseRecyclerAdapter<Integer, Dauerauftrag> {
    private View view;

    public Verwaltung_Daueraufträge_Übersicht_DaueraufträgeRecyclerViewAdapter(Context context, LinkedHashMap<Integer, Dauerauftrag> dauerauftragMap) {
        super(context, dauerauftragMap);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.verwaltung_dauerauftraege_uebersicht_dauerauftragcardviewitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        final Dauerauftrag DauerauftragItem = getItem(position);

        ViewCompat.setTransitionName(holder.itemView, DauerauftragItem.getBeschreibung());

        // define a click listener
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((Verwaltung_Daueraufträge_Übersicht) getContext()).getSupportFragmentManager();
                Verwaltung_Daueraufträge_Detailansicht dialog = new Verwaltung_Daueraufträge_Detailansicht(DauerauftragItem,
                        DauerauftragItem.getKooperationID(),
                        DauerauftragItem.getDauerauftragArt(),
                        true,
                        false,
                        new Verwaltung_Daueraufträge_Detailansicht.Dauerauftrag_Edited_Callback() {
                            @Override
                            public void onDauerauftragEdited(Dauerauftrag dauerauftrag) {
                                notifyDataSetChanged();
                            }
                        }
                );
                dialog.show(fragmentManager, "fragment_edit_dauerauftrag");
            }
        });

        Resources res = getContext().getResources();
        final int newColor = res.getColor(R.color.colorPrimary, null);

        mHolder.KontoArtImage.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);

        Konto kontoEintrag = getItem(position).getKonto();

        mHolder.KontoArtImage.setImageDrawable(getItem(position).getDauerauftragArt().getImage(kontoEintrag));
        mHolder.DauerauftragTitel.setText(DauerauftragItem.getBeschreibung());
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        String sBetrag = formatter.format(getItem(position).getBetrag());
        String optionalPlus = "";
        if (DauerauftragItem.getBetrag() > 0) {
            optionalPlus = "+";
        }
        mHolder.Betrag.setText((DauerauftragItem.getDauerauftragArt() == Dauerauftrag.Dauerauftrag_Art.UMBUCHUNG) ? sBetrag : optionalPlus + sBetrag);
        mHolder.Rhythmus.setText(DauerauftragItem.getRhythmus());
        mHolder.NächsteAusführung.setText("Nächste Ausführung: " + GlobaleVariablen.getInstance().getDE_DateFormat().format(getItem(position).getNächsteAusführung()));

        int CardViewBackgroundColorInactive = res.getColor(R.color.CardViewInactiveBackground, null);
        int RestInactiveColor = res.getColor(R.color.CardViewInactiveRest, null);
        int CardViewBackgroundColorActive = res.getColor(R.color.white, null);
        int RestActiveColorHeaderItem = res.getColor(R.color.ListItem_HeaderTextColor, null);
        int RestActiveColorSubItem = res.getColor(R.color.ListItem_SubTextColor, null);
        int ImageActiveColor = res.getColor(R.color.white, null);

        if (!getItem(position).isAktiv()) {
            mHolder.itemView.setBackgroundColor(CardViewBackgroundColorInactive);
            mHolder.KontoArtImage.setColorFilter(CardViewBackgroundColorInactive, PorterDuff.Mode.SRC_ATOP);
            mHolder.DauerauftragTitel.setTextColor(RestInactiveColor);
            mHolder.Betrag.setTextColor(RestInactiveColor);
            mHolder.Rhythmus.setTextColor(RestInactiveColor);
            mHolder.NächsteAusführung.setTextColor(RestInactiveColor);
        } else {
            mHolder.itemView.setBackgroundColor(CardViewBackgroundColorActive);
            mHolder.KontoArtImage.setColorFilter(ImageActiveColor, PorterDuff.Mode.SRC_ATOP);
            mHolder.NächsteAusführung.setTextColor(RestActiveColorSubItem);
            mHolder.DauerauftragTitel.setTextColor(RestActiveColorHeaderItem);
            mHolder.Rhythmus.setTextColor(RestActiveColorSubItem);
            if (DauerauftragItem.getDauerauftragArt() == Dauerauftrag.Dauerauftrag_Art.NORMAL && getItem(position).getBetrag() > 0) {
                mHolder.Betrag.setTextColor(ContextCompat.getColor(getContext(), R.color.fab_Color_Hauptmenu));
            } else if (DauerauftragItem.getDauerauftragArt() == Dauerauftrag.Dauerauftrag_Art.NORMAL) {
                mHolder.Betrag.setTextColor(ContextCompat.getColor(getContext(), R.color.betrag_negativ));
            } else {
                mHolder.Betrag.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView KontoArtImage;
        private TextView DauerauftragTitel;
        private TextView Rhythmus;
        private TextView Betrag;
        private TextView NächsteAusführung;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            KontoArtImage = itemView.findViewById(R.id.Konto_Image);
            DauerauftragTitel = itemView.findViewById(R.id.Titel);
            Rhythmus = itemView.findViewById(R.id.Rhythmus);
            Betrag = itemView.findViewById(R.id.Betrag);
            NächsteAusführung = itemView.findViewById(R.id.Naechste_Ausführung);
        }
    }
}

