package seoyuki.yuza;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends Activity {

    CustomListViewAdapter adapter;
    ListView lv;
    List<Student> list;
    EditText inputSearch;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        list = xmlParser(); // xml파싱
       // Student searchObj = null;
        Drawable image = null;
        for (int i = 1; i < list.size(); i++) {
            //searchObj = list.get(i);
            image = getResources().getDrawable(R.drawable.yuza_bike);
            list.get(i).setIcon(image);
        }

        inputSearch = (EditText) findViewById(R.id.editText);
        lv = (ListView) findViewById(R.id.listView);
         adapter = new CustomListViewAdapter(this,
                R.layout.listview_item, list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Bundle extras = new Bundle();
                //i값을 얻어서 데이터 전송
                //이 부분에서i값이 for문 i값이 아니라 위 int i값으로 새롭게 불러와지기 떄문에 법천사지부터 1번부터 불러오게 됩니다.
                // 그래서 번들을 위로 빼고
                Student check = list.get(i);
                extras.putString("name", check.getName());
                extras.putString("address", check.getAddress());
                extras.putString("content", check.getContent());
                extras.putString("image", check.getImage());
                extras.putString("wido", check.getWido());
                extras.putString("kyungdo", check.getKyungdo());
                //extra.putString부분을 for문안에 넣고 extras를 배열로 변형 뒤 해보았는데 맨 뒤 정보가 나오게 되었습니다.
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtras(extras);
                startActivity(intent);


            }
        });

        /**
         * Enabling Search Filter
         * */
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                SearchActivity.this.adapter.getFilter().filter(cs);
                //검색기능
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
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