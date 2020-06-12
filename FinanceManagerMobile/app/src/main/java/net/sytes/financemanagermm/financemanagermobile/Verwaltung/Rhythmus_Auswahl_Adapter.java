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

public class Rhythmus_Auswahl_Adapter extends BaseAdapter {
    private ArrayList<String> eintragListe;
    private Context context;

    public Rhythmus_Auswahl_Adapter(Context context, ArrayList<String> StringListe) {
        super();
        this.eintragListe = StringListe;
        this.context = context;
    }

    public ArrayList<String> getEintragListe() {
        return eintragListe;
    }

    @Override
    public int getCount() {
        return eintragListe.size();
    }

    @Override
    public String getItem(int position) {
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
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.verwaltung_dauerauftrag_rhythmusitem, parent, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.Rhythmus_Titel);
        textView.setText(eintragListe.get(position));
        ImageView kontoimage = view.findViewById(R.id.Rhythmus_Image);

         switch (eintragListe.get(position)) {
            case "Rhythmus auswählen":
                kontoimage.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_time_blue_24dp,null));
                break;
            case "monatlich":
                kontoimage.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_time_blue_24dp,null));
                break;
            case "Quartal":
                kontoimage.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_time_blue_24dp,null));
                break;
            case "halbjährlich":
                kontoimage.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_time_blue_24dp,null));
                break;
             case "jährlich":
                 kontoimage.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_time_blue_24dp,null));
                 break;
        }
        return view;
    }


}
