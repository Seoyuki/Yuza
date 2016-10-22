package seoyuki.yuza;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingActivity extends AppCompatActivity {

    SettingOnClickListener settingOnClickListener = new SettingOnClickListener();
    SQLiteDatabase db;
    SqlLiteYuzaOpenHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("설정 바꾸기");

        initSettingBtn();
        //initSettingToggleBtn();

        helper = new SqlLiteYuzaOpenHelper(SettingActivity.this, // 현재 화면의 context
                "yuza.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호

    }

    public void deleteAll () {
        db = helper.getWritableDatabase();
        db.delete("yuzaranking",null,null);
        Toast.makeText(this, "업적이 초기화 되었습니다.", Toast.LENGTH_LONG).show();
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
                    DialoConfirm();
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

    //나중에 사용해볼것
    private void DialogHtmlView() {
        AlertDialog.Builder ab = new AlertDialog.Builder(SettingActivity.this);

        ab.setMessage(Html.fromHtml("<strong><font color=\"#ff0000\"> " + "Html 표현여부 "
                + "</font></strong><br>HTML 이 제대로 표현되는지 본다."));
        ab.setPositiveButton("ok", null);

        ab.show();
    }

    private void DialoConfirm(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("정말로 업적을 초기화 하시겠습니까.").setCancelable(
                false).setPositiveButton("예!!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteAll();
                    }
                }).setNegativeButton("아니오ㅜㅜ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("업적 초기화");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.yuza_stamp_big);
        alert.show();
    }
}