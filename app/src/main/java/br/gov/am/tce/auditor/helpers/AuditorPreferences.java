package br.gov.am.tce.auditor.helpers;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Adriano on 07/03/2018.
 */

public class AuditorPreferences {
    private static final String PREF_USERNAME = "username";

    public static String getUsername(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_USERNAME, null);
    }

    public static void setUsername(Context context, String username) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_USERNAME, username)
                .apply();
    }
}
