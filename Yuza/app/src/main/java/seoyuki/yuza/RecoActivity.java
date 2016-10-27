package seoyuki.yuza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecoActivity extends Activity {

    // List view
    private ListView lv;

    // Listview Adapter
    ArrayAdapter<String> recoadapter;

    public ListViewAdapter recoadapter2;
    // Search EditText
    EditText inputSearch;

    private ArrayList<Student> mListData = new ArrayList<Student>();
    // ArrayList for Listview
    ArrayList<Student> productList;

    Bundle extras = new Bundle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reco);

        // Listview Data
        productList = xmlParser();
        recoadapter2 = new ListViewAdapter(this);
        lv = (ListView) findViewById(R.id.listView);
        inputSearch = (EditText) findViewById(R.id.editText);

        int r1 = (int) (Math.random() * productList.size());
        int r2 = (int) (Math.random() * productList.size());
        int r3 = (int) (Math.random() * productList.size());

//        String[] data = new String[productList.size()];

        for (int i = 0; i < productList.size(); i++) {

                if (r1 == i) {
                    recoadapter2.addItem(getResources().getDrawable(R.drawable.yuza_bike_recommendation),
                            productList.get(i).getName(),
                            productList.get(i).getAddress(), productList.get(i).getWido(), productList.get(i).getKyungdo());
//                data[i] = productList.get(i).getName();
                }
                if (r1 == i) {
                    recoadapter2.addItem(getResources().getDrawable(R.drawable.yuza_bike_recommendation),
                            productList.get(i).getName(),
                            productList.get(i).getAddress(), productList.get(i).getWido(), productList.get(i).getKyungdo());
//                           data[i] = productList.get(i).getName();
                }
                if (r3 == i) {
                    recoadapter2.addItem(getResources().getDrawable(R.drawable.yuza_bike_recommendation),
                            productList.get(i).getName(),
                            productList.get(i).getAddress(), productList.get(i).getWido(), productList.get(i).getKyungdo());
//                       data[i] = productList.get(i).getName();
                }

            Student check = productList.get(i);
            //추천지에서 r1, r2, r3로 3개만 뽑아냈는데 productList로 i값을 얻어오니 3개보다 값이 커서 오류가 나게 됩니다.
            //여기에 어떤 타 변수를 지정해서 넣어야 하는 것 일까요?
            //데이터 넘어가는 과정은 SearchActivity와 동일합니다.
            extras.putString("name", check.getName());
            extras.putString("address", check.getAddress());
            extras.putString("content", check.getContent());
            extras.putString("image", check.getImage());
            extras.putString("wido", check.getWido());
            extras.putString("kyungdo", check.getKyungdo());
        }

        lv.setAdapter(recoadapter2);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(RecoActivity.this, DetailActivity.class);
                intent.putExtras(extras);
                startActivity(intent);


            }
        });
    }

    public void textonClick(View v) {
        Toast toast = Toast.makeText(this, "안녕하세요", Toast.LENGTH_LONG);
                toast.show();

        Intent intent = new Intent(RecoActivity.this, SearchActivity.class);
        startActivity(intent);
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