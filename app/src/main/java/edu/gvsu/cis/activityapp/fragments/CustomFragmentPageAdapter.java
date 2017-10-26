package edu.gvsu.cis.activityapp.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import edu.gvsu.cis.activityapp.util.ActivityMapManager;

/**
 * Created by Kyle Flynn on 10/17/2017.
 */

public class CustomFragmentPageAdapter extends FragmentPagerAdapter {

    public CustomFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        System.out.println("POSITION DUMBASS: " + position);
        switch (position) {
            case 0:
                return new MapFragment();
            case 1:
                return new TestFragment();
            case 2:
                return new TestFragment();
            case 3:
                return new TestFragment();
            default:
                return new TestFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Map";
            case 1:
                return "Places";
            case 2:
                return "Messages";
            case 3:
                return "Favorites?";
            default:
                return "";
        }
    }

}
