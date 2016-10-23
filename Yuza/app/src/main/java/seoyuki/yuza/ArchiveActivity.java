package seoyuki.yuza;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-10-16.
 */

public class ArchiveActivity extends AppCompatActivity {

    SQLiteDatabase db;
    SqlLiteYuzaOpenHelper helper;
    List<YuzaRanking> yuzaRanking;

    private ListView mArchiveListView = null;
    private ListView archiveRecoListView = null;
    private ArchiveActivity.ListViewAdapter mArchiveAdapter = null;
    private ArchiveActivity.ListViewAdapter archiveRecoAdapter = null;
    ArrayList<Student> mArchiveStudentList = new ArrayList<Student>();
    private ArrayList<YuzaRanking> mArchiveListData = new ArrayList<YuzaRanking>();

    private ImageView archiveImage;
    private TextView archiveTitleText;
    private TextView archiveInfoText;

    private StringBuilder archiveTitle = new StringBuilder();
    private int level;
    private final int archiveImageLevelId[] = {
            R.drawable.level1,
            R.drawable.level2,
            R.drawable.level3,
            R.drawable.level4,
            R.drawable.level5,
            R.drawable.level6,
            R.drawable.level7
    };
    private final String archiveInfo[] = {
            "유적들과 친해지는 단계 ^^",
            "조금씩 빠져들고 있음!",
            "본격적으로 유적 다녀와보기!",
            "이제는 매니아라고 해도 되겠어요!",
            "유자의 달인? ^^",
            "유자의 신!",
            "앞으로도 행복한 자전거 여행 하세요!"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("나의 여정");

        archiveRecoListView = (ListView) findViewById(R.id.archiveListView);
        mArchiveListView = (ListView) findViewById(R.id.archiveListView);
        // searchText = (TextView)findViewById(R.id.archiveText);
        mArchiveAdapter = new ListViewAdapter(this);
        archiveRecoAdapter = new ListViewAdapter(this);

        archiveImage = (ImageView) findViewById(R.id.archiveImage);
        archiveTitleText = (TextView) findViewById(R.id.archiveTitleText);
        archiveInfoText = (TextView) findViewById(R.id.archiveInfoText);

        helper = new SqlLiteYuzaOpenHelper(this, // 현재 화면의 context
                "yuza.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호

        List<YuzaRanking> archiveList = select(); // 현재 완료한 곳 리스트 불러오기

        mArchiveStudentList = xmlParser();

        // 도착한 곳의 숫자만큼 업적 이미지와 업적 관련 텍스트 보여주기
        level = archiveList.size() / 15;
        setArchiveImageView(level);
        setAchiveTextView(level);

        for (YuzaRanking i : archiveList) {
            mArchiveAdapter.addItem(i); }

        mArchiveListView.setAdapter(mArchiveAdapter);

        mArchiveListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){

                Student data = mArchiveStudentList.get(position);
                // 다음 액티비티로 넘길 Bundle 데이터를 만든다.
                Bundle extras = new Bundle();
                extras.putString("name", data.getName());
                extras.putString("address", data.getAddress());
                extras.putString("content", data.getContent());
                extras.putString("image", data.getImage());
                extras.putString("wido", data.getWido());
                extras.putString("kyungdo", data.getKyungdo());
                // 인텐트를 생성한다.
                // 컨텍스트로 현재 액티비티를, 생성할 액티비티로 DetailActivity 를 지정한다.
                Intent intent = new Intent(ArchiveActivity.this, DetailActivity.class);
                // 위에서 만든 Bundle을 인텐트에 넣는다.
                intent.putExtras(extras);
                // 액티비티를 생성한다.
                startActivity(intent);
            }
        });



