package br.gov.am.tce.auditor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.domain.BemPublico;

/**
 * Created by Adriano on 24/04/2018.
 */

public class BemPublicoPagerActivity extends AppCompatActivity {
    private static final String TAG = "BemPublicoViewPagerActivity";
    private static final String EXTRA_BENSPUBLICOS_LIST = "br.gov.am.tce.auditor.bensPublicosList";

    private List<BemPublico> mBensPublicos = new ArrayList<>();

    public static Intent newIntent(Context context, List<BemPublico> bensPublicos) {
        Intent intent = new Intent(context, BemPublicoPagerActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_BENSPUBLICOS_LIST, (ArrayList<? extends Parcelable>) bensPublicos);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bempublico_pager);

        mBensPublicos = getIntent().getParcelableArrayListExtra(EXTRA_BENSPUBLICOS_LIST);

        ViewPager viewPager = findViewById(R.id.bemPublico_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                BemPublico bemPublico = mBensPublicos.get(position);
                return BemPublicoFragment.newInstance(bemPublico);
            }

            @Override
            public int getCount() {
                return mBensPublicos.size();
            }
        });
    }
}
