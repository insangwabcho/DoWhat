package com.comnawa.dowhat.kwanwoo;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.DoWhat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class PositionActivity extends AppCompatActivity implements OnMapReadyCallback {
    EditText editPlace;
    Button btnSearch;
    MapFragment fragment;
    private GoogleMap map;

    //GoogleMap
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.position_kwanwoo);

        editPlace = (EditText) findViewById(R.id.fragment1);
        fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment1);
        //미동기적 방식으로 맵 로딩
        fragment.getMapAsync(this);
    }

    //로딩완료시 지도 호출
    @Override
    public void onMapReady(GoogleMap Map) {
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        DoWhat.checkPermission(this, DoWhat.access_fine_location, DoWhat.access_coarse_location);
        map.setMyLocationEnabled(true);

        map.getUiSettings().setZoomControlsEnabled(true);
    }
    public void search(View v){
        String place = editPlace.getText().toString();
        Geocoder corder = new Geocoder(this);
        List<Address> list = null;
        try {
            list = corder.getFromLocationName(place, 1);
        }catch(Exception e){
            e.printStackTrace();
        }
        Address addr = list.get(0);
        double lat = addr.getLatitude();    //위도
        double log = addr.getLongitude();   //경도

        LatLng geoPoint = new LatLng(lat, log);
        map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(geoPoint,15));
        MarkerOptions marker = new MarkerOptions();
        marker.position(geoPoint);
        marker.title(editPlace.getText().toString());
        map.addMarker(marker);
    }
}
