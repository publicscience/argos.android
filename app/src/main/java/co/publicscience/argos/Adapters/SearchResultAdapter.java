package co.publicscience.argos.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import co.publicscience.argos.Models.SearchResult;
import co.publicscience.argos.R;


public class SearchResultAdapter extends SimpleCursorAdapter {
    private Context mContext;

    public SearchResultAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mContext=context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameView = (TextView)view.findViewById(R.id.searchResult);
        nameView.setText(cursor.getString(1));
    }
}
