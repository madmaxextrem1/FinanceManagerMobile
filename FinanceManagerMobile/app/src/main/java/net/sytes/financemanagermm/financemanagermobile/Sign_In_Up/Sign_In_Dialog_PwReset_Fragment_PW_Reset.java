package net.sytes.financemanagermm.financemanagermobile.Sign_In_Up;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.HashMap;
import java.util.Random;

import es.dmoral.toasty.Toasty;

import static android.widget.Toast.LENGTH_SHORT;

public class Sign_In_Dialog_PwReset_Fragment_PW_Reset extends Fragment {
    private static String email;
    private static DialogFragment parent;
    private TextInputLayout txtPassword;
    private TextInputLayout txtPasswordWdh;

    private static int code;
    public static Sign_In_Dialog_PwReset_Fragment_PW_Reset newInstance (int Code, String Email, DialogFragment Parent) {
        Sign_In_Dialog_PwReset_Fragment_PW_Reset fragment = new Sign_In_Dialog_PwReset_Fragment_PW_Reset();
        email = Email;
        parent = Parent;
        code = Code;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ReturnView =  inflater.inflate(R.layout.sign_in_dialog_password_missing_fragment_pw_reset, container, false);
        TextInputLayout txtCode = (TextInputLayout) ReturnView.findViewById(R.id.Code);
        txtPassword = (TextInputLayout) ReturnView.findViewById(R.id.Password);
        txtPasswordWdh = (TextInputLayout) ReturnView.findViewById(R.id.PasswordWdh);
        MaterialButton btnSpeicher = (MaterialButton)ReturnView.findViewById(R.id.btnSpeichern);

        btnSpeicher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtPassword.getEditText().getText().toString().equals("")){
                    if(txtPassword.getEditText().getText().toString().equals(txtPasswordWdh.getEditText().getText().toString())) {
                        HashMap postData = new HashMap();
                        postData.put("password", txtPassword.getEditText().getText().toString());
                        postData.put("email", email);
                        PostResponseAsyncTask loginTask =
                                new PostResponseAsyncTask(getContext(), postData,"Email senden",  new AsyncResponse() {
                                    @Override
                                    public void processFinish(String output) {
                                        Sign_In_Dialog_PwReset signIn = (Sign_In_Dialog_PwReset) parent;
                                        Sing_In signIn_Activity = (Sing_In) signIn.Activity;
                                        signIn_Activity.setPassword(txtPassword.getEditText().getText().toString());
                                        Toasty.success(getContext(),"Passwort geändert",LENGTH_SHORT,true).show();
                                        parent.dismiss();
                                    }});
                        loginTask.execute("http://financemanagermm.sytes.net/fmclient/passwort_ändern.php");
                    } else {txtPasswordWdh.setError("Passwörter stimmen nicht überein."); }
                } else {
                txtPassword.setError("Fehlendes Passwort.");
                }
            }
        });



        return ReturnView;
    }
}