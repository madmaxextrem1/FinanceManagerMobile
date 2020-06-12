package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.tiper.MaterialSpinner;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Finanzbuchung_Buchung;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.FinanzbuchungTokens;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Finanzbuchungen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.FinanzbuchungenCallback;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Konten;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Kooperationen;
import net.sytes.financemanagermm.financemanagermobile.Hauptmenu.Hauptmenu_Fragment_Auswertung;
import net.sytes.financemanagermm.financemanagermobile.Hauptmenu.Hauptmenu_Fragment_Buchungen;
import net.sytes.financemanagermm.financemanagermobile.Hauptmenu.Hauptmenu_Fragment_Buchungen_Filter;
import net.sytes.financemanagermm.financemanagermobile.Hauptmenu.NonNull;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto_Adapter;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static java.util.Calendar.YEAR;

public class Gemeinsame_Finanzen_Kooperation extends AppCompatActivity {
    private ViewPager viewPager;
    private Gemeinsame_Finanzen_Viewpager_Adapter fragmentPagerAdapter;
    private Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Adapter neueAnfrageBenutzerAdapter;
    private ListView lvBenutzerNeueAnfrageDialog;
    private Kooperation kooperation;
    private BottomNavigationView bottomNav;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView toolbarMonth;
    private AppCompatImageButton toolbarFilterButton;
    private Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gemeinsame_finanzen_kooperation_activity);

        kooperation = (Kooperation) getIntent().getExtras().getParcelable("Kooperation");

        bottomNav = findViewById(R.id.navigation_bottom);

        toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        // add the custom view to the action bar
        Objects.requireNonNull(actionBar).setCustomView(R.layout.hauptmenu_toolbar_layout);
        toolbarTitle = (TextView) actionBar.getCustomView().findViewById(
                R.id.Hauptmenu_Toolbar_Title);
        toolbar.setBackgroundColor(getResources().getColor(R.color.fm_Login_Background_Color));
        toolbarMonth = (TextView) actionBar.getCustomView().findViewById(
                R.id.Hauptmenu_Toolbar_Month);
        toolbarFilterButton = (AppCompatImageButton) actionBar.getCustomView().findViewById(R.id.Hauptmenu_Toolbar_FilterButton);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");
        Date currentTime = new Date();
        toolbarMonth.setText(formatter.format(currentTime));
        toolbar.setBackgroundColor(getResources().getColor(R.color.fm_Login_Background_Color));

        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(0);
        //SideMenuErstellen();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbarTitle.setText(bottomNav.getMenu().findItem(bottomNav.getSelectedItemId()).getTitle().toString());
        toolbarFilterButton.setVisibility(View.GONE);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            String Tag = "";
            switch (menuItem.getItemId()) {
                case R.id.mainmenu_bottomnav_home:
                    selectedFragment = new Gemeinsame_Finanzen_Kooperation_Fragment_Home(kooperation);
                    Tag = "Home";
                    toolbarFilterButton.setVisibility(View.GONE);
                    toolbarMonth.setVisibility(View.VISIBLE);
                    break;
                case R.id.mainmenu_bottomnav_buchungen:
                    toolbarFilterButton.setVisibility(View.VISIBLE);
                    toolbarMonth.setVisibility(View.GONE);
                    View filterfragment = getLayoutInflater().inflate(R.layout.hauptmenu_fragment_buchungen_filterfragment,null);
                    TextInputLayout txtBuchungstitel = (TextInputLayout) filterfragment.findViewById(R.id.txtBuchungstitel);
                    ChipGroup chipGroup = (ChipGroup) filterfragment.findViewById(R.id.ChipGroupMerkmale);
                    ChipGroup chipGroupBuchungstitelFilter = (ChipGroup) filterfragment.findViewById(R.id.ChipGroupFilterBuchungstitel);
                    TextInputLayout cboKategorie = (TextInputLayout) filterfragment.findViewById(R.id.cboKategorie);
                    MaterialSpinner cboKonto = (MaterialSpinner) filterfragment.findViewById(R.id.cboKonto);
                    MaterialSpinner cboKooperation = filterfragment.findViewById(R.id.cboKooperation);
                    TextInputLayout txtBetrag = filterfragment.findViewById(R.id.txtBetrag);
                    ChipGroup chipGroupBetrag = filterfragment.findViewById(R.id.ChipGroupFilterBetrag);
                    AppCompatImageButton btnFilter_Bestätigen = (AppCompatImageButton) filterfragment.findViewById(R.id.btn_Filter_Bestätigen);
                    AppCompatImageButton btnFilter_Zurücksetzen = (AppCompatImageButton) filterfragment.findViewById(R.id.btn_Filter_Zurücksetzen);

                    Chip EnhältChip = (Chip) chipGroupBuchungstitelFilter.findViewById(R.id.Buchungstitelfilter_Chip_Enthält);
                    Chip GleichChip = (Chip) chipGroupBuchungstitelFilter.findViewById(R.id.Buchungstitelfilter_Chip_Gleich);
                    Chip BeginntChip = (Chip) chipGroupBuchungstitelFilter.findViewById(R.id.Buchungstitelfilter_Chip_Beginnt);
                    EnhältChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                EnhältChip.setChipBackgroundColorResource(R.color.ChipBackGroundChecked);
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataTitelfilter(txtBuchungstitel.getEditText().getText().toString());
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataTitelfilterTyp(GlobaleVariablen.BuchungstitelFilter.ENHÄLT);
                            } else {
                                EnhältChip.setChipBackgroundColorResource(R.color.ChipBackGroundUnchecked);
                            }
                        }
                    });
                    GleichChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                GleichChip.setChipBackgroundColorResource(R.color.ChipBackGroundChecked);
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataTitelfilter(txtBuchungstitel.getEditText().getText().toString());
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataTitelfilterTyp(GlobaleVariablen.BuchungstitelFilter.GLEICH);
                            } else {
                                GleichChip.setChipBackgroundColorResource(R.color.ChipBackGroundUnchecked);
                            }
                        }
                    });
                    BeginntChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                BeginntChip.setChipBackgroundColorResource(R.color.ChipBackGroundChecked);
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataTitelfilter(txtBuchungstitel.getEditText().getText().toString());
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataTitelfilterTyp(GlobaleVariablen.BuchungstitelFilter.BEGINNT_MIT);
                            } else {
                                BeginntChip.setChipBackgroundColorResource(R.color.ChipBackGroundUnchecked);
                            }
                        }
                    });

                    Hauptmenu_Fragment_Buchungen_Filter.setFilterDataTitelfilterTyp(GlobaleVariablen.BuchungstitelFilter.GLEICH);

                    Hauptmenu_Fragment_Buchungen_Filter.setFilterDataTitelfilter(txtBuchungstitel.getEditText().getText().toString());
                    Hauptmenu_Fragment_Buchungen_Filter.setFilterDataTitelfilterTyp(GlobaleVariablen.BuchungstitelFilter.GLEICH);

                    ArrayList<Integer> ArrayToken = new ArrayList<Integer>();
                    for(FinanzbuchungToken Eintrag: FinanzbuchungTokens.getTokens()) {
                        View chipView = (View) getLayoutInflater().inflate(R.layout.hauptmenu_fragment_buchungen_filterfragment_merkmalchip, null);
                        Chip chip = (Chip) chipView.findViewById(R.id.Chip);
                        chip.setText(Eintrag.getBeschreibung());
                        chip.setId(Eintrag.getId());
                        chipGroup.addView(chip);
                        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    chip.setChipBackgroundColorResource(R.color.ChipBackGroundChecked);
                                    ArrayToken.add(chip.getId());
                                } else {
                                    chip.setChipBackgroundColorResource(R.color.ChipBackGroundUnchecked);
                                    ArrayToken.remove((Object) chip.getId());
                                }
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataTokenIDs(ArrayToken);
                            }
                        });
                    }
                    TextInputLayout txtZeitraumLayout = (TextInputLayout) filterfragment.findViewById(R.id.txtZeitraum);
                    Calendar calendar = Calendar.getInstance();
                    Date Heute = calendar.getTime();
                    calendar.set(calendar.get(YEAR),calendar.get(Calendar.MONTH) - 1, 1);
                    Date initialDatumVon = calendar.getTime();
                    String sInitialDatumVon = GlobaleVariablen.getInstance().getDE_DateFormat().format(initialDatumVon);
                    String sInitialDatumBis = GlobaleVariablen.getInstance().getDE_DateFormat().format(Heute);
                    GlobaleVariablen.getInstance().setHauptmenu_Fragment_Buchungen_Filter_arrTokenID(ArrayToken);
                    Hauptmenu_Fragment_Buchungen_Filter.setFilterDataZeitraum(initialDatumVon, Heute);
                    TextInputEditText txtZeitraum = (TextInputEditText) txtZeitraumLayout.getEditText();
                    Objects.requireNonNull(txtZeitraum).setText(sInitialDatumVon + " - " + sInitialDatumBis);
                    txtZeitraum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MaterialDatePicker.Builder<Pair<Long, Long>> builder =  MaterialDatePicker.Builder.dateRangePicker();
                            builder.setTitleText(R.string.DatePickerDialogTitle_Zeitraum);
                            //CalendarConstraints calendarConstraints = CalendarConstraints.Builder.DEFAULT_END;
                            //builder.setCalendarConstraints(CalendarConstraints.Builder.)
                            MaterialDatePicker<?> picker = builder.build();
                            picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Object>() {
                                @Override
                                public void onPositiveButtonClick(Object selection) {
                                    Pair<Long, Long> StartEnd = (Pair<Long, Long>) selection;
                                    Date DatumVon = new Date(((Pair<Long, Long>) selection).first);
                                    Date DatumBis = new Date(((Pair<Long, Long>) selection).second);
                                    txtZeitraum.setText(GlobaleVariablen.getInstance().getDE_DateFormat().format(DatumVon) + " - " + GlobaleVariablen.getInstance().getDE_DateFormat().format(DatumBis));
                                    Hauptmenu_Fragment_Buchungen_Filter.setFilterDataZeitraum(DatumVon, DatumBis);
                                }
                            });
                            picker.show(getSupportFragmentManager(),"Test");
                        }
                    });

                    Objects.requireNonNull(cboKategorie.getEditText()).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog dialog = Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog.newInstance("Kategorie auswählen", (Buchungskategorie) cboKategorie.getTag());
                            dialog.setCallback(new Verwaltung_Daueraufträge_Fragment_Details_Kategorie_Dialog.Callback() {
                                @Override
                                public void onActionClick(Buchungskategorie Eintrag) {
                                    if (Eintrag != null) {
                                        cboKategorie.getEditText().setText(Eintrag.getBeschreibung());
                                        cboKategorie.setTag(Eintrag);
                                        Hauptmenu_Fragment_Buchungen_Filter.setFilterDataBuchungskategorie(Eintrag);
                                    }
                                }
                            });
                            dialog.show(fragmentManager, "dialog_kategorie_auswahl");
                        }
                    });
                    cboKategorie.setEndIconOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cboKategorie.setTag(null);
                            cboKategorie.getEditText().setText("");
                            Hauptmenu_Fragment_Buchungen_Filter.setFilterDataBuchungskategorie(null);
                        }
                    });

                    Konto_Adapter konto__adapter;
                    konto__adapter = new Konto_Adapter(getBaseContext());
                    cboKonto.setAdapter(konto__adapter);
                    Konto keinKontoFilter = new Konto(0, "Kein Kontofilter", Konto.KontoArt.BANKKONTO, 0.00,0.00,Date.from(Instant.now()),false,0,0.00);
                    konto__adapter.getLinkedMap().put(keinKontoFilter.getIdentifier(), keinKontoFilter);
                    konto__adapter.getLinkedMap().putAll(Konten.getAktiveKonten());
                    konto__adapter.notifyDataSetChanged();

                    cboKonto.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
                            if(konto__adapter.getItem(i).getIdentifier()==0) {
                                materialSpinner.getEditText().setText("");
                                materialSpinner.setTag(null);
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataKonto(null);
                            }
                            else {
                                materialSpinner.getEditText().setText(konto__adapter.getItem(i).getKontoTitel());
                                materialSpinner.setTag(konto__adapter.getItem(i));
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataKonto(konto__adapter.getItem(i));
                            }
                        }

                        @Override
                        public void onNothingSelected(MaterialSpinner materialSpinner) {

                        }
                    });

                    Kooperation_Adapter kooperationAdapter = new Kooperation_Adapter(Gemeinsame_Finanzen_Kooperation.this);
                    kooperationAdapter.getLinkedMap().put(0, new Kooperation());
                    kooperationAdapter.getLinkedMap().putAll(Kooperationen.getAktiveKooperationen());
                    cboKooperation.setAdapter(kooperationAdapter);
                    kooperationAdapter.notifyDataSetChanged();
                    cboKooperation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @org.jetbrains.annotations.Nullable View view, int i, long l) {
                            if (kooperationAdapter.getItem(i).getIdentifier() == 0) {
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataKooperation(null);
                                cboKooperation.setSelection(-1);
                            } else {
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataKooperation(kooperationAdapter.getItem(i));
                                cboKooperation.getEditText().setText(kooperationAdapter.getItem(i).getBeschreibung());
                            }
                        }

                        @Override
                        public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

                        }
                    });

                    Chip GleichChipBetrag = (Chip) chipGroupBetrag.findViewById(R.id.Betragfilter_Chip_Gleich);
                    Chip GrößerGleichChipBetrag = (Chip) chipGroupBetrag.findViewById(R.id.Betragfilter_Chip_Größer);
                    Chip KleinerGleichChipBetrag = (Chip) chipGroupBetrag.findViewById(R.id.Betragfilter_Chip_Kleiner);
                    GleichChipBetrag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                GleichChipBetrag.setChipBackgroundColorResource(R.color.ChipBackGroundChecked);
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataBetrag(
                                        (txtBetrag.getEditText().getText().toString().equals("")) ? 0.0 : Double.parseDouble(txtBetrag.getEditText().getText().toString()
                                        ));
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataBetragFilterTyp(Hauptmenu_Fragment_Buchungen_Filter.BetragFilterTyp.GLEICH);
                            } else {
                                GleichChipBetrag.setChipBackgroundColorResource(R.color.ChipBackGroundUnchecked);
                            }
                        }
                    });
                    GrößerGleichChipBetrag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                GrößerGleichChipBetrag.setChipBackgroundColorResource(R.color.ChipBackGroundChecked);
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataBetrag((txtBetrag.getEditText().getText().toString().equals("")) ? 0.0 : Double.parseDouble(txtBetrag.getEditText().getText().toString()));
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataBetragFilterTyp(Hauptmenu_Fragment_Buchungen_Filter.BetragFilterTyp.GRÖßERGLEICH);
                            } else {
                                GrößerGleichChipBetrag.setChipBackgroundColorResource(R.color.ChipBackGroundUnchecked);
                            }
                        }
                    });
                    KleinerGleichChipBetrag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                KleinerGleichChipBetrag.setChipBackgroundColorResource(R.color.ChipBackGroundChecked);
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataBetrag((txtBetrag.getEditText().getText().toString().equals("")) ? 0.0 : Double.parseDouble(txtBetrag.getEditText().getText().toString()));
                                Hauptmenu_Fragment_Buchungen_Filter.setFilterDataBetragFilterTyp(Hauptmenu_Fragment_Buchungen_Filter.BetragFilterTyp.KLEINERGLEICH);
                            } else {
                                KleinerGleichChipBetrag.setChipBackgroundColorResource(R.color.ChipBackGroundUnchecked);
                            }
                        }
                    });
                    Hauptmenu_Fragment_Buchungen_Filter.setFilterDataBetragFilterTyp(Hauptmenu_Fragment_Buchungen_Filter.BetragFilterTyp.GLEICH);
                    final Drawer result = new DrawerBuilder()
                            .withActivity(Gemeinsame_Finanzen_Kooperation.this)
                            //.withSliderBackgroundColor(BackgroundColor)
                            .withTranslucentStatusBar(false)
                            .withActionBarDrawerToggleAnimated(true)
                            .withActionBarDrawerToggle(false)
                            .withTranslucentNavigationBar(true)
                            .withCustomView(filterfragment)
                            .build();
                    result.setGravity(Gravity.END);

                    btnFilter_Bestätigen.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_green_700)));
                    btnFilter_Zurücksetzen.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_blue_grey_700)));
                    btnFilter_Bestätigen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            result.closeDrawer();
                        }
                    });
                    btnFilter_Zurücksetzen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Objects.requireNonNull(txtZeitraum).setText(sInitialDatumVon + " - " + sInitialDatumBis);
                            Hauptmenu_Fragment_Buchungen_Filter.resetFilterData(initialDatumVon, Heute);

                            txtBuchungstitel.getEditText().setText("");
                            chipGroup.clearCheck();
                            cboKonto.getEditText().setText("");
                            cboKonto.setTag(null);
                            cboKonto.setSelection(-1);
                            cboKategorie.getEditText().setText("");
                            cboKategorie.setTag(null);
                            cboKooperation.setTag(null);
                            cboKooperation.setSelection(-1);
                            txtBetrag.getEditText().setText("");
                            GleichChip.setChecked(true);
                            //EnhältChip.setSelected(false);
                            //BeginntChip.setSelected(false);
                            GleichChipBetrag.setChecked(true);
                            //KleinerGleichChipBetrag.setSelected(false);
                            //GrößerGleichChipBetrag.setSelected(false);
                        }
                    });
                    result.getDrawerLayout().addDrawerListener(new DrawerLayout.DrawerListener() {
                        @Override
                        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                        }

                        @Override
                        public void onDrawerOpened(@NonNull View drawerView) {

                        }

                        @Override
                        public void onDrawerClosed(@NonNull View drawerView) {
                            Hauptmenu_Fragment_Buchungen_Filter.setFilterDataTitelfilter(txtBuchungstitel.getEditText().getText().toString());
                            Hauptmenu_Fragment_Buchungen_Filter.setFilterDataBetrag((txtBetrag.getEditText().getText().toString().equals("")) ? 0.0 : Double.parseDouble(txtBetrag.getEditText().getText().toString()));
                            Hauptmenu_Fragment_Buchungen fragment = (Hauptmenu_Fragment_Buchungen) getSupportFragmentManager().findFragmentByTag("Finanzbuchungen");
                            fragment.Buchungen_Laden();
                            Log.d("Test123!", Hauptmenu_Fragment_Buchungen_Filter.getFilterString());
                        }

                        @Override
                        public void onDrawerStateChanged(int newState) {

                        }
                    });
                    selectedFragment = Hauptmenu_Fragment_Buchungen.newInstance(toolbarFilterButton, result);
                    Tag = "Finanzbuchungen";
                    break;
                case R.id.mainmenu_bottomnav_auswertung:
                    selectedFragment = new Hauptmenu_Fragment_Auswertung();
                    Tag = "Auswertungen";
                    toolbarFilterButton.setVisibility(View.GONE);
                    toolbarMonth.setVisibility(View.GONE);
                    break;
            }
            String finalTag = Tag;
            if(!Finanzbuchungen.getFinanzbuchungenInitialized()) {
                Finanzbuchungen.initializeFinanzbuchungen(new FinanzbuchungenCallback() {
                    @Override
                    public void onFinanzbuchungenSuccessfullyLoaded(ArrayList<Finanzbuchung_Buchung> finanzbuchungen) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.replace(R.id.hauptmenu_framelayout, selectedFragment, finalTag);
                        ft.addToBackStack(finalTag);
                        ft.commit();
                    }
                });
            } else {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.hauptmenu_framelayout, selectedFragment, finalTag);
                ft.addToBackStack(finalTag);
                ft.commit();
            }

            toolbarTitle.setText(menuItem.getTitle());
            return true;
        }
    };
}
