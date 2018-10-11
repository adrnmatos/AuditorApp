package br.gov.am.tce.auditor.service;

import android.os.AsyncTask;

import java.util.List;

import br.gov.am.tce.auditor.control.ContextHandler;
import br.gov.am.tce.auditor.model.Medicao;

public class FetchMedicaoTask extends AsyncTask<String, Void, List<Medicao>> {

    @Override
    protected List<Medicao> doInBackground(String... strings) {
        int count = strings.length;
        if(count == 3) {
            String bpID = strings[0];
            String ctID = strings[1];
            String mdID = strings[2];
            return new EContasFetchr().fetchMedicao(bpID, ctID, mdID);
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Medicao> medicaoList) {
        ContextHandler.get().fetchMDComplete(medicaoList);
    }
}
