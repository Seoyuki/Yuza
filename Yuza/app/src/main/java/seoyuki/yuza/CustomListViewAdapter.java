package seoyuki.yuza;

/**
 * Created by Administrator on 2016-10-27.
 */

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


public class CustomListViewAdapter extends ArrayAdapter<Student> {



    Context context;
    int layoutResourceId;
    Student data[] = null;

    public CustomListViewAdapter(Context context, int layoutResourceId, Student[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new WeatherHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.mImage);
            holder.txtTitle = (TextView)row.findViewById(R.id.mText);

            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }

        Student weather = data[position];
        holder.txtTitle.setText(weather.name);
        holder.imgIcon.setImageResource(weather.searchImgId);

        return row;
    }

    static class WeatherHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }

}
