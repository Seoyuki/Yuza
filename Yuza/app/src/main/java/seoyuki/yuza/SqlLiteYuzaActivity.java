package seoyuki.yuza;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SqlLiteYuzaActivity extends AppCompatActivity {

    SqlLiteYuzaActivity.testgOnClickListener listener = new SqlLiteYuzaActivity.testgOnClickListener();
    SQLiteDatabase db;
    SqlLiteYuzaOpenHelper helper;
    List<YuzaRanking> yuzaRanking;
    public TextView sqlText;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql);

        helper = new SqlLiteYuzaOpenHelper(SqlLiteYuzaActivity.this, // 현재 화면의 context
                "yuza.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호


        //deleteAll();

        // 1. 데이터 저장
        //this.insert(1,"유적지",100,"120분","2016-10-10 20:00");
        //this.insert(2,"유적지2",100,"120분","2016-10-10 20:00");


        //일단주석
        // 2. 수정하기
        //update("유저1", 58); // 나이만 수정하기

        // 3. 삭제하기
       // delete("유저2");

        // 4. 조회하기
        List<YuzaRanking> yuzaRanking =   select();
        String listStr = "";
        for (int i=0 ; i < yuzaRanking.size() ; i++) {
            listStr += "tid: " + yuzaRanking.get(i).getTid() + ", name : " + yuzaRanking.get(i).getName() + ", yuzaid : "+ yuzaRanking.get(i).getYuza_id()
                    + ", ret_time : "+ yuzaRanking.get(i).getRet_time()
                    + ", ret_km : " + yuzaRanking.get(i).getRet_km();

        }
        sqlText = (TextView) findViewById(R.id.textSql);
        sqlText.setText(listStr);

        initBtn();
    }

    // insert
    public void insert(int yuzaid, String name, float km, String time,String ret_date) {
        db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능

        ContentValues values = new ContentValues();
        // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
        // 데이터의 삽입은 put을 이용한다.
        values.put("yuza_id", yuzaid);
        values.put("name", name);
        values.put("ret_km", km);
        values.put("ret_time", time);
        values.put("ret_date", ret_date);
        db.insert("yuzaranking", null, values); // 테이블/널컬럼핵/데이터(널컬럼핵=디폴트)
        // tip : 마우스를 db.insert에 올려보면 매개변수가 어떤 것이 와야 하는지 알 수 있다.
    }

    // update 일단 주석
  /*  public void update (String name, int age) {
        db = helper.getWritableDatabase(); //db 객체를 얻어온다. 쓰기가능

        ContentValues values = new ContentValues();
        values.put("age", age);    //age 값을 수정
        db.update("student", values, "name=?", new String[]{name});

          new String[] {name} 이런 간략화 형태가 자바에서 가능하다
          당연하지만, 별도로 String[] asdf = {name} 후 사용하는 것도 동일한 결과가 나온다.



         public int update (String table,
         ContentValues values, String whereClause, String[] whereArgs)

    }*/


    // delete
    public void delete (String yuzaid) {
        db = helper.getWritableDatabase();
        db.delete("yuzaranking", "yuza_id=?", new String[]{yuzaid});
        Log.i("db", yuzaid + "정상적으로 삭제 되었습니다.");
    }

    public void deleteAll () {
        db = helper.getWritableDatabase();
        db.delete("yuzaranking",null,null);
        Log.i("db", "정상적으로 삭제 되었습니다.");
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
            String ret_date = c.getString(c.getColumnIndex("ret_date"));

            YuzaRanking tmp = new YuzaRanking();
            tmp.setTid(tid);
            tmp.setYuza_id(yuzaid);
            tmp.setName(name);
            tmp.setRet_km(km);
            tmp.setRet_time(time);
            tmp.setRet_date(ret_date);

            yuzaRanking.add(tmp);

        }
        return yuzaRanking;
    }
    private static final int[] mArraySettingBtn = { // 버튼 ID 배열
            R.id.insertBtn1,
            R.id.selectList1,
            R.id.delBtn1,
            R.id.delBtn2
    };
    private void initBtn() {
        for (int SettingBtn : mArraySettingBtn) {
            Button settingButton = (Button) findViewById(SettingBtn);
            settingButton.setOnClickListener(listener);
        }
    }
    class testgOnClickListener implements View.OnClickListener {

        String listStr = "";
        public void onClick(View v) {

            switch (v.getId()) {

                //랜덤 인설트
                case R.id.insertBtn1:
                    int t =  (int) (Math.random() * 10000) + 1;
                    insert(t,"유적지"+t,100+t,"120분","2016-10-10 20:00");
                    yuzaRanking =   select();
                    listStr = "";
                    for (int i=0 ; i < yuzaRanking.size() ; i++) {
                        listStr += "tid: " + yuzaRanking.get(i).getTid() + ", name : " + yuzaRanking.get(i).getName() + ", yuzaid : "+ yuzaRanking.get(i).getYuza_id()
                                + ", ret_time : "+ yuzaRanking.get(i).getRet_time()
                                + ", ret_km : " + yuzaRanking.get(i).getRet_km();

                    }
                    sqlText = (TextView) findViewById(R.id.textSql);
                    sqlText.setText(listStr);
                    break;

                //리스트불러오기
                case R.id.selectList1:
                    yuzaRanking =   select();
                    listStr = "";
                    for (int i=0 ; i < yuzaRanking.size() ; i++) {
                        listStr += "tid: " + yuzaRanking.get(i).getTid() + ", name : " + yuzaRanking.get(i).getName() + ", yuzaid : "+ yuzaRanking.get(i).getYuza_id()
                                + ", ret_time : "+ yuzaRanking.get(i).getRet_time()
                                + ", ret_km : " + yuzaRanking.get(i).getRet_km();

                    }
                    sqlText = (TextView) findViewById(R.id.textSql);
                    sqlText.setText(listStr);
                    break;

                //텍스트값의 로우 삭제
                case R.id.delBtn1:
                    EditText deltxt = (EditText) findViewById(R.id.delEditText);
                    String str =deltxt.getText().toString();
                    if(str != null &&  !"".equals(str)){
                        delete(str);
                    }

                    yuzaRanking =   select();
                    listStr = "";
                    for (int i=0 ; i < yuzaRanking.size() ; i++) {
                        listStr += "tid: " + yuzaRanking.get(i).getTid() + ", name : " + yuzaRanking.get(i).getName() + ", yuzaid : "+ yuzaRanking.get(i).getYuza_id()
                                + ", ret_time : "+ yuzaRanking.get(i).getRet_time()
                                + ", ret_km : " + yuzaRanking.get(i).getRet_km();

                    }
                    sqlText = (TextView) findViewById(R.id.textSql);
                    sqlText.setText(listStr);
                    break;

                case R.id.delBtn2:
                    deleteAll();
                    sqlText.setText("");
                    break;
            }
        }
    }

}