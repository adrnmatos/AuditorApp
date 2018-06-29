package br.gov.am.tce.auditor;

import android.support.v4.app.Fragment;


/**
 * Created by adrnm on 12/10/2017.
 */

public class PhotoGridActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PhotoGridFragment.newInstance();
    }

}
