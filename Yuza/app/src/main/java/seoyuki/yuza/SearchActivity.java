package seoyuki.yuza;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    List<Student> searchList = null;
    boolean isAllList = true;
    // List view
    private ListView lv;
    // Listview Adapter
    public ListViewAdapter searchAdapter;
    List<Student> productList;
    EditText searchEdit;
    Bundle extras = new Bundle();

    SQLiteDatabase db;
    SqlLiteYuzaOpenHelper helper;
    List<YuzaRanking> yuzaRanking;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reco);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("유적 찾아보기");

        helper = new SqlLiteYuzaOpenHelper(this, // 현재 화면의 context
                "yuza.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호
        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.query("yuzaranking", null, null, null, null, null, null);
        yuzaRanking = new ArrayList<>();
        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            int yuzaid = c.getInt(c.getColumnIndex("yuza_id"));
            int tid = c.getInt(c.getColumnIndex("tid"));
            YuzaRanking tmp = new YuzaRanking();
            tmp.setYuza_id(yuzaid);
            tmp.setTid(tid);
            yuzaRanking.add(tmp);

        }

        // Listview Data
        productList = xmlParser();
        searchEdit = (EditText)findViewById(R.id.editText);
        searchAdapter = new ListViewAdapter(this);
        lv = (ListView) findViewById(R.id.listView);


        int r1 = (int) (Math.random() * productList.size()) + 1;
        int r2 = (int) (Math.random() * productList.size()) + 1;
        int r3 = (int) (Math.random() * productList.size()) + 1;

        Student recoObj = null;
        Drawable image = null;
        searchList = new ArrayList<Student>();
        for (int i = 0; i < productList.size(); i++) {
            recoObj = productList.get(i);
            if (r1 == i) {
                image = getResources().getDrawable(R.drawable.yuza_bike_recommendation);
                recoObj.setIcon(image);
                searchAdapter.addItem(recoObj);
                searchList.add(recoObj);
            }
            if (r2 == i) {
                image = getResources().getDrawable(R.drawable.yuza_bike_recommendation);
                recoObj.setIcon(image);
                searchAdapter.addItem(recoObj);
                searchList.add(recoObj);
            }
            if (r3 == i) {
                image = getResources().getDrawable(R.drawable.yuza_bike_recommendation);
                recoObj.setIcon(image);
                searchAdapter.addItem(recoObj);
                searchList.add(recoObj);
            }
        }
        lv.setAdapter(searchAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Student getrecoList = searchList.get(i);
                extras.putString("name", getrecoList.getName());
                extras.putString("address", getrecoList.getAddress());
                extras.putString("content", getrecoList.getContent());
                extras.putString("image", getrecoList.getImage());
                extras.putString("wido", getrecoList.getWido());
                extras.putString("kyungdo", getrecoList.getKyungdo());
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        //에디터 클릭시
        searchEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//모두 뿌리기위해
                if(isAllList){//최초 한번을위해
                    Student recoObj = null;
                    Drawable image ;
                    searchList = new ArrayList<Student>();
                    searchAdapter  = new ListViewAdapter(SearchActivity.this);
                    for (int i = 0; i < productList.size(); i++) {
                        recoObj = productList.get(i);
                        image = getResources().getDrawable(R.drawable.ic_launcher);
                        recoObj.setIcon(image);
                        for (int j = 0; j < yuzaRanking.size(); j++) {
                            if (yuzaRanking.get(j).getYuza_id() == recoObj.getId())  {//비교
                                image = getResources().getDrawable(R.drawable.yuza_stamp_archive);
                                recoObj.setIcon(image);
                            }
                        }
                        productList.get(i).setIcon(image);
                        searchAdapter.addItem(recoObj);
                        searchList.add(recoObj);
                    }
                    lv.setAdapter(searchAdapter);
                    isAllList = false;
                }
            }

        });

        //검색창
        searchEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                String searchWord = arg0.toString();
                Student recoObj = null;
                Drawable image ;
                searchList = new ArrayList<Student>();
                searchAdapter  = new ListViewAdapter(SearchActivity.this);
                for (int i = 0; i < productList.size(); i++) {
                    recoObj = productList.get(i);
                    String objName = recoObj.getName();
                    if (objName.contains(searchWord)) {
                        searchAdapter.addItem(recoObj);
                        searchList.add(recoObj);
                    }
                    image = getResources().getDrawable(R.drawable.ic_launcher);
                    recoObj.setIcon(image);
                    for (int j = 0; j < yuzaRanking.size(); j++) {
                        if (yuzaRanking.get(j).getYuza_id() == recoObj.getId())  {//비교
                            image = getResources().getDrawable(R.drawable.yuza_stamp_archive);
                            recoObj.setIcon(image);
                        }
                    }
                    productList.get(i).setIcon(image);
                }
                lv.setAdapter(searchAdapter);
            }
        });
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
                            student.setId(Integer.valueOf(parser.nextText()));;
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


    @Override
    protected void onDestroy() {
        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();

        super.onDestroy();
    }
}