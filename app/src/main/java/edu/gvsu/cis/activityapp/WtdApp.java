package edu.gvsu.cis.activityapp;

import android.app.Application;
import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by daniel on 12/3/17.
 */

public class WtdApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
