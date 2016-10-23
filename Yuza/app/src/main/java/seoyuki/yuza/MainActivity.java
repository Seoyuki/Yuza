package seoyuki.yuza;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.skp.Tmap.TMapCircle;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapGpsManager.onLocationChangedCallback;
import com.skp.Tmap.TMapInfo;
import com.skp.Tmap.TMapLabelInfo;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapMarkerItem2;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapTapi;
import com.skp.Tmap.TMapView;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static seoyuki.yuza.R.layout.dialog;

public class MainActivity extends BaseActivity implements onLocationChangedCallback, TMapView.OnCalloutRightButtonClickCallback, LocationListener {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    String mokswido;
    String mokskyungdo;
    public String wido2;                        //목적지 위치 담는 변수
    public String kyungdo2;                     //복정지 위치 담는 변수
    String wido;                                   //목적지 상세가기 버튼선택시 목적지 위치를 담기 위한 변수
    String kyungdo;                                //목적지 상세가기 버튼선택시 목적지 위치를 담기 위한 변수
    Double latitude = 0.0;                         //현재 위치를 담는 변수
    Double longitude = 0.0;                        //현재 위치를 담는 변수
    Calendar now = Calendar.getInstance();          //현재 시간
    int hour = 0;                                   //시
    int min = 0;                                    //분
    int sec = 0;                                    //초
    int hours = 0;                                  //도착 시간
    int mins = 0;                                   //도착 분
    int secs = 0;                                   //도착 초
    private static final String PROX_ALERT_INTENT = "com.javacodegeeks.android.lbs.ProximityAlert"; //목적지 도착 알림에 쓰이는 변수
    private static final long POINT_RADIUS = 1000; // in Meters                                           //목적지 도착 알림시 범위
    private static final long PROX_ALERT_EXPIRATION = -1;                                               //목적지 도착 알림창 띄우는 시간
    LocationReceiver receivers = new LocationReceiver();                                                    //목적지로 도착 완료 했을때 실행되는 클래스
    TMapMarkerItem item2 = new TMapMarkerItem();                                                            //지도에 마커를 찍기 위한 마커 생성
    Student student = null;                                                                                 //xml파싱을 위한 변수
    List<Student> marker;                                                                                    //xml파싱을 위한 변수
    Student[] stu;                                                                                           //마커에 정보를 담기위한 변수
    private static final String TAG = "ProximityTest";                                                  //목적지 도착 방송 태그
    private final String POI_REACHED =              // 공중파 방송의 채널 같은 역할. 임의로 정함.
            "com.example.proximitytest.POI_REACHED";    //
    private PendingIntent proximityIntent;                                                                 //목적지 도착 알림시에 쓰이는 PendingIntent
    private CallbackManager callbackManager;                                                                //권한 관련 변수?
    ShareDialog shareDialog;                                                                                   //alert창

    @Override
    public void onLocationChange(Location location) {                                                       //위치 변경시 실행되는 메서드
        Log.d("안녕","그래안녕");
    }

