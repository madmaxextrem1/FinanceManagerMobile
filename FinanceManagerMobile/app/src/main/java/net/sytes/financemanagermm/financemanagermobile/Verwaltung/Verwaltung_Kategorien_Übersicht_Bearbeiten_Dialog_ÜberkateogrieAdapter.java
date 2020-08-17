package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungshauptkategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Verwaltung_Kategorien_Übersicht_Bearbeiten_Dialog_ÜberkateogrieAdapter extends BaseAdapter {
    private LinkedHashMap<Integer, Buchungshauptkategorie> hauptkategorieMap;
    private Context Context;

    public Verwaltung_Kategorien_Übersicht_Bearbeiten_Dialog_ÜberkateogrieAdapter(Context context, LinkedHashMap<Integer, Buchungshauptkategorie> hautpkategorien) {
        super();
        this.hauptkategorieMap = hautpkategorien;
        this.Context = context;
    }

    public LinkedHashMap<Integer, Buchungshauptkategorie> getEintragListe() {
        return hauptkategorieMap;
    }

    public int getItemPosition (Buchungskategorie kategorie) {
        int i = -1;

        for(Integer key:hauptkategorieMap.keySet()) {
            i++;
            if(kategorie.getÜKatId() == key) return i;
        }

        return i;
    }

    @Override
    public int getCount() {
        return getEintragListe().size();
    }

    @Override
    public Buchungshauptkategorie getItem(int position) {
        int i = 0;
        for(Integer key:hauptkategorieMap.keySet()) {
            if(i == position) return hauptkategorieMap.get(key);
            i++;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) Context
                    .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.verwaltung_kategorien_uebersicht_bearbeiten_dialog_kateogrieadapter_groupitem, parent, false);
        }

        TextView txtBeschreibung = view.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_lv_subitem_Titel);
        txtBeschreibung.setText(getItem(position).getBeschreibung());
        TextView imgCircle = view.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_lv_subitem_Circle);

        int Rot = getItem(position).getRot();
        int Grün = getItem(position).getGrün();
        int Blau = getItem(position).getBlau();
        float alpha = new Globale_Funktionen().Helligkeit_Berechnen(Rot, Grün, Blau);
        Color backgroundTint = Color.valueOf(Color.rgb(Rot, Grün, Blau));
        int foregroundTint;
        if ((int) alpha >= 255 / 2) {
            foregroundTint = Color.BLACK;
        } else {
            foregroundTint = Color.WHITE;
        }
        imgCircle.setTextColor(foregroundTint);
        imgCircle.setBackgroundTintList(ColorStateList.valueOf(backgroundTint.toArgb()));
        imgCircle.setText(getItem(position).getBeschreibung().substring(0, 1));

        return view;
    }
}
