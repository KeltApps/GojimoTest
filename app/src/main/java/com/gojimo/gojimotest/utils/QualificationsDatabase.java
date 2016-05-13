package com.gojimo.gojimotest.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gojimo.gojimotest.models.Country;
import com.gojimo.gojimotest.models.Qualification;
import com.gojimo.gojimotest.models.Subject;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class QualificationsDatabase extends SQLiteOpenHelper {
    private static final String TAG = QualificationsDatabase.class.getSimpleName();

    public static final String DATABASE_NAME = "Qualifications.db";

    public static final int DATABASE_VERSION = 1;

    private static QualificationsDatabase ourInstance;

    public static synchronized QualificationsDatabase getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new QualificationsDatabase(context.getApplicationContext());
        return ourInstance;
    }

    private QualificationsDatabase(Context context) {
        super(context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScriptDatabase.CREATE_QUALIFICATIONS_TABLE);
        db.execSQL(ScriptDatabase.CREATE_SUBJECTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ScriptDatabase.QUALIFICATIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ScriptDatabase.SUBJECTS_TABLE_NAME);
        onCreate(db);
    }

    public Cursor getQualifications() {
        return getWritableDatabase().rawQuery(
                "select * from " + ScriptDatabase.QUALIFICATIONS_TABLE_NAME, null);
    }


    private void insertQualification(
            String idQualification,
            String name,
            String country) {

        ContentValues values = new ContentValues();
        values.put(ScriptDatabase.ColumnQualifications.ID_QUALIFICATION, idQualification);
        values.put(ScriptDatabase.ColumnQualifications.NAME, name);
        values.put(ScriptDatabase.ColumnQualifications.COUNTRY, country);

        getWritableDatabase().insert(
                ScriptDatabase.QUALIFICATIONS_TABLE_NAME,
                null,
                values
        );
    }


    private void updateQualification(int id,
                                     String idQualification,
                                     String name,
                                     String country) {

        ContentValues values = new ContentValues();
        values.put(ScriptDatabase.ColumnQualifications.ID_QUALIFICATION, idQualification);
        values.put(ScriptDatabase.ColumnQualifications.NAME, name);
        values.put(ScriptDatabase.ColumnQualifications.COUNTRY, country);

        getWritableDatabase().update(
                ScriptDatabase.QUALIFICATIONS_TABLE_NAME,
                values,
                ScriptDatabase.ColumnQualifications.ID + "=?",
                new String[]{String.valueOf(id)});
    }

    public void synchronizeQualifications(String data) {
        Type fooType = new TypeToken<ArrayList<Qualification>>() {
        }.getType();
        List<Qualification> qualificationList = new GsonBuilder().create().fromJson(data, fooType);
        HashMap<String, Qualification> entryMap = new HashMap<>();
        for (Qualification qualification : qualificationList) {
            if(qualification.getCountry() == null)
                qualification.setCountry(new Country("ZZZ"));
            entryMap.put(qualification.getIdQualification(), qualification);
        }

        Cursor c = getQualifications();
        assert c != null;

        while (c.moveToNext()) {
            String qualificationId = c.getString(c.getColumnIndex(ScriptDatabase.ColumnQualifications.ID_QUALIFICATION));
            Qualification match = entryMap.get(qualificationId);
            if (match != null) {
                entryMap.remove(qualificationId);
                if (!match.getIdQualification().equals(qualificationId)) {
                    updateQualification(
                            c.getInt(c.getColumnIndex(ScriptDatabase.ColumnQualifications.ID)),
                            match.getIdQualification(),
                            match.getName(),
                            match.getCountry().getNameCountry());
                    synchronizeSubjects(match.getSubjectsLinkedList(), match.getIdQualification());
                }
            }
        }
        c.close();
        for (Qualification qualification : entryMap.values()) {
            insertQualification(
                    qualification.getIdQualification(),
                    qualification.getName(),
                    qualification.getCountry().getNameCountry()
            );
            synchronizeSubjects(qualification.getSubjectsLinkedList(), qualification.getIdQualification());
        }

    }

    public Cursor getSubjects() {
        return getWritableDatabase().rawQuery(
                "select * from " + ScriptDatabase.SUBJECTS_TABLE_NAME, null);
    }


    private void insertSubject(
            String idQualification,
            String idSubject,
            String title,
            String colour) {

        ContentValues values = new ContentValues();
        values.put(ScriptDatabase.ColumnSubjects.ID_QUALIFICATION, idQualification);
        values.put(ScriptDatabase.ColumnSubjects.ID_SUBJECT, idSubject);
        values.put(ScriptDatabase.ColumnSubjects.TITLE, title);
        values.put(ScriptDatabase.ColumnSubjects.COLOUR, colour);

        getWritableDatabase().insert(
                ScriptDatabase.SUBJECTS_TABLE_NAME,
                null,
                values
        );
    }

    private void updateSubject(int id,
                               String idQualification,
                               String idSubject,
                               String title,
                               String colour) {

        ContentValues values = new ContentValues();
        values.put(ScriptDatabase.ColumnSubjects.ID_QUALIFICATION, idQualification);
        values.put(ScriptDatabase.ColumnSubjects.ID_SUBJECT, idSubject);
        values.put(ScriptDatabase.ColumnSubjects.TITLE, title);
        values.put(ScriptDatabase.ColumnSubjects.COLOUR, colour);

        getWritableDatabase().update(
                ScriptDatabase.SUBJECTS_TABLE_NAME,
                values,
                ScriptDatabase.ColumnSubjects.ID + "=?",
                new String[]{String.valueOf(id)});
    }


    private void synchronizeSubjects(List<Subject> subjectList, String idQualification) {
        HashMap<String, Subject> entryMap = new HashMap<>();
        for (Subject subject : subjectList)
            entryMap.put(subject.getIdSubject(), subject);

        Cursor c = getSubjects();
        assert c != null;

        while (c.moveToNext()) {
            String subjectId = c.getString(c.getColumnIndex(ScriptDatabase.ColumnSubjects.ID_SUBJECT));
            Subject match = entryMap.get(subjectId);
            if (match != null) {
                entryMap.remove(subjectId);
                if (!match.getIdSubject().equals(subjectId)) {
                    updateSubject(
                            c.getInt(c.getColumnIndex(ScriptDatabase.ColumnQualifications.ID)),
                            idQualification,
                            match.getIdSubject(),
                            match.getTitle(),
                            match.getColour());
                }
            }
        }
        c.close();
        for (Subject subject : entryMap.values()) {
            insertSubject(idQualification,
                    subject.getIdSubject(),
                    subject.getTitle(),
                    subject.getColour()
            );
        }
    }

}
