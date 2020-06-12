package net.sytes.financemanagermm.financemanagermobile.Buchungen;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class Buchungskategorie implements Parcelable {
    private int id;
    private String Beschreibung;
    private int ÜKatID;
    private int Rot;
    private int Grün;
    private int Blau;
    private BuchTyp Buchtyp;

    public int getId() {
        return id;
    }

    public Buchungskategorie getObject() {
        return this;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBeschreibung() {
        return Beschreibung;
    }

    public int getÜKatId() {
        return ÜKatID;
    }

    public void setÜKatId(int ÜKatID) {
        this.ÜKatID = ÜKatID;
    }

    public void setBeschreibung(String Beschreibung) {
        this.Beschreibung = Beschreibung;
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

    public BuchTyp getBuchtyp() {
        return Buchtyp;
    }

    public void setBuchtyp(BuchTyp Buchtyp) {
        this.Buchtyp = Buchtyp;
    }

    public void updateColor(int color) {
        this.Rot = Color.red(color);
        this.Grün = Color.green(color);
        this.Blau = Color.blue(color);
    }

    public int getColor() {
        return Color.rgb(Rot, Grün, Blau);
    }

    public boolean isÜberkategorie() {
        return false;
    }

    public Buchungskategorie(int id, int ÜKatID, String Beschreibung, int Rot, int Grün, int Blau, BuchTyp Buchtyp) {
        this.id = id;
        this.Beschreibung = Beschreibung;
        this.Rot = Rot;
        this.Grün = Grün;
        this.Blau = Blau;
        this.ÜKatID = ÜKatID;
        this.Buchtyp = Buchtyp;
    }

    public Buchungskategorie(JSONObject BuchungskategorieDaten){
        int id = 0;
        int ÜKatId = -1;
        String Beschreibung = "";
        int Rot = 0;
        int Grün = 0;
        int Blau = 0;

        try {
            id = BuchungskategorieDaten.getInt("ID");
            ÜKatId = BuchungskategorieDaten.getInt("ÜKatID");
            Beschreibung = BuchungskategorieDaten.getString("Beschreibung");
            Rot = BuchungskategorieDaten.getInt("Rot");
            Grün = BuchungskategorieDaten.getInt("Grün");
            Blau = BuchungskategorieDaten.getInt("Blau");
            BuchTyp Buchtyp = BuchTyp.NEUTRAL;
            switch (BuchungskategorieDaten.getInt("Buchtyp")) {
                case 0:
                    Buchtyp = BuchTyp.AUSGABE;
                    break;
                case 1:
                    Buchtyp = BuchTyp.EINNAHME;
                    break;
                default:
                    Buchtyp = BuchTyp.NEUTRAL;
                    break;
            };

            this.id = id;
            this.Beschreibung = Beschreibung;
            this.Rot = Rot;
            this.Grün = Grün;
            this.Blau = Blau;
            this.ÜKatID = ÜKatId;
            this.Buchtyp = Buchtyp;
        } catch (JSONException e) {
            e.printStackTrace();
            this.id = id;
            this.Beschreibung = Beschreibung;
            this.Rot = Rot;
            this.Grün = Grün;
            this.Blau = Blau;
            this.ÜKatID = ÜKatId;
            this.Buchtyp = BuchTyp.NEUTRAL;
        }
    }
    public static final Parcelable.Creator<Buchungskategorie> CREATOR = new Parcelable.Creator<Buchungskategorie>() {
        @Override
        public Buchungskategorie createFromParcel(Parcel in) {
            Buchungskategorie Eintrag = null;
            try {
                Eintrag = new Buchungskategorie(in);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Eintrag;
        }

        @Override
        public Buchungskategorie[] newArray(int size) {
            return new Buchungskategorie[size];
        }
    };

    protected Buchungskategorie(Parcel in) throws ParseException {
        id = in.readInt();
        Beschreibung = in.readString();
        Rot = in.readInt();
        Grün = in.readInt();
        Blau = in.readInt();
        ÜKatID = in.readInt();
        Buchtyp = (BuchTyp) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(Beschreibung);
        dest.writeInt(Rot);
        dest.writeInt(Grün);
        dest.writeInt(Blau);
        dest.writeInt(ÜKatID);
        dest.writeSerializable(Buchtyp);
    }

    public enum BuchTyp {
        AUSGABE("Ausgabe", -1),
        EINNAHME("Einnahme", 1),
        NEUTRAL("Neutral", 0);

        private String name;
        private int faktorBetrag;

        BuchTyp(String Name, int FaktorBetrag) {
            this.name = Name;
            this.faktorBetrag = FaktorBetrag;
        }
    }
}

