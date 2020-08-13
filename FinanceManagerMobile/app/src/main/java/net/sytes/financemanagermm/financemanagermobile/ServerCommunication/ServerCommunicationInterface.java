package net.sytes.financemanagermm.financemanagermobile.ServerCommunication;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungshauptkategorie;
import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.Finanzbuchung;
import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.Finanzbuchung_Buchung;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.User;

import java.util.HashMap;
import java.util.LinkedHashMap;

public interface ServerCommunicationInterface {
    void authenticateUser(String eMail, String password, GeneralCommunicationCallback<User> callback);
    boolean registerUser(String eMail, String userName, String password);
    void queryAccounts(int userId, int kooperationId, GeneralCommunicationCallback<LinkedHashMap<Integer, Konto>> callback);
    void queryCategories(int userId, GeneralCommunicationCallback<LinkedHashMap<Integer, Buchungshauptkategorie>> callback);
    void queryCooperations(int userId, GeneralCommunicationCallback<LinkedHashMap<Integer, Kooperation>> callback);
    void queryTokens(int userId, GeneralCommunicationCallback<HashMap<Integer, FinanzbuchungToken>> callback);
    void queryFinancialEntries(QueryFilter filter, GeneralCommunicationCallback<HashMap<Integer, Finanzbuchung>> callback);
    void postFinancialEntry(Finanzbuchung_Buchung entry, boolean editMode, GeneralCommunicationCallback<Finanzbuchung_Buchung> callback);
    void deleteFinancialEntry(Finanzbuchung_Buchung entry, GeneralCommunicationCallback<Boolean> callback);
    public interface GeneralCommunicationCallback<T> {
        void onRequestCompleted(T data);
    }
}
