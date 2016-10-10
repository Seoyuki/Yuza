package seoyuki.yuza;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("설정 바꾸기");

//리스트뷰 참고 : http://recipes4dev.tistory.com/43
//        ListView listview ;
//        ListViewAdapter adapter;
//
//        // Adapter 생성
//        adapter = new ListViewAdapter() ;
//
//        // 리스트뷰 참조 및 Adapter달기
//        listview = (ListView) findViewById(R.id.listview1);
//        listview.setAdapter(adapter);

    }
}
