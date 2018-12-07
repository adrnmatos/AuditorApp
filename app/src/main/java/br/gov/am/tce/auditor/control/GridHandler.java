package br.gov.am.tce.auditor.control;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.gov.am.tce.auditor.ContextPagerActivity;
import br.gov.am.tce.auditor.DAO.ImageDBHandler;
import br.gov.am.tce.auditor.MapActivity;
import br.gov.am.tce.auditor.PhotoActivity;
import br.gov.am.tce.auditor.model.ContextObject;
import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.AuditorPreferences;
import br.gov.am.tce.auditor.service.PhotoLab;

public class GridHandler {
    private Context mContext;
    private List<Photo> mSelectedPhotoList = new ArrayList<>();
    private String bemPublicoFilterStr;
    private String contratoFilterStr;
    private String medicaoFilterStr;
    private ImageDBHandler imageHandler;    // should be factory constructed

    public GridHandler(Context context) {
        mContext = context;
        imageHandler = new ImageDBHandler(mContext);
        bemPublicoFilterStr = contratoFilterStr = medicaoFilterStr = "";
    }

    public List<Photo> getPhotoList() {
        if(bemPublicoFilterStr != null && !bemPublicoFilterStr.isEmpty()) {
            return PhotoLab.get(mContext).searchPhotos(bemPublicoFilterStr, contratoFilterStr, medicaoFilterStr);
        } else {
            return PhotoLab.get(mContext).getPhotos();
        }
    }

    public void selectPhoto(Photo photo) {
        mSelectedPhotoList.add(photo);
    }

    public void deselectPhoto(Photo photo) {
        mSelectedPhotoList.remove(photo);
    }

    public void applyContext() {
        if(mSelectedPhotoList.size() == 0)
            return;

        Intent contextIntent = ContextPagerActivity.newIntent(mContext, mSelectedPhotoList);
        mContext.startActivity(contextIntent);
    }

    public void uploadPhotos() {
        imageHandler.uploadPhotos(mSelectedPhotoList);
    }

    public void downloadPhotos() {
        imageHandler.downloadPhotos(new ArrayList<ContextObject>());
    }

    public void mapPhotos() {
        Intent mapIntent = MapActivity.newIntent(mContext, mSelectedPhotoList);
        mContext.startActivity(mapIntent);
    }

    public void newPhoto() {
        Photo photo = new Photo(UUID.randomUUID().toString());
        photo.setAuthor(AuditorPreferences.getUsername(mContext));
        PhotoLab.get(mContext).addPhoto(photo);
        Intent intent = PhotoActivity.newIntent(mContext, photo);
        mContext.startActivity(intent);
    }

    public void showPhoto(Photo photo) {
        Intent intent = PhotoActivity.newIntent(mContext, photo);
        mContext.startActivity(intent);
    }

    public void filterPhotos(String bemPublico, String contrato, String medicao) {
        bemPublicoFilterStr = bemPublico;
        contratoFilterStr = contrato;
        medicaoFilterStr = medicao;
    }

    public void removeFilterPhotos() {
        bemPublicoFilterStr = contratoFilterStr = medicaoFilterStr = "";
    }

}
