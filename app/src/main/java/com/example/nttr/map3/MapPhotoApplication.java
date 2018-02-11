package com.example.nttr.map3;

import android.app.Application;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by nttr on 2018/02/01.
 */

public class MapPhotoApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        //firebaseのデータ
        @IgnoreExtraProperties
        class mapphoto{

            public int latlng;
            public String uri;
            public int tag;

            //コンストラクタ

            public mapphoto(int latlng, String uri, int tag) {
                this.latlng = latlng;
                this.uri = uri;
                this.tag = tag;
            }

        }

        //Reamlの初期化
        //Realm.init(this);
    }
}
