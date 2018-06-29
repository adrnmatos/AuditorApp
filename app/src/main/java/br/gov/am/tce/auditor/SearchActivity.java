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

import br.gov.am.tce.auditor.control.FindContextHandler;

/**
 * Created by Adriano on 23/04/2018.
 */

public class SearchActivity extends AppCompatActivity {
    private static final String EXTRA_IS_EDITING = "br.gov.am.tce.auditor.isEditing";

    private boolean isEditing;

    private String municipioStr;
    private String jurisdicionadoStr;

    public static Intent newIntent(Context packageContext, boolean isEditing) {
        Intent intent = new Intent(packageContext, SearchActivity.class);
        intent.putExtra(EXTRA_IS_EDITING, isEditing);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bempublico_search);

        isEditing = getIntent().getBooleanExtra(EXTRA_IS_EDITING, false);
        if(!isEditing) {
            findViewById(R.id.fab).setVisibility(View.VISIBLE);
        }

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
                FindContextHandler.get().initBPsFetch(SearchActivity.this, municipioStr, jurisdicionadoStr);
            }
        });
    }

    public void onNewPhotoFABClick(View v) {
        FindContextHandler.get().onFABClick(this);
        finish();
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
