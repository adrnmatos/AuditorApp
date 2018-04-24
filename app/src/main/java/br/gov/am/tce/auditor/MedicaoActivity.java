package br.gov.am.tce.auditor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import br.gov.am.tce.auditor.domain.Contrato;
import br.gov.am.tce.auditor.domain.Medicao;
import br.gov.am.tce.auditor.domain.Photo;
import br.gov.am.tce.auditor.helpers.AuditorPreferences;
import br.gov.am.tce.auditor.helpers.EContasFetchr;
import br.gov.am.tce.auditor.helpers.PhotoLab;

/**
 * Created by Adriano on 05/04/2018.
 */

public class MedicaoActivity extends AppCompatActivity {
    private static final String TAG = "MedicaoActivity";
    private static final String EXTRA_MEDICAO = "br.gov.am.tce.auditor.medicaoActivity";
    private Medicao medicao = null;

    public static Intent newIntent(Context context, Medicao medicao) {
        Intent intent = new Intent(context, MedicaoActivity.class);
        intent.putExtra(EXTRA_MEDICAO, medicao);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicao);

        medicao = getIntent().getParcelableExtra(EXTRA_MEDICAO);

        AuditorPreferences.setMedicao(this, medicao.getNumero());

        TextView mdId_tv = findViewById(R.id.MDId_TV);
        mdId_tv.setText(medicao.getId());

        TextView mdNumero_tv = findViewById(R.id.MDNumero_TV);
        mdNumero_tv.setText(medicao.getNumero());

        TextView mdDataInicio_tv = findViewById(R.id.MDDataInicio_TV);
        mdDataInicio_tv.setText(medicao.getDataInicio());;

        TextView mdDataFim_tv = findViewById(R.id.MDDataFim_TV);
        mdDataFim_tv.setText(medicao.getDataFim());

        TextView mdCTId_tv = findViewById(R.id.MDCTId_TV);
        SpannableString ss = new SpannableString(medicao.getContratoId());
        ss.setSpan(new UnderlineSpan(), 0, medicao.getContratoId().length(), 0);
        mdCTId_tv.setText(ss);
        mdCTId_tv.setTextColor(getResources().getColor(R.color.colorAccent));
        mdCTId_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchContratoTask().execute(medicao.getContratoId());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.photo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_photo:
                Photo photo = new Photo();

                photo.setBemPublico(null);
                photo.setContrato(medicao.getContratoId());
                photo.setMedicao(medicao.getId());
                PhotoLab.get(this).addPhoto(photo);
                Intent intent = PhotoActivity.newIntent(this, photo.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class FetchContratoTask extends AsyncTask<String, Void, Contrato> {
        @Override
        protected Contrato doInBackground(String... strings) {
            String contractId = strings[0];
            return new EContasFetchr().fetchContrato(contractId);
        }

        @Override
        protected void onPostExecute(Contrato contract) {
            Intent intent = ContratoActivity.newIntent(MedicaoActivity.this, contract);
            startActivity(intent);
        }
    }
}