//        // 1. 데이터 저장
//        this.insert(1,"유적지",100,"2016-10-10 20:00");
//        this.insert(2,"유적지2",100,"2016-10-10 20:00");
//        //insert("유저2", 28, "각기도");
//        //insert("유저3", 28, "각도기");
//
//        //일단주석
//        // 2. 수정하기
//        //update("유저1", 58); // 나이만 수정하기
//
//        // 3. 삭제하기
//        delete("유저2");
//
//        // 4. 조회하기
//        List<YuzaRanking> list =   select();
//        String listStr = "";
//        for (int i=0 ; i < yuzaRanking.size() ; i++){
//            listStr += "\n tid: " + yuzaRanking.get(i).getTid() + ", name : "
//                    + yuzaRanking.get(i).getName() + ", yuzaid : " + yuzaRanking.get(i).getRet_time()
//                    + ", time : " + yuzaRanking.get(i).getRet_km();
//        }
//        sqlText = (TextView) findViewById(R.id.textSql);
//        sqlText.setText(listStr);
    }

//    // insert
    public void insert(int yuzaid, String name, float km, String time, String date) {
        db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능

        ContentValues values = new ContentValues();
        // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
        // 데이터의 삽입은 put을 이용한다.
        values.put("yuza_id", yuzaid);
        values.put("name", name);
        values.put("ret_km", km);
        values.put("ret_time", time);
        values.put("ret_date", date);
        db.insert("yuzaranking", null, values); // 테이블/널컬럼핵/데이터(널컬럼핵=디폴트)
        // tip : 마우스를 db.insert에 올려보면 매개변수가 어떤 것이 와야 하는지 알 수 있다.
    }
