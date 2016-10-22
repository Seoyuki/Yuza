package seoyuki.yuza;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class SettingActivity extends AppCompatActivity {

    SettingOnClickListener settingOnClickListener = new SettingOnClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("설정 바꾸기");

        initSettingBtn();
        //initSettingToggleBtn();

    }


    private static final int[] mArraySettingBtn = { // 버튼 ID 배열
            R.id.settingBtn1,
            R.id.settingBtn2,
            R.id.settingBtn3
    };
/*
    private static final int[] mArraySettingToggleBtn = { // 토글 ID 배열
            R.id.settingToggleBtn1
    };
*/
    private void initSettingBtn() {
        for (int SettingBtn : mArraySettingBtn) {
            Button settingButton = (Button) findViewById(SettingBtn);
            settingButton.setOnClickListener(settingOnClickListener);
        }
    }
/*
    private void initSettingToggleBtn() {
        for (int SettingToggleBtn : mArraySettingToggleBtn) {
            ToggleButton settingToggleButton = (ToggleButton) findViewById(SettingToggleBtn);
            settingToggleButton.setOnClickListener(settingOnClickListener);
        }
    }
*/
    class SettingOnClickListener implements View.OnClickListener {

        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {

                //토글1
           /*     case R.id.settingToggleBtn1:
                    intent = new Intent(getBaseContext(), TestBtnActivity.class);
                    startActivity(intent);
                    break;
            */
                //업적 초기화
                case R.id.settingBtn1:
                    intent = new Intent(getBaseContext(), TestBtnActivity.class);
                    startActivity(intent);
                    break;

                //네이버 까페 연동
                case R.id.settingBtn2:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cafe.naver.com/yujainseoul"));
                    startActivity(intent);
                    break;

                //개발자 한마디
                case R.id.settingBtn3:
                    intent = new Intent(getBaseContext(), DevMsgActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}