    private TMapView mMapView = null;                                                                       //지도 생성
    private Context mContext;                                                                               //context
    private ArrayList<Bitmap> mOverlayList;
    private ImageOverlay mOverlay;
    public static String mApiKey = "66800ce1-b178-3464-b52b-74cb4998e20a"; // 발급받은 appKey
    public static String mBizAppID; // 발급받은 BizAppID (TMapTapi로 TMap앱 연동을 할 때 BizAppID 꼭 필요)
    private int m_nCurrentZoomLevel = 0;
    private static int mMarkerID;                                                       //마커 아이디
    ArrayList<String> mArrayMarkerID;
    DialogInterface mArriveDialog = null; // 얼럿을 담는 인터페이스
    @Override
    public void onCalloutRightButton(TMapMarkerItem markerItem) {
        int counts = Integer.parseInt(markerItem.getID()) - 1;                          //선택한 마커의 아이디(숫자)
        wido = stu[counts].getWido();                                                     //선택한 마커의 위도
        kyungdo = stu[counts].getKyungdo();                                                //선택한 마커의 경도
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);                //선택한 마커의 정보를 넘기기 위한 인텐드
        intent.putExtra("content", markerItem.getName());                                   //선택한 마커의 내용
        intent.putExtra("address", markerItem.getCalloutSubTitle());                        //선택한 마커의 주소
        intent.putExtra("name", markerItem.getCalloutTitle());                              //선택한 마커의 이름
        intent.putExtra("image", stu[Integer.parseInt(markerItem.getID()) - 1].getImage());//선택한 마커의 이미지
        intent.putExtra("id", markerItem.getID());                                          //선택한 마커의 아이디
        intent.putExtra("wido", wido);                                                      //선택한 마커의 위도
        intent.putExtra("kyungdo", kyungdo);                                                //선택한 마커의 경도를 EXTRA에 담는다
        startActivity(intent);                                                                  //intent실행
        finish();                                                                                //메인 종료
    }
    private LocationManager locationManager;                                                    //변수 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mMapView = new TMapView(this);
        addView(mMapView);
        configureMapView();
        mokswido = getIntent().getStringExtra("mokwido");                                       //목적지 위도를 담는다
        mokskyungdo = getIntent().getStringExtra("mokkyungdo");                                 //목적지 경도를 담는다
        ImageView img1 = (ImageView) findViewById(R.id.achievementImageView);
        ImageView img2 = (ImageView) findViewById(R.id.searchImageView);
        ImageView img3 = (ImageView) findViewById(R.id.settingImageView);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArchiveActivity.class);
                startActivity(intent);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("yuja", "searchBtn start: ");
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);


            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);


            }
        });




        mMarkerID = 0;                                                                                  //초기화

        showMarkerPoint();                                                                              //마커를 찍는다
        mMapView.setTMapLogoPosition(TMapView.TMapLogoPositon.POSITION_BOTTOMRIGHT);//tmap 위치 변경
        mMapView.setBicycleFacilityInfo(true);                                          //자전거 시설물 표시 여부

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // 권한이 없을 때 요청
                    if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) {
                        Toast.makeText(mContext, "위치 관련 권한이 필요해요.", Toast.LENGTH_LONG).show();

                    } else {
                        requestPermissions(
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                1);
                    }
                    return;
                }
            }

        } catch (Exception e) {

        } finally {
            Location lastLocation = null;        //마지막으로 받았던 위치정보를 담을 변수
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//위치 서비스 실행
            if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null || locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {//gps가 켜저 있으면
                lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);      //gps로 위치 검색
                if (lastLocation == null) {                                                                 //gps가 잡히지 않을때
                    lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);  //network로 실행
                }
                latitude = lastLocation.getLatitude();                                                      //현재위치 위도
                longitude = lastLocation.getLongitude();                                                    //현재 위치 경도
                mMapView.setCenterPoint(longitude, latitude);                                               //지도의 중앙을 현재위치로
                mMapView.setLocationPoint(longitude, latitude);
            } else {
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "GPS를 켜주시기바랍니다.", Snackbar.LENGTH_INDEFINITE).setAction("GPS켜기", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        }).show();
                    }
                });
            }

