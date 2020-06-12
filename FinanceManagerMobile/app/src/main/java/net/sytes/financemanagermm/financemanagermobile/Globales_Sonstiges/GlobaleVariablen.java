package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungshauptkategorie;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.KooperationAnfrage;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Gemeinsame_Finanzen_Anfrage_Benutzer;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Dauerauftrag;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

public class GlobaleVariablen {
    private static GlobaleVariablen instance = new GlobaleVariablen();

    // Getter-Setters
    public static GlobaleVariablen getInstance() {
        return instance;
    }

    public static void setInstance(GlobaleVariablen instance) {
        GlobaleVariablen.instance = instance;
    }

    private int userId;
    private String userName;
    private String pwd;
    private String email;
    private String Hauptmenu_Fragment_Buchungen_Filter_sDatumVon;
    private String Hauptmenu_Fragment_Buchungen_Filter_sDatumBis;
    private ArrayList<Integer> Hauptmenu_Fragment_Buchungen_Filter_arrTokenID = new ArrayList<Integer>();
    private BuchungstitelFilter Hauptmenu_Fragment_Buchungen_Filter_BuchungstitelFilter;
    private String Hauptmenu_Fragment_Buchungen_Filter_Buchungstitel;
    private Konto Hauptmenu_Fragment_Buchungen_Filter_Konto;
    private Buchungskategorie Hauptmenu_Fragment_Buchungen_Filter_Kategorie;
    private final String US_DateFormatPattern = "yyyy.MM.dd";
    private final String DE_DateFormatPattern = "dd.MM.yyyy";
    private final String SQL_DateFormatPattern = "yyyy-MM-dd";
    private SimpleDateFormat US_DateFormat = new SimpleDateFormat(US_DateFormatPattern , Locale.getDefault());
    private SimpleDateFormat DE_DateFormat = new SimpleDateFormat(DE_DateFormatPattern, Locale.getDefault());
    private SimpleDateFormat SQL_DateFormat = new SimpleDateFormat(SQL_DateFormatPattern, Locale.getDefault());
    private ArrayList<Konto> KontenListe = new ArrayList<Konto>();
    private ArrayList<Dauerauftrag> DauerauftragListe = new ArrayList<Dauerauftrag>();
    private ArrayList<Buchungskategorie> KategorienListe = new ArrayList<Buchungskategorie>();
    private ArrayList<Buchungshauptkategorie> BuchungKategorieAuswahlListe = new ArrayList<Buchungshauptkategorie>();
    private ArrayList<FinanzbuchungToken> TokenListe = new ArrayList<FinanzbuchungToken>();
    private ArrayList<Kooperation> KooperationListe = new ArrayList<Kooperation>();
    private ArrayList<KooperationAnfrage> AnfragenListe = new ArrayList<KooperationAnfrage>();
    private ArrayList<Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag> AnfragenBenutzerListe = new ArrayList<Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag>();

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String Pwd) {
        this.pwd = Pwd;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String Email) {
        this.email = Email;
    }
    public ArrayList<Integer> getHauptmenu_Fragment_Buchungen_Filter_arrTokenID() {
        return Hauptmenu_Fragment_Buchungen_Filter_arrTokenID;
    }
    public void setHauptmenu_Fragment_Buchungen_Filter_arrTokenID(ArrayList<Integer> TokenArray) {
     this.Hauptmenu_Fragment_Buchungen_Filter_arrTokenID = TokenArray;
    }
    public String getHauptmenu_Fragment_Buchungen_Filter_Buchungstitel() {return Hauptmenu_Fragment_Buchungen_Filter_Buchungstitel;}
    public void setHauptmenu_Fragment_Buchungen_Filter_Buchungstitel(String Buchungstitel) {this.Hauptmenu_Fragment_Buchungen_Filter_Buchungstitel = Buchungstitel;}
    public BuchungstitelFilter getHauptmenu_Fragment_Buchungen_Filter_BuchungstitelFilter() {
        return Hauptmenu_Fragment_Buchungen_Filter_BuchungstitelFilter;
    }
    public void setHauptmenu_Fragment_Buchungen_Filter_BuchungstitelFilter(BuchungstitelFilter Filter) {
        this.Hauptmenu_Fragment_Buchungen_Filter_BuchungstitelFilter = Filter;
    }
    public Konto getHauptmenu_Fragment_Buchungen_Filter_Konto() {return Hauptmenu_Fragment_Buchungen_Filter_Konto;}
    public void setHauptmenu_Fragment_Buchungen_Filter_Konto(Konto KontoEintrag) {this.Hauptmenu_Fragment_Buchungen_Filter_Konto = KontoEintrag;}
    public Buchungskategorie getHauptmenu_Fragment_Buchungen_Filter_Kategorie() {return Hauptmenu_Fragment_Buchungen_Filter_Kategorie;}
    public void setHauptmenu_Fragment_Buchungen_Filter_Kategorie(Buchungskategorie KategorieEintrag) {this.Hauptmenu_Fragment_Buchungen_Filter_Kategorie = KategorieEintrag;}





    public String getUS_DateFormatPattern() {
        return US_DateFormatPattern;
    }
    public String getDE_DateFormatPattern() {
        return DE_DateFormatPattern;
    }
    public SimpleDateFormat getUS_DateFormat() {
        this.US_DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return US_DateFormat;
    }
    public SimpleDateFormat getDE_DateFormat() {
        this.DE_DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return DE_DateFormat;
    }
    public String getSQL_DateFormatPattern() {
        return SQL_DateFormatPattern;
    }
    public SimpleDateFormat getSQL_DateFormat() {
        this.SQL_DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return SQL_DateFormat;}










    public String getHauptmenu_Fragment_Buchungen_Filter_sDatumVon() {
        return Hauptmenu_Fragment_Buchungen_Filter_sDatumVon;
    }
    public void setHauptmenu_Fragment_Buchungen_Filter_sDatumVon(String DatumVon) {
        this.Hauptmenu_Fragment_Buchungen_Filter_sDatumVon = DatumVon;
    }
    public String getHauptmenu_Fragment_Buchungen_Filter_sDatumBis() {
        return Hauptmenu_Fragment_Buchungen_Filter_sDatumBis;
    }
    public void setHauptmenu_Fragment_Buchungen_Filter_sDatumBis(String DatumBis) {
        this.Hauptmenu_Fragment_Buchungen_Filter_sDatumBis = DatumBis;
    }
    public ArrayList<Konto> getKontenListe() {
        return KontenListe;
    }
    public Konto findKontoById(Integer KontoID) {
        Konto eintrag = null;
        for(int i = 0; i < KontenListe.size(); i++){
                Integer Id= KontenListe.get(i).getIdentifier();
                if(KontoID.equals(Id)) {
                    eintrag = KontenListe.get(i);
                }
            }
        return eintrag;
    }

    public FinanzbuchungToken findTokenById(Integer TokenId) {
        FinanzbuchungToken eintrag = null;
        for(int i = 0; i < TokenListe.size(); i++){
            Integer Id= TokenListe.get(i).getId();
            if(TokenId.equals(Id)) {
                eintrag = TokenListe.get(i);
            }
        }
        return eintrag;
    }
    public Buchungskategorie findKategorieById(Integer KategorieId) {
        Buchungskategorie eintrag = null;
        for(Buchungskategorie Eintrag: KategorienListe){
            if(Eintrag.getId() == KategorieId) {
                eintrag = Eintrag;
                break;
            }
        }
        return eintrag;
    }
    public void setKontenListe(ArrayList<Konto> kontenListe) {
        KontenListe = kontenListe;
    }
    public ArrayList<Dauerauftrag> getDauerauftragListe() {
        return DauerauftragListe;
    }
    public void setDauerauftragListe(ArrayList<Dauerauftrag> dauerauftragListe) {
        DauerauftragListe = dauerauftragListe;
    }
    public ArrayList<Buchungskategorie> getKategorienListe() {
        return KategorienListe;
    }
    public void setKategorienListe(ArrayList<Buchungskategorie> KategorieListe) {
        KategorienListe = KategorieListe;
    }
    public ArrayList<Buchungshauptkategorie> getBuchungKategorieAuswahlListe () {
        return BuchungKategorieAuswahlListe;
    }
    public void setBuchungKategorieAuswahlListe(ArrayList<Buchungshauptkategorie> ListeBuchungKategorieAuswahl) {
        BuchungKategorieAuswahlListe = ListeBuchungKategorieAuswahl;
    }
    public ArrayList<FinanzbuchungToken> getTokenListe () {
        return TokenListe;
    }
    public void setTokenListe(ArrayList<FinanzbuchungToken> ListeTokenAuswahl) {
        TokenListe = ListeTokenAuswahl;
    }
    public ArrayList<Kooperation> getKooperationListe () {
        return KooperationListe;
    }
    public void setKooperationListe(ArrayList<Kooperation> ListeKooperationAuswahl) {
        KooperationListe = ListeKooperationAuswahl;
    }
    public ArrayList<KooperationAnfrage> getAnfragenListe () {
        return AnfragenListe;
    }
    public void setAnfragenListe(ArrayList<KooperationAnfrage> ListeAnfragenAuswahl) {
        AnfragenListe = ListeAnfragenAuswahl;
    }
    public ArrayList<Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag> getAnfragenBenutzerListe () {
        return AnfragenBenutzerListe;
    }
    public void setAnfragenBenutzerListe(ArrayList<Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag> AnfragenBenutzerListe) {
        this.AnfragenBenutzerListe = AnfragenBenutzerListe;
    }
    public ArrayList<Gemeinsame_Finanzen_Anfrage_Benutzer> getAnfrageBenutzerAuswahlListe() {
        ArrayList<Gemeinsame_Finanzen_Anfrage_Benutzer> returnListe = new ArrayList<Gemeinsame_Finanzen_Anfrage_Benutzer>();

        for(Gemeinsame_Finanzen_Anfrage_NeueAnfrage_Benutzer_Eintrag Eintrag: AnfragenBenutzerListe){
            returnListe.add(new Gemeinsame_Finanzen_Anfrage_Benutzer(
                    Eintrag.getBenutzerID(),Eintrag.getBenutzername(),Eintrag.getMail(),false));
        }

        return returnListe;
    }

    public Gemeinsame_Finanzen_Anfrage_Benutzer getBenutzerByEmail(String Email) {
        Gemeinsame_Finanzen_Anfrage_Benutzer returnEintrag = null;

        for(Gemeinsame_Finanzen_Anfrage_Benutzer eintrag:getAnfrageBenutzerAuswahlListe()) {
            if(eintrag.getMail().equals(Email.toLowerCase().trim())) {
                returnEintrag = eintrag;
                break;
            }
        }
        return returnEintrag;
    }
    public  enum BuchungstitelFilter {
        GLEICH("Gleich", 0),
        BEGINNT_MIT("Beginnt mit",1),
        ENHÄLT("Enthält", 2);

        private String Name;
        private int intValue;
        private BuchungstitelFilter(String name, int value) {
            Name = name;
            intValue = value;
        }

        @Override
        public String toString() {
            return Name;
        }

    }

}
