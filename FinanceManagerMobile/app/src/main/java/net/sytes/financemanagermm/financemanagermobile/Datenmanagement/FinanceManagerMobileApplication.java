package net.sytes.financemanagermm.financemanagermobile.Datenmanagement;

import android.app.Application;

public class FinanceManagerMobileApplication extends Application {
    private static FinanceManagerMobileApplication instance;
    private DataManagement dataManagement;

    public static FinanceManagerMobileApplication getInstance() {
        if (instance == null) {
            synchronized (FinanceManagerMobileApplication.class) {
                instance = new FinanceManagerMobileApplication();
                instance.dataManagement = new DataManagement(instance.getApplicationContext());
            }
        }
        return instance;
    }

    public FinanceManagerMobileApplication() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        instance.dataManagement = new DataManagement(instance.getApplicationContext());
    }

    public DataManagement getDataManagement() {
        return dataManagement;
    }
}
