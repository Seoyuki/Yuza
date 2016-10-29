package seoyuki.yuza;

import android.app.Activity;
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

import com.bumptech.glide.Glide;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
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
    private ArchiveActivity.ListViewAdapter mArchiveAdapter = null;
    ArrayList<Student> mArchiveStudentList = new ArrayList<Student>();
    private ArrayList<YuzaRanking> mArchiveListData = new ArrayList<YuzaRanking>();

    private final int YUZA_MAX_NUMBER = 99;
    private int archiveNumber = 0;
    private String decodeImageURL = "";

    private TextView archiveNumberView;
    private TextView archiveTitleText;
    private TextView archiveCheerText;

    private ImageView archiveNoImg;
    private TextView archiveNoMsg;

    private int n;
    private StringBuilder stringBuilder = new StringBuilder();
    private final String ARCHIVE_TITLE_STRING[] = {
            "설레는 시작!",
            "다리가 딴딴! :)",
            "서울유적사랑xD",
            "자전거여행자>o<",
            "계속 두근두근!",
            "조금 더 가까이:)",
            "행복한 자전거^o^"
    };
    private final String ARCHIVE_CHEER_STRING[] = {
            "뿌듯할 것 같아요. xD",
            "오늘 컨디션은 괜찮아요?",
            "친구에게 같이 가자고 해볼까요!?",
            "조금 힘들었죠? :) 화이팅!",
            "오늘도 좋은 하루!",
            "추억이 새록새록...",
            "기분 좋은 음악도 함께라면!? ^^"
    };
    private final String ARCHIVE_GRADE_STRING[] = {
            "유자 초보자입니다. ^^\n",
            "유자 중수예요. :)\n",
            "유자 고수예요. 짝짝짝!\n",
            "유자 초고수예요. 우와!\n",
            "유자 초인(^^;)이에요.\n",
            "유자 마스터예요. >o<\n",
            "유자의 수호신이에요. ㅋㅋ^^\n"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        mArchiveListView = (ListView) findViewById(R.id.archiveListView);
        // searchText = (TextView)findViewById(R.id.archiveText);
        mArchiveAdapter = new ListViewAdapter(this);

        archiveNumberView = (TextView) findViewById(R.id.archiveNumber);
        archiveTitleText = (TextView) findViewById(R.id.archiveTitleText);
        archiveCheerText = (TextView) findViewById(R.id.archiveCheerText);

        archiveNoImg = (ImageView) findViewById(R.id.archiveNoImg);
        archiveNoMsg = (TextView) findViewById(R.id.archiveNoMsg);

        helper = new SqlLiteYuzaOpenHelper(this, // 현재 화면의 context
                "yuza.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호

        final List<YuzaRanking> archiveList = select(); // 현재 완료한 곳 리스트 불러오기
        archiveNumber = archiveList.size();

        mArchiveStudentList = xmlParser();

        // 도착한 곳이 있으면 archiveNoImg, archiveNoMsg를 숨기고 archiveRecord는 보여준다
        if (archiveNumber != 0) {
            archiveNoImg.setVisibility(View.GONE);
            archiveNoMsg.setVisibility(View.GONE);
        }

        // 도착한 곳의 숫자만큼 업적 이미지와 업적 관련 텍스트 보여주기
        setArchiveNumberView(archiveNumber);
        setArchiveTitleTextView(archiveNumber);
        setArchiveCheerTextView(archiveNumber);

        for (YuzaRanking i : archiveList) {
            mArchiveAdapter.addItem(i); }

        mArchiveListView.setAdapter(mArchiveAdapter);

        mArchiveListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){

                Student data = mArchiveStudentList.get((mArchiveListData.get(position).getYuza_id())-1);
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
//    public void insert(int yuzaid, String name, float km, String time, String date) {
//        db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능
//
//        ContentValues values = new ContentValues();
//        // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
//        // 데이터의 삽입은 put을 이용한다.
//        values.put("yuza_id", yuzaid);
//        values.put("name", name);
//        values.put("ret_km", km);
//        values.put("ret_time", time);
//        values.put("ret_date", date);
//        db.insert("yuzaranking", null, values); // 테이블/널컬럼핵/데이터(널컬럼핵=디폴트)
//        // tip : 마우스를 db.insert에 올려보면 매개변수가 어떤 것이 와야 하는지 알 수 있다.
//    }
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

    private void setArchiveNumberView(int number) {
        archiveNumberView.setText(String.valueOf(number)); // 전체 도착 개수 출력
    }

    private void setArchiveTitleTextView(int number) {
        archiveTitleText.setText(ARCHIVE_TITLE_STRING[number/15]);
    }

    private void setArchiveCheerTextView(int number) {

        stringBuilder.append("단계 ").append((number/15)).append(", ");
        stringBuilder.append(ARCHIVE_GRADE_STRING[number/15]);

        n = (int) (Math.random() * 6);
        switch (number) {
            case YUZA_MAX_NUMBER:
                stringBuilder
                        .append("모든 유적에 다녀왔어요!\n");
                stringBuilder.append(ARCHIVE_CHEER_STRING[n]);
                break;
            case 0:
                stringBuilder
                        .append("이제부터 시작! 유적은 모두 ")
                        .append(YUZA_MAX_NUMBER + "개예요.\n");
                break;
            default:
                stringBuilder
                        .append(YUZA_MAX_NUMBER)
                        .append("개의 유적 중 ")
                        .append(YUZA_MAX_NUMBER - number)
                        .append("개가 남았어요. ^^\n");
                stringBuilder.append(ARCHIVE_CHEER_STRING[n]);
        }

        archiveCheerText.setText(stringBuilder);
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
        yuzaRanking = new ArrayList<>();
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
            tmp.setYuza_id(yuzaid);
            tmp.setRet_km(km);
            tmp.setRet_time(time);
            tmp.setRet_date(date);
            yuzaRanking.add(tmp);

        }

        for (int i=0 ; i < yuzaRanking.size() ; i++) {
            Log.i("db", "tid: " + yuzaRanking.get(i).getTid() + ", name : " + yuzaRanking.get(i).getName() + ", yuzaid : " + yuzaRanking.get(i).getYuza_id()
                    + ", time : " + yuzaRanking.get(i).getRet_time());

        }
        return yuzaRanking;
    }

    private class ViewHolder {
        private ImageView mArchiveImageIcon;

        private TextView mArchiveName;

        private TextView mArchiveDate;

        private TextView mArchiveTime;

        private TextView mArchiveDistance;

        //private int id;
    }

    private class ListViewAdapter extends BaseAdapter implements Filterable {
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
//                if (isYellow) {
//                    convertView.setBackgroundColor(getColor(R.color.colorPrimaryLight));
//                    isYellow = !isYellow;
//                } else {
//                    convertView.setBackgroundColor(getColor(android.R.color.white));
//                    isYellow = !isYellow;
//                }

                convertView.setTag(holder);

            }else{
                holder = (ArchiveActivity.ViewHolder) convertView.getTag();
            }

            if (mData != null) {
                decodeImageURL = URLDecoder.decode(mArchiveStudentList.get(mData.getYuza_id()-1).getImage());
                Glide.with(getBaseContext()).load(decodeImageURL).into(holder.mArchiveImageIcon);

                holder.mArchiveName.setText(mData.getName());
                holder.mArchiveDate.setText(mData.getRet_date() + "분에");
                holder.mArchiveTime.setText(mData.getRet_time() + "동안");
                holder.mArchiveDistance.setText(String.valueOf(mData.getRet_km()) + "km 달림");

            }else{
                holder.mArchiveImageIcon.setVisibility(View.GONE);
            }



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
        ArrayList<Student> arrayList = new ArrayList<>();
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
                            assert student != null;
                            student.setId(parser.next());
                        }
                        if (startTag.equals("name")) {
                            assert student != null;
                            student.setName(parser.nextText());
                        }
                        if (startTag.equals("address")) {
                            assert student != null;
                            student.setAddress(parser.nextText());
                        }
                        if (startTag.equals("content")) {
                            assert student != null;
                            student.setContent(parser.nextText());
                        }
                        if (startTag.equals("wido")) {
                            assert student != null;
                            student.setWido(parser.nextText());
                        }
                        if (startTag.equals("kyungdo")) {
                            assert student != null;
                            student.setKyungdo(parser.nextText());
                        }
                        if (startTag.equals("image")) {
                            assert student != null;
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


        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

}