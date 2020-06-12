package net.sytes.financemanagermm.financemanagermobile.Steuerelemente;

import android.annotation.TargetApi;

import android.content.Context;

import android.graphics.Rect;

import android.os.Build;

import android.util.AttributeSet;

import android.view.View;
import android.widget.MultiAutoCompleteTextView;


/**

 * Created by PasiMatalamaki on 7.9.2015.

 */

public class InstantMultiAutoCompleteTextView extends MultiAutoCompleteTextView {



    private boolean showAlways;



    public InstantMultiAutoCompleteTextView(Context context) {

        super(context);

    }



    public InstantMultiAutoCompleteTextView(Context context, AttributeSet attrs) {

        super(context, attrs);

    }



    public InstantMultiAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    public InstantMultiAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

    }



    public void setShowAlways(boolean showAlways) {

        this.showAlways = showAlways;

    }



    @Override

    public boolean enoughToFilter() {

        return showAlways || super.enoughToFilter();

    }



    @Override

    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {

        super.onFocusChanged(focused, direction, previouslyFocusedRect);



        showDropDownIfFocused();

    }



    private void showDropDownIfFocused() {

        if (enoughToFilter() && isFocused() && getWindowVisibility() == View.VISIBLE) {



            showDropDown();

        }

    }



    @Override

    protected void onAttachedToWindow() {

        super.onAttachedToWindow();



        showDropDownIfFocused();

    }



}
