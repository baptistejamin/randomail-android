package net.randomail;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import net.randomail.helpers.PreferencesHelper;
import net.randomail.models.User;

public final class RandomailApplication extends Application {

    private static Context sContext;
    public static String mAuthToken;
    public static String mUserId;

    public static User user;
    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

        Bundle bundle = PreferencesHelper.getUserInfo();
        if (bundle != null) {
            mAuthToken = bundle.getString(PreferencesHelper.PREFERENCES_AUTHTOKEN_KEY);
            mUserId = bundle.getString(PreferencesHelper.PREFERENCES_USERID_KEY);
        }

    }

    public static Context getContext() {
        return sContext;
    }

    //Logging TAG
    private static final String TAG = "Randomail";

    public RandomailApplication() {

        super();
    }
}
