package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daimajia.swipe.SwipeLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.FinanceManagerData_Edited_Interface;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Verwaltung_Konten_Detailansicht;

import java.util.LinkedHashMap;

public class Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Konten extends Fragment {
    private ListView lvKonten;
    private FloatingActionButton btnKontoHinzufügen;
    private TextInputEditText txtBeschreibung;
    private Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Konten_Adapter kontoAdapter;
    private Gemeinsame_Finanzen_Anfrage_BearbeitenDialog parent;
    private KooperationAnfrage anfrage;
    private SwipeLayout swipeLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ReturnView =  inflater.inflate(R.layout.gemeinsame_finanzen_anfrage_neueanfrage_dialog_fragment_konten, container, false);
        parent = (Gemeinsame_Finanzen_Anfrage_BearbeitenDialog) getParentFragment();
        anfrage = parent.getAnfrage();

        lvKonten = ReturnView.findViewById(R.id.lvKonten);
        TextInputLayout txtBeschreibungLayout = (TextInputLayout) ReturnView.findViewById(R.id.txtAnfrage_Beschreibung);

        txtBeschreibung = (TextInputEditText) txtBeschreibungLayout.getEditText();

        btnKontoHinzufügen = ReturnView.findViewById(R.id.btnKonto_Hinzufügen);
        View SwipeView = getLayoutInflater().inflate(R.layout.gemeinsame_finanzen_anfrage_fragment_konten_adapter_item, null);
        swipeLayout = (SwipeLayout) SwipeView.findViewById(R.id.SwipeLayout);

        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, SwipeView.findViewWithTag(R.id.bottom_wrapper));
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        btnKontoHinzufügen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager =getChildFragmentManager();
                Verwaltung_Konten_Detailansicht dialog = new Verwaltung_Konten_Detailansicht(getContext(), null, new FinanceManagerData_Edited_Interface<Konto>() {
                    @Override
                    public void onDataEdited(Konto data, boolean created) {
                        data.setId(kontoAdapter.getLinkedMap().size() + 1);
                        kontoAdapter.getLinkedMap().put(data.getIdentifier(), data);
                        kontoAdapter.notifyDataSetChanged();
                    }
                }, false);
                dialog.show(fragmentManager, "fragment_edit_konto");
            }
        });

        if(parent.getAnfrage() != null) {
            txtBeschreibung.setText(anfrage.getBeschreibung());
            kontoAdapter = new Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Konten_Adapter(getContext(), R.layout.gemeinsame_finanzen_anfrage_fragment_konten_adapter_item, R.id.SwipeLayout);
            anfrage.getKontoMap().values().forEach(konto -> kontoAdapter.getLinkedMap().put(konto.getIdentifier(), konto.getCopy()));
            lvKonten.setAdapter(kontoAdapter);
            kontoAdapter.notifyDataSetChanged();
        } else {
            kontoAdapter = new Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Konten_Adapter(getContext(), R.layout.gemeinsame_finanzen_anfrage_fragment_konten_adapter_item, R.id.SwipeLayout);
            lvKonten.setAdapter(kontoAdapter);
            kontoAdapter.notifyDataSetChanged();
        }

        return ReturnView;
    }

    public String getBeschreibung() {return txtBeschreibung.getText().toString().trim();}
    public LinkedHashMap<Integer, Konto> getKontoMap(){return kontoAdapter.getLinkedMap();}
}