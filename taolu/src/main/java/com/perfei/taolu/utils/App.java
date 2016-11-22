package com.perfei.taolu.utils;

import android.app.Application;

import com.maxleap.MaxLeap;

public class App extends Application {

    public static final String APP_ID = "571f6f5c60b2daef4296f3b8";
    public static final String API_KEY = "dFNHZTY5S1ZLc2p3OTVNdmlHNC1lUQ";

    @Override
    public void onCreate() {
        super.onCreate();

        if (APP_ID.startsWith("Replace") || API_KEY.startsWith("Replace")) {
            throw new IllegalArgumentException("Please replace with your app id and api key first before" +
                    "using MaxLeap SDK.");
        }
        /*
         * Fill in this section with your MaxLeap credentials
		 */
        MaxLeap.setLogLevel(MaxLeap.LOG_LEVEL_VERBOSE);
        MaxLeap.initialize(this, APP_ID, API_KEY, MaxLeap.REGION_CN);


    }

}
