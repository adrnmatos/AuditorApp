package br.gov.am.tce.auditor.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.SearchActivity;
import br.gov.am.tce.auditor.model.ContextObject;
import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.AuditorPreferences;
import br.gov.am.tce.auditor.service.EContasFetcher;
import br.gov.am.tce.auditor.service.PhotoLab;

public class ContextHandler{
    private static final int SEARCH_REQUEST_CODE = 100;

    private Context mContext;
    private List<Photo> photoList;
    private String mBP;
    private String mCT;
    private String mMD;
    private List<ContextObject> contextObjectList = new ArrayList<>();


    public ContextHandler(Context context, List<Photo> photos) {
        mContext = context;
        photoList = photos;
        mBP = mCT = mMD = "";
    }

    public void applyContext() {
        if(photoList.size() == 0)
            return;

        if(photoList.size() == 1)
            if(!photoList.get(0).getBemPublico().isEmpty()) {
                mBP = photoList.get(0).getBemPublico();
                mCT = photoList.get(0).getContrato();
                mMD = photoList.get(0).getMedicao();

/*                contextObjectList = new EContasFetcher().fetchMedicao(mBP, mCT, mMD);*/
                return;
            }

        Intent searchIntent = new Intent(mContext, SearchActivity.class);
        ((Activity)mContext).startActivityForResult(searchIntent, SEARCH_REQUEST_CODE);
    }

    public String getBP() {
        return mBP;
    }

    public void setBP(String bp) {
        mBP = bp;
    }

    public String getCT() {
        return mCT;
    }

    public void setCT(String ct) {
        mCT = ct;
    }

    public String getMD() {
        return mMD;
    }

    public void setMD(String md) {
        mMD = md;
    }

    public List<ContextObject> getContextObjectList() {
        return contextObjectList;
    }

    public void setContextObjectList(List<ContextObject> returnList) {
        contextObjectList = returnList;
    }

    public void navigateUP(String navigationString) {
        if(navigationString.equals("bemPublico")) {
            mCT = mMD = "";
        } else {
            if(navigationString.equals("contrato")) {
                mMD = "";
            }
        }
    }

    /* ***************** MENU SELECTION HANDLING *************************/
    public void onDone() {
        for(Photo photo: photoList) {
            if(photo.getAuthor().equals(AuditorPreferences.getUsername(mContext))) {
                photo.setBemPublico(this.mBP);
                photo.setContrato(this.mCT);
                photo.setMedicao(this.mMD);
                PhotoLab.get(mContext).updatePhoto(photo);
            } else {
                Toast.makeText(mContext, "you cannot edit other authors photo", Toast.LENGTH_LONG).show();
            }
        }
        ((Activity)mContext).finish();
    }

    public void onCancel() {
        ((Activity)mContext).finish();
    }

}
