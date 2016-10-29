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

public class SearchActivity extends Activity {

    List<Student> searchList = new ArrayList<Student>();

    // List view
    private ListView lv;
    // Listview Adapter
    public ListViewAdapter searchAdapter;
    // Search EditText
    EditText inputSearch;

    private ArrayList<Student> mListData = new ArrayList<Student>();
    // ArrayList for Listview
    ArrayList<Student> productList;
    EditText searchEdit;
    Bundle extras = new Bundle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reco);

        // Listview Data
        productList = xmlParser();
        searchEdit = (EditText)findViewById(R.id.editText);
        searchAdapter = new ListViewAdapter(this);
        lv = (ListView) findViewById(R.id.listView);
        inputSearch = (EditText) findViewById(R.id.editText);
        Student recoObj = null;
        Drawable image = null;
        for (int i = 0; i < productList.size(); i++) {
            recoObj = productList.get(i);
            if (i % 2 == 0)  {
                image = getResources().getDrawable(R.drawable.yuza_bike_recommendation);
            recoObj.setIcon(image);
            } else {
                image = getResources().getDrawable(R.drawable.archivebtn);
                recoObj.setIcon(image);
            }
            searchAdapter.addItem(recoObj);
            searchList.add(recoObj);
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
        //검색창
        searchEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

//                String text = searchEdit.getText().toString()
//                        .toLowerCase(Locale.getDefault());
//                searchAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                Toast toast = Toast.makeText(SearchActivity.this, arg0.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
    public void searchOnclick(View v) {

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