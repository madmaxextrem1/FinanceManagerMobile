package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Finanzbuchung_Buchung;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.FinanzbuchungenCallback;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Finanzbuchungen_Kooperation;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.ArrayList;

public class Gemeinsame_Finanzen_Kooperation_Fragment_Home extends Fragment {
    private TextView lblErgebnis;
    private TextView lblAusgaben;
    private TextView lblEinnahmen;
    private Kooperation kooperation;
    private RecyclerView rcvBenutzerBilanzen;


    public Gemeinsame_Finanzen_Kooperation_Fragment_Home (Kooperation kooperation) {
        this.kooperation = kooperation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View returnView = (View) inflater.inflate(R.layout.gemeinsame_finanzen_kooperation_fragment_home, container, false);

        lblErgebnis = (TextView) returnView.findViewById(R.id.lblGesamtergebnis);
        lblEinnahmen = (TextView) returnView.findViewById(R.id.lblEinnahmen);
        lblAusgaben = (TextView) returnView.findViewById(R.id.lblAusgaben);

        rcvBenutzerBilanzen = (RecyclerView) returnView.findViewById(R.id.rcvBenutzerBilanzen);

        if(!Finanzbuchungen_Kooperation.getFinanzbuchungenInitialized()) {
            Finanzbuchungen_Kooperation.initializeFinanzbuchungen(new FinanzbuchungenCallback() {
                @Override
                public void onFinanzbuchungenSuccessfullyLoaded(ArrayList<Finanzbuchung_Buchung> finanzbuchungen) {

                }
            }, kooperation);
        }
        return returnView;
    }
}