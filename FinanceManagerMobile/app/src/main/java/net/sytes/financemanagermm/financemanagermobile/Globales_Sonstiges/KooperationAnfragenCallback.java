package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.KooperationAnfrage;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface KooperationAnfragenCallback {
    void onKooperationAnfragenSuccessfullyLoaded(LinkedHashMap<Integer, KooperationAnfrage> anfragenMap);
}
