package com.example.nttr.map3;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nttr on 2018/02/02.
 */

public class MapPhoto extends RealmObject {

    //同じidが発生しないようにする
    @PrimaryKey
    public long id; //ID

    //Realmに格納したいデータをメンバー変数として定義する
    public Date date; //日付
    public String title; //データの名前
    public String detail; //詳細


}
