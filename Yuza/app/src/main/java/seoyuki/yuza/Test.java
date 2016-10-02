package seoyuki.yuza;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import static seoyuki.yuza.R.xml.testvalues;

/**
 * Created by jaewon on 2016-10-02.
 */

public class Test extends AppCompatActivity implements TextWatcher{

    ListView listView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailtest);

        editText = (EditText)findViewById(R.id.editText);
        Intent intent = getIntent();
        editText.setText(intent.getStringExtra("what"));
        listView = (ListView)findViewById(R.id.listView);
        ArrayList<Student> list = xmlParser();
        String[] data = new String[list.size()];
        for(int i=0;i<list.size();i++) {
            data[i] = list.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                data);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        listView.setFilterText(editText.getText().toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(editText.getText().length() == 0) {
            listView.clearTextFilter();
        }
    }
    private ArrayList<Student> xmlParser()  {
    ArrayList<Student> arrayList = new ArrayList<Student>();
    InputStream is = getResources().openRawResource(R.raw.testvalues);

    // xmlPullParser
    try {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new InputStreamReader(is, "UTF-8"));
        int eventType = parser.getEventType();
        Student student = null;

        while(eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    String startTag = parser.getName();
                    if(startTag.equals("historic")) {
                        student = new Student();
                    }
                    if(startTag.equals("name")) {
                        student.setName(parser.nextText());
                    }
                    if(startTag.equals("content")) {
                        student.setContent(parser.nextText());
                    }
                    if(startTag.equals("address")) {
                        student.setAddress(parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    String endTag = parser.getName();
                    if(endTag.equals("historic")) {
                        arrayList.add(student);
                    }
                    break;
            }
            eventType = parser.next();
        }


    }catch(XmlPullParserException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

    return arrayList;
}
}


//    ListView listView;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.detailtest);
//        Intent intent = getIntent();
//
//        listView = (ListView)findViewById(R.id.listView);
//
//        ArrayList<Student> list = xmlParser();
//        String[] data = new String[list.size()];
//        for(int i=0;i<list.size();i++) {
//            data[i] = list.get(i).getName();
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
//                android.R.layout.simple_list_item_1,
//                data);
//        listView.setAdapter(adapter);
//            TextView tx = (TextView)findViewById(R.id.textView3);
//        adapter.getFilter();
//        switch(intent.getExtras().getString("what")){
//            case "chunggunsas": tx.setText(intent.getStringExtra("what")+"누름");break;
//            case "서울타워": tx.setText(intent.getStringExtra("what")+"누름");break;
//            case "을지로": tx.setText(intent.getStringExtra("what")+"누름");break;
//        }
//
//private ArrayList<Student> xmlParser()  {
//    ArrayList<Student> arrayList = new ArrayList<Student>();
//    InputStream is = getResources().openRawResource(R.raw.testvalues);
//
//    // xmlPullParser
//    try {
//        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//        XmlPullParser parser = factory.newPullParser();
//        parser.setInput(new InputStreamReader(is, "UTF-8"));
//        int eventType = parser.getEventType();
//        Student student = null;
//
//        while(eventType != XmlPullParser.END_DOCUMENT) {
//            switch (eventType) {
//                case XmlPullParser.START_TAG:
//                    String startTag = parser.getName();
//                    if(startTag.equals("historic")) {
//                        student = new Student();
//                    }
//                    if(startTag.equals("name")) {
//                        student.setName(parser.nextText());
//                    }
//                    if(startTag.equals("content")) {
//                        student.setContent(parser.nextText());
//                    }
//                    if(startTag.equals("address")) {
//                        student.setAddress(parser.nextText());
//                    }
//                    break;
//                case XmlPullParser.END_TAG:
//                    String endTag = parser.getName();
//                    if(endTag.equals("historic")) {
//                        arrayList.add(student);
//                    }
//                    break;
//            }
//            eventType = parser.next();
//        }
//
//
//    }catch(XmlPullParserException e) {
//        e.printStackTrace();
//    } catch (UnsupportedEncodingException e) {
//        e.printStackTrace();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//
//    return arrayList;
//}
//}
