package net.sytes.financemanagermm.financemanagermobile.Datenmanagement;

import android.content.Context;

import net.sytes.financemanagermm.financemanagermobile.Buchungen.Finanzbuchung;
import net.sytes.financemanagermm.financemanagermobile.Buchungen.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.Kooperation;
import net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen.KooperationAnfrage;
import net.sytes.financemanagermm.financemanagermobile.ServerCommunication.ServerCommunication;
import net.sytes.financemanagermm.financemanagermobile.ServerCommunication.ServerCommunicationInterface;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Dauerauftrag;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.User;

import java.util.LinkedHashMap;

public class DataManagement {
    private User currentUser;
    private Context context;
    private ServerCommunication serverCommunication;
    private LinkedHashMap<Integer, Konto> accounts;
    private LinkedHashMap<Integer, FinanzbuchungToken> tokens;
    private LinkedHashMap<Integer, Dauerauftrag> recurringOrders;
    private LinkedHashMap<Integer, Finanzbuchung> financialEntries;
    private LinkedHashMap<Integer, KooperationAnfrage> cooperationRequests;
    private LinkedHashMap<Integer, Kooperation> cooperations;

    public DataManagement (Context context) {
        this.context = context;
        this.serverCommunication = new ServerCommunication(context);
    }

    public DataManagement (Context context, User currentUser) {
        this.currentUser =  currentUser;
        this.context = context;
        this.serverCommunication = new ServerCommunication(context);
    }

    public void initializeData() {
        //Initialisierung der Konten
        serverCommunication.queryAccounts(currentUser.getUserId(), 0, new ServerCommunicationInterface.GeneralCommunicationCallback<LinkedHashMap<Integer, Konto>>() {
            @Override
            public void onRequestCompleted(LinkedHashMap<Integer, Konto> data) {
                DataManagement.this.accounts = data;
            }
        });

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
