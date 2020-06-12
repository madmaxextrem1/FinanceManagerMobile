package net.sytes.financemanagermm.financemanagermobile.Datenmanagement;

import android.app.Application;

public class FinanceManagerMobileApplication extends Application {
    private static FinanceManagerMobileApplication instance;
    private DataManagement dataManagement;

    public static FinanceManagerMobileApplication getInstance() {
        if(instance == null) {
            synchronized (FinanceManagerMobileApplication.class) {
                instance = new FinanceManagerMobileApplication();
            }
        }
            return instance;
    }

    public FinanceManagerMobileApplication () {
        this.dataManagement = new DataManagement(getApplicationContext());
    }
}
