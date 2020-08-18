package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.tiper.MaterialSpinner;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungshauptkategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie_Update_Interface;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorien;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.ApplicationController;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.ServerCommunication.ServerCommunication;
import net.sytes.financemanagermm.financemanagermobile.ServerCommunication.ServerCommunicationInterface;
import net.sytes.financemanagermm.financemanagermobile.Sign_In_Up.FinanceManagerMobileApplication;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.CustomAlertDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import es.dmoral.toasty.Toasty;

public class Verwaltung_Kategorien_Übersicht_Bearbeiten_Dialog extends DialogFragment{
    private MaterialButton btnSave;
    private MaterialButton btnClose;
    private FloatingActionButton btnColor;
    private AppCompatImageButton btnDelete;
    private MaterialSpinner cboÜberkategorie;
    private Verwaltung_Kategorien_Übersicht_Bearbeiten_Dialog_ÜberkateogrieAdapter adapterÜberkategorie;
    private TextInputLayout txtKategorieName;
    private SwitchMaterial swtAusgabe;
    private Buchungskategorie kategorie;
    private boolean update;
    private int actColor;
    private RadioGroup rdbGroupKategorieArt;
    private MaterialRadioButton rdbÜberkategorie;
    private MaterialRadioButton rdbUnterkategorie;
    private Buchungskategorie_Update_Interface callback;
    private ServerCommunication serverCommunication;

    public Verwaltung_Kategorien_Übersicht_Bearbeiten_Dialog (Buchungskategorie buchungskategorie, boolean update, Buchungskategorie_Update_Interface callback) {
        this.kategorie = buchungskategorie;
        this.update = update;
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verwaltung_kategorien_uebersicht_bearbeiten_dialog, container,false);

        serverCommunication = new ServerCommunication(getContext());

