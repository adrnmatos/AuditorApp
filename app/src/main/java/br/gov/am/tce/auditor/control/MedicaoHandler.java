package br.gov.am.tce.auditor.control;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import br.gov.am.tce.auditor.ContextPagerActivity;
import br.gov.am.tce.auditor.model.ContextObject;
import br.gov.am.tce.auditor.service.EContasFetcher;

public class MedicaoHandler {
    private ContextPagerActivity mActivity;

    public MedicaoHandler(ContextPagerActivity activity) {
        mActivity = activity;
    }

    public void getContrato() {
        new CTFetchTask(this).execute(mActivity.getContextHandler().getBP(), mActivity.getContextHandler().getCT());
    }

    private void navigate2CT(List<ContextObject> contratoList) {
        mActivity.getContextHandler().navigateUP("contrato");
        mActivity.getContextHandler().setContextObjectList(contratoList);
        mActivity.updateUI();
    }

    private static class CTFetchTask extends AsyncTask<String, Void, List<ContextObject>> {
        private WeakReference<MedicaoHandler> outerContext;
        private CTFetchTask(MedicaoHandler context) {
            outerContext = new WeakReference<>(context);
        }

        @Override
        protected List<ContextObject> doInBackground(String... strings) {
            String bpID = strings[0];
            String ctID = strings[1];
            return new EContasFetcher().fetchContrato(bpID, ctID);
        }

        @Override
        protected void onPostExecute(List<ContextObject> contratoList) {
            super.onPostExecute(contratoList);

            MedicaoHandler handler = outerContext.get();
            if(handler != null) {
                handler.navigate2CT(contratoList);
            }
        }
    }

}
