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
    private String updateURL;
    private String createURL;
    private String deleteURL;
    private RadioGroup rdbGroupKategorieArt;
    private MaterialRadioButton rdbÜberkategorie;
    private MaterialRadioButton rdbUnterkategorie;
    private Buchungskategorie_Update_Interface callback;

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
                int Rot = Color.red(actColor);
                int Grün = Color.green(actColor);
                int Blau = Color.blue(actColor);

                if(rdbÜberkategorie.isChecked()) {
                    kategorie = new Buchungshauptkategorie(0,
                            0,
                            txtKategorieName.getEditText().getText().toString().trim(),
                            Rot, Grün, Blau,
                            new LinkedHashMap<Integer, Buchungskategorie>()
                            );
                } else {
                    kategorie = new Buchungskategorie(0,
                            ((Buchungshauptkategorie) cboÜberkategorie.getTag()).getId(),
                            txtKategorieName.getEditText().getText().toString().trim(),
                            Rot, Grün, Blau,
                            (swtAusgabe.isChecked()) ? Buchungskategorie.BuchTyp.AUSGABE : Buchungskategorie.BuchTyp.EINNAHME);
                }

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

        adapterÜberkategorie = new Verwaltung_Kategorien_Übersicht_Bearbeiten_Dialog_ÜberkateogrieAdapter(getContext(), Buchungskategorien.getBuchungskategorien());
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
        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("kategorieId", String.valueOf((update) ? kategorie.getId() : 0));
        postData.put("red", String.valueOf(Color.red(actColor))) ;
        postData.put("green", String.valueOf(Color.green(actColor)));
        postData.put("blue", String.valueOf(Color.blue(actColor))) ;
        if(!update) postData.put("UserID", String.valueOf(GlobaleVariablen.getInstance().getUserId()));
        if(update) {
            postData.put("üKatId", String.valueOf((!kategorie.isÜberkategorie()) ? ((Buchungshauptkategorie) cboÜberkategorie.getTag()).getId() : 0));
        } else {
            postData.put("üKatId", String.valueOf((rdbUnterkategorie.isChecked()) ? ((Buchungshauptkategorie) cboÜberkategorie.getTag()).getId() : 0));
        }
        postData.put("kategorieName", txtKategorieName.getEditText().getText().toString());
        postData.put("buchtyp", (swtAusgabe.isChecked()) ? "0" :  "1");

        updateURL =  getContext().getResources().getString(R.string.PHP_Scripts_Kategorie_Update);
        createURL = getContext().getResources().getString(R.string.PHP_Scripts_Kategorie_Anlegen);

        // the response listener
        JsonObjectRequest kategorieUpdateRequest = new JsonObjectRequest(Request.Method.POST, updateURL, new JSONObject(postData),
                response -> {
                    kategorie.setBeschreibung(txtKategorieName.getEditText().getText().toString());
                    kategorie.updateColor(actColor);
                    if(!kategorie.isÜberkategorie()) {
                        kategorie.setBuchtyp((swtAusgabe.isChecked()) ? Buchungskategorie.BuchTyp.AUSGABE : Buchungskategorie.BuchTyp.EINNAHME);
                        kategorie.setÜKatId(((Buchungshauptkategorie) cboÜberkategorie.getTag()).getId());
                    }
                    callback.onBuchungskategorieChanged(kategorie);
                    dismiss();
                    Toasty.success(getContext(), "Gespeichert", Toast.LENGTH_SHORT, true).show();
                },
                error -> Log.e("Kategorie_Update_Error", error.getMessage()));

        // the response listener
        JsonObjectRequest kategorieAnlegenRequest = new JsonObjectRequest(Request.Method.POST, createURL, new JSONObject(postData),
                response -> {
                    try {
                        kategorie = (rdbUnterkategorie.isChecked()) ?  new Buchungskategorie(response.getInt("Id"),
                                Integer.valueOf(postData.get("üKatId")),
                                postData.get("kategorieName"),
                                Integer.valueOf(postData.get("red")),
                                Integer.valueOf(postData.get("green")),
                                Integer.valueOf(postData.get("blue")),
                                (swtAusgabe.isChecked()) ? Buchungskategorie.BuchTyp.AUSGABE :  Buchungskategorie.BuchTyp.EINNAHME
                        ) : new Buchungshauptkategorie(response.getInt("Id"),
                                0,
                                postData.get("kategorieName"),
                                Integer.valueOf(postData.get("red")),
                                Integer.valueOf(postData.get("green")),
                                Integer.valueOf(postData.get("blue")),
                                new LinkedHashMap<>()
                        ) ;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(rdbUnterkategorie.isChecked()) {
                        Buchungskategorien.
                                getBuchungskategorien().
                                get(kategorie.getÜKatId()).
                                getUnterkategorien().
                                put(kategorie.getId(), kategorie);
                    } else {
                        Buchungskategorien.getBuchungskategorien().put(kategorie.getId(), (Buchungshauptkategorie) kategorie);
                    }
                    callback.onBuchungskategorieChanged(kategorie);
                    dismiss();
                    Toasty.success(getContext(), "Gespeichert", Toast.LENGTH_SHORT, true).show();
                },
                error -> Log.e("Kategorie_Anlegen_Error", error.getMessage()));

        if(update) {
            ApplicationController.getInstance().addToRequestQueue(kategorieUpdateRequest);
        } else {
            ApplicationController.getInstance().addToRequestQueue(kategorieAnlegenRequest);
        }
        ApplicationController.getInstance().getRequestQueue().start();
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

                HashMap<String, String> postData = new HashMap<String, String>();
                postData.put("kategorieId", String.valueOf(kategorie.getId()));
                CustomAlertDialog dialog = confirmDialog;
                deleteURL =  getContext().getResources().getString(R.string.PHP_Scripts_Kategorie_Löschen);

                // the response listener
                JsonObjectRequest kategorieDeleteRequest = new JsonObjectRequest(Request.Method.POST, deleteURL, new JSONObject(postData),
                        response -> {
                            try {
                                if(response.getInt("Result") == 0) {
                                    Toasty.error(getContext(), "Kategorie konnte nicht gelöscht werden. Es existieren Buchungen mit dieser Kategorie.", Toast.LENGTH_LONG, true).show();
                                } else {
                                    if(kategorie.isÜberkategorie()) {
                                        Buchungskategorien.getBuchungskategorien().remove(kategorie.getId());
                                    } else {
                                        Buchungskategorien.getBuchungskategorien().get(kategorie.getÜKatId()).getUnterkategorien().remove(kategorie.getId());
                                    }
                                    Toasty.success(getContext(), "Gelöscht", Toast.LENGTH_SHORT, true).show();
                                    callback.onBuchungskategorieChanged(kategorie);
                                }
                                confirmDialog.dismiss();
                                dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            Log.e("Kategorie_Delete_Error", error.getMessage());
                        });

                ApplicationController.getInstance().addToRequestQueue(kategorieDeleteRequest);
                ApplicationController.getInstance().getRequestQueue().start();
            }
        };
        confirmDialog.setOkButtonClickListener(okListener);
        confirmDialog.show();
    }
}