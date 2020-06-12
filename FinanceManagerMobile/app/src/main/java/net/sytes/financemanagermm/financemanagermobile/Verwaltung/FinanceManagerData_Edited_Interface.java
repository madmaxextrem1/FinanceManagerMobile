package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import net.sytes.financemanagermm.financemanagermobile.Steuerelemente.FinanceManagerData;

public interface FinanceManagerData_Edited_Interface<T extends FinanceManagerData> {
    void onDataEdited(T data, boolean created);
}
