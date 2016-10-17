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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity implements onLocationChangedCallback, TMapView.OnCalloutRightButtonClickCallback, LocationListener {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private static final String PROX_ALERT_INTENT = "com.javacodegeeks.android.lbs.ProximityAlert";
    private static final long POINT_RADIUS = 1000; // in Meters
    private static final long PROX_ALERT_EXPIRATION = -1;
    LocationReceiver receivers = new LocationReceiver();
    TMapMarkerItem item1 = new TMapMarkerItem();
    TMapMarkerItem item2 = new TMapMarkerItem();
    String decodeStr;
    Bitmap mIcon = null;
    int placenumber;
    Student student = null;
    List<Student> marker;
    Student[] stu;
    private static final String TAG = "ProximityTest";
    private final String POI_REACHED =              // 공중파 방송의 채널 같은 역할. 임의로 정함.
            "com.example.proximitytest.POI_REACHED";    //
    private PendingIntent proximityIntent;
    private CallbackManager callbackManager;
    ShareDialog shareDialog;

    private final double sampleLatitude = 127.028590;  // 목표 위치
    private final double sampleLongitude = 37.495083;
    String mokjuk = "덕수궁";

    @Override
    public void onLocationChange(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
    }

    private TMapView mMapView = null;

    private Context mContext;
    private ArrayList<Bitmap> mOverlayList;
    private ImageOverlay mOverlay;

    public static String mApiKey = "66800ce1-b178-3464-b52b-74cb4998e20a"; // 발급받은 appKey
    public static String mBizAppID; // 발급받은 BizAppID (TMapTapi로 TMap앱 연동을 할 때 BizAppID 꼭 필요)


    private int m_nCurrentZoomLevel = 0;
    private double m_Latitude = 0;
    private double m_Longitude = 0;
    private boolean m_bShowMapIcon = false;

    private boolean m_bTrafficeMode = false;
    private boolean m_bSightVisible = false;
    private boolean m_bTrackingMode = false;

    private boolean m_bOverlayMode = false;

    ArrayList<String> mArrayID;

    ArrayList<String> mArrayCircleID;
    private static int mCircleID;

    ArrayList<String> mArrayLineID;
    private static int mLineID;

    ArrayList<String> mArrayPolygonID;
    private static int mPolygonID;

    ArrayList<String> mArrayMarkerID;
    private static int mMarkerID;
    String[] item = new String[3];
    LocationManager mLocMan;

    String mProvider;
    int mCount;
    double s;
    double d;

    @Override
    public void onCalloutRightButton(TMapMarkerItem markerItem) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("content", markerItem.getName());
        intent.putExtra("address", markerItem.getCalloutSubTitle());
        intent.putExtra("name", markerItem.getCalloutTitle());
        intent.putExtra("image", stu[Integer.parseInt(markerItem.getID()) - 1].getImage());
        intent.putExtra("id", markerItem.getID());
        startActivity(intent);

    }
    //  PendingIntent mPending;
    /**
     * onCreate()
     */
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mContext = this;

        mMapView = new TMapView(this);
        addView(mMapView);

        configureMapView();
        ImageView img1 = (ImageView) findViewById(R.id.achievementImageView);
        ImageView img2 = (ImageView) findViewById(R.id.searchImageView);
        ImageView img3 = (ImageView) findViewById(R.id.settingImageView);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showalert();
//                drawMapPath();
//                mMapView.setTrackingMode(true);
//                Intent intent = new Intent(PROX_ALERT_INTENT);
//                Toast.makeText(getApplicationContext(), getIntent().getStringExtra("mokid"), Toast.LENGTH_LONG).show();
                //showalert();
//                PendingIntent proximityIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
//                locationManager.addProximityAlert(37.1271,127.0125,POINT_RADIUS,PROX_ALERT_EXPIRATION,proximityIntent);
//                IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
//
//                registerReceiver(new Goal(), filter);
//                 mLocMan.addProximityAlert(37.422006, 122.084095, 5, -1, proximityIntent);

//                PendingIntent proximityIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
//                locationManager.addProximityAlert(37.1271,127.0125,POINT_RADIUS,PROX_ALERT_EXPIRATION,proximityIntent);
//                IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
//
//                registerReceiver(new Goal(), filter);
//                 mLocMan.addProximityAlert(37.422006, 122.084095, 5, -1, proximityIntent);


            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("yuja", "searchBtn start: ");
//                Intent intent = new Intent(getApplicationContext(), SqlLiteYuzaActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);


            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double latitude = 0.0;
                Double longitude = 0.0;
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                try {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000, 1, this);
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
                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastLocation != null) {
                        latitude = lastLocation.getLatitude();
                        longitude = lastLocation.getLongitude();
                        // Toast.makeText(getApplicationContext(), "마지막 위치 latitudedddd" + latitude + "\nlongitude" + longitude, Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {

                }
                LogManager.printLog(longitude + "와" + latitude);
                mMapView.setCenterPoint(longitude, latitude);
                mMapView.setLocationPoint(longitude, latitude);
