package net.sytes.financemanagermm.financemanagermobile.Hauptmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.Finanzbuchung_Buchung;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Konten;
import net.sytes.financemanagermm.financemanagermobile.Helper.DateConversionHelper;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class Hauptmenu_Fragment_Buchungen_Liste_SwipeAdapter extends BaseSwipeAdapter {
    private ArrayList<Finanzbuchung_Buchung> eintragListe;
    private Context context;
    private final Hauptmenu_Fragment_Buchungen_Liste_ItemClickListener BuchungItemClickListener;

    public Hauptmenu_Fragment_Buchungen_Liste_SwipeAdapter(Context context, Hauptmenu_Fragment_Buchungen_Liste_ItemClickListener BuchungItemClickListener, ArrayList<Finanzbuchung_Buchung> buchungListe) {
        super();
        this.eintragListe = buchungListe;
        this.context = context;
        this.BuchungItemClickListener = BuchungItemClickListener;
    }

    public ArrayList<Finanzbuchung_Buchung> getEintragListe() {
        return eintragListe;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.Hauptmenu_Fragment_Buchung_lvItem_SwipeLayout;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.hauptmenu_fragment_buchungen_listitem_swipelayout, null);
    }

    @Override
    public void fillValues(int position, View convertView) {
        View view = convertView;
        SwipeLayout swipeLayout = view.findViewById(getSwipeLayoutResourceId(position));

        swipeLayout.close();
        TextView txtBeschreibung = (TextView) view.findViewById(R.id.hauptmenu_buchungen_lv_item_beschreibung);
        txtBeschreibung.setText(eintragListe.get(position).getBeschreibung());
        TextView txtDatum = (TextView) view.findViewById(R.id.hauptmenu_buchungen_lv_item_datum);

        LocalDate date = eintragListe.get(position).getDatum();
        Konto konto = Konten.getKontoById(eintragListe.get(position).getKontoId());
        DecimalFormat formatter = new DecimalFormat("#,###,###.00");
        Double Betrag = Double.valueOf(eintragListe.get(position).getBetrag().toString());
        String sBetrag = formatter.format(Betrag);

        TextView txtBetrag = (TextView) view.findViewById(R.id.hauptmenu_buchungen_lv_item_betrag);
        TextView txtKonto = (TextView) view.findViewById(R.id.hauptmenu_buchungen_lv_item_Konto);
        String OptionalPlus = "";
        if(Betrag>0) {
            OptionalPlus = "+";
        }
        txtBetrag.setText(OptionalPlus + sBetrag + " €");
        txtKonto.setText(konto.getKontoTitel());
        txtDatum.setText(date.format(DateConversionHelper.getDEDateFormatter()));
        ImageView BuchungImage = view.findViewById(R.id.hauptmenu_buchungen_lv_item_circle);

        if (Betrag > 0) {
            txtBetrag.setTextColor(ContextCompat.getColor(context,R.color.fab_Color_Hauptmenu));
        } else { txtBetrag.setTextColor(ContextCompat.getColor(context,R.color.betrag_negativ));}

        BuchungImage.setImageDrawable(konto.getKontoArt().getKonto_Image());

        RelativeLayout bottomLayout = (RelativeLayout) view.findViewById(R.id.bottom_wrapper);
        RelativeLayout topLayout = (RelativeLayout) view.findViewById(R.id.topView);
        RelativeLayout deleteButton = (RelativeLayout) view.findViewById(R.id.ContainerDelete);
        RelativeLayout editButton = (RelativeLayout) view.findViewById(R.id.ContainerEdit);
        deleteButton.setTag(eintragListe.get(position));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuchungItemClickListener.onBuchungtemClicked(position, eintragListe.get(position),false);
            }
        });
        editButton.setTag(eintragListe.get(position));
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                BuchungItemClickListener.onBuchungtemClicked(position, eintragListe.get(position),true);
            }
        });

    }

    @Override
    public int getCount() {
        return eintragListe.size();
    }

    @Override
    public Object getItem(int position) {
        return eintragListe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return eintragListe.get(position).getId();
    }
}
