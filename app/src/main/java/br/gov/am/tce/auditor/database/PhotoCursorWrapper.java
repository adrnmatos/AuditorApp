package br.gov.am.tce.auditor.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import br.gov.am.tce.auditor.domain.Photo;
import br.gov.am.tce.auditor.database.PhotoDbSchema.PhotoTable;

/**
 * Created by adrnm on 24/10/2017.
 */

public class PhotoCursorWrapper extends CursorWrapper {

    public PhotoCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Photo getPhoto() {
        String uuidString = getString(getColumnIndex(PhotoTable.Cols.UUID));
        String title = getString(getColumnIndex(PhotoTable.Cols.TITLE));
        Double latitude = Double.valueOf(getString(getColumnIndex(PhotoTable.Cols.LATITUDE)));
        Double longitude = Double.valueOf(getString(getColumnIndex(PhotoTable.Cols.LONGITUDE)));
        long time = Long.valueOf(getString(getColumnIndex(PhotoTable.Cols.TIME)));

        Photo photo = new Photo(uuidString);
        photo.setTitle(title);
        photo.setLatitude(latitude);
        photo.setLongitude(longitude);
        photo.setTime(time);

        return photo;
    }

}
