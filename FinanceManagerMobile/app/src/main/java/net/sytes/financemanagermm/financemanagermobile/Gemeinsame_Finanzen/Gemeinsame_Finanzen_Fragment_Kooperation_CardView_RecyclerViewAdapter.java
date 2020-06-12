package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.MaterialExpansionPanel.Expandable;
import net.sytes.financemanagermm.financemanagermobile.MaterialExpansionPanel.ExpandingListener;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.LinkedHashMap_BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Gemeinsame_Finanzen_Fragment_Kooperation_CardView_RecyclerViewAdapter extends LinkedHashMap_BaseRecyclerAdapter<Integer, Kooperation>  {
    private View view;

    public Gemeinsame_Finanzen_Fragment_Kooperation_CardView_RecyclerViewAdapter(Context context) {
        super(context);
    }

    public Gemeinsame_Finanzen_Fragment_Kooperation_CardView_RecyclerViewAdapter(
    Context context, LinkedHashMap<Integer, Kooperation> KooperationListe) {
        super(context, KooperationListe);
    }

    @NonNull
    @Override
    public Gemeinsame_Finanzen_Fragment_Kooperation_CardView_RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view =LayoutInflater.from(parent.getContext()).inflate(R.layout.gemeinsame_finanzen_fragment_kooperation_cardviewitem,parent,false);
        return new Gemeinsame_Finanzen_Fragment_Kooperation_CardView_RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        Kooperation KooperationItem = getItem(position);

        // define a click listener
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Resources res = getContext().getResources();
        final int newColor = res.getColor(R.color.colorPrimary, null);

        Gemeinsame_Finanzen_Fragment_Kooperation_CardView_BenutzerAdapter BenutzerAdapter =
                new Gemeinsame_Finanzen_Fragment_Kooperation_CardView_BenutzerAdapter(
                        getContext(), KooperationItem.getBenutzerAuswahlListe(), KooperationItem.getErstellerID());
        mHolder.lvPersonen.setAdapter(BenutzerAdapter);
        BenutzerAdapter.notifyDataSetChanged();

        //mHolder.LeftImage.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        mHolder.Titel.setText(KooperationItem.getBeschreibung());

        mHolder.expandable.setExpandingListener(new ExpandingListener() {
            @Override
            public void onExpanded() {
                int totalHeight = 0;
                View listItemStandard = BenutzerAdapter.getView(0, null, mHolder.lvPersonen);
                listItemStandard.measure(0, 0);
                int maximumHeight = listItemStandard.getMeasuredHeight() * 3;
                for (int i = 0; i < BenutzerAdapter.getCount(); i++) {
                    View listItem = BenutzerAdapter.getView(i, null, mHolder.lvPersonen);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }

                ViewGroup.LayoutParams params = mHolder.lvPersonen.getLayoutParams();
                if(totalHeight <= maximumHeight) {
                    params.height = totalHeight + (mHolder.lvPersonen.getDividerHeight() * (BenutzerAdapter.getCount() - 1));
                } else {
                    params.height = maximumHeight + (mHolder.lvPersonen.getDividerHeight() * (BenutzerAdapter.getCount() - 1));
                }                mHolder.lvPersonen.setLayoutParams(params);
                mHolder.lvPersonen.requestLayout();
            }

            @Override
            public void onCollapsed() {

            }
        });

        if(KooperationItem.getBenutzerAuswahlListe().size() == 2) {
            KooperationBenutzer PartnerEintrag = null;
            for(KooperationBenutzer Eintrag: KooperationItem.getBenutzerAuswahlListe()) {
                if (Eintrag.getBenutzerID() != GlobaleVariablen.getInstance().getUserId()) {
                    PartnerEintrag = Eintrag;
                    break;
                }
            }
            mHolder.SinglePartner.setText("mit " + PartnerEintrag.getBenutzername());
        } else {
            mHolder.SinglePartner.setText("mit " + String.valueOf(KooperationItem.getBenutzerAuswahlListe().size() - 1) + " weiteren Personen");
        }
        mHolder.Erstelldatum.setText("Erstellt am: " + GlobaleVariablen.getInstance().getDE_DateFormat().format(KooperationItem.getErstelldatum()));

        //Übersichtsseite der Kooperation aufrufen
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Gemeinsame_Finanzen_Kooperation.class);
                intent.putExtra("Kooperation", KooperationItem);
                getContext().startActivity(intent);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView LeftImage;
        private TextView Titel;
        private TextView SinglePartner;
        private TextView Erstelldatum;
        private ListView lvPersonen;
        private Expandable expandable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            LeftImage = itemView.findViewById(R.id.LeftImage);
            Titel = itemView.findViewById(R.id.Titel);
            SinglePartner = itemView.findViewById(R.id.PartnerHeader);
            Erstelldatum = itemView.findViewById(R.id.Erstelldatum);
            lvPersonen = itemView.findViewById(R.id.lvPersonen);
            expandable = itemView.findViewById(R.id.TitleLayout);
        }
    }
  }