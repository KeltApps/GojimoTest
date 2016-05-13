package com.gojimo.gojimotest.utils;

import android.provider.BaseColumns;


public class ScriptDatabase {

    public static final String QUALIFICATIONS_TABLE_NAME = "qualificationsTable";
    public static final String SUBJECTS_TABLE_NAME = "subjectsTable";
    private static final String STRING_TYPE = "TEXT";
    private static final String INT_TYPE = "INTEGER";

    public static class ColumnQualifications implements BaseColumns {
        public static final String ID = _ID;
        public static final String ID_QUALIFICATION = "idQualifications";
        public static final String NAME = "name";
        public static final String COUNTRY = "country";
    }

    public static final String CREATE_QUALIFICATIONS_TABLE =
            "CREATE TABLE " + QUALIFICATIONS_TABLE_NAME + "(" +
                    ColumnQualifications.ID + " " + INT_TYPE + " primary key autoincrement," +
                    ColumnQualifications.ID_QUALIFICATION + " " + STRING_TYPE + "," +
                    ColumnQualifications.NAME + " " + STRING_TYPE + "," +
                    ColumnQualifications.COUNTRY + " " + STRING_TYPE + ")";

    public static class ColumnSubjects implements BaseColumns {
        public static final String ID = _ID;
        public static final String ID_QUALIFICATION = "idQualification";
        public static final String ID_SUBJECT = "idSubject";
        public static final String TITLE = "title";
        public static final String COLOUR = "colour";
    }

    public static final String CREATE_SUBJECTS_TABLE =
            "CREATE TABLE " + SUBJECTS_TABLE_NAME + "(" +
                    ColumnSubjects.ID + " " + INT_TYPE + " primary key autoincrement," +
                    ColumnSubjects.ID_QUALIFICATION + " " + STRING_TYPE + "," +
                    ColumnSubjects.ID_SUBJECT + " " + STRING_TYPE + "," +
                    ColumnSubjects.TITLE + " " + STRING_TYPE + "," +
                    ColumnSubjects.COLOUR + " " + STRING_TYPE + ")";

}
