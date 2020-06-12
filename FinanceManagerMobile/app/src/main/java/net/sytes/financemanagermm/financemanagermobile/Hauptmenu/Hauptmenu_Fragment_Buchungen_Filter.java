package net.sytes.financemanagermm.financemanagermobile.Hauptmenu;

import android.util.Log;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import java.util.ArrayList;
import java.util.Date;

public final class Hauptmenu_Fragment_Buchungen_Filter {
    private static String zeitraumVon;
    private static String zeitraumBis;
    private static ArrayList<Integer> tokenIDs;
    private static Buchungskategorie buchungskategorie;
    private static Konto konto;
    private static GlobaleVariablen.BuchungstitelFilter titelfilterTyp;
    private static String titelfilter;
    private static double betrag;
    private static BetragFilterTyp betragFilterTyp;
    private static Kooperation kooperation;

    public static String getFilterString() {
        StringBuilder TokenOutput = new StringBuilder();

        if (tokenIDs != null) {
            for (Integer TokenID : tokenIDs) {
                if (TokenOutput.toString().equals("")) {
                    TokenOutput.append("ID IN (SELECT BuchungID FROM Finanzdaten_Token WHERE TokenID IN (").append(TokenID.toString());
                } else {
                    TokenOutput.append(", ").append(TokenID.toString());
                }
            }
        }

        if (!TokenOutput.toString().equals("")) {
            TokenOutput.append("))");
        }

        StringBuilder BuchungstitelOutput = new StringBuilder();

        if (titelfilter != null && titelfilterTyp != null) {
            if (!titelfilter.equals("")) {
                switch (titelfilterTyp) {
                    case GLEICH:
                        BuchungstitelOutput.append("Beschreibung = '").append(titelfilter).append("'");
                        break;
                    case ENHÄLT:
                        BuchungstitelOutput.append("Beschreibung LIKE '%").append(titelfilter).append("%'");
                        break;
                    case BEGINNT_MIT:
                        BuchungstitelOutput.append("Beschreibung LIKE '").append(titelfilter).append("%'");
                        break;
                }
            }
        }

        StringBuilder KontoOutput = new StringBuilder();
        if (konto != null) {
            KontoOutput.append("KontoID = ").append(konto.getIdentifier());
        }

        StringBuilder KategorieOutput = new StringBuilder();
        if (buchungskategorie != null) {
            KategorieOutput.append("ID IN (SELECT DISTINCT ID FROM Finanzdaten WHERE Datum BETWEEN '").append(zeitraumVon).append("' AND '").append(zeitraumBis).append("' AND KatID IN (").append(buchungskategorie.getId()).append("))");
        }

        StringBuilder betragOutput = new StringBuilder();

        if (betrag != 0 && betragFilterTyp != null) {
            switch (betragFilterTyp) {
                case GLEICH:
                    betragOutput.append("Abs(Betrag) = '").append(betrag).append("'");
                    break;
                case GRÖßERGLEICH:
                    betragOutput.append("Abs(Betrag) >= '").append(betrag).append("'");
                    break;
                case KLEINERGLEICH:
                    betragOutput.append("Abs(Betrag) <= '").append(betrag).append("'");
                    break;
            }
        }

        StringBuilder kooperationOutput = new StringBuilder();
        if (kooperation != null) {
            kooperationOutput.append("KooperationID = ").append(kooperation.getIdentifier());
        }

        StringBuilder sFilter = new StringBuilder();
        if (!BuchungstitelOutput.toString().equals("")) {
            sFilter.append(" AND ").append(BuchungstitelOutput);
        }
        if (!TokenOutput.toString().equals("")) {
            sFilter.append(" AND ").append(TokenOutput);
        }
        if (!KontoOutput.toString().equals("")) {
            sFilter.append(" AND ").append(KontoOutput);
        }
        if (!KategorieOutput.toString().equals("")) {
            sFilter.append(" AND ").append(KategorieOutput);
        }
        if(!betragOutput.toString().equals("")) {
            sFilter.append(" AND ").append(betragOutput);
        }
        if(!kooperationOutput.toString().equals("")) {
            sFilter.append(" AND ").append(kooperationOutput);
        }

        Log.d("Test123!", sFilter.toString());

        return sFilter.toString();
    }

    public static void setFilterDataZeitraum(Date ZeitraumVon, Date ZeitraumBis) {
        zeitraumVon = GlobaleVariablen.getInstance().getUS_DateFormat().format(ZeitraumVon);
        zeitraumBis = GlobaleVariablen.getInstance().getUS_DateFormat().format(ZeitraumBis);
    }

    public static void setFilterDataBuchungskategorie(Buchungskategorie Buchungskategorie) {
        buchungskategorie = Buchungskategorie;
    }

    public static void setFilterDataKonto(Konto Konto) {
        konto = Konto;
    }

    public static void setFilterDataTokenIDs(ArrayList<Integer> TokenIDs) {
        tokenIDs = TokenIDs;
    }

    public static void setFilterDataTitelfilter(String Titelfilter) {
        titelfilter = Titelfilter.trim();
    }

    public static void setFilterDataTitelfilterTyp(GlobaleVariablen.BuchungstitelFilter Filtertyp) {
        titelfilterTyp = Filtertyp;
    }

    public static void setFilterDataBetrag(double betrag) {
        Hauptmenu_Fragment_Buchungen_Filter.betrag = betrag;
    }

    public static void setFilterDataBetragFilterTyp(BetragFilterTyp betragFilterTyp) {
        Hauptmenu_Fragment_Buchungen_Filter.betragFilterTyp = betragFilterTyp;
    }

    public static void setFilterDataKooperation(Kooperation kooperation) {
        Hauptmenu_Fragment_Buchungen_Filter.kooperation = kooperation;
    }

    public static String getZeitraumVonUSFormat() {
        return zeitraumVon;
    }

    public static String getZeitraumBisUSFormat() {
        return zeitraumBis;
    }

    public static void resetFilterData(Date ZeitraumVon, Date ZeitraumBis) {
        zeitraumVon = GlobaleVariablen.getInstance().getUS_DateFormat().format(ZeitraumVon);
        zeitraumBis = GlobaleVariablen.getInstance().getUS_DateFormat().format(ZeitraumBis);

        buchungskategorie = null;
        konto = null;
        tokenIDs = null;
        titelfilterTyp = GlobaleVariablen.BuchungstitelFilter.GLEICH;
        titelfilter = "";
        betrag = 0;
        betragFilterTyp = BetragFilterTyp.GLEICH;
        kooperation = null;
    }

    public enum BetragFilterTyp {
        GLEICH,
        KLEINERGLEICH,
        GRÖßERGLEICH;
    }
}
