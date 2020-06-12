package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import java.util.ArrayList;

public interface TokensCallback {
    void onTokensSuccessfullyLoaded(ArrayList<FinanzbuchungToken> Tokens);
}
