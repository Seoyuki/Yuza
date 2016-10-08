package seoyuki.yuza;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URLDecoder;

/**
 * Created by Administrator on 2016-10-02.
 */

public class DetailActivity extends Activity {
    String decodeStr;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        TextView name_text = (TextView) findViewById(R.id.textView);
        TextView address_text = (TextView) findViewById(R.id.textView2);
        TextView content_text = (TextView) findViewById(R.id.textView3);
        ImageView image_text = (ImageView) findViewById(R.id.imageView);
        // 이전 액티비티로부터 넘어온 데이터를 꺼낸다.

        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        String content = getIntent().getStringExtra("content");
        String image = getIntent().getStringExtra("image");
        //넘어온 데이터를 String값으로 받아온다.
    try {
            decodeStr = URLDecoder.decode(image, "UTF-8");
        //image를 디코딩한다.
        } catch(Exception e) {

        }
    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        //AsyncTask를 사용해 url 이미지 보여주기
        ImageView bmImage;
        public ImageDownloader(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap mIcon = null;
            try {
                InputStream is = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(is);
                //디코딩된 소스를 비트맵에 넣는다.
            } catch (Exception e) {
            }
            return mIcon;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            //결과를 비트맵에 저장한다.
        }
    }
        name_text.setText(name);
        address_text.setText(address);
        content_text.setText(content);
        new ImageDownloader(image_text).execute(decodeStr);
        //받아온 내용들을 뿌려준다.
    }
}



