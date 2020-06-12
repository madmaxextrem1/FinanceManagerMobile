package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Viewpager_Adapter extends FragmentPagerAdapter {
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Viewpager_Adapter(FragmentManager fragmentManager) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }
    @Override
    public  Fragment getItem(int position) {
        switch (position) {
            case 0:
                Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Konten fragmentKonten = new Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Konten();
                return fragmentKonten;
            case 1:
                Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Benutzer fragmentBenutzer = new Gemeinsame_Finanzen_Anfrage_BearbeitenDialog_Fragment_Benutzer();
                return fragmentBenutzer;
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
                return "Konten";
            case 1:
                return "Benutzer";
        }
        return null;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
