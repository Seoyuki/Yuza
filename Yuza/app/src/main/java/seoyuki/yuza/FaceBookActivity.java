package seoyuki.yuza;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
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
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.util.Arrays;

public class FaceBookActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    ShareDialog shareDialog;
    String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);


        Intent intent = getIntent();
        filePath = intent.getStringExtra("filePath");

        Log.d("youja", "filePath: " + filePath);

        callbackManager = CallbackManager.Factory.create();
        // 페이스북 SDK 초기화
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));

        LoginManager.getInstance().logInWithReadPermissions(FaceBookActivity.this,Arrays.asList("public_profile", "email"));

        Log.d("youja", "youja: youjayouja");

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult result) {

                GraphRequest request;
                request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        if (response.getError() != null) {
                            Log.d("youja", "user: " + user.toString());
                            Log.d("youja", "AccessToken: " + result.getAccessToken().getToken());

                        } else {
                            Log.d("youja", "user: " + user.toString());
                            Log.d("youja", "AccessToken: " + result.getAccessToken().getToken());
                            setResult(RESULT_OK);
                            shareDialog = new ShareDialog(FaceBookActivity.this);
                            // finish();
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
}
