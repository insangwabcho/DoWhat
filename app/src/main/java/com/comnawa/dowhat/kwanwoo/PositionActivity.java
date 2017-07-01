package com.comnawa.dowhat.kwanwoo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.DoWhat;
import com.comnawa.dowhat.sangjin.DetailActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PositionActivity extends AppCompatActivity implements OnMapReadyCallback {

    List<Marker> prevMakers = null;

    EditText editPlace;
    MapFragment fragment;
    GoogleMap map;
    LocationManager locationManager;
    LocationListener locationListener; //위치정보 리스너

    double latitude; //위도
    double longitude; //경도

    //GoogleMap
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //위치정보관리자
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //맵 프래그먼트
        fragment = (MapFragment)getFragmentManager().findFragmentById(R.id.fragment1);
      //  fragment.getMapAsync(this); //비동기 방식으로 로딩
        //위치정보리스너 등록
                locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
             latitude = location.getLatitude(); //현재위도
             longitude = location.getLongitude(); //현재경도

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        prevMakers = new ArrayList<Marker>();
        //현재좌표 출력 변수
        LocationManager  locationManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        setContentView(R.layout.position_kwanwoo);
        // btnOk = (Button)findViewById(R.id.btnOk);
        editPlace = (EditText) findViewById(R.id.editPlace);
        fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment1);
        fragment.getMapAsync(this);
        //확인버튼
        Button btnOk = (Button) findViewById(R.id.btnOk);
        //btnOk.setOnClickListener(this);
        btnOk.setOnClickListener(new View.OnClickListener() {
            //static final int REQUEST_CODE=1234;
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mOk = new AlertDialog.Builder(PositionActivity.this);
                mOk.setTitle("설정알림")
                        .setMessage("설정한 위치를 저장하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(PositionActivity.this, "위치가 저장되었습니다."
                                        , Toast.LENGTH_SHORT).show();
                                //저장된 값을 DetailActivity로 이동
                                // Intent intent = new Intent(PositionActivity.this, DetailActivity.class);
                                // Intent intent = new Intent();
                       /*         intent.setClassName("com.comnawa.dowhat.kwanwoo.CalendarCoreActivity"
                                        ,"com.comnawa.dowhat.kwanwoo.PositionActivity");*/
                                //intent.setClassName(CalendarCoreActivity.class,PositionActivity.this);

                                // startActivityForResult(intent,1);
                                // startActivity(intent);
                                //입력한 값을 DetailActivity editPlace에 넘김
                                DetailActivity.address = editPlace.getText().toString();
                                finish(); //이전화면으로 이동
                            }
                        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
            }//onClick
        }); //onClickListener

    }


    @Override
    protected void onResume() {
        super.onResume();
        //출력된 주소 값을 지도액티비티에 다시 불러옴
        editPlace.setText(DetailActivity.address);


    }

    //스타트액티비티포리저트
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        PositionActivity.super.onActivityResult(requestCode, resultCode, intent);
        Bundle extraBundle;
        if (requestCode == 1) {
            Log.d("1", "this close");
            if (resultCode == RESULT_OK) {
                extraBundle = intent.getExtras();
                String str = extraBundle.getString("테스트");
                Toast.makeText(PositionActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        }

    }

    //취소버튼 이벤트
    @Override
    public void onBackPressed() {
        AlertDialog.Builder mBack = new AlertDialog.Builder(PositionActivity.this);
        mBack.setTitle("취소알림")
                .setMessage("위치설정을 그만하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PositionActivity.this, "이전화면으로 돌아갑니다.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PositionActivity.this, "위치설정을 계속합니다."
                        , Toast.LENGTH_SHORT).show();

            }
        }).create().show();

    }

    //위도, 경도로 주소 구하기
    public String getAddress(Context mContext, double lat, double lng) {
        String nowAddress = "현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;

        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);
                if (address != null && address.size() > 0) {
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress = currentLocationAddress;
                    editPlace.setText(nowAddress);

                }
            }
        } catch (Exception e) {
            Toast.makeText(PositionActivity.this, "주소를 가져 올 수 없습니다.", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
        return nowAddress;
    }

    //implements method - 지도가 완성될 때 호출
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //권한
        DoWhat.checkPermission(this, DoWhat.access_fine_location, DoWhat.access_coarse_location);
        //현재위치
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        //이벤트리스너
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,0,0,locationListener);
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,0,0,locationListener);
        //수동으로 현재위치 구할 때
        String locationProvider = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if(lastKnownLocation != null){
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();

        }
            //getAddress()
     //  editPlace.setText(getAddress(this, 123f, 145f));
                                    //현재화면, 위도, 경도
        editPlace.setText(getAddress(this, latitude,longitude));
        //List<Marker> prevMakers = null;
    /*    //중복마커 제거
        HashSet<Marker> hashSet = new HashSet<Marker>();
        hashSet.addAll(prevMakers);
        prevMakers.clear();
        prevMakers.addAll(hashSet);*/
    }

    public void search(View v) {
        map.clear(); //이전에 지정 된 중복마커처리
        String place = editPlace.getText().toString();
        Geocoder corder = new Geocoder(this);
        List<Address> list = null;
        try {
            list = corder.getFromLocationName(place, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Address addr = list.get(0);
        double lat = addr.getLatitude();    //위도
        double log = addr.getLongitude();   //경도
        //좌표객체
        LatLng geoPoint = new LatLng(lat, log);
        //카메라 이동 효과,좌표,줌레벨
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(geoPoint, 15));
        MarkerOptions marker = new MarkerOptions();
        marker.position(geoPoint);
        marker.title("" + editPlace.getText().toString());
        map.addMarker(marker);


    }

}
