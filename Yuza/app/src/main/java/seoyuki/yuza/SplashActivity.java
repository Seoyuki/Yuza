package seoyuki.yuza;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookSdk;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 페이스북 SDK 초기화
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));

        setContentView(R.layout.activity_splash);

        // 스플래시 처리 핸들러
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 엑세스 토큰 확인 부분 (추가)
                Boolean isValidAccessTocken = false;

                if (isValidAccessTocken) {
                    // 엑세스 토큰이 유효 할 경우 메인 액티비티로 이동
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                } else {
                    // 엑세스 토큰이 유효하지 않을 경우 로그인 액티비티로 이동
                    startActivity(new Intent(SplashActivity.this, TestBtnActivity.class));
                }

                // 스플래시 액티비티 종료
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
