package seoyuki.yuza;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CameraActivity extends Activity {

    @SuppressWarnings("deprecation")
    Camera mCamera;
    ImageView imageBtn; // 카메라 버튼(ImageView 인스턴스)
    String str;
    private CameraPreview mPreview;
    private Context mContext = this;
    private SurfaceHolder mHolder;
    SurfaceView surfaceView;
    @SuppressWarnings("deprecation")
    Camera.PictureCallback jpegCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        // 스테이터스바 숨기기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        surfaceView = (SurfaceView) findViewById(R.id.cameraSurf);
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(surfaceListener);

        jpegCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "youja");
                    Toast.makeText(getApplicationContext(),
                            mediaStorageDir.getPath(), Toast.LENGTH_LONG).show();
                    if (! mediaStorageDir.exists()){
                        if (! mediaStorageDir.mkdirs()){
                            Log.d("youja", "failed to create directory");

                        }
                    }

                    str = String.format(mediaStorageDir.getPath()+"/youja%d.png",
                            System.currentTimeMillis());

                    File mediaFile;
                    Log.d("youja", str);
                    mediaFile = new File(str);
                    outStream = new FileOutputStream(mediaFile);

                    outStream.write(data);
                    outStream.close();
                }

                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                finally {
                }

                Toast.makeText(getApplicationContext(),
                        "Picture Saved", Toast.LENGTH_LONG).show();

                mCamera.stopPreview();

                Intent intent = new Intent(CameraActivity.this,
                        ResultActivity.class);
                intent.putExtra("strParamName", str);
                startActivity(intent);
            }
        };

        imageBtn = (ImageView) findViewById(R.id.cameraCaptureImageBtn);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCamera != null){
                    mCamera.takePicture(null, null, jpegCallback);
                }else{
                    Log.d("youja","testddddd");
                }

            }
        });
    }
    /** 카메라 인스턴스를 안전하게 획득합니다 */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch (Exception e){
        }
        return c;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
    @Override
    public void onPause(){
        super.onPause();
        // 보통 안쓰는 객체는 onDestroy에서 해제 되지만 카메라는 확실히 제거해주는게 안전하다.
    }

    private SurfaceHolder.Callback surfaceListener = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                 mCamera = getCameraInstance();
                if(mCamera != null){
                    mCamera.stopPreview();
                }

                Camera.Parameters parameters = mCamera.getParameters();
                if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    parameters.set("orientation", "portrait");
                    mCamera.setDisplayOrientation(90);
                    parameters.setRotation(90);
                } else {
                    parameters.set("orientation", "landscape");
                    mCamera.setDisplayOrientation(0);
                    parameters.setRotation(0);
                }
                mCamera.setParameters(parameters);

                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d("youja", "Error setting camera preview: " + e.getMessage());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // 프리뷰를 회전시키거나 변경시 처리를 여기서 해준다.
            // 프리뷰 변경시에는 먼저 프리뷰를 멈춘다음 변경해야한다.
            if (mHolder.getSurface() == null){
                // 프리뷰가 존재하지 않을때
                return;
            }
            // 우선 멈춘다
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // 프리뷰가 존재조차 하지 않는 경우다
            }

            Camera.Parameters parameters = mCamera.getParameters();
            Camera.Size size = getBestPreviewSize(width, height);
            parameters.setPreviewSize(size.width, size.height);
            mCamera.setParameters(parameters);
            // 프리뷰 변경, 처리 등을 여기서 해준다.

            // 새로 변경된 설정으로 프리뷰를 재생성한다
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){
                Log.d("youja", "Error starting camera preview: " + e.getMessage());
            }
        }
        private Camera.Size getBestPreviewSize(int width, int height)
        {
            Camera.Size result=null;
            Camera.Parameters p = mCamera.getParameters();
            for (Camera.Size size : p.getSupportedPreviewSizes()) {
                if (size.width<=width && size.height<=height) {
                    if (result==null) {
                        result=size;
                    } else {
                        int resultArea=result.width*result.height;
                        int newArea=size.width*size.height;

                        if (newArea>resultArea) {
                            result=size;
                        }
                    }
                }
            }
            return result;

        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // 프리뷰 제거시 카메라 사용도 끝났다고 간주하여 리소스를 전부 반환한다
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }
    };
}
