package br.gov.am.tce.auditor.service;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Adriano on 07/03/2018.
 */

public class AuditorPreferences {
    private static final String PREF_USERNAME = "username";
    private static final String PREF_BEMPUBLICO = "bempublico";
    private static final String PREF_CONTRATO = "contrato";
    private static final String PREF_MEDICAO = "medicao";

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

    public static String getBemPublico(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_BEMPUBLICO, null);
    }

    public static void setBemPublico(Context context, String bemPublico) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_BEMPUBLICO, bemPublico)
                .apply();
    }

    public static String getContrato(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_CONTRATO, null);
    }

    public static void setContrato(Context context, String contrato) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_CONTRATO, contrato)
                .apply();
    }

    public static String getMedicao(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_MEDICAO, null);
    }

    public static void setMedicao(Context context, String medicao) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_MEDICAO, medicao)
                .apply();
    }
}
