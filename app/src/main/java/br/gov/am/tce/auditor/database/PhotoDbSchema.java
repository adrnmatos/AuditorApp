package br.gov.am.tce.auditor.database;

/**
 * Created by adrnm on 24/10/2017.
 */

public class PhotoDbSchema {
    public static final class PhotoTable {
        public static final String NAME = "fotos";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String TIME = "time";
            public static final String BEMPUBLICO = "bemPublico";
            public static final String CONTRATO = "contrato";
            public static final String MEDICAO = "medicao";
        }
    }
}
