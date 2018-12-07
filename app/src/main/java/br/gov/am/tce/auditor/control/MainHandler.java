package br.gov.am.tce.auditor.control;

import android.app.Activity;
import android.content.Intent;

import br.gov.am.tce.auditor.ContextPagerActivity;
import br.gov.am.tce.auditor.SearchActivity;

public class MainHandler {
    private Activity mContext;

    public MainHandler(Activity context) {
        mContext = context;
    }

    public boolean checkGoogleServicesInstall() {
        return false;
    }

    public void requestLocationPermission() {

    }

    public boolean checkLogin() {
        return false;
    }

    public void downloadPhotos() {
        Intent contextIntent = ContextPagerActivity.newIntent(mContext, null);
        mContext.startActivity(contextIntent);
    }

}
