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

import br.gov.am.tce.auditor.domain.Contrato;
import br.gov.am.tce.auditor.helpers.EContasFetchr;

/**
 * Created by Adriano on 13/03/2018.
 */

public class SearchContratosActivity extends AppCompatActivity {
    private static final String TAG = "SearchContratosActivity";

    private String municipioStr;
    private String jurisdicionadoStr;
    private String exercicioStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts_search);

        Spinner municipioSpinner = findViewById(R.id.searchContratoMunicipio_SPN);
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

        Spinner jurisdicionadoSpinner = findViewById(R.id.searchContratoJurisdicionado_SPN);
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

        Spinner exercicioSpinner = findViewById(R.id.searchContratoExercicio_SPN);
        ArrayAdapter<CharSequence> exercicioAdapter = ArrayAdapter.createFromResource(this,
                R.array.exercicios_array, android.R.layout.simple_spinner_item);
        exercicioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exercicioSpinner.setAdapter(exercicioAdapter);
        exercicioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                exercicioStr = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // nothing here
            }
        });

        Button buscar_btn = findViewById(R.id.searchContrato_BTN);
        buscar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchItemsTask().execute(municipioStr, jurisdicionadoStr, exercicioStr);
            }
        });

    }

    private class FetchItemsTask extends AsyncTask<String, Void, List<Contrato>> {

        @Override
        protected List<Contrato> doInBackground(String... args) {
            String municipio_str = args[0];
            String jurisdicionado_str = args[1];
            String exercicio_str = args[2];
            return new EContasFetchr().fetchContratos(municipio_str, jurisdicionado_str, exercicio_str);
        }

        @Override
        protected void onPostExecute(List<Contrato> contratos) {
            Intent intent = ContratoPagerActivity.newIntent(SearchContratosActivity.this, contratos);
            startActivity(intent);
        }
    }

}
