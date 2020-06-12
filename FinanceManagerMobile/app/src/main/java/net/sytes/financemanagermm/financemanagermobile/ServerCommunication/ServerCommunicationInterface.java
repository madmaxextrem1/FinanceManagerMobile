package net.sytes.financemanagermm.financemanagermobile.ServerCommunication;

import net.sytes.financemanagermm.financemanagermobile.Verwaltung.Konto;
import net.sytes.financemanagermm.financemanagermobile.Verwaltung.User;

import java.util.LinkedHashMap;

public interface ServerCommunicationInterface {
    void authenticateUser(String eMail, String password, GeneralCommunicationCallback<User> callback);
    boolean registerUser(String eMail, String userName, String password);
    void queryAccounts(int userId, int kooperationId, GeneralCommunicationCallback<LinkedHashMap<Integer, Konto>> callback);

    interface GeneralCommunicationCallback<T> {
        void onRequestCompleted(T data);
    }
}
