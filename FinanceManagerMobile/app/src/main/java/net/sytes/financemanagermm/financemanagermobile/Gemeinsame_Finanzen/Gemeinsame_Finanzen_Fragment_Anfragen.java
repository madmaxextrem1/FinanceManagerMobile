package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.KooperationAnfragen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.KooperationAnfragenCallback;
import net.sytes.financemanagermm.financemanagermobile.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Gemeinsame_Finanzen_Fragment_Anfragen extends Fragment {
    private RecyclerView rcvAnfragenAufstellung;
    private Gemeinsame_Finanzen_Fragment_Anfrage_RecyclerViewAdapter rcvAnfragenAufstellungAdapter;
    private Activity mCallback;
    private FloatingActionButton mAnfrageHinzufügenButton;

    static Gemeinsame_Finanzen_Fragment_Anfragen newInstance(Activity ParentActivity) {
        Gemeinsame_Finanzen_Fragment_Anfragen frag = new Gemeinsame_Finanzen_Fragment_Anfragen();;
        return frag;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ReturnView =  inflater.inflate(R.layout.gemeinsame_finanzen_fragment_anfrage, container, false);
        rcvAnfragenAufstellung = ReturnView.findViewById(R.id.Gemeinsame_Finanzen_rcvAnfragen);

        Gemeinsame_Finanzen gemeinsameFinanzenActivity = null;
        if(getActivity() instanceof Gemeinsame_Finanzen) {
           gemeinsameFinanzenActivity = (Gemeinsame_Finanzen) getActivity();
        }

        mAnfrageHinzufügenButton = Objects.requireNonNull(gemeinsameFinanzenActivity).findViewById(R.id.fab);
        mAnfrageHinzufügenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gemeinsame_Finanzen_Anfrage_BearbeitenDialog bearbeitenDialog = new Gemeinsame_Finanzen_Anfrage_BearbeitenDialog(false, null, new Gemeinsame_Finanzen_Anfrage_BearbeitenCallback() {
                    @Override
                    public void onAnfrageEdited(KooperationAnfrage Anfrage) {
                        if(Anfrage != null) {
                            rcvAnfragenAufstellungAdapter.getLinkedMap().put(Anfrage.getIdentifier(), Anfrage);
                            rcvAnfragenAufstellungAdapter.notifyDataSetChanged();
                        }
                    }
                });
                bearbeitenDialog.show(getChildFragmentManager(), "AnfrageBearbeitenDialog");
            }
        });

        if(!KooperationAnfragen.getKooperationAnfragenInitialized()) {
            KooperationAnfragen.initializeAnfragen(new KooperationAnfragenCallback() {
                @Override
                public void onKooperationAnfragenSuccessfullyLoaded(LinkedHashMap<Integer, KooperationAnfrage> anfragenMap) {
                    Anfragen_Aktualisieren();
                }
            });
        } else {
            Anfragen_Aktualisieren();
        }

        return ReturnView;
    }

    private void Anfragen_Aktualisieren() {
        rcvAnfragenAufstellungAdapter = new Gemeinsame_Finanzen_Fragment_Anfrage_RecyclerViewAdapter(
                getContext(), KooperationAnfragen.getKooperationAnfragen());
        rcvAnfragenAufstellung.setAdapter(rcvAnfragenAufstellungAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        rcvAnfragenAufstellung.setLayoutManager(layoutManager);
        rcvAnfragenAufstellungAdapter.notifyDataSetChanged();
    }
}