package seoyuki.yuza;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
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


        if(chkGpsService()){
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

    //GPS 설정 체크
    private boolean chkGpsService() {

        String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

            // GPS OFF 일때 Dialog 표시
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle("위치 서비스 설정");
            gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?");
            gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.runFinalizersOnExit(true);
                            System.exit(0);
                            return;
                        }
                    }).create().show();
            return false;

        } else {
            return true;
        }
    }
}
