package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorien;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungshauptkategorie;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.DialogFragment;

public class Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog extends DialogFragment implements View.OnClickListener {

    private ExpandableListView lv_Kategorien;
    private ArrayList<Buchungshauptkategorie> HauptkategorieListe;
    private Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog_lvKategorien_Adapter adapter;
    private Boolean Unterkategorie_Geladen = true;
    private Button btnHinzufügen;
    private int lastExpandedPosition = -1;
    private Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog.Callback callback;
    private TextView DialogTitle;
    private AppCompatImageButton btnClose;

    public static Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog newInstance(String title, Buchungskategorie Kategorie) {
        Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog frag = new Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelable("kategorie",Kategorie);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.NormalDialogFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verwaltung_dauerauftraege_fragment_details_kategorie_dialog, container,false);

        DialogTitle = view.findViewById(R.id.Kontendetails_lblDialogTitle);
        DialogTitle.setText(getArguments().getString("title"));
        lv_Kategorien = view.findViewById(R.id.Kategorie_Dialog_lv_Kategorien);
        btnHinzufügen = view.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_btn_Hinzufügen);
        btnClose = view.findViewById(R.id.Kontendetails_btnDialogClose);
        btnClose.setOnClickListener(this);

        btnHinzufügen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Buchungskategorie Eintrag =  adapter.getChild(adapter.getSelectedGroupIndex(), adapter.getSelectedPosition());
                callback.onActionClick(Eintrag);
                Log.d("Test","ID: " + String.valueOf(Eintrag.getId()) + "; Rot: " + String.valueOf(Eintrag.getRot()) +
                        "; Grün: " + String.valueOf(Eintrag.getGrün()) + "; Blau: " + String.valueOf(Eintrag.getBlau()));
                dismiss();
            }
        });

        adapter = new Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog_lvKategorien_Adapter(getContext());
        lv_Kategorien.setAdapter(adapter);
        lv_Kategorien.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    lv_Kategorien.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
                lv_Kategorien.setSelectionFromTop(groupPosition,0);
            }
        });
        adapter.notifyDataSetChanged();

        return view;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.Kontendetails_btnDialogClose:
                dismiss();
                break; }
    }
    public void setCallback(Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog.Callback callback) {
        this.callback = callback;
    }
    public interface Callback {
        void onActionClick(Buchungskategorie Eintrag);
    }
}