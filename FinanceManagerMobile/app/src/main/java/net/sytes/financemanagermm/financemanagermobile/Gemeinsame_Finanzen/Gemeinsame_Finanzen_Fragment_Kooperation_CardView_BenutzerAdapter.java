package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.ArrayList;

public class Gemeinsame_Finanzen_Fragment_Kooperation_CardView_BenutzerAdapter extends BaseAdapter {
    private ArrayList<KooperationBenutzer> eintragListe;
    private Context context;
    private Integer ErstellerID;

    public Gemeinsame_Finanzen_Fragment_Kooperation_CardView_BenutzerAdapter(Context context, ArrayList<KooperationBenutzer> BenutzerListe, Integer ErstellerID) {
        super();
        this.eintragListe = BenutzerListe;
        this.context = context;
        this.ErstellerID = ErstellerID;
    }

    public ArrayList<KooperationBenutzer> getEintragListe() {
        return eintragListe;
    }

    @Override
    public int getCount() {
        return eintragListe.size();
    }

    @Override
    public KooperationBenutzer getItem(int position) {
        return eintragListe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.gemeinsame_finanzen_fragment_kooperation_lvpersonenitem, parent, false);
        }

        KooperationBenutzer benutzerEintrag = eintragListe.get(position);
        TextView txtBenutzername = (TextView) view.findViewById(R.id.Benutzername);
        TextView txtErsteller = (TextView) view.findViewById(R.id.Benutzername_Ersteller);
        View Divider = (View) view.findViewById(R.id.Divider);
        TextView txtMail = (TextView) view.findViewById(R.id.Mail);
        TextView lblBeitrittsdatum = (TextView) view.findViewById(R.id.Beitrittsdatum);
        ImageView StatusImage = view.findViewById(R.id.StatusImage);


        //beim letzten Item keinen Divider mehr anzeigen
        if(position == eintragListe.size() - 1)
            Divider.setVisibility(View.INVISIBLE);

        //Ersteller feststellen und anzeigen
        if(benutzerEintrag.getBenutzerID() == GlobaleVariablen.getInstance().getUserId()) {
            txtBenutzername.setText("Du");
            if(benutzerEintrag.getBenutzerID() == ErstellerID) {
                txtErsteller.setVisibility(View.VISIBLE);
            } else {
                txtErsteller.setVisibility(View.INVISIBLE);
            }
        } else {
            txtBenutzername.setText(benutzerEintrag.getBenutzername());
            if(benutzerEintrag.getBenutzerID() == ErstellerID) {
                txtErsteller.setVisibility(View.VISIBLE);
            } else {
                txtErsteller.setVisibility(View.INVISIBLE);
            }
        }

        //Beitrittsdatum und Mail-Adresse anzeigen
        lblBeitrittsdatum.setText("Beigetreten: " + GlobaleVariablen.getInstance().getDE_DateFormat().format(benutzerEintrag.getBeitrittsdatum()));
        txtMail.setText(benutzerEintrag.getMail());

        //Icon je nach Status anzeigen
        if(benutzerEintrag.getAktiv()) {
            StatusImage.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.ActiveKooperation)));
            StatusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_white_24dp,null));
        } else if(!benutzerEintrag.getAktiv() && !benutzerEintrag.getGelöscht()) {
            StatusImage.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.colorLightGray)));
            StatusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_white_24dp,null));
        } else if(benutzerEintrag.getGelöscht()) {
            StatusImage.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.betrag_negativ)));
            StatusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_delete_white_24dp,null));
        }

        return view;
    }


}
