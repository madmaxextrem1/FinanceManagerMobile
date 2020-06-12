package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Hauptmenu.Hauptmenu;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.FinanceManagerData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;


public final class Konto implements Parcelable, Serializable, FinanceManagerData {
    private int id;
    private String KontoTitel;
    private Konto.KontoArt KontoArt;
    private Double Bestand;
    private Double Anfangsbestand;
    private int AnzahlBewegungenAktMonat;
    private Double SummeBewegungenAktMonat;
    private Date DatumAnfangsbestand;
    private int KooperationID = 0;
    private boolean Aktiv;

    public Konto(Konto konto) {
        this.id = konto.id;
        this.KontoTitel = konto.KontoTitel;
        this.KontoArt = konto.KontoArt;
        this.Bestand = konto.Bestand;
        this.Anfangsbestand = konto.Anfangsbestand;
        this.DatumAnfangsbestand = konto.DatumAnfangsbestand;
        this.AnzahlBewegungenAktMonat = konto.AnzahlBewegungenAktMonat;
        this.SummeBewegungenAktMonat = konto.SummeBewegungenAktMonat;
        this.Aktiv = konto.Aktiv;
        this.KooperationID = konto.KooperationID;
    }
    public Konto(int id, String KontoTitel, Konto.KontoArt KontoArt, Double Bestand, Double Anfangsbestand, Date DatumAnfangsbestand, boolean Aktiv, int anzahlBewegungenAktMonat, Double summeBewegungenAktMonat, int KooperationID) {
        this.id = id;
        this.KontoTitel = KontoTitel;
        this.KontoArt = KontoArt;
        this.Bestand = Bestand;
        this.Anfangsbestand = Anfangsbestand;
        this.DatumAnfangsbestand = DatumAnfangsbestand;
        this.AnzahlBewegungenAktMonat = anzahlBewegungenAktMonat;
        this.SummeBewegungenAktMonat = summeBewegungenAktMonat;
        this.Aktiv = Aktiv;
        this.KooperationID = KooperationID;
    }

    public Konto(int id, String KontoTitel, Konto.KontoArt KontoArt, Double Bestand, Double Anfangsbestand, Date DatumAnfangsbestand, boolean Aktiv, int anzahlBewegungenAktMonat, Double summeBewegungenAktMonat) {
        this.id = id;
        this.KontoTitel = KontoTitel;
        this.KontoArt = KontoArt;
        this.Bestand = Bestand;
        this.Anfangsbestand = Anfangsbestand;
        this.DatumAnfangsbestand = DatumAnfangsbestand;
        this.AnzahlBewegungenAktMonat = anzahlBewegungenAktMonat;
        this.SummeBewegungenAktMonat = summeBewegungenAktMonat;
        this.Aktiv = Aktiv;
    }

    public Konto(JSONObject jsonObject) {
        try {
            //getting the name from the json object and putting it inside string array
            int id = jsonObject.getInt("ID");
            int koopId = jsonObject.getInt("KooperationID");
            String KontoTitel = jsonObject.getString("KontoTitel");
            String KontoArt = jsonObject.getString("KontoArt");
            Double Bestand = jsonObject.getDouble("Bestand");
            Double Anfangsbestand = jsonObject.getDouble("Anfangsbestand");
            Date DatumAnfangsbestand = GlobaleVariablen.getInstance().getSQL_DateFormat().parse(jsonObject.getString("DatumAnfangsbestand"));
            int AnzahlBewegungen = jsonObject.getInt("AnzahlBewegungen");
            Double SummeBewegungen = jsonObject.getDouble("SummeBewegungen");
            boolean Aktiv = jsonObject.getString("Aktiv").equals("1");
            Konto.KontoArt konto_art = null;
            switch (KontoArt) {
                case "Bargeld":
                    konto_art = this.KontoArt.BARGELD;
                    break;
                case "Bankkonto":
                    konto_art = this.KontoArt.BANKKONTO;
                    break;
                case "Kreditkarte":
                    konto_art = this.KontoArt.KREDITKARTE;
                    break;
            }

            this.id = id;
            this.KontoTitel = KontoTitel;
            this.KontoArt = konto_art;
            this.Bestand = Bestand;
            this.Anfangsbestand = Anfangsbestand;
            this.DatumAnfangsbestand = DatumAnfangsbestand;
            this.AnzahlBewegungenAktMonat = AnzahlBewegungen;
            this.SummeBewegungenAktMonat = SummeBewegungen;
            this.Aktiv = Aktiv;
            this.KooperationID = koopId;

        } catch (JSONException | ParseException e) {
            Log.d("Error_Konten_Laden", e.getMessage());
            return;
        }

    }

