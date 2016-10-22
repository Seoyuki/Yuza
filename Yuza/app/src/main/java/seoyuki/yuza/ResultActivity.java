package seoyuki.yuza;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ResultActivity extends AppCompatActivity {
    String photoPath ="";
    Bitmap bitMap;
    private CallbackManager callbackManager;
    ShareDialog shareDialog;
    private Context mContext = this;
    private int rotation1 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                // 권한이 없을 때 요청
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(mContext, "카메라 관련 권한이 필요해요.", Toast.LENGTH_LONG).show();

                } else {
                    requestPermissions(
                            new String[] {Manifest.permission.CAMERA},
                            1);

                }
                return;
            }

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                // 권한이 없을 때 요청
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(mContext, "저장소 읽기 권한이 필요해요.", Toast.LENGTH_LONG).show();

                } else {
                    requestPermissions(
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            2);

                }
                return;
            }

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                // 권한이 없을 때 요청
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(mContext, "저장소 쓰기 권한이 필요해요.", Toast.LENGTH_LONG).show();

                } else {
                    requestPermissions(
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            3);

                }
                return;
            }
        }

        // 스테이터스바 숨기기
       //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("사진 확인");

        Intent intent = getIntent();
        photoPath = intent.getStringExtra("strParamName");

        rotation1 = intent.getIntExtra("rotation1",90);
        Log.d("youja", "test@@@@@@"+photoPath);
        BitmapFactory.Options options = new BitmapFactory.Options();
       // options.inSampleSize = 4;
        final Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);

        Matrix matrix = new Matrix();

        matrix.preRotate(rotation1);
        Bitmap adjustedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), matrix, true);

        Bitmap stamp = BitmapFactory.decodeResource(getResources(), R.drawable.stamp);

        //크기 리사이징 후 겹치기
        bitMap = overlayMark(adjustedBitmap ,resizeBitmap(stamp,500));

        ImageView img = (ImageView) findViewById(R.id.cameraView);
        img.setImageBitmap(bitMap);





        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button faceBookBtn1 = (Button) findViewById(R.id.faceBookBtn1);
        faceBookBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackManager = CallbackManager.Factory.create();
                // 페이스북 SDK 초기화
                FacebookSdk.sdkInitialize(getApplicationContext());
                FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));

                LoginManager.getInstance().logInWithReadPermissions(ResultActivity.this,Arrays.asList("public_profile", "email"));
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
                                    String filePath =  saveBitmaptoPng(bitMap);

                                    Log.i("yuja", "filePath: " + filePath);
                                 // finish();
                                    shareDialog = new ShareDialog(ResultActivity.this);
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    // options.inSampleSize = 4;
                                    final Bitmap bmp = BitmapFactory.decodeFile(filePath, options);


                                    SharePhoto photo = new SharePhoto.Builder()
                                            .setUserGenerated(true)
                                            .setBitmap(bmp)
                                            .setCaption("Latest score 하하하하하하")
                                            .build();
                                    SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo)
                                            .build();

                                    if (shareDialog.canShow(SharePhotoContent.class)){
                                        shareDialog.show(content);
                                    }
                                    else{
                                        Log.d("youja", "you cannot share photos :(");
                                    }

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
            }
        });

        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(photoPath);
                file.delete();
                saveBitmaptoPng(bitMap);

               Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        File file = new File(photoPath);
        file.delete();
        super.onBackPressed();
    }

    //비트맵 겹치기
    private Bitmap overlayMark(Bitmap bmp1, Bitmap bmp2)

    {

        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());

        Canvas canvas = new Canvas(bmOverlay);

        canvas.drawBitmap(bmp1, 0, 0, null);

        canvas.drawBitmap(bmp2, 100, 10, null);

        return bmOverlay;

    }

    //비트맵 사이즈 조절
    static public Bitmap resizeBitmap(Bitmap original,int resizeWidth) {

        double aspectRatio = (double) original.getHeight() / (double) original.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(original, resizeWidth, targetHeight, false);
        if (result != original) {
            original.recycle();
        }
        return result;
    }

    //비트맵 사진저장
    public  String saveBitmaptoPng(Bitmap bitmap){
        File file_path;

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "youja");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("youja", "failed to create directory");
            }
        }
        String file_name  = String.format(mediaStorageDir.getPath()+"/youja%d.png",
                System.currentTimeMillis());

        try{
            file_path = new File(file_name);

            FileOutputStream out = new FileOutputStream(file_path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            return file_name;
        }catch(FileNotFoundException exception){
            Log.e("FileNotFoundException", exception.getMessage());
        }catch(IOException exception){
            Log.e("IOException", exception.getMessage());
        }
       return file_name;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    // 권한 요청 결과에 따른 콜백 메소드 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "카메라 권한을 승인받았어요. 고마워요!", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this, "권한 거부됨.", Toast.LENGTH_LONG).show();

                }

                break;



            case 2:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "저장소 일기 권한을 승인받았어요. 고마워요!", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this, "권한 거부됨.", Toast.LENGTH_LONG).show();

                }

                break;



            case 3:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "저장소 쓰기 권한을 승인받았어요. 고마워요!", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this, "권한 거부됨.", Toast.LENGTH_LONG).show();

                }

                break;



        }
    }
}
