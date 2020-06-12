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

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Sign_In_Dialog_PwReset_Fragment_Email extends Fragment {
    private static String Email;
    private static DialogFragment Parent;
    public static Sign_In_Dialog_PwReset_Fragment_Email newInstance (String email, DialogFragment parent) {
        Sign_In_Dialog_PwReset_Fragment_Email fragment = new Sign_In_Dialog_PwReset_Fragment_Email();
        Email = email;
        Parent = parent;
        return fragment;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Random rnd = new Random();
        final int Code =  rnd.nextInt(999999 - 100000) + 100000;

        View ReturnView =  inflater.inflate(R.layout.sign_in_dialog_password_missing_fragment_email, container, false);
        TextInputLayout txtEmail = (TextInputLayout) ReturnView.findViewById(R.id.txtEmail);
        Objects.requireNonNull(txtEmail.getEditText()).setText(Email);
        MaterialButton btnSenden = (MaterialButton) ReturnView.findViewById(R.id.btnSenden);
        btnSenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Globale_Funktionen.isValidEmail(txtEmail.getEditText().getText().toString().toLowerCase().trim())) {
                    txtEmail.setError("Ungültige Email-Adresse");
                } else {
                    HashMap postData = new HashMap();
                    postData.put("code", String.valueOf(Code));
                    postData.put("email", txtEmail.getEditText().getText().toString().trim().toLowerCase());
                    PostResponseAsyncTask loginTask =
                            new PostResponseAsyncTask(getContext(), postData, "Senden", new AsyncResponse() {
                                @Override
                                public void processFinish(String output) {
                                    Parent.getChildFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.framelayout,
                                            Sign_In_Dialog_PwReset_Fragment_Code.newInstance(Code, Email, Parent), "123").commit();
                                }
                            });
                    loginTask.execute("http://financemanagermm.sytes.net/fmclient/mail_pw_reset.php");
                }
            }
        });

        return ReturnView;
    }
}