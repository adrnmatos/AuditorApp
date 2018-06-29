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
import br.gov.am.tce.auditor.model.Contrato;
import br.gov.am.tce.auditor.model.Photo;

/**
 * Created by Adriano on 19/03/2018.
 */

public class ContratoPagerActivity extends AppCompatActivity {
    private static final String EXTRA_CONTRACT_LIST = "br.gov.am.tce.auditor.contratoList";
    private static final String EXTRA_IS_EDITING = "br.gov.am.tce.auditor.isEditing";

    private List<Contrato> mContracts = new ArrayList<>();
    private boolean isEditing;

    public static Intent newIntent(Context context, List<Contrato> contracts, boolean isEditing) {
        Intent intent = new Intent(context, ContratoPagerActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_CONTRACT_LIST, (ArrayList<? extends Parcelable>) contracts);
        intent.putExtra(EXTRA_IS_EDITING, isEditing);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        mContracts = getIntent().getParcelableArrayListExtra(EXTRA_CONTRACT_LIST);
        isEditing = getIntent().getBooleanExtra(EXTRA_IS_EDITING, false);

        ViewPager viewPager = findViewById(R.id.view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Contrato contract = mContracts.get(position);
                return ContratoFragment.newInstance(contract, isEditing);
            }

            @Override
            public int getCount() {
                return mContracts.size();
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
