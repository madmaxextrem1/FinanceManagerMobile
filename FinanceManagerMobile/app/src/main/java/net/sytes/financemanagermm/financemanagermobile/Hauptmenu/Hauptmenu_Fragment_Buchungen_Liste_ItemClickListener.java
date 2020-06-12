package net.sytes.financemanagermm.financemanagermobile.Hauptmenu;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Finanzbuchung_Buchung;

public interface Hauptmenu_Fragment_Buchungen_Liste_ItemClickListener {
    void onBuchungtemClicked(int pos, Finanzbuchung_Buchung BuchungEintrag, boolean EditMode);
}
