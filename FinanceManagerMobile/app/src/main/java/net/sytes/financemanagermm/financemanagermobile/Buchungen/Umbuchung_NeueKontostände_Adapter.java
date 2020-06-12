package net.sytes.financemanagermm.financemanagermobile.Buchungen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import java.text.NumberFormat;
import java.util.ArrayList;

public class Umbuchung_NeueKontostände_Adapter extends BaseAdapter implements Filterable {
    private ArrayList<Konto> eintragListe;
    private Context Context;
    private Float Umbuchungsbetrag;

    public Umbuchung_NeueKontostände_Adapter(Context context, Float Umbuchungsbetrag) {
        super();
        this.eintragListe = new ArrayList<Konto>();
        this.Context = context;
        this.Umbuchungsbetrag = Umbuchungsbetrag;
    }

    public ArrayList<Konto> getEintragListe() {
        return eintragListe;
    }

    @Override
    public int getCount() {
        return eintragListe.size();
    }

    @Override
    public Konto getItem(int position) {
        return eintragListe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return eintragListe.get(position).getIdentifier();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) Context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.umbuchung_neuekontostaende_lv_item, parent, false);
        }

        Konto kontoEintrag = getEintragListe().get(position);

        TextView Title = view.findViewById(R.id.Konto_Titel);
        TextView AlterKontostand = view.findViewById(R.id.Konto_AlterBestand);
        TextView Betrag = view.findViewById(R.id.Betrag);
        Title.setText(eintragListe.get(position).getKontoTitel());
        ImageView imgKonto = view.findViewById(R.id.Konto_Image);
        imgKonto.setImageDrawable(kontoEintrag.getKontoArt().getKonto_Image());

        Integer VorfaktorAbbuchung = (position == 0) ? -1 : 1;

        Title.setText(kontoEintrag.getKontoTitel());
        AlterKontostand.setText("Alter Kontostand: " + NumberFormat.getCurrencyInstance().format(kontoEintrag.getBestand()));
        Betrag.setText(NumberFormat.getCurrencyInstance().format(kontoEintrag.getBestand() + VorfaktorAbbuchung * Umbuchungsbetrag));

        return view;
    }


    @Override
    public Filter getFilter() {
        return null;
    }
}
