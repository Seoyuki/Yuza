package seoyuki.yuza;

/**
 * Created by Administrator on 2016-10-26.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

        //추천지 리스트를 뿌려주기 위한 ListViewAdapter
             class ListViewAdapter extends BaseAdapter {
            Activity context;
                    private ArrayList<Student> mListData = new ArrayList<Student>();

                    public ListViewAdapter(Activity context) {
                    super();
                    this.context = context;
                }
                class ViewHolder {
                    public ImageView mIcon;

                    public TextView mText;

                }
                    @Override
            public int getCount() {
                    return mListData.size();
                }

                    @Override
            public Object getItem(int position) {
                    return mListData.get(position);
                }

                    @Override
                        public long getItemId(int position) {
                    return position;
                }

                    public void addItem(Student addInfo){
                    mListData.add(addInfo);
                        }

                    @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                    ViewHolder holder;
                    Student mData = mListData.get(position);

                            if (convertView == null) {
                                                holder = new ViewHolder();

                                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.listview_item, null);

                                    holder.mIcon = (ImageView) convertView.findViewById(R.id.mImage);
                            holder.mText = (TextView) convertView.findViewById(R.id.mText);

                                    convertView.setTag(holder);
                        }else{
                            holder = (ViewHolder) convertView.getTag();
                        }



                                            if (mData.imgId != null) {
                            holder.mIcon.setVisibility(View.VISIBLE);
                            holder.mIcon.setImageDrawable(mData.imgId);
                        }else{
                            holder.mIcon.setVisibility(View.GONE);
                        }

                            holder.mText.setText(mData.getName());

                            return convertView;
                }
        }