package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.expansionpanel.ExpansionHeader;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.google.android.material.button.MaterialButton;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.FinanceManagerCallback;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Konten;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.KooperationAnfragen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Kooperationen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.KooperationenCallback;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.CustomAlertDialog;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.LinkedHashMap_BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

import es.dmoral.toasty.Toasty;

public class Gemeinsame_Finanzen_Fragment_Anfrage_RecyclerViewAdapter extends LinkedHashMap_BaseRecyclerAdapter<Integer, KooperationAnfrage> {
    private View view;

    public Gemeinsame_Finanzen_Fragment_Anfrage_RecyclerViewAdapter(
            Context context, LinkedHashMap<Integer, KooperationAnfrage> anfragenMap) {
        super(context, anfragenMap);
    }

    @NonNull
    @Override
    public Gemeinsame_Finanzen_Fragment_Anfrage_RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gemeinsame_finanzen_fragment_anfrage_cardviewitem, parent, false);
        return new Gemeinsame_Finanzen_Fragment_Anfrage_RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        KooperationAnfrage AnfrageItem = getItem(holder.getAdapterPosition());
        FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();

        if (AnfrageItem.getCreatorMail().equals(GlobaleVariablen.getInstance().getEmail())) {
            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gemeinsame_Finanzen_Anfrage_BearbeitenDialog bearbeitenDialog = new Gemeinsame_Finanzen_Anfrage_BearbeitenDialog(true, AnfrageItem, new Gemeinsame_Finanzen_Anfrage_BearbeitenCallback() {
                        @Override
                        public void onAnfrageEdited(KooperationAnfrage Anfrage) {
                            notifyDataSetChanged();
                        }
                    });
                    bearbeitenDialog.show(fragmentManager, "AnfrageBearbeitenDialog");
                }
            });
        }

        Resources res = getContext().getResources();
        final int newColor = res.getColor(R.color.white, null);
        mHolder.Titel.setText(AnfrageItem.getBeschreibung());

        Gemeinsame_Finanzen_Fragment_Anfrage_CardView_KontenAdapter kontenAdapter = new Gemeinsame_Finanzen_Fragment_Anfrage_CardView_KontenAdapter(getContext(), AnfrageItem.getKontoMap());
        mHolder.lvKonten.setAdapter(kontenAdapter);
        kontenAdapter.notifyDataSetChanged();
        mHolder.lvKonten.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        Gemeinsame_Finanzen_Fragment_Anfrage_CardView_BenutzerAdapter BenutzerAdapter =
                new Gemeinsame_Finanzen_Fragment_Anfrage_CardView_BenutzerAdapter(
                        getContext(), AnfrageItem.getBenutzerMap(), AnfrageItem.getErstellerID());
        mHolder.lvPersonen.setAdapter(BenutzerAdapter);
        BenutzerAdapter.notifyDataSetChanged();

        //ImageView verändern je nach Status der Anfrage ob bereits gestartet oder nicht
        if (AnfrageItem.getKooperationID() != 0) {
            mHolder.LeftImage.setBackgroundTintList(
                    ColorStateList.valueOf(getContext().getColor(R.color.ActiveKooperation)));
            mHolder.LeftImage.setImageDrawable(
                    getContext().getResources().getDrawable(R.drawable.ic_check_white_24dp, null));
        } else {
            mHolder.LeftImage.setBackgroundTintList(
                    ColorStateList.valueOf(getContext().getColor(R.color.CardviewTopIconColor)));
            mHolder.LeftImage.setImageDrawable(
                    getContext().getResources().getDrawable(R.drawable.ic_questionmark_thin, null));
        }

        mHolder.lvPersonen.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        mHolder.expandableLayout.addListener(new ExpansionLayout.Listener() {
            @Override
            public void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded) {
                int totalHeightBenutzer = 0;
                int minHeightBenutzer = 0;
                View listItemStandard = BenutzerAdapter.getView(0, null, mHolder.lvPersonen);
                listItemStandard.measure(0, 0);
                totalHeightBenutzer = listItemStandard.getMeasuredHeight() * BenutzerAdapter.getCount();
                minHeightBenutzer = listItemStandard.getMeasuredHeight() * 2;

                ViewGroup.LayoutParams params = mHolder.lvPersonen.getLayoutParams();
                if (BenutzerAdapter.getCount() <= 2) {
                    params.height = totalHeightBenutzer;
                } else {
                    params.height = minHeightBenutzer;
                }

                mHolder.lvPersonen.setLayoutParams(params);
                mHolder.lvPersonen.requestLayout();

/*                int totalHeightKonto;
                int minHeightKonto;

                View listItemStandardKonto = kontenAdapter.getView(0, null, mHolder.lvKonten);
                listItemStandardKonto.measure(0, 0);
                totalHeightKonto = listItemStandardKonto.getMeasuredHeight() * kontenAdapter.getCount();
                minHeightKonto = listItemStandardKonto.getMeasuredHeight();

                System.out.println(totalHeightKonto + ", " + minHeightKonto);

                ViewGroup.LayoutParams paramsKonto = mHolder.lvKonten.getLayoutParams();
                if (kontenAdapter.getCount() <= 1) {
                    paramsKonto.height = totalHeightKonto;
                } else {
                    paramsKonto.height = minHeightKonto;
                }
                mHolder.lvKonten.setLayoutParams(paramsKonto);
                mHolder.lvKonten.requestLayout();*/
            }
        });

        KooperationAnfrageBenutzer PartnerEintrag = null;
        for (KooperationAnfrageBenutzer Eintrag : AnfrageItem.getBenutzerMap().values()) {
            if (Eintrag.getBenutzerID() != GlobaleVariablen.getInstance().getUserId()) {
                PartnerEintrag = Eintrag;
                break;
            }
        }

        KooperationAnfrageBenutzer EigenerEintrag = null;
        for (KooperationAnfrageBenutzer Eintrag : AnfrageItem.getBenutzerMap().values()) {
            if (Eintrag.getBenutzerID() == GlobaleVariablen.getInstance().getUserId()) {
                EigenerEintrag = Eintrag;
                break;
            }
        }

        String Datum = GlobaleVariablen.getInstance().getDE_DateFormat().format(AnfrageItem.getErstelldatum());
        mHolder.Erstelldatum.setText("Erstellt am: " + Datum);

        KooperationAnfrageBenutzer finalEigenerEintrag = EigenerEintrag;

        if (AnfrageItem.getErstellerID() == GlobaleVariablen.getInstance().getUserId()) {
            mHolder.btnBestätigen.setText("Starten");
            mHolder.btnLöschen.setText("Löschen");

            //Checken, ob schon mindestens ein Teilnehmer bestätigt hat
            mHolder.btnLöschen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomAlertDialog confirmationDialog = new CustomAlertDialog(getContext(), "Bestätigen", "Wollen Sie die Anfrage wirklich löschen?", "Löschen", "Abbrechen");
                    confirmationDialog.setOkButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, String> AnfrageDaten = new HashMap<String, String>();
                            AnfrageDaten.put("AnfrageID", String.valueOf(AnfrageItem.getId()));
                            AnfrageDaten.put("BenutzerID", String.valueOf(finalEigenerEintrag.getBenutzerID()));

                            PostResponseAsyncTask LöschenTask =
                                    new PostResponseAsyncTask(getContext(), AnfrageDaten, false, new AsyncResponse() {
                                        @Override
                                        public void processFinish(String s) {
                                            if (s.equals("error")) {
                                                Toasty.error(getContext(), "Löschen " + s, Toast.LENGTH_LONG, true).show();
                                            } else {
                                                getLinkedMap().remove(getItem(position).getIdentifier());
                                                notifyDataSetChanged();
                                                confirmationDialog.dismiss();
                                                Toasty.success(getContext(), "Gelöscht", Toast.LENGTH_SHORT, true).show();
                                            }
                                        }
                                    });
                            LöschenTask.execute("http://financemanagermm.sytes.net/fmclient/kooperation_anfrage_löschen.php");
                        }
                    });
                    confirmationDialog.show();
                }
            });
            mHolder.btnBestätigen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomAlertDialog confirmationDialog = new CustomAlertDialog(getContext(), "Bestätigen", "Wollen Sie die Kooperation starten?", "Starten", "Abbrechen");
                    confirmationDialog.setOkButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, String> AnfrageDaten = new HashMap<String, String>();
                            AnfrageDaten.put("AnfrageID", String.valueOf(AnfrageItem.getIdentifier()));

                            PostResponseAsyncTask StartenTask =
                                    new PostResponseAsyncTask(getContext(), AnfrageDaten, false, new AsyncResponse() {
                                        @Override
                                        public void processFinish(String s) {
                                            if (s.equals("error")) {
                                                Toasty.error(getContext(), "Fehler beim Starten: " + s, Toast.LENGTH_LONG, true).show();
                                            } else {
                                                Kooperationen.resetInitialize();
                                                Kooperationen.initializeKooperationen(new KooperationenCallback() {
                                                    @Override
                                                    public void onKooperationenSuccessfullyLoaded(LinkedHashMap<Integer, Kooperation> Kooperationen) {
                                                        Konten.initializeKonten(new FinanceManagerCallback() {
                                                            @Override
                                                            public void onDataUpdated(LinkedHashMap linkedHashMap) {

                                                            }
                                                        });
                                                        Gemeinsame_Finanzen gemeinsameFinanzenActivity = (Gemeinsame_Finanzen) getContext();
                                                        gemeinsameFinanzenActivity.getFragmentKooperationen().Kooperationen_Aktualisieren();
                                                        Toasty.success(getContext(), "Kooperation fertiggestellt", Toast.LENGTH_SHORT, true).show();
                                                        gemeinsameFinanzenActivity.getViewPager().setCurrentItem(0, true);
                                                    }
                                                });
                                            }
                                        }
                                    });
                            StartenTask.execute("http://financemanagermm.sytes.net/fmclient/kooperation_anfrage_starten.php");
                            Toasty.success(getContext(), "Kooperation wurde gestartet, Sie erhalten eine Meldung wenn der Server die Kooperation erstellt hat", Toast.LENGTH_LONG, true).show();
                            getLinkedMap().remove(AnfrageItem.getIdentifier());
                            notifyDataSetChanged();
                            confirmationDialog.dismiss();
                        }
                    });
                   confirmationDialog.show();
                }
            });
            mHolder.btnBestätigen.setEnabled(AnfrageItem.getMinOneAccepted());
            mHolder.btnBenutzerHinzufügen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gemeinsame_Finanzen_Anfrage_NeueAnfrage_InviteUserDialog inviteDialog = new Gemeinsame_Finanzen_Anfrage_NeueAnfrage_InviteUserDialog(
                            getContext(), getItem(position).getIdentifier(), getItem(position).isGleichverteilung(), getItemCount(), new Gemeinsame_Finanzen_Anfrage_BenutzerAbfragenCallback() {
                        @Override
                        public void onBenutzerSuccessfullyQueried(KooperationAnfrageBenutzer Benutzer) {
                            Gemeinsame_Finanzen_Fragment_Anfrage_CardView_BenutzerAdapter personenAdapter = (Gemeinsame_Finanzen_Fragment_Anfrage_CardView_BenutzerAdapter) mHolder.lvPersonen.getAdapter();
                            personenAdapter.getLinkedMap().put(Benutzer.getMail(), Benutzer);
                            int totalHeight = 0;
                            int maximumHeight = 72 * 3;
                            for (int i = 0; i < BenutzerAdapter.getCount(); i++) {
                                View listItem = BenutzerAdapter.getView(i, null, mHolder.lvPersonen);
                                listItem.measure(0, 0);
                                totalHeight += listItem.getMeasuredHeight();
                            }

                            ViewGroup.LayoutParams params = mHolder.lvPersonen.getLayoutParams();
                            if (totalHeight <= maximumHeight) {
                                params.height = totalHeight + (mHolder.lvPersonen.getDividerHeight() * (BenutzerAdapter.getCount() - 1));
                            } else {
                                params.height = maximumHeight + (mHolder.lvPersonen.getDividerHeight() * (BenutzerAdapter.getCount() - 1));
                            }
                            mHolder.lvPersonen.setLayoutParams(params);
                            mHolder.lvPersonen.requestLayout();
                            personenAdapter.notifyDataSetChanged();
                            notifyDataSetChanged();
                        }
                    }
                    );
                    inviteDialog.show();
                }
            });
            mHolder.btnBenutzerHinzufügen.setVisibility(View.GONE);
        } else {
            mHolder.btnBestätigen.setText("Bestätigen");
            mHolder.btnLöschen.setText("Ablehnen");
            if (EigenerEintrag.getStatus() == KooperationAnfrageBenutzer.AnfrageBenutzerStatus.BESTÄTIGT) {
                mHolder.btnBestätigen.setEnabled(false);
                mHolder.btnLöschen.setEnabled(true);
            } else if (EigenerEintrag.getStatus() == KooperationAnfrageBenutzer.AnfrageBenutzerStatus.ABGELEHNT) {
                mHolder.btnBestätigen.setEnabled(true);
                mHolder.btnLöschen.setEnabled(false);
            }
            mHolder.btnBestätigen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Kooperation wurde noch nicht gestartet
                    if (AnfrageItem.getKooperationID() == 0) {
                        CustomAlertDialog confirmationDialog = new CustomAlertDialog(getContext(), "Bestätigen", "Wollen Sie die Anfrage annehmen?", "Annehmen", "Abbrechen");
                        confirmationDialog.setOkButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Benutzerdaten_Update(AnfrageItem, finalEigenerEintrag, KooperationAnfrageBenutzer.AnfrageBenutzerStatus.BESTÄTIGT);
                                confirmationDialog.dismiss();
                                notifyDataSetChanged();
                            }
                        });
                        confirmationDialog.show();
                    } else {
                        //Kooperation wurde gestartet, Benutzerdaten updaten und der Kooperation hinzufügen
                        CustomAlertDialog confirmationDialog = new CustomAlertDialog(getContext(), "Bestätigen", "Wollen Sie der Kooperation beitreten?", "Beitreten", "Abbrechen");
                        confirmationDialog.setOkButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Benutzer_Nachträglich_Hinzufügen(AnfrageItem, finalEigenerEintrag);
                                getLinkedMap().remove(AnfrageItem.getIdentifier());
                                confirmationDialog.dismiss();
                                notifyDataSetChanged();
                            }
                        });
                        confirmationDialog.show();
                    }
                }
            });
            mHolder.btnLöschen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomAlertDialog confirmationDialog = new CustomAlertDialog(getContext(), "Bestätigen", "Wollen Sie die Anfrage wirklich ablehnen?", "Ablehnen", "Abbrechen");
                    confirmationDialog.setOkButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Benutzerdaten_Update(AnfrageItem, finalEigenerEintrag, KooperationAnfrageBenutzer.AnfrageBenutzerStatus.ABGELEHNT);
                            getLinkedMap().remove(AnfrageItem.getIdentifier());
                            confirmationDialog.dismiss();
                            notifyDataSetChanged();
                        }
                    });
                    confirmationDialog.show();
                }
            });
            mHolder.btnBenutzerHinzufügen.setVisibility(View.GONE);
            if (EigenerEintrag.getStatus() == KooperationAnfrageBenutzer.AnfrageBenutzerStatus.OFFEN) {
                mHolder.btnBestätigen.setEnabled(true);
                mHolder.btnLöschen.setEnabled(true);
            } else if (EigenerEintrag.getStatus() == KooperationAnfrageBenutzer.AnfrageBenutzerStatus.ABGELEHNT) {
                mHolder.btnBestätigen.setEnabled(true);
                mHolder.btnLöschen.setEnabled(false);
            } else if (EigenerEintrag.getStatus() == KooperationAnfrageBenutzer.AnfrageBenutzerStatus.BESTÄTIGT) {
                mHolder.btnBestätigen.setEnabled(false);
                mHolder.btnLöschen.setEnabled(true);
            }
        }
    }

    private void Benutzerdaten_Update(KooperationAnfrage anfrage, KooperationAnfrageBenutzer benutzerEintrag, KooperationAnfrageBenutzer.AnfrageBenutzerStatus status) {
        HashMap<String, String> BenutzerDaten = new HashMap<String, String>();

        BenutzerDaten.put("Status", status.toString());
        BenutzerDaten.put("Datum", GlobaleVariablen.getInstance().getSQL_DateFormat().format(Calendar.getInstance().getTime()));
        BenutzerDaten.put("AnfrageID", String.valueOf(anfrage.getIdentifier()));
        BenutzerDaten.put("BenutzerID", String.valueOf(benutzerEintrag.getBenutzerID()));

        PostResponseAsyncTask UpdateTask =
                new PostResponseAsyncTask(getContext(), BenutzerDaten, false, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if (s.equals("success")) {
                            Toasty.success(getContext(), "Gespeichert", Toast.LENGTH_SHORT, true).show();
                        } else {
                            Toasty.error(getContext(), "Update Error: " + s, Toast.LENGTH_LONG, true).show();
                        }
                    }
                });
        UpdateTask.execute("http://financemanagermm.sytes.net/fmclient/kooperation_anfrage_benutzer_update.php");

        benutzerEintrag.setStatus(status);
    }

    private void Benutzer_Nachträglich_Hinzufügen(KooperationAnfrage anfrage, KooperationAnfrageBenutzer benutzer) {
        HashMap BenutzerDaten = new HashMap();

        BenutzerDaten.put("KooperationID", String.valueOf(anfrage.getKooperationID()));
        BenutzerDaten.put("AnfrageID", String.valueOf(anfrage.getIdentifier()));
        BenutzerDaten.put("BenutzerID", String.valueOf(benutzer.getBenutzerID()));

        PostResponseAsyncTask HinzufügenTask =
                new PostResponseAsyncTask(getContext(), BenutzerDaten, false, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if (s.equals("error")) {
                            Toasty.error(getContext(), "Fehler beim Beitreten", Toast.LENGTH_SHORT, true).show();
                        } else {
                            Kooperationen.resetInitialize();
                            Kooperationen.initializeKooperationen(new KooperationenCallback() {
                                @Override
                                public void onKooperationenSuccessfullyLoaded(LinkedHashMap<Integer, Kooperation> Kooperationen) {
                                    Gemeinsame_Finanzen gemeinsameFinanzenActivity = (Gemeinsame_Finanzen) getContext();
                                    gemeinsameFinanzenActivity.getFragmentKooperationen().Kooperationen_Aktualisieren();
                                    Toasty.success(getContext(), "Beigetreten", Toast.LENGTH_SHORT, true).show();
                                    gemeinsameFinanzenActivity.getViewPager().setCurrentItem(0, true);
                                    Konten.initializeKonten(new FinanceManagerCallback() {
                                        @Override
                                        public void onDataUpdated(LinkedHashMap linkedHashMap) {

                                        }
                                    });
                                }});
                        }
                    }
                });
        HinzufügenTask.execute("http://financemanagermm.sytes.net/fmclient/kooperation_anfrage_benutzer_nachträglich_hinzufügen.php");
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView LeftImage;
        private TextView Titel;
        private TextView SinglePartner;
        private TextView Erstelldatum;
        private MaterialButton btnBestätigen;
        private MaterialButton btnLöschen;
        private ListView lvPersonen;
        private ListView lvKonten;
        private ExpansionHeader expandableHeader;
        private ExpansionLayout expandableLayout;
        private AppCompatImageButton btnBenutzerHinzufügen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            LeftImage = itemView.findViewById(R.id.LeftImage);
            Titel = itemView.findViewById(R.id.Titel);
            SinglePartner = itemView.findViewById(R.id.PartnerHeader);
            Erstelldatum = itemView.findViewById(R.id.Erstelldatum);
            btnBestätigen = itemView.findViewById(R.id.btnBestätigen);
            btnLöschen = itemView.findViewById(R.id.btnLöschen);
            lvPersonen = itemView.findViewById(R.id.lvPersonen);
            lvKonten = itemView.findViewById(R.id.lvKonten);
            expandableHeader = itemView.findViewById(R.id.TitleLayout);
            expandableLayout = itemView.findViewById(R.id.expansionLayout);
            btnBenutzerHinzufügen = itemView.findViewById(R.id.btnBenutzer_Hinzufügen);
        }
    }
}