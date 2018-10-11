package br.gov.am.tce.auditor.service;

import android.os.AsyncTask;

import java.util.List;

import br.gov.am.tce.auditor.control.ContextHandler;
import br.gov.am.tce.auditor.model.Contrato;

public class FetchContratoTask extends AsyncTask<String, Void, List<Contrato>> {

    @Override
    protected List<Contrato> doInBackground(String... strings) {
        int count = strings.length;
        if(count == 2) {
            String bpID = strings[0];
            String ctID = strings[1];
            return new EContasFetchr().fetchContrato(bpID, ctID);
        } else if(count == 3) {
            String municipio = strings[0];
            String jurisdicionado = strings[1];
            String exercicio = strings[2];
            return new EContasFetchr().fetchContratos(municipio, jurisdicionado, exercicio);
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Contrato> contratoList) {
        ContextHandler.get().fetchCTComplete(contratoList);
    }
}
