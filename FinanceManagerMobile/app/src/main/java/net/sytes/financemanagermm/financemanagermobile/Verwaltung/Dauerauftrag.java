package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Konten;
import net.sytes.financemanagermm.financemanagermobile.Hauptmenu.Hauptmenu;
import net.sytes.financemanagermm.financemanagermobile.R;
import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.FinanceManagerData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

public class Dauerauftrag implements Parcelable, FinanceManagerData {
    private int id;
    private String Beschreibung;
    private Konto Konto;
    private double Betrag;
    private int KategorieID;
    private int TokenID;
    private String Rhythmus;
    private Date AusgeführtAm;
    private Date NächsteAusführung;
    private boolean Aktiv;
    private int kooperationID;
    private Dauerauftrag_Art dauerauftragArt;

    public Dauerauftrag() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBeschreibung() {
        return Beschreibung;
    }

    public void setBeschreibung(String Beschreibung) {
        this.Beschreibung = Beschreibung;
    }

    public Konto getKonto() {
        return Konto;
    }

    public void setKonto(Konto Konto) {
        this.Konto = Konto;
    }

    public Double getBetrag() {
        return Betrag;
    }

    public void setBetrag(Double Betrag) {
        this.Betrag = Betrag;
    }

    public Integer getKategorieID() {
        return KategorieID;
    }

    public void setKategorieID(Integer KategorieID) {
        this.KategorieID = KategorieID;
    }

    public Integer getTokenID() {
        return TokenID;
    }

    public void setTokenID(Integer TokenID) {
        this.TokenID = TokenID;
    }

    public String getRhythmus() {
        return Rhythmus;
    }

    public void setRhythmus(String Rhythmus) {
        this.Rhythmus = Rhythmus;
    }

    public Dauerauftrag(int id, String Beschreibung, double Betrag , int TokenID, int kooperationID, String Rhythmus, Date AusgeführtAm, Date NächsteAusführung, boolean Aktiv, Dauerauftrag_Art dauerauftragArt) {
        this.id = id;
        this.Beschreibung = Beschreibung;
        this.Betrag = Betrag;
        this.TokenID = TokenID;
        this.Rhythmus = Rhythmus;
        this.AusgeführtAm = AusgeführtAm;
        this.NächsteAusführung = NächsteAusführung;
        this.Aktiv = Aktiv;
        this.kooperationID = kooperationID;
        this.dauerauftragArt = dauerauftragArt;
    }

    public Dauerauftrag(int id, String Beschreibung, Konto Konto, double Betrag, int KategorieID, int TokenID, int kooperationID, String Rhythmus, Date AusgeführtAm, Date NächsteAusführung, boolean Aktiv, Dauerauftrag_Art dauerauftragArt) {
        this.id = id;
        this.Beschreibung = Beschreibung;
        this.Konto = Konto;
        this.Betrag = Betrag;
        this.KategorieID = KategorieID;
        this.TokenID = TokenID;
        this.Rhythmus = Rhythmus;
        this.AusgeführtAm = AusgeführtAm;
        this.NächsteAusführung = NächsteAusführung;
        this.Aktiv = Aktiv;
        this.kooperationID = kooperationID;
        this.dauerauftragArt = dauerauftragArt;
    }

