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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.domain.Contract;
import br.gov.am.tce.auditor.helpers.EContasFetchr;

/**
 * Created by Adriano on 13/03/2018.
 */

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    private List<Contract> mContracts = new ArrayList<>();
    private Spinner yearSpinner;
    private Spinner countySpinner;
    private Spinner ownerSpinner;
    private String yearStr;
    private String countyStr;
    private String ownerStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        yearSpinner = findViewById(R.id.exercise_spinner);
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.exercicios_array, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yearStr = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // nothing to see here. move on...
            }
        });


        countySpinner = findViewById(R.id.municipio_spinner);
        ArrayAdapter<CharSequence> countyAdapter = ArrayAdapter.createFromResource(this,
                R.array.municipios_array, android.R.layout.simple_spinner_item);
        countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countySpinner.setAdapter(countyAdapter);
        countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                countyStr = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ownerSpinner = findViewById(R.id.jurisdicionado_spinner);
        ArrayAdapter<CharSequence> ownerAdapter = ArrayAdapter.createFromResource(this,
                R.array.municipios_array, android.R.layout.simple_spinner_item);
        ownerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ownerSpinner.setAdapter(ownerAdapter);
        ownerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ownerStr = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button buscar_btn = findViewById(R.id.buscar_btn);
        buscar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchItemsTask().execute(yearStr, countyStr, ownerStr);
                Toast.makeText(SearchActivity.this, countyStr + " " + yearStr, Toast.LENGTH_LONG)
                        .show();
            }
        });

    }

    private class FetchItemsTask extends AsyncTask<String, Void, List<Contract>> {
        @Override
        protected List<Contract> doInBackground(String... args) {
            String year = args[0];
            String county = args[1];
            String owner = args[2];

            return new EContasFetchr().fetchContracts(year, county, owner);
        }

        @Override
        protected void onPostExecute(List<Contract> contracts) {
            mContracts = contracts;
            Intent intent = new Intent();
            // create an intent to contractActivity and pass the list as parameter
            // create as adapter and set the list as parameter
            // like ViewPager
        }
    }
}
