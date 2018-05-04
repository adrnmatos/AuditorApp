package br.gov.am.tce.auditor;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.database.PhotoDbSchema;
import br.gov.am.tce.auditor.domain.Photo;
import br.gov.am.tce.auditor.helpers.PhotoLab;

public class FilterActivity extends AppCompatActivity {
    private static final String EXTRA_PHOTO_LIST = "br.gov.am.tce.auditor.extra_photo_list";

    private String selectedAuthor = "";
    private String bemPublicoSelected = "";
    private String contractSelected = "";
    private String medicaoSelected = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        List<String> distinctBensPublicos = PhotoLab.get(this).selectDistinctAtributeValues(PhotoDbSchema.PhotoTable.Cols.BEMPUBLICO);
        Spinner bemPublicoSpinner = findViewById(R.id.filterBemPublico_SPN);
        ArrayAdapter bemPublicoAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, distinctBensPublicos);
        bemPublicoSpinner.setAdapter(bemPublicoAdapter);
        bemPublicoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bemPublicoSelected = (String)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> distinctContracts = PhotoLab.get(this).selectDistinctAtributeValues(PhotoDbSchema.PhotoTable.Cols.CONTRATO);
        Spinner contractSpinner = findViewById(R.id.filterContrato_SPN);
        ArrayAdapter contractAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, distinctContracts);
        contractSpinner.setAdapter(contractAdapter);
        contractSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                contractSelected = (String)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> distinctMedicoes = PhotoLab.get(this).selectDistinctAtributeValues(PhotoDbSchema.PhotoTable.Cols.MEDICAO);
        Spinner medicaoSpinner = findViewById(R.id.filterMedicao_SPN);
        ArrayAdapter medicaoAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, distinctMedicoes);
        medicaoSpinner.setAdapter(medicaoAdapter);
        medicaoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                medicaoSelected = (String)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void filterPhotos(View v) {
        List<Photo> photos = PhotoLab.get(this).searchPhotos(bemPublicoSelected, contractSelected, medicaoSelected);
        Intent data = new Intent();
        data.putParcelableArrayListExtra(EXTRA_PHOTO_LIST, (ArrayList<? extends Parcelable>)photos);
        setResult(RESULT_OK, data);
        finish();
    }
}
