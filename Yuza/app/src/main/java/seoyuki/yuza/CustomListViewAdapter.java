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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

        public class CustomListViewAdapter extends ArrayAdapter<Student> {
            Context context;
            public CustomListViewAdapter(Context context, int resourceId, List<Student> items) {
                super(context, resourceId, items);
                this.context = context;
            }
            /*private view holder class*/
            private class ViewHolder {
                ImageView imageView;
                TextView txtTitle;

            }

            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                Student student = getItem(position);
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.listview_item, null);
                    holder = new ViewHolder();
                    holder.txtTitle = (TextView) convertView.findViewById(R.id.mText);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.mImage);
                    convertView.setTag(holder);
                } else {
        holder = (ViewHolder) convertView.getTag();

        holder.txtTitle.setText(student.getName());
        holder.imageView.setImageResource(R.drawable.yuza_bike);
    }

    return convertView;

}
}