package net.randomail.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import net.randomail.RandomailApplication;

public final class PreferencesHelper {

    public static final String PREFERENCES_NAME = "net.randomail.preferences";
    public static final String PREFERENCES_AUTHTOKEN_KEY = "net.randomail.preferences.authtoken";
    public static final String PREFERENCES_USERID_KEY = "net.randomail.preferences.userid";

    private static final Context sContext = RandomailApplication.getContext();

    public static final Bundle getUserInfo() {
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(PREFERENCES_NAME, 0);
        String userToken = sharedPreferences.getString(PREFERENCES_AUTHTOKEN_KEY, null);
        String userId = sharedPreferences.getString(PREFERENCES_USERID_KEY, null);

        if (userToken == null || userId == null) return null;
        else {
            Bundle bundle = new Bundle();
            bundle.putString(PREFERENCES_AUTHTOKEN_KEY, userToken);
            bundle.putString(PREFERENCES_USERID_KEY, userId);
            return bundle;
        }
    }

    public static final void setUserInfo(String userToken, String userId) {
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFERENCES_AUTHTOKEN_KEY, userToken);
        editor.putString(PREFERENCES_USERID_KEY, userId);
        editor.commit();
    }

    public static final void clear() {
        SharedPreferences sharedPreferences = sContext.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
