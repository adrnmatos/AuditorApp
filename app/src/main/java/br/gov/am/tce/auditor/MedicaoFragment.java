package br.gov.am.tce.auditor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.gov.am.tce.auditor.control.FindContextHandler;
import br.gov.am.tce.auditor.model.Medicao;

public class MedicaoFragment extends Fragment {
    private static final String ARG_MEDICAO = "medicao_arg";
    private static final String ARG_IS_EDITING = "isEditing_arg";

    private Medicao mMedicao;
    private boolean isEditing;

    public static Fragment newInstance(Medicao medicao, boolean isEditing) {
        Fragment fragment = new MedicaoFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_MEDICAO, medicao);
        arguments.putBoolean(ARG_IS_EDITING, isEditing);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMedicao = getArguments().getParcelable(ARG_MEDICAO);
        isEditing = getArguments().getBoolean(ARG_IS_EDITING, false);
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
                FindContextHandler.get().initCTFetchFromMD(getActivity(), mMedicao.getContratoId());
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

        return v;
    }
}
