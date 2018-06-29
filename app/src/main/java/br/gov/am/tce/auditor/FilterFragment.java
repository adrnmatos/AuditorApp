package br.gov.am.tce.auditor;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.database.PhotoDbSchema;
import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.PhotoLab;

public class FilterFragment extends DialogFragment {
    public static final String EXTRA_ARGS = "br.gov.am.tce.auditor.extra_dialog_filter_args";

    private String selectedAuthor = "";
    private String selectedBemPublico = "";
    private String selectedContract = "";
    private String selectedMedicao = "";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.activity_filter, null);

        List<String> distinctBensPublicos = PhotoLab.get(getActivity()).selectDistinctAtributeValues(PhotoDbSchema.PhotoTable.Cols.BEMPUBLICO);
        Spinner bemPublicoSpinner = v.findViewById(R.id.filterBemPublico_SPN);
        ArrayAdapter bemPublicoAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, distinctBensPublicos);
        bemPublicoSpinner.setAdapter(bemPublicoAdapter);
        bemPublicoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBemPublico = (String)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> distinctContracts = PhotoLab.get(getActivity()).selectDistinctAtributeValues(PhotoDbSchema.PhotoTable.Cols.CONTRATO);
        Spinner contractSpinner = v.findViewById(R.id.filterContrato_SPN);
        ArrayAdapter contractAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, distinctContracts);
        contractSpinner.setAdapter(contractAdapter);
        contractSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedContract = (String)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> distinctMedicoes = PhotoLab.get(getActivity()).selectDistinctAtributeValues(PhotoDbSchema.PhotoTable.Cols.MEDICAO);
        Spinner medicaoSpinner = v.findViewById(R.id.filterMedicao_SPN);
        ArrayAdapter medicaoAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, distinctMedicoes);
        medicaoSpinner.setAdapter(medicaoAdapter);
        medicaoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMedicao = (String)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.filter)
                .setView(v)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK);
                    }
                });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }

    private void sendResult(int resultCode) {
        if(getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        List<String> selectedArgList = new ArrayList<>();
        selectedArgList.add(selectedAuthor);
        selectedArgList.add(selectedBemPublico);
        selectedArgList.add(selectedContract);
        selectedArgList.add(selectedMedicao);

        intent.putStringArrayListExtra(EXTRA_ARGS, (ArrayList<String>) selectedArgList);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

/*
    public void filterPhotos(View v) {
        List<Photo> photos = PhotoLab.get(this).searchPhotos(bemPublicoSelected, contractSelected, medicaoSelected);
        Intent data = new Intent();
        data.putParcelableArrayListExtra(EXTRA_PHOTO_LIST, (ArrayList<? extends Parcelable>)photos);
        setResult(RESULT_OK, data);
        finish();
    }
*/
}
