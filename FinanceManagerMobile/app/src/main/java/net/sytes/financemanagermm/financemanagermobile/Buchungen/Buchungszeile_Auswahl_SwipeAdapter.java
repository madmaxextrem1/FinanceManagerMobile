package net.sytes.financemanagermm.financemanagermobile.Buchungen;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import androidx.core.content.ContextCompat;

public class Buchungszeile_Auswahl_SwipeAdapter extends BaseSwipeAdapter {
    private ArrayList<FinanzbuchungPosition> eintragListe;
    private Context context;
    private ListView parent;
    private final Buchungszeile_Auswahl_Eintrag_ItemClickListener BuchungszeileItemClickListener;

    public Buchungszeile_Auswahl_SwipeAdapter(Context context, ListView parent, Buchungszeile_Auswahl_Eintrag_ItemClickListener buchungszeile_auswahl_eintrag_itemClickListener) {
        super();
        this.eintragListe = new ArrayList<FinanzbuchungPosition>();
        this.parent = parent;
        this.context = context;
        this.BuchungszeileItemClickListener = buchungszeile_auswahl_eintrag_itemClickListener;
    }

    public ArrayList<FinanzbuchungPosition> getEintragListe() {
        return eintragListe;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.buchung_buchungszeile_lvItem_SwipeLayout;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.buchung_hinzufuegen_lvbuchungszeilen_lvitem, null);
    }
    public double getGesamtsumme() {
        Double Summe = 0.00;
        for(FinanzbuchungPosition Eintrag:getEintragListe()) {
            Summe += Eintrag.getBetrag();
        }
        return Summe;
    }
    @Override
    public void fillValues(int position, View convertView) {
        View view = convertView;
        SwipeLayout swipeLayout = view.findViewById(getSwipeLayoutResourceId(position));

        TextView txtKategorie = (TextView) view.findViewById(R.id.Kategorie);
        TextView txtÜberKategorie = (TextView) view.findViewById(R.id.Überkategorie);
        txtKategorie.setText(eintragListe.get(position).getUnterkategorie());
        txtÜberKategorie.setText(eintragListe.get(position).getÜberkategorie());
        TextView txtBetrag = (TextView) view.findViewById(R.id.Betrag);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        formatter.setCurrency(Currency.getInstance(Locale.getDefault()));


        String sBetrag = formatter.format(eintragListe.get(position).getBetrag());
        Float Betrag = Float.parseFloat(eintragListe.get(position).getBetrag().toString());
        if(Betrag >= 0) {
            txtBetrag.setTextColor(Color.GREEN);
        } else {
            txtBetrag.setTextColor(Color.RED);
         }
        txtBetrag.setText(sBetrag);
        TextView imgCircle = (TextView) view.findViewById(R.id.Kategorie_Image);
        RelativeLayout bottomLayout = (RelativeLayout) view.findViewById(R.id.bottom_wrapper);
        RelativeLayout topLayout = (RelativeLayout) view.findViewById(R.id.topView);
        View deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setTag(eintragListe.get(position));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEintragListe().remove(position);
                swipeLayout.close(false,true);
                notifyDataSetChanged();
            }
        });
        int Rot = getEintragListe().get(position).getRot();
        int Grün = getEintragListe().get(position).getGrün();
        int Blau = getEintragListe().get(position).getBlau();
        float alpha = new Globale_Funktionen().Helligkeit_Berechnen(Rot,Grün,Blau);
        Color backgroundTint = Color.valueOf(Color.rgb(Rot,Grün,Blau));
        int foregroundTint;
        if(Integer.valueOf((int) alpha) >= Integer.valueOf(255/2)) {
            foregroundTint = Color.BLACK;
        } else {
            foregroundTint = Color.WHITE;
        }
        imgCircle.setTextColor(foregroundTint);
        imgCircle.setBackgroundTintList(ColorStateList.valueOf(backgroundTint.toArgb()));
        imgCircle.setText(getEintragListe().get(position).getUnterkategorie().substring(0,1).toString());
        Drawable drawable = ContextCompat.getDrawable(context,R.drawable.hauptmenu_buchungen_lv_item_circletextview);
        imgCircle.setBackground(drawable);
        }

    @Override
    public int getCount() {
        return eintragListe.size();
    }

    @Override
    public FinanzbuchungPosition getItem(int position) {
        return eintragListe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return eintragListe.get(position).getId();
    }
}
