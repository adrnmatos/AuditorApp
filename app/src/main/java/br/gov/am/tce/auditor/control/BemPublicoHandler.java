package br.gov.am.tce.auditor.control;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import br.gov.am.tce.auditor.ContextPagerActivity;
import br.gov.am.tce.auditor.DAO.ImageDBHandler;
import br.gov.am.tce.auditor.model.ContextObject;
import br.gov.am.tce.auditor.service.EContasFetcher;

public class BemPublicoHandler {
    private ContextPagerActivity mActivity;

    public BemPublicoHandler(ContextPagerActivity activity) {
        mActivity = activity;
    }

    public boolean isTherePhotosInContext() {
        return new ImageDBHandler(mActivity).isTherePhotosInContext(mActivity.getContextHandler().getBP());
    }

    public void getContratos() {
        new CTFetchTask(this).execute(mActivity.getContextHandler().getBP());
    }

    private void navigate2CT(List<ContextObject> contractList) {
        mActivity.getContextHandler().setContextObjectList(contractList);
        mActivity.updateUI();
    }

    private static class CTFetchTask extends AsyncTask<String, Void, List<ContextObject>> {
        private WeakReference<BemPublicoHandler> outerContext;
        private CTFetchTask(BemPublicoHandler context) {
            this.outerContext = new WeakReference<>(context);
        }

        @Override
        protected List<ContextObject> doInBackground(String... strings) {
            String bpID = strings[0];
            return new EContasFetcher().fetchContratoCollection(bpID);
        }

        @Override
        protected void onPostExecute(List<ContextObject> contractList) {
            super.onPostExecute(contractList);

            BemPublicoHandler handler = outerContext.get();
            if(handler != null) {
                handler.navigate2CT(contractList);
            }
        }

    }
}
