package net.sytes.financemanagermm.financemanagermobile.Hauptmenu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Hauptmenu_Fragment_Home_KontenRecyclerViewAdapter extends RecyclerView.Adapter  {
    private LinkedHashMap<Integer, Konto> linkedHashMap;
    private Context context;
    private View view;

    public Hauptmenu_Fragment_Home_KontenRecyclerViewAdapter(Context context, LinkedHashMap<Integer, Konto> linkedHashMap) {
        this.context = context;
        this.linkedHashMap = linkedHashMap;
    }

    public Konto getItem(int position) {
        int i = 0;
        System.out.println(position);
        System.out.println("Size: " + linkedHashMap.size());
        for(Integer key:linkedHashMap.keySet()) {
            if(i ==position) return linkedHashMap.get(key);
            i++;
        }
        return null;
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
        Konto eintrag = getItem(position);

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
        mHolder.KontoTitel.setText(getItem(position).getKontoTitel());
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        String sBestand = formatter.format(getItem(position).getBestand());
        String sSummeBewegungen = formatter.format(getItem(position).getSummeBewegungenAktMonat());
        mHolder.KontoBestand.setText(sBestand);
        mHolder.KontoEntwicklung.setText("Anzahl Bewegungen: " +
                eintrag.getAnzahlBewegungenAktMonat() + " (" +
                 sSummeBewegungen + ")");
    }

    @Override
    public int getItemCount() {
        return linkedHashMap.size();
    }
    public LinkedHashMap<Integer, Konto> getLinkedHashMap() {
        return linkedHashMap;
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