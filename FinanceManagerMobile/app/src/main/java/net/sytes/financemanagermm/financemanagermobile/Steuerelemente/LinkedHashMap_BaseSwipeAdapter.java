package net.sytes.financemanagermm.financemanagermobile.Steuerelemente;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.LinkedHashMap;

public abstract class LinkedHashMap_BaseSwipeAdapter<K, V extends FinanceManagerData> extends BaseSwipeAdapter {
    private LinkedHashMap<K, V> linkedMap;
    private Context context;
    private final int layoutId;
    private final int swipeLayoutId;

    public LinkedHashMap_BaseSwipeAdapter(Context context, int LayoutId, int swipeLayoutId) {
        this.context = context;
        this.layoutId = LayoutId;
        this.swipeLayoutId = swipeLayoutId;
        this.linkedMap = new LinkedHashMap<K, V>();
    }
    public LinkedHashMap_BaseSwipeAdapter(Context context, int LayoutId,int swipeLayoutId, LinkedHashMap<K, V> linkedMap) {
        this.context = context;
        this.layoutId = LayoutId;
        this.swipeLayoutId = swipeLayoutId;
        this.linkedMap = linkedMap;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    public LinkedHashMap<K, V> getLinkedMap() {
        return linkedMap;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return swipeLayoutId;
    }

    @Override
    public int getCount() {
        return linkedMap.size();
    }

    @Override
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

    public int getLayoutId() {
        return layoutId;
    }
}
