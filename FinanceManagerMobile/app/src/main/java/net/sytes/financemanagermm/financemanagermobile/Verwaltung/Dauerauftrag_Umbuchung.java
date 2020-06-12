package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.os.Parcel;
import android.os.Parcelable;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Konten;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

public class Dauerauftrag_Umbuchung extends Dauerauftrag implements Parcelable {
    private Konto kontoAuf;
    private Konto kontoVon;

    public Dauerauftrag_Umbuchung(int id, String Beschreibung, Konto kontoAuf, Konto kontoVon, Double Betrag, int TokenID, int kooperationId, String Rhythmus, Date AusgeführtAm, Date NächsteAusführung, boolean Aktiv) {
        super(id, Beschreibung, Betrag, TokenID, kooperationId, Rhythmus, AusgeführtAm, NächsteAusführung, Aktiv,  Dauerauftrag_Art.UMBUCHUNG);
        this.kontoAuf = kontoAuf;
        this.kontoVon = kontoVon;
    }

    public Dauerauftrag_Umbuchung(JSONObject dauerauftragDaten) throws JSONException, ParseException {
        super(dauerauftragDaten.getInt("ID"),
                dauerauftragDaten.getString("Beschreibung"),
                dauerauftragDaten.getDouble("Betrag"),
                dauerauftragDaten.getInt("TokenID"),
                dauerauftragDaten.getInt("KooperationID"),
                dauerauftragDaten.getString("Rhythmus"),
                GlobaleVariablen.getInstance().getSQL_DateFormat().parse(dauerauftragDaten.getString("AusgeführtAm")),
                GlobaleVariablen.getInstance().getSQL_DateFormat().parse(dauerauftragDaten.getString("NächsteAusführung")),
                dauerauftragDaten.getString("Aktiv").equals("1"),
                Dauerauftrag_Art.UMBUCHUNG);

            this.kontoAuf = Konten.getKontoById(dauerauftragDaten.getInt("KontoAufID"));
            this.kontoVon = Konten.getKontoById(dauerauftragDaten.getInt("KontoVonID"));
    }

    public Dauerauftrag_Umbuchung() {
        this.kontoAuf = null;
        this.kontoVon = null;
    }

    public net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto getKontoAuf() {
        return kontoAuf;
    }

    public net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto getKontoVon() {
        return kontoVon;
    }

    public void setKontoAuf(net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto kontoAuf) {
        this.kontoAuf = kontoAuf;
    }

    public void setKontoVon(net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto kontoVon) {
        this.kontoVon = kontoVon;
    }
}