//            latitude = lastLocation.getLatitude();                                                      //현재위치 위도
//            longitude = lastLocation.getLongitude();                                                    //현재 위치 경도
//            mMapView.setCenterPoint(longitude, latitude);                                               //지도의 중앙을 현재위치로
//            mMapView.setLocationPoint(longitude, latitude);                                             //현재위치를 티맵의 현재 위치로

        }

        mMapView.setZoomLevel(17);                                                              //지도 확대 레벨
        mMapView.setIconVisibility(true);                                                       //현재위치 표시 여부
        wido2 = getIntent().getStringExtra("mokwido");                                          //목적지 위도를 받음
        kyungdo2 = getIntent().getStringExtra("mokkyungdo");                                    //목적지 경도를 받음
        if (wido2 != null) {                                                                        //목적지 정보가 있을때
            Double wi = Double.parseDouble(wido2);                                                  //변수 변환
            Double kyu = Double.parseDouble(kyungdo2);
            mMapView.setCompassMode(true);                                                          //나침반 모드 실행(지도가 돌아간다)
            mMapView.setTrackingMode(true);                                                         //지도가 현재 위치를 중심으로 변경
            TMapPoint point1 = new TMapPoint(wi, kyu);                                              //목적지 설정
            TMapPoint point2 = new TMapPoint(latitude, longitude);                                  //출발지 설정
            TMapData tmapdata = new TMapData();                                                        //길찾기를 위한
            tmapdata.findPathDataWithType(TMapData.TMapPathType.BICYCLE_PATH, point2, point1,           //주어진 출도착지도 자전거길을 찾는다
                    new TMapData.FindPathDataListenerCallback() {
                        @Override
                        public void onFindPathData(TMapPolyLine polyLine) {
                            mMapView.addTMapPath(polyLine);
                        }
                    });
            for (int count = 0; count < marker.size(); count++) {                                       //현재 마커수만큼
                String imsy = String.format(count + "", mMarkerID++);
                mMapView.removeAllMarkerItem();                                                         //마커를 지운다
            }

        }

    }


    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {                                                  //위치가 변경되었을때
        Log.d("잘가","그래잘가");

        latitude = location.getLatitude();
        longitude = location.getLongitude();


        if (wido2 != null) {
            mMapView.setLocationPoint(longitude, latitude);
            mMapView.setCenterPoint(longitude, latitude);
            setupProximityAlert();
            Log.d("sds", longitude + " :ee " + latitude);
            LogManager.printLog(longitude + " :qq " + latitude);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    private void configureMapView() {
        mMapView.setSKPMapApiKey(mApiKey);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOverlayList != null) {
            mOverlayList.clear();
        }

        if (mArriveDialog != null) { // 얼럿 화면 null 처리
            mArriveDialog.dismiss();
            mArriveDialog = null;
        }
        try {

            unregisterReceiver(receivers);//실행했던 리시버 삭제
        }catch(IllegalArgumentException e){}
    }
    //xmlParser를 사용해 xml 파싱하기
    ArrayList<Student> xmlParser() {
        ArrayList<Student> arrayList = new ArrayList<Student>();
        InputStream is = getResources().openRawResource(R.raw.testvalues);
        // xmlPullParser
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));
            int eventType = parser.getEventType();


            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if (startTag.equals("historic")) {
                            student = new Student();
                        }
                        if (startTag.equals("name")) {
                            student.setName(parser.nextText());
                        }
                        if (startTag.equals("content")) {
                            student.setContent(parser.nextText());
                        }
                        if (startTag.equals("address")) {
                            student.setAddress(parser.nextText());
                        }
                        if (startTag.equals("image")) {
                            student.setImage(parser.nextText());
                        }
                        if (startTag.equals("wido")) {
                            student.setWido(parser.nextText());
                        }
                        if (startTag.equals("kyungdo")) {
                            student.setKyungdo(parser.nextText());
                        }
                        if (startTag.equals("id")) {
                            student.setId(Integer.parseInt(parser.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if (endTag.equals("historic")) {
                            arrayList.add(student);
                        }
                        break;
                }
                eventType = parser.next();
            }


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    /**
     * showMarkerPoint
     * 지도에 마커를 표출한다.
     */
    public void showMarkerPoint() {
        Bitmap bitmap = null;                                           //비트맵 사진 저장 변수
        TMapPoint point;                                                //위치를 담을 변수
        Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.i_go);//마커 오른쪽 사진
        String strID = String.format("pmarker%d", mMarkerID++);
        bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.i_go);
        item2.setCalloutRightButtonImage(bitmap_i);                                                 //오른쪽에 담길 사진 변경
        marker = xmlParser();
        stu = new Student[marker.size()];
        Double wi;
        Double kyung;
        for (int count = 0; count < marker.size(); count++) {                                       //마커의 수만큼 포인트를 만든다
            TMapMarkerItem item = new TMapMarkerItem();
        }
        for (int count = 0; count < marker.size(); count++) {                                       //마커마다 각 정보를 담는다
            stu[count] = marker.get(count);
            wi = Double.parseDouble(stu[count].getWido());
            kyung = Double.parseDouble(stu[count].getKyungdo());
            point = new TMapPoint(wi, kyung);
            item2 = new TMapMarkerItem();
            item2.setTMapPoint(point);
            item2.setName(stu[count].getContent());
            item2.setVisible(item2.VISIBLE);
            item2.setCalloutTitle(stu[count].getName());
            item2.setCalloutSubTitle(stu[count].getAddress());
            item2.setCanShowCallout(true);
            String imsy1 = stu[count].getImage();
            item2.setID(imsy1);
            bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.i_go);
            item2.setCalloutRightButtonImage(bitmap_i);
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.end);
            item2.setIcon(bitmap);
            strID = String.format(stu[count].getId() + "", mMarkerID++);
            mMapView.addMarkerItem(strID, item2);
        }
    }
    private void setupProximityAlert() {                                                            //목적지 도착여부를 알려주는 메서드
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);             //위치 정보
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {

            Log.d(TAG, "Registering ProximityAlert");
            //방송 시작, 방송이름은 POI_REACHED, 누가 이방송을 필요하는지는 관심없음. 그냥 보내는...
            Intent intent = new Intent(POI_REACHED);
            proximityIntent =
                    PendingIntent.getBroadcast(getApplicationContext(), 0, intent,
                            PendingIntent.FLAG_ONE_SHOT);
            //방송 조건, 목표위치에 50미터 안으로 이동하면 10초간 경보 라는 방송을 보냄. 방송이름은POI_REACHED
            //경보란게 먼지 모르겠음. 에뮬레이터에서 어떻게 확인 가능한지 모름.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
            }

            Double wi =Double.parseDouble(mokswido);                                                //목적지 변수
            Double ky = Double.parseDouble(mokskyungdo);                                             //목적지 변수
            locationManager.addProximityAlert(wi,ky
                    , 1000, 1000000,
                    proximityIntent);

            /*================================================================*/
            //시청자. POI_REACHED 이란 채널명으로 방송된 내용을 보려고 함.
            IntentFilter intentFilter = new IntentFilter(POI_REACHED);
            registerReceiver(receivers,intentFilter);

            /*================================================================*/
        } else {
            Log.d(TAG, "GPS_PROVIDER not available");
        }

    }

    public void showalert() {
        // 도착 완료 화면(얼럿) 코드, dialog.xml과 혼동되지 않도록 변수명을 alertDialog
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        // dialog.xml 파일을 인플레이션해서 보여준다
        // dialog.xml 파일의 세 버튼(카메라, 페이스북, 취소)에 대한 클릭 메소드는
        // alertDialogCameraClick, alertDialogFacebookClick, alertDialogCancelClick이며 가장 아래에 위치해 있다.
        // + TextView(위 세 버튼 이미지는 이미지뷰가 아닌 텍스트뷰의 백그라운드)에 직접 리스너를 달면 에러 발생
        View dialogView = inflater.inflate(dialog, null);
        alertDialog.setView(dialogView);
        alertDialog.create();

        mArriveDialog = alertDialog.show(); // DialogInterface에 alertDialog를 담아서 보여준다. 수명주기 코드를 위해 필요하다.
        showMarkerPoint();
    }

    class LocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //방송을 잘 잡으면 밑에 로그 한번 찍어줌.
            // 지피에스 위치가 변해서 127, 37.5 로 되면 DDMS 에 아래 로그가 찍힘으로 확인 가능
            Log.d("도착", "실패ㅠㅠㅠ");
            Toast.makeText(context, "목표 지점에 접근중..", Toast.LENGTH_SHORT).show();
          //  SqlLiteYuzaActivity sql = new SqlLiteYuzaActivity();
           showalert();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    public String saveBitmaptoPng(Bitmap bitmap) {
        File file_path;

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "youja");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("youja", "failed to create directory");
            }
        }
        String file_name = String.format(mediaStorageDir.getPath() + "/youja%d.png",
                System.currentTimeMillis());

        try {
            file_path = new File(file_name);

            FileOutputStream out = new FileOutputStream(file_path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            return file_name;
        } catch (FileNotFoundException exception) {
            Log.e("FileNotFoundException", exception.getMessage());
        } catch (IOException exception) {
            Log.e("IOException", exception.getMessage());
        }
        return file_name;
    }

    public void alertDialogCameraClick(View v) { // alertDialog(도착 얼럿)의 카메라 버튼 클릭 메소드

        Intent intent = new Intent(getBaseContext(), CameraActivity.class);
        startActivity(intent);

    }

    public void alertDialogFacebookClick(View v) { // alertDialog(도착 얼럿)의 페이스북 버튼 클릭 메소드

        callbackManager = CallbackManager.Factory.create();

        // 페이스북 SDK 초기화
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));

        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this,Arrays.asList("public_profile", "email"));
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
                            Bitmap stamp = BitmapFactory.decodeResource(getResources(), R.drawable.stamp);
                            String filePath =  saveBitmaptoPng(stamp);

                            Log.i("yuja", "filePath: " + filePath);
                            shareDialog = new ShareDialog(MainActivity.this);
                            BitmapFactory.Options options = new BitmapFactory.Options();
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
                            } else {
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

    public void alertDialogCancelClick(View v) { // alertDialog(도착 얼럿)의 취소 버튼 클릭 메소드

        Intent ints = new Intent(MainActivity.this, SqlLiteYuzaActivity.class);

        startActivity(ints);

    }
//

    // 권한 요청 결과에 따른 콜백 메소드 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "위치 권한을 승인받았어요. 고마워요!", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this, "권한 거부됨.", Toast.LENGTH_LONG).show();

                }

                return;
            }

        }
    }



}