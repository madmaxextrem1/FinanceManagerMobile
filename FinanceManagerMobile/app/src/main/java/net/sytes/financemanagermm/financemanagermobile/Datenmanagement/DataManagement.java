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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

public class DataManagement extends Observable {
    private User currentUser;
    private Context context;
    private boolean initialized = false;
    private List<Observer> registeredViews;
    private ServerCommunication serverCommunication;
    private LinkedHashMap<Integer, Konto> accounts;
    private LinkedHashMap<Integer, Buchungshauptkategorie> categories;
    private HashMap<Integer, FinanzbuchungToken> tokens;
    private LinkedHashMap<Integer, Dauerauftrag> recurringOrders;
    private HashMap<Integer, Finanzbuchung> financialEntries;
    private LinkedHashMap<Integer, KooperationAnfrage> cooperationRequests;
    private LinkedHashMap<Integer, Kooperation> cooperations;

    public DataManagement(Context context) {
        this.context = context;
        this.registeredViews = new ArrayList<Observer>();
        this.serverCommunication = new ServerCommunication(context);
        this.accounts = new LinkedHashMap<>();
        this.categories = new LinkedHashMap<>();
        this.tokens = new HashMap<>();
        this.cooperations = new LinkedHashMap<>();
        this.financialEntries = new HashMap<>();
    }

    public DataManagement(Context context, User currentUser) {
        this(context);
        this.currentUser = currentUser;
    }

    public void initializeData() {
        if(initialized) return;

        if (currentUser == null) throw new IllegalStateException("Der User darf nicht NULL sein.");

        //Initialisierung der Konten
        serverCommunication.queryAccounts(currentUser.getUserId(), 0, new ServerCommunicationInterface.GeneralCommunicationCallback<LinkedHashMap<Integer, Konto>>() {
            @Override
            public void onRequestCompleted(LinkedHashMap<Integer, Konto> data) {
                System.out.println("Anzahl geladene Konten: " + data.size());
                data.values().forEach(account -> DataManagement.this.accounts.put(account.getIdentifier(), account));
            }
        });

        //Initialisierung der Buchungskategorien
        serverCommunication.queryCategories(currentUser.getUserId(), new ServerCommunicationInterface.GeneralCommunicationCallback<LinkedHashMap<Integer, Buchungshauptkategorie>>() {
            @Override
            public void onRequestCompleted(LinkedHashMap<Integer, Buchungshauptkategorie> data) {
                data.values().forEach(category -> DataManagement.this.categories.put(category.getId(), category));
            }
        });

        //Initialisierung der Tokens
        serverCommunication.queryTokens(currentUser.getUserId(), new ServerCommunicationInterface.GeneralCommunicationCallback<HashMap<Integer, FinanzbuchungToken>>() {
            @Override
            public void onRequestCompleted(HashMap<Integer, FinanzbuchungToken> data) {
                data.values().forEach(token -> DataManagement.this.tokens.put(token.getIdentifier(), token));
            }
        });

        //Initialisierung der Kooperationen
        serverCommunication.queryCooperations(currentUser.getUserId(), new ServerCommunicationInterface.GeneralCommunicationCallback<LinkedHashMap<Integer, Kooperation>>() {
            @Override
            public void onRequestCompleted(LinkedHashMap<Integer, Kooperation> data) {
                data.values().forEach(coop -> DataManagement.this.cooperations.put(coop.getIdentifier(), coop));
            }
        });

        //Initialisierung der Buchungsdaten
        QueryFilter filter = new QueryFilter();

        serverCommunication.queryFinancialEntries(filter, new ServerCommunicationInterface.GeneralCommunicationCallback<HashMap<Integer, Finanzbuchung>>() {
            @Override
            public void onRequestCompleted(HashMap<Integer, Finanzbuchung> data) {
                data.values().forEach(entry -> DataManagement.this.financialEntries.put(entry.getIdentifier(), entry));

                //Initialisierung ist hier abgeschlossen
                initialized = true;
                updateObservers();
            }
        });
    }

    public void registerView(Observer observer) {
        this.registeredViews.add(observer);
    }

    public void unregisterView(Observer observer) {
        this.registeredViews.remove(observer);
    }

    public void updateObservers() {
        registeredViews.forEach(view -> view.update(DataManagement.this, null));
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

    public void addFinancialEntry(Finanzbuchung entry) {
        financialEntries.put(entry.getIdentifier(), entry);
        updateObservers();
    }

    public LinkedHashMap<Integer, KooperationAnfrage> getCooperationRequests() {
        return cooperationRequests;
    }

    public LinkedHashMap<Integer, Kooperation> getCooperations() {
        return cooperations;
    }

    public void removeMainCategory(int categoryId) {
        categories.remove(categoryId);
        updateObservers();
    }

    public void removeSubCategory(int mainCategoryId, int subCategoryId) {
        categories.get(mainCategoryId).getUnterkategorien().remove(subCategoryId);
        updateObservers();
    }
}
