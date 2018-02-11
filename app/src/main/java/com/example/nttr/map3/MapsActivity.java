package com.example.nttr.map3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //マップ名
    private GoogleMap mMap;
    //緯度経度変数
    private double latitude;
    private double longtitude;

    //マーカー変数名
    private Marker oneMarker;
    private Marker twoMarker;

//    //Realmの定義
//    Realm mRealm;

    //ボタンの定義
    Button create;

    //URIの変数
    String open_file_URI;

    //写真
    private ImageView mOpenImage;
    private Bitmap mBitmap;
    private static final int OPEN_DOCUMENT_REQUEST = 1001;

    //storeage定義
    private StorageReference mStorageRef;
    //realtimedatebase
    private DatabaseReference mDatabase;

    //firebaseのデータ
    @IgnoreExtraProperties
    public class mapphoto{

        public double lat;
        public double lng;
        public String uri;
        //public int tag;

        //コンストラクタ

        public mapphoto(double lat,double lng, String uri) {
            this.lat = lat;
            this.lng = lng;
            this.uri = uri;
            //this.tag = tag;
        }

    }



    //mapの表示
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        //オープン処理
//        mRealm = Realm.getDefaultInstance();

        //ボタンの定義
        create = (Button) findViewById(R.id.create);

        //firebase入れる
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();



//        //realm登録処理
//        create.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mRealm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//
//
//                    }
//                });
//            }
//        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }




//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        //クローズ処理
//        mRealm.close();
//    }

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
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        //マーカー作成
        makeMarker();
        //マップ更新
        mMap.moveCamera(cUpdate);

        //長押しのリスナーをセット
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //ピッカーにより画像を取得する
                //ピッカーを使用してファイルを選択するためのIntent
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                //openFileDescriptor() によるファイル ストリームとして利用可能な「開くことができる」ファイルのカテゴリーを選択
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //MIME タイプを指定、取得するファイルの形式をフィルターする
                intent.setType("*/*");
                startActivityForResult(intent,OPEN_DOCUMENT_REQUEST);

                //緯度経度取得、マーカー作成、カメラ移動
                LatLng newlocation = new LatLng(latLng.latitude,latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(newlocation).title(""+latLng.latitude+" :"+ latLng.longitude));

                //緯度経度の代入
                latitude = latLng.latitude;
                longtitude = latLng.longitude;

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newlocation, 14));

//                //Firebaseテキストの保存
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("message");
//
//                myRef.setValue("Hello, World!");

            }
        });


    }

    private void writeNewPlace(double lat,double lng,String uri){
        mapphoto maphoto = new mapphoto(latitude,longtitude,open_file_URI);

        mDatabase.setValue(maphoto);
        Log.d("message", "firebase保存成功");

    }



    //ピッカーから画像を選択すると、onActivityResult() が呼び出される.
    // 選択した画像を指す URI が resultData パラメータに含まれ返ってきます。それを getData() を使って抽出
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        if (resultCode != Activity.RESULT_OK){
            Log.d("message", "ピッカー写真選択失敗");
            return;
        }else{
            switch (requestCode){
                case OPEN_DOCUMENT_REQUEST:
                    //Intent に選択したファイルの Uri が入っていますので、この Uri からファイルの情報を取得する
                    Uri open_file = resultData.getData();
                    Log.d("uri", String.valueOf(open_file));
                    Log.d("message", "ピッカーから写真選択成功");

                    //Uriをstringに変換
                    open_file_URI = open_file.toString();

                    //writeNewPlace(latitude,longtitude,open_file_URI);


                    BitmapFactory.Options mOptions = new BitmapFactory.Options();
                    mOptions.inSampleSize = 10;
                    InputStream is;
                    try {
                        is = getContentResolver().openInputStream(open_file);
                        // mBitmap は Bitmap 型として定義したインスタンス変数
                        mBitmap = BitmapFactory.decodeStream(is);
                        is.close();
                        // mOpenImage は Bitmap を表示させるための ImageView のインスタンス
                        mOpenImage.setImageBitmap(mBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default :
                    break;
            }
        }
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

    public void click(View view){
        //画面遷移のクラスはIntent
        //IntentはAndroid内でActivity同士やアプリ間の通信を行う
        Intent intent = new Intent(this,PhotoActivity.class);

        //startActivityでIntentの内容を発行する
        startActivity(intent);

    }




//    //ローカル ビジネスなどの端末の現在地を見つけるには、PlaceDetectionApi.getCurrentPlace()を呼び出す
//    PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mMap,null);
//
//    //PlacePickerの表示
//    int PLACE_PICKER_REQUEST = 1;
//    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//    MapsActivity(builder.build(this),PLACE_PICKER_REQUEST);
//
//    //ユーザーが選択したプレイスを取得する
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlacePicker.getPlace(data, this);
//                String toastMsg = String.format("Place: %s", place.getName());
//                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private AutocompleteFilter autocompleteFilter;
//    private LatLngBounds bounds;
//    PendingResult<AutocompletePredictionBuffer> result =
//            Places.GeoDataApi.getAutocompletePredictions(mMap, query, bounds, autocompleteFilter);
//
//
//





}