//
//    // update 일단 주석
//  /*  public void update (String name, int age) {
//        db = helper.getWritableDatabase(); //db 객체를 얻어온다. 쓰기가능
//
//        ContentValues values = new ContentValues();
//        values.put("age", age);    //age 값을 수정
//        db.update("student", values, "name=?", new String[]{name});
//
//          new String[] {name} 이런 간략화 형태가 자바에서 가능하다
//          당연하지만, 별도로 String[] asdf = {name} 후 사용하는 것도 동일한 결과가 나온다.
//
//
//
//         public int update (String table,
//         ContentValues values, String whereClause, String[] whereArgs)
//
//    }*/
//
//
//    // delete
//    public void delete (String yuzaid) {
//        db = helper.getWritableDatabase();
//        db.delete("yuzaranking", "yuza_id=?", new String[]{yuzaid});
//        Log.i("db", yuzaid + "정상적으로 삭제 되었습니다.");
//    }

    private void setArchiveImageView(int level) {
        archiveImage.setImageResource(archiveImageLevelId[level]);
    }

    private void setAchiveTextView(int level) {
        archiveTitle.append(yuzaRanking.size()).append("개의 유적에 다녀왔어요.\n");
        archiveTitleText.setText(archiveTitle.toString());
        archiveInfoText.setText(archiveInfo[level]);
    }

    // select
    public List<YuzaRanking>  select() {

        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.query("yuzaranking", null, null, null, null, null, null);

        /*
         * 위 결과는 select * from student 가 된다. Cursor는 DB결과를 저장한다. public Cursor
         * query (String table, String[] columns, String selection, String[]
         * selectionArgs, String groupBy, String having, String orderBy)
         */
        yuzaRanking = new ArrayList<YuzaRanking>();
        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            int tid = c.getInt(c.getColumnIndex("tid"));
            String name = c.getString(c.getColumnIndex("name"));
            int yuzaid = c.getInt(c.getColumnIndex("yuza_id"));
            String time = c.getString(c.getColumnIndex("ret_time"));
            float km = c.getInt(c.getColumnIndex("ret_km"));
            String date = c.getString(c.getColumnIndex("ret_date"));

            YuzaRanking tmp = new YuzaRanking();
            tmp.setTid(tid);
            tmp.setName(name);
            tmp.setRet_km(km);
            tmp.setRet_time(time);
            tmp.setRet_date(date);
            yuzaRanking.add(tmp);

        }

        for (int i=0 ; i < yuzaRanking.size() ; i++) {
            Log.i("db", "tid: " + yuzaRanking.get(i).getTid() + ", name : " + yuzaRanking.get(i).getName() + ", yuzaid : " + yuzaRanking.get(i).getRet_time()
                    + ", time : " + yuzaRanking.get(i).getRet_km());

        }
        return yuzaRanking;
    }

    private class ViewHolder {
        public ImageView mArchiveImageIcon;

        public TextView mArchiveName;

        public TextView mArchiveDate;

        public TextView mArchiveTime;

        public TextView mArchiveDistance;
    }

    private class ListViewAdapter extends BaseAdapter implements Filterable {
        private Context mContext = null;
        Activity context;

        public ListViewAdapter(Activity context) {
            super();
            this.context = context;
        }

        @Override
        public int getCount() {
            return mArchiveListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mArchiveListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(YuzaRanking yuzaRanking){
            mArchiveListData.add(yuzaRanking);
        }

        public void remove(int position){
            mArchiveStudentList.remove(position);
            dataChange();
        }

        public void dataChange(){
            mArchiveAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ArchiveActivity.ViewHolder holder;
            YuzaRanking mData = mArchiveListData.get(position);

            if (convertView == null) {
                holder = new ArchiveActivity.ViewHolder();

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_archive, null);

                holder.mArchiveImageIcon = (ImageView) convertView.findViewById(R.id.archiveImageIcon);
                holder.mArchiveName = (TextView) convertView.findViewById(R.id.archiveName);
                holder.mArchiveDate = (TextView) convertView.findViewById(R.id.archiveDate);
                holder.mArchiveTime = (TextView) convertView.findViewById(R.id.archiveTime);
                holder.mArchiveDistance = (TextView) convertView.findViewById(R.id.archiveDistance);

                convertView.setTag(holder);

            }else{
                holder = (ArchiveActivity.ViewHolder) convertView.getTag();
            }



            if (mData.name != null) {
                holder.mArchiveImageIcon.setVisibility(View.VISIBLE);
                holder.mArchiveImageIcon.setImageResource(R.drawable.yuza_stamp_archive);
            }else{
                holder.mArchiveImageIcon.setVisibility(View.GONE);
            }

            holder.mArchiveName.setText(mData.getName());
            holder.mArchiveDate.setText("정복 날짜 : " + mData.getRet_date());
            holder.mArchiveTime.setText("걸린 시간 : " + mData.getRet_time());
            holder.mArchiveDistance.setText("이동 거리 : " + String.valueOf(mData.getRet_km()) + "km");

            return convertView;
        }
        public Filter getFilter() {
            Filter filter = new Filter() {


                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    Log.d("yuza","ret performFiltering()");
                    FilterResults results = new FilterResults();
                    Log.d("yuza","constraint "+constraint.toString());

                    if("".equals(constraint.toString())){
                        return results;
                    }
                    String word = constraint.toString();

                    ArrayList<Student> FilteredList = new ArrayList<Student>();
                    if (constraint == null || constraint.length() == 0) {
                        // No filter implemented we return all the list
                        results.values = mArchiveStudentList;
                        results.count = mArchiveStudentList.size();

                    } else {

                        for (int i = 0; i < mArchiveStudentList.size(); i++) {
                            Log.d("yuza","mListData "+mArchiveStudentList.get(i).getName());
                            String data = mArchiveStudentList.get(i).getName();

                            if (data.toLowerCase().contains(
                                    word.toLowerCase())) {
                                FilteredList.add(mArchiveStudentList.get(i));
                                Log.d("yuza","mListData "+mArchiveStudentList.get(i).getName());
                            }
                        }
                        results.values = FilteredList;
                        results.count = FilteredList.size();
                    }

                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint,
                                              Filter.FilterResults results) {
                    Log.d("yuza","ret publishResults()"+results);
                    mArchiveListData = (ArrayList<YuzaRanking>) results.values;
                    if(mArchiveListData != null){
                        for (int i = 0; i < mArchiveListData.size(); i++) {
                            Log.d("yuza","ret "+mArchiveListData.get(i).getName());
                        }
                        ArchiveActivity.this.mArchiveAdapter.notifyDataSetChanged();
                    }

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

