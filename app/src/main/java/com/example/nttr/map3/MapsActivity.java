package com.example.nttr.map3;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //マップ名
    private GoogleMap mMap;

    //マーカー変数名
    private Marker oneMarker;
    private Marker twoMarker;

    //mapの表示
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //mapの表示
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //緯度経度ズーム
        CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(35.710063, 139.8107), 15);

        //マップタイプの設定
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //マーカー作成
        makeMarker();


        //マップ更新
        mMap.moveCamera(cUpdate);


    }


    //マーカーの追加
    public void makeMarker(){
        //１
        oneMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(35.710063, 139.8107))
                .title("20180117"));
        oneMarker.setTag(1);

        //２
        twoMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(35.6994253, 139.8171457))
                .title("20180119"));
        twoMarker.setTag(2);





        //情報windowの設定
        // (API は最初に getInfoWindow(Marker) を呼び出し、null が返された場合は getInfoContents(Marker) を呼び出す)
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            //getInfoWindow(Marker)
            @Override
            public View getInfoWindow(Marker marker) {
            View view = getLayoutInflater().inflate(R.layout.info_window, null);

            // タイトル設定
            TextView title = (TextView)view.findViewById(R.id.info_title);
            title.setText(marker.getTitle());

            // 画像設定
            ImageView img = (ImageView)view.findViewById(R.id.info_image);

            //マーカータグの取得
            Integer clickCount = (Integer)marker.getTag();

            //タグによる画像の表示場合分け
            if (clickCount == 1) {
                img.setImageResource(R.drawable.photo1);
            }else{
                img.setImageResource(R.drawable.photo2);
            }

            return view;
            }

            //getInfoContents(Marker)
            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }

        });

        //マーカークリック時のリスナーに紐付ける
        //mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        
    }







}
