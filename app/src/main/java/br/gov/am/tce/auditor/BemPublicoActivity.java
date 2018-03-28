package br.gov.am.tce.auditor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import br.gov.am.tce.auditor.domain.BemPublico;

/**
 * Created by Adriano on 28/03/2018.
 */

public class BemPublicoActivity extends AppCompatActivity {
    private static final String TAG = "br.gov.am.tce.auditor.BemPublicoActivity";
    private static final String BEMPUBLICO_EXTRA = "bemPublico_extra";

    public static Intent newIntent(Context context, BemPublico bemPublico) {
        Intent intent = new Intent(context, BemPublicoActivity.class);
        intent.putExtra(BEMPUBLICO_EXTRA, bemPublico);
        return intent;
    }
}
