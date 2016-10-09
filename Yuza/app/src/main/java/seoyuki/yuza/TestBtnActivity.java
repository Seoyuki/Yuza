package seoyuki.yuza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;

public class TestBtnActivity extends Activity implements View.OnClickListener {
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        initView();
    }



    private static final int[] mArrayButton = {
            R.id.mainBtn,
            R.id.faceBookBtn,
            R.id.cameraBtn,
            R.id.searchBtn,
            R.id.settingBtn
    };

    /**
     * initView - 버튼에 대한 리스너를 등록한다.
     */
    private void initView() {
        for (int btnView : mArrayButton) {
            Button ViewButton = (Button) findViewById(btnView);
            ViewButton.setOnClickListener(this);
        }
    }

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            //메인으로가기
            case R.id.mainBtn:
                Log.d("yuja", "main start: ");
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

            //페이스북 연동
            case R.id.faceBookBtn:
                callbackManager = CallbackManager.Factory.create();
                // 페이스북 SDK 초기화
                FacebookSdk.sdkInitialize(getApplicationContext());
                FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));

                LoginManager.getInstance().logInWithReadPermissions(this,
                        Arrays.asList("public_profile", "email"));


                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(final LoginResult result) {

                        GraphRequest request;
                        request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject user, GraphResponse response) {
                                if (response.getError() != null) {
                                    Log.i("yuja", "user: " + user.toString());
                                    Log.i("yuja", "AccessToken: " + result.getAccessToken().getToken());

                                } else {
                                    Log.i("yuja", "user: " + user.toString());
                                    Log.i("yuja", "AccessToken: " + result.getAccessToken().getToken());
                                    setResult(RESULT_OK);
                                    finish();
                                    Intent intent = new Intent(
                                            getApplicationContext(), // 현재 화면의 제어권자
                                            SignInActivity.class); // 다음 넘어갈 클래스 지정
                                    startActivity(intent); // 다음 화면으로 넘어간다
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e("yuja", "Error: " + error);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        finish();
                    }
                });
                break;
            //카메라기능
            case R.id.cameraBtn:
                Log.d("yuja", "cameraBtn start: ");
                intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);

                break;
            //검색기능
            case R.id.searchBtn:
                Log.d("yuja", "searchBtn start: ");
                intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);

                break;
            //설정화면
            case R.id.settingBtn:
                Log.d("yuja", "settingBtn start: ");
                intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);

                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
