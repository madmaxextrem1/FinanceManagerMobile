package net.sytes.financemanagermm.financemanagermobile.Steuerelemente;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class CustomAlertDialog extends Dialog {
    private MaterialButton okButton;
    private MaterialButton cancelButton;

    public CustomAlertDialog(Context context, String Title, String Message, String OkButtonMessage, String CancelButtonMessage, View.OnClickListener OkClickListener, View.OnClickListener CancelClickListener) {
        super(context);
        setContentView(R.layout.gemeinsame_finanzen_fragment_anfrage_alertdialog);

        TextView txtTitel = (TextView) findViewById(R.id.Titel);
        txtTitel.setText(Title);
        TextView txtMessage = (TextView) findViewById(R.id.Message);
        txtMessage.setText(Message);

        okButton = (MaterialButton) findViewById(R.id.btnLöschen);
        okButton.setText(OkButtonMessage);
        okButton.setOnClickListener(OkClickListener);

        cancelButton = (MaterialButton) findViewById(R.id.btnAbbrechen);
        cancelButton.setText(CancelButtonMessage);
        cancelButton.setOnClickListener(CancelClickListener);
    }

    public CustomAlertDialog(Context context, String Title, String Message, String OkButtonMessage, String CancelButtonMessage, View.OnClickListener OkClickListener) {
        super(context);
        setContentView(R.layout.gemeinsame_finanzen_fragment_anfrage_alertdialog);

        TextView txtTitel = (TextView) findViewById(R.id.Titel);
        txtTitel.setText(Title);
        TextView txtMessage = (TextView) findViewById(R.id.Message);
        txtMessage.setText(Message);

        okButton = (MaterialButton) findViewById(R.id.btnLöschen);
        okButton.setText(OkButtonMessage);
        okButton.setOnClickListener(OkClickListener);

        cancelButton = (MaterialButton) findViewById(R.id.btnAbbrechen);
        cancelButton.setText(CancelButtonMessage);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public CustomAlertDialog(Context context, String Title, String Message, String OkButtonMessage, String CancelButtonMessage) {
        super(context);
        setContentView(R.layout.gemeinsame_finanzen_fragment_anfrage_alertdialog);

        TextView txtTitel = (TextView) findViewById(R.id.Titel);
        txtTitel.setText(Title);
        TextView txtMessage = (TextView) findViewById(R.id.Message);
        txtMessage.setText(Message);

        okButton = (MaterialButton) findViewById(R.id.btnLöschen);
        okButton.setText(OkButtonMessage);

        cancelButton = (MaterialButton) findViewById(R.id.btnAbbrechen);
        cancelButton.setText(CancelButtonMessage);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setOkButtonClickListener (View.OnClickListener listener) {
        okButton.setOnClickListener(listener);
    }
}
