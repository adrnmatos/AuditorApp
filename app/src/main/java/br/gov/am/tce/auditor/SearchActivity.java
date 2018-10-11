package br.gov.am.tce.auditor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import br.gov.am.tce.auditor.control.ContextHandler;

/**
 * Created by Adriano on 23/04/2018.
 */

public class SearchActivity extends AppCompatActivity {
    private String municipioStr;
    private String jurisdicionadoStr;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, SearchActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bempublico_search);

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
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextHandler.get().fetchBPsInit(SearchActivity.this, municipioStr, jurisdicionadoStr);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_done:
                ContextHandler.get().onDone(this);
                return false;
            case R.id.ic_cancel:
                ContextHandler.get().onCancel(this);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
