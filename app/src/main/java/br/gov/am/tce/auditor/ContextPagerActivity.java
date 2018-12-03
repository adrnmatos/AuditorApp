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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.control.ContextHandler;
import br.gov.am.tce.auditor.model.BemPublico;
import br.gov.am.tce.auditor.model.ContextObject;
import br.gov.am.tce.auditor.model.Contrato;
import br.gov.am.tce.auditor.model.Medicao;
import br.gov.am.tce.auditor.model.Photo;

public class ContextPagerActivity extends AppCompatActivity {
    private static final String PHOTO_LIST = "br.gov.am.tce.auditor.photo_list";
    private ContextHandler mContextHandler;
    private ViewPager mViewPager;
    private ContextPagerAdapter mAdapter;

    private static final int SEARCH_REQUEST_CODE = 100;
    private static final String SEARCH_RETURN_CODE = "br.gov.am.tce.auditor.search_activity_return_code";

    public static Intent newIntent(Context context, List<Photo> photoList) {
        Intent intent = new Intent(context, ContextPagerActivity.class);
        intent.putParcelableArrayListExtra(PHOTO_LIST, (ArrayList<? extends Parcelable>)photoList);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        List<Photo> photoList = getIntent().getParcelableArrayListExtra(PHOTO_LIST);
        mContextHandler = new ContextHandler(this, photoList);
        mViewPager = findViewById(R.id.view_pager);
        updateUI();

        mContextHandler.applyContext();
    }

    public void updateUI() {
        if(mAdapter == null) {
            mAdapter = new ContextPagerAdapter(getSupportFragmentManager());
            mAdapter.setContextObjectList(mContextHandler.getContextObjectList());
            mViewPager.setAdapter(mAdapter);
            mViewPager.addOnPageChangeListener(mAdapter);
        } else {
            mAdapter.setContextObjectList(mContextHandler.getContextObjectList());
            mAdapter.notifyDataSetChanged();
        }
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
                mContextHandler.onDone();
                return false;
            case R.id.ic_cancel:
                mContextHandler.onCancel();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == SEARCH_REQUEST_CODE) {
                List<ContextObject> returnList = data.getParcelableArrayListExtra(SEARCH_RETURN_CODE);
                mContextHandler.setContextObjectList(returnList);
                updateUI();
            }
        }
    }

    public ContextHandler getContextHandler() {
        return mContextHandler;
    }


    // **********************   ADAPTER CLASS   ************************//
    private class ContextPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
        List<ContextObject> contextObjectList;

        private ContextPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            ContextObject contextObject = contextObjectList.get(position);

            if(contextObject instanceof BemPublico)
                return BemPublicoFragment.newInstance((BemPublico) contextObject);
            else if(contextObject instanceof Contrato)
                return ContratoFragment.newInstance((Contrato) contextObject);
            else
                return MedicaoFragment.newInstance((Medicao) contextObject);
        }

        @Override
        public int getCount() {
            return contextObjectList.size();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            ContextObject contextObject = contextObjectList.get(position);

            if(contextObject instanceof BemPublico)
                mContextHandler.setBP(((BemPublico) contextObject).getId());
            else if(contextObject instanceof Contrato)
                mContextHandler.setCT(((Contrato) contextObject).getId());
            else
                mContextHandler.setMD(((Medicao) contextObject).getId());
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        private void setContextObjectList(List<ContextObject> contextObjectList) {
            this.contextObjectList = contextObjectList;
        }

    }

}
