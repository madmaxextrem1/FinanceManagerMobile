package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tylersuehr.chips.Chip;

import net.sytes.financemanagermm.financemanagermobile.Hauptmenu.Hauptmenu;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.text.ParseException;

public class Gemeinsame_Finanzen_Anfrage_Benutzer extends Chip implements Parcelable {
    private int BenutzerID;
    private String Benutzername;
    private String Mail;
    private Boolean Checked;

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
    public Boolean getChecked() {
        return Checked;
    }
    public void setChecked(Boolean Checked) {
        this.Checked = Checked;
    }

    public Gemeinsame_Finanzen_Anfrage_Benutzer(int BenutzerID, String Benutzername, String Mail, Boolean Checked) {
        this.BenutzerID = BenutzerID;
        this.Benutzername = Benutzername;
        this.Mail = Mail;
        this.Checked = Checked;
    }
    public  final Creator<Gemeinsame_Finanzen_Anfrage_Benutzer> CREATOR = new Creator<Gemeinsame_Finanzen_Anfrage_Benutzer>() {
        @Override
        public Gemeinsame_Finanzen_Anfrage_Benutzer createFromParcel(Parcel in) {
            Gemeinsame_Finanzen_Anfrage_Benutzer Eintrag = null;
            try {
                Eintrag =  new Gemeinsame_Finanzen_Anfrage_Benutzer(in);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Eintrag;
        }
        @Override
        public Gemeinsame_Finanzen_Anfrage_Benutzer[] newArray(int size) {
            return new Gemeinsame_Finanzen_Anfrage_Benutzer[size];
        }
    };
    private Gemeinsame_Finanzen_Anfrage_Benutzer(Parcel in) throws ParseException {
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

    @Override
    public Object getId() {
        return BenutzerID;
    }

    @NonNull
    @Override
    public String getTitle() {
        return Benutzername;
    }

    @Nullable
    @Override
    public String getSubtitle() {
        return Mail;
    }

    @Override
    public Uri getAvatarUri() {
        return null;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return Hauptmenu.context.getDrawable(R.drawable.ic_user_icon);
    }
}
