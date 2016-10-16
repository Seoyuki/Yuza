package seoyuki.yuza;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ArrayList<Student> list;
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    private TextView searchText;
    ArrayList<Student> tempList;
    ArrayList<Student> mlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("유적 찾아보기");

        mListView = (ListView) findViewById(R.id.listView);
        searchText = (TextView)findViewById(R.id.editText);
        mAdapter = new ListViewAdapter(this, mlist);


        list = xmlParser();
        String[] data = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
//            data[i] = list.get(i).getName()+"\n"+list.get(i).getName();
            mAdapter.addItem(getResources().getDrawable(R.drawable.yuza_bike_search),
                    list.get(i).getName(),
                    list.get(i).getAddress());
        }
        mListView.setTextFilterEnabled(true);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().equalsIgnoreCase("")) {
                    if (tempList!=null) {
                        mlist = (ArrayList<Student>) tempList.clone();
                    }
                } else {
                    SearchActivity.this.mAdapter.getFilter().filter(s);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                setVisibiltyList();
            }
        });


                //listView.setAdapter(arrad);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                Student mData = mAdapter.mListData.get(position);
                    Student data = list.get(position);
                // 다음 액티비티로 넘길 Bundle 데이터를 만든다.
                Bundle extras = new Bundle();
                extras.putString("name", data.getName());
                extras.putString("address", data.getAddress());
                extras.putString("content", data.getContent());
                extras.putString("image", data.getImage());
                // 인텐트를 생성한다.
                // 컨텍스트로 현재 액티비티를, 생성할 액티비티로 DetailActivity 를 지정한다.
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                // 위에서 만든 Bundle을 인텐트에 넣는다.
                intent.putExtras(extras);
                // 액티비티를 생성한다.
                startActivity(intent);
            }
        });

    }
    public void setVisibiltyList() {
        if (searchText.getText().toString().equals("")) {
            mListView.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.VISIBLE);
        }
    }

    public void textonClick(View v) {
        Toast toast = Toast.makeText(this, "안녕하세요", Toast.LENGTH_LONG);
        toast.show();
        mListView.setAdapter(mAdapter);
    }
    private class ViewHolder {
        public ImageView mIcon;

        public TextView mText;

        public TextView mDate;
    }

    private class ListViewAdapter extends BaseAdapter implements Filterable {
        private Context mContext = null;
        private ArrayList<Student> mListData = new ArrayList<Student>();
        Activity context;

        public ListViewAdapter(Activity context, ArrayList<Student> list) {
            super();
            this.context = context;
            mlist = list;
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

        public void addItem(Drawable icon, String mTitle, String mDate){
            Student addInfo = null;
            addInfo = new Student();
            addInfo.imgId = icon;
            addInfo.name = mTitle;
            addInfo.address = mDate;

            mListData.add(addInfo);
        }

        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }

        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item, null);

                holder.mIcon = (ImageView) convertView.findViewById(R.id.mImage);
                holder.mText = (TextView) convertView.findViewById(R.id.mText);
                holder.mDate = (TextView) convertView.findViewById(R.id.mDate);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            Student mData = mListData.get(position);

            if (mData.imgId != null) {
                holder.mIcon.setVisibility(View.VISIBLE);
                holder.mIcon.setImageDrawable(mData.imgId);
            }else{
                holder.mIcon.setVisibility(View.GONE);
            }

            holder.mText.setText(mData.getName());
            holder.mDate.setText(mData.getAddress());

            return convertView;
        }
        public Filter getFilter() {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,
                                              Filter.FilterResults results) {
                    mlist = (ArrayList<Student>) results.values;
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    ArrayList<Student> FilteredList = new ArrayList<Student>();
                    if (constraint == null || constraint.length() == 0) {
                        // No filter implemented we return all the list
                        results.values = mlist;
                        results.count = mlist.size();

                    } else {

                        for (int i = 0; i < mlist.size(); i++) {
                            String data = mlist.get(i).name;

                            if (data.toLowerCase().contains(
                                    constraint.toString().toLowerCase())) {
                                FilteredList.add(mlist.get(i));
                            }
                        }
                        results.values = FilteredList;
                        results.count = FilteredList.size();
                    }

                    return results;
                }
            };

            return filter;
        }


    }
    //xmlParser를 사용해 xml 파싱하기
    private ArrayList<Student> xmlParser() {
        ArrayList<Student> arrayList = new ArrayList<Student>();
        InputStream is = getResources().openRawResource(R.raw.testvalues);
        // xmlPullParser

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            Student student = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if (startTag.equals("historic")) {
                            student = new Student();
                        }
                        if (startTag.equals("id")) {
                            student.setId(parser.next());
                        }
                        if (startTag.equals("name")) {
                            student.setName(parser.nextText());
                        }
                        if (startTag.equals("address")) {
                            student.setAddress(parser.nextText());
                        }
                        if (startTag.equals("content")) {
                            student.setContent(parser.nextText());
                        }
                        if (startTag.equals("wido")) {
                            student.setWido(parser.nextText());
                        }
                        if (startTag.equals("kyungdo")) {
                            student.setKyungdo(parser.nextText());
                        }
                        if (startTag.equals("image")) {
                            student.setImage(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if (endTag.equals("historic")) {
                            arrayList.add(student);
                        }
                        break;
                }
                eventType = parser.next();
            }


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