//                Intent intent = new Intent(MainActivity.this, TestBtnActivity.class);
//                startActivity(intent);
                setupProximityAlert();  //방송국
            }
        });
        mArrayID = new ArrayList<String>();

        mArrayCircleID = new ArrayList<String>();
        mCircleID = 0;

        mArrayLineID = new ArrayList<String>();
        mLineID = 0;

        mArrayPolygonID = new ArrayList<String>();
        mPolygonID = 0;

        mArrayMarkerID = new ArrayList<String>();
        mMarkerID = 0;
        Double latitude = 0.0;
        Double longitude = 0.0;
//        TMapGpsManager gps = new TMapGpsManager(this);
//        gps.setProvider(TMapGpsManager.GPS_PROVIDER);
//        gps.OpenGps();
//
//        TMapPoint point = gps.getLocation();
//
//        mMapView.setCenterPoint(point.getLongitude(),    point.getLatitude());
//        LogManager.printLog(point.getLongitude()+"안녕"+    point.getLatitude());
        showMarkerPoint();
        mMapView.setTMapLogoPosition(TMapView.TMapLogoPositon.POSITION_BOTTOMRIGHT);
        mMapView.setBicycleFacilityInfo(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000, 1, this);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();
                // Toast.makeText(getApplicationContext(), "마지막 위치 latitudedddd" + latitude + "\nlongitude" + longitude, Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

        }

        GPSListener gpsListener = new GPSListener();
        long minTime = 500;
        float minDistance = 0;

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
        //Toast.makeText(getApplicationContext(), "위치확인 로그를 확인하세요", Toast.LENGTH_LONG).show();


        Location location;

        LogManager.printLog("setLocationPointss " + longitude + " " + latitude);
        TMapPoint tpoint = mMapView.getLocationPoint();
        double Latitude = tpoint.getLatitude();
        double Longitude = tpoint.getLongitude();
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
        mMapView.setIcon(bitmap);
        LogManager.printLog(longitude + "와" + latitude);
        mMapView.setCenterPoint(longitude, latitude);
        mMapView.setLocationPoint(longitude, latitude);
        mMapView.setZoomLevel(13);
        mMapView.setIconVisibility(true);

//        receiver = new LocationReceiver();
//        IntentFilter filter = new IntentFilter("seoyuki.yuza");
//        registerReceiver(receiver, filter);
//
//        // ProximityAlert 등록
//        Intent intent = new Intent("seoyuki.yuza");
//        PendingIntent proximityIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
//        locationManager.addProximityAlert(37.422006, 122.084095, 10000, -1, proximityIntent);

    }

    public void onResume() {
        super.onResume();

        //locationManager.addProximityAlert(37.464366,127.082277,5000,-1,mPending);
    }

    public void onPause() {
        super.onPause();
        LogManager.printLog("이바보야진짜아니야");

        // locationManager.removeProximityAlert(mPending);
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude();

        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
        LogManager.printLog(msg + "안녕하세요");
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


//    @Override
//    Public void onLocationChange(Location location){
//        double lat = location.getLatitude();
//        double lon = location.getLongtitude();
//    }

    /**
     * setSKPMapApiKey()에 ApiKey를 입력 한다.
     * setSKPMapBizappId()에 mBizAppID를 입력한다.
     * -> setSKPMapBizappId는 TMapTapi(TMap앱 연동)를 사용할때 BizAppID 설정 해야 한다. TMapTapi 사용하지 않는다면 setSKPMapBizappId를 하지 않아도 된다.
     */
    private void configureMapView() {
        mMapView.setSKPMapApiKey(mApiKey);
//		mMapView.setSKPMapBizappId(mBizAppID);
    }

    /**
     * initView - 버튼에 대한 리스너를 등록한다.
     */
    private void initView() {
//        for (int btnMapView : mArrayMapButton) {
//            Button ViewButton = (Button) findViewById(btnMapView);
//            ViewButton.setOnClickListener(this);
//        }

        mMapView.setOnApiKeyListener(new TMapView.OnApiKeyListenerCallback() {
            @Override
            public void SKPMapApikeySucceed() {
                LogManager.printLog("IntroActivity SKPMapApikeySucceed");
            }

            @Override
            public void SKPMapApikeyFailed(String errorMsg) {
                LogManager.printLog("IntroActivity SKPMapApikeyFailed " + errorMsg);
            }
        });

        mMapView.setOnEnableScrollWithZoomLevelListener(new TMapView.OnEnableScrollWithZoomLevelCallback() {
            @Override
            public void onEnableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                LogManager.printLog("IntroActivity onEnableScrollWithZoomLevelEvent " + zoom + " " + centerPoint.getLatitude() + " " + centerPoint.getLongitude());
            }
        });

        mMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            @Override
            public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                LogManager.printLog("IntroActivity onDisableScrollWithZoomLevelEvent " + zoom + " " + centerPoint.getLatitude() + " " + centerPoint.getLongitude());
            }
        });

        mMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                LogManager.printLog("IntroActivity onPressUpEvent " + markerlist.size());
                return false;
            }

            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                LogManager.printLog("IntroActivity onPressEvent " + markerlist.size());

                for (int i = 0; i < markerlist.size(); i++) {
                    TMapMarkerItem item = markerlist.get(i);
                    LogManager.printLog("IntroActivity onPressEvent " + item.getName() + " " + item.getTMapPoint().getLatitude() + " " + item.getTMapPoint().getLongitude());
                }
                return false;
            }
        });

        mMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point) {
                LogManager.printLog("IntroActivity onLongPressEvent " + markerlist.size());


            }
        });

        mMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {
                String strMessage = "dddddd";
                strMessage = "ID: " + markerItem.getID() + " " + "Title " + markerItem.getCalloutTitle();
                Common.showAlertDialog(MainActivity.this, "Callout Right Button", strMessage);
            }
        });

        mMapView.setOnClickReverseLabelListener(new TMapView.OnClickReverseLabelListenerCallback() {
            @Override
            public void onClickReverseLabelEvent(TMapLabelInfo findReverseLabel) {
                if (findReverseLabel != null) {
                    LogManager.printLog("MainActivity setOnClickReverseLabelListener " + findReverseLabel.id + " / " + findReverseLabel.labelLat
                            + " / " + findReverseLabel.labelLon + " / " + findReverseLabel.labelName);


                }
            }
        });

        m_nCurrentZoomLevel = -1;
        m_bShowMapIcon = false;
        m_bTrafficeMode = false;
        m_bSightVisible = false;
        m_bTrackingMode = false;
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receivers);
//        alertDialog의 페이스북 버튼 클릭 메소드(alertDialogFacebookBtnClick 메소드) 실행 후 메인으로 돌아올 때 에러
//        에러 회피 위해 주석처리. unregisterReceiver 메소드 분석 후 정리 필요함.

