package com.gojimo.gojimotest.views.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gojimo.gojimotest.R;
import com.gojimo.gojimotest.utils.ScriptDatabase;
import com.squareup.picasso.Picasso;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


public class QualificationsCursorAdapter extends CursorAdapter implements StickyListHeadersAdapter {
    private static String COUNTRY_CHINA = "China";
    private static String COUNTRY_IRELAND = "Ireland";
    private static String COUNTRY_SOUTH_AFRICA = "South Africa";
    private static String COUNTRY_UNITED_KINGDOM = "United Kingdom";
    private static String COUNTRY_UNITED_STATES = "United States";
    private final Context context;
    private Cursor mCursor;

    public QualificationsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.context = context;
        mCursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_qualifications_body, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView text = (TextView) view.findViewById(R.id.item_qualification_body_text);
        text.setText(cursor.getString(cursor.getColumnIndex(ScriptDatabase.ColumnQualifications.NAME)));
    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        ViewHolderHeader holder;
        mCursor.moveToPosition(position);
        if (convertView == null) {
            holder = new ViewHolderHeader();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_qualifications_header, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.item_qualification_header_image);
            holder.text = (TextView) convertView.findViewById(R.id.item_qualification_header_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderHeader) convertView.getTag();
        }

        String sText = mCursor.getString(mCursor.getColumnIndex(ScriptDatabase.ColumnQualifications.COUNTRY));
        if (sText.equals("ZZZ"))
            sText = context.getString(R.string.qualifications_countryWorldwide);
        holder.text.setText(sText);

        setDrawable(holder,sText);

        return convertView;
    }

    private void setDrawable( ViewHolderHeader holder,String sText) {
        int resource = R.drawable.globe;
        if (sText.equals(COUNTRY_CHINA))
            resource = R.drawable.china;
        else if (sText.equals(COUNTRY_IRELAND))
            resource = R.drawable.ireland;
        else if (sText.equals(COUNTRY_SOUTH_AFRICA))
            resource = R.drawable.southafrica;
        else if (sText.equals(COUNTRY_UNITED_KINGDOM))
            resource = R.drawable.unitedkingdom;
        else if (sText.equals(COUNTRY_UNITED_STATES))
            resource = R.drawable.usa;
        Picasso.with(context).load(resource).centerCrop().fit().into(holder.image);
    }

    @Override
    public long getHeaderId(int position) {
        mCursor.moveToPosition(position);
        String country = mCursor.getString(mCursor.getColumnIndex(ScriptDatabase.ColumnQualifications.COUNTRY));
        return country.hashCode();
    }

    private class ViewHolderHeader {
        ImageView image;
        TextView text;
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        return super.swapCursor(newCursor);
    }

}
