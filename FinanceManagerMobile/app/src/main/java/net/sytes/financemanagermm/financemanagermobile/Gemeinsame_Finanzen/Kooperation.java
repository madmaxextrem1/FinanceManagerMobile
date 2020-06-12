package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.os.Parcel;
import android.os.Parcelable;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Kooperationen;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.FinanceManagerData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class Kooperation implements Parcelable, FinanceManagerData {
    private int id;
    private ArrayList<KooperationBenutzer> BenutzerAuswahlListe = new ArrayList<KooperationBenutzer>();
    private Date Erstelldatum;
    private int ErstellerID;
    private String Beschreibung;
    private Boolean Aktiv;
    private Boolean Gelöscht;

    public Kooperation () {
        this.id = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<KooperationBenutzer> getBenutzerAuswahlListe() {
        return BenutzerAuswahlListe;
    }

    public void setBenutzerAuswahlListe(ArrayList<KooperationBenutzer> BenutzerAuswahlListe) {
        this.BenutzerAuswahlListe = BenutzerAuswahlListe;
    }

    public int getErstellerID() {
        return ErstellerID;
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

    public Kooperation(int id, String Beschreibung, int ErstellerID, Date Erstelldatum, ArrayList<KooperationBenutzer> Benutzerdaten, Boolean Aktiv, Boolean Gelöscht) {
        this.id = id;
        this.ErstellerID = ErstellerID;
        this.Erstelldatum = Erstelldatum;
        this.Beschreibung = Beschreibung;
        this.BenutzerAuswahlListe = Benutzerdaten;
        this.Aktiv = Aktiv;
        this.Gelöscht = Gelöscht;
    }

    public Kooperation(JSONObject Kooperationsdaten) {
        try {
            //getting the name from the json object and putting it inside string array
            int id = Kooperationsdaten.getInt("ID");
            int ErstellerID = Kooperationsdaten.getInt("ErstellerID");
            Date Erstelldatum = GlobaleVariablen.getInstance().getSQL_DateFormat().parse(Kooperationsdaten.getString("Erstelldatum"));
            String Beschreibung = Kooperationsdaten.getString("Beschreibung");
            boolean Aktiv = Kooperationsdaten.getString("Aktiv").equals("1");
            boolean Gelöscht = Kooperationsdaten.getString("Gelöscht").equals("1");

            JSONArray benutzerdatenArray = Kooperationsdaten.getJSONArray("Benutzerdaten");
            ArrayList<KooperationBenutzer> BenutzerdatenListe =
                    new ArrayList<KooperationBenutzer>();

            for (int i = 0; i < benutzerdatenArray.length(); i++) {
                BenutzerdatenListe.add(new KooperationBenutzer(benutzerdatenArray.getJSONObject(i)));
            }

            this.id = id;
            this.ErstellerID = ErstellerID;
            this.Erstelldatum = Erstelldatum;
            this.Beschreibung = Beschreibung;
            this.BenutzerAuswahlListe = BenutzerdatenListe;
            this.Aktiv = Aktiv;
            this.Gelöscht = Gelöscht;

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static final Parcelable.Creator<Kooperation> CREATOR = new Creator<Kooperation>() {
        @Override
        public Kooperation createFromParcel(Parcel in) {
            Kooperation Eintrag = null;
            try {
                Eintrag = new Kooperation(in);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Eintrag;
        }

        @Override
        public Kooperation[] newArray(int size) {
            return new Kooperation[size];
        }
    };

    protected Kooperation(Parcel in) throws ParseException {
        id = in.readInt();
        ErstellerID = in.readInt();
        Erstelldatum = new Date(in.readLong());
        Beschreibung = in.readString();
        BenutzerAuswahlListe = in.readArrayList(KooperationBenutzer.class.getClassLoader());
        Aktiv = in.readByte() != 0;
        Gelöscht = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(ErstellerID);
        dest.writeLong(Erstelldatum.getTime());
        dest.writeString(Beschreibung);
        dest.writeList(BenutzerAuswahlListe);
        dest.writeByte((byte) (Aktiv ? 1 : 0));
        dest.writeByte((byte) (Gelöscht ? 1 : 0));
    }

    public KooperationBenutzer getBenutzerById(int BenutzerID) {
        return BenutzerAuswahlListe.stream().filter(kooperationBenutzer -> kooperationBenutzer.getBenutzerID() == BenutzerID).findFirst().orElse(null);
    }

    @Override
    public int getIdentifier() {
        return id;
    }

    public Kooperation getObject() {
        return this;
    }
}

