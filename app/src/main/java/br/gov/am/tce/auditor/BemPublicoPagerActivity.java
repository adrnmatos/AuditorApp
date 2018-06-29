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

import br.gov.am.tce.auditor.control.FindContextHandler;
import br.gov.am.tce.auditor.model.BemPublico;
import br.gov.am.tce.auditor.model.Photo;

/**
 * Created by Adriano on 24/04/2018.
 */

public class BemPublicoPagerActivity extends AppCompatActivity {
    private static final String EXTRA_BEM_PUBLICO_LIST = "br.gov.am.tce.auditor.bempublicoList";
    private static final String EXTRA_IS_EDITING = "br.gov.am.tce.auditor.isEditing";

    private List<BemPublico> mBensPublicos = new ArrayList<>();
    private boolean isEditing;

    public static Intent newIntent(Context context, List<BemPublico> bemPublicoList, boolean isEditing) {
        Intent intent = new Intent(context, BemPublicoPagerActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_BEM_PUBLICO_LIST, (ArrayList<? extends Parcelable>) bemPublicoList);
        intent.putExtra(EXTRA_IS_EDITING, isEditing);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        mBensPublicos = getIntent().getParcelableArrayListExtra(EXTRA_BEM_PUBLICO_LIST);
        isEditing = getIntent().getBooleanExtra(EXTRA_IS_EDITING, false);

        ViewPager viewPager = findViewById(R.id.view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                BemPublico bemPublico = mBensPublicos.get(position);

                return BemPublicoFragment.newInstance(bemPublico, isEditing);
            }

            @Override
            public int getCount() {
                return mBensPublicos.size();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FindContextHandler.get().swipeBP(mBensPublicos.get(position).getId());
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
        if(isEditing) {
            getMenuInflater().inflate(R.menu.photo_edit_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.ic_done:
                FindContextHandler.get().onDone(this);
                return false;
            case R.id.ic_cancel:
                FindContextHandler.get().onCancel(this);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
