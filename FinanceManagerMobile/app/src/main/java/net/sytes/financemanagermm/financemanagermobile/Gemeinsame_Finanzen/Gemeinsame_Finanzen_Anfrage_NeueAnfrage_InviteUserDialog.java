package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.Calendar;

public class Gemeinsame_Finanzen_Anfrage_NeueAnfrage_InviteUserDialog extends Dialog {
    private TextInputLayout txtEmail;
    private MaterialButton bestätigenButton;
    private Slider sldAusgabenverteilung;
    private TextView lblValue;
    private Gemeinsame_Finanzen_Anfrage_BenutzerAbfragenCallback callback;

    public void setCallback(Gemeinsame_Finanzen_Anfrage_BenutzerAbfragenCallback callback) {
        this.callback = callback;
    }

    public Gemeinsame_Finanzen_Anfrage_NeueAnfrage_InviteUserDialog(Context context, int AnfrageID, boolean gleicheAusgabenverteilung, int countUser) {
        super(context);
        setContentView(R.layout.gemeinsame_finanzen_anfrage_neueanfrage_dialog_invite_user_mail_dialog);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        txtEmail = (TextInputLayout) findViewById(R.id.Email);
        bestätigenButton = (MaterialButton) findViewById(R.id.btnLöschen);
        sldAusgabenverteilung = findViewById(R.id.sldAusgabenVerteilung);
        lblValue = findViewById(R.id.lblValue);

        sldAusgabenverteilung.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                lblValue.setText(Globale_Funktionen.getPercentFormatWith0Digits().format(value));
            }
        });

        if(gleicheAusgabenverteilung) {
            sldAusgabenverteilung.setEnabled(false);
            sldAusgabenverteilung.setValue(1.0f / (countUser + 1));
        } else {
            sldAusgabenverteilung.setValue(0.5f);
        }

        sldAusgabenverteilung.setLabelFormatter(new Slider.LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return Globale_Funktionen.getPercentFormatWith0Digits().format(value);
            }
        });

        bestätigenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    Gemeinsame_Finanzen_Anfrage_BearbeitenDialog.getUserFromServerByEmail(txtEmail.getEditText().getText().toString().trim(), new Gemeinsame_Finanzen_Anfrage_BenutzerAbfragenCallback() {
                        @Override
                        public void onBenutzerSuccessfullyQueried(KooperationAnfrageBenutzer Benutzer) {
                            KooperationAnfrageBenutzer user = Benutzer;
                            if (Benutzer == null) {
                                user = new KooperationAnfrageBenutzer(0, "Unregistrierter Benutzer",
                                        txtEmail.getEditText().getText().toString().toLowerCase().trim(), sldAusgabenverteilung.getValue(), KooperationAnfrageBenutzer.AnfrageBenutzerStatus.OFFEN, Calendar.getInstance().getTime());
                            }
                            user.setVerteilung(sldAusgabenverteilung.getValue());
                            callback.onBenutzerSuccessfullyQueried(user);
                        }
                    });
                }
            }
        });

        MaterialButton abbrechenButton = (MaterialButton) findViewById(R.id.btnAbbrechen);
        abbrechenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public Gemeinsame_Finanzen_Anfrage_NeueAnfrage_InviteUserDialog(Context context, int AnfrageID, boolean gleicheAusgabenverteilung, int countUser, Gemeinsame_Finanzen_Anfrage_BenutzerAbfragenCallback Callback) {
        super(context);
        this.callback = Callback;
        setContentView(R.layout.gemeinsame_finanzen_anfrage_neueanfrage_dialog_invite_user_mail_dialog);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        txtEmail = (TextInputLayout) findViewById(R.id.Email);
        bestätigenButton = (MaterialButton) findViewById(R.id.btnLöschen);
        sldAusgabenverteilung = findViewById(R.id.sldAusgabenVerteilung);
        lblValue = findViewById(R.id.lblValue);

        sldAusgabenverteilung.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                lblValue.setText(Globale_Funktionen.getPercentFormatWith0Digits().format(value));
            }
        });

        if(gleicheAusgabenverteilung) {
            sldAusgabenverteilung.setEnabled(false);
            sldAusgabenverteilung.setValue(1.0f / (countUser + 1));
        } else {
            sldAusgabenverteilung.setValue(0.5f);
        }

        sldAusgabenverteilung.setLabelFormatter(new Slider.LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return Globale_Funktionen.getPercentFormatWith0Digits().format(value);
            }
        });

        bestätigenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    Gemeinsame_Finanzen_Anfrage_BearbeitenDialog.getUserFromServerByEmail(txtEmail.getEditText().getText().toString().trim(), new Gemeinsame_Finanzen_Anfrage_BenutzerAbfragenCallback() {
                        @Override
                        public void onBenutzerSuccessfullyQueried(KooperationAnfrageBenutzer Benutzer) {
                            KooperationAnfrageBenutzer user = Benutzer;
                            if (Benutzer == null) {
                                user = new KooperationAnfrageBenutzer(0, "Unregistrierter Benutzer",
                                        txtEmail.getEditText().getText().toString().toLowerCase().trim(), sldAusgabenverteilung.getValue(), KooperationAnfrageBenutzer.AnfrageBenutzerStatus.OFFEN, Calendar.getInstance().getTime());
                            }
                            user.setVerteilung(sldAusgabenverteilung.getValue());
                            dismiss();
                            Callback.onBenutzerSuccessfullyQueried(user);
                        }
                    });
                }
            }
        });

        MaterialButton abbrechenButton = (MaterialButton) findViewById(R.id.btnAbbrechen);
        abbrechenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private boolean validateInput() {
        if (txtEmail.getEditText().getText().toString().equals("")) {
            txtEmail.setError("Fehlende Email-Adresse");
            return false;
        } else {
            if (txtEmail.getEditText().getText().toString().toLowerCase().trim().equals(GlobaleVariablen.getInstance().getEmail().trim().toLowerCase())) {
                txtEmail.setError("Eigene Email-Adresse nicht akzeptiert");
                return false;
            } else {
                if (!Globale_Funktionen.isValidEmail(txtEmail.getEditText().getText().toString().toLowerCase().trim())) {
                    txtEmail.setError("Ungültige Email-Adresse");
                    return false;
                }
            }
        }
        return true;
    }
}
