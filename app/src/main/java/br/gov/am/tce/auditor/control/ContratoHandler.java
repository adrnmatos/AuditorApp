package br.gov.am.tce.auditor.control;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import br.gov.am.tce.auditor.ContextPagerActivity;
import br.gov.am.tce.auditor.model.ContextObject;
import br.gov.am.tce.auditor.service.EContasFetcher;

public class ContratoHandler {
    private ContextPagerActivity mActivity;

    public ContratoHandler(ContextPagerActivity activity) {
        mActivity = activity;
    }

    public void getBemPublico(String bemPublicoId) {
        new BPFetchTask(this).execute(bemPublicoId);
    }

    private void navigate2BP(List<ContextObject> bemPublicoList) {
        mActivity.getContextHandler().navigateUP("bemPublico");
        mActivity.getContextHandler().setContextObjectList(bemPublicoList);
        mActivity.updateUI();
    }

    public void getMedicao() {
        new MDFetchTask(this).execute(mActivity.getContextHandler().getBP(), mActivity.getContextHandler().getCT());
    }

    private void navigate2MD(List<ContextObject> medicaoList) {
        mActivity.getContextHandler().setContextObjectList(medicaoList);
        mActivity.updateUI();
    }

    private static class BPFetchTask extends AsyncTask<String, Void, List<ContextObject>> {
        private WeakReference<ContratoHandler> outerContext;
        public BPFetchTask(ContratoHandler context) {
            this.outerContext = new WeakReference<>(context);
        }

        @Override
        protected List<ContextObject> doInBackground(String... strings) {
            String bpID = strings[0];
            return new EContasFetcher().fetchBemPublico(bpID);
        }

        @Override
        protected void onPostExecute(List<ContextObject> bemPublicoList) {
            super.onPostExecute(bemPublicoList);

            ContratoHandler handler = outerContext.get();
            if(handler != null) {
                handler.navigate2BP(bemPublicoList);
            }
        }
    }

    private static class MDFetchTask extends AsyncTask<String, Void, List<ContextObject>> {
        private WeakReference<ContratoHandler> outerContext;
        private MDFetchTask(ContratoHandler context) {
            this.outerContext = new WeakReference<>(context);
        }

        @Override
        protected List<ContextObject> doInBackground(String... strings) {
            String bpID = strings[0];
            String ctID = strings[1];
            return new EContasFetcher().fetchMedicao(bpID, ctID);
        }

        @Override
        protected void onPostExecute(List<ContextObject> medicaoList) {
            super.onPostExecute(medicaoList);

            ContratoHandler handler = outerContext.get();
            if(handler != null) {
                handler.navigate2MD(medicaoList);
            }
        }
    }

}
