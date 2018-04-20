package br.gov.am.tce.auditor;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.domain.Photo;


/**
 * Created by adrnm on 16/10/2017.
 */

public class MapsActivity extends SingleFragmentActivity {
    private static final String EXTRA_PHOTO_LIST = "br.gov.am.tce.auditor.selectedPhotos";

    public static Intent newIntent(Context packageContext, List<Photo> photoList) {
        Intent intent = new Intent(packageContext, MapsActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_PHOTO_LIST, (ArrayList<? extends Parcelable>) photoList);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        List<Photo> photoList = getIntent().getParcelableArrayListExtra(EXTRA_PHOTO_LIST);
        return new MapsFragment().newInstance(photoList);
    }
}
