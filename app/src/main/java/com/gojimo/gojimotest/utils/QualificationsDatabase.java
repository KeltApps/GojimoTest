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

/**
 * Database with the information retrieved from the server
 */
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

    /**
     * Get all the Qualification inside of the database
     * @return
     */
    public Cursor getQualifications() {
        return getWritableDatabase().rawQuery(
                "select * from " + ScriptDatabase.QUALIFICATIONS_TABLE_NAME, null);
    }

    /**
     * Insert a new Qualification
     * @param idQualification id of the new Qualification
     * @param name name of the new Qualification
     * @param country country of the new Qualification
     */
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

    /**
     * Update a Qualification
     * @param id database id of the Qualification
     * @param idQualification id of the Qualification
     * @param name name of the Qualification
     * @param country country of the Qualification
     */
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

    /**
     * Synchronize the Qualifications inside of data  into the database
     * @param data data obtained from the server
     */
    public void synchronizeQualifications(String data) {
        Type fooType = new TypeToken<ArrayList<Qualification>>() {
        }.getType();
        //Parse the data
        List<Qualification> qualificationList = new GsonBuilder().create().fromJson(data, fooType);
        //Create a HasMap from the list obtained
        HashMap<String, Qualification> entryMap = new HashMap<>();
        for (Qualification qualification : qualificationList) {
            if(qualification.getCountry() == null)
                qualification.setCountry(new Country("ZZZ"));
            entryMap.put(qualification.getIdQualification(), qualification);
        }
        //Get all the Qualification inside of the database
        Cursor c = getQualifications();
        assert c != null;
        //Check if the Qualifications obtained from the server already exists in the database.
        //If the Qualification already exists, It will be updated
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
        //Insert the new Qualifications
        for (Qualification qualification : entryMap.values()) {
            insertQualification(
                    qualification.getIdQualification(),
                    qualification.getName(),
                    qualification.getCountry().getNameCountry()
            );
            synchronizeSubjects(qualification.getSubjectsLinkedList(), qualification.getIdQualification());
        }

    }

    /**
     * Get all the Subjects inside of the database
     * @return
     */
    public Cursor getSubjects() {
        return getWritableDatabase().rawQuery(
                "select * from " + ScriptDatabase.SUBJECTS_TABLE_NAME, null);
    }

    /**
     * Insert a new Subject
     * @param idQualification id of the Qualification which belongs
     * @param idSubject id of the new Subject
     * @param title title of the new Subject
     * @param colour colour of the new colour
     */
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

    /**
     * Update a Subject
     * @param id database id of the subject
     * @param idQualification id of the Qualification which belongs
     * @param idSubject id of the Subject
     * @param title title of the Subject
     * @param colour colour of the colour
     */
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


    /**
     * Synchronize the Subject inside of subjectList into the database
     * @param subjectList Subjects to synchronize
     * @param idQualification if of the Qualification which belongs
     */
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
