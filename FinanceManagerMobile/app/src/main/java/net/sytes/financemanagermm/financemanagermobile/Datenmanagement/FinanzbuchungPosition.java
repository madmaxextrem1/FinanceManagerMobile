package net.sytes.financemanagermm.financemanagermobile.Datenmanagement;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class FinanzbuchungPosition implements Parcelable {
    private int id;
    private Double Betrag;
    private int KategorieID;
    private String Unterkategorie;
    private String Überkategorie;
    private int Rot;
    private int Grün;
    private int Blau;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKategorieId() {
        return KategorieID;
    }

    public void setKategorieID(int KategorieID) {
        this.KategorieID = KategorieID;
    }

    public String getUnterkategorie() {
        return Unterkategorie;
    }

    public void setUnterkategorie(String Unterkategorie) {
        this.Unterkategorie = Unterkategorie;
    }

    public void setÜberkategorie(String Überkategorie) {
        this.Überkategorie = Überkategorie;
    }

    public String getÜberkategorie() {
        return Überkategorie;
    }

    public Double getBetrag() {
        return Betrag;
    }

    public void setBetrag(Double Betrag) {
        this.Betrag = Betrag;
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

    public FinanzbuchungPosition(int Pos, Integer KategorieID, String UnterkategorieName, String ÜberkategorieName, Double Betrag, int Rot, int Grün, int Blau) {
        this.id = Pos;
        this.Betrag = Betrag;
        this.KategorieID = KategorieID;
        this.Unterkategorie = UnterkategorieName;
        this.Überkategorie = ÜberkategorieName;
        this.Rot = Rot;
        this.Grün = Grün;
        this.Blau = Blau;
    }

    public FinanzbuchungPosition(JSONObject Positionsdaten) {
        try {
            int Pos = Positionsdaten.getInt("Pos");
            Double Betrag = Positionsdaten.getDouble("Betrag");
            int KategorieID = Positionsdaten.getInt("KategorieID");
            String ÜKatName = Positionsdaten.getString("ÜKatName");
            String UKatName = Positionsdaten.getString("UKatName");
            int Rot = Positionsdaten.getInt("Rot");
            int Grün = Positionsdaten.getInt("Grün");
            int Blau = Positionsdaten.getInt("Blau");

            this.id = Pos;
            this.Betrag = Betrag;
            this.KategorieID = KategorieID;
            this.Unterkategorie = UKatName;
            this.Überkategorie = ÜKatName;
            this.Rot = Rot;
            this.Grün = Grün;
            this.Blau = Blau;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static final Parcelable.Creator<FinanzbuchungPosition> CREATOR = new Parcelable.Creator<FinanzbuchungPosition>() {
        @Override
        public FinanzbuchungPosition createFromParcel(Parcel in) {
            FinanzbuchungPosition Eintrag = null;
            try {
                Eintrag = new FinanzbuchungPosition(in);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Eintrag;
        }

        @Override
        public FinanzbuchungPosition[] newArray(int size) {
            return new FinanzbuchungPosition[size];
        }
    };

    protected FinanzbuchungPosition(Parcel in) throws ParseException {
        id = in.readInt();
        Betrag = in.readDouble();
        KategorieID = in.readInt();
        Unterkategorie = in.readString();
        Überkategorie = in.readString();
        Rot = in.readInt();
        Grün = in.readInt();
        Blau = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(Betrag);
        dest.writeInt(KategorieID);
        dest.writeString(Unterkategorie);
        dest.writeString(Überkategorie);
        dest.writeInt(Rot);
        dest.writeInt(Grün);
        dest.writeInt(Blau);
    }
}

