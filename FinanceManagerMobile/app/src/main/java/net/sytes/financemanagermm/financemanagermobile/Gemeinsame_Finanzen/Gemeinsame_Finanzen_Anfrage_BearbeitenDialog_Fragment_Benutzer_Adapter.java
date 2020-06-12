package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;

import com.xw.repo.BubbleSeekBar;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.CustomAlertDialog;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.LinkedHashMap_BaseAdapter;

import java.util.LinkedHashMap;

public class Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Benutzer_Adapter extends LinkedHashMap_BaseAdapter<String, KooperationAnfrageBenutzer> {
    private boolean distributeEven;

    Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Benutzer_Adapter(Context context, boolean distributeEven) {
        super(context);
        this.distributeEven = distributeEven;
    }

    Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Benutzer_Adapter(Context context, boolean distributeEven, LinkedHashMap<String, KooperationAnfrageBenutzer> benutzerMap) {
        super(context, benutzerMap);
        this.distributeEven = distributeEven;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                KooperationAnfrageBenutzer Eintrag = getItem(position);
                if (view == null) {
                    LayoutInflater inflater = (LayoutInflater) getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.gemeinsame_finanzen_anfrage_neueanfrage_dialog_fragment_benutzer_lvpersonenitem, parent, false);
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                TextView txtBenutzername = (TextView) view.findViewById(R.id.Benutzername);
                TextView txtMail = (TextView) view.findViewById(R.id.Mail);

                TextView lblVerteilung = view.findViewById(R.id.Verteilung);
                AppCompatImageButton btnLöschen = view.findViewById(R.id.btnLöschen);

                BubbleSeekBar sldVerteilung = view.findViewById(R.id.sldVerteilung);
                ListView parentListView = (ListView) parent;
                parentListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        sldVerteilung.correctOffsetWhenContainerOnScrolling();
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        sldVerteilung.correctOffsetWhenContainerOnScrolling();
                    }
                });

                /*
                sldVerteilung.setLabelFormatter(new Slider.LabelFormatter() {
                    @NonNull
                    @Override
                    public String getFormattedValue(float value) {
                        return Globale_Funktionen.getPercentFormatWith0Digits().format(value);
                    }
                });
                sldVerteilung.addOnChangeListener(new Slider.OnChangeListener() {
                    @Override
                    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                        Eintrag.setVerteilung(value);
                        lblVerteilung.setText(Globale_Funktionen.getPercentFormatWith0Digits().format(Eintrag.getVerteilung()));
                    }
                });
                sldVerteilung.setValue((float) Eintrag.getVerteilung());
                */

                System.out.println(Eintrag.getVerteilung());

                sldVerteilung.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                    @Override
                    public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                        Eintrag.setVerteilung(progressFloat / 100);
                        lblVerteilung.setText(Globale_Funktionen.getPercentFormatWith0Digits().format(Eintrag.getVerteilung()));
                    }

                    @Override
                    public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

                    }

                    @Override
                    public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                    }
                });
                sldVerteilung.setProgress((float) Eintrag.getVerteilung() * 100);
                sldVerteilung.setEnabled(!distributeEven);


                lblVerteilung.setText(Globale_Funktionen.getPercentFormatWith0Digits().format(Eintrag.getVerteilung()));

                txtBenutzername.setText(Eintrag.getBenutzername());
                txtMail.setText(Eintrag.getMail());

                btnLöschen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomAlertDialog confirmationDialog = new CustomAlertDialog(getContext(), "Entfernen", "Wollen Sie den Benutzer wirklich entfernen?", "Entfernen", "Abbrechen");
                        confirmationDialog.setOkButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getLinkedMap().remove(Eintrag.getMail());
                                if (distributeEven) distributeEven();
                                notifyDataSetChanged();
                                confirmationDialog.dismiss();
                            }
                        });
                        confirmationDialog.show();
                    }
                });

                return view;
    }

    public void setDistributeEven(boolean distributeEven) {
        this.distributeEven = distributeEven;
        if (distributeEven) distributeEven();
        notifyDataSetChanged();
    }

    public void distributeEven() {
        double distributionValue = 1.0 / getCount();
        for (KooperationAnfrageBenutzer user : getLinkedMap().values()) {
            user.setVerteilung(distributionValue);
        }
        notifyDataSetChanged();
    }
}
