package br.gov.am.tce.auditor;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import br.gov.am.tce.auditor.domain.BemPublico;
import br.gov.am.tce.auditor.domain.Contract;
import br.gov.am.tce.auditor.helpers.EContasFetchr;

/**
 * Created by Adriano on 22/03/2018.
 */

public class ContractFragment extends Fragment {
    private static final String TAG = "ContractFragment";
    private static final String ARG_CONTRACT = "contract_argument";
    private Contract contract = null;

    private TextView ctId_tv;
    private TextView ctNumero_tv;
    private TextView ctPrazo_tv;
    private TextView ctDataInicio_tv;
    private TextView ctBemPublico_tv;
    private TextView ctContratado_tv;

    public static Fragment newInstance(Contract contract) {
        Fragment fragment = new ContractFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_CONTRACT, contract);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        contract = (Contract) bundle.getParcelable(ARG_CONTRACT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_contract, container, false);

        ctId_tv = v.findViewById(R.id.CTId_TV);
        ctNumero_tv = v.findViewById(R.id.CTNumero_TV);
        ctPrazo_tv = v.findViewById(R.id.CTPrazo_TV);
        ctDataInicio_tv = v.findViewById(R.id.CTDataInicio_TV);
        ctBemPublico_tv = v.findViewById(R.id.CTBemPublico_TV);
        ctContratado_tv = v.findViewById(R.id.CTContratado_TV);

        ctId_tv.setText(contract.getId());
        ctNumero_tv.setText(contract.getNumero());
        ctPrazo_tv.setText(contract.getPrazo());
        ctDataInicio_tv.setText(contract.getDataInicio());
        ctContratado_tv.setText(contract.getContratado());

        SpannableString ss = new SpannableString(contract.getBemPublico());
        ss.setSpan(new UnderlineSpan(), 0, contract.getBemPublico().length(), 0);
        ctBemPublico_tv.setText(ss);
        ctBemPublico_tv.setTextColor(getResources().getColor(R.color.colorAccent));
        ctBemPublico_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchItemTask().execute(contract.getBemPublico());
            }
        });

        return v;
    }

    private class FetchItemTask extends AsyncTask<String, Void, BemPublico> {

        @Override
        protected BemPublico doInBackground(String... strings) {
            String bemPublico = strings[0];
            return new EContasFetchr().fetchBemPublico(bemPublico);
        }

        @Override
        protected void onPostExecute(BemPublico bemPublico) {
            Intent intent = BemPublicoActivity.newIntent(getActivity(), bemPublico);
            startActivity(intent);
        }
    }
}
