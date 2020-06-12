package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.ArrayList;

public class Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Adapter extends BaseAdapter {
    private ArrayList<KooperationAnfrageBenutzer> eintragListe;
    private Context context;
    private Gemeinsame_Finanzen_Anfrage_NeueAnfrage_OnPersonenItemClickListener onPersonenItemClickListener;

    public Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Adapter(Context context,
                                                                    ArrayList<KooperationAnfrageBenutzer> BenutzerListe,
                                                                    Gemeinsame_Finanzen_Anfrage_NeueAnfrage_OnPersonenItemClickListener onPersonenItemClickListener) {
        super();
        this.eintragListe = BenutzerListe;
        this.context = context;
        this.onPersonenItemClickListener = onPersonenItemClickListener;
    }

    public ArrayList<KooperationAnfrageBenutzer> getEintragListe() {
        return eintragListe;
    }

    @Override
    public int getCount() {
        return eintragListe.size();
    }

    @Override
    public KooperationAnfrageBenutzer getItem(int position) {
        return eintragListe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return eintragListe.get(position).getBenutzerID();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        KooperationAnfrageBenutzer Eintrag = getEintragListe().get(position);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.gemeinsame_finanzen_anfrage_neueanfrage_dialog_lvpersonenitem, parent, false);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onPersonenItemClickListener.onPersonItemClicked(position, eintragListe.get(position), convertView);
            }
        });

        TextView txtBenutzername = (TextView) view.findViewById(R.id.Benutzername);
        TextView txtMail = (TextView) view.findViewById(R.id.Mail);
        ImageView StatusImage = (ImageView) view.findViewById(R.id.StatusImage);
        if(eintragListe.get(position).getBenutzerID() == 0) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.addRule(RelativeLayout.END_OF,R.id.StatusImage);
            txtBenutzername.setLayoutParams(params);
            txtMail.setVisibility(View.GONE);
            txtBenutzername.setText("Benutzer hinzufügen");
            txtBenutzername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            StatusImage.setImageResource(R.drawable.ic_add_neueanfragedialog);
        } else {
            txtBenutzername.setText(eintragListe.get(position).getBenutzername());
            txtMail.setText(eintragListe.get(position).getMail());
            txtMail.setVisibility(View.VISIBLE);
            StatusImage.setImageResource(R.drawable.ic_user_neueanfragedialog);
        }

        return view;
    }


}
