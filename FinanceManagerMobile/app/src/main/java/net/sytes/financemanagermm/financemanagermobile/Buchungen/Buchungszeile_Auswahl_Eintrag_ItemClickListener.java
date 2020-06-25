package net.sytes.financemanagermm.financemanagermobile.Buchungen;

import android.view.View;

import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.FinanzbuchungPosition;

public interface Buchungszeile_Auswahl_Eintrag_ItemClickListener {
    void onBuchungszeileItemClicked(int pos, FinanzbuchungPosition BuchungszeileEintrag, View shareCardView);
}
