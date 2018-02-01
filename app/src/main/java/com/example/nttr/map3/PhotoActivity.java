package com.example.nttr.map3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class PhotoActivity extends AppCompatActivity {

    //imageswicherの定義
    ImageSwitcher mImageswitcher;

    //画像の配列
    int[] mImageResources ={
            R.drawable.photo1,
            R.drawable.photo2,
    };

    //今何番目の画像を表示しているか
    int mPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        //imageswicherの取得
        mImageswitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);

        //ファクトリーの生成　setFactoryメソッドでファクトリを指定
        mImageswitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                //イメージビュー生成工場 ImageViewクラスで部品を作成する　returnで返す。
                ImageView imageView = new ImageView(PhotoActivity.this);
                return imageView;
            }
        });

        //アニメーション contextにはactivity.thisを渡す。
        //slidein,sideoutもある
        mImageswitcher.setInAnimation(PhotoActivity.this,android.R.anim.fade_in);
        mImageswitcher.setOutAnimation(PhotoActivity.this,android.R.anim.fade_out);


        //最初の画像の表示
        mImageswitcher.setImageResource(R.drawable.photo1);

        //ボタンの取得
        Button prevButton = (Button) findViewById(R.id.prevButton);
        Button nextButton = (Button) findViewById(R.id.nextButton);

        //戻るボタンを押したときは1を引く
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movePosition(-1);
            }
        });

        //進むボタンを押したときは1をたす
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movePosition(1);
            }
        });



////        //画像の取得
////        imageView = (ImageView) findViewById(R.id.imageView);
////        //画像がタップされた時
////        imageView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
//////                //viewのアニメーションメソッドで取得できる
//////                ViewPropertyAnimator animator = imageView.animate();
//////
//////                //何ミリ秒かけるか
//////                animator.setDuration(3000);
//////                //回る fをつけて小数型であることを示している。
//////                animator.rotation(360.0f * 5f);
////                  //scale(3.0f)3倍に拡大
////                  //xBy(200) yBy(200)で移動
////                  //y(200)下に200移動
////                //imageView.animate().setDuration(3000).rotation(360.0f * 5f).scaleX(3.0f).scaleY(3.0f);
////                imageView.animate().setInterpolator(new BounceInterpolator());
//
//
//            }
//        });



    }

    //進む戻るボタンが押されたときのメソッド
    private void movePosition(int move){
        mPosition = mPosition + move;
        //画像の数以上の数になってしまったときは０に戻る
        if (mPosition >= mImageResources.length){
            mPosition = 0;
        }else if(mPosition < 0){
            //0以下にならないようにする。
            mPosition = mImageResources.length - 1;
        }
        //画像の表示
        mImageswitcher.setImageResource(mImageResources[mPosition]);
    }

    public void click(View view){
        finish();
    }


}
