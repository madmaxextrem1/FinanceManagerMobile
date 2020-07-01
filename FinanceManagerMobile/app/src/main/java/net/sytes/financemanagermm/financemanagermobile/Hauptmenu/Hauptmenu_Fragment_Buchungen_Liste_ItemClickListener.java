package net.sytes.financemanagermm.financemanagermobile.Hauptmenu;

import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.Finanzbuchung;

public interface Hauptmenu_Fragment_Buchungen_Liste_ItemClickListener {
    void onBuchungtemClicked(int pos, Finanzbuchung BuchungEintrag, boolean EditMode);
}
