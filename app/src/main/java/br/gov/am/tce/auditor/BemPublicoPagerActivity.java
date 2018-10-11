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
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.control.ContextHandler;
import br.gov.am.tce.auditor.model.BemPublico;

/**
 * Created by Adriano on 24/04/2018.
 */

public class BemPublicoPagerActivity extends AppCompatActivity {
    private static final String BEM_PUBLICO_EXTRA = "br.gov.am.tce.auditor.bem_publicoList";
    private List<BemPublico> mBemPublicoList = new ArrayList<>();

    public static Intent newIntent(Context context, List<BemPublico> bemPublicoList) {
        Intent intent = new Intent(context, BemPublicoPagerActivity.class);
        intent.putParcelableArrayListExtra(BEM_PUBLICO_EXTRA, (ArrayList<? extends Parcelable>) bemPublicoList);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        mBemPublicoList = getIntent().getParcelableArrayListExtra(BEM_PUBLICO_EXTRA);

        ViewPager viewPager = findViewById(R.id.view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                BemPublico bemPublico = mBemPublicoList.get(position);
                return BemPublicoFragment.newInstance(bemPublico);
            }

            @Override
            public int getCount() {
                return mBemPublicoList.size();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ContextHandler.get().bpPagerSwipe(mBemPublicoList.get(position).getId());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.ic_done:
                ContextHandler.get().onDone(this);
                return false;
            case R.id.ic_cancel:
                ContextHandler.get().onCancel(this);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
