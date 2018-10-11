package br.gov.am.tce.auditor;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.model.BemPublico;
import br.gov.am.tce.auditor.model.ContextObject;
import br.gov.am.tce.auditor.model.Contrato;
import br.gov.am.tce.auditor.model.Medicao;

public class ContextPagerActivity extends AppCompatActivity {
    private List<ContextObject> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        ViewPager viewPager = findViewById(R.id.view_pager);
        FragmentManager fm = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                ContextObject contextObject = mList.get(position);
                if(contextObject instanceof BemPublico) {
                    return BemPublicoFragment.newInstance((BemPublico) contextObject);
                } else if(contextObject instanceof Contrato) {
                    return ContratoFragment.newInstance((Contrato) contextObject);
                } else {
                    return MedicaoFragment.newInstance((Medicao) contextObject);
                }
            }

            @Override
            public int getCount() {
                return mList.size();
            }
        });
    }

    public void setContextObjectList(List<ContextObject> list) {
        mList = list;
    }
}
