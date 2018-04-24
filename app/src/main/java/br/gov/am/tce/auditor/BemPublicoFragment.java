package br.gov.am.tce.auditor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import br.gov.am.tce.auditor.domain.Photo;
import br.gov.am.tce.auditor.helpers.AuditorPreferences;
import br.gov.am.tce.auditor.helpers.EContasFetchr;
import br.gov.am.tce.auditor.helpers.PhotoLab;

/**
 * Created by Adriano on 28/03/2018.
 */

public class BemPublicoFragment extends Fragment {
    private static final String TAG = "br.gov.am.tce.auditor.BemPublicoFragment";
    private static final String BEMPUBLICO_ARG = "bemPublico_extra";

    private BemPublico bemPublico = null;
    private Contrato selectedContract;

    public static Fragment newInstance(BemPublico bemPublico) {
        Fragment fragment = new BemPublicoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BEMPUBLICO_ARG, bemPublico);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        setHasOptionsMenu(true);

        bemPublico = bundle.getParcelable(BEMPUBLICO_ARG);
        AuditorPreferences.setBemPublico(getActivity(), bemPublico.getId());
        AuditorPreferences.setContrato(getActivity(), null);
        AuditorPreferences.setMedicao(getActivity(), null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bempublico, container, false);

        TextView bpId_tv = v.findViewById(R.id.BPId_TV);
        bpId_tv.setText(bemPublico.getId());

        TextView bpArea_tv = v.findViewById(R.id.BPArea_TV);
        bpArea_tv.setText(bemPublico.getArea());

        TextView bpLatitude_tv = v.findViewById(R.id.BPLatitude_TV);
        bpLatitude_tv.setText(bemPublico.getLatitude());

        TextView bpLongitude_tv = v.findViewById(R.id.BPLongitude_TV);
        bpLongitude_tv.setText(bemPublico.getLongitude());

        TextView bpTipo_tv = v.findViewById(R.id.BPTipo_TV);
        bpTipo_tv.setText(bemPublico.getTipo());

        TextView bpNome_tv = v.findViewById(R.id.BPNome_TV);
        bpNome_tv.setText(bemPublico.getNome());

        TextView bpJurisdicionado_tv = v.findViewById(R.id.BPJurisdicionado_TV);
        bpJurisdicionado_tv.setText(bemPublico.getJurisdicionado());

        TextView bpEndereco_tv = v.findViewById(R.id.BPEndereco_TV);
        bpEndereco_tv.setText(bemPublico.getEndereco());

        Spinner bpContratos_spn = v.findViewById(R.id.BPContratos_SPN);
        ArrayAdapter<Contrato> contratosAdapter = new ArrayAdapter<Contrato>(getActivity(), android.R.layout.simple_spinner_item, bemPublico.getContratos());
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

        Button bpFetchContrato_btn = v.findViewById(R.id.BPFetchContrato_BTN);
        bpFetchContrato_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchContratoTask().execute(selectedContract.getId());
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
                photo.setBemPublico(bemPublico.getId());
                PhotoLab.get(getActivity()).addPhoto(photo);
                Intent intent = PhotoActivity.newIntent(getActivity(), photo.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class FetchContratoTask extends AsyncTask<String, Void, Contrato> {
        @Override
        protected Contrato doInBackground(String... args) {
            String contractId = args[0];
            return new EContasFetchr().fetchContrato(contractId);
        }

        @Override
        protected void onPostExecute(Contrato contrato) {
            Intent intent = ContratoActivity.newIntent(getActivity(), contrato);
            startActivity(intent);
        }
    }
}
