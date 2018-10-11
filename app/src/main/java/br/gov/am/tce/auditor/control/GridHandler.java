package br.gov.am.tce.auditor.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.gov.am.tce.auditor.DAO.ImageDBHandler;
import br.gov.am.tce.auditor.MapActivity;
import br.gov.am.tce.auditor.PhotoActivity;
import br.gov.am.tce.auditor.model.Photo;
import br.gov.am.tce.auditor.service.AuditorPreferences;
import br.gov.am.tce.auditor.service.PhotoLab;

public class GridHandler {
    private Context mContext;
    private List<Photo> mDisplayedPhotoList;
    private List<Photo> mSelectedPhotoList = new ArrayList<>();
    private ImageDBHandler imageHandler;    // should be factory constructed

    public GridHandler(Context context) {
        mContext = context;
        imageHandler = new ImageDBHandler(mContext);
        mDisplayedPhotoList = PhotoLab.get(mContext).getPhotos();
    }

    public List<Photo> getPhotoList() {
        return mDisplayedPhotoList;
    }

    public void applyContext() {
        ContextHandler.get().applyContextSomePhotos((Activity) mContext, mSelectedPhotoList);
    }

    public void uploadPhotos() {
        imageHandler.uploadPhotos(mSelectedPhotoList);
    }

    public void downloadPhotos() {
        imageHandler.downloadPhotos();
    }

    public void newPhoto() {
        Photo photo = new Photo(UUID.randomUUID().toString());
        photo.setAuthor(AuditorPreferences.getUsername(mContext));
        PhotoLab.get(mContext).addPhoto(photo);
        Intent intent = PhotoActivity.newIntent(mContext, photo);
        mContext.startActivity(intent);
    }

    public void selectPhoto(Photo photo) {
        mSelectedPhotoList.add(photo);
    }

    public void deselectPhoto(Photo photo) {
        mSelectedPhotoList.remove(photo);
    }

    public void showPhoto(Photo photo) {
        Intent intent = PhotoActivity.newIntent(mContext, photo);
        mContext.startActivity(intent);
    }

    public void mapPhotos() {
        Intent mapIntent = MapActivity.newIntent(mContext, mSelectedPhotoList);
        mContext.startActivity(mapIntent);
    }

    public void filterPhotos(String bemPublico, String contrato, String medicao) {
        mDisplayedPhotoList = PhotoLab.get(mContext).searchPhotos(bemPublico, contrato, medicao);
    }

    public void removeFilterPhotos() {
        mDisplayedPhotoList = PhotoLab.get(mContext).getPhotos();
    }

}
