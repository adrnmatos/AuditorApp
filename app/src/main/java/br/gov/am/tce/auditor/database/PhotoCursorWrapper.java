package br.gov.am.tce.auditor.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import br.gov.am.tce.auditor.database.PhotoDbSchema.PhotoTable;
import br.gov.am.tce.auditor.model.Photo;

/**
 * Created by adrnm on 24/10/2017.
 */

public class PhotoCursorWrapper extends CursorWrapper {

    public PhotoCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Photo getPhoto() {
        String uuidString = getString(getColumnIndex(PhotoTable.Cols.UUID));
        //String autorString = getString(getColumnIndex(PhotoTable.Cols.AUTOR));
        String title = getString(getColumnIndex(PhotoTable.Cols.TITLE));
        Double latitude = Double.valueOf(getString(getColumnIndex(PhotoTable.Cols.LATITUDE)));
        Double longitude = Double.valueOf(getString(getColumnIndex(PhotoTable.Cols.LONGITUDE)));
        long time = Long.valueOf(getString(getColumnIndex(PhotoTable.Cols.TIME)));
        String bemPublico = getString(getColumnIndex(PhotoTable.Cols.BEMPUBLICO));
        String contrato = getString(getColumnIndex(PhotoTable.Cols.CONTRATO));
        String medicao = getString(getColumnIndex(PhotoTable.Cols.MEDICAO));

        Photo photo = new Photo(uuidString);
        //photo.setAutor(autorString);
        photo.setTitle(title);
        photo.setLatitude(latitude);
        photo.setLongitude(longitude);
        photo.setTime(time);
        photo.setBemPublico(bemPublico);
        photo.setContrato(contrato);
        photo.setMedicao(medicao);

        return photo;
    }

}
