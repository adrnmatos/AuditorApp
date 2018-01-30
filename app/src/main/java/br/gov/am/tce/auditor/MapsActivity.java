package br.gov.am.tce.auditor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by adrnm on 16/10/2017.
 */

public class MapsActivity extends SingleFragmentActivity {

    private static final String EXTRA_PHOTO_IDS = "br.gov.am.tce.auditor.photo_ids";

    public static Intent newIntent(Context packageContext, List<String> uuidList) {
        Intent intent = new Intent(packageContext, MapsActivity.class);
        intent.putStringArrayListExtra(EXTRA_PHOTO_IDS, (ArrayList<String>) uuidList);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        ArrayList<String> uuidArrayList = getIntent().getStringArrayListExtra(EXTRA_PHOTO_IDS);
        return new MapsFragment().newInstance(uuidArrayList);
    }
}
