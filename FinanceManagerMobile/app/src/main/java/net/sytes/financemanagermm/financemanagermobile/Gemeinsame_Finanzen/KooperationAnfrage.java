package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.FinanceManagerData;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class KooperationAnfrage implements Parcelable, FinanceManagerData {
    private int id;
    private int ErstellerID;
    private String creatorMail;
    private LinkedHashMap<String, KooperationAnfrageBenutzer> benutzerMap;
    private LinkedHashMap<Integer, Konto> kontoMap;
    private Date Erstelldatum;
    private boolean gleichverteilung;
    private String Beschreibung;
    private int KooperationID;

    public String getCreatorMail() {
        return creatorMail;
    }

    public boolean isGleichverteilung() {
        return gleichverteilung;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getErstellerID() {
        return ErstellerID;
    }

    public void setErstellerID(int ErstellerID) {
        this.ErstellerID = ErstellerID;
    }

    public LinkedHashMap<String, KooperationAnfrageBenutzer> getBenutzerMap() {
        return benutzerMap;
    }

    public LinkedHashMap<Integer, Konto> getKontoMap() {
        return kontoMap;
    }

    public void setKontoMap(LinkedHashMap<Integer, Konto> kontoMap) {
        this.kontoMap = kontoMap;
    }

    public void setGleichverteilung(boolean gleichverteilung) {
        this.gleichverteilung = gleichverteilung;
    }

    public void setBenutzerMap(LinkedHashMap<String, KooperationAnfrageBenutzer> benutzerMap) {
        this.benutzerMap = benutzerMap;
    }

    public Date getErstelldatum() {
        return Erstelldatum;
    }

    public void setErstelldatum(Date Erstelldatum) {
        this.Erstelldatum = Erstelldatum;
    }

    public String getBeschreibung() {
        return Beschreibung;
    }

    public void setBeschreibung(String Beschreibung) {
        this.Beschreibung = Beschreibung;
    }

    public int getKooperationID() {
        return KooperationID;
    }

    public void setKooperationID(int KooperationID) {
        this.KooperationID = KooperationID;
    }

    public KooperationAnfrage(int id, int ErstellerID, String creatorMail, String Beschreibung, boolean Gleichverteilung, Date Erstelldatum, int KooperationID, LinkedHashMap<String, KooperationAnfrageBenutzer> benutzerMap, LinkedHashMap<Integer, Konto> kontoMap) {
        this.id = id;
        this.ErstellerID = ErstellerID;
        this.gleichverteilung = Gleichverteilung;
        this.Erstelldatum = Erstelldatum;
        this.creatorMail = creatorMail;
        this.Beschreibung = Beschreibung;
        this.benutzerMap = benutzerMap;
        this.kontoMap = kontoMap;
        this.KooperationID = KooperationID;
    }

    public KooperationAnfrage(JSONObject Anfragedaten) {
        try {

            //getting the name from the json object and putting it inside string array
            int AnfrageID = Anfragedaten.getInt("ID");
            int ErstellerID = Anfragedaten.getInt("ErstellerID");
            Date Erstelldatum = GlobaleVariablen.getInstance().getSQL_DateFormat().parse(Anfragedaten.getString("Erstelldatum"));
            String Beschreibung = Anfragedaten.getString("Beschreibung");
            int KooperationID = Anfragedaten.getInt("KooperationID");
            boolean Gleichverteilung = Anfragedaten.getInt("Gleichverteilung") == 1;
            String creatorMail = Anfragedaten.getString("ErstellerMail");

            JSONArray benutzerdatenArray = Anfragedaten.getJSONArray("Benutzerdaten");
            LinkedHashMap<String, KooperationAnfrageBenutzer> benutzerMap = new LinkedHashMap<>();

            for (int i = 0; i < benutzerdatenArray.length(); i++) {
                KooperationAnfrageBenutzer user = new KooperationAnfrageBenutzer(benutzerdatenArray.getJSONObject(i));
                benutzerMap.put(user.getMail(), user);
            }

            JSONArray kontoArray = Anfragedaten.getJSONArray("Kontodaten");
            LinkedHashMap<Integer, Konto> kontoMap = new LinkedHashMap<>();

            for (int i = 0; i < kontoArray.length(); i++) {
                Konto konto = new Konto(kontoArray.getJSONObject(i), true);
                kontoMap.put(konto.getIdentifier(), konto);
            }

            this.id = AnfrageID;
            this.ErstellerID = ErstellerID;
            this.Erstelldatum = Erstelldatum;
            this.creatorMail = creatorMail;
            this.gleichverteilung = Gleichverteilung;
            this.Beschreibung = Beschreibung;
            this.benutzerMap = benutzerMap;
            this.kontoMap = kontoMap;
            this.KooperationID = KooperationID;

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<KooperationAnfrage> CREATOR = new Creator<KooperationAnfrage>() {
        @Override
        public KooperationAnfrage createFromParcel(Parcel in) {
            KooperationAnfrage Eintrag = null;
            try {
                Eintrag = new KooperationAnfrage(in);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Eintrag;
        }

        @Override
        public KooperationAnfrage[] newArray(int size) {
            return new KooperationAnfrage[size];
        }
    };

    protected KooperationAnfrage(Parcel in) throws ParseException {
        id = in.readInt();
        ErstellerID = in.readInt();
        creatorMail = in.readString();
        Beschreibung = in.readString();
        gleichverteilung = in.readByte() != 0;
        Erstelldatum = new Date(in.readLong());
        KooperationID = in.readInt();
        benutzerMap = (LinkedHashMap<String, KooperationAnfrageBenutzer>) in.readHashMap(KooperationAnfrageBenutzer.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(ErstellerID);
        dest.writeString(creatorMail);
        dest.writeString(Beschreibung);
        dest.writeByte((byte) (gleichverteilung ? 1 : 0));
        dest.writeLong(Erstelldatum.getTime());
        dest.writeMap(benutzerMap);
        dest.writeInt(KooperationID);
    }

    public boolean getMinOneAccepted() {
        for (KooperationAnfrageBenutzer benutzer : benutzerMap.values()) {
            if (benutzer.getIdentifier() != GlobaleVariablen.getInstance().getEmail().trim().toLowerCase().hashCode() && benutzer.getStatus() == KooperationAnfrageBenutzer.AnfrageBenutzerStatus.BESTÄTIGT ) {
                return true;
            }
        }
        return false;
    }

    public boolean getSelfAccepted() {
        for (KooperationAnfrageBenutzer benutzer : benutzerMap.values()) {
            if (benutzer.getIdentifier() == GlobaleVariablen.getInstance().getEmail().trim().toLowerCase().hashCode() && benutzer.getStatus() == KooperationAnfrageBenutzer.AnfrageBenutzerStatus.BESTÄTIGT) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getIdentifier() {
        return getId();
    }
}

