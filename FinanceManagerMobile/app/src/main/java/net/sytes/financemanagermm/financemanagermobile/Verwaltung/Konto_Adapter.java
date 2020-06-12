package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.LinkedHashMap_BaseAdapter;

import java.util.LinkedHashMap;

public class Konto_Adapter extends LinkedHashMap_BaseAdapter<Integer, Konto> {

    public Konto_Adapter(Context context) {
        super(context);
    }

    public Konto_Adapter(Context context, LinkedHashMap<Integer, Konto> kontoMap) {
        super(context, kontoMap);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.neue_buchung_konto_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.Konto_Titel);
        textView.setText(getItem(position).getKontoTitel());
        ImageView kontoimage = convertView.findViewById(R.id.KontoImage);
        kontoimage.setImageDrawable(getItem(position).getKontoArt().getKonto_Image());

        return convertView;
    }
}
