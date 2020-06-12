package net.sytes.financemanagermm.financemanagermobile.Buchungen;

import android.os.Parcel;
import android.os.Parcelable;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.FinanzbuchungTokens;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.FinanceManagerData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Finanzbuchung implements FinanceManagerData {
    private int id;
    private int benutzerId;
    private int kooperationId;
    private String beschreibung;
    private LocalDate datum;
    private Double betrag;

    public Finanzbuchung(int buchungId) {
        this.id = buchungId;
    }

    public Finanzbuchung(int id, int benutzerId, int kooperationId, String beschreibung, double betrag, LocalDate datum) {
        this.id = id;
        this.benutzerId = benutzerId;
        this.kooperationId = kooperationId;
        this.beschreibung = beschreibung;
        this.betrag = betrag;
        this.datum = datum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBenutzerId() {
        return benutzerId;
    }

    public void setBenutzerId(int benutzerId) {
        this.benutzerId = benutzerId;
    }

    public int getKooperationId() {
        return kooperationId;
    }

    public void setKooperationId(int kooperationId) {
        this.kooperationId = kooperationId;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public Double getBetrag() {
        return betrag;
    }

    public void setBetrag(Double betrag) {
        this.betrag = betrag;
    }

    @Override
    public int getIdentifier() {
        return id;
    }
}

