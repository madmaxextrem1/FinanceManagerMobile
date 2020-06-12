package net.sytes.financemanagermm.financemanagermobile.Hauptmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Finanzbuchung_Buchung;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Konten;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.core.content.ContextCompat;

public class Hauptmenu_Buchungen_Liste_RecyclerViewAdapter extends BaseAdapter {
    private ArrayList<Finanzbuchung_Buchung> eintragListe;
    private Context context;

    public Hauptmenu_Buchungen_Liste_RecyclerViewAdapter(Context context) {
        super();
        this.eintragListe = new ArrayList<Finanzbuchung_Buchung>();
        this.context = context;
    }

    public ArrayList<Finanzbuchung_Buchung> getEintragListe() {
        return eintragListe;
    }

    @Override
    public int getCount() {
        return eintragListe.size();
    }

    @Override
    public Finanzbuchung_Buchung getItem(int position) {
        return eintragListe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return eintragListe.get(position).getId();
    }
    public Konto.KontoArt getKontoArt(int position) {
        return Konten.getKontoById(eintragListe.get(position).getKontoId()).getKontoArt();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Konto konto = Konten.getKontoById(eintragListe.get(position).getKontoId());

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.hauptmenu_buchungen_lv_item, parent, false);
        }
        view.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(parent.getContext(), R.anim.lv_onclick_animation);
                v.startAnimation(hyperspaceJumpAnimation);

            }
        });

        TextView txtBeschreibung = (TextView) view.findViewById(R.id.hauptmenu_buchungen_lv_item_beschreibung);
        txtBeschreibung.setText(eintragListe.get(position).getBeschreibung());
        TextView txtDatum = (TextView) view.findViewById(R.id.hauptmenu_buchungen_lv_item_datum);

        LocalDate date = eintragListe.get(position).getDatum();

        DecimalFormat formatter = new DecimalFormat("#,###,###.00");
        Double Betrag = Double.valueOf(eintragListe.get(position).getBetrag().toString());
        String sBetrag = formatter.format(Betrag);

        TextView txtBetrag = (TextView) view.findViewById(R.id.hauptmenu_buchungen_lv_item_betrag);
        TextView txtKonto = (TextView) view.findViewById(R.id.hauptmenu_buchungen_lv_item_Konto);
        txtBetrag.setText(sBetrag + "€");
        txtKonto.setText(konto.getKontoTitel());
              txtDatum.setText(DateFormat.getDateInstance().format(date));
        ImageView BuchungImage = view.findViewById(R.id.hauptmenu_buchungen_lv_item_circle);

        if (Betrag > 0) {

            txtBetrag.setTextColor(ContextCompat.getColor(context,R.color.fab_Color_Hauptmenu));
        } else { txtBetrag.setTextColor(ContextCompat.getColor(context,R.color.betrag_negativ));}

        BuchungImage.setImageDrawable(konto.getKontoArt().getKonto_Image());

        return view;
    }


}
