package br.gov.am.tce.auditor.control;

import android.app.Activity;
import android.content.Intent;

import java.util.List;

import br.gov.am.tce.auditor.BemPublicoPagerActivity;
import br.gov.am.tce.auditor.ContratoPagerActivity;
import br.gov.am.tce.auditor.MedicaoPagerActivity;
import br.gov.am.tce.auditor.PhotoActivity;
import br.gov.am.tce.auditor.SearchActivity;
import br.gov.am.tce.auditor.service.AuditorPreferences;
import br.gov.am.tce.auditor.service.FetchBemPublicoTask;
import br.gov.am.tce.auditor.service.FetchContratoTask;
import br.gov.am.tce.auditor.service.FetchMedicaoTask;
import br.gov.am.tce.auditor.model.BemPublico;
import br.gov.am.tce.auditor.model.Contrato;
import br.gov.am.tce.auditor.model.Medicao;
import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.PhotoLab;

public class FindContextHandler {
    private static final String REQUEST_PHOTO_EDIT = "br.gov.am.tce.auditor.photoContextEditResult";

    private static FindContextHandler sFindContextHandler;
    private Photo mPhoto;
    private Activity current;
    private boolean isEditing;
    private String bpID = "";
    private String ctID = "";
    private String mdID = "";

    public static FindContextHandler get() {
        if(sFindContextHandler == null) {
            sFindContextHandler = new FindContextHandler();
        }
        return sFindContextHandler;
    }

    public void findContext(Activity origin, Photo photo) {
        this.current = origin;
        if(photo == null) {
            isEditing = false;
        } else {
            isEditing = true;
            this.mPhoto = photo;
            AuditorPreferences.setBemPublico(current, mPhoto.getBemPublico());
            AuditorPreferences.setContrato(current, mPhoto.getContrato());
            AuditorPreferences.setMedicao(current, mPhoto.getMedicao());
        }
        initCtxSelection();
    }

    private void initCtxSelection() {
        if(mPhoto == null || mPhoto.getBemPublico().equals("")) {
            Intent intent = SearchActivity.newIntent(current, isEditing);
            current.startActivity(intent);
            current.finish();
        } else if(mPhoto.getContrato().equals("")) {
            this.bpID = mPhoto.getBemPublico();
            initBPFetch(current, this.bpID);
        } else if(mPhoto.getMedicao().equals("")) {
            this.bpID = mPhoto.getBemPublico();
            this.ctID = mPhoto.getContrato();
            initCTFetch(current, this.bpID, this.ctID);
        } else {
            this.bpID = mPhoto.getBemPublico();
            this.ctID = mPhoto.getContrato();
            this.mdID = mPhoto.getMedicao();
            initMDFetch(current, this.bpID, this.ctID, this.mdID);
        }
    }

    // wanna BPs. called from SearchActivity
    public void initBPsFetch(Activity current, String municipio, String jurisdicionado) {
        this.current = current;
        new FetchBemPublicoTask().execute(municipio, jurisdicionado);
    }

    // wanna BPs. called from Contract fragment link press
    public void initBPFetch(Activity current, String bpID) {
        this.current = current;
        this.bpID = bpID;
        this.ctID = "";
        this.mdID = "";

        new FetchBemPublicoTask().execute(bpID);
    }

    // callback. build BP Pager
    public void completeBPFetch(List<BemPublico> bensPublicos) {
        Intent intent = BemPublicoPagerActivity.newIntent(current, bensPublicos, isEditing);
        current.startActivity(intent);
        current.finish();
    }

    public void swipeBP(String bpID) {
        this.bpID = bpID;
    }

    // wanna CTs. called from BPFragment or indirectly from MDFragment
    public void initCTFetch(Activity current, String bpID, String ctID) {
        this.current = current;
        this.bpID = bpID;
        this.ctID = ctID;
        this.mdID = "";

        new FetchContratoTask().execute(this.bpID, ctID);
    }

    // indirection to navigate to CT from MD. BPId should be saved
    public void initCTFetchFromMD(Activity current, String ctID) {
        initCTFetch(current, this.bpID, ctID);
    }

    // callback. build CT Pager
    public void completeCTFetch(List<Contrato> contratos) {
        Intent intent = ContratoPagerActivity.newIntent(current, contratos, isEditing);
        current.startActivity(intent);
        current.finish();
    }

    // wanna MDs. called from CTFragment
    public void initMDFetch(Activity current, String bpID, String ctID, String mdID) {
        this.current = current;
        this.bpID = bpID;
        this.ctID = ctID;
        this.mdID = mdID;

        new FetchMedicaoTask().execute(bpID, ctID, mdID);
    }

    // callback. will build MD pager
    public void completeMDFetch(List<Medicao> medicoes) {
        Intent intent = MedicaoPagerActivity.newIntent(current, medicoes, isEditing);
        current.startActivity(intent);
        current.finish();
    }

    public void onFABClick(Activity current) {
        Photo photo = new Photo();
        photo.setBemPublico(this.bpID);
        photo.setContrato(this.ctID);
        photo.setMedicao(this.mdID);
        PhotoLab.get(current).addPhoto(photo);
        Intent intent = PhotoActivity.newIntent(current, photo);
        current.startActivity(intent);
        current.finish();
    }

    public void onDone(Activity current) {
        mPhoto.setBemPublico(this.bpID);
        mPhoto.setContrato(this.ctID);
        mPhoto.setMedicao(this.mdID);
        Intent intent = PhotoActivity.newIntent(current, mPhoto);
        current.startActivity(intent);
        current.finish();
    }

    public void onCancel(Activity current) {
        mPhoto.setBemPublico(AuditorPreferences.getBemPublico(current));
        mPhoto.setContrato(AuditorPreferences.getContrato(current));
        mPhoto.setMedicao(AuditorPreferences.getMedicao(current));
        Intent intent = PhotoActivity.newIntent(current, mPhoto);
        current.startActivity(intent);
        current.finish();
    }

}
