package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.os.Parcel;
import android.os.Parcelable;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

public class KooperationBenutzer implements Parcelable {
    private int BenutzerID;
    private String Benutzername;
    private String Mail;
    private Date beigetretenAm;
    private Boolean Aktiv;
    private Boolean Gelöscht;

    public int getBenutzerID() {
        return BenutzerID;
    }
    public void setBenutzerID(int benutzerID) {
        this.BenutzerID = benutzerID;
    }
    public String getBenutzername() {
        return Benutzername;
    }
    public void setBenutzername(String Benutzername) {
        this.Benutzername = Benutzername;
    }
    public String getMail() {
        return Mail;
    }
    public void setMail(String Mail) {
        this.Mail = Mail;
    }
    public Boolean getAktiv() {
        return Aktiv;
    }
    public void setAktiv(Boolean Aktiv) {
        this.Aktiv = Aktiv;
    }
    public Boolean getGelöscht() {
        return Gelöscht;
    }
    public void setGelöscht(Boolean Gelöscht) {
        this.Gelöscht = Gelöscht;
    }

    public Date getBeitrittsdatum() {
        return beigetretenAm;
    }

    public KooperationBenutzer(int BenutzerID, String Benutzername, String Mail, Date BeigetretenAm, Boolean Aktiv, Boolean Gelöscht) {
        this.BenutzerID = BenutzerID;
        this.Benutzername = Benutzername;
        this.Mail = Mail;
        this.beigetretenAm = BeigetretenAm;
        this.Aktiv = Aktiv;
        this.Gelöscht = Gelöscht;
    }

    public KooperationBenutzer(JSONObject KooperationBenutzerDaten) {
        try {
            //getting the name from the json object and putting it inside string array
            int BenutzerID = KooperationBenutzerDaten.getInt("BenutzerID");
            String Benutzername = KooperationBenutzerDaten.getString("Benutzername");
            String Mail = KooperationBenutzerDaten.getString("Email");
            Date BeigetretenAm = GlobaleVariablen.getInstance().getSQL_DateFormat().parse(KooperationBenutzerDaten.getString("BeigetretenAm"));
            Boolean Aktiv = KooperationBenutzerDaten.getString("Aktiv").equals("1");
            Boolean Gelöscht = KooperationBenutzerDaten.getString("Gelöscht").equals("1");

            this.BenutzerID = BenutzerID;
            this.Benutzername = Benutzername;
            this.Mail = Mail;
            this.beigetretenAm = BeigetretenAm;
            this.Aktiv = Aktiv;
            this.Gelöscht = Gelöscht;

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static final Parcelable.Creator<KooperationBenutzer> CREATOR = new Creator<KooperationBenutzer>() {
        @Override
        public KooperationBenutzer createFromParcel(Parcel in) {
            KooperationBenutzer Eintrag = null;
            try {
                Eintrag =  new KooperationBenutzer(in);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Eintrag;
        }
        @Override
        public KooperationBenutzer[] newArray(int size) {
            return new KooperationBenutzer[size];
        }
    };
    public KooperationBenutzer(Parcel in) throws ParseException {
        BenutzerID = in.readInt();
        Benutzername = in.readString();
        Mail = in.readString();
        beigetretenAm = new Date(in.readLong());
        Aktiv = in.readByte() != 0;
        Gelöscht =  in.readByte() != 0;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BenutzerID);
        dest.writeString(Benutzername);
        dest.writeString(Mail);
        dest.writeLong(beigetretenAm.getTime());
        dest.writeByte((byte) (Aktiv ? 1 : 0));
        dest.writeByte((byte) (Gelöscht ? 1 : 0));
    }
}
