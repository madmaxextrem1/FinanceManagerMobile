package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.tiper.MaterialSpinner;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorien;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation_Adapter;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.FinanzbuchungTokens;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Kooperationen;
import net.sytes.financemanagermm.financemanagermobile.R;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class Verwaltung_Daueraufträge_Fragment_Details extends Fragment {
    private TextInputLayout cboKategorie;
    private ChipGroup chgrpMerkmale;
    private MaterialSpinner cboKooperation;
    private Kooperation_Adapter kooperationAdapter;
    private TextInputLayout txtNächsteAusführung;
    private Dauerauftrag dauerauftrag;
    private Dauerauftrag.Dauerauftrag_Art dauerauftragArt;
    private int kooperationID;
    private boolean callFromKooperation;

    public Verwaltung_Daueraufträge_Fragment_Details(Dauerauftrag dauerauftrag, Dauerauftrag.Dauerauftrag_Art dauerauftragArt, int kooperationID, boolean callFromKooperation) {
        this.dauerauftrag = dauerauftrag;
        this.dauerauftragArt = dauerauftragArt;
        this.kooperationID = kooperationID;
        this.callFromKooperation = callFromKooperation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ReturnView = inflater.inflate(R.layout.verwaltung_dauerauftraege_fragment_details, container, false);

        cboKooperation = ReturnView.findViewById(R.id.cboKooperation);

        if (callFromKooperation) {
            cboKooperation.setVisibility(View.GONE);
        } else {
            kooperationAdapter = new Kooperation_Adapter(getContext());
            kooperationAdapter.getLinkedMap().put(0, new Kooperation());
            kooperationAdapter.getLinkedMap().putAll(Kooperationen.getAktiveKooperationen());
            cboKooperation.setAdapter(kooperationAdapter);
            kooperationAdapter.notifyDataSetChanged();
            cboKooperation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
                    Verwaltung_Daueraufträge_Detailansicht detailansicht = (Verwaltung_Daueraufträge_Detailansicht) getParentFragment();
                    if (kooperationAdapter.getItem(i).getIdentifier() == 0) {
                        cboKooperation.setTag(null);
                        detailansicht.setKooperationID(0);
                        cboKooperation.setSelection(-1);
                    } else {
                        cboKooperation.setTag(kooperationAdapter.getItem(i));
                        detailansicht.setKooperationID(kooperationAdapter.getItem(i).getId());
                        cboKooperation.getEditText().setText(kooperationAdapter.getItem(i).getBeschreibung());
                    }
                }

                @Override
                public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

                }
            });
        }

        cboKategorie = ReturnView.findViewById(R.id.cboKategorie);
        cboKategorie.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getChildFragmentManager();
                Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog dialog = Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog.newInstance("Kategorie auswählen", (Buchungskategorie) cboKategorie.getTag());
                dialog.setCallback(new Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog.Callback() {
                    @Override
                    public void onActionClick(Buchungskategorie Eintrag) {
                        if (Eintrag != null) {
                            cboKategorie.getEditText().setText(Eintrag.getBeschreibung());
                            cboKategorie.setTag(Eintrag);
                        }
                    }
                });
                dialog.show(fragmentManager, "dialog_kategorie_auswahl");
            }
        });
        chgrpMerkmale = ReturnView.findViewById(R.id.ChipGroupMerkmale);
        txtNächsteAusführung = ReturnView.findViewById(R.id.txtNächsteAusführung);

        for (FinanzbuchungToken eintrag : FinanzbuchungTokens.getTokens()) {
            Chip chip = new Chip(getContext());
            chip.setText(eintrag.getBeschreibung());
            chip.setChipBackgroundColorResource(R.color.ChipBackGroundUnchecked);
            chip.setCheckable(true);
            chip.setTag(eintrag);
            chip.setTextColor(getResources().getColor(R.color.white, null));
            chip.setCheckedIconVisible(false);
            chip.setCloseIconVisible(false);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        chip.setChipBackgroundColorResource(R.color.ChipBackGroundChecked);
                        chgrpMerkmale.setTag(chip.getTag());
                    } else {
                        chip.setChipBackgroundColorResource(R.color.ChipBackGroundUnchecked);
                        chgrpMerkmale.setTag(null);
                    }
                }
            });
            chgrpMerkmale.addView(chip);
        }

        txtNächsteAusführung.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long selection = 0;
                if (!txtNächsteAusführung.getEditText().getText().toString().equals("")) {
                    try {
                        Date currentDate = GlobaleVariablen.getInstance().getDE_DateFormat().parse(txtNächsteAusführung.getEditText().getText().toString().trim());
                        selection = currentDate.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    selection = Calendar.getInstance().getTime().getTime();
                }

                MaterialDatePicker<?> picker = Globale_Funktionen.getMaterialDatePicker(getString(R.string.DatePickerDialogTitle_Zeitpunkt), Optional.of(new Date(selection)), new MaterialPickerOnPositiveButtonClickListener<Object>() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Long selectedDate = (Long) selection;
                        Date Datum = new Date(selectedDate);
                        txtNächsteAusführung.getEditText().setText(GlobaleVariablen.getInstance().getDE_DateFormat().format(Datum));
                    }
                });
                picker.show(getChildFragmentManager(), "Test");
            }
        });

        if (dauerauftrag != null) {
            if (dauerauftrag.getDauerauftragArt() == Dauerauftrag.Dauerauftrag_Art.NORMAL) {
                cboKategorie.setTag(Buchungskategorien.getBuchungskategorieById(dauerauftrag.getKategorieID()));
                cboKategorie.getEditText().setText(Buchungskategorien.getBuchungskategorieById(dauerauftrag.getKategorieID()).getBeschreibung());
            } else {
                cboKategorie.setVisibility(View.GONE);
                cboKooperation.setVisibility(View.GONE);
            }

            for (int i = 0; i < chgrpMerkmale.getChildCount(); i++) {
                FinanzbuchungToken TokenEintrag = (FinanzbuchungToken) chgrpMerkmale.getChildAt(i).getTag();
                Chip MerkmalChip = (Chip) chgrpMerkmale.getChildAt(i);
                if (TokenEintrag.getId() == dauerauftrag.getTokenID()) {
                    MerkmalChip.setChecked(true);
                    break;
                }
            }

            Objects.requireNonNull(txtNächsteAusführung.getEditText()).setText(GlobaleVariablen.getInstance().getDE_DateFormat().format(dauerauftrag.getNächsteAusführung()));

            if (!callFromKooperation) {
                cboKooperation.setSelection((kooperationID == 0) ? -1 : kooperationAdapter.getItemPositionById(kooperationID));
            }

        } else {
            if (dauerauftragArt == Dauerauftrag.Dauerauftrag_Art.UMBUCHUNG) {
                cboKategorie.setVisibility(View.GONE);
                cboKooperation.setVisibility(View.GONE);
            }
        }
        return ReturnView;
    }

    public int getTokenID () {
        return (chgrpMerkmale.getTag() != null) ? ((FinanzbuchungToken) chgrpMerkmale.getTag()).getId() : 0;
    }

    public int getKategorieId() {
        return (cboKategorie.getTag() != null) ? ((Buchungskategorie) cboKategorie.getTag()).getId() : 0;
    }

    public String getNächsteAusführung() {
        return txtNächsteAusführung.getEditText().getText().toString().trim();
    }
}