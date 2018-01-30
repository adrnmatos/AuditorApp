package br.gov.am.tce.auditor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


/**
 * Created by adrnm on 12/10/2017.
 */

public class PhotoListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PhotoListFragment.newInstance();
    }

}
