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
import br.gov.am.tce.auditor.model.Medicao;

public class MedicaoPagerActivity extends AppCompatActivity {
    public static final String EXTRA_MEDICAO_LIST = "br.gov.am.tce.auditor.medicao";
    public static final String EXTRA_IS_EDITING = "br.gov.am.tce.auditor.isEditing";

    List<Medicao> mMedicaoList = new ArrayList<>();
    boolean isEditing;

    public static Intent newIntent(Context context, List<Medicao> medicaoList, boolean isEditing) {
        Intent intent = new Intent(context, MedicaoPagerActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_MEDICAO_LIST, (ArrayList<? extends Parcelable>) medicaoList);
        intent.putExtra(EXTRA_IS_EDITING, isEditing);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        mMedicaoList = getIntent().getParcelableArrayListExtra(EXTRA_MEDICAO_LIST);
        isEditing = getIntent().getBooleanExtra(EXTRA_IS_EDITING, false);

        ViewPager pager = findViewById(R.id.view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        pager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Medicao medicao = mMedicaoList.get(position);
                return MedicaoFragment.newInstance(medicao, isEditing);
            }

            @Override
            public int getCount() {
                return mMedicaoList.size();
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
        switch (item.getItemId()) {
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
