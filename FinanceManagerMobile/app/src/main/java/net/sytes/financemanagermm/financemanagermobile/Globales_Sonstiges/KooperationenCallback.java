package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface KooperationenCallback {
    void onKooperationenSuccessfullyLoaded(LinkedHashMap<Integer, Kooperation> Kooperationen);
}
