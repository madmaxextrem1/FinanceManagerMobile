package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Kooperationen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.KooperationenCallback;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Gemeinsame_Finanzen_Fragment_Kooperationen extends Fragment implements Gemeinsame_Finanzen_Kooperation_Auswahl_Eintrag_ItemClickListener {
    private RecyclerView rcvKooperationenAufstellung;
    private FloatingActionButton mAnfrageHinzufügenButton;
    private Gemeinsame_Finanzen_Fragment_Kooperation_CardView_RecyclerViewAdapter kooperation_recyclerViewAdapter;

    static Gemeinsame_Finanzen_Fragment_Kooperationen newInstance() {
        Gemeinsame_Finanzen_Fragment_Kooperationen frag = new Gemeinsame_Finanzen_Fragment_Kooperationen();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ReturnView =  inflater.inflate(R.layout.gemeinsame_finanzen_fragment_kooperation, container, false);
        rcvKooperationenAufstellung = ReturnView.findViewById(R.id.Gemeinsame_Finanzen_rcvKooperationen);

        Gemeinsame_Finanzen gemeinsameFinanzenActivity = null;
        if(getActivity() instanceof Gemeinsame_Finanzen) {
            gemeinsameFinanzenActivity = (Gemeinsame_Finanzen) getActivity();
        }

        mAnfrageHinzufügenButton = Objects.requireNonNull(gemeinsameFinanzenActivity).findViewById(R.id.fab);
        mAnfrageHinzufügenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Gemeinsame_Finanzen_Anfrage_BearbeitenDialog.class);
                startActivity(intent);
            }
        });

        if(!Kooperationen.getKooperationenInitialized()) {
            Kooperationen.initializeKooperationen(new KooperationenCallback() {
                @Override
                public void onKooperationenSuccessfullyLoaded(LinkedHashMap<Integer, Kooperation> Kooperationen) {
                    Kooperationen_Aktualisieren();
                }
            });
        } else {
            Kooperationen_Aktualisieren();
        }

        return ReturnView;
    }

    public void Kooperationen_Aktualisieren() {
        kooperation_recyclerViewAdapter =
                new Gemeinsame_Finanzen_Fragment_Kooperation_CardView_RecyclerViewAdapter(
                        getContext(), Kooperationen.getKooperationen());
        rcvKooperationenAufstellung.setAdapter(kooperation_recyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rcvKooperationenAufstellung.setLayoutManager(layoutManager);
        kooperation_recyclerViewAdapter.notifyDataSetChanged();
    }
    @Override
    public void onKooperationItemClicked(int pos, Kooperation KooperationItem, View shareCardView) {

    }

    public Gemeinsame_Finanzen_Fragment_Kooperation_CardView_RecyclerViewAdapter getKooperationAdapter() {
        return kooperation_recyclerViewAdapter;
    }
}