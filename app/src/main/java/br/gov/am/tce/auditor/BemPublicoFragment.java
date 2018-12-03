package br.gov.am.tce.auditor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.gov.am.tce.auditor.control.BemPublicoHandler;
import br.gov.am.tce.auditor.model.BemPublico;
import br.gov.am.tce.auditor.model.Contrato;

/**
 * Created by Adriano on 28/03/2018.
 */

public class BemPublicoFragment extends Fragment implements View.OnClickListener {
    private static final String BEM_PUBLICO_ARG = "BEM_PUBLICO_ARG";
    private BemPublico mBemPublico;
    private BemPublicoHandler mBemPublicoHandler;

    public static Fragment newInstance(BemPublico bemPublico) {
        Fragment fragment = new BemPublicoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BEM_PUBLICO_ARG, bemPublico);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBemPublico = getArguments().getParcelable(BEM_PUBLICO_ARG);
        mBemPublicoHandler = new BemPublicoHandler((ContextPagerActivity) getActivity());
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

        v.findViewById(R.id.BPFetchContrato_BTN).setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        mBemPublicoHandler.getContratos();
    }

}
