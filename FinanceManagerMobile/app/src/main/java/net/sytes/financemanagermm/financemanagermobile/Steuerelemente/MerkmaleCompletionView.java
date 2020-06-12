package net.sytes.financemanagermm.financemanagermobile.Steuerelemente;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.R;

public class MerkmaleCompletionView extends TokenCompleteTextView<FinanzbuchungToken> {

    public MerkmaleCompletionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected View getViewForObject(FinanzbuchungToken Merkmal) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.buchung_hinzufuegen_merkmal, (ViewGroup) getParent(), false);
        view.setText(Merkmal.getBeschreibung());

        return view;
    }

    @Override
    public boolean shouldIgnoreToken(FinanzbuchungToken Token) {
        return getObjects().contains(Token);
    }

    @Override
    protected FinanzbuchungToken defaultObject(String completionText) {

        //Stupid simple example of guessing if we have an email or not

        int index = completionText.indexOf('@');

        if (index == -1) {

            //return new FinanzbuchungToken(completionText, completionText.replace(" ", "") + "@example.com");

        } else {

            //return new FinanzbuchungToken(completionText.substring(0, index), completionText);

        }

        return null;
    }
}
