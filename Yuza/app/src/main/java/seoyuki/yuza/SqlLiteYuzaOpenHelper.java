package seoyuki.yuza;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SqlLiteYuzaOpenHelper extends SQLiteOpenHelper {

    public SqlLiteYuzaOpenHelper(Context context, String name,
                                 SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // SQLiteOpenHelper 가 최초 실행 되었을 때
        String sql = "create table yuzaranking (" +
                "tid integer primary key autoincrement, " +
                "yuza_id integer, " +
                "name text, " +
                "ret_time text, " +
                "ret_km real," +
                "ret_date text);";
        db.execSQL(sql);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db = 적용할 db, old/new 구 버전/신버전
        // TODO Auto-generated method stub
        /*
         * db 버전이 업그레이드 되었을 때 실행되는 메소드
         * 이 부분은 사용에 조심해야 하는 일이 많이 있다. 버전이 1인 사용자가 2로 바뀌면
         * 한번의 수정만 하면 되지만 버전이 3으로 되면 1인 사용자가 2, 3을 거쳐야 하고
         * 2인 사용자는 3 까지만 거치면 된다. 이렇게 증가할 수록 수정할 일이 많아지므로
         * 적절히 사용해야 하며 가능하면 최초 설계 시에 완벽을 기하는 것이 가장 좋을 것이다.
         * 테스트에서는 기존의 데이터를 모두 지우고 다시 만드는 형태로 하겠다.
         */

        String sql = "drop table if exists yuzaranking";
        db.execSQL(sql);

        onCreate(db); // 테이블을 지웠으므로 다시 테이블을 만들어주는 과정
    }
}
