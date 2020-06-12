package net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges;

import android.util.Log;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungskategorie;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;

import java.time.LocalDate;
import java.util.ArrayList;

public final class SQL_Finanzbuchungen_Filter {
    private static int benutzerId = GlobaleVariablen.getInstance().getUserId();
    private static UserAndKooperationFilterTyp userAndKooperationFilterTyp = UserAndKooperationFilterTyp.USER;
    private static LocalDate zeitraumVon = LocalDate.now().withDayOfMonth(1);
    private static LocalDate zeitraumBis = LocalDate.now();
    private static ArrayList<Integer> tokenIDs;
    private static Buchungskategorie buchungskategorie;
    private static int kontoId;
    private static GlobaleVariablen.BuchungstitelFilter titelfilterTyp;
    private static String titelfilter;
    private static double betrag;
    private static BetragFilterTyp betragFilterTyp;
    private static int kooperationId = 0;

    public static String getFilterString() {
        StringBuilder benutzerKooperationOutput = new StringBuilder();
        switch (userAndKooperationFilterTyp) {
            case USER:
                benutzerKooperationOutput.append("BenutzerID = ").append(benutzerId);
                break;
            case KOOPERATION:
                benutzerKooperationOutput.append("KooperationID = ").append(kooperationId);
                break;
            case BOTH_OR:
                benutzerKooperationOutput.append("(BenutzerID = ").
                        append(benutzerId).
                        append(" OR KooperationID = ").
                        append(kooperationId).append(")");
                break;
            case BOTH_AND:
                benutzerKooperationOutput.append("(BenutzerID = ").
                        append(benutzerId).
                        append(" AND KooperationID = ").
                        append(kooperationId).append(")");
                break;
        }

        StringBuilder dateOutput = new StringBuilder();
        if(zeitraumVon != null && zeitraumBis != null) {
            dateOutput.append("Datum BETWEEN ").
                    append(GlobaleVariablen.getInstance().getSQL_DateFormat().format(zeitraumVon)).
                    append(" AND ").
                    append(GlobaleVariablen.getInstance().getSQL_DateFormat().format(zeitraumBis));
        }

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
        if (kontoId != 0) {
            KontoOutput.append("KontoID = ").append(kontoId);
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

        //Hier wird der Filter-String komplett zusammengesetzt
        StringBuilder sFilter = new StringBuilder();
        if(!userAndKooperationFilterTyp.toString().equals("")) {
            sFilter.append(userAndKooperationFilterTyp);
        }

        if(!dateOutput.toString().equals("")) {
            sFilter.append(" AND ").append(dateOutput.toString());
        }

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
        Log.d("Test123!", sFilter.toString());

        return sFilter.toString();
    }

    public static void setFilterDataZeitraum(LocalDate ZeitraumVon, LocalDate ZeitraumBis) {
        zeitraumVon = ZeitraumVon;
        zeitraumBis = ZeitraumBis;
    }

    public static void setFilterDataBuchungskategorie(Buchungskategorie Buchungskategorie) {
        buchungskategorie = Buchungskategorie;
    }

    public static void setFilterDataKontoId(int kontoId) {
        kontoId = kontoId;
    }

    public static void setFilterDataTokenIDs(ArrayList<Integer> TokenIDs) {
        tokenIDs = TokenIDs;
    }

    public static void setFilterDataTitelfilter(String titel, GlobaleVariablen.BuchungstitelFilter filterTyp) {
        titelfilter = titel.trim();
        titelfilterTyp = filterTyp;
    }

    public static void setFilterDataBetrag(double betrag, BetragFilterTyp betragFilterTyp) {
        SQL_Finanzbuchungen_Filter.betrag = betrag;
        SQL_Finanzbuchungen_Filter.betragFilterTyp = betragFilterTyp;
    }

    public static void setFilterDataKooperationUserId(int userId, int kooperationId, UserAndKooperationFilterTyp filterTyp) {
        SQL_Finanzbuchungen_Filter.kooperationId = kooperationId;
        SQL_Finanzbuchungen_Filter.benutzerId = userId;
        SQL_Finanzbuchungen_Filter.userAndKooperationFilterTyp = filterTyp;
    }


    public static String getZeitraumVonSQLFormat() {
        return GlobaleVariablen.getInstance().getSQL_DateFormat().format(zeitraumVon);
    }

    public static String getZeitraumBisSQLFormat() {
        return GlobaleVariablen.getInstance().getSQL_DateFormat().format(zeitraumBis);
    }

    public static void resetFilterData() {
        zeitraumVon = LocalDate.now().withDayOfMonth(1);
        zeitraumBis = LocalDate.now();
        buchungskategorie = null;
        kontoId = 0;
        tokenIDs = null;
        titelfilterTyp = GlobaleVariablen.BuchungstitelFilter.GLEICH;
        titelfilter = "";
        betrag = 0;
        betragFilterTyp = BetragFilterTyp.GLEICH;
        kooperationId = 0;
        benutzerId = GlobaleVariablen.getInstance().getUserId();
        userAndKooperationFilterTyp = UserAndKooperationFilterTyp.USER;
    }

    public enum BetragFilterTyp {
        GLEICH,
        KLEINERGLEICH,
        GRÖßERGLEICH;
    }

    public enum UserAndKooperationFilterTyp {
        USER,
        KOOPERATION,
        BOTH_OR,
        BOTH_AND;
    }
}
