package net.sytes.financemanagermm.financemanagermobile.Hauptmenu;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.google.android.material.button.MaterialButton;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.mikepenz.materialdrawer.Drawer;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchung;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Finanzbuchung_Buchung;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Finanzbuchungen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.FinanzbuchungenCallback;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import es.dmoral.toasty.Toasty;

public class Hauptmenu_Fragment_Buchungen extends Fragment implements Hauptmenu_Fragment_Buchungen_Liste_ItemClickListener {
    private ListView lv_Buchungen;
    private Hauptmenu_Fragment_Buchungen_Liste_SwipeAdapter hauptmenu_buchungen_liste_adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeLayout swipeLayout;
    private static AppCompatImageButton ToolbarFilterButton;
    private static Drawer SideSheetDrawer;

    public static Hauptmenu_Fragment_Buchungen newInstance (AppCompatImageButton toolbarFilterButton, Drawer sideSheetDrawer) {
        Hauptmenu_Fragment_Buchungen fragment = new Hauptmenu_Fragment_Buchungen();
        ToolbarFilterButton = toolbarFilterButton;
        SideSheetDrawer = sideSheetDrawer;

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ReturnView =  inflater.inflate(R.layout.hauptmenu_fragment_buchungen, container, false);
        View SwipeView = inflater.inflate(R.layout.hauptmenu_fragment_buchungen_listitem_swipelayout,container,false);
        lv_Buchungen = (ListView) ReturnView.findViewById(R.id.lv_Buchungen);

        swipeRefreshLayout = (SwipeRefreshLayout) ReturnView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.fm_Login_Background_Color),
                        getResources().getColor(R.color.colorPrimary),
                Color.GREEN);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Buchungen_Laden();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });

        swipeLayout = (SwipeLayout) SwipeView.findViewById(R.id.Hauptmenu_Fragment_Buchung_lvItem_SwipeLayout);

        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, SwipeView.findViewById(R.id.bottom_wrapper));
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

        ToolbarFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             SideSheetDrawer.openDrawer();
            }
        });

        Buchungen_Laden();
        return ReturnView;
    }
    public void Buchungen_Laden() {
        Finanzbuchungen.initializeFinanzbuchungen(new FinanzbuchungenCallback() {
                @Override
                public void onFinanzbuchungenSuccessfullyLoaded(ArrayList<Finanzbuchung_Buchung> finanzbuchungen) {
                    hauptmenu_buchungen_liste_adapter = new Hauptmenu_Fragment_Buchungen_Liste_SwipeAdapter(getContext(), Hauptmenu_Fragment_Buchungen.this, finanzbuchungen);
                    lv_Buchungen.setAdapter(hauptmenu_buchungen_liste_adapter);
                    System.out.println(hauptmenu_buchungen_liste_adapter.getCount());
                    hauptmenu_buchungen_liste_adapter.notifyDataSetChanged();
                }
            });
    }
    @Override
    public void onBuchungtemClicked(int pos, Finanzbuchung_Buchung BuchungEintrag, boolean EditMode) {
        if(EditMode) {
            System.out.println(BuchungEintrag);
            Intent intent = new Intent(getContext(), Buchung.class);
            intent.putExtra("EditMode",true);
            intent.putExtra("BuchungEintrag",BuchungEintrag);
            int requestCode = 1; // Or some number you choose
            startActivityForResult(intent, requestCode, ActivityOptions.makeBasic().toBundle());
            swipeLayout.close(false,true);

            Buchung.setBuchungCreatedCallback(new Buchung.Buchung_Created_Interface() {
                @Override
                public void onBuchungCreated(Finanzbuchung_Buchung buchung) {
                    Finanzbuchungen.getFinanzbuchungen().set(pos, BuchungEintrag);
                    Finanzbuchungen.reorderBuchungen();
                    hauptmenu_buchungen_liste_adapter.notifyDataSetChanged();
                }
            });
        } else {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.gemeinsame_finanzen_fragment_anfrage_alertdialog);

            TextView txtTitel = (TextView) dialog.findViewById(R.id.Titel);
            txtTitel.setText("Buchung löschen?");
            TextView txtMessage = (TextView) dialog.findViewById(R.id.Message);
            txtMessage.setText("Buchung mit dem Titel '" + BuchungEintrag.getBeschreibung() + "' wirklich löschen?" );

            MaterialButton löschenButton = (MaterialButton) dialog.findViewById(R.id.btnLöschen);
            löschenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap BuchungDaten = new HashMap();
                    BuchungDaten.put("BuchungID",String.valueOf(BuchungEintrag.getId()));

                    PostResponseAsyncTask LöschenTask =
                            new PostResponseAsyncTask(getContext(), BuchungDaten, false,  new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    if(s.equals("success")) {
                                        hauptmenu_buchungen_liste_adapter.getEintragListe().remove(BuchungEintrag);
                                        swipeLayout.close(false, true);
                                        Toasty.success(getContext(),"Gelöscht", Toast.LENGTH_SHORT,true).show();
                                        hauptmenu_buchungen_liste_adapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                    } else {
                                        Toasty.error(getContext(), "Error: " + s, Toast.LENGTH_SHORT,true).show();
                                    }
                                }
                            });
                    LöschenTask.execute("http://financemanagermm.sytes.net/fmclient/buchung_löschen.php");

                }
            });

            MaterialButton abbrechenButton = (MaterialButton) dialog.findViewById(R.id.btnAbbrechen);
            abbrechenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

    }

    public Hauptmenu_Fragment_Buchungen_Liste_SwipeAdapter getBuchungAdapter() {
        return hauptmenu_buchungen_liste_adapter;
    }
}