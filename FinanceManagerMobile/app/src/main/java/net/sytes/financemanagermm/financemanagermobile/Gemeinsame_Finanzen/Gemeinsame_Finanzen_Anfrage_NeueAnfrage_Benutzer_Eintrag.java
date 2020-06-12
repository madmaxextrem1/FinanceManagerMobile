package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;

public class Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag implements Parcelable {
    private int BenutzerID;
    private String Benutzername;
    private String Mail;

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
    public Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag(int BenutzerID, String Benutzername, String Mail) {
        this.BenutzerID = BenutzerID;
        this.Benutzername = Benutzername;
        this.Mail = Mail;
    }
    public  final Creator<Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag> CREATOR = new Creator<Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag>() {
        @Override
        public Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag createFromParcel(Parcel in) {
            Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag Eintrag = null;
            try {
                Eintrag =  new Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag(in);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Eintrag;
        }
        @Override
        public Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag[] newArray(int size) {
            return new Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag[size];
        }
    };
    private Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag(Parcel in) throws ParseException {
        BenutzerID = in.readInt();
        Benutzername = in.readString();
        Mail = in.readString();
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
    }
}