//		gps.CloseGps();
        if (mOverlayList != null) {
            mOverlayList.clear();
        }
    }


    public TMapPoint randomTMapPoint() {
        double latitude = ((double) Math.random()) * (37.575113 - 37.483086) + 37.483086;
        double longitude = ((double) Math.random()) * (127.027359 - 126.878357) + 126.878357;

        latitude = Math.min(37.575113, latitude);
        latitude = Math.max(37.483086, latitude);

        longitude = Math.min(127.027359, longitude);
        longitude = Math.max(126.878357, longitude);

        LogManager.printLog("randomTMapPoint" + latitude + " " + longitude);

        TMapPoint point = new TMapPoint(latitude, longitude);

        return point;
    }

    public void overlay() {
        m_bOverlayMode = !m_bOverlayMode;
        if (m_bOverlayMode) {
            mMapView.setZoomLevel(6);

            if (mOverlay == null) {
                mOverlay = new ImageOverlay(this, mMapView);
            }

            mOverlay.setLeftTopPoint(new TMapPoint(45.640171, 114.9652948));
            mOverlay.setRightBottomPoint(new TMapPoint(29.2267177, 138.7206798));
            mOverlay.setImage(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.test_image));

            if (mOverlayList == null) {
                mOverlayList = new ArrayList<Bitmap>();
                mOverlayList.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.test_image));
                mOverlayList.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ani1));
                mOverlayList.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ani2));
            }

            mOverlay.setAnimationIcons(mOverlayList);
            mOverlay.setAniDuration(10000);
            mOverlay.startAnimation();
            mMapView.addTMapOverlayID(0, mOverlay);
        } else {
            mOverlay.stopAnimation();
            mMapView.removeTMapOverlayID(0);
        }
    }

    public void animateTo() {
//		TMapPoint point = randomTMapPoint();
//		mMapView.setCenterPoint(point.getLongitude(), point.getLatitude(), true);
//		Bitmap capture = mMapView.getCaptureImage();
        addTMapCircle();

    }

    public Bitmap overlayMark(Bitmap bmp1, Bitmap bmp2, int width, int height) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());

        int marginLeft = 7;
        int marginTop = 5;

        if (width >= 1500 || height > 1500) {
            bmp2 = Bitmap.createScaledBitmap(bmp2, bmp1.getWidth() - 40, bmp1.getHeight() - 50, true);
            marginLeft = 20;
            marginTop = 10;
        } else if (width >= 1200 || height > 1200) {
            bmp2 = Bitmap.createScaledBitmap(bmp2, bmp1.getWidth() - 22, bmp1.getHeight() - 35, true);
            marginLeft = 11;
            marginTop = 7;
        } else {
            bmp2 = Bitmap.createScaledBitmap(bmp2, bmp1.getWidth() - 15, bmp1.getHeight() - 25, true);
        }

        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, 0, 0, null);
        canvas.drawBitmap(bmp2, marginLeft, marginTop, null);
        return bmOverlay;
    }

    /**
     * mapZoomIn
     * 지도를 한단계 확대한다.
     */
    public void mapZoomIn() {
        mMapView.MapZoomIn();
    }

    /**
     * mapZoomOut
     * 지도를 한단계 축소한다.
     */
    public void mapZoomOut() {
        mMapView.MapZoomOut();
    }

    /**
     * getZoomLevel
     * 현재 줌의 레벨을 가지고 온다.
     */
    public void getZoomLevel() {
        int nCurrentZoomLevel = mMapView.getZoomLevel();
        Common.showAlertDialog(this, "", "현재 Zoom Level : " + Integer.toString(nCurrentZoomLevel));
    }

    /**
     * setZoomLevel
     * Zoom Level을 설정한다.
     */
    public void setZoomLevel() {
        final String[] arrString = getResources().getStringArray(R.array.a_zoomlevel);
        AlertDialog dlg = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Select Zoom Level")
                .setSingleChoiceItems(R.array.a_zoomlevel, m_nCurrentZoomLevel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        m_nCurrentZoomLevel = item;
                        dialog.dismiss();
                        mMapView.setZoomLevel(Integer.parseInt(arrString[item]));
                    }
                }).show();
    }

    /**
     * seetMapType
     * Map의 Type을 설정한다.
     */
    public void setMapType() {
        AlertDialog dlg = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Select MAP Type")
                .setSingleChoiceItems(R.array.a_maptype, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        LogManager.printLog("Set Map Type " + item);
                        dialog.dismiss();
                        mMapView.setMapType(item);
                    }
                }).show();
    }

    /**
     * getLocationPoint
     * 현재위치로 표시될 좌표의 위도, 경도를 반환한다.
     */
    public void getLocationPoint() {
        TMapPoint point = mMapView.getLocationPoint();

        double Latitude = point.getLatitude();
        double Longitude = point.getLongitude();

        m_Latitude = Latitude;
        m_Longitude = Longitude;

        LogManager.printLog("Latitude " + Latitude + " Longitude " + Longitude);

        String strResult = String.format("Latitude = %f Longitude = %f", Latitude, Longitude);

        mMapView.MapZoomIn();
    }

    /**
     * setLocationPoint
     * 현재위치로 표시될 좌표의 위도,경도를 설정한다.
     */
    public void setLocationPoint() {

        TMapGpsManager tmapgps = new TMapGpsManager(this);
        tmapgps.setProvider(TMapGpsManager.GPS_PROVIDER);
        tmapgps.OpenGps();
        TMapPoint point = tmapgps.getLocation();
        double Latitude = point.getLatitude();
        double Longitude = point.getLongitude();
        LogManager.printLog("setLocationPoint " + Latitude + " " + Longitude);


        mMapView.setLocationPoint(Longitude, Latitude);
        mMapView.MapZoomOut();
    }

    /**
     * setMapIcon
     * 현재위치로 표시될 아이콘을 설정한다.
     */
    public void setMapIcon() {
        m_bShowMapIcon = !m_bShowMapIcon;
        TMapTapi tmaptapi = new TMapTapi(this);
        boolean isTmapApp = tmaptapi.isTmapApplicationInstalled();
        if (isTmapApp == false) {
            LogManager.printLog("티맵이 깔려있지않습니다");
        } else {
            HashMap<String, String> pathInfo = new HashMap<String, String>();
            pathInfo.put("rGoName", "덕수궁");
            pathInfo.put("rGolat", "37.565847");
            pathInfo.put("rGolon", "126.975069");
            pathInfo.put("rStName", "집");
            pathInfo.put("rStlat", "37.50861147");
            pathInfo.put("rStlon", "126.8911457");
            tmaptapi.invokeRoute(pathInfo);
        }
        if (m_bShowMapIcon) {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
            mMapView.setIcon(bitmap);
        }
        mMapView.setIconVisibility(m_bShowMapIcon);
    }

    /**
     * setCompassMode
     * 단말의 방항에 따라 움직이는 나침반모드로 설정한다.
     */
    public void setCompassMode() {
        mMapView.setCompassMode(!mMapView.getIsCompass());

    }

    /**
     * getIsCompass
     * 나침반모드의 사용여부를 반환한다.
     */
    public void getIsCompass() {
        Boolean bGetIsCompass = mMapView.getIsCompass();


        mMapView.setBicycleFacilityInfo(true);
        mMapView.setBicycleInfo(true);
    }

    /**
     * setTrafficeInfo
     * 실시간 교통정보를 표출여부를 설정한다.
     */
    public void setTrafficeInfo() {
        m_bTrafficeMode = !m_bTrafficeMode;
        mMapView.setTrafficInfo(m_bTrafficeMode);
    }

    /**
     * getIsTrafficeInfo
     * 실시간 교통정보 표출상태를 반환한다.
     */
    public void getIsTrafficeInfo() {
        Boolean bIsTrafficeInfo = mMapView.IsTrafficInfo();
        Common.showAlertDialog(this, "", "현재 실시간 교통정보 표출상태는  : " + bIsTrafficeInfo.toString());
    }


    /**
     * setTrackingMode
     * 화면중심을 단말의 현재위치로 이동시켜주는 트래킹모드로 설정한다.
     */
    public void setTrackingMode() {
        m_bTrackingMode = !m_bTrackingMode;
        mMapView.setTrackingMode(m_bTrackingMode);
    }

    /**
     * getIsTracking
     * 트래킹모드의 사용여부를 반환한다.
     */
    public void getIsTracking() {
        Boolean bIsTracking = mMapView.getIsTracking();
        Common.showAlertDialog(this, "", "현재 트래킹모드 사용 여부  : " + bIsTracking.toString());
    }

    /**
     * addTMapCircle()
     * 지도에 서클을 추가한다.
     */
    public void addTMapCircle() {
        TMapCircle circle = new TMapCircle();

        circle.setRadius(300);
        circle.setLineColor(Color.BLUE);
        circle.setAreaAlpha(50);
        circle.setCircleWidth((float) 10);
        circle.setRadiusVisible(true);

        TMapPoint point = new TMapPoint(37.565847, 126.975069);
        circle.setCenterPoint(point);

        String strID = String.format("circle%d", mCircleID++);
        mMapView.addTMapCircle(strID, circle);
        mArrayCircleID.add(strID);
    }

    /**
     * removeTMapCircle
     * 지도상의 해당 서클을 제거한다.
     */
    public void removeTMapCircle() {
        if (mArrayCircleID.size() <= 0)
            return;

        String strCircleID = mArrayCircleID.get(mArrayCircleID.size() - 1);
        mMapView.removeTMapCircle(strCircleID);
        mArrayCircleID.remove(mArrayCircleID.size() - 1);
    }

    public void showMarkerPoint2() {
        ArrayList<Bitmap> list = null;
        for (int i = 0; i < 50; i++) {

            MarkerOverlay marker1 = new MarkerOverlay(this, mMapView);
            String strID = String.format("%02d", i);

            marker1.setID(strID);
            marker1.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.map_pin_red));
            marker1.setTMapPoint(randomTMapPoint());

            if (list == null) {
                list = new ArrayList<Bitmap>();
            }

            list.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_red));
            list.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.end));

            marker1.setAnimationIcons(list);
            mMapView.addMarkerItem2(strID, marker1);
        }

        mMapView.setOnMarkerClickEvent(new TMapView.OnCalloutMarker2ClickCallback() {

            @Override
            public void onCalloutMarker2ClickEvent(String id, TMapMarkerItem2 markerItem2) {
                LogManager.printLog("ClickEvent " + " id " + id + " " + markerItem2.latitude + " " + markerItem2.longitude);

                String strMessage = "ClickEvent " + " id " + id + " " + markerItem2.latitude + " " + markerItem2.longitude;

                Common.showAlertDialog(MainActivity.this, "TMapMarker2", strMessage);
            }
        });
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
        TMapView tmapview = new TMapView(this);
        Bitmap bitmap = null;
