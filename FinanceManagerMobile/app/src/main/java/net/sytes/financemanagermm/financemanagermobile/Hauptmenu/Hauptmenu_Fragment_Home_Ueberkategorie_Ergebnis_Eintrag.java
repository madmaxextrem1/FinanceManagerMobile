package net.sytes.financemanagermm.financemanagermobile.Hauptmenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Hauptmenu_Fragment_Home_Ueberkategorie_Ergebnis_Eintrag {
    private int id;
    private int PerID;
    private String Periode;
    private String Beschreibung;
    private double Ergebnis;
    private int Rot;
    private int Grün;
    private int Blau;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPerID() {
        return PerID;
    }

    public void setPerID(int perid) {
        this.PerID = perid;
    }

    public String getBeschreibung() {
        return Beschreibung;
    }

    public void setBeschreibung(String Beschreibung) {
        this.Beschreibung = Beschreibung;
    }

    public String getPeriode() {
        return Periode;
    }

    public void setPeriode(String periode) {
        this.Periode = periode;
    }

    public Double getErgebnis() {
        return Ergebnis;
    }

    public void setErgebnis(Double ergebnis) {
        this.Ergebnis = ergebnis;
    }

    public int getRot() {
        return Rot;
    }

    public void setRot(int Rot) {
        this.Rot = Rot;
    }

    public int getGrün() {
        return Grün;
    }

    public void setGrün(int Grün) {
        this.Grün = Grün;
    }

    public int getBlau() {
        return Blau;
    }

    public void setBlau(int Blau) {
        this.Blau = Blau;
    }

    public Hauptmenu_Fragment_Home_Ueberkategorie_Ergebnis_Eintrag(int id, int PerID, String Periode, String Beschreibung, int Rot, int Grün, int Blau, Double Ergebnis) {
        this.id = id;
        this.Beschreibung = Beschreibung;
        this.Rot = Rot;
        this.Grün = Grün;
        this.Blau = Blau;
        this.PerID = PerID;
        this.Periode = Periode;
        this.Ergebnis = Ergebnis;
    }

    public Hauptmenu_Fragment_Home_Ueberkategorie_Ergebnis_Eintrag(JSONObject jsonObject) {
        try {
            //getting json object from the json array
            JSONObject obj = jsonObject;

            //getting the name from the json object and putting it inside string array
            int id = obj.getInt("ID");
            int perid = obj.getInt("PerID");
            String periode = obj.getString("Periode");
            String Beschreibung = obj.getString("Überkategorie");
            int Rot = obj.getInt("Rot");
            int Grün = obj.getInt("Grün");
            int Blau = obj.getInt("Blau");
            double Ergebnis = obj.getDouble("Ergebnis");

            this.id = id;
            this.Beschreibung = Beschreibung;
            this.Rot = Rot;
            this.Grün = Grün;
            this.Blau = Blau;
            this.PerID = perid;
            this.Periode = periode;
            this.Ergebnis = Ergebnis;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

