package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.LinkedHashMap_BaseAdapter;

import java.util.LinkedHashMap;

public class Verwaltung_Token_Übersicht_TokenAdapter extends LinkedHashMap_BaseAdapter<Integer, FinanzbuchungToken> {
    public Verwaltung_Token_Übersicht_TokenAdapter(Context context, LinkedHashMap<Integer, FinanzbuchungToken> linkedMap) {
        super(context, linkedMap);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.verwaltung_token_uebersicht_token_item, parent, false);
        }

        final FinanzbuchungToken token = getItem(position);

        TextView lblBeschreibung =  view.findViewById(R.id.Beschreibung);
        TextView lblTyp = view.findViewById(R.id.Typ);

        lblBeschreibung.setText(token.getBeschreibung());
        lblTyp.setText("Persönlich");

        // define a click listener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                Verwaltung_Token_Übersicht_Bearbeiten_Dialog dialog = new Verwaltung_Token_Übersicht_Bearbeiten_Dialog(getContext(), KontoItem, new FinanceManagerData_Edited_Interface<Konto>() {
                    @Override
                    public void onDataEdited(Konto data, boolean created) {
                        notifyDataSetChanged();
                    }
                }, true);
                dialog.show(fragmentManager, "dialog_edit_token");
            }
        });

        return view;
    }
}