////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////
//        final String[] arrString = getResources().getStringArray(R.array.ducksugung);
        TMapPoint point = new TMapPoint(37.565847, 126.975069);
//
//
//


        Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.i_go);
        String strID = String.format("pmarker%d", mMarkerID++);
//
//
//
        point = new TMapPoint(37.55102510077652, 126.98789834976196);
//
//
//        item2.setTMapPoint(point);
//        item2.setName("서울타워");
//        item2.setVisible(item2.VISIBLE);
//        item2.setCalloutTitle("청호타워");
//
//        item2.setCanShowCallout(true);
//
        bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.i_go);
        item2.setCalloutRightButtonImage(bitmap_i);

//        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pin_tevent);
//        item2.setIcon(bitmap);

        strID = String.format("pmarker%d", mMarkerID++);

        mMapView.addMarkerItem(strID, item2);
        mArrayMarkerID.add(strID);
//
//
//        point = new TMapPoint(37.58102510077652, 126.98789834976196);
//        item2 = new TMapMarkerItem();
//
//        item2.setTMapPoint(point);
//        item2.setName("chunggunsas");
//        item2.setVisible(item2.VISIBLE);
//        item2.setCalloutTitle("chunggunsas");
//
//        item2.setCalloutSubTitle("을지로입구역 500M");
//        item2.setCanShowCallout(true);
//
//
//        bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.i_go);
//        item2.setCalloutRightButtonImage(bitmap_i);
//
//        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_red);
//        item2.setIcon(bitmap);
//
//        strID = String.format("pmarker%d", mMarkerID++);
//
//        mMapView.addMarkerItem(strID, item2);
//        mArrayMarkerID.add(strID);


