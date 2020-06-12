package net.sytes.financemanagermm.financemanagermobile.Steuerelemente;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokenautocomplete.FilteredArrayAdapter;

import java.util.ArrayList;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.R;


public class Token_Auswahl_Adapter extends FilteredArrayAdapter<FinanzbuchungToken> {

    @LayoutRes
    private int layoutId;
    private ArrayList<FinanzbuchungToken> eintragListe;
    private Context context;
    private int viewResourceId;


    public Token_Auswahl_Adapter(Context context, @LayoutRes int layoutId, ArrayList<FinanzbuchungToken> MerkmalListe) {
        super(context, layoutId, MerkmalListe);
        this.eintragListe = MerkmalListe;
        this.context = context;
        this.layoutId = layoutId;

    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = l.inflate(R.layout.buchung_hinzufuegen_token_spinnerlayout, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.Buchung_Hinzufügen_Token_Spinnerlayout_Textview)).setText(eintragListe.get(position).getBeschreibung());

        return convertView;
        }

/*    public void disableAllTokens() {
        for (int i = 0; i < eintragListe.size(); i++) {
        getView.setEnabled(false);
        }
    }*/

    public FinanzbuchungToken getTokenById(int TokenID) {
        FinanzbuchungToken Eintrag = null;
        for (int i = 0; i < eintragListe.size(); i++) {
            if(getItem(i).getId() ==TokenID) {
                Eintrag = getItem(i);
                break;
            }
        }
        return Eintrag;
    }
    @Override
    protected boolean keepObject(FinanzbuchungToken Merkmal, String mask) {

        mask = mask.toLowerCase();

        return Merkmal.getBeschreibung().toLowerCase().startsWith(mask);

    }

}