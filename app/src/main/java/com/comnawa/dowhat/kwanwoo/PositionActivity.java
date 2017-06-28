package com.comnawa.dowhat.kwanwoo;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class PositionActivity extends AppCompatActivity implements OnMapReadyCallback{

    List<Marker> prevMakers = null;

    EditText editPlace;
    MapFragment fragment;
    GoogleMap map;
    //Button btnOk;

    //GoogleMap
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prevMakers = new ArrayList<Marker>();

        setContentView(R.layout.position_kwanwoo);
       // btnOk = (Button)findViewById(R.id.btnOk);
        editPlace=(EditText)findViewById(R.id.editPlace);
        fragment=(MapFragment)getFragmentManager().findFragmentById(R.id.fragment1);
        fragment.getMapAsync(this);
            //확인버튼
        Button btnOk = (Button)findViewById(R.id.btnOk);
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
                                Toast.makeText(PositionActivity.this,"위치가 저장되었습니다."
                                ,Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(PositionActivity.this,CalendarCoreActivity.class);
                                   // Intent intent = new Intent();
                       /*         intent.setClassName("com.comnawa.dowhat.kwanwoo.CalendarCoreActivity"
                                        ,"com.comnawa.dowhat.kwanwoo.PositionActivity");*/
                                //intent.setClassName(CalendarCoreActivity.class,PositionActivity.this);
                                startActivityForResult(intent,1);
                               // startActivity(intent);
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

        //스타트액티비티포리저트
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        PositionActivity.super.onActivityResult(requestCode,resultCode, intent);
        Bundle extraBundle;
        if(requestCode == 1){
            Log.d("1","this close");
            if(resultCode == RESULT_OK){
                extraBundle=intent.getExtras();
                String str=extraBundle.getString("테스트");
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
                Toast.makeText(PositionActivity.this,"위치설정을 계속합니다."
                ,Toast.LENGTH_SHORT).show();

            }
        }).create().show();

    }
    //implements method - 지도가 완성될 때 호출
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //권한
        DoWhat.checkPermission(this, DoWhat.access_fine_location,DoWhat.access_coarse_location);
        //현재위치
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        //List<Marker> prevMakers = null;
    /*    //중복마커 제거
        HashSet<Marker> hashSet = new HashSet<Marker>();
        hashSet.addAll(prevMakers);
        prevMakers.clear();
        prevMakers.addAll(hashSet);*/
    }

        public void search(View v){
            map.clear(); //이전에 지정 된 중복마커처리
            String place = editPlace.getText().toString();
            Geocoder corder = new Geocoder(this);
            List<Address> list = null;
            try {
                list = corder.getFromLocationName(place, 5);
            }catch (Exception e){
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
            marker.title(""+editPlace.getText().toString());
            map.addMarker(marker);



        }

}
