package br.gov.am.tce.auditor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import br.gov.am.tce.auditor.domain.BemPublico;

/**
 * Created by Adriano on 24/04/2018.
 */

public class BemPublicoActivity extends SingleFragmentActivity {
    private static final String EXTRA_BEMPUBLICO = "br.gov.tce.auditor.bempublicoActivity";

    public static Intent newIntent(Context context, BemPublico bemPublico) {
        Intent intent = new Intent(context, BemPublicoActivity.class);
        intent.putExtra(EXTRA_BEMPUBLICO, bemPublico);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        BemPublico bemPublico = getIntent().getParcelableExtra(EXTRA_BEMPUBLICO);
        return BemPublicoFragment.newInstance(bemPublico);
    }
}
