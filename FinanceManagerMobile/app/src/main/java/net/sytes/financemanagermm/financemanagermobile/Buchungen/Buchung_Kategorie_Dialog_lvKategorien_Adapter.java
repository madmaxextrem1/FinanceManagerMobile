package net.sytes.financemanagermm.financemanagermobile.Buchungen;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.Kategorie_Expandable_BaseAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import androidx.core.content.ContextCompat;

public class Buchung_Kategorie_Dialog_lvKategorien_Adapter extends Kategorie_Expandable_BaseAdapter {
    public Buchung_Kategorie_Dialog_lvKategorien_Adapter(Context context) {
        super(context);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.buchung_hinzufuegen_kategorie_dialog_lv_item, parent, false);
             }
        TextView txtBeschreibung = (TextView) convertView.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_lv_item_Titel);
        txtBeschreibung.setText(getGroup(groupPosition).getBeschreibung());
        TextView imgCircle = (TextView) convertView.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_lv_item_Circle);
        int Rot = getGroup(groupPosition).getRot();
        int Grün = getGroup(groupPosition).getGrün();
        int Blau = getGroup(groupPosition).getBlau();
        float alpha = new Globale_Funktionen().Helligkeit_Berechnen(Rot,Grün,Blau);
        Color backgroundTint = Color.valueOf(Color.rgb(Rot,Grün,Blau));
        int foregroundTint;
           if(Integer.valueOf((int) alpha) >= Integer.valueOf(255/2)) {
               foregroundTint = Color.BLACK;
           } else {
               foregroundTint = Color.WHITE;
           }
        imgCircle.setTextColor(foregroundTint);
        imgCircle.setBackgroundTintList(ColorStateList.valueOf(backgroundTint.toArgb()));
        imgCircle.setText(getGroup(groupPosition).getBeschreibung().substring(0,1));
        imgCircle.setTag(R.id.Buchung_Hinzufügen_Kategorie_Dialog_lv_item_Circle, groupPosition);
        ExpandableListView expandableListView = (ExpandableListView) parent;
            convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandableListView.isGroupExpanded(groupPosition)){
                    expandableListView.collapseGroup(groupPosition);
                }else{
                    expandableListView.expandGroup(groupPosition);
                }
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.buchung_hinzufuegen_kategorie_dialog_lv_subitem, parent, false);
        }
        TextView txtBeschreibung = (TextView) convertView.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_lv_subitem_Titel);
        txtBeschreibung.setText(getChild(groupPosition, childPosition).getBeschreibung());
        TextView imgCircle = (TextView) convertView.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_lv_subitem_Circle);
        int Rot = getChild(groupPosition, childPosition).getRot();
        int Grün = getChild(groupPosition, childPosition).getGrün();
        int Blau = getChild(groupPosition, childPosition).getBlau();
        float alpha = new Globale_Funktionen().Helligkeit_Berechnen(Rot,Grün,Blau);
        Color backgroundTint = Color.valueOf(Color.rgb(Rot,Grün,Blau));
        int foregroundTint;
        if(Integer.valueOf((int) alpha) >= Integer.valueOf(255/2)) {
            foregroundTint = Color.BLACK;
        } else {
            foregroundTint = Color.WHITE;
        }
        imgCircle.setTextColor(foregroundTint);
        ExpandableListView expandableListView = (ExpandableListView) parent;
        int index = expandableListView.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_lv_subitem_Layout);

        if (expandableListView.getCheckedItemPosition() == index) {
            Drawable drawable = ContextCompat.getDrawable(parent.getContext(),R.color.NeueBuchung_background);
            txtBeschreibung.setTextColor(Color.WHITE);
            layout.setBackground(drawable);
        } else {
            Drawable drawable = ContextCompat.getDrawable(parent.getContext(),R.color.white);
            int forecolor = ContextCompat.getColor(parent.getContext(),R.color.fm_Login_Background_Color);
            txtBeschreibung.setTextColor(forecolor);
            layout.setBackground(drawable);
        }
        imgCircle.setBackgroundTintList(ColorStateList.valueOf(backgroundTint.toArgb()));
        imgCircle.setText(getChild(groupPosition, childPosition).getBeschreibung().substring(0,1).toString());

        View kategoriedialog = (View) parent.getParent();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableListView.setItemChecked(index, true);
                setSelectedGroupIndex(groupPosition);
                setSelectedPosition(childPosition);
                if(getChild(groupPosition, childPosition).getBuchtyp() == Buchungskategorie.BuchTyp.EINNAHME) {
                    RadioButton rdbEinnahme = (RadioButton) kategoriedialog.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_rdbEinnahme);
                    rdbEinnahme.setChecked(true);
                } else {
                    RadioButton rdbAusgabe = (RadioButton) kategoriedialog.findViewById(R.id.Buchung_Hinzufügen_Kategorie_Dialog_rdbAusgabe);
                    rdbAusgabe.setChecked(true);
                }
            }
        });

        return convertView;
    }
}
