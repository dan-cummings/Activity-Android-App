package edu.gvsu.cis.activityapp.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.gvsu.cis.activityapp.R;

/**
 * Created by Kyle Flynn on 10/17/2017.
 */

public class TabbedFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public TabbedFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_nav, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new CustomFragmentPageAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
