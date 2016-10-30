package seoyuki.yuza;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.URLDecoder;

/**
 * Created by Administrator on 2016-10-02.
 */

public class DetailActivity extends Activity {
    String decodeStr = "";
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        TextView name_text = (TextView) findViewById(R.id.textView);
        TextView address_text = (TextView) findViewById(R.id.textView2);
        TextView content_text = (TextView) findViewById(R.id.textView3);
        ImageView image_text = (ImageView) findViewById(R.id.imageView);
        ImageView image_start = (ImageView) findViewById(R.id.detailStartImage);

        TextView detailBottomMsg = (TextView) findViewById(R.id.detailBottomMsg); // 최하단 메시지


        // 이전 액티비티로부터 넘어온 데이터를 꺼낸다.

       final String name = getIntent().getStringExtra("name");
        final String address = getIntent().getStringExtra("address");
        final String content = getIntent().getStringExtra("content");
        final String wido = getIntent().getStringExtra("wido");
        final String kyungdo = getIntent().getStringExtra("kyungdo");
        final String id = getIntent().getStringExtra("id");
        LogManager.printLog(wido+"::"+kyungdo);
        String image = getIntent().getStringExtra("image");


        try {
                 decodeStr = URLDecoder.decode(image, "UTF-8");
        } catch(Exception e) {

        }
        //넘어온 데이터를 String값으로 받아온다.
//          class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
//           //AsyncTask를 사용해 url 이미지 보여주기
//                           ImageView bmImage;
//                    public ImageDownloader(ImageView bmImage) {
//                            this.bmImage = bmImage;
//                        }
//
//                    @Override
//                    protected Bitmap doInBackground(String... params) {
//                            String url = params[0];
//                            Bitmap mIcon = null;
//                            try {
//                                    InputStream is = new java.net.URL(url).openStream();
//                                    mIcon = BitmapFactory.decodeStream(is);
//                                    //디코딩된 소스를 비트맵에 넣는다.
//                                        } catch (Exception e) {
//                                }
//                           return mIcon;
//                        }
//                    @Override
//                    protected void onPostExecute(Bitmap result) {
//                            bmImage.setImageBitmap(result);
//                            //결과를 비트맵에 저장한다.
//                                }
//                }

        name_text.setText(name);
        address_text.setText(address);
        content_text.setText(content);
        image_start.setImageResource(R.drawable.detail_start);
        image_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 유적지 길찾기 코드
                Bundle extras = new Bundle();
                extras.putString("mokwido", wido);
                extras.putString("mokkyungdo",kyungdo);
                extras.putString("mokid",id);
                extras.putString("mokname",name);
                // 인텐트를 생성한다.
                // 컨텍스트로 현재 액티비티를, 생성할 액티비티로 DetailActivity 를 지정한다.
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                // 위에서 만든 Bundle을 인텐트에 넣는다.
                intent.putExtras(extras);
                // 액티비티를 생성한다.
                startActivity(intent);
                finish();
            }
        });
//        new ImageDownloader(image_text).execute(decodeStr);
            Glide.with(getBaseContext()).load(decodeStr).into(image_text);
            //받아온 내용들을 뿌려준다.


        detailBottomMsg.setText("여행할 목적지가 있다는 것은 좋은 일이다.\n그러나 중요한 것은 여행 자체다.\n\n어슐러 K. 르 귄(Ursula Kroeber Le Guin)");
    }


    @Override
    protected void onDestroy() {
        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();

        super.onDestroy();
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(DetailActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}




