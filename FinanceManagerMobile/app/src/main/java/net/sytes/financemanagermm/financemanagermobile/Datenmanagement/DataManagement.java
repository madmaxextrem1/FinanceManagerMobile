package net.sytes.financemanagermm.financemanagermobile.Datenmanagement;

import android.content.Context;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungshauptkategorie;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.KooperationAnfrage;
import net.sytes.financemanagermm.financemanagermobile.ServerCommunication.QueryFilter;
import net.sytes.financemanagermm.financemanagermobile.ServerCommunication.ServerCommunication;
import net.sytes.financemanagermm.financemanagermobile.ServerCommunication.ServerCommunicationInterface;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Dauerauftrag;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.User;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class DataManagement {
    private User currentUser;
    private Context context;
    private boolean initialized;
    private ServerCommunication serverCommunication;
    private LinkedHashMap<Integer, Konto> accounts;
    private LinkedHashMap<Integer, Buchungshauptkategorie> categories;
    private HashMap<Integer, FinanzbuchungToken> tokens;
    private LinkedHashMap<Integer, Dauerauftrag> recurringOrders;
    private HashMap<Integer, Finanzbuchung> financialEntries;
    private LinkedHashMap<Integer, KooperationAnfrage> cooperationRequests;
    private LinkedHashMap<Integer, Kooperation> cooperations;

    public DataManagement (Context context) {
        this.context = context;
        this.serverCommunication = new ServerCommunication(context);
        this.accounts = new LinkedHashMap<>();
        this.categories = new LinkedHashMap<>();
        this.tokens = new HashMap<>();
        this.cooperations = new LinkedHashMap<>();
        this.financialEntries = new HashMap<>();
    }

    public DataManagement (Context context, User currentUser) {
        this(context);
        this.currentUser =  currentUser;
    }

    public void initializeData() {
        if(currentUser==null) throw new IllegalStateException("Der User darf nicht NULL sein.");

        //Initialisierung der Konten
        synchronized (accounts) {
            serverCommunication.queryAccounts(currentUser.getUserId(), 0, new ServerCommunicationInterface.GeneralCommunicationCallback<LinkedHashMap<Integer, Konto>>() {
                @Override
                public void onRequestCompleted(LinkedHashMap<Integer, Konto> data) {
                    data.values().forEach(account -> DataManagement.this.accounts.put(account.getIdentifier(), account));
                }
            });
        }


        //Initialisierung der Buchungskategorien
        synchronized (categories) {
            serverCommunication.queryCategories(currentUser.getUserId(), new ServerCommunicationInterface.GeneralCommunicationCallback<LinkedHashMap<Integer, Buchungshauptkategorie>>() {
                @Override
                public void onRequestCompleted(LinkedHashMap<Integer, Buchungshauptkategorie> data) {
                    data.values().forEach(category -> DataManagement.this.categories.put(category.getId(), category));
                }
            });
        }

        //Initialisierung der Tokens
        synchronized (tokens) {
            serverCommunication.queryTokens(currentUser.getUserId(), new ServerCommunicationInterface.GeneralCommunicationCallback<HashMap<Integer, FinanzbuchungToken>>() {
                @Override
                public void onRequestCompleted(HashMap<Integer, FinanzbuchungToken> data) {
                    data.values().forEach(token -> DataManagement.this.tokens.put(token.getIdentifier(), token));
                }
            });
        }

        //Initialisierung der Kooperationen
        synchronized (cooperations) {
            serverCommunication.queryCooperations(currentUser.getUserId(), new ServerCommunicationInterface.GeneralCommunicationCallback<LinkedHashMap<Integer, Kooperation>>() {
                @Override
                public void onRequestCompleted(LinkedHashMap<Integer, Kooperation> data) {
                    data.values().forEach(coop -> DataManagement.this.cooperations.put(coop.getIdentifier(), coop));
                }
            });
        }

        //Initialisierung der Buchungsdaten
        synchronized (financialEntries) {
            QueryFilter filter = new QueryFilter();

            serverCommunication.queryFinancialEntries(filter, new ServerCommunicationInterface.GeneralCommunicationCallback<HashMap<Integer, Finanzbuchung>>() {
                @Override
                public void onRequestCompleted(HashMap<Integer, Finanzbuchung> data) {
                    data.values().forEach(entry -> DataManagement.this.financialEntries.put(entry.getIdentifier(), entry));
                }
            });
        }

    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public LinkedHashMap<Integer, Konto> getAccounts() {
        return accounts;
    }

    public LinkedHashMap<Integer, Konto> getActiveAccounts() {
        return accounts.values().stream().filter(Konto::getAktiv).collect(Collectors.toMap(Konto::getIdentifier, konto -> konto, (prev, next) -> next, LinkedHashMap::new));
    }

    public LinkedHashMap<Integer, Buchungshauptkategorie> getCategories() {
        return categories;
    }

    public HashMap<Integer, FinanzbuchungToken> getTokens() {
        return tokens;
    }

    public LinkedHashMap<Integer, Dauerauftrag> getRecurringOrders() {
        return recurringOrders;
    }

    public HashMap<Integer, Finanzbuchung> getFinancialEntries() {
        return financialEntries;
    }

    public LinkedHashMap<Integer, KooperationAnfrage> getCooperationRequests() {
        return cooperationRequests;
    }

    public LinkedHashMap<Integer, Kooperation> getCooperations() {
        return cooperations;
    }

}
