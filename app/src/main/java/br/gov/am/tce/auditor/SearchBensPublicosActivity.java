package br.gov.am.tce.auditor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

import br.gov.am.tce.auditor.domain.BemPublico;
import br.gov.am.tce.auditor.helpers.EContasFetchr;

/**
 * Created by Adriano on 23/04/2018.
 */

public class SearchBensPublicosActivity extends AppCompatActivity {
    private static final String TAG = "SearchBensPublicosActivity";

    private String municipioStr;
    private String jurisdicionadoStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benspublicos_search);

        Spinner municipioSpinner = findViewById(R.id.searchBemPublicoMunicipio_SPN);
        ArrayAdapter<CharSequence> municipioAdapter = ArrayAdapter.createFromResource(this,
                R.array.municipios_array, android.R.layout.simple_spinner_item);
        municipioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        municipioSpinner.setAdapter(municipioAdapter);
        municipioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                municipioStr = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // nothing here
            }
        });

        Spinner jurisdicionadoSpinner = findViewById(R.id.searchBemPublicoJurisdicionado_SPN);
        ArrayAdapter<CharSequence> jurisdicionadoAdapter = ArrayAdapter.createFromResource(this,
                R.array.municipios_array, android.R.layout.simple_spinner_item);
        jurisdicionadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jurisdicionadoSpinner.setAdapter(jurisdicionadoAdapter);
        jurisdicionadoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jurisdicionadoStr = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // nothing here
            }
        });

        Button buscar_btn = findViewById(R.id.searchBemPublico_BTN);
        buscar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchItemsTask().execute(municipioStr, jurisdicionadoStr);
            }
        });

    }

    private class FetchItemsTask extends AsyncTask<String, Void, List<BemPublico>> {

        @Override
        protected List<BemPublico> doInBackground(String... strings) {
            String municipio = strings[0];
            String jurisdicionado = strings[1];
            return new EContasFetchr().fetchBensPublicos(municipio, jurisdicionado);
        }

        @Override
        protected void onPostExecute(List<BemPublico> bensPublicos) {
            Intent intent = BemPublicoPagerActivity.newIntent(SearchBensPublicosActivity.this, bensPublicos);
            startActivity(intent);
        }
    }
}
