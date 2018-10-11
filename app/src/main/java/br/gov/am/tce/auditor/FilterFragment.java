package br.gov.am.tce.auditor;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import br.gov.am.tce.auditor.database.PhotoDbSchema;
import br.gov.am.tce.auditor.service.PhotoLab;

public class FilterFragment extends DialogFragment {
    private String selectedBemPublico = "";
    private String selectedContract = "";
    private String selectedMedicao = "";

    public interface PhotoFilterDialogListener {
        void onPhotoFilterDialogPositiveClick(String bemPublico, String contrato, String medicao);
        void onPhotoFilterDialogNegativeClick(DialogFragment dialog);
    }
    PhotoFilterDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (PhotoFilterDialogListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + " muss implement PhotoFilterDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.activity_filter, null);

        List<String> distinctBemPublico = PhotoLab.get(getActivity()).selectDistinctAtributeValues(PhotoDbSchema.PhotoTable.Cols.BEMPUBLICO);
        Spinner bemPublicoSpinner = v.findViewById(R.id.filterBemPublico_SPN);
        ArrayAdapter bemPublicoAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, distinctBemPublico);
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
                        mListener.onPhotoFilterDialogPositiveClick(selectedBemPublico, selectedContract, selectedMedicao);
                    }
                });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onPhotoFilterDialogNegativeClick(FilterFragment.this);
            }
        });

        return builder.create();
    }

}
