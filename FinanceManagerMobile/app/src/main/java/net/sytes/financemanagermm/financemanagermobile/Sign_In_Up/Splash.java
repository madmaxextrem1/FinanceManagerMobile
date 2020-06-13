package net.sytes.financemanagermm.financemanagermobile.Sign_In_Up;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.transition.Transition;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

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

import es.dmoral.toasty.Toasty;

public class Splash extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 800;
    private static String loginURL;
    private static final String Username = "";
    private static final String Pwd = "";
    private static final String Remember = "false";
    private static final String VAL_KEY_Username = "Username";
    private static final String VAL_KEY_Pwd = "Pwd";
    private static final String VAL_KEY_Remember = "Remember";
    private ServerCommunication serverCom;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.sign_in_splash);
        serverCom = new ServerCommunication(this);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //Falls RememberMe gesetzt ist und Passwort und User in den Preferences stehen -> Login überspringen
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                loginURL = getResources().getString(R.string.PHP_Scripts_Login);

                Log.d("Test123!", String.valueOf(sharedPreferences.getBoolean(VAL_KEY_Remember, Boolean.valueOf(Remember)) + ", " +
                        sharedPreferences.getString(VAL_KEY_Username, Username) + ", " +
                        sharedPreferences.getString(VAL_KEY_Pwd, Pwd)) + ", " + String.valueOf(sharedPreferences.getBoolean(VAL_KEY_Remember, Boolean.valueOf(Remember)) &&
                        !sharedPreferences.getString(VAL_KEY_Username, Username).equals("") &&
                        !sharedPreferences.getString(VAL_KEY_Pwd, Pwd).equals("")));

                if(sharedPreferences.getBoolean(VAL_KEY_Remember, Boolean.valueOf(Remember)) &&
                        !sharedPreferences.getString(VAL_KEY_Username, Username).equals("") &&
                        !sharedPreferences.getString(VAL_KEY_Pwd, Pwd).equals("")) {

                    executeLogin(sharedPreferences.getString(VAL_KEY_Username, Username),
                            sharedPreferences.getString(VAL_KEY_Pwd, Pwd));
                } else {
                    Intent mainIntent = new Intent(Splash.this,Sing_In.class);
                    Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(Splash.this,
                            R.anim.fade_in, R.anim.fade_out).toBundle();
                    startActivity(mainIntent, bundle);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    public void executeLogin(String Username, String Password) {
        serverCom.authenticateUser(Username, Password, new ServerCommunicationInterface.GeneralCommunicationCallback<User>() {
            @Override
            public void onRequestCompleted(User data) {
                if (data != null) {
                    Intent intent = new Intent(Splash.this, Hauptmenu.class);

                    FinanceManagerMobileApplication.getInstance().getDataManagement().setCurrentUser(data);

                    Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(Splash.this,
                            R.anim.fade_in, R.anim.fade_out).toBundle();
                    startActivity(intent, bundle);
                    finish();
                }
            }
        });
    }
}
