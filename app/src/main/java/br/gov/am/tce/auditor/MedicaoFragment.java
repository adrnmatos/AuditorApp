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

import br.gov.am.tce.auditor.control.ContextHandler;
import br.gov.am.tce.auditor.model.Medicao;

public class MedicaoFragment extends Fragment {
    private static final String MEDICAO_ARG = "medicao_arg";
    private Medicao mMedicao;

    public static Fragment newInstance(Medicao medicao) {
        Fragment fragment = new MedicaoFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(MEDICAO_ARG, medicao);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMedicao = getArguments().getParcelable(MEDICAO_ARG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_medicao, container, false);

        TextView mdId_tv = v.findViewById(R.id.MDId_TV);
        mdId_tv.setText(mMedicao.getId());

        TextView mdNumero_tv = v.findViewById(R.id.MDNumero_TV);
        mdNumero_tv.setText(mMedicao.getNumero());

        TextView mdDataInicio_tv = v.findViewById(R.id.MDDataInicio_TV);
        mdDataInicio_tv.setText(mMedicao.getDataInicio());;

        TextView mdDataFim_tv = v.findViewById(R.id.MDDataFim_TV);
        mdDataFim_tv.setText(mMedicao.getDataFim());

        TextView mdCTId_tv = v.findViewById(R.id.MDCTId_TV);
        SpannableString ss = new SpannableString(mMedicao.getContratoId());
        ss.setSpan(new UnderlineSpan(), 0, mMedicao.getContratoId().length(), 0);
        mdCTId_tv.setText(ss);
        mdCTId_tv.setTextColor(getResources().getColor(R.color.colorAccent));
        mdCTId_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextHandler.get().fetchCTFromMD(getActivity(), mMedicao.getContratoId());
            }
        });

        return v;
    }
}
