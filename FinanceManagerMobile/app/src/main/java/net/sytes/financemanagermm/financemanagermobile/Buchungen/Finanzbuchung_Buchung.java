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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class Finanzbuchung_Buchung extends Finanzbuchung implements Parcelable {
    private int kontoId;
    private ArrayList<FinanzbuchungPosition> buchungspositionen;
    private ArrayList<FinanzbuchungToken> tokens;

    public Finanzbuchung_Buchung(int buchungID) {super(buchungID);}


    public Finanzbuchung_Buchung (int id, int benutzerId, int kooperationId, String beschreibung, double betrag,
                         LocalDate datum, int kontoId, ArrayList<FinanzbuchungPosition> Buchungszeilen, Integer[] TokenIDs) {
        super(id, benutzerId, kooperationId, beschreibung, betrag, datum);
        ArrayList<FinanzbuchungToken> listTokens = new ArrayList<FinanzbuchungToken>();
        for (Integer tokenID : TokenIDs) {
            listTokens.add(FinanzbuchungTokens.getTokenById(tokenID));
        }
        this.tokens = listTokens;
        this.kontoId = kontoId;
        this.buchungspositionen = Buchungszeilen;
    }

    public Finanzbuchung_Buchung(JSONObject FinanzbuchungDaten) throws JSONException, ParseException {
            super( FinanzbuchungDaten.getInt("ID"),
                        FinanzbuchungDaten.getInt("BenutzerID"),
                        FinanzbuchungDaten.getInt("KooperationID"),
                        FinanzbuchungDaten.getString("Beschreibung"),
                        FinanzbuchungDaten.getDouble("Betrag"),
                        GlobaleVariablen.getInstance().getSQL_DateFormat().parse(FinanzbuchungDaten.getString("Datum")).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                );

            //getting the name from the json object and putting it inside string array
        try {
            int kontoId = FinanzbuchungDaten.getInt("KontoID");

            ArrayList<FinanzbuchungPosition> listBuchungspositionen = new ArrayList<FinanzbuchungPosition>();
            JSONArray jsonArrayBuchungszeilen = FinanzbuchungDaten.getJSONArray("Buchungszeilen");
            for (int i = 0; i < jsonArrayBuchungszeilen.length(); i++) {
                listBuchungspositionen.add(new FinanzbuchungPosition(jsonArrayBuchungszeilen.getJSONObject(i)));
            }

            ArrayList<FinanzbuchungToken> listTokens = new ArrayList<FinanzbuchungToken>();
            JSONArray jsonArrayTokenIDs = FinanzbuchungDaten.getJSONArray("Tokens");
            for (int t = 0; t < jsonArrayTokenIDs.length(); t++) {
                //getting json object from the json array
                JSONObject tokenIDsJSONObject = jsonArrayTokenIDs.getJSONObject(t);

                listTokens.add(FinanzbuchungTokens.getTokenById(tokenIDsJSONObject.getInt("TokenID")));
            }

            this.kontoId = kontoId;
            this.buchungspositionen = listBuchungspositionen;
            this.tokens = listTokens;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static final Parcelable.Creator<Finanzbuchung_Buchung> CREATOR = new Parcelable.Creator<Finanzbuchung_Buchung>() {
        @Override
        public Finanzbuchung_Buchung createFromParcel(Parcel in) {
            Finanzbuchung_Buchung Eintrag = null;
            try {
                Eintrag = new Finanzbuchung_Buchung(in);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Eintrag;
        }

        @Override
        public Finanzbuchung_Buchung[] newArray(int size) {
            return new Finanzbuchung_Buchung[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    protected Finanzbuchung_Buchung(Parcel in) throws ParseException {
        super(in.readInt(), in.readInt(), in.readInt(), in.readString(), in.readDouble(), (LocalDate) in.readSerializable());
        kontoId = in.readInt();
        buchungspositionen = in.createTypedArrayList(FinanzbuchungPosition.CREATOR);
        tokens = in.createTypedArrayList(FinanzbuchungToken.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeInt(getBenutzerId());
        dest.writeInt(getKooperationId());
        dest.writeString(getBeschreibung());
        dest.writeDouble(getBetrag());
        dest.writeSerializable(getDatum());
        dest.writeInt(kontoId);
        dest.writeTypedList(buchungspositionen);
        dest.writeTypedList(tokens);
    }

    public int getKontoId() {
        return kontoId;
    }

    public ArrayList<FinanzbuchungPosition> getBuchungspositionen() {
        return buchungspositionen;
    }

    public ArrayList<FinanzbuchungToken> getTokens() {
        return tokens;
    }

    public void setKontoId(int kontoId) {
        this.kontoId = kontoId;
    }

    public void setBuchungspositionen(ArrayList<FinanzbuchungPosition> buchungspositionen) {
        this.buchungspositionen = buchungspositionen;
    }

    public void setTokens(ArrayList<FinanzbuchungToken> tokens) {
        this.tokens = tokens;
    }
}

