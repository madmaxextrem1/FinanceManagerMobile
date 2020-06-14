package net.sytes.financemanagermm.financemanagermobile.Datenmanagement;

import android.content.Context;

import com.klinker.android.link_builder.Link;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Buchungshauptkategorie;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.Finanzbuchung;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.KooperationAnfrage;
import net.sytes.financemanagermm.financemanagermobile.ServerCommunication.ServerCommunication;
import net.sytes.financemanagermm.financemanagermobile.ServerCommunication.ServerCommunicationInterface;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Dauerauftrag;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.User;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class DataManagement {
    private User currentUser;
    private Context context;
    private boolean initialized;
    private ServerCommunication serverCommunication;
    private LinkedHashMap<Integer, Konto> accounts;
    private LinkedHashMap<Integer, Buchungshauptkategorie> categories;
    private HashMap<Integer, FinanzbuchungToken> tokens;
    private LinkedHashMap<Integer, Dauerauftrag> recurringOrders;
    private LinkedHashMap<Integer, Finanzbuchung> financialEntries;
    private LinkedHashMap<Integer, KooperationAnfrage> cooperationRequests;
    private LinkedHashMap<Integer, Kooperation> cooperations;

    public DataManagement (Context context) {
        this.context = context;
        this.serverCommunication = new ServerCommunication(context);
        this.accounts = new LinkedHashMap<>();
        this.categories = new LinkedHashMap<>();
        this.tokens = new HashMap<>();
        this.cooperations = new LinkedHashMap<>();
    }

    public DataManagement (Context context, User currentUser) {
        this.currentUser =  currentUser;
        this.context = context;
        this.serverCommunication = new ServerCommunication(context);
    }

    public void initializeData() {
        if(currentUser==null) throw new IllegalStateException("Der User darf nicht NULL sein.");

        //Initialisierung der Konten
        synchronized (accounts) {
            serverCommunication.queryAccounts(currentUser.getUserId(), 0, new ServerCommunicationInterface.GeneralCommunicationCallback<LinkedHashMap<Integer, Konto>>() {
                @Override
                public void onRequestCompleted(LinkedHashMap<Integer, Konto> data) {
                    DataManagement.this.accounts = data;
                }
            });
        }

        //Initialisierung der Buchungskategorien
        synchronized (categories) {
            serverCommunication.queryCategories(currentUser.getUserId(), new ServerCommunicationInterface.GeneralCommunicationCallback<LinkedHashMap<Integer, Buchungshauptkategorie>>() {
                @Override
                public void onRequestCompleted(LinkedHashMap<Integer, Buchungshauptkategorie> data) {
                    DataManagement.this.categories = data;
                }
            });
        }

        //Initialisierung der Tokens
        synchronized (tokens) {
            serverCommunication.queryTokens();
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

    public void setAccounts(LinkedHashMap<Integer, Konto> accounts) {
        this.accounts = accounts;
    }

    public LinkedHashMap<Integer, FinanzbuchungToken> getTokens() {
        return tokens;
    }

    public void setTokens(LinkedHashMap<Integer, FinanzbuchungToken> tokens) {
        this.tokens = tokens;
    }

    public LinkedHashMap<Integer, Dauerauftrag> getRecurringOrders() {
        return recurringOrders;
    }

    public void setRecurringOrders(LinkedHashMap<Integer, Dauerauftrag> recurringOrders) {
        this.recurringOrders = recurringOrders;
    }

    public LinkedHashMap<Integer, Finanzbuchung> getFinancialEntries() {
        return financialEntries;
    }

    public void setFinancialEntries(LinkedHashMap<Integer, Finanzbuchung> financialEntries) {
        this.financialEntries = financialEntries;
    }

    public LinkedHashMap<Integer, KooperationAnfrage> getCooperationRequests() {
        return cooperationRequests;
    }

    public void setCooperationRequests(LinkedHashMap<Integer, KooperationAnfrage> cooperationRequests) {
        this.cooperationRequests = cooperationRequests;
    }

    public LinkedHashMap<Integer, Kooperation> getCooperations() {
        return cooperations;
    }

    public void setCooperations(LinkedHashMap<Integer, Kooperation> cooperations) {
        this.cooperations = cooperations;
    }
}
