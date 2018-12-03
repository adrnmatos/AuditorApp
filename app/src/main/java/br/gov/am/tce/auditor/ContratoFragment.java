package br.gov.am.tce.auditor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.gov.am.tce.auditor.control.ContratoHandler;
import br.gov.am.tce.auditor.model.Contrato;

/**
 * Created by Adriano on 22/03/2018.
 */

public class ContratoFragment extends Fragment implements View.OnClickListener {
    private static final String CONTRATO_ARG = "CONTRATO_ARG";
    private Contrato mContrato;
    private ContratoHandler mContratoHandler;

    public static Fragment newInstance(Contrato contrato) {
        Fragment fragment = new ContratoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CONTRATO_ARG, contrato);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContrato = getArguments().getParcelable(CONTRATO_ARG);
        mContratoHandler = new ContratoHandler((ContextPagerActivity) getActivity());
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
                mContratoHandler.getBemPublico(mContrato.getBemPublico());
            }
        });

        TextView ctContratado_tv = v.findViewById(R.id.CTContratado_TV);
        ctContratado_tv.setText(mContrato.getContratado());

        v.findViewById(R.id.CTFetchMedicoes_BTN).setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        mContratoHandler.getMedicao();
    }

}