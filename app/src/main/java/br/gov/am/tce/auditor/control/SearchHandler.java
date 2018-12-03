package br.gov.am.tce.auditor.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.model.ContextObject;
import br.gov.am.tce.auditor.service.EContasFetcher;

public class SearchHandler {
    private static final String SEARCH_RETURN_CODE = "br.gov.am.tce.auditor.search_activity_return_code";
    private Context mContext;


    public SearchHandler(Context context) {
        mContext = context;
    }

    public void search(String municipio, String jurisdicionado) {
        new BPFetchTask(this).execute(municipio, jurisdicionado);
    }

    private void navigate(List<ContextObject> bemPublicoList) {
        Intent returnData = new Intent();
        returnData.putParcelableArrayListExtra(SEARCH_RETURN_CODE, (ArrayList<? extends Parcelable>)bemPublicoList);
        ((Activity)mContext).setResult(Activity.RESULT_OK, returnData);
        ((Activity)mContext).finish();
    }

    private static class BPFetchTask extends AsyncTask<String, Void, List<ContextObject>> {
        private WeakReference<SearchHandler> outerContext;
        private BPFetchTask(SearchHandler context) {
            this.outerContext = new WeakReference<>(context);
        }

        @Override
        protected List<ContextObject> doInBackground(String... strings) {
            String municipio = strings[0];
            String jurisdicionado = strings[1];
            return new EContasFetcher().fetchBemPublicoCollection(municipio, jurisdicionado);
        }

        @Override
        protected void onPostExecute(List<ContextObject> bemPublicoList) {
            super.onPostExecute(bemPublicoList);

            SearchHandler handler = outerContext.get();
            if(handler != null) {
                handler.navigate(bemPublicoList);
            }
        }

    }

}
