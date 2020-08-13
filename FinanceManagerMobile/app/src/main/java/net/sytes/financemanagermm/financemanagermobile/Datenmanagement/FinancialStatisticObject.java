package net.sytes.financemanagermm.financemanagermobile.Datenmanagement;

import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.KooperationBenutzer;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.User;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalField;
import java.util.Calendar;

public class FinancialStatisticObject {
        private double einzahlungen, auszahlungen, ergebnis;
        private User benutzer;
        private LocalDate fromDate, toDate;

        public FinancialStatisticObject(User benutzer, int year, int month, double einzahlungen, double auszahlungen, double ergebnis) {
            LocalDate fromDate = LocalDate.of(year, month, 1);
            LocalDate toDate = LocalDate.of(year, month, fromDate.lengthOfMonth());

            this.benutzer = benutzer;
            this.fromDate = fromDate;
            this.toDate = toDate;
            this.einzahlungen = einzahlungen;
            this.auszahlungen = auszahlungen;
            this.ergebnis = ergebnis;
        }

        public FinancialStatisticObject(User benutzer, LocalDate fromDate, LocalDate toDate, double einzahlungen, double auszahlungen, double ergebnis) {
            this.benutzer = benutzer;
            this.fromDate = fromDate;
            this.toDate = toDate;
            this.einzahlungen = einzahlungen;
            this.auszahlungen = auszahlungen;
            this.ergebnis = ergebnis;
        }

        public LocalDate getFromDate() {
            return fromDate;
        }

        public LocalDate getToDate() {
            return toDate;
        }

        public double getEinzahlungen() {
            return einzahlungen;
        }

        public double getAuszahlungen() {
            return auszahlungen;
        }

        public double getErgebnis() {
            return ergebnis;
        }

        public User getBenutzer() {
            return benutzer;
        }
}
