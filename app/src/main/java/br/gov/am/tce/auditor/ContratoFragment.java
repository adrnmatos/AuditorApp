package br.gov.am.tce.auditor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import br.gov.am.tce.auditor.control.ContextHandler;
import br.gov.am.tce.auditor.model.Contrato;
import br.gov.am.tce.auditor.model.Medicao;

/**
 * Created by Adriano on 22/03/2018.
 */

public class ContratoFragment extends Fragment {
    private static final String ARG_CONTRATO = "contract_arg";

    private Contrato mContrato = null;
    private Medicao selectedMedicao;

    public static Fragment newInstance(Contrato contract) {
        Fragment fragment = new ContratoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_CONTRATO, contract);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mContrato = bundle.getParcelable(ARG_CONTRATO);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contract, container, false);

        TextView ctId_tv = v.findViewById(R.id.CTId_TV);
        ctId_tv.setText(mContrato.getId());

        TextView ctNumero_tv = v.findViewById(R.id.CTNumero_TV);
        ctNumero_tv.setText(mContrato.getNumero());

        TextView ctPrazo_tv = v.findViewById(R.id.CTPrazo_TV);
        ctPrazo_tv.setText(mContrato.getPrazo());

        TextView ctDataInicio_tv = v.findViewById(R.id.CTDataInicio_TV);
        ctDataInicio_tv.setText(mContrato.getDataInicio());

        TextView ctBemPublico_tv = v.findViewById(R.id.CTBemPublico_TV);
        SpannableString ss = new SpannableString(mContrato.getBemPublico());
        ss.setSpan(new UnderlineSpan(), 0, mContrato.getBemPublico().length(), 0);
        ctBemPublico_tv.setText(ss);
        ctBemPublico_tv.setTextColor(getResources().getColor(R.color.colorAccent));
        ctBemPublico_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextHandler.get().fetchBPInit(getActivity(), mContrato.getBemPublico());
            }
        });

        TextView ctContratado_tv = v.findViewById(R.id.CTContratado_TV);
        ctContratado_tv.setText(mContrato.getContratado());

        Spinner ctMedicoes_spn = v.findViewById(R.id.CTMedicoes_SPN);
        ArrayAdapter<Medicao> medicoesAdapter = new ArrayAdapter<Medicao>(getActivity(), android.R.layout.simple_spinner_item, mContrato.getMedicaoLista());
        medicoesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ctMedicoes_spn.setAdapter(medicoesAdapter);
        ctMedicoes_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMedicao = (Medicao) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        v.findViewById(R.id.CTFetchMedicoes_BTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextHandler.get().fetchMDInit(getActivity(), mContrato.getBemPublico(),
                        mContrato.getId(), selectedMedicao.getId());
            }
        });

        return v;
    }

}