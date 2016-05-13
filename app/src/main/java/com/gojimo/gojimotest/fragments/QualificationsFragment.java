package com.gojimo.gojimotest.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gojimo.gojimotest.R;
import com.gojimo.gojimotest.network.VolleySingleton;
import com.gojimo.gojimotest.utils.QualificationsDatabase;
import com.gojimo.gojimotest.utils.QualificationsProvider;
import com.gojimo.gojimotest.utils.ScriptDatabase;
import com.gojimo.gojimotest.views.adapters.QualificationsCursorAdapter;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


public class QualificationsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, Response.Listener<String>, Response.ErrorListener, AdapterView.OnItemClickListener {
    private static final String TAG = QualificationsFragment.class.getSimpleName();
    private QualificationsCursorAdapter qualificationsCursorAdapter = null;
    private StickyListHeadersListView stickyList;
    private Cursor mCursor = null;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_qualifications, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.fragment_qualifications_progress);
        stickyList = (StickyListHeadersListView) rootView.findViewById(R.id.fragment_qualifications_list);
        stickyList.setOnItemClickListener(this);
        ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        requestQualifications();
        return rootView;
    }

    private void requestQualifications() {
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(new StringRequest(Request.Method.GET,
                VolleySingleton.URL_QUALIFICATIONS,
                QualificationsFragment.this, QualificationsFragment.this));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        qualificationsCursorAdapter = null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (!isAdded())
            return;
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public void onResponse(String response) {
        if (!isAdded())
            return;
        QualificationsDatabase.getInstance(getActivity()).synchronizeQualifications(response);
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), QualificationsProvider.CONTENT_URI_QUALIFICATIONS, null, null, null,
                ScriptDatabase.ColumnQualifications.COUNTRY + " ASC" + ", " + ScriptDatabase.ColumnQualifications.NAME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        progressBar.setVisibility(View.INVISIBLE);
        mCursor = data;
        if (qualificationsCursorAdapter == null) {
            qualificationsCursorAdapter = new QualificationsCursorAdapter(getActivity(), data);
            stickyList.setAdapter(qualificationsCursorAdapter);
        } else
            qualificationsCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
        if (qualificationsCursorAdapter != null)
            qualificationsCursorAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SubjectsFragment subjectsFragment = new SubjectsFragment();
        Bundle bundle = new Bundle();
        mCursor.moveToPosition(position);
        String idQualifications = mCursor.getString(mCursor.getColumnIndex(ScriptDatabase.ColumnQualifications.ID_QUALIFICATION));
        bundle.putString(SubjectsFragment.ARGS_ID_QUALIFICATIONS, idQualifications);
        String name = mCursor.getString(mCursor.getColumnIndex(ScriptDatabase.ColumnQualifications.NAME));
        bundle.putString(SubjectsFragment.ARGS_NAME, name);
        subjectsFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.activity_main_container, subjectsFragment, getString(R.string.fragment_subjects));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }
}

