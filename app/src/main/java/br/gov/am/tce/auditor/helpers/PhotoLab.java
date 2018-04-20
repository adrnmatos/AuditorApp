package br.gov.am.tce.auditor.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.gov.am.tce.auditor.database.PhotoBaseHelper;
import br.gov.am.tce.auditor.database.PhotoCursorWrapper;
import br.gov.am.tce.auditor.database.PhotoDbSchema.PhotoTable;
import br.gov.am.tce.auditor.domain.Photo;

/**
 * Created by adrnm on 24/10/2017.
 */

public class PhotoLab {

    public static PhotoLab sPhotoLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static PhotoLab get(Context context) {
        if(sPhotoLab == null) {
            sPhotoLab = new PhotoLab(context);
        }
        return sPhotoLab;
    }

    private PhotoLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PhotoBaseHelper(mContext).getWritableDatabase();
    }

    public void addPhoto(Photo photo) {
        ContentValues values = getContentValues(photo);
        mDatabase.insert(PhotoTable.NAME, null, values);
    }

    public void updatePhoto(Photo photo) {
        String id = photo.getId();
        ContentValues values = getContentValues(photo);

        mDatabase.update(PhotoTable.NAME, values,
                PhotoTable.Cols.UUID + " = ?",
                new String[] {id});
    }

    public void deletePhoto(Photo photo) {

        File file = getPhotoFile(photo);
        boolean b = file.delete();

        String id = photo.getId();
        mDatabase.delete(PhotoTable.NAME, PhotoTable.Cols.UUID + " = ?",
                new String[] {id});
    }

    public List<Photo> getPhotos() {
        List<Photo> photos = new ArrayList<>();
        PhotoCursorWrapper cursor = queryPhotos(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Photo photo = cursor.getPhoto();
                photos.add(photo);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return photos;
    }

    public Photo getPhoto(String id) {
        PhotoCursorWrapper cursor = queryPhotos(
                PhotoTable.Cols.UUID + " = ?",
                new String[] { id }
        );

        try {
            if(cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            Photo photo = cursor.getPhoto();
            return photo;
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Photo photo) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, photo.getPhotoFilename());
    }

    private PhotoCursorWrapper queryPhotos(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                PhotoTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new PhotoCursorWrapper(cursor);
    }

    // key-value collection used when persisting data
    private static ContentValues getContentValues(Photo photo) {
        ContentValues values = new ContentValues();
        values.put(PhotoTable.Cols.UUID, photo.getId());
        values.put(PhotoTable.Cols.TITLE, photo.getTitle());
        values.put(PhotoTable.Cols.LATITUDE, String.valueOf(photo.getLatitude()));
        values.put(PhotoTable.Cols.LONGITUDE, String.valueOf(photo.getLongitude()));
        values.put(PhotoTable.Cols.TIME, String.valueOf(photo.getTime()));
        values.put(PhotoTable.Cols.BEMPUBLICO, String.valueOf(photo.getBemPublico()));
        values.put(PhotoTable.Cols.CONTRATO, String.valueOf(photo.getContrato()));
        values.put(PhotoTable.Cols.MEDICAO, String.valueOf(photo.getMedicao()));

        return values;
    }
}
