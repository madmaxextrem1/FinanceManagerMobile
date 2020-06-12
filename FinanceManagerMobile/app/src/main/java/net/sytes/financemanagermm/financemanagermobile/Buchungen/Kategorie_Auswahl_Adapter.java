package net.sytes.financemanagermm.financemanagermobile.Buchungen;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.ArrayList;

public class Kategorie_Auswahl_Adapter extends BaseAdapter implements Filterable {
    private ArrayList<Buchungskategorie> eintragListe;
    private Context context;

    public Kategorie_Auswahl_Adapter(Context context) {
        super();
        this.eintragListe = new ArrayList<Buchungskategorie>();
        this.context = context;
    }

    public ArrayList<Buchungskategorie> getEintragListe() {
        return eintragListe;
    }

    @Override
    public int getCount() {
        return eintragListe.size();
    }

    @Override
    public Buchungskategorie getItem(int position) {
        return eintragListe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return eintragListe.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.verwaltung_dauerauftraege_fragment_details_cbokategorie_adapteritem, parent, false);
        }
        TextView Title = view.findViewById(R.id.Kategorie_Auswahl_Titel);
        Title.setText(eintragListe.get(position).getBeschreibung());
        TextView Circle = view.findViewById(R.id.Kategorie_Auswahl_Image);
        int Rot = eintragListe.get(position).getRot();
        int Grün = eintragListe.get(position).getGrün();
        int Blau = eintragListe.get(position).getBlau();
        float alpha = new Globale_Funktionen().Helligkeit_Berechnen(Rot,Grün,Blau);
        Color backgroundTint = Color.valueOf(Color.rgb(Rot,Grün,Blau));
        int foregroundTint;
        if((int) alpha >= 255 / 2) {
            foregroundTint = Color.BLACK;
        } else {
            foregroundTint = Color.WHITE;
        }
        Circle.setTextColor(foregroundTint);
        Circle.setBackgroundTintList(ColorStateList.valueOf(backgroundTint.toArgb()));
        Circle.setText(eintragListe.get(position).getBeschreibung().substring(0,1).toString());

        return view;
    }


    @Override
    public Filter getFilter() {
        return null;
    }
}
