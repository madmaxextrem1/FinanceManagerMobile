package net.sytes.financemanagermm.financemanagermobile.Buchungen;

import android.text.Editable;

public interface MerkmaleAutoCompleteCallback<T> {
        boolean onPopupItemClicked(Editable editable, T item);
        void onPopupVisibilityChanged(boolean shown);
}
