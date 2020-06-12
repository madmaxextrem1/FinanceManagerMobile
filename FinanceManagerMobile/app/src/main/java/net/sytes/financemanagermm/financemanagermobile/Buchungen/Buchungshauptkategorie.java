package net.sytes.financemanagermm.financemanagermobile.Buchungen;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Buchungshauptkategorie extends Buchungskategorie {
    private LinkedHashMap<Integer, Buchungskategorie> mapUnterkategorien;

    public boolean isÜberkategorie() {
        return true;
    }
    public Buchungshauptkategorie(int id, int ÜKatID, String Beschreibung, int Rot, int Grün, int Blau, LinkedHashMap<Integer, Buchungskategorie> Unterkategorien) {
        super(id, ÜKatID, Beschreibung, Rot, Grün, Blau, BuchTyp.NEUTRAL);
        this.mapUnterkategorien = Unterkategorien;
    }

    public Buchungshauptkategorie(JSONObject BuchungskategorieDaten, LinkedHashMap<Integer, Buchungskategorie> Unterkategorien) {
        super(BuchungskategorieDaten);
        this.mapUnterkategorien = Unterkategorien;
    }

    public LinkedHashMap<Integer, Buchungskategorie> getUnterkategorien() {return mapUnterkategorien;}
    public void clearUnterkategorien () {
        mapUnterkategorien.clear();
    }
}

