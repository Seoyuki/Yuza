package seoyuki.yuza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        final SharedPreferences checkFirstInfoEnd = getSharedPreferences("checkFirstInfoEnd", MODE_PRIVATE);

        // 배경을 주황색으로
        RelativeLayout introLayout = (RelativeLayout) findViewById(R.id.intro_layout);
        introLayout.setBackgroundColor(Color.rgb(255, 127, 0));

        // 소스 정보 : http://blog.naver.com/alens82/220720141554
        // 액션바 숨기기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 스테이터스바 숨기기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 2초 후 인트로 제거
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 메인 액티비티로 넘어가기
                // 일단 테스트로

                boolean isFirstInfoEnd = checkFirstInfoEnd.getBoolean("isFirstInfoEnd", false);

                if (isFirstInfoEnd) {
                    Intent intent = new Intent(Intro.this, TestBtnActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Intro.this, FirstInfoActivity.class);
                    startActivity(intent);
                }

                finish(); // 인트로 종료
            }
        }, 2000);

    }
}
