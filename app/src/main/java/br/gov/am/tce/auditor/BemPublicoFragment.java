package br.gov.am.tce.auditor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import br.gov.am.tce.auditor.control.FindContextHandler;
import br.gov.am.tce.auditor.model.BemPublico;
import br.gov.am.tce.auditor.model.Contrato;
import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.EContasFetchr;
import br.gov.am.tce.auditor.service.PhotoLab;

/**
 * Created by Adriano on 28/03/2018.
 */

public class BemPublicoFragment extends Fragment {
    private static final String ARG_BEM_PUBLICO = "bemPublico_arg";
    private static final String ARG_IS_EDITING = "isEditing_arg";

    private BemPublico mBemPublico = null;
    private boolean isEditing;
    private Contrato selectedContract;

    public static Fragment newInstance(BemPublico bemPublico, boolean isEditing) {
        Fragment fragment = new BemPublicoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_BEM_PUBLICO, bemPublico);
        bundle.putBoolean(ARG_IS_EDITING, isEditing);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mBemPublico = bundle.getParcelable(ARG_BEM_PUBLICO);
        isEditing = bundle.getBoolean(ARG_IS_EDITING);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bempublico, container, false);

        TextView bpId_tv = v.findViewById(R.id.BPId_TV);
        bpId_tv.setText(mBemPublico.getId());

        TextView bpArea_tv = v.findViewById(R.id.BPArea_TV);
        bpArea_tv.setText(mBemPublico.getArea());

        TextView bpLatitude_tv = v.findViewById(R.id.BPLatitude_TV);
        bpLatitude_tv.setText(mBemPublico.getLatitude());

        TextView bpLongitude_tv = v.findViewById(R.id.BPLongitude_TV);
        bpLongitude_tv.setText(mBemPublico.getLongitude());

        TextView bpTipo_tv = v.findViewById(R.id.BPTipo_TV);
        bpTipo_tv.setText(mBemPublico.getTipo());

        TextView bpNome_tv = v.findViewById(R.id.BPNome_TV);
        bpNome_tv.setText(mBemPublico.getNome());

        TextView bpJurisdicionado_tv = v.findViewById(R.id.BPJurisdicionado_TV);
        bpJurisdicionado_tv.setText(mBemPublico.getJurisdicionado());

        TextView bpEndereco_tv = v.findViewById(R.id.BPEndereco_TV);
        bpEndereco_tv.setText(mBemPublico.getEndereco());

        Spinner bpContratos_spn = v.findViewById(R.id.BPContratos_SPN);
        ArrayAdapter<Contrato> contratosAdapter = new ArrayAdapter<Contrato>(getActivity(), android.R.layout.simple_spinner_item, mBemPublico.getContratos());
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

        if(!isEditing) {
            FloatingActionButton fab = v.findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FindContextHandler.get().onFABClick(getActivity());
                }
            });
        }

        v.findViewById(R.id.BPFetchContrato_BTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindContextHandler.get().initCTFetch(getActivity(), selectedContract.getBemPublico(),
                        selectedContract.getId());
            }
        });

        return v;
    }

}
