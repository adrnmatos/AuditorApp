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

import br.gov.am.tce.auditor.domain.Contract;
import br.gov.am.tce.auditor.domain.Medicao;

/**
 * Created by Adriano on 19/03/2018.
 */

public class ContractPagerActivity extends AppCompatActivity {
    private static final String TAG = "ContractPagerActivity";
    private static final String EXTRA_CONTRACT_LIST = "br.gov.am.tce.auditor.contractList";

    private ViewPager mViewPager;
    private List<Contract> mContracts = new ArrayList<>();

    public static Intent newIntent(Context context, List<Contract> contracts) {
        Intent intent = new Intent(context, ContractPagerActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_CONTRACT_LIST, (ArrayList<? extends Parcelable>) contracts);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_pager);

        mContracts = getIntent().getParcelableArrayListExtra(EXTRA_CONTRACT_LIST);

        mViewPager = findViewById(R.id.contract_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Contract contract = mContracts.get(position);
                return ContractFragment.newInstance(contract);
            }

            @Override
            public int getCount() {
                return mContracts.size();
            }
        });

    }
}