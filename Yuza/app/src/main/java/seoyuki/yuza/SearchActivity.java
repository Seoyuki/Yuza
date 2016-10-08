package seoyuki.yuza;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

public class SearchActivity extends AppCompatActivity{
    ArrayList<Student> list;
    ListView listView;
    EditText editText;
    ArrayAdapter<String> arrad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        listView = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.editText);

        list = xmlParser();
        String[] data = new String[list.size()];
        String[] recommend = new String[list.size()];

            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i).getName();
            }

            arrad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);

        listView.setAdapter(arrad);

            listView.setTextFilterEnabled(true);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    SearchActivity.this.arrad.getFilter().filter(s);
                }
            });
        listView.setOnItemClickListener(mItemClickListener);
        }

    public void textonClick(View v) {
        Toast toast = Toast.makeText(this, "안녕하세요", Toast.LENGTH_LONG);
        toast.show();
        //listView.setAdapter(arrad);
    }

    /*  ListView의 아이템 중 하나가 클릭될 때 호출되는 메소드
      첫번째  : 클릭된 아이템을 보여주고 있는 AdapterView 객체(여기서는 ListView객체)
      두번째  : 클릭된 아이템 뷰
      세번째  : 클릭된 아이템의 위치(ListView이 첫번째 아이템(가장위쪽)부터 차례대로 0,1,2,3.....)
      네번재  : 클릭된 아이템의 아이디(특별한 설정이 없다면 세번째 파라이터인 position과 같은 값)*/
    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
    };

    //xmlParser를 사용해 xml 파싱하기
    private ArrayList<Student> xmlParser() {
        ArrayList<Student> arrayList = new ArrayList<Student>();
        InputStream is = getResources().openRawResource(R.raw.testvalues);
        // xmlPullParser
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));
            int eventType = parser.getEventType();
            Student student = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if (startTag.equals("historic")) {
                            student = new Student();
                        }
                       // if (startTag.equals("id")) {
                       // student.setId(parser.nextText());
                       //}
                        if (startTag.equals("name")) {
                            student.setName(parser.nextText());
                        }
                        if (startTag.equals("content")) {
                            student.setContent(parser.nextText());
                        }
                        if (startTag.equals("address")) {
                            student.setAddress(parser.nextText());
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