package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Hauptmenu.Hauptmenu;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.FinanceManagerData;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

public class KooperationAnfrageBenutzer implements Parcelable, FinanceManagerData {
    private int BenutzerID;
    private String Benutzername;
    private String Mail;
    private double verteilung;
    private AnfrageBenutzerStatus status;
    private Date Datum;

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
    public Date getDatum() {
        return Datum;
    }
    public void setDatum(Date Datum) {
        this.Datum = Datum;
    }
    public double getVerteilung() {
        return verteilung;
    }
    public void setVerteilung(double verteilung) {
        this.verteilung = verteilung;
    }
    public AnfrageBenutzerStatus getStatus() {
        return status;
    }
    public void setStatus(AnfrageBenutzerStatus status) {
        this.status = status;
    }

    public KooperationAnfrageBenutzer(KooperationAnfrageBenutzer benutzer) {
        this.BenutzerID = benutzer.BenutzerID;
        this.Benutzername = benutzer.Benutzername;
        this.Mail = benutzer.Mail;
        this.verteilung = benutzer.verteilung;
        this.status = benutzer.status;
        this.Datum = benutzer.Datum;
    }

    public KooperationAnfrageBenutzer(int BenutzerID, String Benutzername, String Mail, double verteilung, AnfrageBenutzerStatus status, Date Datum) {
        this.BenutzerID = BenutzerID;
        this.Benutzername = Benutzername;
        this.Mail = Mail;
        this.verteilung = verteilung;
        this.status = status;
        this.Datum = Datum;
    }

    public KooperationAnfrageBenutzer (JSONObject Benutzerdaten) {
        try {
            //getting the name from the json object and putting it inside string array
            this.BenutzerID = Benutzerdaten.getInt("BenutzerID");
            this.Benutzername = Benutzerdaten.getString("Benutzername");
            this.Mail = Benutzerdaten.getString("Email");
            this.verteilung = Benutzerdaten.getDouble("Verteilung");
            this.status = AnfrageBenutzerStatus.getStatusFromString(Benutzerdaten.getString("Status"));
            this.Datum = (Benutzerdaten.getString("Datum").equals("")) ? null : GlobaleVariablen.getInstance().getSQL_DateFormat().parse(Benutzerdaten.getString("Datum"));
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public  final Creator<KooperationAnfrageBenutzer> CREATOR = new Creator<KooperationAnfrageBenutzer>() {
        @Override
        public KooperationAnfrageBenutzer createFromParcel(Parcel in) {
            KooperationAnfrageBenutzer Eintrag = null;
            try {
                Eintrag =  new KooperationAnfrageBenutzer(in);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Eintrag;
        }
        @Override
        public KooperationAnfrageBenutzer[] newArray(int size) {
            return new KooperationAnfrageBenutzer[size];
        }
    };
    public KooperationAnfrageBenutzer(Parcel in) throws ParseException {
        BenutzerID = in.readInt();
        Benutzername = in.readString();
        Mail = in.readString();
        verteilung = in.readDouble();
        status = (AnfrageBenutzerStatus) in.readSerializable();
        Datum = new Date(in.readLong());
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
        dest.writeDouble(verteilung);
        dest.writeSerializable(status);
        dest.writeLong(Datum.getTime());
    }

    @Override
    public int getIdentifier() {
        return Mail.hashCode();
    }
    public KooperationAnfrageBenutzer getCopy() {
        return new KooperationAnfrageBenutzer(this);
    }

    public enum AnfrageBenutzerStatus {
        OFFEN("Offen", R.drawable.ic_questionmark_thin),
        BESTÄTIGT("Bestätigt", R.drawable.ic_check_white_24dp),
        ABGELEHNT("Abgelehnt", R.drawable.ic_clear_white_24dp),
        KOOPERATION("Kooperation", R.drawable.ic_group_white_24dp);
        private String stringValue;
        private int Image_ResID;

        AnfrageBenutzerStatus(String Name, int Image_resID) {
            stringValue = Name;
            Image_ResID = Image_resID;
        }

        @Override
        public String toString() {
            return stringValue;
        }

        public Drawable getStatusImage() {
            return Hauptmenu.context.getDrawable(Image_ResID);
        }
        public static AnfrageBenutzerStatus getStatusFromString (String status) {
            switch (status) {
                case "Bestätigt":
                    return BESTÄTIGT;
                case "Abgelehnt":
                    return ABGELEHNT;
                case "Kooperation":
                    return KOOPERATION;
                default:
                    return OFFEN;
            }
        }
    }
 }
