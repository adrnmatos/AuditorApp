package br.gov.am.tce.auditor.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.widget.Toast;

import java.util.List;

import br.gov.am.tce.auditor.BemPublicoPagerActivity;
import br.gov.am.tce.auditor.ContratoPagerActivity;
import br.gov.am.tce.auditor.MedicaoPagerActivity;
import br.gov.am.tce.auditor.SearchActivity;
import br.gov.am.tce.auditor.model.BemPublico;
import br.gov.am.tce.auditor.model.Contrato;
import br.gov.am.tce.auditor.model.Medicao;
import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.AuditorPreferences;
import br.gov.am.tce.auditor.service.FetchBemPublicoTask;
import br.gov.am.tce.auditor.service.FetchContratoTask;
import br.gov.am.tce.auditor.service.FetchMedicaoTask;
import br.gov.am.tce.auditor.service.PhotoLab;

public class ContextHandler{
    private static ContextHandler sContextHandler;
    private Activity mContext;
    private List<Photo> mPhotos;
    private String BPid = "";
    private String CTid = "";
    private String MDid = "";

    public static ContextHandler get() {
        if(sContextHandler == null) {
            sContextHandler = new ContextHandler();
        }
        return sContextHandler;
    }

    private ContextHandler() {

    }

    public void applyContextSinglePhoto(Activity initialContext, List<Photo> photos) throws Exception{
        mContext = initialContext;
        mPhotos = photos;
        if(mPhotos.size() != 1) {
            throw new Exception();
        }
        BPid = CTid = MDid = "";
        startPhotoContextDiscovery();
    }

    public void applyContextSomePhotos(Activity initialContext, List<Photo> photos) {
        mContext = initialContext;
        mPhotos = photos;
        BPid = CTid = MDid = "";
        startContextDiscoveryEmpty();
    }

    private void startPhotoContextDiscovery() {
        Photo photo = mPhotos.get(0);

        if(photo.getBemPublico().equals("")) {
            startContextDiscoveryEmpty();
        } else if(photo.getContrato().equals("")) {
            this.BPid = photo.getBemPublico();
            fetchBPInit(mContext, this.BPid);
        } else if(photo.getMedicao().equals("")) {
            this.BPid = photo.getBemPublico();
            this.CTid = photo.getContrato();
            fetchCTInit(mContext, this.BPid, this.CTid);
        } else {
            this.BPid = photo.getBemPublico();
            this.CTid = photo.getContrato();
            this.MDid = photo.getMedicao();
            fetchMDInit(mContext, this.BPid, this.CTid, this.MDid);
        }
    }

    /* **************** BEM PUBLICO ***************/
    private void startContextDiscoveryEmpty() {
        Intent intent = SearchActivity.newIntent(mContext);
        mContext.startActivity(intent);
    }

    public void fetchBPsInit(Activity current, String municipio, String jurisdicionado) {
        this.mContext = current;
        new FetchBemPublicoTask().execute(municipio, jurisdicionado);
    }

    public void fetchBPComplete(List<BemPublico> bemPublicoList) {
        Intent intent = BemPublicoPagerActivity.newIntent(mContext, bemPublicoList);
        mContext.startActivity(intent);
        mContext.finish();
    }

    public void bpPagerSwipe(String bpID) {
        this.BPid = bpID;
    }

    public void fetchBPInit(Activity current, String bpID) {
        this.mContext = current;
        this.BPid = bpID;
        this.CTid = "";
        this.MDid = "";

        new FetchBemPublicoTask().execute(bpID);
    }

    /* **************** CONTRATO ***********************************/
    public void fetchCTInit(Activity current, String bpID, String ctID) {
        this.mContext = current;
        this.BPid = bpID;
        this.CTid = ctID;
        this.MDid = "";

        new FetchContratoTask().execute(bpID, ctID);
    }

    public void fetchCTComplete(List<Contrato> contratoList) {
        Intent intent = ContratoPagerActivity.newIntent(mContext, contratoList);
        mContext.startActivity(intent);
        mContext.finish();
    }

    public void ctPagerSwipe(String ctID) {
        this.CTid = ctID;
    }

    public void fetchCTFromMD(Activity current, String ctID) {
        fetchCTInit(current, this.BPid, ctID);
    }

    /* ******************** MEDICAO *********************/
    public void fetchMDInit(Activity current, String bpID, String ctID, String mdID) {
        this.mContext = current;
        this.BPid = bpID;
        this.CTid = ctID;
        this.MDid = mdID;

        new FetchMedicaoTask().execute(bpID, ctID, mdID);
    }

    public void fetchMDComplete(List<Medicao> medicaoList) {
        Intent intent = MedicaoPagerActivity.newIntent(mContext, medicaoList);
        mContext.startActivity(intent);
        mContext.finish();
    }

    public void mdPagerSwipe(String mdID) {
        this.MDid = mdID;
    }

    /* ***************** FINNISH *************************/
    public void onDone(Activity current) {
        for(Photo photo: mPhotos) {
            if(photo.getAuthor().equals(AuditorPreferences.getUsername(mContext))) {
                photo.setBemPublico(this.BPid);
                photo.setContrato(this.CTid);
                photo.setMedicao(this.MDid);
                PhotoLab.get(mContext).updatePhoto(photo);
            } else {
                Toast.makeText(mContext, "you cannot edit other authors photo ContextHandler", Toast.LENGTH_LONG).show();
            }
        }
        current.finish();
    }

    public void onCancel(Activity current) {
        current.finish();
    }

}