    public Konto(JSONObject jsonObject, boolean gemeinsameFinanzen) {
        try {
            //getting the name from the json object and putting it inside string array
            int id = jsonObject.getInt("KontoID");
            int koopId = 0;
            String KontoTitel = jsonObject.getString("Beschreibung");
            String KontoArt = jsonObject.getString("KontoArt");
            Double Bestand = jsonObject.getDouble("Anfangsbestand");
            Double Anfangsbestand = jsonObject.getDouble("Anfangsbestand");
            Date DatumAnfangsbestand = GlobaleVariablen.getInstance().getSQL_DateFormat().parse(jsonObject.getString("DatumAnfangsbestand"));
            int AnzahlBewegungen = 0;
            Double SummeBewegungen = 0.0;
            boolean Aktiv = true;

            Konto.KontoArt konto_art = null;
            switch (KontoArt) {
                case "Bargeld":
                    konto_art = this.KontoArt.BARGELD;
                    break;
                case "Bankkonto":
                    konto_art = this.KontoArt.BANKKONTO;
                    break;
                case "Kreditkarte":
                    konto_art = this.KontoArt.KREDITKARTE;
                    break;
            }

            this.id = id;
            this.KontoTitel = KontoTitel;
            this.KontoArt = konto_art;
            this.Bestand = Bestand;
            this.Anfangsbestand = Anfangsbestand;
            this.DatumAnfangsbestand = DatumAnfangsbestand;
            this.AnzahlBewegungenAktMonat = AnzahlBewegungen;
            this.SummeBewegungenAktMonat = SummeBewegungen;
            this.Aktiv = Aktiv;
            this.KooperationID = koopId;

        } catch (JSONException | ParseException e) {
            Log.d("Error_Konten_Laden", e.getMessage());
            return;
        }

    }

    public int getKooperationID() {
        return KooperationID;
    }

    public boolean isAktiv() {
        return Aktiv;
    }

    public static final Parcelable.Creator<Konto> CREATOR = new Parcelable.Creator<Konto>() {
        @Override
        public Konto createFromParcel(Parcel in) {
            Konto Eintrag = null;
            try {
                Eintrag = new Konto(in);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Eintrag;
        }

        @Override
        public Konto[] newArray(int size) {
            return new Konto[size];
        }
    };

    protected Konto(Parcel in) throws ParseException {
        id = in.readInt();
        KontoTitel = in.readString();
        KontoArt = (Konto.KontoArt) in.readSerializable();
        Bestand = in.readDouble();
        Anfangsbestand = in.readDouble();
        AnzahlBewegungenAktMonat = in.readInt();
        SummeBewegungenAktMonat = in.readDouble();
        DatumAnfangsbestand = GlobaleVariablen.getInstance().getDE_DateFormat().parse(in.readString());
        Aktiv = in.readByte() != 0;
        KooperationID = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(KontoTitel);
        dest.writeSerializable(KontoArt);
        dest.writeDouble(Bestand);
        dest.writeDouble(Anfangsbestand);
        dest.writeInt(AnzahlBewegungenAktMonat);
        dest.writeDouble(SummeBewegungenAktMonat);
        dest.writeString(GlobaleVariablen.getInstance().getDE_DateFormat().format(DatumAnfangsbestand));
        dest.writeByte((byte) (Aktiv ? 1 : 0));
        dest.writeInt(KooperationID);
    }

    public enum KontoArt {
        BARGELD("Bargeld", R.drawable.ic_account_balance_wallet_white_24dp),
        BANKKONTO("Bankkonto", R.drawable.ic_account_balance_white_24dp),
        KREDITKARTE("Kreditkarte", R.drawable.ic_credit_card_white_24dp);

        private String stringValue;
        private int Image_ResID;

        KontoArt(String Name, int Image_resID) {
            stringValue = Name;
            Image_ResID = Image_resID;
        }

        @Override
        public String toString() {
            return stringValue;
        }

        public Drawable getKonto_Image() {
            return Hauptmenu.context.getDrawable(Image_ResID);
        }
        public static KontoArt getKontoArtByName(String name) {
            switch (name) {
                case "Bargeld":
                    return BARGELD;
                case "Bankkonto":
                    return BANKKONTO;
                case "Kreditkarte":
                    return KREDITKARTE;
            }
            return BARGELD;
        }
    }

    public int getIdentifier() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKontoTitel() {
        return KontoTitel;
    }

    public void setKontoTitel(String KontoTitel) {
        this.KontoTitel = KontoTitel;
    }

    public Konto.KontoArt getKontoArt() {
        return KontoArt;
    }

    public void setKontoArt(Konto.KontoArt KontoArt) {
        this.KontoArt = KontoArt;
    }

    public Double getBestand() {
        return Bestand;
    }

    public void setBestand(Double Bestand) {
        this.Bestand = Bestand;
    }

    public Double getAnfangsbestand() {
        return Anfangsbestand;
    }

    public void setAnfangsbestand(Double Anfangsbestand) {
        this.Anfangsbestand = Anfangsbestand;
    }

    public Date getDatumAnfangsbestand() {
        return DatumAnfangsbestand;
    }

    public void setDatumAnfangsbestand(Date DatumAnfangbestand) {
        this.DatumAnfangsbestand = DatumAnfangbestand;
    }

    public int getAnzahlBewegungenAktMonat() {
        return AnzahlBewegungenAktMonat;
    }

    public void setAnzahlBewegungenAktMonat(int anzahlBewegungenAktMonat) {
        this.AnzahlBewegungenAktMonat = anzahlBewegungenAktMonat;
    }

    public Double getSummeBewegungenAktMonat() {
        return SummeBewegungenAktMonat;
    }

    public void setSummeBewegungenAktMonat(Double summeBewegungenAktMonat) {
        this.SummeBewegungenAktMonat = summeBewegungenAktMonat;
    }

    public boolean getAktiv() {
        return this.Aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.Aktiv = aktiv;
    }

    public Konto getCopy() {
        return new Konto(this);
    }
}

