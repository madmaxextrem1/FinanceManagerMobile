package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.FinanceManagerData;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public interface FinanceManagerCallback<K extends Number, V extends FinanceManagerData> {
    void onDataUpdated(LinkedHashMap<K, V> linkedHashMap);
}
