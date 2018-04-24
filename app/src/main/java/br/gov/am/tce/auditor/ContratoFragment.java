package br.gov.am.tce.auditor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import br.gov.am.tce.auditor.domain.BemPublico;
import br.gov.am.tce.auditor.domain.Contrato;
import br.gov.am.tce.auditor.domain.Medicao;
import br.gov.am.tce.auditor.domain.Photo;
import br.gov.am.tce.auditor.helpers.AuditorPreferences;
import br.gov.am.tce.auditor.helpers.EContasFetchr;
import br.gov.am.tce.auditor.helpers.PhotoLab;

/**
 * Created by Adriano on 22/03/2018.
 */

public class ContratoFragment extends Fragment {
    private static final String TAG = "ContratoFragment";
    private static final String CONTRATO_ARG = "contract_argument";
    private Contrato contrato = null;
    private Medicao selectedMedicao;

    public static Fragment newInstance(Contrato contract) {
        Fragment fragment = new ContratoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CONTRATO_ARG, contract);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        setHasOptionsMenu(true);

        contrato = bundle.getParcelable(CONTRATO_ARG);
        AuditorPreferences.setContrato(getActivity(), contrato.getNumero());
        AuditorPreferences.setMedicao(getActivity(), null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contract, container, false);

        TextView ctId_tv = v.findViewById(R.id.CTId_TV);
        ctId_tv.setText(contrato.getId());

        TextView ctNumero_tv = v.findViewById(R.id.CTNumero_TV);
        ctNumero_tv.setText(contrato.getNumero());

        TextView ctPrazo_tv = v.findViewById(R.id.CTPrazo_TV);
        ctPrazo_tv.setText(contrato.getPrazo());

        TextView ctDataInicio_tv = v.findViewById(R.id.CTDataInicio_TV);
        ctDataInicio_tv.setText(contrato.getDataInicio());

        TextView ctBemPublico_tv = v.findViewById(R.id.CTBemPublico_TV);
        SpannableString ss = new SpannableString(contrato.getBemPublico());
        ss.setSpan(new UnderlineSpan(), 0, contrato.getBemPublico().length(), 0);
        ctBemPublico_tv.setText(ss);
        ctBemPublico_tv.setTextColor(getResources().getColor(R.color.colorAccent));
        ctBemPublico_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchBemPublicoTask().execute(contrato.getBemPublico());
            }
        });

        TextView ctContratado_tv = v.findViewById(R.id.CTContratado_TV);
        ctContratado_tv.setText(contrato.getContratado());

        Spinner ctMedicoes_spn = v.findViewById(R.id.CTMedicoes_SPN);
        ArrayAdapter<Medicao> medicoesAdapter = new ArrayAdapter<Medicao>(getActivity(), android.R.layout.simple_spinner_item, contrato.getMedicaoLista());
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

        Button ctFetchMedicoes_btn = v.findViewById(R.id.CTFetchMedicoes_BTN);
        ctFetchMedicoes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchMedicaoTask().execute(selectedMedicao.getId());
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.photo_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_photo:
                Photo photo = new Photo();
                photo.setBemPublico(contrato.getBemPublico());
                photo.setContrato(contrato.getId());
                PhotoLab.get(getActivity()).addPhoto(photo);
                Intent intent = PhotoActivity.newIntent(getActivity(), photo.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class FetchBemPublicoTask extends AsyncTask<String, Void, BemPublico> {
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

    private class FetchMedicaoTask extends AsyncTask<String, Void, Medicao> {
        @Override
        protected Medicao doInBackground(String... strings) {
            String medicao = strings[0];
            return new EContasFetchr().fetchMedicao(medicao);
        }

        @Override
        protected void onPostExecute(Medicao medicao) {
            Intent intent = MedicaoActivity.newIntent(getActivity(), medicao);
            startActivity(intent);
        }
    }

}
