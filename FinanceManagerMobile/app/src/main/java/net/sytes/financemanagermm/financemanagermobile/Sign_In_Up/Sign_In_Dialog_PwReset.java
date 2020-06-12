package net.sytes.financemanagermm.financemanagermobile.Sign_In_Up;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;

import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.Objects;

public class Sign_In_Dialog_PwReset extends DialogFragment {
    private static FrameLayout frmLayout;
    private static String Email;
    private static Context Context;
    public static Activity Activity;

    public Sign_In_Dialog_PwReset(@NonNull Context context, android.app.Activity activity, String email) {
        Context = context;
        Email = email;
        Activity = activity;
    }
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ReturnView =  inflater.inflate(R.layout.sign_in_dialog_password_missing, container, false);
        frmLayout = ReturnView.findViewById(R.id.framelayout);
        getChildFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.framelayout,Sign_In_Dialog_PwReset_Fragment_Email.newInstance(Email, this),"123").commit();
        setCancelable(true);

        return ReturnView;
    }
}
