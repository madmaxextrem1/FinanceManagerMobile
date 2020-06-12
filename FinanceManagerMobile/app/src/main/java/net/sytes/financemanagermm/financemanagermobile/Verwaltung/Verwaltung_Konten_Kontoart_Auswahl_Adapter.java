package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.ArrayList;
import java.util.Objects;

public class Verwaltung_Konten_Kontoart_Auswahl_Adapter extends BaseAdapter {
    private ArrayList<Konto.KontoArt> eintragListe;
    private Context Context;

    public Verwaltung_Konten_Kontoart_Auswahl_Adapter(Context context, ArrayList<Konto.KontoArt> KontoArt_Liste) {
        super();
        this.eintragListe = KontoArt_Liste;
        this.Context = context;
    }

    public ArrayList<Konto.KontoArt> getEintragListe() {
        return eintragListe;
    }

    @Override
    public int getCount() {
        return eintragListe.size();
    }

    @Override
    public Konto.KontoArt getItem(int position) {
        return eintragListe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) Context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = Objects.requireNonNull(inflater).inflate(R.layout.verwaltung_konten_detailansicht_kontoartitem, parent, false);
        }

        Konto.KontoArt eintrag = eintragListe.get(position);
        TextView textView = (TextView) view.findViewById(R.id.KontoArt_Titel);
        textView.setText(eintrag.toString());
        ImageView kontoimage = view.findViewById(R.id.KontoArt_Image);
        kontoimage.setImageDrawable(eintrag.getKonto_Image());

        return view;
    }

}