        btnSave = view.findViewById(R.id.btnSpeichern);
        btnClose = view.findViewById(R.id.btnAbbrechen);
        btnColor = view.findViewById(R.id.fab);
        btnDelete = view.findViewById(R.id.btnDelete);
        cboÜberkategorie = view.findViewById(R.id.Überkategorie);
        txtKategorieName = view.findViewById(R.id.KategorieName);
        swtAusgabe = view.findViewById(R.id.swtAusgabe);
        rdbGroupKategorieArt = view.findViewById(R.id.rdbGroupKategorieArt);
        rdbÜberkategorie = view.findViewById(R.id.rdbÜberkategorie);
        rdbUnterkategorie = view.findViewById(R.id.rdbUnterkategorie);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPickerDialog = ColorPickerDialog.newBuilder()
                        .setColor(actColor)
                        .setShowAlphaSlider(false)
                        .setDialogTitle(R.string.colorpickerdialog_title)
                        .setAllowPresets(true)
                        .create();
                colorPickerDialog.setColorPickerDialogListener(new ColorPickerDialogListener() {
                    @Override
                    public void onColorSelected(int dialogId, int color) {
                        setColorOfColorPickerButton(color);
                    }

                    @Override
                    public void onDialogDismissed(int dialogId) {

                    }
                });
                colorPickerDialog.show(getFragmentManager(), null);

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateInput()) return;
                updateOrCreateKategorie();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteKategorie(kategorie, new Buchungskategorie_Update_Interface() {
                    @Override
                    public void onBuchungskategorieChanged(Buchungskategorie Kategorie) {
                        callback.onBuchungskategorieChanged(Kategorie);
                    }
                });
            }
        });
        rdbUnterkategorie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    cboÜberkategorie.setVisibility(View.VISIBLE);
                    swtAusgabe.setVisibility(View.VISIBLE);
                }
            }
        });
        rdbÜberkategorie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    cboÜberkategorie.setVisibility(View.GONE);
                    swtAusgabe.setVisibility(View.GONE);
                }
            }
        });

        adapterÜberkategorie = new Verwaltung_Kategorien_Übersicht_Bearbeiten_Dialog_ÜberkateogrieAdapter(getContext(), FinanceManagerMobileApplication.getInstance().getDataManagement().getCategories());
        cboÜberkategorie.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
                cboÜberkategorie.setTag(adapterÜberkategorie.getItem(i));
                cboÜberkategorie.getEditText().setText(adapterÜberkategorie.getItem(i).getBeschreibung());
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });
        cboÜberkategorie.setAdapter(adapterÜberkategorie);
        adapterÜberkategorie.notifyDataSetChanged();

        if(update) {
            rdbGroupKategorieArt.setVisibility(View.GONE);
            txtKategorieName.getEditText().setText(kategorie.getBeschreibung());
            cboÜberkategorie.setSelection(adapterÜberkategorie.getItemPosition(kategorie));
            actColor = kategorie.getColor();
            setColorOfColorPickerButton(actColor);
            swtAusgabe.setChecked(!kategorie.getBuchtyp().getDatabaseValue());

            if(kategorie.isÜberkategorie()) {
                swtAusgabe.setChecked(true);
                cboÜberkategorie.setVisibility(View.GONE);
                swtAusgabe.setVisibility(View.GONE);
            }

        } else {
            btnDelete.setVisibility(View.GONE);
            swtAusgabe.setChecked(true);
            actColor = getContext().getColor(R.color.primary);
            setColorOfColorPickerButton(actColor);
        }

        return view;
    }

    private void setColorOfColorPickerButton(int color) {
        int Rot = Color.red(color);
        int Grün = Color.green(color);
        int Blau = Color.blue(color);
        float alpha = new Globale_Funktionen().Helligkeit_Berechnen(Rot, Grün, Blau);

        int foregroundTint;
        if ((int) alpha >= 255 / 2) {
            foregroundTint = Color.BLACK;
        } else {
            foregroundTint = Color.WHITE;
        }

        btnColor.setBackgroundTintList(ColorStateList.valueOf(color));
        ImageViewCompat.setImageTintList(
                btnColor,
                ColorStateList.valueOf(foregroundTint)
        );
        actColor = color;
    }

    private boolean validateInput() {
        if((!update && rdbUnterkategorie.isChecked()) || !kategorie.isÜberkategorie()) {
            if (cboÜberkategorie.getTag() == null) {
                Toasty.error(getContext(), "Sie müssen eine Überkategorie auswählen.", Toast.LENGTH_LONG, true).show();
                return false;
            }
        }

        if(txtKategorieName.getEditText().getText().toString().equals("")) {
                Toasty.error(getContext(), "Sie müssen einen Namen für die Kategorie eingeben.", Toast.LENGTH_LONG, true).show();
                return false;
        }

        return true;
    }

    private void updateOrCreateKategorie() {
        Buchungskategorie category = assembleCategory(update);

        if(update) {
            serverCommunication.updateCategory(category, new ServerCommunicationInterface.GeneralCommunicationCallback<Buchungskategorie>() {
                @Override
                public void onRequestCompleted(Buchungskategorie data) {
                    if(!category.isÜberkategorie()) {
                        FinanceManagerMobileApplication.getInstance().getDataManagement().updateCreateSubCategory(category);
                    } else {
                        FinanceManagerMobileApplication.getInstance().getDataManagement().updateCreateMainCategory((Buchungshauptkategorie) category);
                    }
                    callback.onBuchungskategorieChanged(kategorie);
                    dismiss();
                    Toasty.success(getContext(), "Gespeichert", Toast.LENGTH_SHORT, true).show();
                }
            });
        } else {
            serverCommunication.createCategory(FinanceManagerMobileApplication.getInstance().getDataManagement().getCurrentUser().getUserId(),
                    category, new ServerCommunicationInterface.GeneralCommunicationCallback<Buchungskategorie>() {
                        @Override
                        public void onRequestCompleted(Buchungskategorie data) {
                            if(rdbUnterkategorie.isChecked()) {
                                FinanceManagerMobileApplication.getInstance().getDataManagement().updateCreateSubCategory(category);
                            } else {
                                FinanceManagerMobileApplication.getInstance().getDataManagement().updateCreateMainCategory((Buchungshauptkategorie) category);
                            }
                            callback.onBuchungskategorieChanged(kategorie);
                            dismiss();
                            Toasty.success(getContext(), "Gespeichert", Toast.LENGTH_SHORT, true).show();
                        }
                    });
        }
    }
    private Buchungskategorie assembleCategory(boolean update) {
        int categoryId = (update) ? kategorie.getId() : 0;
        int red = Color.red(actColor);
        int green = Color.green(actColor);
        int blue = Color.blue(actColor);
        int mainCategoryId = (!kategorie.isÜberkategorie()) ? ((Buchungshauptkategorie) cboÜberkategorie.getTag()).getId() : 0;

        String categoryName = txtKategorieName.getEditText().getText().toString();
        Buchungskategorie.BuchTyp buchTyp = Buchungskategorie.BuchTyp.createFromDataBaseValue(!swtAusgabe.isChecked());

        Buchungskategorie category = (!this.kategorie.isÜberkategorie()) ?  new Buchungskategorie(categoryId,
                mainCategoryId,
                categoryName,
                red,
                green,
                blue,
                buchTyp
        ) : new Buchungshauptkategorie(categoryId,
                mainCategoryId,
                categoryName,
                red,
                green,
                blue,
                ((Buchungshauptkategorie) kategorie).getUnterkategorien()
        ) ;

        return category;
    }

    private void deleteKategorie(Buchungskategorie kategorie, Buchungskategorie_Update_Interface callback) {
        final CustomAlertDialog confirmDialog = new CustomAlertDialog(getContext(), "Bestätigen", "Wollen Sie diese Kategorie wirklich löschen?", "Löschen", "Abbrechen");

        View.OnClickListener okListener =  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kategorie.isÜberkategorie() && ((Buchungshauptkategorie) kategorie).getUnterkategorien().size() > 0) {
                    Toasty.error(getContext(), "Überkategorie konnte nicht gelöscht werden. Es sind noch Kategorien zugeordnet. Ordnen Sie zunächst alle Unterkategorien einer anderen Überkategorie zu.", Toast.LENGTH_LONG, true).show();
                    return;
                }

                serverCommunication.deleteCategory(kategorie.getId(), new ServerCommunicationInterface.GeneralCommunicationCallback<Boolean>() {
                    @Override
                    public void onRequestCompleted(Boolean data) {
                        if(!data) {
                            Toasty.error(getContext(), "Kategorie konnte nicht gelöscht werden. Es existieren Buchungen mit dieser Kategorie.", Toast.LENGTH_LONG, true).show();
                        } else {
                            if(kategorie.isÜberkategorie()) {
                                FinanceManagerMobileApplication.getInstance().getDataManagement()
                                        .removeMainCategory(kategorie.getId());
                            } else {
                                FinanceManagerMobileApplication.getInstance().getDataManagement()
                                        .removeSubCategory(kategorie.getÜKatId(), kategorie.getId());
                            }
                            Toasty.success(getContext(), "Gelöscht", Toast.LENGTH_SHORT, true).show();
                            callback.onBuchungskategorieChanged(kategorie);
                        }
                        confirmDialog.dismiss();
                        dismiss();
                    }
                });
            }
        };
        confirmDialog.setOkButtonClickListener(okListener);
        confirmDialog.show();
    }
}