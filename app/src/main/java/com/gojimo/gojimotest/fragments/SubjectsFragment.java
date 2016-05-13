package com.gojimo.gojimotest.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gojimo.gojimotest.R;
import com.gojimo.gojimotest.utils.QualificationsProvider;
import com.gojimo.gojimotest.utils.ScriptDatabase;
import com.gojimo.gojimotest.views.adapters.SubjectsCursorAdapter;

/**
 * Fragment to show the subjects of a qualification
 */
public class SubjectsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static String ARGS_ID_QUALIFICATIONS = "argsIdQualifications";
    public static String ARGS_NAME = "argsName";
    private SubjectsCursorAdapter subjectsCursorAdapter;
    private String idQualifications;
    private TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subjects, container, false);
        Bundle bundle = getArguments();
        idQualifications = bundle.getString(ARGS_ID_QUALIFICATIONS);
        ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(bundle.getString(ARGS_NAME));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        text = (TextView) rootView.findViewById(R.id.fragment_subjects_text);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_subjects_recyclerView);
        subjectsCursorAdapter = new SubjectsCursorAdapter();
        recyclerView.setAdapter(subjectsCursorAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getLoaderManager().initLoader(0, null, this);

        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subjectsCursorAdapter = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] selectionArgs = {idQualifications};
        return new CursorLoader(getActivity(), QualificationsProvider.CONTENT_URI_SUBJECTS, null, ScriptDatabase.ColumnSubjects.ID_QUALIFICATION + " =? ", selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount() == 0)
            text.setVisibility(View.VISIBLE);
        else
        subjectsCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (subjectsCursorAdapter != null)
            subjectsCursorAdapter.swapCursor(null);
    }
}
