package net.sytes.financemanagermm.financemanagermobile.Sign_In_Up;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.FinanceManagerMobileApplication;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.ApplicationController;
import net.sytes.financemanagermm.financemanagermobile.Hauptmenu.Hauptmenu;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.ServerCommunication.ServerCommunication;
import net.sytes.financemanagermm.financemanagermobile.ServerCommunication.ServerCommunicationInterface;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Sing_In extends AppCompatActivity {
    private static final String Username = "";
    private static final String Pwd = "";
    private static final String Remember = "false";
    private static final String VAL_KEY_Username = "Username";
    private static final String VAL_KEY_Pwd = "Pwd";
    private static final String VAL_KEY_Remember = "Remember";
    private String loginURL;
    private TextInputLayout txtUsername;
    private TextInputLayout txtPwd;
    private MaterialCheckBox chkRemember;
    private TextView lblSignUp;
    private TextView lblPwVergessen;
    private MaterialButton btnLogin;
    private SharedPreferences preferences;
    private ServerCommunication serverCom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loginURL = getResources().getString(R.string.PHP_Scripts_Login);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        serverCom = new ServerCommunication(this);

        txtUsername = findViewById(R.id.txtUsername);
        txtPwd = findViewById(R.id.txtPwd);
        chkRemember = findViewById(R.id.chkRemember);
        lblPwVergessen = findViewById(R.id.lblPwVergessen);
        btnLogin = findViewById(R.id.btn_Login);
        btnLogin.requestFocus();

        lblSignUp = findViewById(R.id.Login_lblSignUp);

        if(getIntent().getExtras() != null && getIntent().getExtras().getBoolean("Abmelden")){
            chkRemember.setChecked(false);
            txtPwd.getEditText().setText("");
            txtUsername.getEditText().setText("");

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(VAL_KEY_Username, "");
            editor.putString(VAL_KEY_Pwd, "");
            editor.putBoolean(VAL_KEY_Remember, false);
            editor.apply();
        } else {
            preferences = getPreferences(MODE_PRIVATE);
            Objects.requireNonNull(txtUsername.getEditText()).setText(preferences.getString(VAL_KEY_Username, Username));
            Objects.requireNonNull(txtPwd.getEditText()).setText(preferences.getString(VAL_KEY_Pwd, Pwd));
            chkRemember.setChecked(preferences.getBoolean(VAL_KEY_Remember, Boolean.valueOf(Remember)));
            Log.d("Test123!", String.valueOf(preferences.getBoolean(VAL_KEY_Remember, Boolean.valueOf(Remember)) + ", " +
                    preferences.getString(VAL_KEY_Username, Username) + ", " +
                    preferences.getString(VAL_KEY_Pwd, Pwd)));
        }

        //Linkbuilder für Passwort-Vergessen Link
        Link linkPasswortVergessen = new Link("Passwort vergessen?")
                .setTextColor(Color.WHITE)
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        Sign_In_Dialog_PwReset dialog = new Sign_In_Dialog_PwReset(Sing_In.this, Sing_In.this, txtUsername.getEditText().getText().toString());
                        dialog.show(getSupportFragmentManager(),"test");
                    }
                });

        //Linkbuilder für Sign-Up Link
        Link linkSignUp = new Link("hier")
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        Intent intent = new Intent(Sing_In.this, Sign_Up.class);
                        int requestCode = 1; // Or some number you choose
                        startActivityForResult(intent, requestCode);
                    }
                });

        // create the link builder object add the link rule
        LinkBuilder.on(lblPwVergessen)
                .addLink(linkPasswortVergessen)
                .build(); // create the clickable links

        // create the link builder object add the link rule
        LinkBuilder.on(lblSignUp)
                .addLink(linkSignUp)
                .build(); // create the clickable links

        btnLogin.setOnClickListener(view -> {
            if(txtUsername.getEditText().getText().toString().equals("") && txtPwd.getEditText().getText().toString().equals("")) {
                Toasty.error(Sing_In.this, "Username oder Passwort fehlt",Toast.LENGTH_LONG, true).show();
            } else {
                executeLogin(txtUsername.getEditText().getText().toString(), txtPwd.getEditText().getText().toString());
            }
        });

        chkRemember.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SharedPreferences sharedPrefs1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPrefs1.edit();
                editor.putString(VAL_KEY_Username, txtUsername.getEditText().getText().toString());
                editor.putString(VAL_KEY_Pwd, txtPwd.getEditText().getText().toString());
                editor.putBoolean(VAL_KEY_Remember, isChecked);
                editor.apply();
            } else {
                SharedPreferences sharedPrefs1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPrefs1.edit();
                editor.putString(VAL_KEY_Username, "");
                editor.putString(VAL_KEY_Pwd, "");
                editor.putBoolean(VAL_KEY_Remember, false);
                editor.apply();
            }
        });
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        // Collect data from the intent and use it
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Objects.requireNonNull(txtUsername.getEditText()).setText(data.getStringExtra("Username"));
            Objects.requireNonNull(txtPwd.getEditText()).setText(data.getStringExtra("Password"));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void executeLogin(String Username, String Password) {
        serverCom.authenticateUser(Username, Password, new ServerCommunicationInterface.GeneralCommunicationCallback<User>() {
            @Override
            public void onRequestCompleted(User data) {
                if (data != null) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor =  sharedPreferences.edit();
                    editor.clear();
                    editor.putString(VAL_KEY_Username, txtUsername.getEditText().getText().toString());
                    editor.putString(VAL_KEY_Pwd, txtPwd.getEditText().getText().toString());
                    editor.putBoolean(VAL_KEY_Remember, chkRemember.isChecked());
                    editor.apply();

                    Intent intent = new Intent(Sing_In.this, Hauptmenu.class);
                    FinanceManagerMobileApplication.getInstance().getDataManagement().setCurrentUser(data);

                    Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(Sing_In.this,
                            R.anim.fade_in, R.anim.fade_out).toBundle();
                    startActivity(intent, bundle);
                    finish();
                }
            }
        });
    }
    public void setPassword(String password) {
        txtPwd.getEditText().setText(password);
    }
}