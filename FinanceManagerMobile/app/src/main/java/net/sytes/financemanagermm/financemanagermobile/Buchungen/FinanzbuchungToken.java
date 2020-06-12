package net.sytes.financemanagermm.financemanagermobile.Buchungen;

import android.os.Parcel;
import android.os.Parcelable;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class FinanzbuchungToken implements Parcelable {
    private int id;
    private String Beschreibung;
    private TokenTyp Typ;
    private int kooperationId;
    private int benutzerId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBenutzerId() {
        return benutzerId;
    }

    public void setBenutzerId(int BenutzerID) {
        this.benutzerId = BenutzerID;
    }

    public TokenTyp getTyp() {
        return Typ;
    }

    public void setTyp(TokenTyp Typ) {
        this.Typ = Typ;
    }

    public String getBeschreibung() {
        return Beschreibung;
    }

    public void setBeschreibung(String Merkmal) {
        this.Beschreibung = Merkmal;
    }

    public FinanzbuchungToken(int id, String Beschreibung, TokenTyp Typ, int KooperationID, int BenutzerID) {
        this.id = id;
        this.Beschreibung = Beschreibung;
        this.Typ = Typ;
        this.benutzerId = BenutzerID;
        this.kooperationId = KooperationID;
    }

    public FinanzbuchungToken(JSONObject Tokendaten) {
        try {
            int ID = Tokendaten.getInt("ID");
            String Merkmal = Tokendaten.getString("Token");

            this.id = ID;
            this.Beschreibung = Merkmal;
            this.Typ = TokenTyp.PERSOENLICH;
            this.benutzerId = GlobaleVariablen.getInstance().getUserId();
            this.kooperationId = 0;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static final Parcelable.Creator<FinanzbuchungToken> CREATOR = new Parcelable.Creator<FinanzbuchungToken>() {
        @Override
        public FinanzbuchungToken createFromParcel(Parcel in) {
            FinanzbuchungToken Eintrag = null;
            try {
                Eintrag = new FinanzbuchungToken(in);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Eintrag;
        }

        @Override
        public FinanzbuchungToken[] newArray(int size) {
            return new FinanzbuchungToken[size];
        }
    };

    protected FinanzbuchungToken(Parcel in) throws ParseException {
        id = in.readInt();
        Beschreibung = in.readString();
        Typ = (TokenTyp) in.readSerializable();
        benutzerId = in.readInt();
        kooperationId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(Beschreibung);
        dest.writeSerializable(Typ);
        dest.writeInt(benutzerId);
        dest.writeInt(kooperationId);
    }

    public enum TokenTyp {
        PERSOENLICH("Persönlich"),
        GRUPPE("Gruppe");

        private String stringValue;

        TokenTyp(String Name) {
            stringValue = Name;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }
}
