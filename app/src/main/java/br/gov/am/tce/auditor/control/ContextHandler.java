package br.gov.am.tce.auditor.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.DAO.ImageDBHandler;
import br.gov.am.tce.auditor.SearchActivity;
import br.gov.am.tce.auditor.model.ContextObject;
import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.AuditorPreferences;
import br.gov.am.tce.auditor.service.PhotoLab;

public class ContextHandler{
    private static final int SEARCH_REQUEST_CODE = 100;

    private Context mContext;
    private List<Photo> photoList;
    private String mBP;
    private String mCT;
    private String mMD;
    private List<ContextObject> contextObjectList = new ArrayList<>();
    private ImageDBHandler imageHandler;

    public ContextHandler(Context context, List<Photo> photos) {
        mContext = context;
        photoList = photos;
        mBP = mCT = mMD = "";
        imageHandler = new ImageDBHandler(mContext);
    }

    public void initiateNavigation() {
        // download photos only
        if(photoList == null) {
            Intent searchIntent = new Intent(mContext, SearchActivity.class);
            ((Activity)mContext).startActivityForResult(searchIntent, SEARCH_REQUEST_CODE);
        }
        // context assignment
        else {
            // security check
            if(photoList.size() == 0)
                return;

            // from grid activity
            if(photoList.size() > 1) {
                Intent searchIntent = new Intent(mContext, SearchActivity.class);
                ((Activity)mContext).startActivityForResult(searchIntent, SEARCH_REQUEST_CODE);
            }
            // one photo selected
            else {
                if(photoList.get(0).getBemPublico().isEmpty()) {
                    Intent searchIntent = new Intent(mContext, SearchActivity.class);
                    ((Activity)mContext).startActivityForResult(searchIntent, SEARCH_REQUEST_CODE);
                }
                // initiate context assignment from actual context
                else {
                    mBP = photoList.get(0).getBemPublico();
                    mCT = photoList.get(0).getContrato();
                    mMD = photoList.get(0).getMedicao();

                    // contextObjectList = new EContasFetcher().fetchMedicao(mBP, mCT, mMD);
                }
            }
        }
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

    public void putInDownloadList() {
        imageHandler.searchPhotos(imageHandler.buildDatabaseReference(mBP, mCT, mMD));
        //        imageHandler.putInDownloadList(imageHandler.buildDatabaseReference(mBP, mCT, mMD));
        imageHandler.downloadPhotos();
    }

    public ImageDBHandler getImageHandler() {
        return imageHandler;
    }

    // MENU METHODS
    public void onDone() {
        if(photoList != null) {
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
        }
        imageHandler.downloadPhotos();
        ((Activity)mContext).finish();
    }

    public void onCancel() {
        ((Activity)mContext).finish();
    }

}
