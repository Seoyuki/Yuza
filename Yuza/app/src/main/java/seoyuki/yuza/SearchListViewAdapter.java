package seoyuki.yuza;

/**
 * Created by Administrator on 2016-10-29.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import seoyuki.yuza.Student;

public class SearchListViewAdapter extends ArrayAdapter<Student> {

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<Student> infoList = null;
    private Context mContext = null;

    public SearchListViewAdapter(Context c, int textViewResourceId,
                                 ArrayList<Student> arrays) {
        super(c, textViewResourceId, arrays);
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
    }

    class ViewHolder {
        public ImageView mIcon;

        public TextView mText;

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Student getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        View v = convertview;

        if (v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.listview_item, null);
            viewHolder.mText = (TextView) v.findViewById(R.id.mText);
            viewHolder.mIcon = (ImageView) v.findViewById(R.id.mImage);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.mText.setText(getItem(position).name);

        viewHolder.mIcon.setTag(position);

        return v;
    }
}
//</infoclass></infoclass></infoclass>