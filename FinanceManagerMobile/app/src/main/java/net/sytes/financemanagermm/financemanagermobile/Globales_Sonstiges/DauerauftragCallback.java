package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Dauerauftrag;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface DauerauftragCallback {
    void onDaueraufträgeSuccessfullyLoaded(LinkedHashMap<Integer, Dauerauftrag> daueraufträge);
}
