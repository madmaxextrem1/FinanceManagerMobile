package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungshauptkategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie_Update_Interface;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorien;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.Hauptmenu.Hauptmenu_Fragment_Buchungen_Filter;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.Kategorie_Expandable_BaseAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class Verwaltung_Kategorien_Übersicht_KategorieAdapter extends Kategorie_Expandable_BaseAdapter {

    public Verwaltung_Kategorien_Übersicht_KategorieAdapter(Context context) {
        super(context);
    }

    public Verwaltung_Kategorien_Übersicht_KategorieAdapter(Context context, LinkedHashMap<Integer, Buchungshauptkategorie> kategorien) {
        super(context, kategorien);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.verwaltung_kategorien_uebersicht_kategorieadapter_groupitem, parent, false);
        }

        TextView txtBeschreibung = convertView.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_lv_item_Titel);
        txtBeschreibung.setText(getGroup(groupPosition).getBeschreibung());
        TextView imgCircle = convertView.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_lv_item_Circle);
        int Rot = getGroup(groupPosition).getRot();
        int Grün = getGroup(groupPosition).getGrün();
        int Blau = getGroup(groupPosition).getBlau();
        float alpha = new Globale_Funktionen().Helligkeit_Berechnen(Rot, Grün, Blau);
        Color backgroundTint = Color.valueOf(Color.rgb(Rot, Grün, Blau));
        int foregroundTint;
        if ((int) alpha >= 255 / 2) {
            foregroundTint = Color.BLACK;
        } else {
            foregroundTint = Color.WHITE;
        }
        imgCircle.setTextColor(foregroundTint);
        imgCircle.setBackgroundTintList(ColorStateList.valueOf(backgroundTint.toArgb()));
        imgCircle.setText(getGroup(groupPosition).getBeschreibung().substring(0, 1));
        imgCircle.setTag(R.id.Buchung_Hinzufügen_Kategorie_Dialog_lv_item_Circle, groupPosition);
        ExpandableListView expandableListView = (ExpandableListView) parent;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableListView.isGroupExpanded(groupPosition)) {
                    expandableListView.collapseGroup(groupPosition);
                } else {
                    expandableListView.expandGroup(groupPosition);
                }
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showEditDialog(getGroup(groupPosition), true);
                return true;
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.verwaltung_kategorien_uebersicht_kateogrieadapter_subitem, parent, false);
        }

        TextView txtBeschreibung = (TextView) convertView.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_lv_subitem_Titel);
        txtBeschreibung.setText(getChild(groupPosition, childPosition).getBeschreibung());
        TextView imgCircle = (TextView) convertView.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_lv_subitem_Circle);
        int Rot = getChild(groupPosition, childPosition).getRot();
        int Grün = getChild(groupPosition, childPosition).getGrün();
        int Blau = getChild(groupPosition, childPosition).getBlau();
        float alpha = new Globale_Funktionen().Helligkeit_Berechnen(Rot, Grün, Blau);
        Color backgroundTint = Color.valueOf(Color.rgb(Rot, Grün, Blau));
        int foregroundTint;
        if ((int) alpha >= 255 / 2) {
            foregroundTint = Color.BLACK;
        } else {
            foregroundTint = Color.WHITE;
        }
        imgCircle.setTextColor(foregroundTint);

        imgCircle.setBackgroundTintList(ColorStateList.valueOf(backgroundTint.toArgb()));
        imgCircle.setText(getChild(groupPosition, childPosition).getBeschreibung().substring(0, 1));

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showEditDialog(getChild(groupPosition, childPosition), true);
                return true;
            }
        });

        return convertView;
    }

    private void showEditDialog(Buchungskategorie kategorie, boolean update) {
        Verwaltung_Kategorien_Übersicht_Bearbeiten_Dialog dialog = new Verwaltung_Kategorien_Übersicht_Bearbeiten_Dialog(kategorie, update, new Buchungskategorie_Update_Interface() {
            @Override
            public void onBuchungskategorieChanged(Buchungskategorie Kategorie) {
                Buchungskategorien.rebuildKategorieHierarchy(new Buchungskategorie_Update_Interface() {
                    @Override
                    public void onBuchungskategorieChanged(Buchungskategorie Kategorie) {
                        notifyDataSetChanged();
                    }
                });
            }
        });
        FragmentManager fragmentManager = ((Verwaltung_Kategorien_Übersicht) getContext()).getSupportFragmentManager();
        dialog.show(fragmentManager, "dialog_kategorie_auswahl");
    }
}
