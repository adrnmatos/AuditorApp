package br.gov.am.tce.auditor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import br.gov.am.tce.auditor.domain.Contrato;

/**
 * Created by Adriano on 04/04/2018.
 */

public class ContratoActivity extends SingleFragmentActivity {
    private static final String EXTRA_CONTRACT = "br.gov.am.tce.auditor.contractActivity";

    public static Intent newIntent(Context context, Contrato contrato) {
        Intent intent = new Intent(context, ContratoActivity.class);
        intent.putExtra(EXTRA_CONTRACT, contrato);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Contrato contract = getIntent().getParcelableExtra(EXTRA_CONTRACT);
        return new ContratoFragment().newInstance(contract);
    }

}
