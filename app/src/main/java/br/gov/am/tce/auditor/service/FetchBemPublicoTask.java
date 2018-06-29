package br.gov.am.tce.auditor.service;

import android.os.AsyncTask;

import java.util.List;

import br.gov.am.tce.auditor.control.FindContextHandler;
import br.gov.am.tce.auditor.model.BemPublico;

public class FetchBemPublicoTask extends AsyncTask<String, Void, List<BemPublico>> {

    @Override
    protected List<BemPublico> doInBackground(String... strings) {
        int count = strings.length;
        if(count == 1) {
            String bpID = strings[0];
            return new EContasFetchr().fetchBemPublico(bpID);
        } else if(count == 2) {
            String municipio = strings[0];
            String jurisdicionado = strings[1];
            return new EContasFetchr().fetchBensPublicos(municipio, jurisdicionado);
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<BemPublico> bensPublicos) {
        FindContextHandler.get().completeBPFetch(bensPublicos);
    }
}
