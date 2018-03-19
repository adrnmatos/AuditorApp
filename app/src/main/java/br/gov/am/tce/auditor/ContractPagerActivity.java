package br.gov.am.tce.auditor;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import br.gov.am.tce.auditor.domain.Contract;

/**
 * Created by Adriano on 19/03/2018.
 */

public class ContractPagerActivity extends AppCompatActivity {
    private static final String TAG = "ContractPagerActivity";
    private static final String EXTRA_CONTRACT_LIST = "br.gov.am.tce.auditor.contractList";

    private ViewPager mViewPager;
    private List<Contract> mContracts;

    public static Intent newIntent(Context context, List<Contract> mContracts) {
        Intent intent = new Intent(context, ContractPagerActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_CONTRACT_LIST, mContracts);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_pager);

        mViewPager = findViewById(R.id.contract_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return null;
            }

            @Override
            public int getCount() {
                return 0;
            }
        });

    }
}
