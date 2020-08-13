package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;

import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.FinancialStatisticObject;
import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.Finanzbuchung_Buchung;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.Finanzbuchungen_Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Gemeinsame_Finanzen_Kooperation_BenutzerBilanzen_Statistik {
    private ArrayList<KooperationBenutzer> benutzer;
    private Kooperation kooperation;

    public Gemeinsame_Finanzen_Kooperation_BenutzerBilanzen_Statistik (Kooperation kooperation) {
        this.benutzer = kooperation.getBenutzerAuswahlListe();
        this.kooperation = kooperation;
    }

    public ArrayList<FinancialStatisticObject> getStatisticOfMonth(int month, int year) {
        LocalDate firstDate = LocalDate.of(year, month, 1);
        LocalDate lastDate = LocalDate.of(year, month, firstDate.lengthOfMonth());
        Predicate<LocalDate> datePredicate = date -> (date.isAfter(firstDate) || date.isEqual(firstDate)) && (date.isBefore(lastDate) || date.isEqual(lastDate));
        ArrayList<Finanzbuchung_Buchung> listRelevanteBuchungen = Finanzbuchungen_Kooperation.getFinanzbuchungen().stream().filter(buchung -> datePredicate.test(buchung.getDatum())).collect(Collectors.toCollection(ArrayList::new));

        double gesamteAusgaben;
        Function<Integer, KooperationBenutzer> benutzerMappingFunction = id -> kooperation.getBenutzerById(id);

        return null;
    }


}
