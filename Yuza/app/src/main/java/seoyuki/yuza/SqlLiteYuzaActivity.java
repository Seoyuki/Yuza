package seoyuki.yuza;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SqlLiteYuzaActivity extends AppCompatActivity {
    SQLiteDatabase db;
    SqlLiteYuzaOpenHelper helper;
    List<YuzaRanking> yuzaRanking;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql);

        helper = new SqlLiteYuzaOpenHelper(SqlLiteYuzaActivity.this, // 현재 화면의 context
                "yuza.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호

        // 1. 데이터 저장
        //insert("유저1", 18, "경기도");
        //insert("유저2", 28, "각기도");
        //insert("유저3", 28, "각도기");

        //일단주석
        // 2. 수정하기
        //update("유저1", 58); // 나이만 수정하기

        // 3. 삭제하기
        delete("유저2");

        // 4. 조회하기
        select();
    }

    // insert
    public void insert(int yuzaid, String name, float km, String time) {
        db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능

        ContentValues values = new ContentValues();
        // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
        // 데이터의 삽입은 put을 이용한다.
        values.put("yuza_id", yuzaid);
        values.put("name", name);
        values.put("ret_km", km);
        values.put("ret_time", time);
        db.insert("student", null, values); // 테이블/널컬럼핵/데이터(널컬럼핵=디폴트)
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
        db.delete("student", "yuza_id=?", new String[]{yuzaid});
        Log.i("db", yuzaid + "정상적으로 삭제 되었습니다.");
    }

    // select
    public void select() {

        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.query("student", null, null, null, null, null, null);

        /*
         * 위 결과는 select * from student 가 된다. Cursor는 DB결과를 저장한다. public Cursor
         * query (String table, String[] columns, String selection, String[]
         * selectionArgs, String groupBy, String having, String orderBy)
         */
        yuzaRanking = new ArrayList<YuzaRanking>();
        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            int _id = c.getInt(c.getColumnIndex("_id"));
            String name = c.getString(c.getColumnIndex("name"));
            int yuzaid = c.getInt(c.getColumnIndex("yuzaid"));
            String time = c.getString(c.getColumnIndex("time"));
            float km = c.getInt(c.getColumnIndex("km"));
            Log.i("db", "id: " + _id + ", name : " + name + ", yuzaid : " + yuzaid
                    + ", time : " + time);
            YuzaRanking tmp = new YuzaRanking();

           // yuzaRanking.add()

        }
    }
}