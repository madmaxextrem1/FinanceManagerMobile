package net.sytes.financemanagermm.financemanagermobile.Sign_In_Up;

import android.os.Bundle;
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

import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.Random;

public class Sign_In_Dialog_PwReset_Fragment_Code extends Fragment {
    private static String email;
    private static DialogFragment parent;
    private static int code;
    public static Sign_In_Dialog_PwReset_Fragment_Code newInstance (int Code, String Email, DialogFragment Parent) {
        Sign_In_Dialog_PwReset_Fragment_Code fragment = new Sign_In_Dialog_PwReset_Fragment_Code();
        email = Email;
        parent = Parent;
        code = Code;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ReturnView =  inflater.inflate(R.layout.sign_in_dialog_password_missing_fragment_code, container, false);
        TextInputLayout txtCode = (TextInputLayout) ReturnView.findViewById(R.id.Code);
        MaterialButton btnWeiter = (MaterialButton) ReturnView.findViewById(R.id.btnWeiter);

        btnWeiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtCode.getEditText().getText().toString().equals(String.valueOf(code))) {
                    parent.getChildFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.framelayout,
                            Sign_In_Dialog_PwReset_Fragment_PW_Reset.newInstance(code,email,parent),"123").commit();
                } else {
                    txtCode.setError("Code inkorrekt");
                }
            }
        });

        return ReturnView;
    }
}