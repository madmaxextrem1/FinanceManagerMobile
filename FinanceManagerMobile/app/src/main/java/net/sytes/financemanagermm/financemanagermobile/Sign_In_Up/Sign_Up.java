package net.sytes.financemanagermm.financemanagermobile.Sign_In_Up;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;

import es.dmoral.toasty.Toasty;

public class Sign_Up extends AppCompatActivity implements View.OnClickListener {
    private MaterialButton btnSignUp;
    private TextInputLayout txtUsername;
    private TextInputLayout txtPassword;
    private TextInputLayout txtPasswordWdh;
    private TextInputLayout txtEmail;
    private AppCompatImageButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        btnSignUp = (MaterialButton) findViewById(R.id.btn_SignUp);
        txtUsername = (TextInputLayout) findViewById(R.id.Benutzername);
        txtPassword = (TextInputLayout) findViewById(R.id.Password);
        txtPasswordWdh = (TextInputLayout) findViewById(R.id.PasswordWdh);
        txtEmail = (TextInputLayout) findViewById(R.id.eMail);
        btnClose = findViewById(R.id.Buchung_Toolbar_btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Eingaben_Überprüfen()) {
                    Toasty.error(Sign_Up.this,getResources().getString(R.string.SignUpFehler), Toast.LENGTH_SHORT,true).show();
                    return;
                }

                HashMap postData = new HashMap();
                postData.put("Passwort", Objects.requireNonNull(txtPassword.getEditText()).getText().toString());
                postData.put("Username", Objects.requireNonNull(txtUsername.getEditText()).getText().toString());
                postData.put("Mail", Objects.requireNonNull(txtEmail.getEditText()).getText().toString());
                PostResponseAsyncTask BenutzerAnlegenTask = new PostResponseAsyncTask(Sign_Up.this, postData, false,new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.equals("success")) {
                            Toasty.success(Sign_Up.this, "Erfolgreich angelegt", Toast.LENGTH_SHORT,true).show();
                            Intent intent = new Intent();
                            intent.putExtra("Username", txtEmail.getEditText().getText().toString());
                            intent.putExtra("Password", txtPassword.getEditText().getText().toString());
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toasty.error(Sign_Up.this, "E-Mail-Adresse bereits vorhanden. Bitte geben Sie eine andere E-Mail-Adresse an.", Toast.LENGTH_SHORT,true).show();
                        }
                    }});
                BenutzerAnlegenTask.execute("http://financemanagermm.sytes.net/fmclient/benutzer_anlegen.php");
            }});
            }

    private boolean Eingaben_Überprüfen() {
        boolean Ergebnis = true;
        if(Objects.requireNonNull(txtUsername.getEditText()).getText().toString().equals("")) {
            Ergebnis = false;
        }

        if(!Globale_Funktionen.isValidPassword(txtPassword.getEditText().getText().toString())) {
            Ergebnis = false;
            txtPassword.setError("6 - 20 Zeichen, Groß- und Kleinbuchstaben, Zahlen und Sonderzeichen (!#$%)");
        } else {
            txtPassword.setError("");
        }

        if(Objects.requireNonNull(txtPasswordWdh.getEditText()).getText().toString().equals("")) {
            Ergebnis = false;
        }

        if(!Globale_Funktionen.isValidEmail(txtEmail.getEditText().getText().toString())) {
            Ergebnis = false;
            txtEmail.setError("Ungültige Email-Adresse");
        } else {
            txtEmail.setError("");
        }

       if(!txtPassword.getEditText().getText().toString().equals(txtPasswordWdh.getEditText().getText().toString())) {
            Ergebnis = false;
       }

        return Ergebnis;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.Buchung_Toolbar_btnClose:
                onBackPressed();
                break; }
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
