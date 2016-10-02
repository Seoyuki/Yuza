package seoyuki.yuza;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        String photoPath = intent.getStringExtra("strParamName");
        Log.d("youja", "test@@@@@@"+photoPath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        final Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);

        Matrix matrix = new Matrix();
        matrix.preRotate(90);
        Bitmap adjustedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), matrix, true);

        Bitmap stemp = BitmapFactory.decodeResource(getResources(), R.drawable.stemp);

        Bitmap bitMap = overlayMark(adjustedBitmap ,resizeBitmap(stemp,150));

        ImageView img = (ImageView) findViewById(R.id.cameraView);
        img.setImageBitmap(bitMap);
    }

    //isVerticalMode = true를 주면 세로로, false를주면 가로로 합친다. 리턴은 Bitmap
    private Bitmap combineImage(Bitmap first, Bitmap second, boolean isVerticalMode){
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inDither = true;
        option.inPurgeable = true;

        Bitmap bitmap = null;
        if(isVerticalMode)
            bitmap = Bitmap.createScaledBitmap(first, first.getWidth(), first.getHeight()+second.getHeight(), true);
        else
            bitmap = Bitmap.createScaledBitmap(first, first.getWidth()+second.getWidth(), first.getHeight(), true);

        Paint p = new Paint();
        p.setDither(true);
        p.setFlags(Paint.ANTI_ALIAS_FLAG);

        Canvas c = new Canvas(bitmap);
        c.drawBitmap(first, 0, 0, p);
        if(isVerticalMode)
            c.drawBitmap(second, 0, first.getHeight(), p);
        else
            c.drawBitmap(second, first.getWidth(), 0, p);

        first.recycle();
        second.recycle();

        return bitmap;
    }


    //비트맵 겹치기
    private Bitmap overlayMark(Bitmap bmp1, Bitmap bmp2)

    {

        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());

        Canvas canvas = new Canvas(bmOverlay);

        canvas.drawBitmap(bmp1, 0, 0, null);

        canvas.drawBitmap(bmp2, 10, 10, null);

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
}
