package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.FinanzbuchungToken;

import java.util.ArrayList;

public interface TokensCallback {
    void onTokensSuccessfullyLoaded(ArrayList<FinanzbuchungToken> Tokens);
}
