package net.sytes.financemanagermm.financemanagermobile.Gemeinsame_Finanzen;
import android.app.Activity;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Gemeinsame_Finanzen_Viewpager_Adapter extends FragmentPagerAdapter {
    private Activity ParentActivity;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public Gemeinsame_Finanzen_Viewpager_Adapter(FragmentManager fragmentManager, Activity ParentActivity) {
        super(fragmentManager);
        this.ParentActivity = ParentActivity;
    }
    @Override
    public  Fragment getItem(int position) {
        switch (position) {
            case 0:
                Gemeinsame_Finanzen_Fragment_Kooperationen fragmentKooperationen = Gemeinsame_Finanzen_Fragment_Kooperationen.newInstance();
                return fragmentKooperationen;
            case 1:
                Gemeinsame_Finanzen_Fragment_Anfragen fragmentAnfragen = Gemeinsame_Finanzen_Fragment_Anfragen.newInstance(ParentActivity);
                return fragmentAnfragen;
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
                return "Kooperationen";
            case 1:
                return "Anfragen";
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
