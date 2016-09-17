package seoyuki.yuza;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends Activity {

    @SuppressWarnings("deprecation")
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Button button;
    String str;

    @SuppressWarnings("deprecation")
    Camera.PictureCallback jpegCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getWindow().setFormat(PixelFormat.UNKNOWN);

        surfaceView = (SurfaceView) findViewById(R.id.cameraSurf);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceListener);
       // surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);



        jpegCallback = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    str = String.format("/sdcard/%d.jpg",
                            System.currentTimeMillis());
                    outStream = new FileOutputStream(str);

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
                refreshCamera();

                Intent intent = new Intent(CameraActivity.this,
                        ResultActivity.class);
                intent.putExtra("strParamName", str);
                startActivity(intent);
            }
        };

        button = (Button) findViewById(R.id.cameraCaptureBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(camera != null){
                    camera.takePicture(null, null, jpegCallback);
                }else{
                    Log.d("yuja","testddddd");
                }

            }
        });
    }



    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        }

        catch (Exception e) {
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private SurfaceHolder.Callback surfaceListener = new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {

            camera = Camera.open();
            camera.stopPreview();
            camera.setDisplayOrientation(90); // potrait
            camera.setDisplayOrientation(180); // landscape
            Camera.Parameters param = camera.getParameters();
            param.setRotation(90);

            camera.setParameters(param);

            try {

                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (Exception e) {
                System.err.println(e);
                return;
            }
        }


        public void surfaceChanged(SurfaceHolder holder,
                                   int format, int width, int height) {
            refreshCamera();
        }


        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    };
}
