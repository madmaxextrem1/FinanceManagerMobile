package net.sytes.financemanagermm.financemanagermobile.Steuerelemente;

import android.content.Context;
import android.widget.BaseAdapter;

import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;

public abstract class LinkedHashMap_BaseRecyclerAdapter<K, V extends FinanceManagerData> extends RecyclerView.Adapter {
    private LinkedHashMap<K, V> linkedMap;
    private Context context;

    public LinkedHashMap_BaseRecyclerAdapter() {super();}

    public LinkedHashMap_BaseRecyclerAdapter(Context context) {
        this.context = context;
        this.linkedMap = new LinkedHashMap<K, V>();
    }
    public LinkedHashMap_BaseRecyclerAdapter(Context context, LinkedHashMap<K, V> linkedMap) {
        this.context = context;
        this.linkedMap = linkedMap;
    }

    public LinkedHashMap<K, V> getLinkedMap() {
        return linkedMap;
    }

    @Override
    public int getItemCount() {
        return linkedMap.size();
    }

    public V getItem(int position) {
        int i = 0;
        for(K key:linkedMap.keySet()) {
            if(i == position) return linkedMap.get(key);
            i++;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getIdentifier();
    }

    public int getItemPosition(V item) {
        int i = - 1;
        for(V value:linkedMap.values()) {
            i++;
            if(value == item) return i;
        }
        return i;
    }

    public int getItemPositionById(K itemId) {
        int i = - 1;
        for(K key:linkedMap.keySet()) {
            i++;
            if(key.equals(itemId)) return i;
        }
        return i;
    }

    public Context getContext() {
        return context;
    }
 }
