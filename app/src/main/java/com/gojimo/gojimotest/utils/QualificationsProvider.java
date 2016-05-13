package com.gojimo.gojimotest.utils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;


public class QualificationsProvider extends ContentProvider {
    private static final String prefix_content = "content://";
    private static final String prefix_uri = "com.gojimo.gojimotest.contentproviders";
    private static final String uri_qualifications = prefix_content + prefix_uri + "/" + ScriptDatabase.QUALIFICATIONS_TABLE_NAME;
    public static final Uri CONTENT_URI_QUALIFICATIONS = Uri.parse(uri_qualifications);
    private static final String uri_subjects = prefix_content + prefix_uri + "/" + ScriptDatabase.SUBJECTS_TABLE_NAME;
    public static final Uri CONTENT_URI_SUBJECTS = Uri.parse(uri_subjects);

    private static final String MIME_LIST = "vnd.android.cursor.dir/vnd.gojimotest.qualifications";


    private QualificationsDatabase qualificationsDatabase;

    //UriMatcher
    private static final int QUALIFICATIONS = 1;
    private static final int SUBJECTS = 2;
    private static final UriMatcher uriMatcher;

    //Initialize UriMatcher
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(prefix_uri, ScriptDatabase.QUALIFICATIONS_TABLE_NAME, QUALIFICATIONS);
        uriMatcher.addURI(prefix_uri, ScriptDatabase.SUBJECTS_TABLE_NAME, SUBJECTS);
    }


    @Override
    public boolean onCreate() {
        qualificationsDatabase = QualificationsDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case QUALIFICATIONS:
                return qualificationsDatabase.getWritableDatabase().query(ScriptDatabase.QUALIFICATIONS_TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
            case SUBJECTS:
                return qualificationsDatabase.getWritableDatabase().query(ScriptDatabase.SUBJECTS_TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case QUALIFICATIONS:
                return MIME_LIST;
            case SUBJECTS:
                return MIME_LIST;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long regId;
        switch (uriMatcher.match(uri)) {
            case QUALIFICATIONS:
                regId = qualificationsDatabase.getWritableDatabase().insert(ScriptDatabase.QUALIFICATIONS_TABLE_NAME, null, values);
                return ContentUris.withAppendedId(CONTENT_URI_QUALIFICATIONS, regId);
            case SUBJECTS:
                regId = qualificationsDatabase.getWritableDatabase().insert(ScriptDatabase.SUBJECTS_TABLE_NAME, null, values);
                return ContentUris.withAppendedId(CONTENT_URI_SUBJECTS, regId);
            default:
                return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case QUALIFICATIONS:
                return qualificationsDatabase.getWritableDatabase().delete(ScriptDatabase.QUALIFICATIONS_TABLE_NAME, selection, selectionArgs);
            case SUBJECTS:
                return qualificationsDatabase.getWritableDatabase().delete(ScriptDatabase.SUBJECTS_TABLE_NAME, selection, selectionArgs);
            default:
                return 0;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case QUALIFICATIONS:
                return qualificationsDatabase.getWritableDatabase().update(ScriptDatabase.QUALIFICATIONS_TABLE_NAME, values, selection, selectionArgs);
            case SUBJECTS:
                return qualificationsDatabase.getWritableDatabase().update(ScriptDatabase.SUBJECTS_TABLE_NAME, values, selection, selectionArgs);
            default:
                return 0;
        }
    }
}
