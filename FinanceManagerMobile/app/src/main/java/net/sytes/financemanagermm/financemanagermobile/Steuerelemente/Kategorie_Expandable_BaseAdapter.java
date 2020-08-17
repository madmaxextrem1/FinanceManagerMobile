package net.sytes.financemanagermm.financemanagermobile.Steuerelemente;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungshauptkategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie_Update_Interface;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorien;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.FinanceManagerApplication;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Globale_Funktionen;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Sign_In_Up.FinanceManagerMobileApplication;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Verwaltung_Kategorien_Übersicht;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Verwaltung_Kategorien_Übersicht_Bearbeiten_Dialog;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public abstract class Kategorie_Expandable_BaseAdapter extends BaseExpandableListAdapter {
    private LinkedHashMap<Integer, Buchungshauptkategorie> hauptkategorieMap;
    private Context context;
    private int SelectedGroupIndex = 0;
    private int SelectedPosition = 0;

    public Kategorie_Expandable_BaseAdapter(Context context) {
        super();
        this.hauptkategorieMap = FinanceManagerMobileApplication.getInstance().getDataManagement().getCategories();
        this.context = context;
    }

    public Kategorie_Expandable_BaseAdapter(Context context, LinkedHashMap<Integer, Buchungshauptkategorie> kategorien) {
        super();
        this.hauptkategorieMap = kategorien;
        this.context = context;
    }

    public LinkedHashMap<Integer, Buchungshauptkategorie> getHauptkategorieMap() {
        return hauptkategorieMap;
    }

    @Override
    public int getGroupCount() {
        return hauptkategorieMap.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
           return getGroup(groupPosition).getUnterkategorien().size();
    }

    @Override
    public Buchungshauptkategorie getGroup(int groupPosition) {
        int i = 0;
        for(Integer key:hauptkategorieMap.keySet()) {
            if(i == groupPosition) return hauptkategorieMap.get(key);
            i++;
        }
        return null;
    }

    @Override
    public Buchungskategorie getChild(int groupPosition, int childPosition) {
        int i = 0;
        Buchungshauptkategorie hauptkategorie = getGroup(groupPosition);
        for(Integer key:hauptkategorie.getUnterkategorien().keySet()) {
            if(i == childPosition) return hauptkategorie.getUnterkategorien().get(key);
            i++;
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return getGroup(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getChild(groupPosition, childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public int getSelectedGroupIndex() {
        return SelectedGroupIndex;
    }

    public void setSelectedGroupIndex(int GroupIndex) {
        this.SelectedGroupIndex = GroupIndex;
    }

    public int getSelectedPosition() {
        return SelectedPosition;
    }

    public void setSelectedPosition(int Position) {
        this.SelectedPosition = Position;
    }

    public Context getContext() {
        return context;
    }
}
