package net.sytes.financemanagermm.financemanagermobile.Datenmanagement;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;

public class Finanzbuchung_Umbuchung extends Finanzbuchung {
    private int kontoVonId;
    private int kontoAufId;

    public Finanzbuchung_Umbuchung(int buchungId) {
        super(buchungId);
    }

    public Finanzbuchung_Umbuchung(int id, int benutzerId, int kooperationId, String beschreibung, double betrag, LocalDate datum,
                                   int kontoVonId, int kontoAufId) {
        super(id, benutzerId, kooperationId, beschreibung, betrag, datum);
        this.kontoVonId = kontoVonId;
        this.kontoAufId = kontoAufId;
    }

    public Finanzbuchung_Umbuchung(JSONObject umbuchungDaten) throws JSONException, ParseException {
        super(umbuchungDaten.getInt("ID"),
                umbuchungDaten.getInt("BenutzerID"),
                umbuchungDaten.getInt("KooperationID"),
                umbuchungDaten.getString("Beschreibung"),
                umbuchungDaten.getDouble("Betrag"),
                GlobaleVariablen.getInstance().getSQL_DateFormat().parse(umbuchungDaten.getString("Datum")).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                );

            this.kontoVonId = umbuchungDaten.getInt("KontoVonID");
            this.kontoAufId = umbuchungDaten.getInt("KontoAufID");
    }

    public int getKontoVonId() {
        return kontoVonId;
    }

    public void setKontoVonId(int kontoVonId) {
        this.kontoVonId = kontoVonId;
    }

    public int getKontoAufId() {
        return kontoAufId;
    }

    public void setKontoAufId(int kontoAufId) {
        this.kontoAufId = kontoAufId;
    }
}

