package net.sytes.financemanagermm.financemanagermobile.Verwaltung;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Verwaltung_Daueraufträge_Fragment_Viewpager_Adapter extends FragmentPagerAdapter {
    private Dauerauftrag dauerauftrag;
    private Dauerauftrag.Dauerauftrag_Art dauerauftragArt;
    private int kooperationID;
    private boolean callFromKooperation;

    public Verwaltung_Daueraufträge_Fragment_Viewpager_Adapter(FragmentManager fragmentManager, Dauerauftrag Dauerauftrag, Dauerauftrag.Dauerauftrag_Art dauerauftragArt, int kooperationID, boolean callFromKooperation) {
        super(fragmentManager);
        this.dauerauftrag = Dauerauftrag;
        this.dauerauftragArt = dauerauftragArt;
        this.kooperationID = kooperationID;
        this.callFromKooperation = callFromKooperation;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Verwaltung_Daueraufträge_Fragment_Allgemein fragmentAllgemein = new Verwaltung_Daueraufträge_Fragment_Allgemein(dauerauftrag, dauerauftragArt);
                return fragmentAllgemein;
            case 1:
                Verwaltung_Daueraufträge_Fragment_Details fragmentDetails = new Verwaltung_Daueraufträge_Fragment_Details (dauerauftrag, dauerauftragArt, kooperationID, callFromKooperation);
                return fragmentDetails;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Allgemein";
            case 1:
                return "Details";
        }
        return null;
    }
}
