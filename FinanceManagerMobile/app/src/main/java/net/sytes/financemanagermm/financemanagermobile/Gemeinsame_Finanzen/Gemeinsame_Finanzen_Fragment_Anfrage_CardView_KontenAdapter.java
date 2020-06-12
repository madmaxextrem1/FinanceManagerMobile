package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.daimajia.swipe.SwipeLayout;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.CustomAlertDialog;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.LinkedHashMap_BaseAdapter;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.LinkedHashMap_BaseSwipeAdapter;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.FinanceManagerData_Edited_Interface;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Verwaltung_Konten_Detailansicht;

import java.util.LinkedHashMap;

public class Gemeinsame_Finanzen_Fragment_Anfrage_CardView_KontenAdapter extends LinkedHashMap_BaseAdapter<Integer, Konto> {

    public Gemeinsame_Finanzen_Fragment_Anfrage_CardView_KontenAdapter(Context context, LinkedHashMap<Integer, Konto> kontoMap) {
        super(context, kontoMap);
    }

    public Gemeinsame_Finanzen_Fragment_Anfrage_CardView_KontenAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gemeinsame_finanzen_fragment_anfrage_cardview_konto_adapter_kontoitem, parent, false);
        }

        Konto konto = getItem(position);

        TextView textView = (TextView) convertView.findViewById(R.id.Konto_Titel);
        ImageView kontoimage = convertView.findViewById(R.id.KontoImage);
        TextView erstelltAm = (TextView) convertView.findViewById(R.id.Datum);
        TextView bestand = (TextView) convertView.findViewById(R.id.Bestand);

        textView.setText(konto.getKontoTitel());
        erstelltAm.setText("Erstellt am: " + GlobaleVariablen.getInstance().getDE_DateFormat().format(konto.getDatumAnfangsbestand()));
        bestand.setText(Globale_Funktionen.getCurrencyFormatWith0Digits().format(konto.getAnfangsbestand()));
        kontoimage.setImageDrawable(konto.getKontoArt().getKonto_Image());

        View Divider = (View) convertView.findViewById(R.id.Divider);
        if(position == getLinkedMap().size() - 1)
            Divider.setVisibility(View.INVISIBLE);

        return convertView;
    }
}
