package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.LinkedHashMap_BaseAdapter;

import java.util.LinkedHashMap;

public class Kooperation_Adapter extends LinkedHashMap_BaseAdapter<Integer, Kooperation> {

    public Kooperation_Adapter(Context context) {
        super(context);
    }

    public Kooperation_Adapter(Context context, LinkedHashMap<Integer, Kooperation> kooperationMap) {
        super(context, kooperationMap);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gemeinsame_finanzen_kooperation_listitem, parent, false);
        }

        TextView titelLabel = (TextView) convertView.findViewById(R.id.Titel);


        TextView benutzerLabel = (TextView) convertView.findViewById(R.id.Benutzer);
        if(getItem(position).getIdentifier() == 0) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) titelLabel.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            titelLabel.setLayoutParams(layoutParams);
            titelLabel.setText("Keine Kooperation");
            benutzerLabel.setVisibility(View.INVISIBLE);
        } else {
            titelLabel.setText(getItem(position).getBeschreibung());
            StringBuffer stringBuffer = new StringBuffer();

            getItem(position).getBenutzerAuswahlListe().forEach(benutzer -> {
                stringBuffer.append(benutzer.getBenutzername());
                stringBuffer.append(", ");
            });

            benutzerLabel.setText(stringBuffer.substring(0, stringBuffer.length() - 2));
        }
        return convertView;
    }
}
