package seoyuki.yuza;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

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

public class ResultActivity extends Activity {
    String photoPath ="";
    Bitmap bitMap;
    private CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        // 스테이터스바 숨기기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        photoPath = intent.getStringExtra("strParamName");
        Log.d("youja", "test@@@@@@"+photoPath);
        BitmapFactory.Options options = new BitmapFactory.Options();
       // options.inSampleSize = 4;
        final Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);

        Matrix matrix = new Matrix();
        matrix.preRotate(90);
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
}
