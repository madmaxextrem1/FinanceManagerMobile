package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Finanzbuchung_Buchung;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Konten;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.LinkedHashMap_BaseRecyclerAdapter;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Locale;

public class Gemeinsame_Finanzen_Kooperation_Fragment_Home_BenutzerBilanzen_Adapter extends LinkedHashMap_BaseRecyclerAdapter<Integer, Finanzbuchung_Buchung> {
    private View view;

    public Gemeinsame_Finanzen_Kooperation_Fragment_Home_BenutzerBilanzen_Adapter(Context context, LinkedHashMap<Integer, Finanzbuchung_Buchung> linkedHashMap) {
        super(context, linkedHashMap);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view =LayoutInflater.from(parent.getContext()).inflate(R.layout.hauptmenu_fragment_home_kontencardviewitem,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        Konto eintrag = Konten.getKontoById(getItem(position).getKontoId());

        if(eintrag.getSummeBewegungenAktMonat() > 0) {
            mHolder.KontoEntwicklungImage.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_arrow_upward_white_24dp,null));
        }
        if(eintrag.getSummeBewegungenAktMonat() < 0) {
            mHolder.KontoEntwicklungImage.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_arrow_downward_white_24dp,null));
        }
        if(eintrag.getSummeBewegungenAktMonat() == 0) {
            mHolder.KontoEntwicklungImage.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_arrow_forward_white_24dp,null));
        }

        mHolder.KontoArtImage.setImageDrawable(eintrag.getKontoArt().getKonto_Image());
        mHolder.KontoTitel.setText(eintrag.getKontoTitel());
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        String sBestand = formatter.format(eintrag.getBestand());
        String sSummeBewegungen = formatter.format(eintrag.getSummeBewegungenAktMonat());
        mHolder.KontoBestand.setText(sBestand);
        mHolder.KontoEntwicklung.setText("Anzahl Bewegungen: " +
                eintrag.getAnzahlBewegungenAktMonat() + " (" +
                 sSummeBewegungen + ")");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView KontoArtImage;
        private ImageView KontoEntwicklungImage;
        private TextView KontoTitel;
        private TextView KontoBestand;
        private TextView KontoEntwicklung;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            KontoArtImage = itemView.findViewById(R.id.KontoArtImage);
            KontoEntwicklungImage = itemView.findViewById(R.id.KontoEntwicklungImage);
            KontoTitel = itemView.findViewById(R.id.KontoTitel);
            KontoBestand = itemView.findViewById(R.id.KontoBestand);
            KontoEntwicklung = itemView.findViewById(R.id.KontoEntwicklung);
        }
    }
  }