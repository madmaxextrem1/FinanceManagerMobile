package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.Finanzbuchung_Buchung;

import java.util.ArrayList;

public interface FinanzbuchungenCallback {
    void onFinanzbuchungenSuccessfullyLoaded(ArrayList<Finanzbuchung_Buchung> finanzbuchungen);
}
