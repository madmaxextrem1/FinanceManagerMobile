package net.sytes.financemanagermm.financemanagermobile.Steuerelemente;

import android.content.Context;
import android.widget.BaseAdapter;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Konten;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import java.util.HashMap;

public abstract class Konto_BaseAdapter extends BaseAdapter {
    private HashMap<Integer, Konto> kontoMap;
    private Context context;

    public Konto_BaseAdapter (Context context) {
        this.context = context;
        this.kontoMap = Konten.getKonten();
    }
    public Konto_BaseAdapter (Context context, HashMap<Integer, Konto> kontoMap) {
        this.context = context;
        this.kontoMap = kontoMap;
    }

    public HashMap<Integer, Konto> getKontoMap() {
        return kontoMap;
    }

    @Override
    public int getCount() {
        return kontoMap.size();
    }

    @Override
    public Konto getItem(int position) {
        int i = 0;
        for(Integer key:kontoMap.keySet()) {
            if(i == position) return kontoMap.get(key);
            i++;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getIdentifier();
    }

    public Context getContext() {
        return context;
    }
 }