//
//        for (int i = 4; i < 10; i++) {
//            TMapMarkerItem item3 = new TMapMarkerItem();
//
//            item3.setID(strID);
//            item3.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.map_pin_red));
//
//            item3.setTMapPoint(randomTMapPoint());
//            item3.setCalloutTitle(">>>>" + strID + "<<<<<");
//            item3.setCanShowCallout(true);
//
//            strID = String.format("pmarker%d", mMarkerID++);
//
//            mMapView.addMarkerItem(strID, item2);
//            mArrayMarkerID.add(strID);
//        }
        marker = xmlParser();
        stu = new Student[marker.size()];
        Double wi;
        Double kyung;

        for (int count = 0; count < marker.size(); count++) {
            TMapMarkerItem item = new TMapMarkerItem();

        }
        for (int count = 0; count < marker.size(); count++) {

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
            String imsy = stu[count].getImage();
            item2.setID(imsy);


            bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.i_go);
            item2.setCalloutRightButtonImage(bitmap_i);


            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.end);
            item2.setIcon(bitmap);

            strID = String.format(stu[count].getId() + "", mMarkerID++);

            mMapView.addMarkerItem(strID, item2);
            mArrayMarkerID.add(strID);


        }


    }

    public void removeMarker() {
        if (mArrayMarkerID.size() <= 0)
            return;

        String strMarkerID = mArrayMarkerID.get(mArrayMarkerID.size() - 1);
        mMapView.removeMarkerItem(strMarkerID);
        mArrayMarkerID.remove(mArrayMarkerID.size() - 1);
    }

    /**
     * moveFrontMarker
     * 마커를 맨 앞으로 표시 하도록 한다.
     * showMarkerPoint() 함수를 먼저 클릭을 한 후, 클릭을 해야 함.
     */
    public void moveFrontMarker() {
        TMapMarkerItem item = mMapView.getMarkerItemFromID("1");
        mMapView.bringMarkerToFront(item);
    }

    /**
     * moveBackMarker
     * 마커를 맨 뒤에 표시하도록 한다.
     * showMarkerPoint() 함수를 먼저 클릭을 한 후, 클릭을 해야 함.
     */
    public void moveBackMarker() {
        TMapMarkerItem item = mMapView.getMarkerItemFromID("1");
        mMapView.sendMarkerToBack(item);
    }


    /**
     * drawMapPath
     * 지도에 시작-종료 점에 대해서 경로를 표시한다.
     */
    public void drawMapPath() {
//		TMapPoint point1 = mMapView.getCenterPoint();
//		TMapPoint point2 = randomTMapPoint();
//        GPSListener gpsListener = new GPSListener();
//        long minTime = 1000;
//        float minDistance = 0;
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
//        Toast.makeText(getApplicationContext(), "위치확인 로그를 확인하세요", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(this,Goal.class);
////        mPending = PendingIntent.getBroadcast(this,0,intent,0);
////        locationManager.addProximityAlert(37.464366,127.082277,500,-1,mPending);
//        Location location;
//        double s = mMapView.getLatitude();
//        double d = mMapView.getLongitude();
//        LogManager.printLog("setLocationPointss " + s + " " + d);

        TMapPoint point1 = new TMapPoint(37.565847, 126.975069);
        TMapPoint point2 = new TMapPoint(s, d);
        TMapData tmapdata = new TMapData();
        tmapdata.findPathDataWithType(TMapData.TMapPathType.BICYCLE_PATH, point2, point1,
                new TMapData.FindPathDataListenerCallback() {
                    @Override
                    public void onFindPathData(TMapPolyLine polyLine) {
                        mMapView.addTMapPath(polyLine);
                    }
                });

//		tmapdata.findPathData(point1, point2, new FindPathDataListenerCallback() {
//
//			@Override
//			public void onFindPathData(TMapPolyLine polyLine) {
//				mMapView.addTMapPath(polyLine);
//			}
//		});

    }

    public void mokdrawMapPath() {
//		TMapPoint point1 = mMapView.getCenterPoint();
//		TMapPoint point2 = randomTMapPoint();
//        GPSListener gpsListener = new GPSListener();
//        long minTime = 1000;
//        float minDistance = 0;
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
//        Toast.makeText(getApplicationContext(), "위치확인 로그를 확인하세요", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(this,Goal.class);
////        mPending = PendingIntent.getBroadcast(this,0,intent,0);
////        locationManager.addProximityAlert(37.464366,127.082277,500,-1,mPending);
//        Location location;
//        double s = mMapView.getLatitude();
//        double d = mMapView.getLongitude();
//        LogManager.printLog("setLocationPointss " + s + " " + d);
        Intent inent = getIntent();

        TMapMarkerItem markeritem = mMapView.getMarkerItemFromID(getIntent().getStringExtra("mokname"));
        TMapPoint point1 = new TMapPoint(37.565847, 126.975069);
        TMapPoint point2 = markeritem.getTMapPoint();
        TMapData tmapdata = new TMapData();
        tmapdata.findPathDataWithType(TMapData.TMapPathType.BICYCLE_PATH, point2, point1,
                new TMapData.FindPathDataListenerCallback() {
                    @Override
                    public void onFindPathData(TMapPolyLine polyLine) {
                        mMapView.addTMapPath(polyLine);
                    }
                });

//		tmapdata.findPathData(point1, point2, new FindPathDataListenerCallback() {
//
//			@Override
//			public void onFindPathData(TMapPolyLine polyLine) {
//				mMapView.addTMapPath(polyLine);
//			}
//		});

    }

    private String getContentFromNode(Element item, String tagName) {
        NodeList list = item.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            if (list.item(0).getFirstChild() != null) {
                return list.item(0).getFirstChild().getNodeValue();
            }
        }
        return null;
    }

    /**
     * displayMapInfo()
     * POI들이 모두 표시될 수 있는 줌레벨 결정함수와 중심점리턴하는 함수
     */
    public void displayMapInfo() {
		/*
		TMapPoint point1 = mMapView.getCenterPoint();
		TMapPoint point2 = randomTMapPoint();
		*/
        TMapPoint point1 = new TMapPoint(37.541642248630524, 126.99599611759186);
        TMapPoint point2 = new TMapPoint(37.541243493556976, 126.99659830331802);
        TMapPoint point3 = new TMapPoint(37.540909826755524, 126.99739581346512);
        TMapPoint point4 = new TMapPoint(37.541080713272095, 126.99874675273895);

        ArrayList<TMapPoint> point = new ArrayList<TMapPoint>();

        point.add(point1);
        point.add(point2);
        point.add(point3);
        point.add(point4);

        TMapInfo info = mMapView.getDisplayTMapInfo(point);

        String strInfo = "Center Latitude" + info.getTMapPoint().getLatitude() + "Center Longitude" + info.getTMapPoint().getLongitude() +
                "Level " + info.getTMapZoomLevel();

        Common.showAlertDialog(this, "", strInfo);
    }

    /**
     * removeMapPath
     * 경로 표시를 삭제한다.
     */
    public void removeMapPath() {
        mMapView.removeTMapPath();
    }

    /**
     * naviGuide
     * 길안내
     */
    public void naviGuide() {
        TMapPoint point1 = randomTMapPoint();
        TMapPoint point2 = new TMapPoint(37.565847, 126.975069);
//				 mMapView.getCenterPoint();

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataAll(point1, point2, new TMapData.FindPathDataAllListenerCallback() {
            @Override
            public void onFindPathDataAll(Document doc) {
                LogManager.printLog("onFindPathDataAll: " + doc);
            }
        });
    }


    public void drawPedestrianPath() {
        TMapPoint point1 = mMapView.getCenterPoint();
        TMapPoint point2 = randomTMapPoint();

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, point1, point2, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                polyLine.setLineColor(Color.BLUE);
                mMapView.addTMapPath(polyLine);
            }
        });
    }

    public void drawBicyclePath() {
        TMapPoint point1 = mMapView.getCenterPoint();
        TMapPoint point2 = randomTMapPoint();

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataWithType(TMapData.TMapPathType.BICYCLE_PATH, point1, point2, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                mMapView.addTMapPath(polyLine);
            }
        });
    }


    /**
     * convertToAddress
     * 지도에서 선택한 지점을 주소를 변경요청한다.
     */
    public void convertToAddress() {
        TMapPoint point = mMapView.getCenterPoint();

        TMapData tmapdata = new TMapData();

        if (mMapView.isValidTMapPoint(point)) {
            tmapdata.convertGpsToAddress(point.getLatitude(), point.getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
                @Override
                public void onConvertToGPSToAddress(String strAddress) {
                    LogManager.printLog("선택한 위치의 주소는 " + strAddress);
                }
            });

//		    tmapdata.geoCodingWithAddressType("F02", "서울시", "구로구", "새말로", "6", "", new GeoCodingWithAddressTypeListenerCallback() {
//
//				@Override
//				public void onGeoCodingWithAddressType(TMapGeocodingInfo geocodingInfo) {
//					LogManager.printLog(">>> strMatchFlag : " + geocodingInfo.strMatchFlag);
//					LogManager.printLog(">>> strLatitude : " + geocodingInfo.strLatitude);
//					LogManager.printLog(">>> strLongitude : " + geocodingInfo.strLongitude);
//					LogManager.printLog(">>> strCity_do : " + geocodingInfo.strCity_do);
//					LogManager.printLog(">>> strGu_gun : " + geocodingInfo.strGu_gun);
//					LogManager.printLog(">>> strLegalDong : " + geocodingInfo.strLegalDong);
//					LogManager.printLog(">>> strAdminDong : " + geocodingInfo.strAdminDong);
//					LogManager.printLog(">>> strBunji : " + geocodingInfo.strBunji);
//					LogManager.printLog(">>> strNewMatchFlag : " + geocodingInfo.strNewMatchFlag);
//					LogManager.printLog(">>> strNewLatitude : " + geocodingInfo.strNewLatitude);
//					LogManager.printLog(">>> strNewLongitude : " + geocodingInfo.strNewLongitude);
//					LogManager.printLog(">>> strNewRoadName : " + geocodingInfo.strNewRoadName);
//					LogManager.printLog(">>> strNewBuildingIndex : " + geocodingInfo.strNewBuildingIndex);
//					LogManager.printLog(">>> strNewBuildingName : " + geocodingInfo.strNewBuildingName);
//				}
//			});
        }
    }


    public void setTileType() {
        AlertDialog dlg = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Select MAP Tile Type")
                .setSingleChoiceItems(R.array.a_tiletype, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        LogManager.printLog("Set Map Tile Type " + item);
                        dialog.dismiss();
                        mMapView.setTileType(item);
                    }
                }).show();
    }

    public void setBicycle() {
        mMapView.setBicycleInfo(!mMapView.IsBicycleInfo());
    }

    public void setBicycleFacility() {
        mMapView.setBicycleFacilityInfo(!mMapView.isBicycleFacilityInfo());
    }

    public void invokeRoute() {
        final TMapPoint point = mMapView.getCenterPoint();
        TMapData tmapdata = new TMapData();

        if (mMapView.isValidTMapPoint(point)) {
            tmapdata.convertGpsToAddress(point.getLatitude(), point.getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
                @Override
                public void onConvertToGPSToAddress(String strAddress) {
                    TMapTapi tmaptapi = new TMapTapi(MainActivity.this);
                    float fY = (float) point.getLatitude();
                    float fX = (float) point.getLongitude();
                    tmaptapi.invokeRoute(strAddress, fX, fY);
                }
            });
        }
    }

    public void invokeSetLocation() {
        final TMapPoint point = mMapView.getCenterPoint();
        TMapData tmapdata = new TMapData();

        tmapdata.convertGpsToAddress(point.getLatitude(), point.getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
            @Override
            public void onConvertToGPSToAddress(String strAddress) {
                TMapTapi tmaptapi = new TMapTapi(MainActivity.this);
                float fY = (float) point.getLatitude();
                float fX = (float) point.getLongitude();
                tmaptapi.invokeSetLocation(strAddress, fX, fY);
            }
        });
    }

    public void invokeSearchProtal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("T MAP 통합 검색");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strSearch = input.getText().toString();

                new Thread() {
                    @Override
                    public void run() {
                        TMapTapi tmaptapi = new TMapTapi(MainActivity.this);
                        if (strSearch.trim().length() > 0)
                            tmaptapi.invokeSearchPortal(strSearch);
                    }
                }.start();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void tmapInstall() {
        new Thread() {
            @Override
            public void run() {
                TMapTapi tmaptapi = new TMapTapi(MainActivity.this);
                Uri uri = Uri.parse(tmaptapi.getTMapDownUrl().get(0));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

        }.start();
    }

    public void captureImage() {
        mMapView.getCaptureImage(20, new TMapView.MapCaptureImageListenerCallback() {

            @Override
            public void onMapCaptureImage(Bitmap bitmap) {

                String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();

                File path = new File(sdcard + File.separator + "image_write");
                if (!path.exists())
                    path.mkdir();

                File fileCacheItem = new File(path.toString() + File.separator + System.currentTimeMillis() + ".png");
                OutputStream out = null;

                try {
                    fileCacheItem.createNewFile();
                    out = new FileOutputStream(fileCacheItem);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean bZoomEnable = false;

    public void disableZoom() {
        bZoomEnable = !bZoomEnable;
        mMapView.setUserScrollZoomEnable(bZoomEnable);
    }

    public void timeMachine() {
        TMapData tmapdata = new TMapData();

        HashMap<String, String> pathInfo = new HashMap<String, String>();
        pathInfo.put("rStName", "T Tower");
        pathInfo.put("rStlat", Double.toString(37.566474));
        pathInfo.put("rStlon", Double.toString(126.985022));
        pathInfo.put("rGoName", "신도림");
        pathInfo.put("rGolat", "37.50861147");
        pathInfo.put("rGolon", "126.8911457");
        pathInfo.put("type", "arrival");

        Date currentTime = new Date();
        tmapdata.findTimeMachineCarPath(pathInfo, currentTime, null);
    }


    private void setupProximityAlert() {
        LocationManager locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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
            locationManager.addProximityAlert(sampleLongitude,
                    sampleLatitude, 500, 1000000,
                    proximityIntent);

            /*================================================================*/
            //시청자. POI_REACHED 이란 채널명으로 방송된 내용을 보려고 함.
            IntentFilter intentFilter = new IntentFilter(POI_REACHED);
            registerReceiver(receivers,
                    intentFilter);

            /*================================================================*/
        } else {
            Log.d(TAG, "GPS_PROVIDER not available");
        }

    }

    public void showalert() {
        // 도착 완료 화면(얼럿) 코드
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();

        // dialog.xml 파일을 인플레이션해서 보여준다
        // dialog.xml 파일의 세 버튼(카메라, 페이스북, 취소)에 대한 클릭 메소드는
        // alertDialogCameraClick, alertDialogFacebookClick, alertDialogCancelClick이며 가장 아래에 위치해 있다.
        // + TextView(위 세 버튼 이미지는 이미지뷰가 아닌 텍스트뷰의 백그라운드)에 직접 리스너를 달면 에러 발생
        View dialogView = inflater.inflate(R.layout.dialog, null);
        dialog.setView(dialogView);
        dialog.create().show();

    }

    class LocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //방송을 잘 잡으면 밑에 로그 한번 찍어줌.
            // 지피에스 위치가 변해서 127, 37.5 로 되면 DDMS 에 아래 로그가 찍힘으로 확인 가능
            Log.d("도착", "실패ㅠㅠㅠ");
            Toast.makeText(context, "목표 지점에 접근중..", Toast.LENGTH_LONG).show();
            SqlLiteYuzaActivity sql = new SqlLiteYuzaActivity();
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
                            // finish();
                            shareDialog = new ShareDialog(MainActivity.this);
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
        ints.putExtra("mokjuk", mokjuk);
        startActivity(ints);

    }

}