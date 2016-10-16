package seoyuki.yuza;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by jaewon on 2016-10-15.
 */
public class LocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //방송을 잘 잡으면 밑에 로그 한번 찍어줌.
        // 지피에스 위치가 변해서 127, 37.5 로 되면 DDMS 에 아래 로그가 찍힘으로 확인 가능
        Log.d("도착", "실패ㅠㅠㅠ");
        Toast.makeText(context, "목표 지점에 접근중..", Toast.LENGTH_LONG).show();
        SqlLiteYuzaActivity sql = new SqlLiteYuzaActivity();

    }

}
