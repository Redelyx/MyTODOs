package com.example.androidmobdev;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * A FragmentPagerAdapter that returns a fragment corresponding to the current page
 * of the ViewPager
 */
public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

    public AppSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Returns the Fragment associated to the element in position 'i'
     */
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new MainFragment(0);
            case 1:
                return new MainFragment(1);
            case 2:
                return new MainFragment(2);
            default:
                return new MainFragment(0);        }
    }

    /**
     * Returns the number of element in the ViewPager
     */
    @Override
    public int getCount() {
        return 3;
    }

    /**
     * Returns the title (as CharSequence) of the Page at position 'position'
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "All";
            case 1:
                return "To Do";
            case 2:
                return "Done";
            default:
                return "No Title !";
        }
    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }
}
