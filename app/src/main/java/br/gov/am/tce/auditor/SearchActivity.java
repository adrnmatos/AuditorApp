package br.gov.am.tce.auditor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import br.gov.am.tce.auditor.control.SearchHandler;

/**
 * Created by Adriano on 23/04/2018.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private SearchHandler searchHandler;
    private String municipioStr;
    private String jurisdicionadoStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bempublico_search);
        searchHandler = new SearchHandler(this);

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

        Button search_btn = findViewById(R.id.searchBemPublico_BTN);
        search_btn.setOnClickListener(this);
    }

    public void onClick(View view) {
        searchHandler.search(municipioStr, jurisdicionadoStr);
    }

}
