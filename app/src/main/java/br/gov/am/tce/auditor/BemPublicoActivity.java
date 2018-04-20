package br.gov.am.tce.auditor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import br.gov.am.tce.auditor.domain.BemPublico;
import br.gov.am.tce.auditor.domain.Contrato;
import br.gov.am.tce.auditor.domain.Photo;
import br.gov.am.tce.auditor.helpers.EContasFetchr;
import br.gov.am.tce.auditor.helpers.PhotoLab;

/**
 * Created by Adriano on 28/03/2018.
 */

public class BemPublicoActivity extends AppCompatActivity {
    private static final String TAG = "br.gov.am.tce.auditor.BemPublicoActivity";
    private static final String BEMPUBLICO_EXTRA = "bemPublico_extra";
    private BemPublico bemPublico;
    private Contrato selectedContract;

    public static Intent newIntent(Context context, BemPublico bemPublico) {
        Intent intent = new Intent(context, BemPublicoActivity.class);
        intent.putExtra(BEMPUBLICO_EXTRA, bemPublico);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bempublico);

        bemPublico = getIntent().getParcelableExtra(BEMPUBLICO_EXTRA);

        TextView bpId_tv = findViewById(R.id.BPId_TV);
        bpId_tv.setText(bemPublico.getId());

        TextView bpArea_tv = findViewById(R.id.BPArea_TV);
        bpArea_tv.setText(bemPublico.getArea());

        TextView bpLatitude_tv = findViewById(R.id.BPLatitude_TV);
        bpLatitude_tv.setText(bemPublico.getLatitude());

        TextView bpLongitude_tv = findViewById(R.id.BPLongitude_TV);
        bpLongitude_tv.setText(bemPublico.getLongitude());

        TextView bpTipo_tv = findViewById(R.id.BPTipo_TV);
        bpTipo_tv.setText(bemPublico.getTipo());

        TextView bpNome_tv = findViewById(R.id.BPNome_TV);
        bpNome_tv.setText(bemPublico.getNome());

        TextView bpJurisdicionado_tv = findViewById(R.id.BPJurisdicionado_TV);
        bpJurisdicionado_tv.setText(bemPublico.getJurisdicionado());

        TextView bpEndereco_tv = findViewById(R.id.BPEndereco_TV);
        bpEndereco_tv.setText(bemPublico.getEndereco());

        Spinner bpContratos_spn = findViewById(R.id.BPContratos_SPN);
        ArrayAdapter<Contrato> contratosAdapter = new ArrayAdapter<Contrato>(BemPublicoActivity.this, android.R.layout.simple_spinner_item, bemPublico.getContratos());
        contratosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bpContratos_spn.setAdapter(contratosAdapter);

        bpContratos_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedContract = (Contrato) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button bpFetchContrato_btn = findViewById(R.id.BPFetchContrato_BTN);
        bpFetchContrato_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchContratoTask().execute(selectedContract.getId());
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
                photo.setBemPublico(bemPublico.getId());
                PhotoLab.get(this).addPhoto(photo);
                Intent intent = PhotoActivity.newIntent(this, photo.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class FetchContratoTask extends AsyncTask<String, Void, Contrato> {
        @Override
        protected Contrato doInBackground(String... args) {
            String contractId = args[0];
            return new EContasFetchr().fetchContract(contractId);
        }

        @Override
        protected void onPostExecute(Contrato contrato) {
            Intent intent = ContratoActivity.newIntent(BemPublicoActivity.this, contrato);
            startActivity(intent);
        }
    }
}
