package com.gojimo.gojimotest.views.adapters;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gojimo.gojimotest.R;
import com.gojimo.gojimotest.utils.ScriptDatabase;


public class SubjectsCursorAdapter extends CursorRecyclerViewAdapter<SubjectsCursorAdapter.ViewHolderSubjects> {

    public SubjectsCursorAdapter() {
        super(null);
    }

    @Override
    protected void onBindViewHolder(SubjectsCursorAdapter.ViewHolderSubjects viewHolder, Cursor cursor) {
        viewHolder.bindHolder(cursor);
    }

    @Override
    public SubjectsCursorAdapter.ViewHolderSubjects onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderSubjects(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subjects, parent, false));
    }


    public static class ViewHolderSubjects extends RecyclerView.ViewHolder {
        View container;
        TextView text;

        public ViewHolderSubjects(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.item_subjects_container);
            text = (TextView) itemView.findViewById(R.id.item_subjects_text);
        }

        public void bindHolder(Cursor cursor) {
            text.setText(cursor.getString(cursor.getColumnIndex(ScriptDatabase.ColumnSubjects.TITLE)));
            String color = cursor.getString(cursor.getColumnIndex(ScriptDatabase.ColumnSubjects.COLOUR));
            if(color != null && !color.equals(""))
               container.setBackgroundColor(Color.parseColor(color));
        }

    }
}
