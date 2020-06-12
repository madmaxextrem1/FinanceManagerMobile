package net.sytes.financemanagermm.financemanagermobile.Steuerelemente;

import android.app.Dialog;
import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class CustomConfirmationDialog<T> extends Dialog {
    private MaterialButton okButton;
    private RadioGroup rdbGroup;
    private MaterialButton cancelButton;
    private ArrayList<Pair<T, String>> choiceList;
    private ArrayList<MaterialRadioButton> rdbList = new ArrayList<>();

    public CustomConfirmationDialog(Context context, String Title, String OkButtonMessage, String CancelButtonMessage, ArrayList<Pair<T, String >> choiceList, CustomConfirmationDialog_Finished<T> callback) {
        super(context);
        setContentView(R.layout.custom_confirmation_dialog);
        this.choiceList = choiceList;

        TextView txtTitel = (TextView) findViewById(R.id.Titel);
        txtTitel.setText(Title);

        rdbGroup = findViewById(R.id.rdbGroup);
        boolean alreadyChecked = false;
        int i = 1;
        for(Pair<T, String> eintrag:choiceList) {
            MaterialRadioButton rdb = new MaterialRadioButton(getContext());
            rdb.setText(eintrag.second);
            rdb.setTag(eintrag.first);
            rdb.setTextSize(16);
            rdb.setId(i);
            rdb.setHeight(Globale_Funktionen.Integer_dpToPixels(getContext(), 56));
            if(!alreadyChecked) {
                rdb.setChecked(true);
                alreadyChecked = true;
            }

            rdbList.add(rdb);
            rdbGroup.addView(rdb);
            i++;
        }


        okButton = (MaterialButton) findViewById(R.id.btnLöschen);
        okButton.setText(OkButtonMessage);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onCustomConfirationDialog_Finished(evaluateResult());
                dismiss();
            }
        });

        cancelButton = (MaterialButton) findViewById(R.id.btnAbbrechen);
        cancelButton.setText(CancelButtonMessage);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private T evaluateResult () {
        T result = null;
        for(MaterialRadioButton rdb:rdbList) {
            if(rdb.isChecked()) {
                result = (T) rdb.getTag();
                break;
            }
        }
        return result;
    }

    public interface CustomConfirmationDialog_Finished<T> {
        void onCustomConfirationDialog_Finished(T result);
    }
}
