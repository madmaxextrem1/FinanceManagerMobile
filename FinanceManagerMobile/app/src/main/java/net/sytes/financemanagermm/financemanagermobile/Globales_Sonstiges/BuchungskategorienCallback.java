package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungshauptkategorie;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface BuchungskategorienCallback {
    void onBuchungskategorienSuccessfullyLoaded(LinkedHashMap<Integer, Buchungshauptkategorie> Buchungskategorien);
}