    public Dauerauftrag(JSONObject dauerauftragDaten) {

        JSONObject obj = dauerauftragDaten;
        try {
            int id = obj.getInt("ID");
            String Beschreibung = obj.getString("Beschreibung");
            int KontoID = obj.getInt("KontoID");
            int KategorieID = obj.getInt("KategorieID");
            int TokenID = obj.getInt("TokenID");
            int kooperationID = obj.getInt("KooperationID");
            double Betrag = obj.getDouble("Betrag");
            String Rhythmus = obj.getString("Rhythmus");
            String AusgeführtAm = obj.getString("AusgeführtAm");
            String NächsteAusführung = obj.getString("NächsteAusführung");
            String Aktiv = obj.getString("Aktiv");

            this.id = id;
            this.Beschreibung = Beschreibung;
            this.Konto = Konten.getKontoById(KontoID);
            this.Betrag = Betrag;
            this.KategorieID = KategorieID;
            this.TokenID = TokenID;
            this.kooperationID = kooperationID;
            this.Rhythmus = Rhythmus;
            this.AusgeführtAm = GlobaleVariablen.getInstance().getSQL_DateFormat().parse(AusgeführtAm);
            this.NächsteAusführung = GlobaleVariablen.getInstance().getSQL_DateFormat().parse(NächsteAusführung);
            this.Aktiv = Aktiv.equals("1");
            this.dauerauftragArt = Dauerauftrag_Art.NORMAL;

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<Dauerauftrag> CREATOR = new Creator<Dauerauftrag>() {
        @Override
        public Dauerauftrag createFromParcel(Parcel in) {
            Dauerauftrag Eintrag = null;
            try {
                Eintrag = new Dauerauftrag(in);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Eintrag;
        }

        @Override
        public Dauerauftrag[] newArray(int size) {
            return new Dauerauftrag[size];
        }
    };

    protected Dauerauftrag(Parcel in) throws ParseException {
        id = in.readInt();
        Beschreibung = in.readString();
        Rhythmus = in.readString();
        Betrag = in.readDouble();
        Konto = (net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto) in.readSerializable();
        TokenID = in.readInt();
        kooperationID = in.readInt();
        KategorieID = in.readInt();
        AusgeführtAm = GlobaleVariablen.getInstance().getDE_DateFormat().parse(in.readString());
        NächsteAusführung = GlobaleVariablen.getInstance().getDE_DateFormat().parse(in.readString());
        Aktiv = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(Beschreibung);
        dest.writeString(Rhythmus);
        dest.writeDouble(Betrag);
        dest.writeSerializable(Konto);
        dest.writeInt(TokenID);
        dest.writeInt(kooperationID);
        dest.writeInt(KategorieID);
        dest.writeLong(AusgeführtAm.getTime());
        dest.writeLong(NächsteAusführung.getTime());
        dest.writeByte((byte) (Aktiv ? 1 : 0));
    }

    public void setBetrag(double betrag) {
        Betrag = betrag;
    }

    public void setKategorieID(int kategorieID) {
        KategorieID = kategorieID;
    }

    public void setTokenID(int tokenID) {
        TokenID = tokenID;
    }

    public Date getAusgeführtAm() {
        return AusgeführtAm;
    }

    public void setAusgeführtAm(Date ausgeführtAm) {
        AusgeführtAm = ausgeführtAm;
    }

    public Date getNächsteAusführung() {
        return NächsteAusführung;
    }

    public void setNächsteAusführung(Date nächsteAusführung) {
        NächsteAusführung = nächsteAusführung;
    }

    public boolean isAktiv() {
        return Aktiv;
    }

    public void setAktiv(boolean aktiv) {
        Aktiv = aktiv;
    }

    public int getKooperationID() {
        return kooperationID;
    }

    public void setKooperationID(int kooperationID) {
        this.kooperationID = kooperationID;
    }

    public Dauerauftrag_Art getDauerauftragArt() {
        return dauerauftragArt;
    }

    public void setDauerauftragArt(Dauerauftrag_Art dauerauftragArt) {
        this.dauerauftragArt = dauerauftragArt;
    }

    @Override
    public int getIdentifier() {
        return 37 * id + 87 * dauerauftragArt.toString().hashCode();
    }

    enum Dauerauftrag_Art {
        NORMAL("Normal"),
        UMBUCHUNG("Umbuchung");

        private String stringValue;

        Dauerauftrag_Art(String Name) {
            stringValue = Name;
        }

        @Override
        public String toString() {
            return stringValue;
        }

        public Drawable getImage(Konto konto) {
            return (stringValue.equals("Normal")) ? konto.getKontoArt().getKonto_Image() : Hauptmenu.context.getDrawable(R.drawable.ic_swap_horizontal_orientation_arrows);
        }
        public static net.sytes.financemanagermm.financemanagermobile.Verwaltung.Dauerauftrag.Dauerauftrag_Art getDauerauftragArtByName(String name) {
            switch (name) {
                case "Normal":
                    return NORMAL;
                case "Umbuchung":
                    return UMBUCHUNG;
            }
            return NORMAL;
        }
    }

}

