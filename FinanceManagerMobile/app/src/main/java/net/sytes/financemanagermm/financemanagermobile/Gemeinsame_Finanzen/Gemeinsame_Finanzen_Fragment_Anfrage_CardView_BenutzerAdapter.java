package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.LinkedHashMap_BaseAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Gemeinsame_Finanzen_Fragment_Anfrage_CardView_BenutzerAdapter extends LinkedHashMap_BaseAdapter<String, KooperationAnfrageBenutzer> {
    private Integer ErstellerID;

    public Gemeinsame_Finanzen_Fragment_Anfrage_CardView_BenutzerAdapter(Context context, LinkedHashMap<String,KooperationAnfrageBenutzer> benutzerMap, int ErstellerID) {
        super(context, benutzerMap);
        this.ErstellerID = ErstellerID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.gemeinsame_finanzen_fragment_anfrage_lvpersonenitem, parent, false);
        }

        View Divider = (View) view.findViewById(R.id.Divider);
        if(position == getLinkedMap().size() - 1)
            Divider.setVisibility(View.INVISIBLE);

        TextView txtBenutzername = (TextView) view.findViewById(R.id.Benutzername);
        TextView txtErsteller = (TextView) view.findViewById(R.id.Benutzername_Ersteller);
        TextView txtVerteilung = view.findViewById(R.id.Verteilung);
        BubbleSeekBar sldVerteilung = view.findViewById(R.id.sldVerteilung);

        txtVerteilung.setText(Globale_Funktionen.getPercentFormatWith0Digits().format(getItem(position).getVerteilung()));
        sldVerteilung.setProgress((float) getItem(position).getVerteilung() * 100);

        if(getItem(position).getBenutzerID() == GlobaleVariablen.getInstance().getUserId()) {
            txtBenutzername.setText("Du");
            if(getItem(position).getBenutzerID() == ErstellerID) {
                txtErsteller.setVisibility(View.VISIBLE);
            } else {
                txtErsteller.setVisibility(View.INVISIBLE);
            }
        } else {
            txtBenutzername.setText(getItem(position).getBenutzername());
            if(getItem(position).getBenutzerID() == ErstellerID) {
                txtErsteller.setVisibility(View.VISIBLE);
            } else {
                txtErsteller.setVisibility(View.INVISIBLE);
            }
        }

        TextView txtMail = (TextView) view.findViewById(R.id.Mail);
        txtMail.setText(getItem(position).getMail());
        ImageView StatusImage = view.findViewById(R.id.StatusImage);

        switch (getItem(position).getStatus()) {
            case OFFEN:
                StatusImage.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.colorLightGray)));
                break;
            case BESTÄTIGT:
            case KOOPERATION:
                StatusImage.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.ActiveKooperation)));
                break;
            case ABGELEHNT:
                StatusImage.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.betrag_negativ)));
                break;
        }

        StatusImage.setImageDrawable(getItem(position).getStatus().getStatusImage());

        return view;
    }


}
