package seoyuki.yuza;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

public class SignInActivity extends AppCompatActivity {
    ShareDialog shareDialog;
    private SurfaceHolder mHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button b = (Button)findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();

            }
        });

        shareDialog = new ShareDialog(this);



        Button btn2 = (Button) findViewById(R.id.button4); //xml 버튼을 찾습니다.
        //버튼 클릭 메소드를 생성 합니다.
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .setContentTitle("test")
                        .setImageUrl(Uri.parse("https://static.xx.fbcdn.net/rsrc.php/v2/yb/r/zpNMvPYcpG3.png"))
                        .setContentDescription("test입니다")
                        .build();
                shareDialog.show(content);//공유 이미지 함수를 호출 합니다.



            }
        });

        Button btn = (Button) findViewById(R.id.button3); //xml 버튼을 찾습니다.
        //버튼 클릭 메소드를 생성 합니다.
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

               ImageView image =(ImageView)findViewById(R.id.imageview1);
                //image.setImageResource(R.drawable.imgsam);
                BitmapDrawable d = (BitmapDrawable)((ImageView) findViewById(R.id.imageview1)).getDrawable();
                Bitmap b = d.getBitmap();

                //사진 공유
               // Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.imgSam);


               /* SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(b)
                        .setCaption("Give me my codez or I will ... you know, do that thing you don't like!")
                        .build();

                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();

                ShareApi.share(content, null);*/


                SharePhoto photo = new SharePhoto.Builder()
                        .setUserGenerated(true)
                        .setBitmap(b)
                        .setCaption("Latest score 하하하하하하")
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo)
                        .build();

                if (shareDialog.canShow(SharePhotoContent.class)){
                    shareDialog.show(content);
                }
                else{
                    Log.d("Activity", "you cannot share photos :(");
                }

            }
        });


    }



}
