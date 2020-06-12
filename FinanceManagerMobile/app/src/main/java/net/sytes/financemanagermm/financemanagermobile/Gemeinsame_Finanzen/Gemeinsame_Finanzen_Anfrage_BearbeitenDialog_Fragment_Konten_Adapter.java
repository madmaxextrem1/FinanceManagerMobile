package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.daimajia.swipe.SwipeLayout;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.CustomAlertDialog;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.LinkedHashMap_BaseSwipeAdapter;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.FinanceManagerData_Edited_Interface;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Verwaltung_Konten_Detailansicht;

import java.util.LinkedHashMap;

public class Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Konten_Adapter extends LinkedHashMap_BaseSwipeAdapter<Integer, Konto> {

    public Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Konten_Adapter(Context context, int LayoutId, int SwipeLayoutId) {
        super(context, LayoutId, SwipeLayoutId);
    }

    public Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Konten_Adapter(Context context, int LayoutId, int SwipeLayoutId, LinkedHashMap<Integer, Konto> kontoMap) {
        super(context, LayoutId, SwipeLayoutId, kontoMap);
    }

    @Override
    public void fillValues(int position, View convertView) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(getLayoutId(), null, false);
        }

        SwipeLayout swipeLayout = convertView.findViewById(getSwipeLayoutResourceId(position));

        Konto konto = getItem(position);

        TextView textView = (TextView) convertView.findViewById(R.id.Konto_Titel);
        ImageView kontoimage = convertView.findViewById(R.id.KontoImage);
        TextView erstelltAm = (TextView) convertView.findViewById(R.id.Datum);
        TextView bestand = (TextView) convertView.findViewById(R.id.Bestand);
        ImageView deleteButton = convertView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialog confirmationDialog = new CustomAlertDialog(getContext(), "Entfernen", "Wollen Sie den Benutzer wirklich entfernen?", "Entfernen", "Abbrechen");
                confirmationDialog.setOkButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getLinkedMap().remove(getItem(position).getIdentifier());
                        swipeLayout.close(false, true);
                        notifyDataSetChanged();
                        confirmationDialog.dismiss();
                    }
                });
                confirmationDialog.show();
            }
        });

        textView.setText(konto.getKontoTitel());
        erstelltAm.setText("Erstellt am: " + GlobaleVariablen.getInstance().getDE_DateFormat().format(konto.getDatumAnfangsbestand()));
        bestand.setText(Globale_Funktionen.getCurrencyFormatWith0Digits().format(konto.getAnfangsbestand()));
        kontoimage.setImageDrawable(konto.getKontoArt().getKonto_Image());

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                Verwaltung_Konten_Detailansicht dialog = new Verwaltung_Konten_Detailansicht(getContext(), getItem(position), new FinanceManagerData_Edited_Interface<Konto>() {
                    @Override
                    public void onDataEdited(Konto data, boolean created) {
                        getLinkedMap().put(data.getIdentifier(), data);
                        notifyDataSetChanged();
                    }
                }, true);
                dialog.show(fragmentManager, "fragment_edit_konto");
                return true;
            }
        });
    }
}
